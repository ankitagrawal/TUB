<script id="summary-view-template" type='text/template'>
  <div style='padding:10px'>
   	<div style='border-bottom:none;border-right:none;margin-bottom:15px;padding:5px;box-shadow:2px 2px 8px #888,-2px -2px 8px #888;-webkit-box-shadow:2px 2px 8px #888,-2px -2px 8px #888'>
   		{{ if(data.addressSelected!=null){ }}
  			<h2>Delivery Address</h2>
    			<div class='addressText'>
					<b>{{print(data.addressSelected.name)}}</b><br>
					{{print(data.addressSelected.line1)}}<br>
					{{print(data.addressSelected.line2)}}<br>
					{{print(data.addressSelected.city)}}, {{print(data.addressSelected.state)}}<br>
					PIN Code: {{print(data.addressSelected.pin)}}<br>
					Ph: {{print(data.addressSelected.phone)}}<br>
				</div>
  		{{ } }}
   </div>
	<div style='border-bottom:none;border-right:none;margin-bottom:15px;padding:5px;box-shadow:2px 2px 8px #888,-2px -2px 8px #888;-webkit-box-shadow:2px 2px 8px #888,-2px -2px 8px #888'>
   		<h2>You selected</h2>
   		<table style='width:100%;text-align:center;font-size:1.2em'>
   			<tr style='background:url(images/navbar_bg.png);color:white;webkit-text-shadow:none;text-shadow:none;'>
				<th>Product</th>
				<th>Qty</th>
				<th>Amt(Rs.)</th>
			</tr>
   		{{ for(var i=0;i<data.cartItems.length;i++) { }}
   			<tr style='background-color:#eee'>
				<td>{{print(data.cartItems[i].name)}}</td>
				<td>{{print(data.cartItems[i].qty)}}</td>
				<td>{{print(data.cartItems[i].hkPrice)}}</td>
			</tr>
    	{{ } }}
		</table>

		{{ if(data.pricingDto!=null){ }}
	   		<h2 style='color:#888'>Shipping: Rs.{{print(data.pricingDto.shippingTotal)}}</h2>
	  		<h3 style='color:#888'>You Saved: Rs.{{print(data.pricingDto.totalPromoDiscount+data.pricingDto.subscriptionDiscount+data.pricingDto.totalHkProductsDiscount + data.pricingDto.totalHkPrepaidServiceDiscount + data.pricingDto.totalHkPostpaidServiceDiscount - data.pricingDto.totalPostpaidAmount)}}</h3>
	   		<h2 style='color:#F87500'>Total: Rs.{{print(data.pricingDto.grandTotalPayable)}}</h2>
		{{ } }}
	</div>
</div>
</script>