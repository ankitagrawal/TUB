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