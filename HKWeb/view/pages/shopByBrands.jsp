<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.ShopByBrandsAction" var="brandAction"/>
<%
  
	boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);

%>
<s:layout-render name="/layouts/default100.jsp" pageTitle="Shop by Brands">
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#brands_button').addClass("active");
      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <h1>Shop by Brand</h1>

    <c:forEach items="${brandAction.categories}" var="category">
      <div id="${category}">&nbsp;</div>
      <div class="brandBox">
        <a href="${pageContext.request.contextPath}/${category.name}"><h4>${category.displayName}</h4></a>
        <table width="100%">
          <c:forEach items="${hk:brandsInCategory(category)}" var="brand" varStatus="idx">
            <c:if test="${idx.index%5 == 0}">
              <tr>
            </c:if>
            <td class="col">
              <s:link beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" class="bl">
                ${brand}
                <s:param name="brand" value="${brand}"/>
                <s:param name="topLevelCategory" value="${category.name}"/>
              </s:link>
            </td>
            <c:if test="${idx.index%5 == 4 || idx.last}">
              </tr>
            </c:if>
          </c:forEach>
        </table>
        <div class="floatfix"></div>

      </div>
    </c:forEach>
		
			<c:if test="${not isSecure }">
				<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=1" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
			</c:if>
			
  </s:layout-component>


</s:layout-render>
