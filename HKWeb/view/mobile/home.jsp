<!DOCTYPE html>
<html>
<head>
<title>Health Kart | Online health Store</title>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=primaryCategory class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		<div data-role="content"
			style='background-color: white; margin-top: 10px'>
			<div id='categoryContainer'>
				<ul data-inset=true>
				</ul>
			</div>
			<%@ include file='menuFooter.jsp'%>

		</div>

		<!--div data-role="footer" class="footer-docs" data-theme="c">
			<p>&copy; 2012 jQuery Foundation and other contributors</p>
	</div-->
		<%@ include file="template/home-templates.jsp"%>
		<script type='text/javascript' src='${httpPath}/js/home.js'></script>



	</div>
</body>
</html>
