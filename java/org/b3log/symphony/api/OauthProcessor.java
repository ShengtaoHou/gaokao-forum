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

import org.b3log.latke.Keys;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.model.User;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.util.MD5;
import org.b3log.latke.util.Requests;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.service.UserMgmtService;
import org.b3log.symphony.service.UserQueryService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Oauth processor.
 *
 * <ul>
 * <li>(/oauth/token), POST</li>
 * </ul>
 *
 * @author <a href="http://wdx.me">DX</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Jun 23, 2016
 * @since 1.3.0
 */
@RequestProcessor
public class OauthProcessor {

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * User management service.
     */
    @Inject
    private UserMgmtService userMgmtService;

    /**
     * Mobile logins user.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws ServletException servlet exception
     * @throws IOException io exception
     * @throws JSONException JSONException
     */
    @RequestProcessing(value = "/oauth/token", method = HTTPRequestMethod.POST)
    public void mobileLogin(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        final String error = "invalid grant";
        final String errorDescription = "The provided authorization grant is invalid, expired, revoked, does not match";
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        try {
            JSONObject user = userQueryService.getUserByName(username);
            if (null == user) {
                user = userQueryService.getUserByEmail(username);
            }

            if (null == user || null == password) {
                ret.put("error", error);
                ret.put("error_description", errorDescription);
                return;
            }

            if (UserExt.USER_STATUS_C_INVALID == user.optInt(UserExt.USER_STATUS)
                    || UserExt.USER_STATUS_C_INVALID_LOGIN == user.optInt(UserExt.USER_STATUS)) {
                userMgmtService.updateOnlineStatus(user.optString(Keys.OBJECT_ID), "", false);
                ret.put("error", error);
                ret.put("error_description", errorDescription);
                return;
            }

            final String userPassword = user.optString(User.USER_PASSWORD);
            if (userPassword.equals(MD5.hash(password))) {
                final String ip = Requests.getRemoteAddr(request);
                userMgmtService.updateOnlineStatus(user.optString(Keys.OBJECT_ID), ip, true);
                ret.put("access_token", "{\"userPassword\":\"" + user.optString(User.USER_PASSWORD) + "\",\"userEmail\":\""
                        + user.optString(User.USER_EMAIL) + "\"}");
                ret.put("token_type", "bearer");
                ret.put("scope", "user");
                ret.put("created_at", new Date().getTime());
            }
        } catch (final ServiceException e) {
            ret.put("error", error);
            ret.put("error_description", errorDescription);
        }
    }
}
