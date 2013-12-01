<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
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
				<a href='http://www.healthkart.com/Home.action'>Go To DeskTop Version</a>
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
		<center style='padding:4px'>&copy; HealthKart 2013</center>
        <%
          if (AnalyticsConstants.analytics) {
        %>
        <script type="text/javascript">

          var _gaq = _gaq || [];
          _gaq.push(['_setAccount', 'UA-21820217-12']);
          _gaq.push(['_trackPageview']);

          (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
          })();
        
        </script>
        <%}%>