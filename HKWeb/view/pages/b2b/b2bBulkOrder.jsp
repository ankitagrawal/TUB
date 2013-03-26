<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.hk.constants.core.PermissionConstants"%>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ include file="/includes/_taglibInclude.jsp"%>
<%@ include file="/layouts/_userData.jsp"%>

<%
	HttpServletResponse res = (HttpServletResponse) pageContext
			.getResponse();
%>
<style>
.b2bTable {
	position: relative;
	top: 6px;
	width: auto;
	height: auto;
	clear: both;
	float: left;
	margin-bottom: 12px;
	margin-right: 50px;
	margin-left: 50px;
}

.b2bDiv {
	position: relative;
	clear: both;
	margin-left: 15px;
	margin-right: 15px;
	top: 5px;
	height: auto;
	margin-bottom: 17px;
	padding: 25px;
}

.b2bTableInput {
	position: relative;
	margin-bottom: 15px;
	width: 65% !important;
}

.b2bDiv th {
	font-weight: bold !important;
}
</style>


<s:layout-render name="/layouts/b2bLayout.jsp">
	<s:useActionBean
		beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction" var="atc" />
	<s:useActionBean
		beanclass="com.hk.web.action.core.b2b.B2BBulkOrderAction" var="pa" />

	<c:set var="poCancelled"
		value="<%=EnumPurchaseOrderStatus.Cancelled.getId()%>" />
	<c:set var="poApproved"
		value="<%=EnumPurchaseOrderStatus.Approved.getId()%>" />
	<c:set var="poPlaced"
		value="<%=EnumPurchaseOrderStatus.SentToSupplier.getId()%>" />
	<c:set var="poReceived"
		value="<%=EnumPurchaseOrderStatus.Received.getId()%>" />
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/css/jquery.lightbox-0.5.css"
			media="screen" />
		<script type="text/javascript">
		
		

		var productList = [];
		$(document).ready(function () {
			var totalPayableAmount=0.0;
			$('.totalPrice').each(function(){
				totalPayableAmount=totalPayableAmount+parseFloat($(this).html());
			});
			$("#totalPayableAmount").html(totalPayableAmount);
			var counter=$("#indexCounter").val();
		    $('.addRowButton').click(function () {
		    	if(isNaN(counter)){
		    		counter=0;
		    	}
		        ++counter;
				var index=counter-1;
		        var newRowHtml =
		            '<tr class="bodyTr">' +
		            '<td>' + counter +
		            '</td>' +
		            '<td><input name="b2bProductList[' + index+ '].productId" class="variant b2bTableInput" /></td>' +
		            '<td align="center"><span id="pvDetails" class="pvDetails"></span></td>' +
		            '<td><div class="img48" style="vertical-align:top;"></div></td>'+
		            '<td align="center"><span id="mrp" class="mrp"/></td>' +
		            '<td><input name="b2bProductList[' + index + '].quantity" class="qty b2bTableInput" value="0"/></td>' +		            
		            '<td align="center"><label id="totalPrice" class="totalPrice b2bTableInput">0</label></td>' +
		            '</tr><tr height="10"></tr>';
		        $('#poTable').append(newRowHtml);
		        return true;
		        });
		   
			$('.qty').live("blur", function () {
								var row = $(this).parent().parent(); 
								var quantity = parseFloat(row.find('.qty').val());
								var price = parseFloat(row.find(".mrp").html());
								if (quantity != null && quantity >= 0 && !isNaN(price)) {
									var totalPrice = parseFloat(quantity * price);
									row.find(".totalPrice").html(totalPrice);
								} else {
									row.find(".totalPrice").html(0);
								}
								
								var productVariantId = row.find('.variant').val();
								updateTotal('.totalPrice', "#totalPayableAmount", productVariantId, quantity);
						});

						$('.variant').live("blur", function () {
							
								$("#variantDetailsInavlid").html('');
								var row = $(this).parent().parent();
								var productVariantId = row.find('.variant').val();
								var self1 = row.find(".pvDetails");
								var self2 = row.find(".mrp");
								var self3 = row.find(".totalPrice");
								var self4 = row.find(".qty");
								var imgDiv = row.find(".img48");

								$.getJSON($('#pvInfoLink').attr('href'), {
									productVariantId : productVariantId
								},

								function(res) {
									if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
	                    	
										$('input[type="submit"]').attr('disabled','disabled');
										var error = false;
										var variantName = res.data.product.toCapitalCase();
										var options = res.data.options.toCapitalCase();
										self1.html(variantName + "</br>" + "<em>" + options + "</em>");
										self2.html(res.data.variant.b2bPrice);
										self4.val(0);
										var path = "${pageContext.request.contextPath}";
										var url = path + res.data.imageUrl;
										imgDiv.html('<img class="prod48" src="' + url + '" />');
										row.find('.variant').attr("title", "");
										row.find('.variant').css("background-color", "");
										if (res.data.variant.outOfStock) {
										row.find('.variant').css("background-color", "#FF0000");
										row.find('.variant').attr("title", "Product Out of Stock");
										$("#variantDetailsInavlid").html('<h4>'+"Out Of Stock"+'</h4>');
										error=true;
										} 
										
										if(res.data.variant.b2bPrice==null){
											row.find('.variant').css("background-color", "#FF0000");
											row.find('.variant').attr("title", "B2BPrice Not Found");
											$("#variantDetailsInavlid").html('<h4>'+"B2BPrice not found"+'</h4>');
											error=true;
										}
										if (res.data.inventory == 0) {
										row.find('.variant').css("background-color", "#FF0000");
										row.find('.variant').attr("title", "Inventory Not Found");
										$("#variantDetailsInavlid").html('<h4>'+"Inventory Not Found"+'</h4>');
										error=true;
										}
										if((res.data.variant.outOfStock)&& (res.data.inventory == 0)){
										row.find('.variant').css("background-color", "#FF0000");
										row.find('.variant').attr("title", "Inventory Not Found and out of stock also");
										error=true;
										}
										if(!error){
											row.css('background-color', '#F2F7FB');
											 $('input[type="submit"]').removeAttr('disabled');
											 $("#variantDetailsInavlid").html('');
										}

									updateTotal('.totalPrice', "#totalPayableAmount", productVariantId, 0);
								} else {
									
									row.find('.variant').css("background-color", "");
									row.find('.variant').attr("title", "");
									$('input[type="submit"]').removeAttr('disabled');
									if(productVariantId!=""){
										row.find('.variant').attr("title", "Invalid Input");
										row.find('.variant').css("background-color", "#FF0000");
										$('input[type="submit"]').attr('disabled','disabled');
									}
									self1.html('');
									self2.html('');
									self3.html(0);
									self4.val(0);
									imgDiv.html("");
									$("#variantDetailsInavlid").html('<h4>' + res.message + '</h4>');
								}

							});
						});
					});

			function Poduct(id, qty) {
				this.id = id;
				this.qty = qty;
			}

			function lookup(lookupId) {
				for ( var i = 0, len = productList.length; i < len; i++) {
					if (productList[i].id === lookupId)
						return true;
				}
				return false;
			}

			function update(updateId, updateQty) {
				for ( var i = 0, len = productList.length; i < len; i++) {
					if (productList[i].id === updateId) {
						productList[i].qty = updateQty;
					}
				}
			}

			function updateTotal(fromTotalClass, toTotalClass, a, b) {
				var total = 0;
				$.each($(fromTotalClass), function(index, value) {
					var eachRow = $(value);
					var eachRowValue = eachRow.html().trim();
					total += parseFloat(eachRowValue);
				});

				if (!lookup(a)) {
					if (b > 0) {
						productList.push({
							id : a,
							qty : b
						});
					}
				} else {
					update(a, b);

				}

				$(toTotalClass).html(total);

			};

			String.prototype.toCapitalCase = function() {
				var re = /\s/;
				var words = this.split(re);
				re = /(\S)(\S+)/;
				for (i = words.length - 1; i >= 0; i--) {
					re.exec(words[i]);
					words[i] = RegExp.$1.toUpperCase() + RegExp.$2.toLowerCase();
				}
				return words.join(' ');
			}
		</script>
	</s:layout-component>
	<s:layout-component name="checkoutStep">
		<div style="display: none">
			<s:link beanclass="com.hk.web.action.core.b2b.B2BBulkOrderAction"
				id="pvInfoLink" event="getPVDetails"></s:link>
		</div>
		<s:form body="center" accept-charset="UTF-8" class="addToCartForm"
			beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction">
			<div align="center">
				<label style="font-weight: bold;"> Add Your Orders Here</label>
			</div>

			<div id="b2bTable" class="b2bDiv">
				<table border="1" width="100%">

					<tr height="50px" bgcolor="#EEE8CD">
						<th align="left" width="10%">Serial No.</th>
						<th align="left" width="18%">Variant ID</th>
						<th align="left" width="20%">Product Detail</th>
						<th align="left" width="17%">Product Image</th>
						<th align="left" width="10%">B2B Price</th>
						<th align="left" width="13%">Quantity</th>
						<th align="left" width="12%">Total</th>
					</tr>

					<tbody id="poTable">
						<c:if test="${(pa.cartLineItems)!=null}">
							<c:forEach items="${pa.cartLineItems}" var="lineItem"
								varStatus="item">
								<tr class="bodyTr" style="background-color: #F2F7FB">
									<td>${item.count}.</td>
									<td><input
										name="b2bProductList[${item.count-1}].productId"
										class="variant b2bTableInput" readonly="readonly" type="text"
										value="${lineItem.productVariant.id}" /></td>
									<td align="center"><span id="pvDetails" class="pvDetails">${lineItem.productVariant.product.name}
											<br> <em>
												${lineItem.productVariant.optionsCommaSeparated} </em>
									</span></td>
									<td><div class="img48" style="vertical-align: top;">
											<img class="prod48"
												src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${lineItem.productVariant.product.id}.jpg" />
										</div></td>
									<td align="center"><span id="mrp" class="mrp">${lineItem.productVariant.b2bPrice}</span></td>
									<td><input name="b2bProductList[${item.count-1}].quantity"
										class="qty b2bTableInput" value="${lineItem.qty}" /></td>
									<td align="center"><label id="totalPrice"
										class="totalPrice b2bTableInput">${lineItem.productVariant.b2bPrice*lineItem.qty}</label></td>
								</tr>
								<tr height="10"></tr>
								<c:set var="newIndex" value="${item.count}" scope="page" />
							</c:forEach>
						</c:if>
						<s:hidden id="indexCounter" name="indexCounter"
							value="${newIndex}" />
					</tbody>
				</table>
			</div>
			<br />
			<div class="left" style="position: relative; float: left; left: 15px">
				<span id="variantDetailsInavlid"></span> <a href="#"
					class="addRowButton" style="font-size: 1.2em">Add new row</a>
			</div>
			<div
				style="text-align: center; font-size: 10px; font-weight: bold; left: 36%;">
				<h1>
					<span class="special"> you pay </span> <br> <strong>
						<span id="summaryGrandTotalPayable">Rs: <label
							id="totalPayableAmount" style="font-weight: bold;"></label></span>
					</strong>
				</h1>

			</div>
			<div style="position: relative; float: right; top: -45px; right: 12%">
				<span class="special"> C-Form </span>
				<c:choose>
					<c:when test="${(pa.cFormAvailable)}">
						<span><input type="checkbox" name="cFormAvailable"
							id="cFormAvailable" value="${(pa.cFormAvailable)}"></input></span>
					</c:when>
					<c:otherwise>
						<span><input type="checkbox" name="cFormAvailable"
							id="cFormAvailable"></input></span>
					</c:otherwise>
				</c:choose>

			</div>

			<div style="position: relative; float: right; top: -33px">
				<input type="submit" name="addToCart" value="Save To Cart"
					class="addToCartButton cta button_green" />

			</div>

		</s:form>


	</s:layout-component>
	<s:layout-component name="endScripts">
		<script type="text/javascript">
		var validateCheckbox;
		$(document).ready(function () {
			
			$('.addToCartForm').ajaxForm({dataType:'json', data:params, success:_addToCart});
			
          /*  function validate()
           {
        	   var result = true;
				$('#poTable  > .bodyTr').each(
						function() {
							
								if ($(this).find(".totalPrice").val() ==0
										|| isNaN($(this).find(".qty").val())) {
									result = false;
								}
								if ($(this).find(".pvDetails").text() == "") {
									result = false;
								}
								if(($(this).find(".totalPrice").val() ==0)&&
										($(this).find(".pvDetails").text() == "")){
									result = true;
								}
							
						});

				if (result)
					return true;
				else
					alert('correct the input values | check your ID or Qty field | Or Just Clear the row');
				return false;

           } */
			var params = {};
			params.productReferrerId =null;
			function _addToCart(res1) {
				if (res1.code == '<%=HealthkartResponse.STATUS_OK%>') {
					$('.message .line1').html("Your cart has been updated");
					$('#productsInCart').html(res1.data.itemsInCart);
					if(res1.data.itemsInCart > 0){
						$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart.png");
					}else{
						$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart_empty.png");
					}
					$('.progressLoader').hide();

					/* show_message();
                    $('#gulal').show();
                    $(this).parents().find('.progressLoader').show();
					$(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
					$(this).hide(); */
					
					alert('Added To Cart');
				}
				else if(res1.code == "null_exception"){
					alert(res1.message);
				}
                else if(res1.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                	
                	if(res1.data.notAvailable!=null){
                		alert("Not available");
                	}
                	else{
                   alert("Warning: Some of the given products is out of stock, So they were not added");
                    location.reload();
                	}
                }

			}

			function _addToCart2(res) {
				if (res1.code == '<%=HealthkartResponse.STATUS_OK%>') {
					$('.message .line1').html("<strong>" + res1.data.name + "</strong> is added to your shopping cart");
				} else if (res1.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
					$('#cart_error1').html(getErrorHtmlFromJsonResponse(res1)).slowFade(3000, 2000);
				} else if (res1.code == '<%=HealthkartResponse.STATUS_REDIRECT%>
			') {
						window.location.replace(res1.data.url);
					}
					$('.progressLoader').hide();
				}

				$('.addToCartButton').click(function(e) {
					if ($("#checkBoxEngraving").is(":checked")) {
						if ($.trim($("#engrave").val()) == '') {
							alert("Please specify name to be engraved, or uncheck the engraving option");
							$('.progressLoader').hide();
							return false;
						}
					}

					/*  if (!window.validateCheckbox) {

						$(this).parents().find('.progressLoader').show();
						$(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
						$(this).hide();
						e.stopPropagation();

					} */
				});

				$(".message .close").click(function() {
					hide_message();
				});
				$(document).click(function() {
					hide_message();
				});

				function hide_message() {
					$('.message').animate({
						top : '-170px',
						opacity : 0
					}, 100);
				}

				function show_message() {
					$('.message').css("top", "50px");
					$('.message').animate({
						opacity : 1
					}, 500);
				}

				$(".top_link, .go_to_top").click(function(event) {
					event.preventDefault();
					$('html,body').animate({
						scrollTop : ($(this.hash).offset().top - 45)
					}, 300);
				});

				$(document).click(function() {
					$('.checkboxError').fadeOut();
				});

				$('.checkboxError').hide();
				$("#checkBoxEngraving").click(function() {
					var stethoscopeConfigOption = $("#stethoscopeConfigOption").val();
					if ($("#checkBoxEngraving").is(":checked")) {
						$('#configOptionValueMap option[value=' + stethoscopeConfigOption + ']').attr('selected', 'selected');
						$(".engraveDiv").show();
					} else {
						$('#configOptionValueMap option[value=' + stethoscopeConfigOption + ']').attr('selected', false);
						$("#engrave").val('');
						$("#checkBoxEngraving").val(0);
						$(".engraveDiv").hide();
					}
				});

			});
		</script>
	</s:layout-component>
</s:layout-render>
