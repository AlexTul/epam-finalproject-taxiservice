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
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-3"></div>

        <div class="col-lg-6">
            <!--Main layout-->
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-4 pt-3">
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
                    <div class="col-md-4 pt-3">
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
                    <div class="col-md-4 pt-3">
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
        </div>

        <div class="col-lg-3"></div>
    </div>
    <!--Main layout-->
    <br>

    <%--Locale and Logout--%>
    <div class="row">
        <div class="col-lg-3"></div>

        <div class="col-lg-5">
            <%--Locale--%>
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
            <%--Locale--%>
        </div>

        <div class="col-lg-1">
            <%-- Log out link --%>
            <a class="text-info" href="logout"><fmt:message key="logout"/></a>
            <%-- Log out link --%>
        </div>

        <div class="col-lg-3"></div>
    </div>
    <%--Locale and Logout--%>

    <div class="row">
        <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
