<%@ page import="java.util.Date" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="HealthKart.com Store : New Order">
<s:useActionBean beanclass="com.hk.web.action.admin.pos.POSAction" var="pos"/>
<%
    UserService userService = ServiceLocatorFactory.getService(UserService.class);
    pageContext.setAttribute("warehouse", userService.getWarehouseForLoggedInUser());
%>

<c:set var="paymentModeCard" value="<%=EnumPaymentMode.OFFLINE_CARD_PAYMENT.getId()%>" />
<s:layout-component name="htmlHead">
	<style type="text/css">
		table tr th {
			text-align: left;
		}

		.apply-border td {
			border: 1px solid #DDD;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			var paymentMode = $('#paymentMode').find('option:selected');
			if(paymentMode.val() == $('#paymentModeCard').val()) {
				$('#paymentRemarksDiv').show();
			} else {
				$('#paymentRemarksDiv').hide();
			}

			$('#reset').click(function() {
				window.location.href=window.location.href;
			});

			$('#productVariantBarcode').keypress(function(e) {
				if(e.which == 13) {
					e.preventDefault();
					$(this).trigger("change");
				}
			});

			$('#productVariantBarcode').change(function (event) {
				var productVariantBarcode = $('#productVariantBarcode').val();
				$('.orderDetails').html(' ');
				var skuItemList = [];
				var idx = 0;
				$("#skuItemHidden option").each(function() {
					skuItemList[idx] = $(this).val();
					idx++;
				});

				$.getJSON(
						$('#barcodeLink').attr('href'), {productVariantBarcode:productVariantBarcode,skuItemListToBeCheckedOut:skuItemList},
						function (res) {
							if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
								createNewRow();
								$('.lastRow').find('.item').html(res.data.product + '<br/>' + res.data.options);
								$('.lastRow').find('.itemHidden').val(res.data.product + '<br/>' + res.data.options);
								$('.lastRow').find('.mrp').val(res.data.mrp);
								$('.lastRow').find('.offerPrice').val(res.data.offerPrice);
								$('.lastRow').find('.qty').val(1);
								$('.lastRow').find('.skuItemId').val(res.data.skuItemId);
								$('.lastRow').find('.total').val(res.data.offerPrice);
								$('.lastRow').find('.pvBarcodeHidden').val(productVariantBarcode);

								$('#skuItemHidden').append('<option value='+res.data.skuItemId+'>'+res.data.skuItemId+'</option>');
								updateTotal('.total', '.grandTotal', 0);

							} else {
								$('.orderDetails').html('<h2>' + res.message + '</h2>');
							}
						}
				);

				$('#productVariantBarcode').val('');
			});

			function updateTotal(fromTotalClass, toTotalClass, toHtml) {
				var total = 0;
				$.each($(fromTotalClass), function (index, value) {
					var eachRow = $(value);
					var eachRowValue = eachRow.val().trim();
					total += parseFloat(eachRowValue);
				});
				if (toHtml == 1) {
					$(toTotalClass).html(total);
				} else {
					$(toTotalClass).val(total.toFixed(2));
				}
				updateFinalPayable();
			}

			function createNewRow() {
				var lastIndex = $('.lastRow').attr('count');
				if (!lastIndex) {
					lastIndex = -1;
				}
				$('.lastRow').removeClass('lastRow');

				var nextIndex = eval(lastIndex + "+1");
				var newRowHtml =
						'<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
								'<td>' + Math.round(nextIndex + 1) + '.</td>' +
								'<td class="item"></td>' +
								'<td><input type="text" class="mrp" name="posLineItems[' + nextIndex + '].mrp" readonly="readonly"/></td>' +
								'<td><input type="text" class="offerPrice" name="posLineItems[' + nextIndex + '].offerPrice" readonly="readonly"/></td>' +
								'<td><input type="text" class="qty" name="posLineItems[' + nextIndex + '].qty" readonly="readonly"/></td>' +
								'<td><input type="text" class="total" name="posLineItems[' + nextIndex + '].total" readonly="readonly"/></td>' +
								'<td><input type="hidden" class="skuItemId" name="posLineItems[' + nextIndex + '].skuItem"/></td>' +
								'<td><input type="hidden" class="itemHidden" name="posLineItems[' + nextIndex + '].productName"/></td>' +
								'<td><input type="hidden" class="pvBarcodeHidden" name="posLineItems[' + nextIndex + '].productVariantBarcode"/></td>' +
								'</tr>';

				$('#orderTable').append(newRowHtml);
			}

			$('#email').keypress(function(e) {
				if(e.which == 13) {
					e.preventDefault();
					$(this).trigger("change");
				}
			});

			$('#email').change(function() {
				$.getJSON(
						$('#emailLink').attr('href'), {email:$('#email').val()},
						function (res) {
							if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
								$('#name').val(res.data.customer.name);
								$('#customer').val(res.data.customer.id);
								$('#line1').val(res.data.address.line1);
								$('#line2').val(res.data.address.line2);
								$('#city').val(res.data.address.city);
								$('#state').val(res.data.address.state);
								$('#pincode').val(res.data.pincode);
								$('#address').val(res.data.address.id);
								$('#phone').val(res.data.address.phone);
								fillPOSLoyaltyDetails(res);
								if(res.data.rewardPoints > 0) {
									$('#rewardPoints').text(res.data.rewardPoints);
									$('#rewardPointsRow').show();
								}
							} 
						}
				);
			});

			function validateCustomerDetails () {
				
				if ($.trim($("#line1").val()) != '' && ($.trim($("#city").val()) == '' || $.trim($("#pincode").val()) == '') ) {
					alert("Please fill the complete address");
					return false;
				}

				if ($.trim($("#city").val()) != '' && ($.trim($("#line1").val()) == '' || $.trim($("#pincode").val()) == '') ) {
					alert("Please fill the complete address");
					return false;
				}

				if ($.trim($("#pincode").val()) != '' && ($.trim($("#line1").val()) == '' || $.trim($("#city").val()) == '') ) {
					alert("Please fill the complete address");
					return false;
				}
			return true;
			}
			$('#receivePayment').click(function() {

				var paymentMode = $('#paymentMode').find('option:selected');
				if(paymentMode.text() == "-Select-") {
					alert('Please select the payment mode');
					return false;
				}

				if(!validateCustomerDetails()) { 
					return false;
					}
				
				if($('.amountReceived').val() == '') {
					alert('Please fill the actual Amount Received');
					return false;
				}

				var discount = $('#discount').find('option:selected').val();
				var grandTotal = $('.grandTotal').val();
				var rewardPts = 0;
				if($('#useRewardPoints').checked) {
					rewardPts = parseFloat($('#rewardPoints').text());
				}
				if((parseFloat(discount) + rewardPts) > grandTotal) {
					alert('Discount cannot be more than the total amount');
					return false;
				}
			});

			$('#paymentMode').change(function() {
				var paymentMode = $('#paymentMode').find('option:selected');
				if(paymentMode.val() == $('#paymentModeCard').val()) {
					$('#paymentRemarksDiv').show();
				} else {
					$('#paymentRemarksDiv').hide();
				}
			});

			var warehouse = $('#warehouse').val();
			$("#productselect").autocomplete({
				url:"${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?getProductsInWarehouse=&warehouse=" + warehouse + "&q="
			});

			$('.addressClass').change(function() {
				$('#newAddress').val(true);
			});

			$('#discount').change(function() {
				updateFinalPayable();
			});

		    // Reward points conversion
		  	$("#rewardLink").click(function(e) {
		  		e.preventDefault();
		  		if(confirm("Please note that by clicking OK all loyalty points of the customer will be converted to reward points." +
		  				" These points will be valid for next six months only. Convert Points?")) {
			  		$.getJSON($('#rewardLink').attr('href'), {loyaltyCustomer:$('#loyaltyCustomer').val(),email:$('#email').val()},
			  				function (res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
									alert(res.message);
									$('#loyaltyPoints').text(0);
									$('#rewardPoints').text(res.data.totalRewardPoints);
									$('#rewardPointsRow').show();
								} else {
									//
									alert(res.message);
									}
							});
		  		} else {
		  			return false;
		  		}
		  		
		  	});

		    $('#useRewardPoints').click(function() {
		    	updateFinalPayable();
	    	});
		    
		    $('#updateCustomerInfo').click(function (e) {
		    	e.preventDefault();
				if(!validateCustomerDetails()) { 
					return false;
					}
				var form = $('#posForm');
				var dataUrl = form.attr('action') + "?" + $(this).attr('name') + "=";
				$.ajax({
						url: dataUrl,
						data: form.serialize(),
						success: function (res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
								alert(res.message);
								fillPOSLoyaltyDetails(res);
								}
							},
						error: 	function(res) {
							alert(res.message);
						}
						
				});
					return false;
		    });
		    
		    $('#rewardPointsRow').hide();
		    $('#oldLoyaltyCustomer').hide();
		    
		    function updateFinalPayable() {
		    	var discount = $('#discount').find('option:selected').val();
				var grandTotal = $('.grandTotal').val();
				var rewardPts = 0;
				if($('#useRewardPoints').is(':checked')) {
					rewardPts = parseFloat($('#rewardPoints').text());
				}
				$('#finalPayable').val((parseFloat(grandTotal) - discount).toFixed(0) - rewardPts.toFixed(2));
		    }
		    
		    function fillPOSLoyaltyDetails(res) {
		    	if (res.data.loyaltyUser) {
					$('#oldLoyaltyCustomer').show();
					$('#newLoyaltyCustomer').hide();
					$('.loyaltyUser').val(res.data.loyaltyUser);
					$('#loyaltyCustomerName').text(res.data.customerName);
					$('#badgeName').text(res.data.badgeName);
					$('#loyaltyPoints').text(res.data.loyaltyPoints);
					$('#cardNumber').val(res.data.cardNumber);
					$('#addLoyaltyUser').removeAttr('checked');
					var historyUrl = $('#historyLink').attr('href') + "&" + "email=" + $('#email').val();
			  		$('#historyLink').attr('href', historyUrl);
			  		$('#loyaltyCustomer').val(res.data.customer.id);
				} else {
					$('#addLoyaltyUser').attr('checked','checked');
					$('#newLoyaltyCustomer').show();
					$('#oldLoyaltyCustomer').hide();
				}
		    }
		});
	</script>
