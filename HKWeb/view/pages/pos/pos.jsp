<%@ page import="java.util.Date" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="HealthKart.com Store : New Order">
<s:useActionBean beanclass="com.hk.web.action.admin.pos.POSAction" var="pos"/>

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
			$('#reset').click(function() {
				location.reload();
			});
              //var index = -1;
			$('#productVariantBarcode').keypress(function(e) {
				if(e.which == 13) {
					e.preventDefault();
					$(this).trigger("change");
				}
			});

			$('#productVariantBarcode').change(function (event) {
				var productVariantBarcode = $('#productVariantBarcode').val();

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
								//var existingRow = findIfSkuGroupExists(res.data.skuGroupId);
								//if(existingRow == null) {
									createNewRow();
									$('.lastRow').find('.item').html(res.data.product + '<br/>' + res.data.options);
									$('.lastRow').find('.itemHidden').val(res.data.product + '<br/>' + res.data.options);
									$('.lastRow').find('.mrp').val(res.data.mrp);
									$('.lastRow').find('.offerPrice').val(res.data.offerPrice);
									$('.lastRow').find('.qty').val(1);
									$('.lastRow').find('.skuItemId').val(res.data.skuItemId);
									$('.lastRow').find('.total').val(res.data.offerPrice);
									$('.lastRow').find('.pvBarcodeHidden').val(productVariantBarcode);
								/*} else {
									var qty = parseInt( existingRow.find('.qty').val() );
									var offerPrice = parseFloat( existingRow.find('.offerPrice').val() );
									existingRow.find('.qty').val(qty + 1);
									existingRow.find('.total').val((offerPrice * (qty + 1)).toFixed(2));
								}
								index++;*/
								//$('.skuItemSelect').append('<input type="hidden" name="skuItemListToBeCheckedOut['+ index+'].id" value="'+ res.data.skuItemHidden +'" />');
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
			}

			/*function findIfSkuGroupExists(newSkuGroupId) {
				var rowFound = null;
				$('.skuGroupId').each(function(index, value) {
					var eachRow = $(value);
					var skuGroupId = eachRow.val().trim();
					if(skuGroupId == newSkuGroupId) {
						rowFound = eachRow.parents('.lineItemRow');
					}

				});
				return rowFound;
			}*/

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
								$('#pincode').val(res.data.address.pin);
								$('#address').val(res.data.address.id);
								$('#phone').val(res.data.address.phone);
							} else {
								//$('.orderDetails').html('<h2>' + res.message + '</h2>');
							}
						}
				);
			});

			$('#receivePayment').click(function() {
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
			});

		});
	</script>
</s:layout-component>

<s:layout-component name="content">
	<div style="display: none;">
		<s:link beanclass="com.hk.web.action.admin.pos.POSAction" id="barcodeLink" event="getProductDetailsByBarcode"></s:link>
	</div>
	<div style="display: none;">
		<s:link beanclass="com.hk.web.action.admin.pos.POSAction" id="emailLink" event="getCustomerDetailsByLogin"></s:link>
	</div>
	<s:form beanclass="com.hk.web.action.admin.pos.POSAction" id="posForm">
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
		<table cellpadding="1" width="100%">

			<tr class="applyBorder" style="background:#EEE">
				<td align="right"><input type="button" value="Reset / New Order" id="reset"/></td>
				<td style="font-size:.9em">Search Order</td>
				<td style="font-size:.9em">Order List</td>
				<td style="font-size:.9em">Inventory</td>
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
							<tr><td>Address Line1</td><td><s:text name="address.line1" id="line1"/></td></tr>
							<tr><td>Address Line2</td><td><s:text name="address.line2" id="line2"/></td></tr>
							<tr><td>City</td><td><s:text name="address.city" id="city"/></td></tr>
							<tr>
								<td>State</td>
								<td><s:select name="address.state" id="state">
							        <c:forEach items="<%=StateList.stateList%>" var="state">
							          <s:option value="${state}">${state}</s:option>
							        </c:forEach>
							      </s:select>
								</td>
							</tr>
							<tr><td>Pincode</td><td><s:text name="address.pin" id="pincode"/></td></tr>
						</table>
					</td>
				</tr>
			</table>
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
						<td><s:text name="posLineItems[${ctr.index}].mrp" value="${posLineItemDto.mrp}" class="mrp"/></td>
						<td><s:text name="posLineItems[${ctr.index}].offerPrice" value="${posLineItemDto.offerPrice}" class="offerPrice"/></td>
						<td><s:text name="posLineItems[${ctr.index}].qty" value="${posLineItemDto.mrp}" class="qty"/></td>
						<td><s:text name="posLineItems[${ctr.index}].total" value="${posLineItemDto.total}" class="total"/></td>
						<td><s:hidden class="skuItemId" name="posLineItems[${ctr.index}].skuItem" value="${posLineItemDto.skuItem.id}"/></td>
						<td><s:hidden class="itemHidden" name="posLineItems[${ctr.index}].productName" value="${posLineItemDto.productName}"/></td>
						<td><s:hidden class="pvBarcodeHidden" name="posLineItems[${ctr.index}].productVariantBarcode" value="${posLineItemDto.productVariantBarcode}"/></td>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
				<tr>
					<td colspan="5" align="right"><b>Grand Total</b></td>
					<td><s:text name="grandTotal" value="${pos.grandTotal}" class="grandTotal" readonly="readonly"/></td>
				</tr>
				<tr><td><b>Order ID</b></td><td>${pos.order.id}</td><td colspan="3" align="right"><b>Payment Mode</b></td><td><select><option>Cash</option><option>Card</option> </select></td></tr>
				</tfoot>
			</table>
		</fieldset>

		<table cellpadding="1" width="100%">
			<tr>
				<%--<td colspan="2" align="right"><input type="button" value="Reset" id="reset"/></td>--%>
				<c:if test="${pos.order.id == null}">
					<td colspan="2" align="right"><s:submit name="receivePaymentAndProcessOrder" value="Receive Payment" id="receivePayment"/></td>
				</c:if>
					<c:if test="${pos.order.id != null}">
						<td colspan="2" align="right">
							<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" target="_blank" style="font-size: 2.0em; color:red">
								Print<s:param name="shippingOrder" value="${pos.shippingOrderToPrint}" />
							</s:link>
						</td>
					</c:if>
			</tr>
		</table>
	</s:form>
	<div class="orderDetails info"></div>

</s:layout-component>
</s:layout-render>