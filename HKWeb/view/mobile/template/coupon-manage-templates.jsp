<script id='coupon-manage-template' type='text/template'>
	<td class='couponSelectFlag'>
		<input type=radio name='couponId' {{if(selectedOffer==true)print('checked')}} value='{{print(id)}}' />
	</td>
	<td class='couponDesc'>
		{{print(offerDescription)}}
		<!--<h3>Terms:</h3>
		{{print(offerTerms)}}-->
	</td>
	<td class='couponDate'>
		{{print(endDate)}}
	</td>
	<td class='couponCode'>
		{{print(couponCode)}}
	</td>
</script>