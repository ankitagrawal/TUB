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

    <fieldset class="top_label">
			<s:form beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction" method="get" autocomplete="false">
      <ul>
        <div class="grouped">
          <li><label>Order ID</label> <s:text name="orderId"/></li>
          <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" class="gatewayOrderId"/></li>
          <li><label>Tracking ID</label> <s:text name="trackingId"/></li>
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
          <s:select name="courier" class="courierService">
            <s:option value="">All Couriers</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                       label="name"/>
          </s:select>
          </li>
          <li>
                <label>Zone:</label>
                <s:select name="zone">
                    <s:option value="null">Select</s:option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
                                               value="id"
                                               label="name"/>
                </s:select>
          </li>

          <li>
            <div class="buttons"><s:submit name="searchOrders" value="Search"/></div>
          </li>
        </div>
      </ul>
			</s:form>
    </fieldset>

<%--
	<fieldset class="top_label">
		<s:form beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction" method="get" autocomplete="false">
			<ul>
				<div class="grouped">
					<li><label>Order ID</label> <s:text name="orderId"/></li>
					<li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" class="gatewayOrderId"/></li>
					<li><label>Tracking ID</label> <s:text name="trackingId"/></li>
						&lt;%&ndash;
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
						&ndash;%&gt;
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
		</s:form>
	</fieldset>
--%>

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
      <div style="float:left; font-size: 0.9em; margin-top: 5px; margin-left:20px">
          <s:hidden name="shippingOrderList" value="${deliveryQueueBean.shippingOrderList}"/>

          <s:submit name="batchPrintOrders" class="batchPrinting" value=" Do Batch Printing"/>
          <s:hidden name="baseGatewayOrderId" value="${deliveryQueueBean.orderId}"/>
          <s:hidden name="gatewayOrderId" value="${deliveryQueueBean.gatewayOrderId}"/>
          <s:hidden name="courier" value="${deliveryQueueBean.courier}"/>
          <s:hidden name="startDate" value="${deliveryQueueBean.startDate}"/>
          <s:hidden name="endDate" value="${deliveryQueueBean.endDate}"/>
      </div>
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

    $(".batchPrinting").click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            shippingOrderDetailCheckbox.attr("checked", true);
        });
        var invoiceLinks = document.getElementsByClassName("accountingInvoiceLink");
        //var personalCareInvoiceLinks = document.getElementsByClassName("personalCareInvoiceLink");
        var len = invoiceLinks.length;
        for (var i = 0; i < len; i++) {
            var j = i + 1;
            //Normal Partial Invoice
            document.getElementById("orderInvoice").src = invoiceLinks[i].href;
            if (document.getElementById("orderInvoice").src != "") {
                alert("Sending copy 1 to printer of " + j + " of  " + len + " orders");
                printInvoice('orderInvoice');
            }
            /*//Personal Care Partial Invoice
             document.getElementById("orderInvoice").src = personalCareInvoiceLinks[i].href;
             if (document.getElementById("orderInvoice").src != "") {
             alert("Sending copy 2 to printer of " + j + " of  " + len + " orders");
             printInvoice('orderInvoice');
             }*/
        }
        return true;
    });

  </script>
</s:layout-component>
</s:layout-render>


