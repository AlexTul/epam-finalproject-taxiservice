<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>

<c:set var="url" value="${sessionScope.role eq 'ADMINISTRATOR' ?
                                'admin' : 'client'}"/>

<div class="row">
    <div class="col-lg-6"></div>

    <div class="col-lg-1">
        <%-- Admin menu cars --%>
        <div id="nav-link" class="text-right">
            <a class="text-info" href="${url}"><fmt:message key="menu"/></a>
        </div>
        <%-- Admin menu cars --%>
    </div>

    <div class="col-lg-5"></div>
</div>

<%--Locale and Logout--%>
<div class="row">
    <div class="col-lg-5"></div>

    <div class="col-lg-1">
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

    <div class="col-lg-1">
        <%-- Log out link --%>
        <div id="nav-link" class="text-right">
            <a class="text-info" href="${pageContext.request.contextPath}/logout"><fmt:message key="logout"/></a>
        </div>
        <%-- Log out link --%>
    </div>

    <div class="col-lg-5"></div>
</div>
<%--Locale and Logout--%>

<div class="row">
    <p class="col-lg-12">&copy;<fmt:message key="reserved"/>. 2023</p>
</div>