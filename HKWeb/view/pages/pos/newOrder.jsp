<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: Ajeet
  Date: 21 Dec, 2012
  Time: 12:03:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<html>
<head>
	<title>HealthKart.com Store : New Order</title>
	<style type="text/css">
		table tr th { text-align: left;}

		.apply-border td {border:1px solid #DDD;}
	</style>
</head>
<body>
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
		<td><b>Welcome! Ajeet</b></td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<td colspan="4">
			<fieldset>
				<legend><b>Customer</b></legend>
				<table width="100%">
					<tr>
						<td>
							<table>
								<tr><td>Name: <input> </td></tr>
								<tr><td>Email: <input> </td></tr>
								<tr><td>Phone: <input> </td></tr>
							</table>
						</td>
						<td>
							Address:<textarea rows="4" cols="30"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr><td colspan="4"><b>Order ID:</b></td></tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<td colspan="4">
			<fieldset>
				<legend><b>Order</b></legend>
				<table width="100%" border="1">
					<tr><th>S.No.</th><th>Item</th><th>MRP</th><th>Offer Price</th><th>Qty</th><th>Total</th></tr>
					<tr><td>1.</td><td><input> </td><td><input></td><td><input></td><td><input></td><td><input></td></tr>
					<tr><td>2.</td><td><input> </td><td><input></td><td><input></td><td><input></td><td><input></td></tr>
					<tr><td>3.</td><td><input> </td><td><input></td><td><input></td><td><input></td><td><input></td></tr>
					<tr><td colspan="5" align="right"><b>Grand Total</b></td><td><input></td></tr>
				</table>
			</fieldset>
		</td>
	</tr>

	<tr><td style="border:0px;">&nbsp;</td></tr>

	<tr>
		<td colspan="2" align="left"><input type="button" value="Cancel or Reset"/></td>
	    <td colspan="2" align="right"><input type="button" value="Confirm"/>&nbsp;<input type="button" value="Print"/></td>
	</tr>
</table>

</body>
</html>