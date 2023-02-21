<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<html>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Login</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <%-- Forma for enter login`s credential --%>
    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-2">
            <form class="form-signing" method="post" action="/auth">
                <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message
                        key="please"/><br>
                    <fmt:message key="login.register"/></h1>

                <label for="login" class="sr-only">Login form</label>
                <input type="text" id="login" name="login" class="form-control"
                       placeholder="email@gmail.com"
                       minlength="2" maxlength="40" required>
                <p style="color: red">${requestScope.loginValidate}</p>

                <label for="password" class="sr-only">Password form</label>
                <input type="password" id="password" name="password" class="form-control"
                       placeholder="<fmt:message key="password"/>"
                       minlength="10" maxlength="20" required>
                <p style="color: red">${requestScope.passwordValidate}</p>

                <%-- Log In button --%>
                <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="login"/></button>
                <%-- Log In button --%>
            </form>
            <%-- Forma for enter login`s credential --%>
        </div>

        <div class="col-lg-5"></div>
    </div>
    <br>

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

        <div class="col-lg-1">
            <%-- Register link --%>
            <div id="register-link" class="text-right">
                <a class="text-info" href="register"><fmt:message key="register"/></a>
            </div>
            <%-- Register link --%>
        </div>

        <div class="col-lg-5"></div>
    </div>
    <%--Locale and Register--%>

    <div class="row">
        <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>