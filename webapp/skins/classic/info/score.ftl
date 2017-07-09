<#include "macro-info.ftl">
<@info "score">

<h2 class="sub-head">
    <span></span> 分数线查询
</h2>

<div class="container-fluid">

    <br>
    <h4>批次查询<h4>
        <div class="row">
            <form action="${servePath}/info/score" method="POST" class="form-search">
            <div class="span4">
                <div class="form-group col-lg-4">
                    <label for="name">省份</label>
                    <select id="studentProvince" name="studentProvince" class="form-control">
                        <option value ="">请选择学校所在省份</option>
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
            <div class="span4">
                <div class="form-group col-lg-4">
                    <label for="name">年份</label>
                    <select id="year" name="year" class="form-control">
                        <option value="">请选择年份</option>
                        <option value="2016">2016</option>
                        <option value="2015">2015</option>
                        <option value="2014">2014</option>
                        <option value="2013">2013</option>
                    </select>
                </div>
            </div>
            <div class="span4">
                <div class="form-group col-lg-2">
                    <label for="name"></label><br>
                    <button type="submit" class="btn">查找</button>
                </div>
            </div>
            </form>
        </div>

        <br>
        <h4>某高校投档线查询<h4>
            <div class="row">
                <form action="${servePath}/info/score" method="POST" class="form-search">
                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">学校所在省份</label>
                        <select id="schoolProvince" name="schoolProvince" class="form-control">
                            <option value ="">请选择学校所在省份</option>
                            <option value ="北京市">北京市 </option>
                            <option value ="天津市">天津市 </option>
                            <option value ="上海市">上海市 </option>
                            <option value ="重庆市">重庆市 </option>
                            <option value ="河北省">河北省 </option>
                            <option value ="山西省">山西省 </option>
                            <option value ="辽宁省">辽宁省 </option>
                            <option value ="吉林省">吉林省 </option>
                            <option value ="黑龙江省">黑龙江省</option>
                            <option value ="江苏省">江苏省 </option>
                            <option value ="浙江省">浙江省 </option>
                            <option value ="安徽省">安徽省 </option>
                            <option value ="福建省">福建省 </option>
                            <option value ="江西省">江西省 </option>
                            <option value ="山东省">山东省 </option>
                            <option value ="河南省">河南省 </option>
                            <option value ="湖北省">湖北省 </option>
                            <option value ="湖南省">湖南省 </option>
                            <option value ="广东省">广东省 </option>
                            <option value ="海南省">海南省 </option>
                            <option value ="四川省">四川省 </option>
                            <option value ="贵州省">贵州省 </option>
                            <option value ="云南省">云南省 </option>
                            <option value ="陕西省">陕西省 </option>
                            <option value ="甘肃省">甘肃省 </option>
                            <option value ="青海省">青海省 </option>
                            <option value ="台湾省">台湾省 </option>
                            <option value ="广西壮族自治区">广西壮族自治区</option>
                            <option value ="内蒙古自治区">内蒙古自治区</option>
                            <option value ="西藏自治区">西藏自治区</option>
                            <option value ="宁夏回族自治区">宁夏回族自治区 </option>
                            <option value ="新疆维吾尔自治区">新疆维吾尔自治区</option>
                            <option value ="香港特别行政区">香港特别行政区</option>
                            <option value ="澳门特别行政区">澳门特别行政区</option>
                        </select>
                    </div>
                </div>

                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">学校名称</label>
                        <select id="schoolName" name="schoolName" class="form-control">
                            <option >请选择大学</option>
                            <option>北京大学</option>
                        </select>
                    </div>
                </div>

                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">考生所在省份</label>
                        <select id="studentProvince" name="studentProvince" class="form-control">
                            <option value ="">请选择省份</option>
                            <option value ="北京市">北京市 </option>
                            <option value ="天津市">天津市 </option>
                            <option value ="上海市">上海市 </option>
                            <option value ="重庆市">重庆市 </option>
                            <option value ="河北省">河北省 </option>
                            <option value ="山西省">山西省 </option>
                            <option value ="辽宁省">辽宁省 </option>
                            <option value ="吉林省">吉林省 </option>
                            <option value ="黑龙江省">黑龙江省</option>
                            <option value ="江苏省">江苏省 </option>
                            <option value ="浙江省">浙江省 </option>
                            <option value ="安徽省">安徽省 </option>
                            <option value ="福建省">福建省 </option>
                            <option value ="江西省">江西省 </option>
                            <option value ="山东省">山东省 </option>
                            <option value ="河南省">河南省 </option>
                            <option value ="湖北省">湖北省 </option>
                            <option value ="湖南省">湖南省 </option>
                            <option value ="广东省">广东省 </option>
                            <option value ="海南省">海南省 </option>
                            <option value ="四川省">四川省 </option>
                            <option value ="贵州省">贵州省 </option>
                            <option value ="云南省">云南省 </option>
                            <option value ="陕西省">陕西省 </option>
                            <option value ="甘肃省">甘肃省 </option>
                            <option value ="青海省">青海省 </option>
                            <option value ="台湾省">台湾省 </option>
                            <option value ="广西壮族自治区">广西壮族自治区</option>
                            <option value ="内蒙古自治区">内蒙古自治区</option>
                            <option value ="西藏自治区">西藏自治区</option>
                            <option value ="宁夏回族自治区">宁夏回族自治区 </option>
                            <option value ="新疆维吾尔自治区">新疆维吾尔自治区</option>
                            <option value ="香港特别行政区">香港特别行政区</option>
                            <option value ="澳门特别行政区">澳门特别行政区</option>
                        </select>
                    </div>
                </div>

                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">文理科</label>
                        <select id="artOrSci" name="artOrSci"class="form-control">
                            <option value="">请选择文理科</option>
                            <option value="文科">文科</option>
                            <option value="理科">理科</option>
                        </select>
                    </div>
                </div>

                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name">批次</label>
                        <select id="batch" name="batch" class="form-control">
                            <option value="">请选择批次</option>
                            <option value="提前批">提前批</option>
                            <option value="本一批">本一批</option>
                            <option value="本二批">本二批</option>
                            <option value="本三批">本三批</option>
                        </select>
                    </div>
                </div>

                <div class="span2">
                    <div class="form-group col-lg-2">
                        <label for="name"></label><br>
                        <button type="submit" class="btn">查找</button>
                    </div>
                </div>
            </form>
            </div>
            <br>

            <h4>若干高校某年投档线查询<h4>
                <div class="row">
                <form action="${servePath}/info/score" method="POST" class="form-search">
                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">学校所在省份</label>
                            <select id="schoolProvince" name="schoolProvince" class="form-control">
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
                                <option value ="山西">山西省</option>
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
                        <div class="form-group col-lg-2">
                            <label for="name">年份</label>
                            <select id="year" name="year" class="form-control">
                                <option value="">请选择年份</option>
                                <option value="2016">2016</option>
                                <option value="2015">2015</option>
                                <option value="2014">2014</option>
                                <option value="2013">2013</option>
                            </select>
                        </div>
                    </div>

                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">考生所在省份</label>
                            <select id="studentProvince" name="studentProvince" class="form-control">
                                <option value ="">请选择省份</option>
                                <option value ="北京">北京市</option>
                                <option value ="天津">天津市</option>
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
                                <option value ="山西">山西省</option>
                                <option value ="甘肃">甘肃省 </option>
                                <option value ="青海">青海省 </option>
                                <option value ="台湾">台湾省 </option>
                                <option value ="广西">广西壮族自治区</option>
                                <option value ="内蒙古">内蒙古自治区</option>
                                <option value ="西藏">西藏自治区</option>
                                <option value ="宁夏回族">宁夏回族自治区 </option>
                                <option value ="新疆维吾尔">新疆维吾尔自治区</option>
                                <option value ="香港">香港特别行政区</option>
                                <option value ="澳门">澳门特别行政区</option>
                            </select>
                        </div>
                    </div>

                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">文理科</label>
                            <select id="artOrSci" name="artOrSci"class="form-control">
                                <option value="">请选择文理科</option>
                                <option value="文史">文科</option>
                                <option value="理工">理科</option>
                            </select>
                        </div>
                    </div>

                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name">批次</label>
                            <select id="batch" name="batch" class="form-control">
                                <option value="">请选择批次</option>
                                <option value="提前批">提前批</option>
                                <option value="一本">本一批</option>
                                <option value="二本">本二批</option>
                                <option value="三本">本三批</option>
                            </select>
                        </div>
                    </div>

                    <div class="span2">
                        <div class="form-group col-lg-2">
                            <label for="name"></label><br>
                            <button type="submit" class="btn">查找</button>
                        </div>
                    </div>

</form>
</div>



        <div class="row">

            <div class="span12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th><th>学校名称</th><th>年份</th><th>投档线</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#if topScore??>
                            <#assign x=0/>
                            <#list topScore as tscores>
                                <#assign x=x+1/>
                            <tr>
                                <td>${x}</td>
                                <td>${tscores.schoolName}</td>
                                <td>${tscores.year}</td>
                                <td>${tscores.scoreMin}</td>
                            </tr>
                            </#list>

                        <#else>

                        </#if>

                    </tbody>
                </table>
            </div>
        </div>
                <div class="row">

                    <div class="span12">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>序号</th><th>文科一本线</th><th>文科二本线</th><th>文科三本线</th><th>理科一本线</th><th>理科二本线</th><th>理科三本线</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#if batchScore??>
                                    <#assign x=0 />
                                    <#list batchScore as scores>
                                        <#assign x=x+1/>
                                    <tr>
                                        <td>${x}</td>
                                        <td>${scores.a1}</td>
                                        <td>${scores.a2}</td>
                                        <td>${scores.a3}</td>
                                        <td>${scores.s1}</td>
                                        <td>${scores.s2}</td>
                                        <td>${scores.s3}</td>
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