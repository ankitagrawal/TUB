<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyFiles/css/bootstrap.css" rel="stylesheet">
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
		<div class="row">
			<div class="span12">
				Your order ID is <strong>${pla.order.gatewayOrderId}</strong>
			</div>
		</div>
		
		<c:if test="${empty pla.errorMessage}">
			<div class="row">
				<div class="span4"></div>
				<div class="span4">
					<h4>Order Processed Successfully</h4>
				</div>
				<div class="span4"></div>
			</div>
		</c:if>
		<c:if test="${not empty pla.errorMessage}">
			<div class="row">
				<div class="span12">
					<div class="alert alert-error">
						<strong>Couldn't process the order!&nbsp;&nbsp;&nbsp;</strong>
						<span>${pla.errorMessage}</span>
					</div>
				</div>
			</div>
		</c:if>
		<hr>
	</stripes:layout-component>
</stripes:layout-render>

