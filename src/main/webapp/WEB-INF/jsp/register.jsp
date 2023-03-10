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
    <title>Register</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-2">
            <form class="form-signing" method="post" action="register">
                <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message
                        key="please"/><br>
                    <fmt:message key="enter.data"/></h1>

                <label for="firstname" class="sr-only">First name form</label>
                <input type="text" id="firstname" name="firstname" class="form-control"
                       placeholder="<fmt:message key="first.name"/>"
                       minlength="2" maxlength="20" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\\- ]{1,20}" required>
                <p style="color: red">${sessionScope.firstname}</p><br>

                <label for="lastname" class="sr-only">Last name form</label>
                <input type="text" id="lastname" name="lastname" class="form-control"
                       placeholder="<fmt:message key="last.name"/>"
                       minlength="2" maxlength="20" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\\- ]{1,20}" required>
                <p style="color: red">${sessionScope.lastname}</p><br>

                <label for="email" class="sr-only">Login form</label>
                <input type="text" id="email" name="login" class="form-control"
                       placeholder="email@gmail.com"
                       minlength="2" maxlength="40" pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required>
                <p style="color: red">${sessionScope.loginValidate}</p><br>

                <label for="password" class="sr-only">Password form</label>
                <input type="password" id="password" name="password" class="form-control"
                       placeholder="<fmt:message key="password"/>"
                       minlength="10" maxlength="20" pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,20}$" required>
                <p style="color: red">${sessionScope.passwordValidate}</p><br>

                <%-- Register button --%>
                <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="register"/></button>
                <%-- Register button --%>
            </form>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <%--Locale and Log In--%>
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
            <%--Log In link--%>
            <div id="nav-link" class="text-right">
                <a class="text-info" href="${pageContext.request.contextPath}/"><fmt:message key="login"/></a>
            </div>
            <%--Log In link--%>
        </div>

        <div class="col-lg-5"></div>
    </div>
    <%--Locale and Log In--%>

    <div class="row">
        <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>