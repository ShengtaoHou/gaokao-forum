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
import org.b3log.symphony.model.SchoolScore;
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
public class SchoolScoreRepository extends AbstractRepository {

    /**
     * Public constructor.
     */
    public SchoolScoreRepository() {
        super(SchoolScore.SCHOOLSCORE);
    }


    public List<JSONObject> getOneSchool(final String stutentProvince,final String artOrSci,final String batch,final String schoolName,final String schoolProvince) throws RepositoryException {

        final List<JSONObject> ret = new ArrayList<JSONObject>();

        final List<Filter> filters = new ArrayList<Filter>();

        int filcount=0;

        if(!(stutentProvince== null ||"".equals(stutentProvince))){
            filters.add(new PropertyFilter(SchoolScore.STUDENT_PROVINCE, FilterOperator.EQUAL, stutentProvince));
            filcount++;
        }
        if(!(artOrSci==null ||"".equals(artOrSci))){
            filters.add(new PropertyFilter(SchoolScore.ARTORSCI, FilterOperator.EQUAL, artOrSci));
            filcount++;
        }
        if(!(batch== null ||"".equals(batch))){
            filters.add(new PropertyFilter(SchoolScore.BATCH, FilterOperator.EQUAL, batch));
            filcount++;
        }
        if(!(schoolName== null ||"".equals(schoolName))){
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_NAME, FilterOperator.EQUAL, schoolName));
            filcount++;
        }
        if(!(schoolProvince== null ||"".equals(schoolProvince))){
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, schoolProvince));
            filcount++;
        }


        final Query query;
        if(filcount>=2){
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        }else if(filcount==1){
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, "打酱油的字符串"));
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.OR, filters));
        }else{
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, "打酱油的字符串"));
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, "打酱油的字符串"));
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.OR, filters));
        }

        final JSONObject result1 = get(query);
        final JSONArray array1 = result1.optJSONArray(Keys.RESULTS);
        final List<JSONObject> list1 = CollectionUtils.<JSONObject>jsonArrayToList(array1);
        ret.addAll(list1);
        return ret;
    }

    public List<JSONObject> getManySchool(final String stutentProvince,final String artOrSci,final String batch,final String year,final String schoolProvince) throws RepositoryException {

        final List<JSONObject> ret = new ArrayList<JSONObject>();

        final List<Filter> filters = new ArrayList<Filter>();

        int filcount=0;

        if(!(stutentProvince== null ||"".equals(stutentProvince))){
            filters.add(new PropertyFilter(SchoolScore.STUDENT_PROVINCE, FilterOperator.EQUAL, stutentProvince));
            filcount++;
        }
        if(!(artOrSci==null ||"".equals(artOrSci))){
            filters.add(new PropertyFilter(SchoolScore.ARTORSCI, FilterOperator.EQUAL, artOrSci));
            filcount++;
        }
        if(!(batch== null ||"".equals(batch))){
            filters.add(new PropertyFilter(SchoolScore.BATCH, FilterOperator.EQUAL, batch));
            filcount++;
        }
        if(!(year== null ||"".equals(year))){
            filters.add(new PropertyFilter(SchoolScore.YEAR, FilterOperator.EQUAL, year));
            filcount++;
        }
        if(!(schoolProvince== null ||"".equals(schoolProvince))){
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, schoolProvince));
            filcount++;
        }

        final Query query;
        if(filcount>=2){
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        }else if(filcount==1){
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, schoolProvince));
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.OR, filters));
        }else{
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, schoolProvince));
            filters.add(new PropertyFilter(SchoolScore.SCHOOL_PROVINCE, FilterOperator.EQUAL, schoolProvince));
            query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.OR, filters));
        }

        final JSONObject result1 = get(query);
        final JSONArray array1 = result1.optJSONArray(Keys.RESULTS);
        final List<JSONObject> list1 = CollectionUtils.<JSONObject>jsonArrayToList(array1);
        ret.addAll(list1);
        return ret;
    }
}
