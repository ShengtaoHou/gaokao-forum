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
<@info "score">

<p style="color: #1B9AF7;font-size: 34px">分数线查询</p>
<hr>
<div style="height: auto;width: 777px">
    <div style="height:auto;">
        <form action="${servePath}/info/score" method="POST" class="form-search">
            <input placeholder="按校名查找" id="schoolname" name="schoolname" class="button button-border button-rounded button-primary" type="text" />
        <#--button type="submit" class="btn" >查找</button-->
            <button type="submit" class="button button-rounded button-primary"  >查找</button>
        </form>
    </div>
</div>
<hr>

<div class="container-fluid">
    <p style="font-size: 20px">批次查询</p>
        <div class="row">
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                        <option value ="">省份</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2">
                    <label for="name"></label>
                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                        <option value ="">年份</option>
                    </select>
                </div>
            </div>
            <div class="span2">
                <div class="form-group col-lg-2"style="padding-top: 5px">
                    <button type="submit" class="button button-rounded button-primary-flat">查找</button>
                </div>
            </div>
        </div>

        <br>
    <p style="font-size: 20px">某高校投档线查询</p>
            <div class="row">
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name"></label>
                        <select placeho class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                            <option value ="">学校所在省份</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">

                        <label for="name"></label>
                        <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                            <option value ="">学校名称</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name"></label>
                        <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                            <option value ="">考生所在省份</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name"></label>
                        <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                            <option value ="">文理科</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name"></label>
                        <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                            <option value ="">批次</option>
                        </select>
                    </div>
                </div>
                <div class="span2">
                    <div class="form-group col-lg-2"style="padding-top: 5px">
                        <button type="submit" class="button button-rounded button-primary-flat">查找</button>
                    </div>
                </div>
            </div>

            <br>
    <p style="font-size: 20px">若干高校某年投档线查询</p>
                <div class="row">
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label>
                            <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                <option value ="">学校所在省份</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label>
                            <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                <option value ="">年份</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label>
                            <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                <option value ="">考生所在省份</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label>
                            <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                <option value ="">文理科</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label>
                            <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                <option value ="">批次</option>
                            </select>
                        </div>
                    </div>
                    <div class="span2">
                        <div class="form-group col-lg-2"style="padding-top: 5px">
                            <button type="submit" class="button button-rounded button-primary-flat">查找</button>
                        </div>
                    </div>
                </div>

                <br>
    <p style="font-size: 20px">某大学个专业分数线查询</p>

                    <div class="row">
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name"></label>
                                <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                    <option value ="">学生所在省份</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name"></label>
                                <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                    <option value ="">学校名称</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name"></label>
                                <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                    <option value ="">考生所在省份</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name"></label>
                                <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                    <option value ="">文理科</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2">
                                <label for="name"></label>
                                <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                    <option value ="">批次</option>
                                </select>
                            </div>
                        </div>
                        <div class="span2">
                            <div class="form-group col-lg-2"style="padding-top: 5px">
                                <button type="submit" class="button button-rounded button-primary-flat">查找</button>
                            </div>
                        </div>
                    </div>

                    <br>
    <p style="font-size: 20px">某专业各大学分数线查询</p>
                        <div class="row">
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name"></label>
                                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                        <option value ="">学校所在省份</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name"></label>
                                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                        <option value ="">专业名称</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name"></label>
                                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                        <option value ="">考生所在省份</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name"></label>
                                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                        <option value ="">文理科</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2">
                                    <label for="name"></label>
                                    <select class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #1B9AF7;">
                                        <option value ="">年份</option>
                                    </select>
                                </div>
                            </div>
                            <div class="span2">
                                <div class="form-group col-lg-2"style="padding-top: 5px">
                                    <button type="submit" class="button button-rounded button-primary-flat">查找</button>
                                </div>
                            </div>
                        </div>


    <hr>
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