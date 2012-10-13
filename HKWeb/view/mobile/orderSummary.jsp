<!DOCTYPE html>
<html>
<head>
		<%@ include file='header.jsp' %>	
	
</head>
<body>
<div data-role="page" id=orderSummary class="type-home">
<div data-role=header>
	<%@ include file='menuHeader.jsp'%>
	<%@ include file='menuNavBtn.jsp'%>
</div>
	<div data-role="content" style='height:auto'>
	
				<div style='background-color:#319aff;padding:10px;margin-bottom:10px;color:white;font-size:16px'>
				<center>Order Summary</center>
				</div>
			</div>
<!-- end left -->


<!-- start right pannel -->
<div id='defaultViewContent' class=viewContent>

  <div id="signin-block">
   <div style='padding:10px'>
   <div style='border:1px solid #234;margin-bottom:10px;padding:5px'>
   <h2>Address 1</h2>
   Plot No:233<br>
   Sector 14, Gurgaon<br>
   Pin-123231, Haryana
   <div><a href='#'>Delete</a></div>
   </div>
   <div style='border:1px solid #234;margin-bottom:10px;padding:5px'>
   <h2>You selected</h2>
   <ul data-role=listview>
   <li>product 1(Qty:2,Amt:Rs 2000)
   <li>product 2(Qty:5,Amt:Rs 800)
   <li>product 3(Qty:1,Amt:Rs 2600)
   </ul>
  
   </div>
   <div style='border:1px solid #234;margin-bottom:10px;padding:5px'>
   <h2>Instuctions(If any)</h2>
   <textarea ></textarea>
  
   </div>
   </div>
   <a href='paymentOption.jsp' data-role=button>Make Payment</a>
  </div> 
</div>
<!-- end right pannel -->

</div>

	
<script>
$('#orderSummary').bind('pageshow',function(){

		
	
});

</script>
</div>
</body>
</html>
