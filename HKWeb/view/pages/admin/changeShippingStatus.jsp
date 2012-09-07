<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit shipment details">

	<s:useActionBean beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction" var="csda"/>
	<c:set var="delivered" value="<%=EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus()%>"/>
	<c:set var="lost" value="<%=EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus()%>"/>
	<s:layout-component name="heading">
		Change Shipping Order Status
	</s:layout-component>


	<s:layout-component name="htmlHead">

		<link href="${pageContext.request.contextPath}/css/" rel="stylesheet" type="text/css"/>
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

			.button {
				box-shadow: 5px 5px 5px rgba(68, 68, 68, 0.6);
				background-color: darkgreen;
				color: white;
				font-size: inherit;
				display: inline;
				font-size: 1.2em;
				font-weight: bold;
				margin-bottom: 0px;
				min-width: 4em;
				padding: 4px 20px 3px 12px;
				font-family: serif;;
			}

		</style>

	</s:layout-component>
	<s:layout-component name="content">
		<div class="error" style="background-color:salmon; width:380px; display:none;">

		</div>

		<div>
			<div style="float: left; width:40%">
				<s:form beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">
					<fieldset class="top_label">
						<legend> Search Shipping Order</legend>
						<s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>
						<s:text name="gatewayOrderId" style="width:170px" class="text"/> <br/>

						<div class="clear"></div>
						<div style="margin-top:15px;"></div>
						<s:submit name="search" value="Search Order"/>

					</fieldset>
				</s:form>
			</div>
			<div class="clear"></div>
			<div style="margin-top:40px;"></div>
			<div style="float: left; width:40%">
				<c:if test="${csda.visible == true}">
					<s:link event="save" beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction"
					        class="button">Delivered
						<s:param name="originalShippingOrderStatus" value="${csda.shippingOrder.orderStatus}"/>
						<s:param name="shippingOrder.orderStatus" value="${delivered}"/>
						<s:param name="shippingOrder" value="${csda.shippingOrder}"/>

					</s:link>
					<s:link event="save" beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction"
					        class="button">Lost
						<s:param name="originalShippingOrderStatus" value="${csda.shippingOrder.orderStatus}"/>
						<s:param name="shippingOrder.orderStatus" value="${lost}"/>
						<s:param name="shippingOrder" value="${csda.shippingOrder}"/>
					</s:link>

				</c:if>

			</div>
		</div>

	</s:layout-component>
</s:layout-render>















