<#include "../macro-head.ftl">
<#include "../macro-list.ftl">
<#include "../macro-pagination.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="SymHub - ${symphonyLabel}">
        <meta name="description" content="${symDescriptionLabel}"/>
        </@head>
    </head>
    <body>
        <#include "../header.ftl">
        <div class="main">
            <div class="content">
                <div class="fn-hr10"></div>
                <div class="list">
                    <ul>
                        <#list syms as sym>
                            <li>
                                <a rel="nofollow" href="${sym.symURL}" target="_blank">
                                    <span class="avatar-small slogan" style="background-image:url('${sym.symIcon}')"></span>
                                </a>
                                <a rel="friend" class="title"  target="_blank" href="${sym.symURL}">${sym.symTitle} -
                                    <span class="ft-gray">${sym.symDesc}</span>
                                </a>
                            </li>
                        </#list>
                    </ul>
                </div>
                <div class="fn-hr10"></div>
            </div>
            <div class="wrapper">
                <div class="side">
                    <#include "../side.ftl">
                </div>
            </div>
        </div>
        <#include "../footer.ftl">
        <@listScript/>
    </body>
</html>
