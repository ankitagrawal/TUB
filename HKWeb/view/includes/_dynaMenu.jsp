<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
<s:layout-definition>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/menu.jsp"></script>--%>

    <c:set var="topCategoryUrl" value="/${topCategory}"/>
    <c:set var="allCategories" value="${allCategories}"/>

    <script type="text/javascript">
        $(document).ready(function() {
            var menuStr = renderMenu();
            var secondaryMenuStr = renderSecondaryMenu();
            $('#menuUl').html(menuStr);
            $('#secondaryMenuUl').html(secondaryMenuStr);
        });

        function renderMenu() {
            var menuStr = "";
            menuStr += "<li class='lvl1' title='go to healthkart home' id='home_button'><a href='/'><div id='homeIcon'></div></a></li>";
        <c:forEach items='${menuAction.menuNodes}' var='topMenuNode' varStatus='idx'>
        <c:if test="${topMenuNode.name != 'Baby'}">
            menuStr += "<li class='lvl1 ${topMenuNode.url == topCategoryUrl ? 'active' : ''}'><a href='${pageContext.request.contextPath}${topMenuNode.url}' ${topCategoryUrl == '/' || topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}>${topMenuNode.name}</a><div class='lvl2'>";
        <c:forEach items='${topMenuNode.childNodes}' var='firstLevelMenuNode'>
            menuStr += "<div class='categories'><a href='${pageContext.request.contextPath}${firstLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}><span class='head2'>${firstLevelMenuNode.name}</span></a><ul>";
        <c:forEach items='${firstLevelMenuNode.childNodes}' var='secondLevelMenuNode'>
            menuStr += "<li><a href='${pageContext.request.contextPath}${secondLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url && hk:firstStringContainsSecond(allCategories, firstLevelMenuNode.slug) ? '' : 'rel=\'noFollow\''}>${secondLevelMenuNode.name}</a></li>";
        </c:forEach>
            menuStr += "</ul></div>";
        </c:forEach>
            menuStr += "</div></li>";
        </c:if>
        </c:forEach>
        <%--<shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">--%>
        <%--</shiro:hasPermission>--%>
            menuStr += "<li class='' id='brands_button' style='float:right; margin: 4px 6px 0 0; padding: 2px 4px; background-color: #2b659d; -moz-border-radius: 3px; border-radius: 3px; border: 1px solid #4c97df;'><a href='http://www.healthkartplus.com' style='color: yellow; border-bottom: 0;' target='_blank'>HealthKartPlus &gt;</a></li>";
            return menuStr;
        }

        function renderSecondaryMenu() {
            var menuStr = "";
        <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
        <c:if test="${topMenuNode.url == topCategoryUrl}">
            menuStr += "<div class='lvl2Bar'><ul>";
        <c:forEach items="${topMenuNode.childNodes}" var="firstLevelMenuNode">
            menuStr += "<li class='lvl2'><a href='${pageContext.request.contextPath}${firstLevelMenuNode.url}' rel='nofollow'><span class='head2'>${firstLevelMenuNode.name}</span></a><div class='lvl3Container'><ul>";
        <c:forEach items="${firstLevelMenuNode.childNodes}" var="secondLevelMenuNode">
            menuStr += "<li><a href='${pageContext.request.contextPath}${secondLevelMenuNode.url}' rel='nofollow'>${secondLevelMenuNode.name}</a></li>";
        </c:forEach>
            menuStr += "</ul></div></li>";
        </c:forEach>
            menuStr += "</ul></div>";
        </c:if>
        </c:forEach>
            return menuStr;
        }

    </script>

</s:layout-definition>