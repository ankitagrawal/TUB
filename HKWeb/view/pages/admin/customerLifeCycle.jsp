<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.CustomerLifeCycleAction" var="customerLifeCycleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Lifecycle">
  <s:layout-component name="heading">Order Lifecycle : Order#${customerLifeCycleBean.user.login}</s:layout-component>
  <s:layout-component name="content">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
      <tr class="even_row">
        <th width="20%">Product</th>
        <th width="20%">1st Checked on</th>
        <th width="20%">Last Seen on</th>
        <th width="20%">Counter</th>
        <th width="20%">Added to Cart ?</th>
      </tr>
      <c:forEach items="${customerLifeCycleBean.userProductsList}" var="userProduct">
        <tr>
          <td>${userProduct.product.name}</td>
          <td><fmt:formatDate value="${userProduct.createDate}" type="both"/></td>
          <td><fmt:formatDate value="${userProduct.lastDate}" type="both"/></td>
          <td>${userProduct.counter}</td>
          <td>${userProduct.bought}</td>
        </tr>
      </c:forEach>
    </table>

    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
      <tr class="even_row">
        <th width="20%">Product</th>
        <th width="20%">1st Added on</th>
        <th width="20%">Last Seen on</th>
        <th width="20%">Counter</th>
        <th width="20%">Is Bought ?</th>
      </tr>
      <c:forEach items="${customerLifeCycleBean.userCartList}" var="userCart">
        <tr>
          <td>${userCart.product.name}</td>
          <td><fmt:formatDate value="${userCart.createDate}" type="both"/></td>
          <td><fmt:formatDate value="${userCart.lastDate}" type="both"/></td>
          <td>${userCart.counter}</td>
          <td>${userCart.bought}</td>
        </tr>
      </c:forEach>
    </table>

  </s:layout-component>
</s:layout-render>
