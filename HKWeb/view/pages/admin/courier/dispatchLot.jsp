<%@ page import="com.hk.constants.courier.EnumDispatchLotStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add/Edit Dispatch Lot">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="dispatch"/>
	<c:set var="dispatchLotGenerated" value="<%=EnumDispatchLotStatus.Generated.getId()%>"/>
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
				<script type="text/javascript">
				 $(document).ready(function(){
					 $('#shipment-upload-button').click(function(event){
						 $('#shipment-upload-button').hide();
					 });
				 });
		</script>
		<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
			<div style="float: left; width: 70%">

				<s:hidden name="dispatchLot" value="${dispatch.dispatchLot.id}"/>
				<fieldset class="top_label">
					<legend> Enter Details</legend>
					<s:label name="docketNumber" class="label">Docket Number</s:label>
						<s:text name="dispatchLot.docketNumber" style="width:200px" class="text"/>
					<span class="aster">*</span>

					<div class="clear"></div>

					<s:label name="courier" class="label">Courier</s:label>
					<s:select name="dispatchLot.courier" class="text">
					<s:option value="">-Select-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="couriersForDispatchLot"
						                           value="id"
						                           label="name"/>
					</s:select>
					<span class="aster">*</span>

					<div class="clear"></div>

					<s:label name="zone" class="label">Zone</s:label>
					<s:select name="dispatchLot.zone" class="text">
					<s:option value="">-Select-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
						                           value="id"
						                           label="name"/>
					</s:select>

					<span class="aster">*</span>

					<div class="clear"></div>
					<s:label name="source" class="label">Source</s:label>
					<s:select name="dispatchLot.source" class="text">
					<s:option value="">-Select-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="sourceAndDestinationListForDispatchLot"
								/>
					</s:select>
					<span class="aster">*</span>

					<div class="clear"></div>


					<s:label name="destination" class="label">Destination</s:label>
					<s:select name="dispatchLot.destination" class="text">
					<s:option value="">-Select-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="sourceAndDestinationListForDispatchLot"
								/>
					</s:select>
					<span class="aster">*</span>

					<div class="clear"></div>

					<s:label name="noOfMotherBags" class="label">No. of Mother Bags</s:label>
						<s:text name="dispatchLot.noOfMotherBags" style="width:200px" class="text"/>

					<div class="clear"></div>

					<s:label name="totalWeight" class="label">Total Weight</s:label>
						<s:text name="dispatchLot.totalWeight" style="width:200px" class="text"/>

					<div class="clear"></div>

					<s:label name="remarks" class="label">Remarks</s:label>
						<s:textarea name="dispatchLot.remarks" class="text"/>

					<div class="clear"></div>
					<s:label name="noOfShipmentsSent" class="label">No. Of Shipments Sent</s:label>
						<s:text name="dispatchLot.noOfShipmentsSent" style="width:200px" class="text" disabled="disabled"/>

					<div class="clear"></div>
						<s:param name="dispatchLot" value="${dispatch.dispatchLot.id}"/>
					<c:if test="${dispatch.dispatchLot.id == null || dispatch.dispatchLot.dispatchLotStatus.id == dispatchLotGenerated}">
						<s:submit name="save" value="Save"/>
					</c:if>

			</div>
			<div style="float: right; width: 30%">
				<c:if test="${dispatch.dispatchLot.id != null && dispatch.dispatchLot.dispatchLotStatus.id == dispatchLotGenerated}">
					<div class="reportBox">
						<c:if test="${dispatch.dispatchLot.noOfShipmentsSent == null || dispatch.dispatchLot.noOfShipmentsSent == 0}">
						<fieldset class="right_label">
							<legend>Update Shipment Details In Dispatch Lot</legend>
							<br>
							<span class="large">(SHIPPERS REFERENCE NUMBER) as excel header</span>
							<ul>
								<li>
									<h3>Shipment Details File to Upload: <s:file name="fileBean" size="30" id="uploadFile"/></h3>
								</li>
								<li>
									<s:submit name="parse" value="Upload Shipment Details" id="shipment-upload-button"/>
									<s:param name="dispatchLot" value="${dispatch.dispatchLot.id}"/>
								</li>
							</ul>
						</fieldset>
						</c:if>
						<fieldset class="right_label">
							<legend>Upload POD for Dispatch Lot</legend>
							<ul>
								<li>
									<h3>Upload POD :<s:file name="uploadDocumentFileBean" size="30"
									                        id="uploadDocument"/></h3>
								</li>
								<li>
									<s:param name="dispatchLot" value="${dispatch.dispatchLot.id}"/>
									<s:submit name="uploadDocument" value="Upload Document"/>
								</li>
							</ul>
						</fieldset>
					</div>
					<div class="clear"></div>
				<c:if test="${dispatch.dispatchLot.noOfShipmentsSent != null && dispatch.dispatchLot.noOfShipmentsSent > 0}">
					<div>
						<br><br>
						<s:submit name="markLotAsInTransit" value="Mark Lot As 'In Transit'" />
					</div>
				</c:if>
				</c:if>
			</div>

		</s:form>
	</s:layout-component>
</s:layout-render>
