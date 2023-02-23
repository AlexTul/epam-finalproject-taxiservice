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
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Menu</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-2"></div>

        <div class="col-lg-8">
            <!--Main layout-->
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-4 pt-3">
                        <div class="card" style="background-color:white;">
                            <div class="card-body">
                                <h4 class="card-title"><fmt:message key="new.order"/></h4>
                                <p class="card-text"><fmt:message key="manage.section"/></p>
                                <form method="get" action="order">
                                    <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 pt-3">
                        <div class="card" style="background-color:white;">
                            <div class="card-body">
                                <h4 class="card-title"><fmt:message key="report"/></h4>
                                <p class="card-text"><fmt:message key="manage.section"/></p>
                                <form method="get" action="report-client">
                                    <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 pt-3">
                        <div class="card" style="background-color:white;">
                            <div class="card-body">
                                <h4 class="card-title"><fmt:message key="profile"/></h4>
                                <p class="card-text"><fmt:message key="manage.section"/></p>
                                <form method="get" action="profile">
                                    <button type="submit" class="btn btn-primary"><fmt:message key="manage"/></button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-2"></div>
    </div>
    <!--Main layout-->
    <br>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>
