<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<c:set var="url" value="${sessionScope.role eq 'ADMINISTRATOR' ?
                                'admin' : 'customer'}"/>

<div class="row">
    <div class="col-lg-1"></div>

    <%-- Pager --%>
    <div class="col-lg-4">
        <jsp:include page="/WEB-INF/templates/_pager.jsp"></jsp:include>
    </div>
    <%-- Pager --%>
    <div class="col-lg-2"></div>
    <%-- Admin menu link --%>
    <div class="col-lg-4">
        <a class="text-info" href="${url}"><fmt:message key="menu"/></a>
    </div>
    <%-- Admin menu link --%>

    <div class="col-lg-1"></div>
</div>
<br>

<%--Locale and Logout--%>
<div class="row">
    <div class="col-lg-1"></div>

    <div class="col-lg-4">
        <%--Locale--%>
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
        <%--Locale--%>
    </div>

    <div class="col-lg-2"></div>

    <div class="col-lg-4">
        <%-- Log out link --%>
        <a class="text-info" href="logout"><fmt:message key="logout"/></a>
        <%-- Log out link --%>
    </div>

    <div class="col-lg-1"></div>
</div>
<%--Locale and Logout--%>

<div class="row">
    <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
</div>