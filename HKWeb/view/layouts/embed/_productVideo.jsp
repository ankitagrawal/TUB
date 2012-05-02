<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="mhc.service.dao.ProductDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    ProductDao productDao = InjectorFactory.getInjector().getInstance(ProductDao.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product = productDao.find(product_productThumbId);
    pageContext.setAttribute("product", product);
  %>

  <div>
      ${product.videoEmbedCode}
  </div>
</s:layout-definition>