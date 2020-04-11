<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    Login page
    ${message?ifExists}
    <@l.login "/login" true/>
</@c.page>
