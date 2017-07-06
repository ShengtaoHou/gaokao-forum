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

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.repository.*;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.urlfetch.*;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Paginator;
import org.b3log.symphony.model.School;
import org.b3log.symphony.repository.SchoolRepository;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Book query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.2, Jan 16, 2017
 * @since 1.9.0
 */
@Service
public class SchoolQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AvatarQueryService.class);

    /**
     * Book repository.
     */
    @Inject
    private SchoolRepository schoolRepository;



    /**
     * Gets a book's information with the specified ISBN.
     *
     * @param isbn the specified ISBN
     * @return book info, returns {@code null} if not found
     */
    @Transactional
    public List<JSONObject> getSchoolByName(final String name) {
        System.out.println("dddd");
        try {
            return schoolRepository.getByName(name);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets school by name [" + name + "] failed", e);
            return null;
        }
    }
}
