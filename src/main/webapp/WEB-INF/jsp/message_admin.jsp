<%@ taglib prefix="ctg" uri="customtags" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<fmt:setBundle basename="resources"/>

<html>
<c:set var="title" value="Панель администратора" scope="page"/>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Reports</title>
</head>
<body class="text-center">

<div>
    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <div>
        <fmt:message key="hello"/><ctg:hello userLogin="${login}"/>
    </div>

    <div>
        <h3><fmt:message key="no.orders"/></h3>
    </div>

    <%-- Report link--%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="report">Report</a>
    </div>
    <%-- Report link--%>

    <%-- Log out link --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
    </div>
    <%-- Log out link --%>

    <div>
        <p class="mt-5 mb-3 text-muted">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
