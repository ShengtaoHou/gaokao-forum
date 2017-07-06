<#include "macro-info.ftl">
<@info "searchcollege">

<h2 class="sub-head">
    <span></span> 大学查询
</h2>
<div class="list">
        <ul>
            <#list topSchool as schools>
                <li>
                ${schools.schoolName}
                </li>
            </#list>
        </ul>


</div>
</@info>