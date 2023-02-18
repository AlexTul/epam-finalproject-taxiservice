<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%--Locale--%>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>
<%--Locale--%>
<c:set var="title" value="Панель администратора" scope="page"/>

<link rel="stylesheet" type="text/css" href="static/css/pager.css">

<div class="pagination">
    <a href="${url}?page=${currentPage-1>=0?currentPage-1:0}" class="prev">&lt; <fmt:message key="prev"/></a>

    <%-- show left pages --%>
    <c:choose>
        <c:when test="${showAllPrev}">
            <c:if test="${currentPage > 0}">
                <c:forEach begin="0" end="${currentPage - 1}" var="p">
                    <a href="${url}?page=${p}">${p + 1}</a>
                </c:forEach>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:forEach begin="0" end="${N_PAGES_FIRST - 1}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${currentPage - N_PAGES_PREV}" end="${currentPage - 1}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <%-- show current page --%>
    <a href="${url}?page=${currentPage}" class="current">${currentPage + 1}</a>
    <%-- show right pages --%>
    <c:choose>
        <c:when test="${showAllNext}">
            <c:forEach begin="${currentPage + 1}" end="${lastPage}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach begin="${currentPage + 1}" end="${currentPage + 1 + (N_PAGES_NEXT - 1)}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
            <span style="margin-right: 5px">...</span>
            <c:forEach begin="${lastPage - (N_PAGES_LAST - 1)}" end="${lastPage}" var="p">
                <a href="${url}?page=${p}">${p + 1}</a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <a href="${url}?page=${currentPage + 1 > lastPage ? lastPage : currentPage + 1}" class="next"><fmt:message key="next"/> &gt;</a>
</div>