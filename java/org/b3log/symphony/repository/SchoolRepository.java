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
package org.b3log.symphony.repository;

import org.b3log.latke.Keys;
import org.b3log.latke.repository.*;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.symphony.model.School;
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
/**
 * School repository.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 3, 2017
 * @since 1.9.0
 */
@Repository
public class SchoolRepository extends AbstractRepository {

    /**
     * Public constructor.
     */
    public SchoolRepository() {
        super(School.SCHOOL);
    }

    /**
     * Gets a School with the specified ISBN.
     *
     * @param isbn the specified ISBN
     * @return a School, returns {@code null} if not found
     * @throws RepositoryException reposiory exception
     */
    public List<JSONObject> getByName(final String name) throws RepositoryException {

        final List<JSONObject> ret = new ArrayList<JSONObject>();

        Query query = new Query().setFilter(new PropertyFilter(School.SCHOOL_NAME, FilterOperator.EQUAL, name))
                .setPageCount(1);;

        final JSONObject result1 = get(query);


        final JSONArray array1 = result1.optJSONArray(Keys.RESULTS);

        final List<JSONObject> list1 = CollectionUtils.<JSONObject>jsonArrayToList(array1);

        ret.addAll(list1);

        return ret;

    }
}
