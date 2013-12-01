<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction" var="deliveryQueueBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<s:layout-component name="heading">Delivery Awaiting Queue</s:layout-component>
<s:layout-component name="content">
  <s:form beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction" method="get" autocomplete="false">
    <fieldset class="top_label">
      <ul>
        <div class="grouped">
          <li><label>Order ID</label> <s:text name="orderId"/></li>
          <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" class="gatewayOrderId"/></li>
          <li><label>Tracking ID</label> <s:text name="trackingId"/></li>
<%--
          <li>
            <label>Start
              date</label><s:text class="date_input startDate" style="width:150px"
                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
          </li>
          <li>
            <label>End
              date</label><s:text class="date_input endDate" style="width:150px"
                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
          <li>
--%>
          <s:select name="courier" class="courierService">
            <s:option value="">All Couriers</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                       label="name"/>
          </s:select>
          <li>
            <div class="buttons"><s:submit name="searchOrders" value="Search"/></div>
          </li>
        </div>
      </ul>
    </fieldset>
  </s:form>

  <s:form beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction" autocomplete="off">
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${deliveryQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${deliveryQueueBean}"/>
    <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                     shippingOrders="${deliveryQueueBean.shippingOrderList}"/>
    <div>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${deliveryQueueBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${deliveryQueueBean}"/>
    </div>
    <div id="hiddenShippingIds"></div>
    <div style="display:inline;float:right;">
      <s:submit name="markShippingOrderAsDelivered" id="markShippingOrdersAsDelivered"
                value="Mark Orders As Delivered"/></div>
  </s:form>
  <script type="text/javascript">
    $('#markShippingOrdersAsDelivered').click(function() {
      $('.shippingOrderDetailCheckbox').each(function() {
        var shippingOrderDetailCheckbox = $(this);
        var isChecked = shippingOrderDetailCheckbox.attr('checked');
        if (isChecked) {
          $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
        }
      });
      return true;
    });

  </script>
</s:layout-component>
</s:layout-render>


