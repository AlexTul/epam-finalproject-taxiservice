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

    <title>Reports</title>
</head>
<body class="text-center">

<div>
    <img class="mb-4" src="static/img/img.jpg" alt="" width="241" height="125">
    <div>
        <fmt:message key="hello"/><ctg:hello userLogin="${login}"/>
    </div>

    <%-- Table of users --%>
    <div class="tab-pane fade show active" id="users" role="tabpanel"
         aria-labelledby="internet-tab">
        <table class="table table-striped">
            <thead>
            <tr>
                <th scope="col">
                    Id
                </th>
                <th scope="col">
                    <fmt:message key="first.name"/>
                </th>
                <th scope="col">
                    <fmt:message key="last.name"/>
                </th>
                <th scope="col">
                    <fmt:message key="email"/>
                </th>
                <th scope="col">
                    <fmt:message key="role"/>
                </th>
                <th scope="col">
                    <fmt:message key="delete"/>
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="userResponse" items="${sessionScope.userResponses}">
                <tr>
                    <td>${userResponse.id}</td>
                    <td>${userResponse.firstName}</td>
                    <td>${userResponse.lastName}</td>
                    <td>${userResponse.email}</td>
                    <td>${userResponse.role}</td>
                    <td>
                        <form method="post" action="user?id=${userResponse.id}">
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
