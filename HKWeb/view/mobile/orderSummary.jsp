<!DOCTYPE html>
<html>
<head>
<%@ include file='header.jsp'%>

</head>
<body>
	<div data-role="page" id=orderSummary class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>
			<div
				style='background-color: #319aff; padding: 10px; margin-bottom: 10px; color: white; font-size: 16px'>
				<center>Order Summary</center>
			</div>
		</div>
	
		<div id='defaultViewContent' class=viewContent>
			<div id="summary-address-block"></div>
			<form id="order_makeapayment">
				<div
					style='border-bottom: none; border-right: none; padding: 10px;; margin: 10px; box-shadow: 2px 2px 8px #888, -2px -2px 8px #888; -webkit-box-shadow: 2px 2px 8px #888, -2px -2px 8px #888'>
					<h2>Instructions(If any)</h2>
					<textarea name="comment" id="orderSummary_comments"></textarea>
					<h2>COD Contact Name</h2>
					<textarea name="codContactName" id="orderSummary_name"></textarea>
					<h2>COD Contact Number</h2>
					<textarea name="codContactPhone" id="orderSummary_phone"></textarea>

				</div>
				<a href='javascript:void(0)' id="orderSummary_makepayment"
					style='width: 95%; margin: 0px auto; margin-bottom: 10px'
					data-role=button>Place Order</a>
			</form>
		</div>
	</div>
	
		<%@ include file='menuFooter.jsp'%>
	</div>

	<%@include file='template/order-summary-templates.jsp'%>

	<script type="text/javascript" src='${httpPath}/js/order-summary.js'>
		
	</script>
	</div>
</body>
</html>