<table class='menuContainer'>
	<tr>
		<td><a href='${httpPath}/home.jsp'><img
				src='${httpPath}/images/logo.png'
				style='position: relative; top: 3px; left: 3px' /></a></td>
		<td>
			<div class=sessionOptions>
				<b>Hi <%=(session.getAttribute("userName") != null ? session
					.getAttribute("userName") : "Guest")%>
				</b><br>
				<%
					if (session.getAttribute("userName") != null) {
				%>
				<a href='javascript:void(0)' onclick='javascript:logout()'
					style='text-decoration: none'>Logout</a>
				<%
					} else {
				%>
				<a href='${httpPath}/login-signup.jsp' style='text-decoration: none'>Login/Signup</a>
				<%
					}
				%>
			</div>
		</td>
	</tr>
</table>
