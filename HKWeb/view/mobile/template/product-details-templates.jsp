<script type='text/template' id='product-detail-main-template'>
	<td class='image-container'>
		<img src='{{print(imageUrl)}}'/>
	</td>
	<td class='text-container' style='padding:20px'>
		<h3 class='ui-li-heading' style='margin:0px;white-space:normal'>{{print(name)}}</h3>
		<p style='margin:0px'>
			<span class='svPrcnt'>
				{{print(brand)}}
				{{ if(minDays>0 && maxDays>0) { }}
					<br>Delivered in: {{print(minDays)}} - {{print(maxDays)}} days
				{{ } }}
			</span>			
			{{if(codAllowed == false) { }}
				<span class='redText'>COD Not Available</span>
			{{ } }}
		</p>
	</td>					
</script>
<script type='text/template' id='product-variant-template'>
{{
	var urlEval = new URLEval();
    var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
    var queryString = x.search;
	var productReferrerId = getURLParameterValue(queryString,'productReferrerId');
}}
<table class='variantList' width=100%>
	<tr>
		{{ if(productOptions.length>0) { }}
			<td class='variant-description'>
				{{for(i=0;i<productOptions.length;i++) {
					print("<span class='svPrcnt'>"+productOptions[i].name+" : "+productOptions[i].value+"</span>");
				} }}					
			</td>
		{{ } }}
		<td class='text-container'>
			<p style='padding-top: 3px'>
				{{ if(hkPrice != 0) { }} 
					<strike>Rs {{print(markedPrice)}}</strike>
					<span class='ofrPrc'> Rs {{print(hkPrice)}}</span> 
				{{ } else { }} 
					<span class='ofrPrc'>Rs {{print(markedPrice)}}</span> 
				{{ } }}
				{{if(discountPercent > 0) { }} 
					<span class='svPrcnt'>{{print(discountPercent)}}% off</span> 
				{{ } }}
			</p>
			<a class='ad2Crt' href='#' data-url="productVariantId={{print(id)}}&productId={{print(id.substring(0,id.indexOf('-')))}}&productReferrerId={{print(productReferrerId)}}">
				Place Order
			</a>
		</td>
	</tr>
</table>
</script>