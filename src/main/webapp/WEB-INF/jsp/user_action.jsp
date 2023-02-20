<%@ page import="com.epam.alextuleninov.taxiservice.model.car.category.CarCategory" %>
<%@ page import="com.epam.alextuleninov.taxiservice.model.car.status.CarStatus" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<html>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Cars action</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-2">
            <form class="form-signing" method="post" action="user" >
                <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message key="please"/>
                    <fmt:message key="enter.data"/></h1>

                <p class="h3 mb-3 font-weight-normal"><fmt:message key="first.name"/>: ${requestScope.userResponse.firstName}</p>

                <p class="h3 mb-3 font-weight-normal"><fmt:message key="last.name"/>: ${requestScope.userResponse.lastName}</p>

                <p class="h3 mb-3 font-weight-normal"><fmt:message key="email"/>: ${requestScope.userResponse.email}</p>
                <br>

                <label for="password" class="sr-only">Password form</label>
                <input type="password" id="password" name="newPassword" class="form-control"
                       placeholder="<fmt:message key="password"/>"
                       minlength="10" maxlength="20" required>
                <p style="color: red">${sessionScope.passwordValidate}</p><br>

                <%-- Register button --%>
                <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="register"/></button>
                <%-- Register button --%>
            </form>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <div class="row">
        <div class="col-lg-6"></div>

        <div class="col-lg-1">
            <%-- Admin menu cars --%>
            <div id="nav-link" class="text-right">
                <a class="text-info" href="/user"><fmt:message key="users"/></a>
            </div>
            <%-- Admin menu cars --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <%--Locale and Logout--%>
    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-1">
            <%--Locale--%>
            <form method="get" class="d-flex" action="user/update">
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
            <div id="nav-link" class="text-right">
                <a class="text-info" href="${pageContext.request.contextPath}/logout"><fmt:message key="logout"/></a>
            </div>
            <%-- Log out link --%>
        </div>

        <div class="col-lg-5"></div>
    </div>
    <%--Locale and Logout--%>

    <div class="row">
        <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
