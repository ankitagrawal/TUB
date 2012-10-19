<!DOCTYPE html>
<html>
<head>
		<%@ include file='header.jsp' %>	
	
</head>
<body>
<div data-role="page" id=paymentSuccessOption class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtn.jsp'%>
</div>
<div data-role="content" style='height:auto'>
	
				<div style='padding:10px;margin-bottom:10px;font-size:16px'>
				<center>Your Order(#<%=request.getParameter("orderId")%>) has been successfully placed. </center>
				</div>
			
<!-- end left -->


<!-- start right pannel -->

  	<%@ include file='menuFooter.jsp' %>
</div>
<!-- end right pannel -->

</div>
	
<script>
$('#paymentSuccessOption').bind('pageshow',function(){
		$.mobile.urlHistory.stack = [];
});

</script>
</div>
</body>
</html>
