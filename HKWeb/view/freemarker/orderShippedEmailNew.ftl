Your order ${order.baseOrder.gatewayOrderId} has been shipped.

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Just waiting eagerly to come home...</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
	<td align="center" valign="top">
		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center"
		       style="font-size:13px; line-height:1.75em;">


			<tr>
				<td height="20"></td>
			</tr>


			<tr>
				<td>
					<table width="500" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="222"><a href="http://www.healthkart.com"><img
									src="http://img.healthkart.com/email/logos/logo.png" alt="HealthKart.com Logo"
									width="207"
									height="30" border="0"/></a></td>
							<td width="15" style="border-left: solid 1px #999999"></td>
							<td width="245" align="left" style="font-size:13px; font-weight:bold; color:#666666"><a
									href="http://www.healthkart.com" style="color:#666666; text-decoration:none">India&#39;s
								premier
								e-health store!</a></td>
							<td width="18">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5" height="10"></td>
						</tr>
						<tr>
							<td colspan="5"><a href="http://www.healthkart.com"><img
									src="http://img.healthkart.com/email/order_shipped_email_new/main_banner.jpg"
									width="500"
									height="148" alt="Just waiting eagerly to come home..." border="0"/></a></td>
						</tr>


					</table>
				</td>
			</tr>


			<tr>
				<td align="left" height="15"></td>
			</tr>
			<tr>
				<td width="579" valign="top">Hi ${order.baseOrder.address.name}!<br/>

					Following items of your order ${order.baseOrder.gatewayOrderId}, placed
					on ${order.baseOrder.payment.paymentDate} have been dispatched.
					This completes the shipping of your order in its totality. Here are
					the details:<br/>
					<br/>
					<table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
						<tr>
							<td><strong>Item</strong></td>
							<td><strong>Quantity</strong></td>
							<td><strong>TrackingId</strong></td>
							<td><strong>Courier</strong></td>
                            <td><strong>Est. Delivery Date</strong></td>
						</tr>
					<#list order.lineItems as lineItem>
						<tr>

							<td>${lineItem.sku.productVariant.product.name}
								<#if lineItem.sku.productVariant.variantName??>
								${lineItem.sku.productVariant.variantName}
								</#if>
								<br/>

								<#list lineItem.sku.productVariant.productOptions as
								productOption>
									<em style="font-size:0.9em; color:#666">${productOption.name} ${productOption.value} </em>
								</#list>
							</td>
							<td>
							${lineItem.qty}
							</td>
							<td>
							${order.shipment.awb.awbNumber}
								<#if order.shipment.trackLink??>
									<h5><a href="${order.shipment.trackLink}" target="_blank"> Track This </a></h5>
								</#if>
							</td>
							<td>
							${order.shipment.awb.courier.name}
							</td>
                            <td>
                            ${targetDeliverDate}
                            </td>
						</tr>
					</#list>
					</table>

				</td>
			</tr>

		<#if shippingOrderAlreadySentList??>
			<#list shippingOrderAlreadySentList as shippingOrder>
			<tr>
				<td width="579" valign="top">
					<br>
					<#if shippingOrder.shipment.shipDate??>
						Following items were dispatched earlier on ${shippingOrder.shipment.shipDate?date} via a different shipment <br><br>
					<#else >
						Following items were dispatched earlier via a different shipment <br><br>
					</#if>

					<table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
						<tr>
							<td><strong>Item</strong></td>
							<td><strong>Quantity</strong></td>
						</tr>

							<#list shippingOrder.lineItems as lineItem>
								<tr>
									<td>${lineItem.sku.productVariant.product.name}
										<#if lineItem.sku.productVariant.variantName??>
										${lineItem.sku.productVariant.variantName}
										</#if>
										<br/>

										<#list lineItem.sku.productVariant.productOptions as
										productOption>
											<em style="font-size:0.9em; color:#666">${productOption.name} ${productOption.value} </em>
										</#list>
									</td>
									<td>
									${lineItem.qty}
									</td>
								</tr>
							</#list>

					</table>

				</td>
			</tr>
			</#list>
		</#if>
			<tr>
				<td width="579" valign="top">
					<br/>
					<br/>

					<strong>Note: Our products are shipped in sealed bags & boxes and we request you not to accept
						any tampered packets.</strong>
					<br/>
					<br/>
					In case you have any queries, feel free to chat with our Customer Care or call them at
					0124-4616444. <br/>
					<br/>

					Healthy Shopping!<br/>
					HealthKart.com <br/>
					(India&#39;s Premier eHealth Store)
				</td>
			</tr>
			<tr>
				<td height="15"></td>
			</tr>
		</table>
	</td>
</tr>

<tr>
	<td bgcolor="#b8b8b8" style="border-top: solid #606060 2px">
		<table width="500" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td height="70" colspan="3" align="left" valign="middle"><a href="http://www.healthkart.com/"><img
						src="http://img.healthkart.com/email/logos/hk_logo_bw.jpg" alt="HealthKart.com logo"
						width="178"
						height="28" border="0"/></a></td>
				<td width="312" align="right" valign="middle"
				    style="color:#606060; font-weight:bold; font-size:13px;"><a
						href="http://www.healthkart.com/" style="color:#606060; text-decoration:none;">www.healthkart.com</a>
				</td>
			</tr>
			<tr>
				<td width="106" height="60" style="border-top: solid #929292 1px; color:#606060; font-size:13px;">
					Spread the
					word:
				</td>
				<td width="39" align="left" valign="middle" style="border-top: solid #929292 1px"><a
						href="http://www.facebook.com/healthkart"><img
						src="http://img.healthkart.com/email/logos/facebook.png" alt="facebook" width="32"
						height="32"
						border="0"/></a></td>
				<td width="43" align="left" valign="middle" style="border-top: solid #929292 1px"><a
						href="http://twitter.com/healthkart"><img
						src="http://img.healthkart.com/email/logos/twitter.png"
						alt="twitter" width="32" height="32" border="0"/></a></td>
				<td align="right" valign="middle"
				    style="border-top: solid #929292 1px; color:#606060; font-size:13px;">e: <a
						href="mailto:info@healthkart.com" style="color:#606060">info@healthkart.com</a> &nbsp;|
					&nbsp;t: 0124-4616444
				</td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td align="center" valign="middle"
	    style="border-top: solid #FFFFFF 2px; font-size:11px; text-align:center; color:#929292; padding:10px">

		Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br/>
		&copy; 2013 HealthKart.com. All Rights Reserved.
	</td>
</tr>
</table>
</body>
</html>
