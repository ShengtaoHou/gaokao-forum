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
import org.b3log.latke.util.CollectionUtils;
import org.b3log.symphony.model.ProvinceScore;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * School repository.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 3, 2017
 * @since 1.9.0
 */
@Repository
public class ProvinceScoreRepository extends AbstractRepository {

    /**
     * Public constructor.
     */
    public ProvinceScoreRepository() {
        super(ProvinceScore.PROVINCESCORE);
    }


    public List<JSONObject> getScore(final String province,final String year) throws RepositoryException {
        System.out.println("PS repository getP");
        final List<JSONObject> ret = new ArrayList<JSONObject>();

        final Query query = new Query().setFilter(CompositeFilterOperator.and(
                new PropertyFilter(ProvinceScore.YEAR, FilterOperator.EQUAL, year),
                new PropertyFilter(ProvinceScore.PROVINCE, FilterOperator.EQUAL, province)
        ));

        final JSONObject result1 = get(query);
        final JSONArray array1 = result1.optJSONArray(Keys.RESULTS);
        final List<JSONObject> list1 = CollectionUtils.<JSONObject>jsonArrayToList(array1);
        ret.addAll(list1);
        return ret;
    }

}
