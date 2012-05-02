<%@ page import="java.util.Set" %>
<%@ page import="mhc.common.dto.MenuNode" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.BrandCatalogAction" var="ca"/>
<s:layout-definition>
<%
  Set<MenuNode> menuNodes = (Set<MenuNode>) pageContext.getAttribute("menuNodeList");
  pageContext.setAttribute("menuNodeList", menuNodes);
%>
  <div class='box'>
    <h5 class='heading1'>
      Categories
    </h5>
    <ul>
      <c:forEach items="${menuNodeList}" var="menuNode">
        <li>
          <a href="${pageContext.request.contextPath}${menuNode.url}?brand=${hk:urlEncode(ca.brand)}">
              ${menuNode.name}
          </a>
        </li>
      </c:forEach>
    </ul>
  </div>
  <script type="text/javascript">
    $('.catalog_filters ul li.lvl2').click(function() {
      url = $(this).children("a").attr("href");
      document.location.href = url;
    });
  </script>
</s:layout-definition>
