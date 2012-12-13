<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="View Dispatch Lot">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="dispatchLotBean"/>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

		<style type="text/css">
			.text {
				margin-left: 100px;
				margin-top: 20px;
			}

			.label {
				margin-top: 20px;
				float: left;
				margin-left: 10px;
				width: 150px;
			}

		</style>
	</s:layout-component>
	<s:layout-component name="heading">
		<h4>View Dispatch Lot: ${dispatchLotBean.dispatchLot.id}</h4>
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset class="top_label">
			<legend>Details</legend>
			<table>
				<tr>
					<td><label><strong>Docket Number</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.docketNumber}</td>

					<td><label><strong>Courier</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.courier.name}</td>

					<td><label><strong>Zone</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.zone.name}</td>

					<td><label><strong>No. Of Shipments Sent</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.noOfShipmentsSent}</td>
				</tr>

				<tr>
					<td><label><strong>Source</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.source}</td>

					<td><label><strong>Destination</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.destination}</td>

					<td><label><strong>No. of mother bags</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.noOfMotherBags}</td>

					<td><label><strong>No. Of Shipments Received</strong></label></td>
					<td>${dispatchLotBean.dispatchLot.noOfShipmentsReceived}</td>
				</tr>
			</table>
		</fieldset>

		<h4>Shipment Details for Dispatch Lot: ${dispatchLotBean.dispatchLot.id}</h4>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Shipment ID</th>
				<th>Shipping Order</th>
				<th>Gateway Order Id</th>
				<th>AWB Number</th>
				<th>Shipment Status</th>
			</tr>
			</thead>
			<c:forEach items="${dispatchLotBean.dispatchLotShipments}" var="dispatchLotShipment">
				<tr>
					<td>${dispatchLotShipment.shipment.id}</td>
					<td>${dispatchLotShipment.shipment.shippingOrder.id}</td>
					<td>${dispatchLotShipment.shipment.shippingOrder.gatewayOrderId}</td>
					<td>${dispatchLotShipment.shipment.awb.awbNumber}</td>
					<td>${dispatchLotShipment.shipmentStatus}</td>
				</tr>
			</c:forEach>
			<tbody>
		</table>

	</s:layout-component>
</s:layout-render>

