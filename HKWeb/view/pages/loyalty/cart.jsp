<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes"	uri="http://stripes.sourceforge.net/stripes.tld"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />

<link href="<hk:vhostJs/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
		 
		<script type="text/javascript">
			$(document).ready(function() {
				$('.lineItemQty').blur(function(event) {
					var qty = $(this).val();
					var qty_box = $(this);
					var error_message = $(this).parent().parent().find('.error-message');
					var variant_id = $(this).attr("id");
					var url = $('#update-qty-url').attr("href");
					$.ajax({
						type: 'POST',
						url: url,
						data: {qty:qty, productVariantId: variant_id},
						success: function(resp) {
							if(resp.code == "error"){
								qty_box.val(qty-1);
								error_message.slideDown().delay(5000).slideUp();
							}
							else{
								$('.total-shopping-points').html(resp.data.totalShoppingPoints);
							}
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
								<td width="50%">
									<table>
										<tr>
											<td width="20%"><img
													src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lp.variant.product.id}.jpg'
													alt="${lp.variant.product.name}"/>
											</td>
											<td width="80%">
												<h8>${lp.variant.product.name}</h8>
												<ul>
													<c:forEach items="${lp.variant.productOptions}" var="productOption"
													           varStatus="ctr">
													<li style="font-size:12px;">${productOption.name}:${productOption.value}
														</c:forEach>
												</ul>
											</td>
										</tr>

									</table>


								</td>
								<td width="30%">

									<input id="${lp.variant.id}" class="lineItemQty" type=text value="${lp.qty}"
								           style="width:30px;height:24px;"/>
									<br/>
									<span class="error-message" style="font-size:12px; color:red; font-weight:200; margin-left: -30px; display:none">
										Not enough stellar points to increase the quantity.
									</span>
								</td>
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
						<h4>Cart is Empty! </h4> <a title="stellar" class="blue" href="/healthkart/loyaltypg"> Click here</a> to go back to home page.
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</stripes:layout-component>
</stripes:layout-render>



