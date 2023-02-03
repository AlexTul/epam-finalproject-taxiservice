<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>
<c:set var="title" value="Панель администратора" scope="page"/>

<link rel="stylesheet" type="text/css" href="static/css/pager.css">

<div class="pagination">
    <a href="${url}?page=${current_page-1>=0?current_page-1:0}" class="prev">&lt; <fmt:message key="prev"/></a>

    <%-- show left pages --%>
    <c:choose>
        <c:when test="${showAllPrev}">
            <c:if test="${current_page > 0}">
                <c:forEach begin="0" end="${current_page - 1}" var="p">
                    <a href="${url}?page=${p}">${p + 1}</a>
                </c:forEach>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:forEach begin="0" end="${N_PAGES_FIRST - 1}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${current_page - N_PAGES_PREV}" end="${current_page - 1}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <%-- show current page --%>
    <a href="${url}?page=${current_page}" class="current">${current_page + 1}</a>
    <%-- show right pages --%>
    <c:choose>
        <c:when test="${showAllNext}">
            <c:forEach begin="${current_page + 1}" end="${last_page}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach begin="${current_page + 1}" end="${current_page + 1 + (N_PAGES_NEXT - 1)}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${last_page - (N_PAGES_LAST - 1)}" end="${last_page}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <a href="${url}?page=${current_page + 1 > last_page ? last_page : current_page + 1}" class="next"><fmt:message key="next"/> &gt;</a>
</div>