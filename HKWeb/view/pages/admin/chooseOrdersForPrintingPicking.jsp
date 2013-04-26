<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ChooseOrdersForPrintPickAction" var="printPickBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<%
	int lineItemGlobalCtr = 0;
%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Print Pack Awaiting Queue">
	<s:layout-component name="htmlHead">
		<%

			ShippingOrderStatusService shippingOrderStatusService = ServiceLocatorFactory.getService(ShippingOrderStatusService.class);
			pageContext.setAttribute("statusForPrinting", shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ReadyForProcess));
			pageContext.setAttribute("statusForPicking", shippingOrderStatusService.find(EnumShippingOrderStatus.SO_MarkedForPrinting));
			CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
			pageContext.setAttribute("categoryList", categoryDao.getPrimaryCategories());
		%>

		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

	</s:layout-component>
	<s:layout-component name="heading">Print Pick Queue</s:layout-component>
	<s:layout-component name="content">
		<script type="text/javascript">
			$(document).ready(function() {
				$(".batchPrinting").click(function() {
					var invoiceLinks = document.getElementsByClassName("invoiceLink");
					var personalCareInvoiceLinks = document.getElementsByClassName("personalCareInvoiceLink");
					var len = invoiceLinks.length;
					for (var i = 0; i < len; i++) {
						var j = i + 1;
						//Normal Partial Invoice
						document.getElementById("orderInvoice").src = invoiceLinks[i].href;
						if (document.getElementById("orderInvoice").src != "") {
							alert("Sending copy 1 to printer of " + j + " of  " + len + " orders");
							printInvoice('orderInvoice');
						}
						//Personal Care Partial Invoice
						document.getElementById("orderInvoice").src = personalCareInvoiceLinks[i].href;
						if (document.getElementById("orderInvoice").src != "") {
							alert("Sending copy 2 to printer of " + j + " of  " + len + " orders");
							printInvoice('orderInvoice');
						}
					}
					return true;
				});

				$('#orderSelector').click(function() {
					if ($(this).attr("checked") == "checked") {
						$('.orderCheckBox').each(function() {
							this.checked = true;
						})
					} else {
						$('.orderCheckBox').each(function() {
							this.checked = false;
						})
					}
				});

				function printInvoice(elementId) {
					var getMyFrame = document.getElementById(elementId);
					getMyFrame.focus();
					getMyFrame.contentWindow.print();
				}

			});
		</script>
		<div align="left">
			<c:if test="${printPickBean.category != null}">
				Showing Orders for Category: <strong>${printPickBean.category.displayName}</strong>
			</c:if>
			<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction" event="createCycleCount" style="float:right;">Brands to Exclude/Audit</s:link>
		</div>
		<s:form beanclass="com.hk.web.action.admin.queue.ChooseOrdersForPrintPickAction" method="get" >
		<div align="center">
			<%--<label width="5" style="font-weight:bold;color:red;font-size:1.2em">Brand To Restrict:</label><s:text name="brand" class="brand" />--%>
			Category
			<s:select name="category" value="${printPickBean.category.name}">
                <s:option value="">Any Category</s:option>
				<c:forEach items="${categoryList}" var="category">
					<s:option value="${category.name}">${category.displayName} </s:option>
				</c:forEach>
			</s:select>
			<s:submit name="searchOrdersForPrinting" value="Search By Basket Category" style="font-size:0.9em"/>
		</div>
		<div align="center">
		<label>Courier</label>
            <s:select name="courier">
              <s:option value="">All Couriers</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id"
                                         label="name"/>
            </s:select>
            <s:submit name="searchOrdersForPrinting" value="Search By Courier"
			          style="font-size:0.9em"/>
		
		</div>
		<div align="center">
			SO Gateway Order Id:<s:text name="gatewayOrderId"/>
			BO Gateway Order Id:<s:text name="baseGatewayOrderId"/>
			<s:submit name="searchOrdersForPrinting" value="Search By Gateway OrderId"
			          style="font-size:0.9em"/>
		</div>

		<div align="center">
			<label>Escalation Start
				Date </label><s:text class="date_input startDate" style="width:150px"
				                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>

			<label>End
				Date </label><s:text class="date_input endDate" style="width:150px"
				                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>

			<s:submit name="searchOrdersForPrinting" value="Search By Escalation Date"
			          style="font-size:0.9em"/>

		</div>
            <div align="center">
                <label>Payment Start
                    Date </label><s:text class="date_input startDate" style="width:150px"
                                         formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="paymentStartDate"/>

                <label>Payment
                    Date </label><s:text class="date_input endDate" style="width:150px"
                                         formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="paymentEndDate"/>

                <s:submit name="searchOrdersForPrinting" value="Search By Payment Date"
                          style="font-size:0.9em"/>

            </div>
            <div align="center">
                <label>Target Start
                    Date </label><s:text class="date_input startDate" style="width:150px"
                                         formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startTargetDispatchDate"/>

                <label>Target End
                    Date </label><s:text class="date_input endDate" style="width:150px"
                                         formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endTargetDispatchDate"/>

                <s:submit name="searchOrdersForPrinting" value="Search By Target Date"
                          style="font-size:0.9em"/>

            </div>
		</s:form>

		<s:form beanclass="com.hk.web.action.admin.queue.ChooseOrdersForPrintPickAction" autocomplete="off">
			<%--  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${printPickBean}"/>
				<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${printPickBean}"/>--%>
			<s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
			                 shippingOrders="${printPickBean.shippingOrdersList}"/>
			<s:hidden name="category" value="${printPickBean.category}"/>
			<s:hidden name="baseGatewayOrderId" value="${printPickBean.baseGatewayOrderId}"/>
			<s:hidden name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
			<s:hidden name="courier" value="${printPickBean.courier}"/>
			<s:hidden name="startDate" value="${printPickBean.startDate}"/>
			<s:hidden name="endDate" value="${printPickBean.endDate}"/>
			<div id="hiddenShippingIds"></div>
			<%-- <div>
					<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${printPickBean}"/>
					<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${printPickBean}"/>
				  </div>--%>
			<c:if test="${fn:length(printPickBean.shippingOrdersList) > 0}">
				<c:if test="${printPickBean.shippingOrderStatus == statusForPrinting}">
					<div style="float:left; font-size: 0.9em; margin-top: 5px; margin-left:20px">
						<s:hidden name="shippingOrdersList"  value="${printPickBean.shippingOrdersList}"/>

						<s:submit name="batchPrintOrders" class="batchPrinting" value=" Do Batch Printing"/>
						<s:hidden name="baseGatewayOrderId" value="${printPickBean.baseGatewayOrderId}"/>
						<s:hidden name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
						<s:hidden name="courier" value="${printPickBean.courier}"/>
						<s:hidden name="startDate" value="${printPickBean.startDate}"/>
						<s:hidden name="endDate" value="${printPickBean.endDate}"/>
					</div>
				</c:if>
				<c:if test="${printPickBean.shippingOrderStatus == statusForPicking}">
					<div style="float:left; font-size: 0.9em; margin-top: 7px; margin-left:50px">
						<s:submit name="sendOrdersBackToProcessingQueue" id="sendOrdersBackToProcessingQueue"
						          value="Move orders to processing Queue"/>
						<s:link beanclass="com.hk.web.action.admin.queue.JobCartAction" target="_blank" class="button_orange">
							<s:param name="category" value="${printPickBean.category}"/>
							<s:param name="courier" value="${printPickBean.courier}"/>
							<s:param name="baseGatewayOrderId" value="${printPickBean.baseGatewayOrderId}"/>
							<s:param name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
							<s:param name="startDate" value="${printPickBean.startDate}"/>
							<s:param name="endDate" value="${printPickBean.endDate}"/>
							Print Job Card
						</s:link>
						<s:submit name="clearPickingQueue" value="Job Done - Clear Queue"/>
					</div>
				</c:if>
			</c:if>

		</s:form>

		<iframe id="orderInvoice" name="orderInvoice" src=""
		        style="dispaly:none;visibility:hidden;"></iframe>
		<script type="text/javascript">
			$('#sendOrdersBackToProcessingQueue').click(function() {
				$('.shippingOrderDetailCheckbox').each(function() {
					var shippingOrderDetailCheckbox = $(this);
					var isChecked = shippingOrderDetailCheckbox.attr('checked');
					if (isChecked) {
						$('#hiddenShippingIds').append('<input type="hidden" name="shippingOrdersList[]" value="' + $(this).attr('dataId') + '"/>');
					}
				});
				return true;
			});


		</script>
	</s:layout-component>
</s:layout-render>
