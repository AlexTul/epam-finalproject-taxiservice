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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>User action</title>
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

                <p class="h5 mb-3 font-weight-normal"><fmt:message key="first.name"/>: ${sessionScope.userResponse.firstName}</p>
                <p class="h5 mb-3 font-weight-normal"><fmt:message key="last.name"/>: ${sessionScope.userResponse.lastName}</p>
                <p class="h5 mb-3 font-weight-normal"><fmt:message key="email"/>: ${sessionScope.userResponse.email}</p>
                <p class="h5 mb-3 font-weight-normal"><fmt:message key="role"/>: ${sessionScope.userResponse.role}</p>
                <br>

                <label for="password" class="sr-only">Password form</label>
                <input type="password" id="password" name="newPassword" class="form-control"
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

    <div class="row">
        <div class="col-lg-6"></div>

        <div class="col-lg-1">
            <%-- Admin menu users --%>
            <div id="nav-link" class="text-right">
                <a class="text-info" href="user?action=user"><fmt:message key="users"/></a>
            </div>
            <%-- Admin menu users --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>
