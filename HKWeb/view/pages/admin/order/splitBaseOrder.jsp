<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="mhc.service.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.order.SplitBaseOrderAction" var="splitBaseOrderAction"/>
<%
  WarehouseService warehouseService = InjectorFactory.getInjector().getInstance(WarehouseService.class);
  pageContext.setAttribute("warehouses", warehouseService.getAllWarehouses());
  pageContext.setAttribute("corporateOffice", warehouseService.getCorporateOffice());
%>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  </s:layout-component>
  <s:layout-component name="heading">Split Base Order Manually</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.order.SplitBaseOrderAction">
      <table width="80%" class="align_top">
        <thead>
        <tr>
          <th>Product Variant</th>
          <th>Quantity</th>
          <th>Warehouse</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${hk:getProductLineItems(splitBaseOrderAction.baseOrder)}" var="cartLineItem" varStatus="ctr">
          <c:set value="${cartLineItem.productVariant}" var="productVariant"/>
          <c:set value="${productVariant.product}" var="product"/>
          <c:choose>
            <c:when test="${product.service}">
              <tr>
                <td>${product.name}<br/>${productVariant.optionsCommaSeparated}
                </td>
                <td>${cartLineItem.qty}</td>
                <td>
                   ${corporateOffice.name}
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <tr>
                <td>${product.name}<br/>${productVariant.optionsCommaSeparated}
                </td>
                <td>${cartLineItem.qty}</td>
                <td>
                  <select name="cartLineItemWarehouseMap[${cartLineItem.id}]">
                    <c:forEach items="${warehouses}" var="warehouse">
                      <option value="${warehouse.id}">${warehouse.city}</option>
                    </c:forEach>
                  </select>
                </td>
              </tr>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        </tbody>
      </table>
      <s:hidden name="baseOrder" value="${splitBaseOrderAction.baseOrder}"/>
      <s:submit name="splitBaseOrder" value="Split Order"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
