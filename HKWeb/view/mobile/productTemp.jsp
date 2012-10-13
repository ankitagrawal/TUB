<!DOCTYPE html>
<html>
<head>
	
</head>
<body>
<div data-role="page" id=productTemp class="type-home">

	<div data-role="content" style='background-color:white'>
	
	</div>
	
	
		
<script>

$('#productTemp').bind('pageshow',function(){
		var urlEval = new URLEval();
		//alert(urlEval.getURLFromHash(location.href));
var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
		var queryString = x.search;
		$.mobile.changePage('product.jsp'+queryString);
		/**Backbone code for product list*E*/
});

</script>


	
</div>

</body>
</html>
