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
package org.b3log.symphony.processor.advice.validate;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.Lifecycle;
import org.b3log.latke.ioc.inject.Named;
import org.b3log.latke.ioc.inject.Singleton;
import org.b3log.latke.model.User;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.LangPropsServiceImpl;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.advice.BeforeRequestProcessAdvice;
import org.b3log.latke.servlet.advice.RequestProcessAdviceException;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Role;
import org.b3log.symphony.model.Tag;
import org.b3log.symphony.service.OptionQueryService;
import org.b3log.symphony.service.TagQueryService;
import org.b3log.symphony.util.StatusCodes;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Validates for article adding locally.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.4.11, Mar 9, 2017
 * @since 0.2.0
 */
@Named
@Singleton
public class ArticleAddValidation extends BeforeRequestProcessAdvice {

    /**
     * Max article title length.
     */
    public static final int MAX_ARTICLE_TITLE_LENGTH = 255;

    /**
     * Max article content length.
     */
    public static final int MAX_ARTICLE_CONTENT_LENGTH = 102400;

    /**
     * Min article content length.
     */
    public static final int MIN_ARTICLE_CONTENT_LENGTH = 4;

    /**
     * Max article reward content length.
     */
    public static final int MAX_ARTICLE_REWARD_CONTENT_LENGTH = 102400;

    /**
     * Min article reward content length.
     */
    public static final int MIN_ARTICLE_REWARD_CONTENT_LENGTH = 4;

    /**
     * Validates article fields.
     *
     * @param request           the specified HTTP servlet request
     * @param requestJSONObject the specified request object
     * @throws RequestProcessAdviceException if validate failed
     */
    public static void validateArticleFields(final HttpServletRequest request,
                                             final JSONObject requestJSONObject) throws RequestProcessAdviceException {
        final LatkeBeanManager beanManager = Lifecycle.getBeanManager();
        final LangPropsService langPropsService = beanManager.getReference(LangPropsServiceImpl.class);
        final TagQueryService tagQueryService = beanManager.getReference(TagQueryService.class);
        final OptionQueryService optionQueryService = beanManager.getReference(OptionQueryService.class);

        final JSONObject exception = new JSONObject();
        exception.put(Keys.STATUS_CODE, StatusCodes.ERR);

        String articleTitle = requestJSONObject.optString(Article.ARTICLE_TITLE);
        articleTitle = StringUtils.trim(articleTitle);
        if (Strings.isEmptyOrNull(articleTitle) || articleTitle.length() > MAX_ARTICLE_TITLE_LENGTH) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("articleTitleErrorLabel")));
        }
        if (optionQueryService.containReservedWord(articleTitle)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("contentContainReservedWordLabel")));
        }

        requestJSONObject.put(Article.ARTICLE_TITLE, articleTitle);

        final int articleType = requestJSONObject.optInt(Article.ARTICLE_TYPE);
        if (Article.isInvalidArticleType(articleType)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("articleTypeErrorLabel")));
        }

        String articleTags = requestJSONObject.optString(Article.ARTICLE_TAGS);
        articleTags = Tag.formatTags(articleTags);

        if (StringUtils.isBlank(articleTags)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("tagsEmptyErrorLabel")));
        }

        if (optionQueryService.containReservedWord(articleTags)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("contentContainReservedWordLabel")));
        }

        if (StringUtils.isNotBlank(articleTags)) {
            String[] tagTitles = articleTags.split(",");

            tagTitles = new LinkedHashSet<String>(Arrays.asList(tagTitles)).toArray(new String[0]);
            final List<String> invalidTags = tagQueryService.getInvalidTags();

            final StringBuilder tagBuilder = new StringBuilder();
            for (int i = 0; i < tagTitles.length; i++) {
                final String tagTitle = tagTitles[i].trim();

                if (Strings.isEmptyOrNull(tagTitle)) {
                    throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("tagsErrorLabel")));
                }

                if (!Tag.containsWhiteListTags(tagTitle)) {
                    if (!Tag.TAG_TITLE_PATTERN.matcher(tagTitle).matches()) {
                        throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("tagsErrorLabel")));
                    }

                    if (tagTitle.length() > Tag.MAX_TAG_TITLE_LENGTH) {
                        throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("tagsErrorLabel")));
                    }
                }

                final JSONObject currentUser = (JSONObject) request.getAttribute(User.USER);
                if (!Role.ROLE_ID_C_ADMIN.equals(currentUser.optString(User.USER_ROLE))
                        && ArrayUtils.contains(Symphonys.RESERVED_TAGS, tagTitle)) {
                    throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("articleTagReservedLabel")
                            + " [" + tagTitle + "]"));
                }

                if (invalidTags.contains(tagTitle)) {
                    throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("articleTagInvalidLabel")
                            + " [" + tagTitle + "]"));
                }

                tagBuilder.append(tagTitle).append(",");
            }
            if (tagBuilder.length() > 0) {
                tagBuilder.deleteCharAt(tagBuilder.length() - 1);
            }
            requestJSONObject.put(Article.ARTICLE_TAGS, tagBuilder.toString());
        }

        String articleContent = requestJSONObject.optString(Article.ARTICLE_CONTENT);
        articleContent = StringUtils.trim(articleContent);
        if (Strings.isEmptyOrNull(articleContent) || articleContent.length() > MAX_ARTICLE_CONTENT_LENGTH
                || articleContent.length() < MIN_ARTICLE_CONTENT_LENGTH) {
            String msg = langPropsService.get("articleContentErrorLabel");
            msg = msg.replace("{maxArticleContentLength}", String.valueOf(MAX_ARTICLE_CONTENT_LENGTH));

            throw new RequestProcessAdviceException(exception.put(Keys.MSG, msg));
        }

        if (optionQueryService.containReservedWord(articleContent)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("contentContainReservedWordLabel")));
        }

        final int rewardPoint = requestJSONObject.optInt(Article.ARTICLE_REWARD_POINT, 0);
        if (rewardPoint < 0) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("invalidRewardPointLabel")));
        }

        if (rewardPoint > 0) {
            final String articleRewardContnt = requestJSONObject.optString(Article.ARTICLE_REWARD_CONTENT);
            if (Strings.isEmptyOrNull(articleRewardContnt) || articleRewardContnt.length() > MAX_ARTICLE_CONTENT_LENGTH
                    || articleRewardContnt.length() < MIN_ARTICLE_CONTENT_LENGTH) {
                String msg = langPropsService.get("articleRewardContentErrorLabel");
                msg = msg.replace("{maxArticleRewardContentLength}", String.valueOf(MAX_ARTICLE_REWARD_CONTENT_LENGTH));

                throw new RequestProcessAdviceException(exception.put(Keys.MSG, msg));
            }
        }
    }

    @Override
    public void doAdvice(final HTTPRequestContext context, final Map<String, Object> args) throws RequestProcessAdviceException {
        final HttpServletRequest request = context.getRequest();

        JSONObject requestJSONObject;
        try {
            requestJSONObject = Requests.parseRequestJSONObject(request, context.getResponse());
            request.setAttribute(Keys.REQUEST, requestJSONObject);
        } catch (final Exception e) {
            throw new RequestProcessAdviceException(new JSONObject().put(Keys.MSG, e.getMessage()));
        }

        validateArticleFields(request, requestJSONObject);
    }
}
