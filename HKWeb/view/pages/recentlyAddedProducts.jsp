<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.RecentlyAddedProductsAction" var="rapa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Recently Added Products">

  <s:layout-component name="content">
    <h2>Recently Added Products</h2>
    <table border="1" class="zebra_vert">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>Product ID</th>
        <th>Product Name</th>
        <th>Top Category</th>
        <th>Create Date</th>
      </tr>
      </thead>
      <c:forEach var="product" items="${rapa.productList}" varStatus="ctr">
        <tr>
          <td>${ctr.index+1}.</td>
          <td valign="top">
              ${product.id}
          </td>
          <td valign="top">
              ${product.name}
          </td>
          <td valign="top">
            ${hk:topLevelCategory(product)}
          </td>
          <td>
            <fmt:formatDate value="${product.createDate}" type="both" timeStyle="short"/>
          </td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>
</s:layout-render>