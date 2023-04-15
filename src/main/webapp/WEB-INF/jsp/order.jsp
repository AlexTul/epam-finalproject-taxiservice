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
    <link href="https://getbootstrap.com/docs/4.0/examples/sign-in/signin.css" rel="stylesheet">
    <title>Order</title>
</head>
<body>

<c:set var="addressInvalid" value="${sessionScope.locale eq 'en' ?
                                sessionScope.addressInvalid : sessionScope.addressInvalidUK}"/>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-4"></div>

        <%-- Customer order --%>
        <div class="col-lg-4">
            <form class="form-signing" method="get" action="order">
                <input type="hidden" name="action" value="confirm">

                <h2 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/>, <fmt:message
                        key="make.order"/></h2>

                <%-- Choose start, end of travel --%>
                <p style="color: red">${addressInvalid}</p>
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
                <p style="color: red">${requestScope.numberOfPassengersValidate}</p>
                <%-- Choose car category --%>

                <%-- Choose the date for ride --%>
                <label for="dateRide"><fmt:message key="enter.date.time.trip"/>:</label>
                <input type="datetime-local" id="dateRide" name="dateOfTravel"><br>
                <p style="color: red">${requestScope.dateTimeValidate}</p>
                <label><fmt:message key="car.delivery.time"/></label>
                <br>
                <%-- Choose the date for ride --%>

                <%-- To order button --%>
                <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="to.order"/></button>
                <%-- To order button --%>
            </form>
        </div>
        <%-- Customer order --%>

        <div class="col-lg-4"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>