<%@ taglib prefix="ctg" uri="customtags" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<fmt:setBundle basename="resources"/>

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

    <%-- Choose section --%>
    <div class="row">
        <%-- Choose by customer --%>
        <div class="col-md-3">
            <form class="form-signing" method="get" action="report">
                <label for="customersOfOrders"><fmt:message key="choose.order.customer"/>:</label><br>
                <select id="customersOfOrders" name="customerOfOrders" onchange='submit();'>
                    <c:forEach items="${customersOfOrders}" var="customer">
                        <option value="${customer}">${customer}</option>
                    </c:forEach>
                </select>
                <%--        <input type="submit">--%>
            </form>
        </div>
        <%-- Choose by customer --%>

        <%-- Choose by date --%>
        <div class="col-md-3">
            <form class="form-signing" method="get" action="report">
                <label for="datesOfOrders"><fmt:message key="choose.order.start.end"/>:</label><br>
                <select id="datesOfOrders" name="dateOfOrders" onchange='submit();'>
                    <c:forEach items="${datesOfOrders}" var="date">
                        <option value="${date}">${date}</option>
                    </c:forEach>
                </select>
            </form>
        </div>
        <%-- Choose by date --%>
    </div>
    <%-- Choose section --%>

    <%-- Table of orders --%>
    <div class="tab-pane fade show active" id="users" role="tabpanel"
         aria-labelledby="internet-tab">
        <h3><fmt:message key="table.orders"/>: ${whoseOrders}. ${sort}</h3>
        <table class="table table-striped">
            <thead>
            <tr>
                <c:set var="orderBy"
                       value="${sessionScope.orderBy eq 'desc' ? 'asc' : 'desc'}"/>
                <th scope="col">
                    Id
                </th>
                <th scope="col">
                    <a href="report?sortByDate=${orderBy}" class="list-group-item">
                        <i class="fa fa-arrows-v" style="color:black"></i>
                    </a>
                    <fmt:message key="createdAt"/>
                </th>
                <th scope="col">
                    <fmt:message key="customers"/>
                </th>
                <th scope="col">
                    <fmt:message key="passengers"/>
                </th>
                <th scope="col">
                    <fmt:message key="route"/>
                </th>
                <th scope="col">
                    <a href="report?sortByCost=${orderBy}" class="list-group-item">
                        <i class="fa fa-arrows-v" style="color:black"></i>
                    </a>
                    <fmt:message key="cost"/>
                </th>
                <th scope="col">
                    <fmt:message key="startedAt"/>
                </th>
                <th scope="col">
                    <fmt:message key="finishesAt"/>
                </th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="orderResponse" items="${orders}">
                <tr>
                    <td>${orderResponse.id}</td>
                    <td>${orderResponse.createdAt}</td>
                    <td>${orderResponse.customerEmail}</td>
                    <td>${orderResponse.numberOfPassengers}</td>
                    <td>${orderResponse.startEndLocale}</td>
                    <td>${orderResponse.cost}</td>
                    <td>${orderResponse.startedAt}</td>
                    <td>${orderResponse.finishedAt}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <%-- Table of orders --%>

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

    <div>
        <p class="mt-5 mb-3 text-muted">&copy;<fmt:message key="reserved"/>. 2023</p>
    </div>
</div>

</body>
</html>
