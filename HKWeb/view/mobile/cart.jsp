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
			<br /> <a
				href='<%if(session.getAttribute("userName")==null){%>${httpPath}/login-signup.jsp?target=address<% }else{%>${httpPath}/address.jsp<%}%>'
				id='checkout'
				style='width: 95%; margin: 0px auto; margin-bottom: 8px; margin-top: 12px'
				data-role=button id='btnChkOut'>CheckOut</a>

			<%@ include file='menuFooter.jsp'%>
		</div>

		<%@ include file='template/cart-templates.jsp'%>
		<script type="text/javascript" src='${httpPath}/js/cart.js'></script>
	</div>

</body>
</html>
