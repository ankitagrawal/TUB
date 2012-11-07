<script type='text/template' id='cart-template'>	
	<table width='100%'>
		<tr>
			<td class='image-container'><img src='{{print(imageUrl)}}' /></td>
			<td class='text-container'>
				<h3 style='white-space: normal'>{{print(name)}}</h3>
				<p>
					<strike>Rs {{print(markedPrice)}}</strike> 
					<span class='ofrPrc'>Rs	{{print(hkPrice)}}</span>
					{{ if(discountOnHkPrice>0) {}} 
						<span class='svPrcnt'>Rs {{print(discountOnHkPrice)}} off</span> 
					{{ } }}	
				</p>
			</td>
		</tr>
		<tr>
			
				{{ if(productOptions.length>0) { }}
			<td class='variant-description' style='padding-left:20px;font-size:12px;font-weight:normal'colspan=2>
				{{for(i=0;i<productOptions.length;i++) {
					print("<span class='svPrcnt'>"+productOptions[i].name+" : "+productOptions[i].value+"</span>");
				} }}					
			</td>
		{{ } }}
			
		</tr>
	</table>
	<table style='width:90%;margin:0px auto'>
		<tr>
			<td>
				<table style='margin:0px auto'>
					<tr>
						<td class='su2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}&qty={{print(qty - 1)}}'>
							<img src='${httpPath}/images/sub.png' alt='Minus'/>
						</td>
						<td>
							<input type='text' value='{{print(qty)}}'  style='text-align:center;width:30px;margin:0px auto; border:none' readonly />
						</td>
						<td class='pl2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}&qty={{print(qty + 1)}}'>
							<img src='${httpPath}/images/plus.png' alt='Plus'/>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<a href='javascript:void(0)' class='rm2Crt' data-url='cartLineItemId={{print(cartLineItemId)}}'>
					<img src='${httpPath}/images/remove.png'/>
				</a>
			</td>
		</tr>
	</table>			
</script>
<script type="text/template" id="cart-total-template">
	
	{{if( productsMrpSubTotal + prepaidServiceMrpSubTotal + postpaidServiceMrpSubTotal > productsHkSubTotal + prepaidServiceHkSubTotal + postpaidServiceHkSubTotal) { }}
		<div class=product-mrp >
			Total Price : Rs. {{ print(productsMrpSubTotal + prepaidServiceMrpSubTotal + postpaidServiceMrpSubTotal) }}
		</div>
	{{ } }}
		
	<div class=product-hk-price>
		Our Price : Rs. {{ print(productsHkSubTotal + prepaidServiceHkSubTotal + postpaidServiceHkSubTotal) }}
	</div>

	{{ if(productsMrpSubTotal + prepaidServiceMrpSubTotal > productsHkSubTotal + prepaidServiceHkSubTotal +postpaidServiceHkSubTotal) { }}
		<div class='product-discount green'>
			<span class='product-discount-text'>HealthKart Discount</span> : (<span class='blue value'>Rs. {{print(totalHkProductsDiscount + totalHkPrepaidServiceDiscount + totalHkPostpaidServiceDiscount - totalPostpaidAmount)}}</span>)
		</div>
	{{ } }}

	{{ if(shippingSubTotal - shippingDiscount >= .005) { }}
		<div class='product-shipping green'>
			Shipping : (<span class=blue>Rs. {{ print(shippingSubTotal - shippingDiscount) }}</span>)
		</div>
	{{ } }}

	{{ if(shippingSubTotal - shippingDiscount == 0) { }}
		<div class=product-shipping>
			Shipping : Free!
		</div>
	{{ } }}

	{{ if(subscriptionDiscount > 0) { }}
		<div class='product-subscription-discount green'>
			Subscription Discount : (<span class=blue>Rs. {{ print(subscriptionDiscount) }}</span>)
		</div>
	{{ } }}

	{{if(totalPromoDiscount > 0) { }}
		<div class='product-promo-discount green'>
			Promo Discount : (<span class='blue value'>Rs. {{ print(totalPromoDiscount) }}</span>)
		</div>
	{{ } }}
		
	<div class='grand-total green'>
		you pay <div class='orange value'>Rs. {{ print(grandTotalPayable) }}</div>
		<sub>(inclusive of shipping, handling and taxes.)</sub>
	</div>

	{{ if(totalCashback > 0) { }}
		<div class='product-cashback'>
			Total Cash back : Rs. {{ print(totalCashback) }}
		</div>
	{{ } }}
</script>
