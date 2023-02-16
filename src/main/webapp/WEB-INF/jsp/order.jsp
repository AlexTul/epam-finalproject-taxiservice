<%@ page import="com.epam.alextuleninov.taxiservice.model.car.category.CarCategory" %>
<%@ taglib prefix="ctg" uri="customtags" %>
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
    <title>Hi, client</title>
</head>
<body class="text-center">

<div>
    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <%-- Customer order --%>
    <form class="form-signing" method="post" action="order">

        <h2 class="h3 mb-3 font-weight-normal"><fmt:message key="taxiservice"/>, <fmt:message key="make.order"/></h2>
        <ctg:hello userLogin="${login}"/>

        <%-- Choose start, end of travel --%>
        <label for="start" class="sr-only">Start travel form</label>
        <input type="text" id="start" name="startTravel" class="form-control"
               placeholder="<fmt:message key="start.travel"/>"
               minlength="2" required><br>

        <label for="end" class="sr-only">Start travel form</label>
        <input type="text" id="end" name="endTravel" class="form-control"
               placeholder="<fmt:message key="end.travel"/>"
               minlength="2" required><br>
        <%-- Choose start, end of travel --%>

        <%-- Choose number of passengers --%>
<%--        <label for="numberPassengers"><fmt:message key="enter.passengers"/>:</label>--%>
        <input type="number" id="numberPassengers" name="numberOfPassengers" class="form-control"
               placeholder="<fmt:message key="enter.passengers"/>"
               min="1" minlength="1" maxlength="1" required>
        <br>
        <%-- Choose number of passengers --%>

        <%-- Choose car`s category --%>
        <label for="carCategory"><fmt:message key="choose.car.category"/>:</label>
        <select id="carCategory" name="carCategory">
            <option value="<%=CarCategory.PASSENGER.toString()%>"><fmt:message key="passenger"/></option>
            <option value="<%=CarCategory.CARGO.toString()%>"><fmt:message key="cargo"/></option>
        </select>
        <br><br>
        <%-- Choose car category --%>

        <%-- Choose the date for ride --%>
        <label for="dateRide"><fmt:message key="enter.date.time.trip"/>:</label>
        <input type="datetime-local" id="dateRide" name="dateOfTravel"><br>
        <label><fmt:message key="car.delivery.time"/></label><br>
        <%-- Choose the date for ride --%>

        <%-- To order button --%>
        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="to.order"/></button>
        <%-- To order button --%>
    </form>
    <%-- Customer order --%>

    <%-- Log out link --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
    </div>
    <%-- Log out link --%>

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