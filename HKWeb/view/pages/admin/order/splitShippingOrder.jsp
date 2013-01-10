<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.SplitShippingOrderAction" var="splitShippingOrderActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  </s:layout-component>
  <s:layout-component name="heading">Split Shipping Order (for Partial Esc.)</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.shippingOrder.SplitShippingOrderAction">
      <table width="80%" class="align_top">
        <thead>
        <tr>
          <th>Product Variant</th>
          <th>Quantity</th>
          <th>Select Items For New SO</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${splitShippingOrderActionBean.shippingOrder.lineItems}" var="lineItem" varStatus="ctr">
          <c:set value="${lineItem.cartLineItem.productVariant}" var="productVariant"/>
          <c:set value="${productVariant.product}" var="product"/>
              <tr>
                <td>${product.name}<br/>${productVariant.optionsCommaSeparated}
                    <c:if test="${product.dropShipping}">&nbsp; ( DS) </c:if>
                    <c:if test= "${!product.dropShipping}">&nbsp; ( AS)  </c:if>
                </td>
                <td>${lineItem.qty}</td>
                <td>
                  <s:checkbox name="lineItems[${ctr.index}]" value="${lineItem.id}"/>                     
                </td>
              </tr>
        </c:forEach>
        </tbody>
      </table>
      <s:hidden name="shippingOrder" value="${splitShippingOrderActionBean.shippingOrder}"/>
      <s:submit name="splitShippingOrder" value="Split Shipping Order"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
