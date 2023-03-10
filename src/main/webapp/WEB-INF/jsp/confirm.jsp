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
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Confirm</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>
    <br>

    <div class="row">
        <div class="col-lg-4"></div>

        <%-- Customer order --%>
        <div class="col-lg-4">
            <form class="form-signing" method="post" action="order">
                <h2 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/>, <fmt:message
                        key="confirm.cancel"/></h2>
                <br>

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
        </div>
        <%-- Customer order --%>

        <div class="col-lg-4"></div>
    </div>

    <div class="row">
        <div class="col-lg-5"></div>

        <div class="col-lg-1"></div>

        <%-- New order button --%>
        <div class="col-lg-1">
            <div class="text-right">
                <a class="text-info" href="order?action=new"><fmt:message key="new.order"/></a>
            </div>
        </div>
        <%-- New order button --%>

        <div class="col-lg-5"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>