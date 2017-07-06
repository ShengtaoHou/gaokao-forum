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
            <form class="form-search">
                <input class="input-medium search-query" type="text" /> <button type="submit" class="btn">查找</button>
            </form>
        </div>
    </div>

    <a rel="nofollow" href="${servePath}/info/search-major">
        查询
    </a>

    <div class="row-fluid">
        <h4>
            按条件查询专业
        </h4>

        <div class="span10">

                    <div class="form-group col-lg-8">
                        <label for="name">类别</label>
                        <select class="form-control">
                            <option>理工</option>
                            <option>医学</option>
                            <option>财经</option>
                        </select>
                    </div>

        </div>
        <div class="span2">
            <div class="form-group col-lg-4">
                <label for="name"></label><br>
                <button type="submit" class="btn">查找</button>
            </div>
        </div>
    </div>

    <div class="row-fluid">

        <div class="span12">
            <table class="table">
                <thead>
                <tr>
                    <th>编号</th><th>专业名称</th><th>专业类别</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td><td>计算机</td><td>搬砖</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</div>
</@info>