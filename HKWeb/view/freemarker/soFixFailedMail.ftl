Shipping Order ${gatewayId} could not fixed.

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Shipping Order could not fixed</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
Shipping Order ${gatewayId} could not fixed for the following line items :

<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
	<td align="center" valign="top">
		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center"
		       style="font-size:13px; line-height:1.75em;">

			<tr>
				<td height="20"></td>
			</tr>

			<tr>
				<td align="left" height="15"></td>
			</tr>
			<tr>
				<td width="579" valign="top">

					<b>Here are the details:</b><br/>
					<br/>
					<table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
						<tr>
							<td><strong>Line Item</strong></td>
							<td><strong>Quantity</strong></td>
							<td><strong>MRP</strong></td>
							<td><strong>Reason</strong></td>
						</tr>
						<tr>
							<td>${variantName}</td>
							<td>
							${quantity}
							</td>
							<td>
							${mrp}
							</td>
							<td>
							${reason}
							</td>
						</tr>
					</table>
			</tr>

			<tr>
				<td height="15"></td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>
