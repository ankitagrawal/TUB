<!DOCTYPE html>
<html>
<head>
<%@ include file='header.jsp'%>

</head>
<body>
	<div data-role="page" id=paymentSuccessOption class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>

			<div style='padding: 10px; margin-bottom: 10px; font-size: 16px'>
				<center>
					Your Order(#<%=request.getParameter("orderId")%>) has been successfully placed.
				</center>
			</div>

			<!-- end left -->


			<!-- start right pannel -->

			<%@ include file='menuFooter.jsp'%>
		</div>
		<!-- end right pannel -->

	</div>

	<script>
		$('#paymentSuccessOption').bind('pageshow', function() {
			$.mobile.urlHistory.stack = [];
		});
	</script>
    <%
    if (AnalyticsConstants.analytics) {
    %>
    <div id="sdt-js"></div>
    <script>
        var _beaconping = _beaconping || [];
        _beaconping.push({goalName:"Conversions", appId:"cb71699d-7566-45ad-9b77-a253b8fb25fb",event:"onloadbeacon"});
        (function() {
            var e = document.createElement('script');
            e.src = 'http://sdtbeacon.appsdt.com/sdtbeacon.js';
            e.async = true;
            document.getElementById('sdt-js').appendChild(e);
        }());
    </script>
    <%}%>
</body>
</html>
