<#include "macro-info.ftl">
<@info "major">
<h2 class="sub-head">
    专业查询
</h2>

<div class="container-fluid">
    <div class="row-fluid">
        <h4>
            按名称查询专业
        </h4>
        <div class="span12">
            <form action="${servePath}/info/major" method="POST" class="form-search">
                <input id="majorName" name="majorName" class="input-medium search-query" type="text" />
                <button type="submit" class="btn">查找</button>
            </form>
        </div>
    </div>


    <div class="row-fluid">
        <h4>
            按条件查询专业
        </h4>
        <form action="${servePath}/info/major" method="POST" class="form-search">
            <div class="span5">
                <div class="form-group col-lg-8">
                    <label for="name">学科门类</label>
                    <select id="majorBtype" name="majorBtype" class="form-control">
                        <option value=""></option>
                        <option value="经济学">经济学</option>
                    </select>
                </div>
            </div>
            <div class="span5">
                <div class="form-group col-lg-8">
                    <label for="name">专业类别</label>
                    <select id="majorStype" name="majorStype"class="form-control">
                        <option value=""></option>
                        <option value="经济学类">经济学类</option>
                        <option value="财政学类">财政学类</option>
                        <option value="金融学类">金融学类</option>
                        <option value="经济与贸易类">经济与贸易类</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-4">
                    <label for="name"></label><br>
                    <button type="submit" class="btn">查找</button>
                </div>
            </div>
        </form>
    </div>

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