<%@ taglib prefix="ctg" uri="customtags" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<div class="row">
    <img src="/static/img/img.jpg" class="rounded mx-auto d-block" alt="" width="241" height="125">
</div>
<div class="row">
    <ctg:hello userLogin="${login}"/>
</div>