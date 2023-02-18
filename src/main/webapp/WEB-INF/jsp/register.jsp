<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<fmt:setBundle basename="resources"/>

<html>
<c:set var="title" value="Панель администратора" scope="page"/>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Register</title>
</head>
<body class="text-center">

<div>
    <form class="form-signing" method="post" action="register">
        <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
        <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message key="please"/><br>
            <fmt:message key="enter.data"/></h1>

        <label for="firstname" class="sr-only">First name Form</label>
        <input type="text" id="firstname" name="firstname" class="form-control"
               placeholder="<fmt:message key="first.name"/>"
               minlength="2" maxlength="20" required><br>

        <label for="lastname" class="sr-only">Last name Form</label>
        <input type="text" id="lastname" name="lastname" class="form-control"
               placeholder="<fmt:message key="last.name"/>"
               minlength="2" maxlength="20" required><br>

        <label for="email" class="sr-only">Login Form</label>
        <input type="text" id="email" name="email" class="form-control"
               placeholder="email@gmail.com"
               minlength="2" maxlength="40" required><br>

        <label for="password" class="sr-only">Password Form</label>
        <input type="password" id="password" name="password" class="form-control"
               placeholder="<fmt:message key="password"/>"
               minlength="10" maxlength="20" required><br>

        <%-- Register button --%>
        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="register"/></button>
        <%-- Register button --%>
    </form>

    <%-- Log In link --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="/"><fmt:message key="login"/></a>
    </div>
    <%-- Log In link --%>

    <div>
        <p class="mt-5 mb-3 text-muted">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>