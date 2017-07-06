<#include "macro-info.ftl">
<@info "college">

<h2 class="sub-head">
    <span></span> 大学查询
</h2>

<div class="container-fluid">
    <div class="row-fluid">
        <h4>
            按校名查询高校
        </h4>
        <div class="span12" id="testdiv">
            <form class="form-search">
                <input id="schoolname" name="schoolname" class="input-medium search-query" type="text" />
                <button type="submit" class="btn" onclick="Search.searchSchoolByName()">查找</button>

                <a rel="nofollow" href="${servePath}/info/search-college">
                    院校查询
                </a>

            </form>
        </div>
    </div>

    <h4>按条件查询大学<h4>
        <div class="row">
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name">批次</label>
                    <select class="form-control">
                        <option>提前批</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name">级别</label>
                    <select class="form-control">
                        <option>985</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name">学校类型</label>
                    <select class="form-control">
                        <option>综合</option>
                        <option>理工</option>
                        <option>医学</option>
                        <option>财经</option>
                        <option>军校</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name">高校所在地</label>
                    <select class="form-control">
                        <option>北京</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
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
                    <th>编号</th><th>学校名称</th><th>级别</th><th>所在省份</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td><td>西安交通大学</td><td>三本</td><td>陕西</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</div>

</@info>