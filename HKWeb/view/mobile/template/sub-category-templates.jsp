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
<script type='text/template' id='sub-category-template'>
	<a href='product.jsp?primaryCategory={{print(currentCategory)}}&secondaryCategory={{print(encodeURIComponent(url))}}'>
		{{print(name)}}
	</a>
</script>