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
            <form class="form-signing" method="post" action="profile" >
                <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message
                        key="please"/><br>
                    <fmt:message key="enter.data"/></h1>

                <label for="firstname" class="sr-only">First name form</label>
                <input type="text" id="firstname" name="firstname" class="form-control"
                       value="${sessionScope.userResponse.firstName}"
                       minlength="2" maxlength="20" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\\- ]{1,20}" required>
                <p style="color: red">${requestScope.firstname}</p><br>

                <label for="lastname" class="sr-only">Last name form</label>
                <input type="text" id="lastname" name="lastname" class="form-control"
                       value="${sessionScope.userResponse.lastName}"
                       minlength="2" maxlength="20" pattern="^[A-Za-zА-ЩЬЮЯҐІЇЄа-щьюяґіїє'\\- ]{1,20}" required>
                <p style="color: red">${requestScope.lastname}</p><br>

                <label for="email" class="sr-only">Login form</label>
                <input type="text" id="email" name="login" class="form-control"
                       value="${sessionScope.userResponse.email}"
                       minlength="2" maxlength="40" pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$" required>
                <p style="color: red">${requestScope.loginValidate}</p><br>

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
                <a class="text-info" href="admin"><fmt:message key="menu.admin"/></a>
            </div>
            <%-- Admin menu cars --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>
