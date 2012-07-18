<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>

  <%
    ProductDao productDao = (ProductDao)ServiceLocatorFactory.getService(ProductDao.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product = productDao.getProductById(product_productThumbId);
    pageContext.setAttribute("product", product);
  %>

  <div>
  <hk:productVideo videoCode="${product.videoEmbedCode}" />
  <!--
      ${product.videoEmbedCode}
  -->
  </div>
</s:layout-definition>