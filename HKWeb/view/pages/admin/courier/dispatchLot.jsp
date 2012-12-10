<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
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
		<c:choose>
			<c:when test="${dispatch.dispatchLot.id != null}">
				<h4>Edit Dispatch Lot # ${dispatch.dispatchLot.id}</h4>
			</c:when>
			<c:otherwise>
				<h4>Create New Dispatch Lot</h4>
			</c:otherwise>
		</c:choose>
	</s:layout-component>

	<s:layout-component name="content">
		<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
		<div style="float: left; width:40%">

			<s:hidden name="dispatchLot" value="${dispatch.dispatchLot.id}"/>
			<fieldset class="top_label">
				<legend> Enter Details</legend>
				<s:label name="docketNumber" class="label">Docket Number</s:label>
					<s:text name="dispatchLot.docketNumber" style="width:200px" class="text"/>
				<span class="aster">*</span>

				<div class="clear"></div>

				<s:label name="courier" class="label">Courier</s:label>
				<s:select name="dispatchLot.courier" class="text">
				<s:option value="">-All-</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
					                           value="id"
					                           label="name"/>
				</s:select>
				<span class="aster">*</span>

				<div class="clear"></div>

				<s:label name="zone" class="label">Zone</s:label>
				<s:select name="dispatchLot.zone" class="text">
				<s:option value="null">Select</s:option>
					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
					                           value="id"
					                           label="name"/>
				</s:select>

				<span class="aster">*</span>

				<div class="clear"></div>
				<s:label name="source" class="label">Source</s:label>
					<s:text name="dispatchLot.source" style="width:200px" class="text"/>
				<span class="aster">*</span>

				<div class="clear"></div>


				<s:label name="destination" class="label">Destination</s:label>
					<s:text name="dispatchLot.destination" style="width:200px" class="text"/>
				<span class="aster">*</span>

				<div class="clear"></div>

					<%--<s:label name="noOfShipmentsSent" class="label">No. of Shipments Received</s:label>
										<s:text name="dispatchLot.noOfShipmentsReceived" style="width:200px" class="text"/>

									<div class="clear"></div>--%>

				<s:label name="noOfMotherBags" class="label">No. of Mother Bags</s:label>
					<s:text name="dispatchLot.noOfMotherBags" style="width:200px" class="text"/>

				<div class="clear"></div>

				<s:label name="totalWeight" class="label">Total Weight</s:label>
					<s:text name="dispatchLot.totalWeight" style="width:200px" class="text"/>
				<span class="aster">*</span>

				<div class="clear"></div>

				<s:label name="deliveryDate" class="label">Delivery Date</s:label>
					<s:text class="date_input text" style="width:150px"
					        formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="dispatchLot.dispatchDate"
					        id="deliveryDate"/>
				<span class="aster">*</span>

				<div class="clear"></div>

				<s:label name="remarks" class="label">Remarks</s:label>
					<s:textarea name="dispatchLot.remarks" class="text" rows="2"/>

				<div class="clear"></div>
				<s:label name="noOfShipmentsSent" class="label">No. of Shipments Sent</s:label>
					<s:text name="dispatchLot.noOfShipmentsSent" style="width:200px" class="text" readonly="readonly"/>

				<div class="clear"></div>
					<s:submit name="save" value="Save"/>

		</div>

		<c:if test="${dispatch.dispatchLot.id != null}">
			<div class="reportBox">
				<%--<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">--%>
					<fieldset class="right_label">
						<legend>Update Shipment Details In Dispatch Lot</legend>
						<br>
						<span class="large">(AWB NUMBER) as excel header</span>
						<ul>
								<%--<li>
									   <label>Payment Mode:</label>
									   <s:select name="paymentProcess" class="uploadPaymentMode" style="width: 100">
										   <s:option value="all">-Select-</s:option>
										   <s:option value="cod">COD</s:option>
										   <s:option value="techprocess">Prepaid</s:option>
									   </s:select>
								   </li>--%>
							<li>
								<h3>File to Upload: <s:file name="fileBean" size="30" id="uploadFile"/></h3>

							</li>
							<li>
								<s:submit name="parse" value="Upload Shipment Details"
								          class="requiredFieldValidator"/>
							</li>
						</ul>
					</fieldset>
				<%--</s:form>--%>
			</div>
		</c:if>
			</s:form>
	</s:layout-component>
</s:layout-render>
