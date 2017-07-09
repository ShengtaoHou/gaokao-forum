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
package org.b3log.symphony.processor;

import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.processor.advice.*;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.ActivityQueryService;
import org.b3log.symphony.service.DataModelService;
import org.b3log.symphony.service.PointtransferQueryService;
import org.b3log.symphony.service.SchoolQueryService;
import org.b3log.symphony.service.MajorQueryService;

import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import org.b3log.latke.util.Requests;
//
import com.qiniu.util.Auth;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.Locales;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.processor.advice.PermissionGrant;
import org.b3log.symphony.service.*;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Top ranking list processor.
 * <p>
 * <ul>
 * <li>Top balance (/top/balance), GET</li>
 * <li>Top consumption (/top/consumption), GET</li>
 * <li>Top checkin (/top/checkin), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.0.2, Oct 26, 2016
 * @since 1.3.0
 */
@RequestProcessor
public class InfoProcessor {

    /**
     * Data model service.
     */
    @Inject
    private DataModelService dataModelService;

    /**
     * Pointtransfer query service.
     */
    @Inject
    private PointtransferQueryService pointtransferQueryService;

    /**
     * Activity query service.
     */
    @Inject
    private ActivityQueryService activityQueryService;

    /**
     * Activity query service.
     */
    @Inject
    private SchoolQueryService schoolQueryService;

    /**
     * Activity query service.
     */
    @Inject
    private MajorQueryService majorQueryService;

    /**
     * Activity query service.
     */
    @Inject
    private ScoreQueryService scoreQueryService;

    /**
     * Option query service.
     */
    @Inject
    private OptionQueryService optionQueryService;

    /**
     * Option management service.
     */
    @Inject
    private OptionMgmtService optionMgmtService;


    /**
     * Shows balance ranking list.
     *
     * @param context  the specified context
     * @param request  the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/info/college", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showBalance(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);

        renderer.setTemplateName("/info/college.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    @RequestProcessing(value = "/info/college", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {StopwatchStartAdvice.class, PermissionCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void updateMisc(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);
        renderer.setTemplateName("info/college.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final String value = request.getParameter(name);
            System.out.println("param "+name+" value "+value);
        }

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);//

        final String schoolname = request.getParameter("schoolname");
        final String schoolBatch = request.getParameter("schoolBatch");
        final String schoolLevel = request.getParameter("schoolLevel");
        final String schoolType = request.getParameter("schoolType");
        final String schoolProvince = request.getParameter("schoolProvince");

        if(schoolname== null ||"".equals(schoolname)){
            //schoolname为空，按条件搜索
            final List<JSONObject> schools = schoolQueryService.getSchoolByCondition(schoolBatch,schoolLevel,schoolType,schoolProvince);
            System.out.println("searching for school by condition");
            dataModel.put(Common.TOP_SCHOOL, schools);

        }else{
            //schoolname不为空，按文字搜索
            final List<JSONObject> schools = schoolQueryService.getSchoolByName(schoolname);
            System.out.println("searching for school by name: "+schoolname);
            dataModel.put(Common.TOP_SCHOOL, schools);
        }



        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);//
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }


    /**
     * Shows balance ranking list.
     *
     * @param context  the specified context
     * @param request  the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/info/college-detail", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showBalanceSearch(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final String schoolname = requestJSONObject.optString("schoolname");
        System.out.println("sc2"+schoolname);

        renderer.setTemplateName("/info/college-detail.ftl");


        final Map<String, Object> dataModel = renderer.getDataModel();

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        final List<JSONObject> schools = schoolQueryService.getSchoolByName(schoolname);
        dataModel.put(Common.TOP_SCHOOL, schools);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    /**
     * Shows consumption ranking list.
     *
     * @param context  the specified context
     * @param request  the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/info/major", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showConsumption(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        ;
        context.setRenderer(renderer);

        renderer.setTemplateName("/info/major.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    @RequestProcessing(value = "/info/major", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {StopwatchStartAdvice.class, PermissionCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void searchmajor(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);
        renderer.setTemplateName("info/major.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final String value = request.getParameter(name);
            System.out.println("param "+name+" value "+value);
        }

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);//

        final String majorName = request.getParameter("majorName");
        final String majorBtype = request.getParameter("majorBtype");
        final String majorStype = request.getParameter("majorStype");

        if(majorName== null ||"".equals(majorName)){
            //majorname为空，按条件搜索
            final List<JSONObject> majors = majorQueryService.getMajorByCondition(majorBtype,majorStype);
            System.out.println("searching for major by condition");
            dataModel.put(Common.TOP_MAJOR,majors);

        }else{
            //majorname不为空，按文字搜索
            final List<JSONObject> majors = majorQueryService.getMajorByName(majorName);
            System.out.println("searching for major by name: "+majorName);
            dataModel.put(Common.TOP_MAJOR, majors);
        }



        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);//
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }
    /**
     * Shows consumption ranking list.
     *
     * @param context  the specified context
     * @param request  the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/info/major-detail", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showConsumptionSearch(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        ;
        context.setRenderer(renderer);

        renderer.setTemplateName("/info/major-detail.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        final List<JSONObject> users = pointtransferQueryService.getTopConsumptionUsers(
                avatarViewMode, Symphonys.getInt("topConsumptionCnt"));
        dataModel.put(Common.TOP_CONSUMPTION_USERS, users);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    /**
     * Shows checkin ranking list.
     *
     * @param context  the specified context
     * @param request  the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/info/score", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void showCheckin(final HTTPRequestContext context,
                            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        ;
        context.setRenderer(renderer);

        renderer.setTemplateName("/info/score.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    @RequestProcessing(value = "/info/score", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {StopwatchStartAdvice.class, PermissionCheck.class})
    @After(adviceClass = {PermissionGrant.class, StopwatchEndAdvice.class})
    public void searchscore(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(request);
        context.setRenderer(renderer);
        renderer.setTemplateName("info/score.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final String value = request.getParameter(name);
            System.out.println("param "+name+" value "+value);
        }

        final int avatarViewMode = (int) request.getAttribute(UserExt.USER_AVATAR_VIEW_MODE);


        final String schoolProvince = request.getParameter("schoolProvince");
        final String studentProvince = request.getParameter("studentProvince");
        final String batch = request.getParameter("batch");
        final String artOrSci = request.getParameter("artOrSci");

        final String schoolName = request.getParameter("schoolName");
        final String year = request.getParameter("year");

        if(!(schoolName== null ||"".equals(schoolName))){
            //2 某高校投档线查询，因为只有这种需求学校名称非空
            final List<JSONObject> scores = scoreQueryService.getOneSchoolScore(studentProvince,artOrSci,batch,schoolName,schoolProvince);
            System.out.println("searching for one school score");
            dataModel.put(Common.TOP_SCORE,scores);
        }else if(!(artOrSci== null ||"".equals(artOrSci))){
            //若干高校投档线查询，有文理科参数
            final List<JSONObject> scores = scoreQueryService.getManySchoolScore(studentProvince,artOrSci,batch,year,schoolProvince);
            System.out.println("searching for many school score");
            dataModel.put(Common.TOP_SCORE,scores);
        }else {
            //批次线查询，没有文理科参数
            final List<JSONObject> scores = scoreQueryService.getProvinceScore(studentProvince,year);
            System.out.println("searching for province school score");
            dataModel.put(Common.BATCH_SCORE,scores);
        }

        dataModelService.fillHeaderAndFooter(request, response, dataModel);
        dataModelService.fillRandomArticles(avatarViewMode, dataModel);//
        dataModelService.fillSideHotArticles(avatarViewMode, dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

}
