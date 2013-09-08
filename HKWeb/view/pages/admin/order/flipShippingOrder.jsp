<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.warehouse.Warehouse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.constants.warehouse.EnumWarehouseType" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" var="soaActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <%
    WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
    List<Warehouse> whList = warehouseDao.getAllWarehouses(Arrays.asList(EnumWarehouseType.Online_B2B.getId()), null, Boolean.TRUE);
   whList.remove(soaActionBean.getShippingOrder().getWarehouse());
    pageContext.setAttribute("whList", whList);
  %>
  <s:layout-component name="heading">Flip Warehouse - SO#${soaActionBean.shippingOrder.id}, WH=${soaActionBean.shippingOrder.warehouse.identifier}</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction">
      <table width="80%" class="align_top">
        <thead>
        <tr>
          <th>Product Variant</th>
          <th>Quantity</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${soaActionBean.shippingOrder.lineItems}" var="lineItem" varStatus="ctr">
          <c:set value="${lineItem.cartLineItem.productVariant}" var="productVariant"/>
          <c:set value="${productVariant.product}" var="product"/>
              <tr>
                <td>${product.name}
                </td>
                <td>${lineItem.qty}</td>
              </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td><b>Select a WH: </b></td>
            <td>
                <s:select name="warehouseToUpdate" style="height:30px;font-size:1.2em;padding:1px;">
                    <s:option value="0">-None-</s:option>
                    <c:forEach items="${whList}" var="wh">
                        <s:option value="${wh.id}">${wh.identifier}</s:option>
                    </c:forEach>
                </s:select>
            </td>
        </tr>
        </tfoot>
      </table>

      <s:hidden name="shippingOrder" value="${soaActionBean.shippingOrder}"/>
      <s:submit name="flipWarehouse" value="Flip Warehouse"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
