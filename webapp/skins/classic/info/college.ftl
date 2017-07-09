<#include "macro-info.ftl">
<head>
    <link rel="stylesheet" href="${staticServePath}/css/buttons.css">
    <link href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="${staticServePath}/css/bootstrap-css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" href="${staticServePath}/css/bootstrap-select.min.css">
    <script src="${staticServePath}/js/bootstrap-select.min.js"></script>
    <script src="${staticServePath}/js/bootstrap-js"></script>
    <script src="${staticServePath}/js/lib/jquery/jquery-3.1.0.min.js"></script>
</head>
<@info "college">

<p style="color: #7B72E9;font-size: 34px">院校查询</p>
<hr>
<div style="height: auto;width: 777px">
    <div style="height:auto;">
        <form action="${servePath}/info/college" method="POST" class="form-search">
            <input placeholder="按校名查找" id="schoolname" name="schoolname" class="button button-border button-rounded button-royal" type="text" />
            <#--button type="submit" class="btn" >查找</button-->
            <button type="submit" class="button button-rounded button-royal">查找</button>
            </form>
    </div>
</div>
<hr>
<div>
        <div class="row">
            <form action="${servePath}/info/college" method="POST" class="form-search">
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="schoolBatch" name="schoolBatch" class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #7B72E9;">
                        <option value="">请选择层次</option>
                        <option value="本科">本科</option>
                        <option value="高职专科">高职专科</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="schoolLevel" name="schoolLevel" class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #7B72E9;">
                        <option value="">请选择级别</option>
                        <option value="985">985</option>
                        <option value="211">211</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="schoolType" name="schoolType"class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #7B72E9;">
                        <option value="">请选择类型</option>
                        <option value="综合">综合</option>
                        <option value="理工">理工</option>
                        <option value="医学">医学</option>
                        <option value="财经">财经</option>
                        <option value="军校">军校</option>
                        <option value="综合">综合</option>
                        <option value="工科">工科</option>
                        <option value="农业">农业</option>
                        <option value="师范">师范</option>
                        <option value="民族">民族</option>
                        <option value="林业">林业</option>
                        <option value="医药">医药</option>
                        <option value="语言">语言</option>
                        <option value="财经">财经</option>
                        <option value="体育">体育</option>
                        <option value="艺术">艺术</option>
                        <option value="政法">政法</option>
                        <option value="军事">军事</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="schoolProvince" name="schoolProvince" class="form-control" style="width:140px;height: 40px; line-height: 36px;border-width: 2px;border-color: #7B72E9;">
                        <option value ="">请选择省份</option>
                        <option value ="北京">北京市 </option>
                        <option value ="天津">天津市 </option>
                        <option value ="上海">上海市 </option>
                        <option value ="重庆">重庆市 </option>
                        <option value ="河北">河北省 </option>
                        <option value ="山西">山西省 </option>
                        <option value ="辽宁">辽宁省 </option>
                        <option value ="吉林">吉林省 </option>
                        <option value ="黑龙江">黑龙江省</option>
                        <option value ="江苏">江苏省 </option>
                        <option value ="浙江">浙江省 </option>
                        <option value ="安徽">安徽省 </option>
                        <option value ="福建">福建省 </option>
                        <option value ="江西">江西省 </option>
                        <option value ="山东">山东省 </option>
                        <option value ="河南">河南省 </option>
                        <option value ="湖北">湖北省 </option>
                        <option value ="湖南">湖南省 </option>
                        <option value ="广东">广东省 </option>
                        <option value ="海南">海南省 </option>
                        <option value ="四川">四川省 </option>
                        <option value ="贵州">贵州省 </option>
                        <option value ="云南">云南省 </option>
                        <option value ="陕西">陕西省 </option>
                        <option value ="甘肃">甘肃省 </option>
                        <option value ="青海">青海省 </option>
                        <option value ="台湾">台湾省 </option>
                        <option value ="广西">广西壮族自治区</option>
                        <option value ="内蒙古">内蒙古自治区</option>
                        <option value ="西藏">西藏自治区</option>
                        <option value ="宁夏">宁夏回族自治区 </option>
                        <option value ="新疆">新疆维吾尔自治区</option>
                        <option value ="香港">香港特别行政区</option>
                        <option value ="澳门">澳门特别行政区</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2"style="padding-top: 5px">
                    <button type="submit" class="button button-rounded button-royal">查找</button>
                </div>
            </div>
            </form>
        </div>
<hr>
    <div class="row-fluid">

        <div class="span12 table-responsive">
            <table class="table table-bordered table-striped"
                   data-toggle="table"
                   data-height="460"
                   data-pagination="true"
                   data-search="true">
                <thead>
                <tr>
                    <th>编号</th><th data-sortable="true">学校名称</th><th data-sortable="true">办学层次</th><th data-sortable="true">学校类型</th><th data-sortable="true">级别</th><th data-sortable="true">所在省份</th>
                </tr>
                </thead>
                <tbody>
                    <#if topSchool??>
                        <#assign x=0/>
                        <#list topSchool as schools>
                        <#assign x=x+1/>
                        <tr>
                            <td>${x}</td>
                            <td><a rel="nofollow" href="${servePath}/info/college-detail">${schools.schoolName}</a></td>
                            <td>${schools.schoolBatch}</td>
                            <td>${schools.schoolType}</td>
                            <td>${schools.schoolLevel}</td>
                            <td>${schools.schoolProvince}</td>
                        </tr>
                        </#list>

                    <#else>

                        <tr>
                            <td>1</td>
                            <td><a rel="nofollow" href="${servePath}/info/college-detail">清华大学</a></td>
                            <td>本科</td>
                            <td>综合</td>
                            <td>985</td>
                            <td>北京</td>
                        </tr>

                    </#if>

                </tbody>
            </table>
        </div>
    </div>

</div>
</@info>