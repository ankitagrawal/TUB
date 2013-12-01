<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=productDetails class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		<div data-role="content" style='background-color: white'>

			<div
				style='clear: both; padding-top: 6px; background-color: #ddd; font-weight: bold; text-align: center'>
				Product Detail</div>

			<table width=100% style='font-size: 12px' id='productDetailMain'
				class='variantList'>
			</table>

			<div
				style='clear: both; padding: 4px; background-color: #ddd; text-align: center; margin-bottom: 6px; font-weight: bold'>
				Variants</div>
			<ul data-role=listview style='padding-top: 4px; padding-bottom: 20px'
				id='variantList'>
			</ul>

			<%@ include file='menuFooter.jsp'%>
		</div>

		<%@ include file='template/product-details-templates.jsp'%>
		<script type="text/javascript" src="${httpPath}/js/product-details.js"></script>
	</div>

</body>
</html>
