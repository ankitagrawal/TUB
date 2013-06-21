<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.hk.domain.catalog.category.Category"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="com.hk.domain.order.Order"%>
<%@ page import="com.hk.domain.order.CartLineItem"%>
<%@ page import="com.hk.domain.catalog.product.Product"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<s:layout-definition>

	<%
	    if (pageContext.getAttribute("order") != null) {
	        Order order = (Order) pageContext.getAttribute("order");

	        String lineItemString = "";
	        for (CartLineItem cartLineItem : order.getExclusivelyProductCartLineItems()) {
	            if (cartLineItem.getProductVariant() != null && cartLineItem.getProductVariant().getId() != null && cartLineItem.getQty() != null) {
	                lineItemString = lineItemString.concat("prod:").concat(cartLineItem.getProductVariant().getId()).concat(":qty:").concat(cartLineItem.getQty().toString());
	            }

	        }

	        pageContext.setAttribute("lineItemString", lineItemString);
	        pageContext.setAttribute("transactionId", order.getGatewayOrderId());
	        pageContext.setAttribute("city", order.getAddress().getCity());
	        pageContext.setAttribute("saleAmount", order.getAmount());
	        pageContext.setAttribute("modeOfPayment", order.getPayment().getPaymentMode().getName());
	    }
	%>

	<script type="text/javascript"
		src="https://affiliates.tyroodr.com/i_sale_third/10960/${lineItemString}/${transactionId}/${city},${saleAmount},${modeOfPayment}"
		class="tyroo_track"></script>
	<noscript><img
		src="https://affiliates.tyroodr.com/i_track_sale/10960/${lineItemString}/${transactionId}/${city},${saleAmount},${modeOfPayment}"></noscript>
</s:layout-definition>