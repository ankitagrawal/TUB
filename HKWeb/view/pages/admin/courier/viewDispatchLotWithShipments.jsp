<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
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
				<legend> Enter Details</legend>
	   	<table>
			   <tr>
				   <td><label><strong>Docket Number</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.docketNumber}</td>

				   <td><label><strong>Courier</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.courier.name}</td>

				   <td><label><strong>Zone</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.zone}</td>
			   </tr>

			   <tr>
				   <td><label><strong>Source</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.source}</td>

				   <td><label><strong>Destination</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.destination}</td>

				   <td><label><strong>No. of mother bags</strong></label></td>
				   <td>${dispatchLotBean.dispatchLot.noOfMotherBags}</td>
			   </tr>
	   	</table>
		</fieldset>
		</s:layout-component>
	</s:layout-render>

