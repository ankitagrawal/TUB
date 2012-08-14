<%--
<%@ page import="com.hk.constants.EnumLineItemType" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.EnumLineItemStatus" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ShipmentEmailAwaitingQueueAction" var="shipmentQueueBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumLineItemType.Product.getId()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipped and Shipment Email Awaiting Queue">

<s:layout-component name="heading">Shipped but Shipment Email Awaiting Queue</s:layout-component>
<s:layout-component name="content">

  <fieldset class="top_label">
    <ul>
      <div class="grouped grid_6">
        <s:form beanclass="com.hk.web.action.admin.queue.ShipmentEmailAwaitingQueueAction" method="get" autocomplete="false">
          <li><label>Order ID</label> <s:text name="orderId"/></li>
          <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId"/></li>
          <li>
            <label>Courier</label>
            <s:select name="courier" class="courierService">
              <s:option value="">All Couriers</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
            </s:select>
          </li>
          <li>
            <div class="buttons"><s:submit name="searchOrders" value="Search"/></div>
          </li>
        </s:form>
        <s:form beanclass="com.hk.web.action.admin.queue.ShipmentEmailAwaitingQueueAction" target="_blank">
          <div class="buttons"><s:submit name="generateCourierReport" value="Download Courier Excel"/></div>
        </s:form>

        <s:link class="" beanclass="com.hk.web.action.admin.order.UpdateOrderStatusAndSendEmailAction">Send shipping emails<br/><span class="sml gry">(and updated order status)</span></s:link>


      </div>
    </ul>

  </fieldset>

  <s:form beanclass="com.hk.web.action.admin.queue.ShipmentEmailAwaitingQueueAction" autocomplete="off">

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                     shippingOrders="${shipmentQueueBean.orderList}"/>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  </s:form>
</s:layout-component>
</s:layout-render>
--%>
