<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="java.util.Stack" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <%
    MenuHelper menuHelper = (MenuHelper)ServiceLocatorFactory.getService("MenuHelper");

    String breadcrumbUrlFragment = (String) pageContext.getAttribute("breadcrumbUrlFragment");
    if (breadcrumbUrlFragment == null) {
      Product breadcrumbProduct = (Product) pageContext.getAttribute("breadcrumbProduct");
//      System.out.println("breadcrumbProduct="+breadcrumbProduct);
      breadcrumbUrlFragment = menuHelper.getUrlFragementFromProduct(breadcrumbProduct);
    }
    MenuNode breadcrumbMenuNode = menuHelper.getMenuNode(breadcrumbUrlFragment);
    pageContext.setAttribute("breadcrumbMenuNode", breadcrumbMenuNode);

    String lastLinkStr = (String) pageContext.getAttribute("lastLink");
    boolean lastLink = StringUtils.isBlank(lastLinkStr) || !lastLinkStr.toLowerCase().equals("true") ? false : true;
    Stack<MenuNode> menuNodeStack = new Stack<MenuNode>();
    if (breadcrumbMenuNode != null) {
      menuNodeStack.push(breadcrumbMenuNode);
      MenuNode parentMenuNode = breadcrumbMenuNode.getParentNode();
      while (parentMenuNode != null) {
        menuNodeStack.push(parentMenuNode);
        parentMenuNode = parentMenuNode.getParentNode();
      }
    }
  %>
  <s:layout-component name="breadcrumb">
    <div class='crumb_outer'>
    <a href="${pageContext.request.contextPath}/" class="crumb">Home</a>
      <%
        while (!menuNodeStack.isEmpty()) {
        MenuNode menuNode = menuNodeStack.pop();
        String url = request.getContextPath() + menuNode.getUrl();
      %>
        &gt;
        <%
          if (menuNodeStack.isEmpty() && !lastLink) {
        %>
          <span class="crumb last" style="font-size: 12px;"><%=menuNode.getName()%></span>
          <%
          } else {
          %>
            <a class="crumb" href="<%=url%>"><%=menuNode.getName()%></a>
            <%
          }
          }
        %>
    <h1 class='title'>
      ${topHeading}
    </h1>
  </div>
  </s:layout-component>
</s:layout-definition>