<%@ page import="java.util.Date" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
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
				$('#productVariantBarcode').change(function () {
					var productVariantBarcode = $('#productVariantBarcode').val();
					$.getJSON(
							$('#barcodeLink').attr('href'), {productVariantBarcode:productVariantBarcode},
							function (res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
									var existingRow = findIfSkuGroupExists(res.data.skuGroupId);
									if(existingRow == null) {
										createNewRow();
										$('.lastRow').find('.item').html(res.data.product + '<br/>' + res.data.options);
										$('.lastRow').find('.itemHidden').val(res.data.product + '<br/>' + res.data.options);
										$('.lastRow').find('.mrp').val(res.data.mrp);
										$('.lastRow').find('.offerPrice').val(res.data.offerPrice);
										$('.lastRow').find('.qty').val(1);
										$('.lastRow').find('.skuGroupId').val(res.data.skuGroupId);
										$('.lastRow').find('.total').val(res.data.offerPrice);
									} else {
										var qty = parseInt( existingRow.find('.qty').val() );
										var offerPrice = parseFloat( existingRow.find('.offerPrice').val() );
										existingRow.find('.qty').val(qty + 1);
										existingRow.find('.total').val((offerPrice * (qty + 1)).toFixed(2));
									}

								} else {
									$('.orderDetails').html('<h2>' + res.message + '</h2>');
								}
							}
					);

					$('#productVariantBarcode').val('');
				});

				function findIfSkuGroupExists(newSkuGroupId) {
					var rowFound = null;
					$('.skuGroupId').each(function(index, value) {
						var eachRow = $(value);
						var skuGroupId = eachRow.val().trim();
						if(skuGroupId == newSkuGroupId) {
							rowFound = eachRow.parents('.lineItemRow');
						}

					});
					return rowFound;
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
									'<td><input type="hidden" class="skuGroupId" name="posLineItems[' + nextIndex + '].skuGroup"/></td>' +
									'<td><input type="hidden" class="itemHidden" name="posLineItems[' + nextIndex + '].productName"/></td>' +
									'</tr>';

					$('#orderTable').append(newRowHtml);

				}

				$('#email').change(function() {
					$.getJSON(
							$('#emailLink').attr('href'), {email:$('#email').val()},
							function (res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
									$('#name').val(res.data.customer.name);
									$('#customer').val(res.data.customer.id);

								} else {
									//$('.orderDetails').html('<h2>' + res.message + '</h2>');
								}
							}
					);
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
		<s:form beanclass="com.hk.web.action.admin.pos.POSAction">
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
		<td><b>Welcome!<shiro:principal property="firstName"/></b></td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr class="applyBorder" style="background:#EEE">
		<td><b>New Order</b></td>
		<td style="font-size:.9em">Search Order</td>
		<td style="font-size:.9em">Order List</td>
		<td style="font-size:.9em">Inventory</td>
	</tr>
	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<td colspan="4">
			<fieldset>
				<legend><b>Customer</b></legend>
				<table width="100%">
					<tr>
						<td width="50%">
							<table>
								<tr><td>Phone:</td><td><s:text name="phone" id="phone"/></td></tr>
								<tr><td>Email:</td><td><s:text name="email" id="email"/></td></tr>
								<tr><td>Name: </td><td><s:text name="name" id="name"/></td></tr>
							</table>
						</td>
						<td>
							Address:<s:textarea name="address" />
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<td colspan="4">

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

			<fieldset>
				<legend><b>Order</b></legend>
				<table width="100%" border="1" id="orderTable">
					<tr><th>S.No.</th><th>Item</th><th>MRP</th><th>Offer Price</th><th>Qty</th><th>Total</th></tr>
					<c:forEach var="posLineItemDto" items="${pos.posLineItems}" varStatus="ctr">
					<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
						<td>${ctr.index + 1}</td>
						<td class="item">${posLineItemDto.productName}</td>
						<td><s:text name="posLineItems[${ctr.index}].mrp" value="${posLineItemDto.mrp}" class="mrp"/></td>
						<td><s:text name="posLineItems[${ctr.index}].offerPrice" value="${posLineItemDto.offerPrice}" class="offerPrice"/></td>
						<td><s:text name="posLineItems[${ctr.index}].qty" value="${posLineItemDto.mrp}" class="qty"/></td>
						<td><s:text name="posLineItems[${ctr.index}].total" value="${posLineItemDto.total}" class="total"/></td>
						<td><s:hidden class="skuGroupId" name="posLineItems[${ctr.index}].skuGroup" value="${posLineItemDto.skuGroup.id}"/></td>
						<td><s:hidden class="itemHidden" name="posLineItems[${ctr.index}].productName" value="${posLineItemDto.productName}"/></td>
					</tr>
					</c:forEach>
					<%--<tr><td>1.</td><td><input>
					</td><td><input></td><td><input></td><td><input></td><td><input></td></tr>
					<tr><td>2.</td><td><input>
					</td><td><input></td><td><input></td><td><input></td><td><input></td></tr>
					<tr><td>3.</td><td><input>
					</td><td><input></td><td><input></td><td><input></td><td><input></td></tr>--%>
					<tr><td colspan="5" align="right"><b>Grand Total</b></td><td><input></td></tr>
					<tr><td><b>Order ID</b></td><td>&nbsp;</td><td colspan="3" align="right"><b>Payment Mode</b></td><td><select><option>Cash</option><option>Card</option> </select></td></tr>
				</table>
			</fieldset>

		</td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<s:hidden name="customer" id="customer" />
		<%--<td colspan="2" align="left"><input type="button" value="Cancel or Reset"/></td>--%>
		<td colspan="2" align="right"><s:submit name="confirmOrder" value="Confirm"/>
			<%--&nbsp;<input type="button" value="Print"/></td>--%>
	</tr>

</table>
	</s:form>
			<div class="orderDetails info"></div>

	</s:layout-component>
</s:layout-render>