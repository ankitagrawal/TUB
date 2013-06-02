<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link href="<hk:vhostCss/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />
<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>


		<table class="table table-bordered">
			<thead>
				<tr>
					<th style="width: 150px;">Product</th>
					<th>Quantity</th>
					<th>Loyalty Points / Item</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ca.loyaltyProductList}" var="lp">
					<tr>
						<td>
						<c:set var="imageId" value = "${lp.variant.product.mainImageId }" />
						<img src="${hk:getS3ImageUrl(imageSmallSize, imageId)}" alt="${lp.variant.product.name}"/>
						<h8>${lp.variant.product.name}</h8>
						</td>
						<td>${lp.qty}</td>
						<td>${hk:roundNumberForDisplay(lp.points)}</td>
					</tr>
				</c:forEach>
				<tr style="background-color: #f9f9f9;">
					<td><h8>Total Shopping points</h8></td>
					<td colspan="2">
						<div>${hk:roundNumberForDisplay(ca.totalShoppingPoints)} Points</div>
					</td>
				</tr>
			</tbody>
		</table>

		<table class="table table-bordered">
			<tbody>
				<tr>
					<td><h8>Shipping Address</h8></td>
					<td colspan="2" style="background-color: #f9f9f9;">
						<address>
							<strong>${pla.shipmentAddress.name}</strong><br>
							${pla.shipmentAddress.line1}, ${pla.shipmentAddress.line2}<br>
							${pla.shipmentAddress.city}<br>
							${pla.shipmentAddress.state}, ${pla.shipmentAddress.pincode.pincode}<br>
							Ph: ${pla.shipmentAddress.phone}
						</address>
					</td>
				</tr>
			</tbody>
		</table>

		<div class="row">
			<div class="span9"></div>
			<div class="span3">
				<div class="pull-right">
					<s:form
						beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" >
						<s:submit name="confirm" value="Confirm Order" class="btn" id="confirmBtn"/>
					</s:form>
				</div>
			</div>
		</div>
<script type="text/javascript" >
$(document).ready(function() {
	$("#confirmBtn").click(function () {
    	  if (confirm('Confirm your Order ?')) {
			return true;
    	  } else {
    		  return false;
    		  }
    	  });
    });

</script>
	</stripes:layout-component>
</stripes:layout-render>
