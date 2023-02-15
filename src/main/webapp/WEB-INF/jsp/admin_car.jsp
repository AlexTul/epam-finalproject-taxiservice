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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Cars</title>
</head>
<body class="text-center">

<div>
    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <div>
        <fmt:message key="hello"/><ctg:hello userLogin="${login}"/>
    </div>

    <form method="get" action="car/add">
        <button type="submit" class="btn btn-primary">Add car</button>
    </form>

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
                        <form method="get" action="car/update">
                            <input type="number" hidden name="id" value="${carResponse.id}"/>
                            <button type="submit" class="btn btn-warning btn-lg"></button>
                        </form>
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

    <%-- Pager --%>
    <div>
        <jsp:include page="/WEB-INF/templates/_pager.jsp"></jsp:include>
    </div>
    <%-- Pager --%>

    <%-- Admin menu link --%>
    <div id="nav-link" class="text-right">
        <a class="text-info" href="admin"><fmt:message key="menu.admin"/></a>
    </div>
    <%-- Admin menu link --%>

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
