<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />
<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">

		<table class="table table-bordered">
			<thead>
				<tr>
					<th style="width: 150px;">Product</th>
					<th>Quantity</th>
					<th>Loyalty Points</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ca.loyaltyProductList}" var="lp">
					<tr>
						<td><img
							src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lp.variant.product.id}.jpg'
							alt="${lp.variant.product.name}" /> <h8>${lp.variant.product.name}</h8>
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
					<td><h8>Shipping Addfress</h8></td>
					<td colspan="2" style="background-color: #f9f9f9;">
						<address>
							<strong>${pla.selectedAddress.name}</strong><br>
							${pla.selectedAddress.line1}, ${pla.selectedAddress.line2}<br>
							${pla.selectedAddress.city}<br>
							${pla.selectedAddress.state}, ${pla.selectedAddress.pin}<br>
							<abbr title="Phone">P:</abbr> ${pla.selectedAddress.phone}
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
						beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction">
						<s:submit name="confirm" value="Confirm Order" class="btn btn-primary" />
					</s:form>
				</div>
			</div>
		</div>
	</stripes:layout-component>
</stripes:layout-render>