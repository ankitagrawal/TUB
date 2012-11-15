<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=cart class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		<div data-role="content" style='background-color: white'>


			<div
				style='clear: both; padding: 4px; background-color: #ddd; text-align: center; margin-bottom: 6px; font-weight: bold'>
				Product in Cart</div>
			<ul data-role=listview style='padding-top: 4px; padding-bottom: 20px'
				id='cartList'>

			</ul>
			<div id=cartTotal data-role=none>
			
			</div>
			<%if(session.getAttribute("userName")!=null){%>
			<table class=hide id='couponContainer' style='margin:0px auto'>
				<tr>
					<td>
						<input type=text id=couponText name=couponText  placeholder='Discount Coupon' />
					</td>
					<td>
						<button id=submitCoupon>Apply</button>
					</td>
				</tr>
				<tr>
					<td colspan=2 style='text-align:center'>
						<a href='${httpPath}/coupon-manage.jsp'>(see previously applied offers)</a>
					</td>
				</tr>
			</table>
			<%} %>
			<br /> <a
				href='<%if(session.getAttribute("userName")==null){%>${httpPath}/login-signup.jsp?target=address<% }else{%>${httpPath}/address.jsp<%}%>'
				id='checkout'
				style=' margin: 0px auto; margin-bottom: 8px; '
				data-role=button id='btnChkOut'>CheckOut</a>

			<%@ include file='menuFooter.jsp'%>
		</div>
	<%@ include file='template/cart-templates.jsp'%>
		<script type="text/javascript" src='${httpPath}/js/cart.js'></script>	

		
	</div>

</body>
</html>
