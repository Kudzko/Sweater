<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    <div>
        <@l.logout />
    </div>
    <div>
        <form method="post" action="/main">
            <input type="text" name="text" placeholder="Enter message/ Введите сообщение"/>
            <input type="text" name="tag" placeholder="Tag/ Тэг"/>
            <button type="submit"> Добавить</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
        </form>
    </div>

    <div>List of messages/ Список сообщений</div>

    <form method="post" action="filter">
        <input type="text" name="filter">
        <button type="submit">Найти</button>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>

    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else >
        No messages
    </#list>
</@c.page>
