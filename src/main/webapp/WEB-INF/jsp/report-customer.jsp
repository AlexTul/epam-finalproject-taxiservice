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
    <title>Reports</title>
</head>
<body>

<div class="container-fluid" style="text-align: center">
    <jsp:include page="/WEB-INF/templates/_header.jsp"></jsp:include>

    <%-- Table of orders --%>
    <div class="row">
        <div class="col-lg-12">
            <div class="tab-pane fade show active" id="reports" role="tabpanel"
                 aria-labelledby="internet-tab">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <c:set var="orderBy"
                               value="${sessionScope.orderBy eq 'desc' ? 'asc' : 'desc'}"/>
                        <th scope="col">
                            Id
                        </th>
                        <th scope="col">
                            <a href="report-client?sortByDate=${orderBy}" class="list-group-item">
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
                            <fmt:message key="cars"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="start.travel"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="end.travel"/>
                        </th>
                        <th scope="col">
                            <a href="report-client?sortByCost=${orderBy}" class="list-group-item">
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
                        <th scope="col">
                            <fmt:message key="edit"/>
                        </th>
                        <th scope="col">
                            <fmt:message key="delete"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="orderResponse" items="${requestScope.orders}">
                        <tr>
                            <td>${orderResponse.id}</td>
                            <td>${orderResponse.createdAt}</td>
                            <td>${orderResponse.customerEmail}</td>
                            <td>${orderResponse.numberOfPassengers}</td>
                            <td>${orderResponse.stringOfCars}</td>
                            <td>${orderResponse.startTravel}</td>
                            <td>${orderResponse.endTravel}</td>
                            <td>${orderResponse.cost}</td>
                            <td>${orderResponse.startedAt}</td>
                            <td>${orderResponse.finishedAt}</td>
                            <td>
                                <a class="btn btn-warning btn-lg" href="order?action=update&id=${orderResponse.id}"
                                   role="button"></a>
                            </td>
                            <td>
                                <form method="post" action="order?id=${orderResponse.id}">
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
    <%-- Table of orders --%>

    <jsp:include page="/WEB-INF/templates/_footer.jsp"></jsp:include>
</div>

</body>
</html>
