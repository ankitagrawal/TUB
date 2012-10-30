<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id='sub-category'>
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		
		<div data-role="content" style='background-color: white'>
			<div id='subCategoryContainer' style='clear: both; padding: 1px 0px'>
				<ul data-inset=true data-role=listview>
				</ul>
			</div>
			
			<%@ include file='menuFooter.jsp'%>
		</div>

		<%@ include file='template/sub-category-templates.jsp'%>
		<script type='text/javascript' src='${httpPath}/js/sub-category.js'></script>
	</div>

</body>
</html>
