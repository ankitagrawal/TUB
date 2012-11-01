<!DOCTYPE html>
<html>
<head>
	<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=orderSuccess class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>

			<div
				style='background-color: #319aff; padding: 10px; margin-bottom: 10px; color: white; font-size: 16px'>
				<center>Order Summary</center>
			</div>

			<!-- end left -->


			<!-- start right pannel -->
			<div id='defaultViewContent' class=viewContent>


				<div id=accord>
					<div class=accordCont>
						<h3>Cash On delivery</h3>
						<div>
							<pre>
								<h2>Order Total</h2>Order Total Rs. 428.00
COD Charges Rs. 70.00 
Grand Total Rs. 498.00
<b>Contact Details</b>
Please verify the name and contact number of the person who will receive this order. 
You will receive a phone call within 1 business day to confirm your order before it is sent for processing.

<b>Contact Name</b>
Sunit Jindal
<b>Contact Phone</b>
9266780108
<a href='orderSuccess.jsp' class=ad2Crt>Confirm</a>
</pre>


						</div>
					</div>
					<div class=accordCont>
						<h3>Credit Card</h3>
						<div>Coming Soon</div>
					</div>
					<div class=accordCont>
						<h3>Net Banking</h3>
						<div>Coming Soon</div>
					</div>
					<div class=accordCont>
						<h3>Cheque Payment</h3>
						<div>Coming Soon</div>
					</div>
				</div>

			</div>
			<!-- end right pannel -->

		</div>
		<style>
#accord {
	width: 95%;
	margin: 0px auto;
}

#accord .accordCont {
	margin-bottom: 10px
}

#accord .accordCont h3 {
	background-color: #C5D3DD;
	margin-bottom: 0px;
	padding: 8px;
}

#accord .accordCont div {
	border: 1px solid #C5D3DD;
	border-top: none;
	padding: 5px;
}
</style>

		<script>
			$('#paymentOption').bind('pageshow', function() {
				$('.accordCont div').hide();
				$('.accordCont:nth-child(1) div').show();
				$('#accord').on('click', '.accordCont h3', function(e) {
					$('.accordCont div').hide();

					$(e.currentTarget).siblings('div').show();
				});

			});
		</script>
	</div>
</body>
</html>
