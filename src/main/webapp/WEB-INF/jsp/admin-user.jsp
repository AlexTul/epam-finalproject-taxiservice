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
    <title>Users</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">

    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <%-- Table of users --%>
    <div class="row">
        <div class="col-lg-12">
            <div class="tab-pane fade show active" id="users" role="tabpanel"
                 aria-labelledby="internet-tab">
                <h3><fmt:message key="table.users"/></h3>
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
                            <fmt:message key="edit"/>
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
                                <form method="get" action="user/update">
                                    <input type="text" hidden name="login" value="${userResponse.email}"/>
                                    <button type="submit" class="btn btn-warning btn-lg"></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="user?login=${userResponse.email}">
                                    <button type="submit" class="btn btn-danger btn-lg"></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <%-- Table of users --%>

    <jsp:include page="/WEB-INF/templates/_footer-admin.jsp"></jsp:include>
</div>

</body>
</html>
