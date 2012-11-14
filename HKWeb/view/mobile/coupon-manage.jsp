<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div id='couponManage' data-role=page>
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role=content>
			<table id='couponManagerContainer'>
			</table>
			<table style='width:100%;margin:0px auto;text-align:center'>
				<tr>
					<td>
						<div id='couponApply' class='greenBtn'>Apply Offer</div>
					</td>
					<td>
						<div id='couponRemove' class='greenBtn'>Remove Applied Offer</div>
					</td>
				</tr>
			</table>
			<div class=clear></div>
			<a href='javascript:void(0)' data-role=button data-rel=back>Go Back</a>
		</div>
		<%@ include file='template/coupon-manage-templates.jsp'%>
		<script type="text/javascript" src='${httpPath}/js/couponManage.js'></script>
	</div>
	

</body>
</html>
