<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<fmt:setBundle basename="resources"/>

<html>
<c:set var="title" value="Панель администратора" scope="page"/>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Successful</title>
</head>
<body class="text-center">

<div>

    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxiservice"/><br>
        <fmt:message key="order.successfully"/></h1>

    <%-- Show route --%>
    <label><fmt:message key="date.time.trip"/>:</label>
    <c:out value="${dateTimeTrip}"/>
    <br>
    <%-- Show route --%>

    <%-- New order button --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="order"><fmt:message key="new.order"/></a>
    </div>
    <%-- New order button --%>

    <%-- Log out button --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
    </div>
    <%-- Log out button --%>

    <div>
        <p class="mt-5 mb-3 text-muted">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>