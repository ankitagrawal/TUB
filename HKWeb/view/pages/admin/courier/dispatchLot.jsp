<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add/Edit Dispatch Lot">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="dispatch"/>
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
		Add/Edit Dispatch Lot
	</s:layout-component>

	<s:layout-component name="content">
		<div style="float: left; width:40%">
		<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
			<fieldset class="top_label">
				<legend> Enter Details</legend>
				<s:label name="docketNumber" class="label">Docket Number</s:label>
					<s:text name="dispatchLot.docketNumber" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="courier" class="label">Courier</s:label>
				<s:select name="dispatchLot.courier" class="text">
				<s:option value="">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
					                           label="name"/>
				</s:select>

				<div class="clear"></div>

				<s:label name="zone" class="label">Zone</s:label>
					<s:text name="dispatchLot.zone" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="source" class="label">Source</s:label>
					<s:text name="dispatchLot.source" style="width:200px" class="text"/>

				<div class="clear"></div>


				<s:label name="destination" class="label">Destination</s:label>
					<s:text name="dispatchLot.destination" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="noOfShipmentsSent" class="label">No. of Shipments Sent</s:label>
					<s:text name="dispatchLot.noOfShipmentsSent" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="noOfShipmentsSent" class="label">No. of Shipments Received</s:label>
					<s:text name="dispatchLot.noOfShipmentsReceived" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="noOfMotherBags" class="label">No. of Mother Bags</s:label>
					<s:text name="dispatchLot.noOfMotherBags" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="totalWeight" class="label">Total Weight</s:label>
					<s:text name="dispatchLot.totalWeight" style="width:200px" class="text"/>

				<div class="clear"></div>

				<%--<s:label name="deliveryDate" class="label">Delivery Date</s:label>
					&lt;%&ndash;<s:text name="dispatchLot.deliveryDate" style="width:200px" class="text"/>&ndash;%&gt;
					<s:text class="date_input startDate text" style="width:150px"
												                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="dispatchLot.deliveryDate" id="startDateId"/>

				<div class="clear"></div>
--%>
					<s:submit name="save" value="Save"/>
			</div>

		</s:form>
	</s:layout-component>
</s:layout-render>
