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
    <title>Reports</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <br>
    <div class="row">
        <%-- Message for user --%>
        <div class="col-lg-12">
            <div>
                <h5>${sessionScope.messageUser}</h5>
            </div>
        </div>
        <%-- Message for user --%>
    </div>
    <br><br>

    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-1"></div>

        <div class="col-lg-1">
            <%-- Log In link --%>
            <div class="text-right">
                <a class="text-info" href="${pageContext.request.contextPath}/"><fmt:message key="login"/></a>
            </div>
            <%-- Log In link --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <%--Locale and Register--%>
    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-1">
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

        <div class="col-lg-1" style="text-align: right">
            <%-- Register link --%>
            <a class="text-info" href="register"><fmt:message key="register"/></a>
            <%-- Register link --%>
        </div>

        <div class="col-lg-5"></div>
    </div>
    <%--Locale and Register--%>
    <br><br>

    <div class="row">
        <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
