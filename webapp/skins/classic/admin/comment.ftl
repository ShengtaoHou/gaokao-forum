<#include "macro-admin.ftl">
<@admin "comments">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${unmodifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <label for="oId">Id</label>
            <input type="text" id="oId" value="${comment.oId}" readonly="readonly" />

            <label for="commentAuthorId">${authorIdLabel}</label>
            <input type="text" id="commentAuthorId" name="commentAuthorId" value="${comment.commentAuthorId}" readonly="readonly" />

            <label for="commentOnArticleId">${articleLabel} Id</label>
            <input type="text" id="commentOnArticleId" name="commentOnArticleId" value="${comment.commentOnArticleId}" readonly="readonly" />

            <label for="commentSharpURL">URL</label>
            <input type="text" id="commentSharpURL" name="commentSharpURL" value="${comment.commentSharpURL}" readonly="readonly" />

            <label for="commentIP">IP</label>
            <input type="text" id="commentIP" name="commentIP" value="${comment.commentIP}" readonly="readonly" />

            <label for="commentUA">UA</label>
            <input type="text" id="commentUA" name="commentUA" value="${comment.commentUA}" readonly="readonly" />

            <label for="commentAnonymous">${anonymousLabel}</label>
            <select id="commentAnonymous" name="commentAnonymous" disabled="disabled">
                <option value="0"<#if 0 == comment.commentAnonymous> selected</#if>>${noLabel}</option>
                <option value="1"<#if 1 == comment.commentAnonymous> selected</#if>>${yesLabel}</option>
            </select>
        </div>
    </div>

    <#if permissions["commentUpdateCommentBasic"].permissionGrant>
    <div class="module">
        <div class="module-header">
            <h2>${modifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="${servePath}/admin/comment/${comment.oId}" method="POST">
                <label>${commentStatusLabel}</label>
                <select id="commentStatus" name="commentStatus">
                    <option value="0"<#if 0 == comment.commentStatus> selected</#if>>${validLabel}</option>
                    <option value="1"<#if 1 == comment.commentStatus> selected</#if>>${banLabel}</option>
                </select>

                <label for="commentContent">${commentContentLabel}</label>
                <textarea id="commentContent" name="commentContent" rows="10">${comment.commentContent}</textarea>

                <label for="commentGoodCnt">${goodCntLabel}</label>
                <input type="text" id="commentGoodCnt" name="commentGoodCnt" value="${comment.commentGoodCnt}" />

                <label for="commentBadCnt">${badCntLabel}</label>
                <input type="text" id="commentBadCnt" name="commentBadCnt" value="${comment.commentBadCnt}" />

                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
        </div>
    </div>
    </#if>

    <#if permissions["commentRemoveComment"].permissionGrant>
    <div class="module">
        <div class="module-header">
            <h2 class="ft-red">${removeDataLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="${servePath}/admin/remove-comment" method="POST" onsubmit="return window.confirm('${confirmRemoveLabel}')">
                <label for="commentId">Id</label>
                <input type="text" id="commentId" name="commentId" value="${comment.oId}" readonly="readonly"/>

                <br/><br/>
                <button type="submit" class="green fn-right" >${submitLabel}</button>
            </form>
        </div>
    </div>
    </#if>
</div>
</@admin>