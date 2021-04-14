<#if boundedContexts?has_content>
    <#list boundedContexts as bc>
        * ${bc.name}<#lt>
    </#list>
</#if>