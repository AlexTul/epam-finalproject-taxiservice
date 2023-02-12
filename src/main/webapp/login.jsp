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
    <title>Login</title>
</head>
<body class="text-center">

<div>
    <%-- Forma for enter login`s credential --%>
    <form class="form-signing" method="post" action="auth">
        <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
        <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxiservice"/><br><fmt:message key="please"/><br>
            <fmt:message key="login.register"/></h1>

        <label for="login" class="sr-only">Login form</label>
        <input type="text" id="login" name="login" class="form-control"
               placeholder="email@gmail.com"
               minlength="2" maxlength="40" required><br>

        <label for="password" class="sr-only">Password form</label>
        <input type="password" id="password" name="password" class="form-control"
               placeholder="<fmt:message key="password"/>"
               minlength="10" maxlength="20" required><br>

        <%-- Log In button --%>
        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="login"/></button>
        <%-- Log In button --%>
    </form>
    <%-- Forma for enter login`s credential --%>

    <%-- Register link --%>
    <div id="register-link" class="text-right">
        <a class="text-info" href="register"><fmt:message key="register"/></a>
    </div>
    <%-- Register link --%>

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