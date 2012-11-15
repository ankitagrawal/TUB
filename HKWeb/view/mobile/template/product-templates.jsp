<script type='text/template' id='product-fav-template'>
	<tr>
	
		{{for(var i =0;i<favItem.length;i++){ }}
		<td class=scrollBlock>
		
			<div class=scrollBlockContent>
				<img src='images/{{print(favItem[i].img)}}'/>
				<div>
					{{print(favItem[i].label)}}
				</div>
			</div>
		</td>
		{{ } }}
	
	</tr>
</script>
<script type='text/template' id='product-list-template'>			
	<a href='${httpPath}/productDetails.jsp?productSlug={{print(productSlug)}}&productId={{print(encodeURIComponent(id))}}&productReferrerId={{print(productReferrerId)}}'>
		<table width='100%'>
			<tr>
			<td class='image-container'>
				<img src='{{print(imageUrl)}}'/>
			</td>
			<td class='text-container'>
				<h3 style='white-space:normal'>{{print(name)}}</h3>
				<p>
					<strike>Rs {{print(markedPrice)}}</strike>
					<span class='ofrPrc'>Rs {{print(hkPrice)}}</span>
					{{if(discountPercentage > 0) { }}
						<span class='svPrcnt'>{{print(discountPercentage)}}% off</span>
					{{ } }}
					{{if(outOfStock == true) { }}
						<span class='redText'>Out of Stock</span>
					{{ } }}
				</p>
			</td>
			</tr>
		</table>
	</a>		
</script>