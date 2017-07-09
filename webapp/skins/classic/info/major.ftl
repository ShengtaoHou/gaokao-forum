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
<@info "major">
<p style="color: #A5DE37;font-size: 34px">专业查询</p>
<hr>
<div style="height: auto;width: 777px">
    <div style="height:auto;">
        <form action="${servePath}/info/major" method="POST" class="form-search">
            <input placeholder="按名称查询专业" id="majorName" name="majorName" class="button button-border button-rounded button-action" type="text" />
        <#--button type="submit" class="btn" >查找</button-->
            <button type="submit" class="button button-rounded button-action">查找</button>
        </form>
    </div>
</div>
<hr>
<div>
    <div class="row">
        <form action="${servePath}/info/major" method="POST" class="form-search">
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="majorBtype" name="majorBtype" class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #A5DE37;">
                        <option value="">学科门类</option>
                        <option value="经济学">经济学</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select id="majorStype" name="majorStype"class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #A5DE37;">
                        <option value="">专业类别</option>
                        <option value="经济学类">经济学类</option>
                        <option value="财政学类">财政学类</option>
                        <option value="金融学类">金融学类</option>
                        <option value="经济与贸易类">经济与贸易类</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2"style="padding-top: 5px">
                    <button type="submit" class="button button-rounded button-action">查找</button>
                </div>
            </div>
        </form>
    </div>
    <hr>
    <div class="row-fluid">

        <div class="span12">
            <table class="table">
                <thead>
                <tr>
                    <th>编号</th><th>专业名称</th><th>学科类别</th><th>专业类别</th>
                </tr>
                </thead>
                <tbody>
                    <#if topMajor??>

                        <#list topMajor as majors>
                        <tr>
                            <td>1</td>
                            <td><a rel="nofollow" href="${servePath}/info/major-detail">${majors.majorName}</a></td>
                            <td>${majors.majorBtype}</td>
                            <td>${majors.majorStype}</td>
                        </tr>
                        </#list>

                    <#else>

                    <tr>
                        <td>1</td>
                        <td><a rel="nofollow" href="${servePath}/info/college-detail">111</a></td>
                        <td>11</td>
                        <td>1</td>
                    </tr>

                    </#if>

                </tbody>
            </table>
        </div>
    </div>

</div>
</@info>