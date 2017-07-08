<#macro info type>
<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>

        <#if type == "college">
        <@head title="${wealthRankLabel} - ${symphonyLabel}">
        <link rel="canonical" href="${servePath}/info/college">
        </@head>
        </#if>

        <#if type == "college-detail">
            <@head title="${wealthRankLabel} - ${symphonyLabel}">
                <link rel="canonical" href="${servePath}/info/college-detail">
            </@head>
        </#if>

        <#if type == "major">
        <@head title="${consumptionRankLabel} - ${symphonyLabel}">
            <link rel="canonical" href="${servePath}/info/major">
        </@head>
        </#if>

        <#if type == "major-detail">
            <@head title="${consumptionRankLabel} - ${symphonyLabel}">
                <link rel="canonical" href="${servePath}/info/major-detail">
            </@head>
        </#if>

        <#if type == "score">
        <@head title="${checkinTopLabel} - ${symphonyLabel}">
        <link rel="canonical" href="${servePath}/info/score">
        </@head>
        </#if>

        <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
        <link rel="stylesheet" href="${staticServePath}/css/bootstrap-css/bootstrap.min.css">

        <link rel="stylesheet" href="${staticServePath}/css/index.css?${staticResourceVersion}" />
        <link rel="stylesheet" href="${staticServePath}/css/responsive.css?${staticResourceVersion}" />
    </head>
    <body>
        <#include "../header.ftl">

    <div class="main">
        <div class="wrapper">
            <div class="content">
                <div class="module">
                    <#nested>
                </div>
            </div>
            <div class="side">
                <#include "../side.ftl">
            </div>
        </div>
    </div>

        <#include "../footer.ftl">

        <script src="${staticServePath}/js/settings${miniPostfix}.js?${staticResourceVersion}"></script>
        <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
        <script src="${staticServePath}/js/bootstrap-js/bootstrap.min.js"></script>
    </body>
</html>
</#macro>
