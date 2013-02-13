<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatusDelivrd" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatusRTO_instantiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusSO_returned" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search Replacement Order">
	<s:useActionBean beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction"
	                 var="replacementOrderBean"/>
	<s:layout-component name="heading">
		Search Replacement Orders for shipping order
	</s:layout-component>

	<s:layout-component name="content">

		<fieldset class="right_label">
			<s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
				<label>Search Shipping Order</label>
				<br/><br/>
				<label>Shipping order id: </label>
				<s:text name="shippingOrderId" id="shippingOrderIdText" style="width:200px;"/>
				<br/>
                <br/>
	            <label>Gateway order id: </label>
	            <s:text name="gatewayOrderId" id="shippingOrderIdText" style="width:200px;"/>
				<br/>
				<br/>
				<s:submit name="searchReplacementOrders" value="Search"/>
			</s:form>
		</fieldset>

		<c:choose>
			<c:when test="${!empty replacementOrderBean.replacementOrderList}">
				<table>
				<th>S.No</th>
				<th>Replacement Order Id</th>
				<th>Replacement Order<br/> Gateway order ID</th>
				<th>Is RTO</th>
				<th>Actions</th>
					<c:forEach items="${replacementOrderBean.replacementOrderList}" var="replacementOrder" varStatus="ctr">
					<tr>

						<td>${ctr.index+1}</td>
						<td>${replacementOrder.id}</td>
						<td>${replacementOrder.gatewayOrderId}</td>
						<td>
							<%--${replacementOrder.rto}--%>
							<c:choose>
								<c:when test="${replacementOrder.rto}" >
									Yes
								</c:when>
								<c:otherwise>
									No
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" target="_blank" event="searchShippingOrder">
								<s:param name="shippingOrderId" value="${replacementOrder.id}" />
								<s:param name="shippingOrderGatewayId" value="${replacementOrder.gatewayOrderId}" />View details
							</s:link>
						</td>
					</tr>

					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>

			</c:otherwise>
		</c:choose>

	</s:layout-component>
</s:layout-render>