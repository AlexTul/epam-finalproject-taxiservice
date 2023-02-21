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

    <!-- Custom styles for this template -->
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
            <form class="form-signing" method="post" action="car">
                <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br><fmt:message key="please"/>
                    <fmt:message key="enter.data"/></h1>

                <label for="carName"><fmt:message key="enter.car.name"/>:</label>
                <input type="text" id="carName" name="carName" class="form-control"
                       value="${requestScope.carResponses.carName}"
                       minlength="3" maxlength="20" required><br>

                <label for="numberPassengers"><fmt:message key="enter.passengers"/>:</label>
                <input type="number" id="numberPassengers" name="numberOfPassengers" class="form-control"
                       value="${requestScope.carResponses.numberOfPassengers}"
                       min="1" minlength="1" maxlength="1" required>
                <br>

                <label for="carCategory"><fmt:message key="choose.car.category"/>:</label>
                <select id="carCategory" name="carCategory">
                    <option value="<%=CarCategory.PASSENGER.toString()%>"><fmt:message key="passenger"/></option>
                    <option value="<%=CarCategory.CARGO.toString()%>"><fmt:message key="cargo"/></option>
                </select>
                <br><br>

                <label for="carStatus"><fmt:message key="choose.car.status"/>:</label>
                <select id="carStatus" name="carStatus">
                    <option value="<%=CarStatus.AVAILABLE.toString()%>"><fmt:message key="available"/></option>
                    <option value="<%=CarStatus.INACTIVE.toString()%>"><fmt:message key="inactive"/></option>
                </select>
                <br><br>

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
                <a class="text-info" href="/car"><fmt:message key="cars"/></a>
            </div>
            <%-- Admin menu cars --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>
