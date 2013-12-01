<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
<s:layout-definition>

  <c:set var="topCategoryUrl" value="/${topCategory}"/>
  <c:set var="allCategories" value="${allCategories}"/>

  <script type="text/javascript">
    $(document).ready(function() {
      var menuStr = renderMenu();
      var secondaryMenuStr = renderSecondaryMenu();
      $('#menuUl').html(menuStr);
      $('#secondaryMenuUl').html(secondaryMenuStr);

      function renderMenu() {
        var menuStr = "";
      <c:forEach items='${menuAction.menuNodes}' var='topMenuNode' varStatus='idx'>
        menuStr += "<li class='lvl1 ${topMenuNode.url == topCategoryUrl ? 'active' : ''} ${topMenuNode.url == '/home-living' ? 'new' : ''}'><a href='${pageContext.request.contextPath}${topMenuNode.url}' ${topCategoryUrl == '/' || topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}>${topMenuNode.name}</a><div class='lvl2'>";
      <c:forEach items='${topMenuNode.childNodes}' var='firstLevelMenuNode'>
        menuStr += "<div class='categories'><a href='${pageContext.request.contextPath}${firstLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}><span class='head2'>${firstLevelMenuNode.name}</span></a><ul>";
      <c:forEach items='${firstLevelMenuNode.childNodes}' var='secondLevelMenuNode'>
        menuStr += "<li><a href='${pageContext.request.contextPath}${secondLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url && hk:firstStringContainsSecond(allCategories, firstLevelMenuNode.slug) ? '' : 'rel=\'noFollow\''}>${secondLevelMenuNode.name}</a></li>";
      </c:forEach>
        menuStr += "</ul></div>";
      </c:forEach>
        menuStr += "</div></li>";
      </c:forEach>
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
    });


  </script>

</s:layout-definition>