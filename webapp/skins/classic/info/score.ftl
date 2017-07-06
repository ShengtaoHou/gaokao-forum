<#include "macro-info.ftl">
<@info "score">

<h2 class="sub-head">
    <span></span> 分数线查询
</h2>

<div class="container-fluid">
    <div class="row">
        <h4>
            按校名查询高校
        </h4>
        <div class="span12">
            <form class="form-search">
                <input class="input-medium search-query" type="text" /> <button type="submit" class="btn">查找</button>
            </form>

        </div>
    </div>

    <br>
    <h4>批次查询<h4>
        <div class="row">
            <div class="span4">
                <div class="form-group col-lg-4">
                    <label for="name">省份</label>
                    <select class="form-control">
                        <option>北京</option>
                    </select>
                </div>
            </div>
            <div class="span4">
                <div class="form-group col-lg-4">
                    <label for="name">年份</label>
                    <select class="form-control">
                        <option>2016</option>
                    </select>
                </div>
            </div>
            <div class="span4">
                <div class="form-group col-lg-2">
                    <label for="name"></label><br>
                    <button type="submit" class="btn">查找</button>
                </div>
            </div>
        </div>

        <br>
        <h4>某高校投档线查询<h4>
            <div class="row">
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">学校所在省份</label>
                        <select class="form-control">
                            <option>北京</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">学校名称</label>
                        <select class="form-control">
                            <option>北京大学</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">考生所在省份</label>
                        <select class="form-control">
                            <option>北京</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">文理科</label>
                        <select class="form-control">
                            <option>文科</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">批次</label>
                        <select class="form-control">
                            <option>综合</option>
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

            <br>
            <h4>若干高校某年投档线查询<h4>
                <div class="row">
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">学校所在省份</label>
                            <select class="form-control">
                                <option>北京</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">年份</label>
                            <select class="form-control">
                                <option>2016</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">考生所在省份</label>
                            <select class="form-control">
                                <option>北京</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">文理科</label>
                            <select class="form-control">
                                <option>文科</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">批次</label>
                            <select class="form-control">
                                <option>综合</option>
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

                <br>
                <h4>某大学各专业分数线查询<h4>
                    <div class="row">
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name">学校所在省份</label>
                                <select class="form-control">
                                    <option>北京</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name">学校名称</label>
                                <select class="form-control">
                                    <option>北京大学</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name">考生所在省份</label>
                                <select class="form-control">
                                    <option>北京</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name">文理科</label>
                                <select class="form-control">
                                    <option>文科</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name">批次</label>
                                <select class="form-control">
                                    <option>综合</option>
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

                    <br>
                    <h4>某专业各大学分数线查询<h4>
                        <div class="row">
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name">学校所在省份</label>
                                    <select class="form-control">
                                        <option>北京</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name">专业名称名称</label>
                                    <select class="form-control">
                                        <option>计算机</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name">考生所在省份</label>
                                    <select class="form-control">
                                        <option>北京</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name">文理科</label>
                                    <select class="form-control">
                                        <option>文科</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name">年份</label>
                                    <select class="form-control">
                                        <option>2016</option>
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



                        <div class="row">
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