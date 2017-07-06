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
package org.b3log.symphony.service;

import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.processor.channel.TimelineChannel;
import org.b3log.symphony.repository.UserRepository;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Timeline management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Jul 22, 2016
 * @since 1.3.0
 */
@Service
public class TimelineMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TimelineMgmtService.class.getName());

    /**
     * Timelines.
     */
    private LinkedList<JSONObject> timelines = new LinkedList<JSONObject>();

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Adds the specified timeline.
     *
     * @param timeline the specified timeline, for example,      <pre>
     * {
     *     "userId": "",
     *     "type": "article",
     *     "content": timelineArticleAddLabel
     * }
     * </pre>
     */
    public void addTimeline(final JSONObject timeline) {
        String userId = timeline.optString(Common.USER_ID);
        try {
            final JSONObject user = userRepository.get(userId);

            if (UserExt.USER_XXX_STATUS_C_PUBLIC != user.optInt(UserExt.USER_TIMELINE_STATUS)) {
                return;
            }
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets user [userId=" + userId + "] failed", e);
        }

        TimelineChannel.notifyTimeline(timeline);

        timelines.addFirst(timeline);

        final int maxCnt = Symphonys.getInt("timelineCnt");

        if (timelines.size() > maxCnt) {
            timelines.remove(maxCnt);
        }
    }

    /**
     * Gets timelines.
     *
     * @return timelines
     */
    public List<JSONObject> getTimelines() {
        return timelines;
    }
}