</s:layout-component>

<s:layout-component name="content">
	<input type="hidden" value="${warehouse.id}" id="warehouse" />
	<input type="hidden" value="${paymentModeCard}" id="paymentModeCard" />
	<div style="display: none;">
		<s:link beanclass="com.hk.web.action.admin.pos.POSAction" id="barcodeLink" event="getProductDetailsByBarcode"></s:link>
	</div>
	<div style="display: none;">
		<s:link beanclass="com.hk.web.action.admin.pos.POSAction" id="emailLink" event="getCustomerDetailsByLogin"></s:link>
	</div>
	<s:form beanclass="com.hk.web.action.admin.pos.POSAction" id="posForm">
		<input type="hidden" id="newAddress" name="newAddress"/>
		<%--<input type="hidden" id="address" name="address"/>--%>
		<table cellpadding="1" width="100%">
			<tr class="apply-border">
				<td>
					<div class='logoBox' style="float:left;">
						<a href="/" title='go to healthkart home'>
							<img src='<hk:vhostImage/>/images/logo.png' alt="healthkart logo"/>
						</a>
					</div>
				</td>
				<td>
					<strong>Aquamarine Healthcare Pvt. Ltd. <br/> TIN#</strong>
				</td>
				<td>
					<strong><%=new Date()%>
					</strong>
				</td>
				<td><b>Welcome, <shiro:principal property="firstName"/></b></td>
			</tr>
		</table>
		<table cellpadding="1" >
			<tr class="applyBorder" style="background:#EEE">
				<td align="right"><input type="button" value="Reset / New Order" id="reset"/></td>
				<td><s:label name="Shipping Gateway Order Id"/> </td>
				<td><s:text name="shippingGatewayOrderId"/></td>
				<td><s:submit name="createReverseOrderForPOS" value="Return Order" id="reverseOrder"/></td>
				<td><s:link beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction" target="_blank">Checkin Return Inventory</s:link> </td>
				<%--<td>Search a Product</td>
				<td><input type="text" style="width:300px;float: left;padding-top: 0;padding-bottom: 0;font: inherit;" id="productselect"/></td>--%>
			</tr>
		</table>


		<fieldset class="right_label">
			<legend><b>Customer</b></legend>
			<table width="100%">
				<tr>
					<td width="50%">
						<table>
							<tr><td>Email:</td><td><s:text name="email" id="email" style="width:200px"/></td></tr>
							<tr><td>Phone:</td><td><s:text name="phone" id="phone" style="width:200px"/></td></tr>
							<tr><td>Name: </td><td><s:text name="name" id="name" style="width:200px"/></td></tr>
						</table>
					</td>
					<td>
						<table>
							<tr><td colspan="2">Address:</td></tr>
							<tr><td>Address Line1</td><td><s:text name="addressLine1" id="line1" style="width:400px" class="addressClass"/></td></tr>
							<tr><td>Address Line2</td><td><s:text name="addressLine2" id="line2" style="width:400px" class="addressClass"/></td></tr>
							<tr><td>City</td><td><s:text name="addressCity" id="city" style="width:400px" class="addressClass"/></td></tr>
							<tr>
								<td>State</td>
								<td><s:select name="addressState" id="state" class="addressClass">
							        <c:forEach items="<%=StateList.stateList%>" var="state">
							          <s:option value="${state}">${state}</s:option>
							        </c:forEach>
							      </s:select>
								</td>
							</tr>
							<tr><td>Pincode</td><td><s:text name="addressPincode" id="pincode" class="addressClass"/></td></tr>
						</table>
					</td>
				</tr>
			</table>
			
			<div id="loyaltyDiv" style="font-size: 14px;"> 
					<span id="newLoyaltyCustomer">&nbsp;<s:checkbox name="addLoyaltyUser" id="addLoyaltyUser" checked="checked"></s:checkbox> Add as Loyalty User</span> 
				
				<span id="oldLoyaltyCustomer" style="float:left;">
					Presently <span id="loyaltyCustomerName"> </span>  has <span id="badgeName"> </span> 
					status and <span id="loyaltyPoints"> </span> loyalty points.
					<s:link beanclass="com.hk.web.action.admin.pos.POSAction" id="historyLink" event="getCustomerLoyaltyHistory"
					 target="_blank" style="color:red; font-size:1;">Customer History
					 </s:link>
 					<br/>
 					<s:link id="rewardLink" beanclass="com.hk.web.action.admin.pos.POSAction" event="convertLoyaltyPoints" >Click here 
 					<s:hidden id="loyaltyCustomer" name="loyaltyCustomer"  />
 					</s:link>
 					to convert customer's loyalty points to reward points.
				</span>	
				<span style="float:right; margin-right:150px;">Customer Loyalty Card number <s:text name="cardNumber" id="cardNumber" /></span> 
				
			</div>
				<br/>  
				<s:hidden name="loyaltyUser" class="loyaltyUser"/>
				<s:button name="updateCustomerInfo" value="Update Customer Info" id="updateCustomerInfo" style="float:right; clear:both;"/>
				<br/>
		</fieldset>
		<c:if test="${pos.order.id == null}">
			<fieldset class="right_label">
				<legend>Scan Barcode:</legend>
				<ul>
					<li>
						<s:label name="barcode">Product Variant Barcode</s:label>
						<s:text name="productVariantBarcode" id="productVariantBarcode"/>
					</li>
					<li></li>
				</ul>
			</fieldset>
			<div class="orderDetails info"></div>
		</c:if>
		<fieldset>
			<legend><b>Order</b></legend>
			<s:hidden name="customer" id="customer" />
			<input type="hidden" name="order" id="order" value="${pos.order.id}" />
			<input type="hidden" name="address" value="${pos.address.id}" id="address" />
			<table width="100%" border="1" >
				<thead>
				<tr><th>S.No.</th><th>Item</th><th>MRP</th><th>Offer Price</th><th>Qty</th><th>Total</th></tr>
				</thead>
				<tbody id="orderTable">
				<div class="skuItemSelect" style="display: none;">
					<select id="skuItemHidden"></select>
				</div>
				<c:forEach var="posLineItemDto" items="${pos.posLineItems}" varStatus="ctr">
					<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
						<td>${ctr.index + 1}</td>
						<td class="item">${posLineItemDto.productName}</td>
						<td><s:text name="posLineItems[${ctr.index}].mrp" value="${posLineItemDto.mrp}" class="mrp" readonly="readonly"/></td>
						<td><s:text name="posLineItems[${ctr.index}].offerPrice" value="${posLineItemDto.offerPrice}" class="offerPrice" readonly="readonly"/></td>
						<td><s:text name="posLineItems[${ctr.index}].qty" value="${posLineItemDto.mrp}" class="qty" readonly="readonly"/></td>
						<td><s:text name="posLineItems[${ctr.index}].total" value="${posLineItemDto.total}" class="total" readonly="readonly"/></td>
						<td><s:hidden class="skuItemId" name="posLineItems[${ctr.index}].skuItem" value="${posLineItemDto.skuItem.id}"/></td>
						<td><s:hidden class="itemHidden" name="posLineItems[${ctr.index}].productName" value="${posLineItemDto.productName}"/></td>
						<td><s:hidden class="pvBarcodeHidden" name="posLineItems[${ctr.index}].productVariantBarcode" value="${posLineItemDto.productVariantBarcode}"/></td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
				<tr>
					<td colspan="5" align="right"><b>Total</b></td>
					<td><s:text name="grandTotal" value="${pos.grandTotal}" class="grandTotal" readonly="readonly"/></td>
				</tr>
				<tr>
					<td colspan="4"></td>
					<td align="right"><b>Discount (In Rupees)</b></td>
					<td><s:select name="discount" value="${pos.discount}" id="discount">
						<s:option value="">-Select</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="discountsForPOS" />
					</s:select></td>
				</tr>
				<tr id="rewardPointsRow">
					<td align="right"><b>Reward Points available</b></td>
					<td colspan="3"><span id="rewardPoints" class="rewardPoints"> </span></td>
                    <td> Use Reward Points</td><td> <s:checkbox name="useRewardPoints" id="useRewardPoints" /></td>
					
				</tr>
				
				<tr>
					<td colspan="5" align="right"><b>Final Payable</b></td>
					<td><input type="text" id="finalPayable" readonly="readonly"/></td>
				</tr>
				<tr><td><b>Order ID</b></td><td colspan="3">${pos.order.id}</td>
					<td align="right"><b>Payment Mode</b></td>
					<td>
					<s:select name="paymentMode" id="paymentMode">
						<s:option value="">-Select-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentModeForStore"
						                           value="id"
						                           label="name"/>
					</s:select>
					</td>
				</tr>
				</tfoot>
			</table>

			<div id="paymentRemarksDiv" style="display: none;">
				<table>
					<tr>
						<td colspan="3" align="right">Payment Reference No.</td><td><s:text name="paymentReferenceNumber"/></td>
						<td align="right">Last four digit card No.</td><td><s:text name="lastFourDigitCardNo"/></td>
						<td>Card/Bank Name(Remarks)</td><td><s:text name="paymentRemarks" style="width:300px; height:50px" maxlength="45" /></td>
					</tr>
				</table>
			</div>

		</fieldset>

		<table cellpadding="1" width="100%">
			<tr>
				<c:if test="${pos.order.id == null}">
					<td colspan="2" align="right"><s:submit name="receivePaymentAndProcessOrder" value="Receive Payment" id="receivePayment"/></td>
				</c:if>
					<c:if test="${pos.order.id != null}">
						<td colspan="2" align="right">
							<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="posPrintInvoice" target="_blank" class="button_orange">
								Print<s:param name="shippingOrder" value="${pos.shippingOrderToPrint}" />
							</s:link>
						</td>
					</c:if>
			</tr>
		</table>
	</s:form>

</s:layout-component>
</s:layout-render>