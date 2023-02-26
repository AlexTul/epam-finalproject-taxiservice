<%@ taglib prefix="ctg" uri="customtags" %>
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
    <title>Message order</title>
</head>
<body>

<c:set var="message" value="${sessionScope.locale eq 'en' ?
                                sessionScope.messageOrder : sessionScope.messageOrderUK}"/>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-12">
            <%-- Message for user --%>
            <div>
                <h5>${message}</h5>
            </div>
            <%-- Message for user --%>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <%-- Show date of travel --%>
            <div>
                <c:if test="${sessionScope.dateTimeTravel != null}">
                    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="taxi.service"/><br>
                        <fmt:message key="order.successfully"/></h1>
                    <label><fmt:message key="date.time.travel"/>:</label>
                    <c:out value="${sessionScope.dateTimeTravel}"/>
                    <br>
                </c:if>
            </div>
            <%-- Show date of travel --%>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-6"></div>

        <div class="col-lg-1">
            <%-- New order --%>
            <div id="nav-link" class="text-right">
                <a class="text-info" href="order"><fmt:message key="new.order"/></a>
            </div>
            <%-- New order --%>
        </div>

        <div class="col-lg-5"></div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer-action.jsp"></jsp:include>
</div>

</body>
</html>
