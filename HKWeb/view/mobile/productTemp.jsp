<!DOCTYPE html>
<html>
<head>

</head>
<body>
	<div data-role="page" id=productTemp class="type-home">

		<div data-role="content" style='background-color: white'></div>



<script>
	$('#productTemp').bind('pageshow',function() {
		$('div[id=product]').remove();
		var urlEval = new URLEval();
		
		var x = $.mobile.path.parseUrl(urlEval.getURLFromHash(location.href));
		var queryString = x.search;
		$.mobile.changePage('${pageContext.request.contextPath}/mobile/product.jsp'+ queryString);
		
	});
</script>
	</div>

</body>
</html>
