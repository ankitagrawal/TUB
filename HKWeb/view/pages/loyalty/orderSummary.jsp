<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<link href="<hk:vhostJs/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />
<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>
<%
boolean isSecure = pageContext.getRequest().isSecure();
pageContext.setAttribute("isSecure", isSecure);
%>

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
						<img src="${hk:getS3ImageUrl(imageSmallSize, imageId,isSecure)}" alt="${lp.variant.product.name}"/>
						<h8>${lp.variant.product.name}</h8>
						</td>
						<td>${lp.qty}</td>
						<td>${lp.points}</td>
					</tr>
				</c:forEach>
				<tr style="background-color: #f9f9f9;">
					<td><h8>Total Shopping points</h8></td>
					<td colspan="2">
						<div>${ca.totalShoppingPoints} Points</div>
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
	alert("Page ready.");
    $("#confirmBtn").click(function () {
    	  if (confirm('Confirm your Order ?')) {
			return true;
    	  } else {
    		  return false;
    		  }
    	  }
    });

</script>
	</stripes:layout-component>
</stripes:layout-render>
