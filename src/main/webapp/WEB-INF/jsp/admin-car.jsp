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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Cars</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <div class="row">
        <div class="col-lg-12">
            <a class="btn btn-primary" href="car?action=add" role="button"><fmt:message key="car.add"/></a>

            <%-- Table of users --%>
            <div class="tab-pane fade show active" id="cars" role="tabpanel"
                 aria-labelledby="internet-tab">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th scope="col">
                            Id
                        </th>
                        <th scope="col">
                            <fmt:message key="name"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="passengers"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="car.category"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="car.status"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="edit"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="delete"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="carResponse" items="${sessionScope.carResponses}">
                        <tr>
                            <td>${carResponse.id}</td>
                            <td>${carResponse.carName}</td>
                            <td>${carResponse.numberOfPassengers}</td>
                            <td>${carResponse.carCategory}</td>
                            <td>${carResponse.carStatus}</td>
                            <td>
                                <a class="btn btn-warning btn-lg" href="car?action=update&id=${carResponse.id}"
                                   role="button"></a>
                            </td>
                            <td>
                                <form method="post" action="car?id=${carResponse.id}">
                                    <button type="submit" class="btn btn-danger btn-lg"></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <%-- Table of users --%>
        </div>
    </div>

    <jsp:include page="/WEB-INF/templates/_footer.jsp"></jsp:include>
</div>

</body>
</html>
