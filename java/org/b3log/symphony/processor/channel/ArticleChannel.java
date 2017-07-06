/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2017,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.processor.channel;

import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.LatkeBeanManagerImpl;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.jdbc.JdbcRepository;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.LangPropsServiceImpl;
import org.b3log.latke.util.Locales;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.*;
import org.b3log.symphony.processor.SkinRenderer;
import org.b3log.symphony.repository.ArticleRepository;
import org.b3log.symphony.service.RoleQueryService;
import org.b3log.symphony.service.TimelineMgmtService;
import org.b3log.symphony.service.UserQueryService;
import org.b3log.symphony.util.Emotions;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Article channel.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 2.3.9.6, Dec 17, 2016
 * @since 1.3.0
 */
@ServerEndpoint(value = "/article-channel", configurator = Channels.WebSocketConfigurator.class)
public class ArticleChannel {

    /**
     * Session set.
     */
    public static final Set<Session> SESSIONS = Collections.newSetFromMap(new ConcurrentHashMap());
    /**
     * Article viewing map &lt;articleId, count&gt;.
     */
    public static final Map<String, Integer> ARTICLE_VIEWS
            = Collections.synchronizedMap(new HashMap<String, Integer>());
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ArticleChannel.class.getName());

    /**
     * Notifies the specified article heat message to browsers.
     *
     * @param message the specified message, for example,
     *                "articleId": "",
     *                "operation": "" // "+"/"-"
     */
    public static void notifyHeat(final JSONObject message) {
        message.put(Common.TYPE, Article.ARTICLE_T_HEAT);

        final String msgStr = message.toString();

        for (final Session session : SESSIONS) {
            final String viewingArticleId = (String) Channels.getHttpParameter(session, Article.ARTICLE_T_ID);
            if (Strings.isEmptyOrNull(viewingArticleId)
                    || !viewingArticleId.equals(message.optString(Article.ARTICLE_T_ID))) {
                continue;
            }

            if (session.isOpen()) {
                session.getAsyncRemote().sendText(msgStr);
            }
        }
    }

    /**
     * Notifies the specified comment message to browsers.
     *
     * @param message the specified message
     */
    public static void notifyComment(final JSONObject message) {
        message.put(Common.TYPE, Comment.COMMENT);

        final LatkeBeanManager beanManager = LatkeBeanManagerImpl.getInstance();
        final UserQueryService userQueryService = beanManager.getReference(UserQueryService.class);
        final ArticleRepository articleRepository = beanManager.getReference(ArticleRepository.class);
        final RoleQueryService roleQueryService = beanManager.getReference(RoleQueryService.class);
        final LangPropsService langPropsService = beanManager.getReference(LangPropsServiceImpl.class);

        for (final Session session : SESSIONS) {
            final String viewingArticleId = (String) Channels.getHttpParameter(session, Article.ARTICLE_T_ID);
            if (Strings.isEmptyOrNull(viewingArticleId)
                    || !viewingArticleId.equals(message.optString(Article.ARTICLE_T_ID))) {
                continue;
            }

            final int articleType = Integer.valueOf(Channels.getHttpParameter(session, Article.ARTICLE_TYPE));
            final JSONObject user = (JSONObject) Channels.getHttpSessionAttribute(session, User.USER);
            final boolean isLoggedIn = null != user;

            try {
                if (Article.ARTICLE_TYPE_C_DISCUSSION == articleType) {
                    if (!isLoggedIn) {
                        continue;
                    }

                    final String userName = user.optString(User.USER_NAME);
                    final String userRole = user.optString(User.USER_ROLE);

                    final JSONObject article = articleRepository.get(viewingArticleId);
                    if (null == article) {
                        continue;
                    }

                    final String articleAuthorId = article.optString(Article.ARTICLE_AUTHOR_ID);
                    final String userId = user.optString(Keys.OBJECT_ID);
                    if (!userId.equals(articleAuthorId)) {
                        final String articleContent = article.optString(Article.ARTICLE_CONTENT);
                        final Set<String> userNames = userQueryService.getUserNames(articleContent);

                        boolean invited = false;
                        for (final String inviteUserName : userNames) {
                            if (inviteUserName.equals(userName)) {
                                invited = true;

                                break;
                            }
                        }

                        if (Role.ROLE_ID_C_ADMIN.equals(userRole)) {
                            invited = true;
                        }

                        if (!invited) {
                            continue; // next session
                        }
                    }
                }

                message.put(Comment.COMMENT_T_NICE, false);
                message.put(Common.REWARED_COUNT, 0);
                message.put(Comment.COMMENT_T_VOTE, -1);
                message.put(Common.REWARDED, false);
                message.put(Comment.COMMENT_REVISION_COUNT, 1);

                final Map dataModel = new HashMap();
                dataModel.put(Common.IS_LOGGED_IN, isLoggedIn);
                dataModel.put(Common.CURRENT_USER, user);
                dataModel.put(Common.CSRF_TOKEN, Channels.getHttpSessionAttribute(session, Common.CSRF_TOKEN));
                Keys.fillServer(dataModel);
                dataModel.put(Comment.COMMENT, message);

                String templateDirName = Symphonys.get("skinDirName");
                if (isLoggedIn) {
                    dataModel.putAll(langPropsService.getAll(Locales.getLocale(user.optString(UserExt.USER_LANGUAGE))));
                    final String userId = user.optString(Keys.OBJECT_ID);
                    final Map<String, JSONObject> permissions
                            = roleQueryService.getUserPermissionsGrantMap(userId);
                    dataModel.put(Permission.PERMISSIONS, permissions);

                    templateDirName = user.optString(UserExt.USER_SKIN);
                } else {
                    dataModel.putAll(langPropsService.getAll(Locales.getLocale()));
                    final Map<String, JSONObject> permissions
                            = roleQueryService.getPermissionsGrantMap(Role.ROLE_ID_C_VISITOR);
                    dataModel.put(Permission.PERMISSIONS, permissions);
                }

                final Template template = SkinRenderer.getTemplate(templateDirName, "common/comment.ftl",
                        false, user);
                final StringWriter stringWriter = new StringWriter();
                template.process(dataModel, stringWriter);
                stringWriter.close();

                message.put("cmtTpl", stringWriter.toString());
                final String msgStr = message.toString();
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(msgStr);
                }
            } catch (final Exception e) {
                LOGGER.log(Level.ERROR, "Notify comment error", e);
            } finally {
                JdbcRepository.dispose();
            }
        }
    }

    /**
     * Called when the socket connection with the browser is established.
     *
     * @param session session
     */
    @OnOpen
    public void onConnect(final Session session) {
        final String articleId = (String) Channels.getHttpParameter(session, Article.ARTICLE_T_ID);
        if (StringUtils.isBlank(articleId)) {
            return;
        }

        SESSIONS.add(session);

        synchronized (ARTICLE_VIEWS) {
            if (!ARTICLE_VIEWS.containsKey(articleId)) {
                ARTICLE_VIEWS.put(articleId, 1);
            } else {
                final int count = ARTICLE_VIEWS.get(articleId);
                ARTICLE_VIEWS.put(articleId, count + 1);
            }
        }

        final JSONObject message = new JSONObject();
        message.put(Article.ARTICLE_T_ID, articleId);
        message.put(Common.OPERATION, "+");

        ArticleListChannel.notifyHeat(message);
        notifyHeat(message);

        final JSONObject user = (JSONObject) Channels.getHttpSessionAttribute(session, User.USER);
        if (null == user) {
            return;
        }

        final String userName = user.optString(User.USER_NAME);

        // Timeline
        final LatkeBeanManager beanManager = LatkeBeanManagerImpl.getInstance();
        final ArticleRepository articleRepository = beanManager.getReference(ArticleRepository.class);
        final LangPropsService langPropsService = beanManager.getReference(LangPropsServiceImpl.class);
        final TimelineMgmtService timelineMgmtService = beanManager.getReference(TimelineMgmtService.class);

        try {
            final JSONObject article = articleRepository.get(articleId);
            if (null == article) {
                return;
            }

            String articleTitle = Jsoup.parse(article.optString(Article.ARTICLE_TITLE)).text();
            articleTitle = Emotions.convert(articleTitle);

            final String articlePermalink = Latkes.getServePath() + article.optString(Article.ARTICLE_PERMALINK);

            final JSONObject timeline = new JSONObject();
            timeline.put(Common.USER_ID, user.optString(Keys.OBJECT_ID));
            timeline.put(Common.TYPE, Article.ARTICLE);
            String content = langPropsService.get("timelineInArticleLabel");
            content = content.replace("{user}", "<a target='_blank' rel='nofollow' href='" + Latkes.getServePath()
                    + "/member/" + userName + "'>" + userName + "</a>")
                    .replace("{article}", "<a target='_blank' rel='nofollow' href='" + articlePermalink
                            + "'>" + articleTitle + "</a>");
            timeline.put(Common.CONTENT, content);

            timelineMgmtService.addTimeline(timeline);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Timeline error", e);
        } finally {
            JdbcRepository.dispose();
        }
    }

    /**
     * Called when the connection closed.
     *
     * @param session     session
     * @param closeReason close reason
     */
    @OnClose
    public void onClose(final Session session, final CloseReason closeReason) {
        removeSession(session);
    }

    /**
     * Called when a message received from the browser.
     *
     * @param message message
     */
    @OnMessage
    public void onMessage(final String message) {
    }

    /**
     * Called in case of an error.
     *
     * @param session session
     * @param error   error
     */
    @OnError
    public void onError(final Session session, final Throwable error) {
        removeSession(session);
    }

    /**
     * Removes the specified session.
     *
     * @param session the specified session
     */
    private void removeSession(final Session session) {
        SESSIONS.remove(session);

        final String articleId = (String) Channels.getHttpParameter(session, Article.ARTICLE_T_ID);
        if (StringUtils.isBlank(articleId)) {
            return;
        }

        synchronized (ARTICLE_VIEWS) {
            if (!ARTICLE_VIEWS.containsKey(articleId)) {
                return;
            }

            final int count = ARTICLE_VIEWS.get(articleId);
            final int newCount = count - 1;
            if (newCount < 1) {
                ARTICLE_VIEWS.remove(articleId);
            } else {
                ARTICLE_VIEWS.put(articleId, newCount);
            }
        }

        final JSONObject message = new JSONObject();
        message.put(Article.ARTICLE_T_ID, articleId);
        message.put(Common.OPERATION, "-");

        ArticleListChannel.notifyHeat(message);
        notifyHeat(message);

        final JSONObject user = (JSONObject) Channels.getHttpSessionAttribute(session, User.USER);
        if (null == user) {
            return;
        }

        final String userName = user.optString(User.USER_NAME);

        // Timeline
        final LatkeBeanManager beanManager = LatkeBeanManagerImpl.getInstance();
        final ArticleRepository articleRepository = beanManager.getReference(ArticleRepository.class);
        final LangPropsService langPropsService = beanManager.getReference(LangPropsServiceImpl.class);
        final TimelineMgmtService timelineMgmtService = beanManager.getReference(TimelineMgmtService.class);

        try {
            final JSONObject article = articleRepository.get(articleId);
            if (null == article) {
                return;
            }

            String articleTitle = Jsoup.parse(article.optString(Article.ARTICLE_TITLE)).text();
            articleTitle = Emotions.convert(articleTitle);
            final String articlePermalink = Latkes.getServePath() + article.optString(Article.ARTICLE_PERMALINK);

            final JSONObject timeline = new JSONObject();
            timeline.put(Common.USER_ID, user.optString(Keys.OBJECT_ID));
            timeline.put(Common.TYPE, Article.ARTICLE);
            String content = langPropsService.get("timelineOutArticleLabel");
            content = content.replace("{user}", "<a target='_blank' rel='nofollow' href='" + Latkes.getServePath()
                    + "/member/" + userName + "'>" + userName + "</a>")
                    .replace("{article}", "<a target='_blank' rel='nofollow' href='" + articlePermalink
                            + "'>" + articleTitle + "</a>");
            timeline.put(Common.CONTENT, content);

            timelineMgmtService.addTimeline(timeline);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Timeline error", e);
        } finally {
            JdbcRepository.dispose();
        }
    }
}
