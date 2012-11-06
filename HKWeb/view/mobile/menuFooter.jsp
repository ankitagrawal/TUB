<table id='footerLink'>
		<tr>
			<td class='arr-r'>
				<a href='${httpPath}/home.jsp'>Home</a>
			</td>
		</tr>
		<tr>
			<td class='arr-r'>
				<a href='${httpPath}/trackOrder.jsp'>Track Orders</a>
			</td>
		</tr>
		<tr>
			<td class='arr-r'>
				<a href='${httpPath}/home.jsp'>Offers</a>
			</td>
		</tr>
		<tr>
			<td class='arr-r'>
				<a href='${httpPath}/cart.jsp'>Cart</a>
			</td>
		</tr>
		<tr>
			<td class='arr-r'>
				<% if (session.getAttribute("userName") != null) {%>
					<a href='javascript:void(0)' onclick='javascript:logout()' >Logout</a>
				<%} else {%>
					<a href='${httpPath}/login-signup.jsp'>Login</a>
				<%}%>
			</td>
		</tr>
		<tr>
			<td class='arr-r'>
				<a href='${httpPath}'>Go To DeskTop Version</a>
			</td>
		</tr>
		<!--
		<tr>
			<td class='arr-r'>
				<a href='contactUs.jsp'>Contact Us</a>
			</td>
		</tr>
		-->
		</table>
		<center style='padding:4px'>&copy; HealthKart 2012</center>
