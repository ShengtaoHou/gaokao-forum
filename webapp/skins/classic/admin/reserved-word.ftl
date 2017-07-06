<#include "macro-admin.ftl">
<@admin "reservedWords">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${unmodifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <label for="oId">Id</label>
            <input type="text" id="oId" value="${word.oId}" readonly="readonly" />
        </div>
    </div>

    <#if permissions["rwUpdateReservedWordBasic"].permissionGrant>
    <div class="module">
        <div class="module-header">
            <h2>${modifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="${servePath}/admin/reserved-word/${word.oId}" method="POST">
                <label for="optionValue">${reservedWordLabel}</label>
                <input type="text" id="optionValue" name="optionValue" value="${word.optionValue}" />

                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
        </div>
    </div>
    </#if>

    <#if permissions["rwRemoveReservedWord"].permissionGrant>
    <div class="module">
        <div class="module-header">
            <h2 class="ft-red">${removeDataLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="${servePath}/admin/remove-reserved-word" method="POST" onsubmit="return window.confirm('${confirmRemoveLabel}')">
                <label for="id">Id</label>
                <input type="text" id="id" name="id" value="${word.oId}" readonly="readonly"/>

                <br/><br/>
                <button type="submit" class="green fn-right" >${submitLabel}</button>
            </form>
        </div>
    </div>
    </#if>
</div>
</@admin>