<%@ taglib prefix="ctg" uri="customtags" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<html>
<c:set var="title" value="Панель администратора" scope="page"/>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Menu</title>
</head>
<body class="text-center">

<div>
    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <div>
        <fmt:message key="hello"/><ctg:hello userLogin="${login}"/>
    </div>

    <!--Main layout-->
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-4 pt-3" >
                <div class="card" style="background-color:white;">
                    <div class="card-body">
                        <h4 class="card-title"><fmt:message key="orders"/></h4>
                        <p class="card-text"><fmt:message key="manage.section"/></p>
                        <form method="get" action="report">
                            <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-4 pt-3" >
                <div class="card" style="background-color:white;">
                    <div class="card-body">
                        <h4 class="card-title"><fmt:message key="users"/></h4>
                        <p class="card-text"><fmt:message key="manage.section"/></p>
                        <form method="get" action="user">
                            <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-4 pt-3" >
                <div class="card" style="background-color:white;">
                    <div class="card-body">
                        <h4 class="card-title"><fmt:message key="cars"/></h4>
                        <p class="card-text"><fmt:message key="manage.section"/></p>
                        <form method="get" action="car">
                            <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--Main layout-->

    <br>
    <%-- Log out link --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
    </div>
    <%-- Log out link --%>

    <%--Locale--%>
    <div>
        <form method="get" class="d-flex">
            <label>
                <select name="locale" onchange='submit();'>
                    <option value="en" ${sessionScope.locale eq 'en' ? 'selected' : ''}>
                        <fmt:message key="en"/>
                    </option>
                    <option value="uk_UA" ${sessionScope.locale eq 'uk_UA' ? 'selected' : ''}>
                        <fmt:message key="ua"/>
                    </option>
                </select>
            </label>
        </form>
    </div>
    <%--Locale--%>

    <div>
        <p class="mt-5 mb-3 text-muted">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
