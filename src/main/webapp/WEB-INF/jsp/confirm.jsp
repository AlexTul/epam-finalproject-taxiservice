<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<html>
<head>
    <link href="https://getbootstrap.com/docs/4.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Confirm</title>
</head>
<body class="text-center">

<div>
    <%-- Customer order --%>
    <form class="form-signing" method="post" action="confirm">
        <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
        <h2 class="h3 mb-3 font-weight-normal"><fmt:message key="taxiservice"/>, <fmt:message key="confirm.cancel"/></h2>
        <ctg:hello userLogin="${login}"/>

        <%-- Show route --%>
        <label><fmt:message key="route"/>:</label>
        <c:out value="${sessionScope.startTravel} - ${sessionScope.endTravel}"/>
        <br>
        <%-- Show route --%>

        <%-- Show number of passengers --%>
        <label><fmt:message key="number.of.passengers"/>:</label>
        <c:out value="${sessionScope.numberOfPassengers}"/>
        <br>
        <%-- Show number of passengers --%>

        <%-- Show list of cars --%>
        <label><fmt:message key="list.cars"/>:</label>
        <c:out value="${sessionScope.listOfCars}"/>
        <br>
        <%-- Show car category --%>

        <%-- Show the date for ride --%>
        <label><fmt:message key="date.time.travel"/>:</label>
        <c:out value="${sessionScope.dateOfTravel}"/>
        <br>
        <%-- Show the date for ride --%>

        <%-- Show the price --%>
        <label><fmt:message key="price.travel"/>, $:</label>
        <c:out value="${sessionScope.priceOfTravel}"/>
        <br>
        <%-- Show the price --%>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="confirm"/></button>
    </form>
    <%-- Customer order --%>

    <%-- New order button --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="order"><fmt:message key="new.order"/></a>
    </div>
    <%-- New order button --%>

    <%-- Log out button --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
    </div>
    <%-- Log out button --%>

    <%--Locale--%>
    <div>
        <form method="get" class="d-flex" action="confirm">
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