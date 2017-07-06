<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${gobangLabel} - ${activityLabel} - ${symphonyLabel}">
        </@head>
        <link rel="stylesheet" href="${staticServePath}/css/index.css?${staticResourceVersion}" />
        <link rel="canonical" href="${servePath}/activity/gobang">
    </head>
    <body onbeforeunload="closing();">
        <#include "../header.ftl">
        <div class="main">
            <div class="wrapper">
                <div class="content">
                    <div class="module">
                        <h2 class="sub-head">
                            <div class="avatar-small tooltipped tooltipped-ne"
                                 aria-label="${gobangLabel}" style="background-image:url('${staticServePath}/images/activities/gobang.png')"></div>
                            ${gobangLabel}
                            <span class="ft-13 ft-gray">${activityGobangTitleLabel}</span>
                        </h2>
                        <div class="fn-content">
                            <canvas id="gobangCanvas" height="600px" width="600px"></canvas>
                        </div>
                    </div>
                </div>
                <div class="side">
                    <div class="module">
                        <div class="module-header fn-clear">
                            <button class="red fn-right fn-none" onclick="Gobang.requestDraw()">${activityRequestDrawLabel}</button>
                            <button class="fn-right green" onclick="Gobang.initGobang('${wsScheme}://${serverHost}:${serverPort}${contextPath}')">${gameStartLabel}</button>
                        </div>
                        <div class="module-panel list">
                            <div class="form fn-content">
                                <input type="text" placeholder="回车发送消息" id="chatInput" class="fn-none"/>
                            </div>
                            <ul>
                                <li class="ft-center">Welcome</li>
                            </ul>
                            <br/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <#include "../footer.ftl">
        <script src="${staticServePath}/js/activity${miniPostfix}.js?${staticResourceVersion}"></script>
        <script src="${staticServePath}/js/gobang${miniPostfix}.js?${staticResourceVersion}"></script>
        <script>
            Label.activityStartGobangTipLabel = '${activityStartGobangTipLabel}';
            Label.activityAskForDrawLabel='${activityAskForDrawLabel}';
            Gobang.initCanvas('oMark', 'gobangCanvas');
            document.getElementById("gobangCanvas").addEventListener("click",Gobang.moveChess, false);
        </script>
    </body>
</html>