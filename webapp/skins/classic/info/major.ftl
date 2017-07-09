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
                <div class="form-group col-lg-5">
                    <label for="name">学科门类</label>
                    <select id="majorBtype" name="majorBtype" class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #A5DE37;">
                        <option value=""></option>
                        <option value="哲学">哲学</option>
                        <option value="经济学">经济学</option>
                        <option value="法学">法学</option>
                        <option value="教育学">教育学</option>
                        <option value="文学">文学</option>
                        <option value="历史学">历史学</option>
                        <option value="理学">理学</option>
                        <option value="工学">工学</option>
                        <option value="农学">农学</option>
                        <option value="医学">医学</option>
                        <option value="管理学">管理学</option>
                        <option value="军事学">军事学</option>
                        <option value="艺术学">艺术学</option>
                    </select>
                </div>
            </div>

            <div class="span2">
                <div class="form-group col-lg-5">
                    <label for="name">专业类别</label>
                    <select id="majorStype" name="majorStype"class="form-control" style="width:140px;height: 40px;line-height: 36px;border-width: 2px;border-color: #A5DE37;">
                        <option value="">请选择专业类别</option>
                        <option value="哲学类">哲学类</option>
                        <option value="经济学类">经济学类</option>
                        <option value="财政学类">财政学类</option>
                        <option value="金融学类">金融学类</option>
                        <option value="经济与贸易类">经济与贸易类</option>
                        <option value="法学类">法学类</option>
                        <option value="政治学类">政治学类</option>
                        <option value="社会学类">社会学类</option>
                        <option value="民族学类">民族学类</option>
                        <option value="马克思主义理论类">马克思主义理论类</option>
                        <option value="公安学类">公安学类</option>
                        <option value="教育学类">教育学类</option>
                        <option value="体育学类">体育学类</option>
                        <option value="中国语言文学类">中国语言文学类</option>
                        <option value="外国语言文学类">外国语言文学类</option>
                        <option value="新闻传播学类">新闻传播学类</option>
                        <option value="历史学类">历史学类</option>
                        <option value="数学类">数学类</option>
                        <option value="物理学类">物理学类</option>
                        <option value="化学类">化学类</option>
                        <option value="天文学类">天文学类</option>
                        <option value="大气科学类">大气科学类</option>
                        <option value="海洋科学类">海洋科学类</option>
                        <option value="地球物理学类">地球物理学类</option>
                        <option value="地质学类">地质学类</option>
                        <option value="生物科学类">生物科学类</option>
                        <option value="心理学类">心理学类</option>
                        <option value="统计学类">统计学类</option>
                        <option value="地理科学类">地理科学类</option>
                        <option value="理论与应用力学">理论与应用力学</option>
                        <option value="机械类">机械类</option>
                        <option value="仪器类">仪器类</option>
                        <option value="材料类">材料类</option>
                        <option value="能源动力类">能源动力类</option>
                        <option value="能源与动力类">能源与动力类</option>
                        <option value="电气类">电气类</option>
                        <option value="电子信息类">电子信息类</option>
                        <option value="自动化类">自动化类</option>
                        <option value="计算机类">计算机类</option>
                        <option value="土木类">土木类</option>
                        <option value="水利类">水利类</option>
                        <option value="测绘类">测绘类</option>
                        <option value="化工与制药类">化工与制药类</option>
                        <option value="地质类">地质类</option>
                        <option value="矿业类">矿业类</option>
                        <option value="纺织类">纺织类</option>
                        <option value="轻工类">轻工类</option>
                        <option value="交通运输类">交通运输类</option>
                        <option value="海洋工程类">海洋工程类</option>
                        <option value="航空航天类">航空航天类</option>
                        <option value="兵器类">兵器类</option>
                        <option value="核工程类">核工程类</option>
                        <option value="农业工程类">农业工程类</option>
                        <option value="林业工程类">林业工程类</option>
                        <option value="环境科学与工程类">环境科学与工程类</option>
                        <option value="生物医药工程">生物医药工程</option>
                        <option value="生物医学工程类">生物医学工程类</option>
                        <option value="食品科学与工程类">食品科学与工程类</option>
                        <option value="建筑类">建筑类</option>
                        <option value="安全科学与工程类">安全科学与工程类</option>
                        <option value="生物工程类">生物工程类</option>
                        <option value="公安技术类">公安技术类</option>
                        <option value="植物生产类">植物生产类</option>
                        <option value="自然保护与环境生态类">自然保护与环境生态类</option>
                        <option value="动物生产类">动物生产类</option>
                        <option value="动物医学类">动物医学类</option>
                        <option value="林学类">林学类</option>
                        <option value="水产类">水产类</option>
                        <option value="水产学类">水产学类</option>
                        <option value="草学类">草学类</option>
                        <option value="基础医学类">基础医学类</option>
                        <option value="临床医学类">临床医学类</option>
                        <option value="口腔医学">口腔医学</option>
                        <option value="公共卫生与预防医学类">公共卫生与预防医学类</option>
                        <option value="中医学类">中医学类</option>
                        <option value="中西医结合类">中西医结合类</option>
                        <option value="药学类">药学类</option>
                        <option value="中药学类">中药学类</option>
                        <option value="法医学类">法医学类</option>
                        <option value="医学技术类">医学技术类</option>
                        <option value="护理学类">护理学类</option>
                        <option value="管理科学与工程类">管理科学与工程类</option>
                        <option value="工商管理类">工商管理类</option>
                        <option value="农林经济管理类">农林经济管理类</option>
                        <option value="公共管理类">公共管理类</option>
                        <option value="图书情报与档案管理类">图书情报与档案管理类</option>
                        <option value="物流管理与工程类">物流管理与工程类</option>
                        <option value="工业工程类">工业工程类</option>
                        <option value="电子商务类">电子商务类</option>
                        <option value="旅游管理类">旅游管理类</option>
                        <option value="艺术学理论类">艺术学理论类</option>
                        <option value="音乐与舞蹈学类">音乐与舞蹈学类</option>
                        <option value="戏剧与影视学类">戏剧与影视学类</option>
                        <option value="美术学类">美术学类</option>
                        <option value="设计学类">设计学类</option>
                    </select>
                </div>
            </div>
            <div class="span2">

                <div class="form-group col-lg-2" style="padding-top: 5px">
                    <label for="name"></label><br>
                    <button type="submit" class="btn">查找</button>
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
                        <#assign x=0/>
                        <#list topMajor as majors>
                            <#assign x=x+1/>
                        <tr>
                            <td>${x}</td>
                            <td><a rel="nofollow" href="${servePath}/info/major-detail">${majors.majorName}</a></td>
                            <td>${majors.majorBtype}</td>
                            <td>${majors.majorStype}</td>
                        </tr>
                        </#list>

                    <#else>


                    </#if>

                </tbody>
            </table>
        </div>
    </div>

</div>
</@info>