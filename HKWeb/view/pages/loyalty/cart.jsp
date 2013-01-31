<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction"
	var="ca" />

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
		 
		<script type="text/javascript">
			$(document).ready(function() {
				$('.lineItemQty').blur(function(event) {
					var qty = $(this).val();
					var variant_id = $(this).attr("id");
					var url = $('#update-qty-url').attr("href");
					$.ajax({
						type: 'POST',
						url: url,
						data: {qty:qty, productVariantId: variant_id},
						success: function(resp) {
							$('.total-shopping-points').html(resp.data.totalShoppingPoints);
						}
					});
				});
				$('.lineItemQty').jqStepper(1);
			});
		</script>
		<div style="display: none;">
			<s:link beanclass="com.hk.web.action.core.loyaltypg.CartAction" id="update-qty-url" event="updateQuantity" />
		</div>
		<c:choose>
			<c:when test="${not empty ca.loyaltyProductList}">

				<table class="table table-bordered">
					<thead>
						<tr>
							<th style="width: 150px;">Product</th>
							<th>Quantity</th>
							<th>Loyalty Points/Item</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ca.loyaltyProductList}" var="lp">
							<tr>
								<td><img
									src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lp.variant.product.id}.jpg'
									alt="${lp.variant.product.name}" /> <h8>${lp.variant.product.name}</h8>
								</td>
								<td><input id="${lp.variant.id}" class= "lineItemQty" type=text value="${lp.qty}" style="width:30px;"/></td>
								<td>${lp.points}</td>
							</tr>
						</c:forEach>
						<tr style="background-color: #f9f9f9;">
							<td><h8>Total Shopping points</h8></td>
							<td colspan="2">
								<div class="total-shopping-points">${ca.totalShoppingPoints} Points</div>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="row">
					<div class="span9"></div>
					<div class="span3">
						<div class="pull-right">
							<s:form beanclass="com.hk.web.action.core.loyaltypg.CartAction">
								<s:submit name="checkout" value="Checkout"
									class="btn btn-primary" />
							</s:form>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="row">
					<div class="span12">
						<h4>Cart is Empty</h4>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</stripes:layout-component>
</stripes:layout-render>



