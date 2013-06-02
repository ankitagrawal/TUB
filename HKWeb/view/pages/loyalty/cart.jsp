<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes"	uri="http://stripes.sourceforge.net/stripes.tld"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />

<link href="<hk:vhostCss/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootbox.js"></script>

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
								error_message.addClass('errorMessage').slideDown().delay(3000).slideUp();
							}
							else{
								var rowCount = $("#itemRows").val();
								if (rowCount ==="0") {
									window.location.reload();
								} else {							
									if ($('.total-shopping-points').html() === (resp.data.totalShoppingPoints + " Points")) {
										qty_box.val(qty-1);
									}
									$('.total-shopping-points').html(resp.data.totalShoppingPoints + " Points");
								}
								
							}
						}
					});
				});
				
				$('.removeLink').click(function(){
					var lineItemQty =  $(this).parent().find('.lineItemQty');
					var currentQty = parseInt(lineItemQty.val());
					lineItemQty.val(0); 
					$(this).parent().parent().hide();
					lineItemQty.trigger('blur');
					var rowCount = $("#itemRows").val();
					$("#itemRows").val(rowCount-1);
				});
	
				$('.plus').click(function(){
					var lineItemQty =  $(this).parent().find('.lineItemQty');
					var currentQty = parseInt(lineItemQty.val());
					lineItemQty.val(currentQty + 1); 
					lineItemQty.trigger('blur');
				});
				
				$('.minus').click(function(){
					var lineItemQty =  $(this).parent().find('.lineItemQty');
					var currentQty = parseInt(lineItemQty.val());
					if(currentQty > 1) {
						lineItemQty.val(currentQty-1); 
						lineItemQty.trigger('blur');
					}
				});
			});
			
			 
			
		     
		</script>
 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>


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
						<s:form name="deleteForm" beanclass="com.hk.web.action.core.loyaltypg.CartAction" event="emptyCart">
						<s:hidden name="emptyCart" value="emptyCart"/>
						</s:form>
							<input type=text id="itemRows" value="${fn: length(ca.loyaltyProductList)}"  style="display:none;"/>
						
						<c:forEach items="${ca.loyaltyProductList}" var="lp">
							<tr>
								<td width="50%">
									<table>
										<tr>
											<td width="20%">
											<c:set var="imageId" value = "${lp.variant.product.mainImageId }" />
											<img src="${hk:getS3ImageUrl(imageSmallSize, imageId)}" alt="${lp.variant.product.name}"/>
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
								<td width="30%" style="text-align:center;">
									<span class="minus" style="cursor:pointer;"><img src="<hk:vhostCss/>/pages/loyalty/resources/images/minus.png" style="height: 20px; width:20px;"></span>
									&nbsp;&nbsp;
									<input id="${lp.variant.id}" class="lineItemQty" type=text value="${lp.qty}" maxlength="3"
								           style="width:30px;height:24px;" disabled="disabled"/>
								     &nbsp;&nbsp;
								     <span class="plus" style="cursor:pointer;"><img src="<hk:vhostCss/>/pages/loyalty/resources/images/plus.png" style="height: 20px; width:20px;"></span>
								        <p class="removeLink">(Remove)</p>
									<br/>
									<span class="error-message" style="display:none">
										Not enough stellar points to increase the quantity.
									</span>
								</td>
								<td>${hk:roundNumberForDisplay(lp.points)}</td>
							</tr>
						</c:forEach>
						<tr style="background-color: #f9f9f9;">
							<td><h8>Total Shopping points</h8></td>
							<td colspan="2">
								<div class="total-shopping-points">${hk:roundNumberForDisplay(ca.totalShoppingPoints)} Points</div>
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
						<h4>Cart is Empty! </h4> <a title="stellar" class="blue" href="${pageContext.request.contextPath}/loyaltypg"> Click here</a> to go back to shop something.
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</stripes:layout-component>
</stripes:layout-render>



