<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${linkForgeLabel} - ${symphonyLabel}">
        <meta name="description" content="${symDescriptionLabel}"/>
        </@head>
        <link rel="canonical" href="${servePath}/link-forge">
    </head>
    <body>
        <#include "../header.ftl">
        <div class="main">
            <div class="wrapper">
                <div class="content fn-clear link-forge">
                    <div class="link-forge-upload form fn-clear">
                        <input type="text" placeholder="${linkForgeTipLabel}" /><button class="green fn-right">${submitLabel}</button>
                        <div id="uploadLinkTip" class="tip"></div>
                    </div>
                    <#list tags as tag>
                    <div class="module">
                        <div class="module-header">
                            <h2>
                                <a href="${servePath}/tag/${tag.tagURI}">
                                    <#if tag.tagIconPath != ''>
                                    <span class="avatar-small"  style="background-image:url('${staticServePath}/images/tags/${tag.tagIconPath}')"></span>
                                    </#if>
                                    ${tag.tagTitle}
                                </a>
                            </h2>
                            <a class="ft-gray fn-right" rel="nofollow" href="javascript:void(0)">${tag.tagLinksCnt} Links</a>
                        </div>
                        <div class="module-panel">
                            <ul class="module-list">
                                <#list tag.tagLinks as link>
                                <li>
                                    <a class="title fn-ellipsis" target="_blank" rel="nofollow" href="${link.linkAddr}">${link.linkTitle}</a>
                                </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                    </#list>
                </div>
                <div class="side">
                    <#include "../common/person-info.ftl">
                    <div class='domains-count'>
                        Tags: <b>${tagCnt}</b><br/>
                        Links: <b>${linkCnt}</b>
                    </div>
                    <#if ADLabel!="">
                    <div class="module">
                        <div class="module-header">
                            <h2>
                                ${sponsorLabel} 
                                <a href="https://hacpai.com/article/1460083956075" class="fn-right ft-13 ft-gray" target="_blank">${wantPutOnLabel}</a>
                            </h2>
                        </div>
                        <div class="module-panel ad fn-clear">
                            ${ADLabel}
                        </div>
                    </div>
                    </#if>
                </div>
            </div>
        </div>
        <#include "../footer.ftl">
        <script>
            Label.invalidUserURLLabel = "${invalidUserURLLabel}";
            Label.forgeUploadSuccLabel = "${forgeUploadSuccLabel}";
            Util.linkForge();
        </script>
    </body>
</html>
