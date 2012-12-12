<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=product class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		<div data-role="content" style='background-color: white'>

			<div style='clear: both; padding: 1px 0px'></div>

			<div style='padding-top: 4px' id='productList' data-pageNo='1'
				data-perPage='10' data-more='true' data-request='free'>
				<ul>
				</ul>
			</div>
			<br> <br>
			<%@ include file='menuFooter.jsp'%>
		</div>
		<%@ include file='template/product-templates.jsp'%>
		<script type='text/javascript' src='${httpPath}/js/product.js'></script>
	</div>
</body>
</html>
