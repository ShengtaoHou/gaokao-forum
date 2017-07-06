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
package org.b3log.symphony.api;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.model.User;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.util.Requests;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Comment;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.service.ArticleQueryService;
import org.b3log.symphony.service.CommentMgmtService;
import org.b3log.symphony.service.CommentQueryService;
import org.b3log.symphony.service.UserQueryService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Set;

/**
 * Comment processor.
 *
 * <ul>
 * <li>(/api/v1/stories/{id}/reply), POST</li>
 * </ul>
 *
 * @author <a href="http://wdx.me">DX</a>
 * @version 1.0.0.2, Jan 21, 2017
 * @since 1.3.0
 */
@RequestProcessor
public class CommentProcessor {

    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * Comment query service.
     */
    @Inject
    private CommentQueryService commentQueryService;

    /**
     * Comment management service.
     */
    @Inject
    private CommentMgmtService commentMgmtService;

    /**
     * Reply.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @param id the story's id
     * @throws ServletException servlet exception
     * @throws JSONException json ex
     * @throws IOException io ex
     * @throws ServiceException service ex
     */
    @RequestProcessing(value = "/api/v1/comments/{id}/reply", method = HTTPRequestMethod.POST)
    public void reply(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response,
            final String id) throws ServletException, JSONException, IOException, ServiceException {
        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        final JSONObject comment = commentQueryService.getCommentById(avatarViewMode, id);
        comment(context, request, response, comment.optString(Comment.COMMENT_ON_ARTICLE_ID));
    }

    /**
     * Comment.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @param id the story's id
     * @throws ServletException servlet exception
     * @throws JSONException json ex
     * @throws IOException io ex
     */
    @RequestProcessing(value = "/api/v1/stories/{id}/reply", method = HTTPRequestMethod.POST)
    public void comment(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response,
            final String id) throws ServletException, JSONException, IOException {

        final String auth = request.getHeader("Authorization");
        if (auth == null) {//TODO validate
            return;
        }

        final String email = new JSONObject(auth.substring("Bearer ".length())).optString("userEmail");
        final String httpBody = getBody(request);
        final String content = httpBody.substring("comment[body]=".length());
        final String ip = Requests.getRemoteAddr(request);
        final String ua = request.getHeader(Common.USER_AGENT);

        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        final JSONObject comment = new JSONObject();
        comment.put(Comment.COMMENT_CONTENT, content);
        comment.put(Comment.COMMENT_ON_ARTICLE_ID, id);
        comment.put(Comment.COMMENT_IP, "");
        if (StringUtils.isNotBlank(ip)) {
            comment.put(Comment.COMMENT_IP, ip);
        }
        comment.put(Comment.COMMENT_UA, "");
        if (StringUtils.isNotBlank(ua)) {
            comment.put(Comment.COMMENT_UA, ua);
        }

        try {
            final JSONObject currentUser = userQueryService.getUserByEmail(email);
            if (null == currentUser) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            final String currentUserName = currentUser.optString(User.USER_NAME);
            final JSONObject article = articleQueryService.getArticle(id);
            final String articleContent = article.optString(Article.ARTICLE_CONTENT);
            final String articleAuthorId = article.optString(Article.ARTICLE_AUTHOR_ID);
            final JSONObject articleAuthor = userQueryService.getUser(articleAuthorId);
            final String articleAuthorName = articleAuthor.optString(User.USER_NAME);

            final Set<String> userNames = userQueryService.getUserNames(articleContent);
            if (Article.ARTICLE_TYPE_C_DISCUSSION == article.optInt(Article.ARTICLE_TYPE)
                    && !articleAuthorName.equals(currentUserName)) {
                boolean invited = false;
                for (final String userName : userNames) {
                    if (userName.equals(currentUserName)) {
                        invited = true;

                        break;
                    }
                }

                if (!invited) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            comment.put(Comment.COMMENT_AUTHOR_ID, currentUser.optString(Keys.OBJECT_ID));
            comment.put(Comment.COMMENT_T_COMMENTER, currentUser);

            final String newId = commentMgmtService.addComment(comment);
            final JSONObject commentObj = new JSONObject();
            commentObj.put("id", Long.valueOf(newId)); //FIXME need the comment id.
            commentObj.put("body_html", content);
            commentObj.put("depth", 0);
            commentObj.put("user_display_name", currentUser.optString(User.USER_NAME));
            commentObj.put("user_job", currentUser.optString(UserExt.USER_INTRO));
            commentObj.put("vote_count", 0);
            commentObj.put("created_at", formatDate(new Date()));
            commentObj.put("user_portrait_url", comment.optString(Comment.COMMENT_T_ARTICLE_AUTHOR_THUMBNAIL_URL));
            ret.put("comment", commentObj);
        } catch (final ServiceException e) {
            ret.put("error", "invalid");
        }

    }

    /**
     * The demand format date.
     *
     * @param date the original date
     * @return the format date like "2015-08-03T07:26:57Z"
     */
    private String formatDate(final Object date) {
        return DateFormatUtils.format(((Date) date).getTime(), "yyyy-MM-dd")
                + "T" + DateFormatUtils.format(((Date) date).getTime(), "HH:mm:ss") + "Z";
    }

    /**
     * Get request body.
     *
     * @param request req
     * @return body
     * @throws IOException io exception
     */
    public String getBody(final HttpServletRequest request) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            final InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                final char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (final IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException ex) {
                    throw ex;
                }
            }
        }

        return stringBuilder.toString();
    }
}
