Your order ${order.gatewayOrderId} has been delivered. Please share some feedback!

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Your order has been delivered...</title>
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
									href="http://www.healthkart.com" style="color:#666666; text-decoration:none">India&#39;s premier e-health store!</a></td>
							<td width="18">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5" height="10"></td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td align="left" height="15"></td>
			</tr>
			<tr>
				<td width="579" valign="top">Dear ${order.user.name}<br/> <br/>
					Thank you for shopping with HealthKart. We trust that you had a good shopping experience and are on your way to the zenith of health.<br/>
					Your Order No.: ${order.gatewayOrderId} has been successfully delivered.<br/><br/>

					We would like to know more about your experience shopping with us, so that we can improve our services. <br>
					While we like positive feedback, we think your criticism can help us correct our course & serve you better.<br>
					Feel free to be frank, we're listening.<br/>

					<table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1" RULES=COLS >
						<tr>
							<td>
								<table cellpadding="5" cellspacing="5" >
									<tr>
										<td colspan="10"><strong>How likely is it that you would
											recommend HealthKart to a friend or colleague? </strong>
										</td>
									</tr>
									</table>
								<table cellpadding="5" cellspacing="5" border="1" style="font-size:12px;" RULES=COLS FRAME=BOX>
									<tr>
										<td><strong>Not likely</strong></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td><strong>Very likely</strong></td>
									</tr>
									<tr>
										<td>
											<a href="${feedbackPage}?recommendToFriends=0&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="0"/></a> 0
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=1&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="1"/></a> 1
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=2&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="2"/></a> 2
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=3&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="3"/></a> 3
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=4&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="4"/></a> 4
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=5&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="5"/></a> 5
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=6&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="6"/></a> 6
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=7&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="7"/></a> 7
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=8&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="8"/></a> 8
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=9&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="9"/></a> 9
										</td>
										<td>
											<a href="${feedbackPage}?recommendToFriends=10&baseOrderId=${order}">
												<input type="radio" name="recommendToFriends" value="10"/></a> 10
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td></td>
						</tr>
					</table>

					<br/>
					
					<div class="line-separator" style="height:1px; background:#717171; border-bottom:1px solid #313030;"></div>

					<br/>Summary of your order: <br/>

					<table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
						<tr>
							<td><strong>Item</strong></td>
							<td><strong>Quantity</strong></td>
						</tr>
					<#list order.shippingOrders as shippingOrder>
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
					</#list>
					</table>

					<br/>
					If you have any problems or concerns about your recent purchase, please get in touch with our customer service as soon as possible and we will do everything we can to help.
					<br/> <br/>
					Reach our Customer Care at 0124-4616444.
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
