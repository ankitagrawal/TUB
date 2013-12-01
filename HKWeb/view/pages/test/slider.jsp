<!DOCTYPE html>
<%@ include file="/includes/_taglibInclude.jsp" %>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>jQuery UI Slider - Range slider</title>
  <link href="<hk:vhostCss/>/css/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
	<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.ui.all.css">--%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jquery.session.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.8.16.custom.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.core.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.mouse.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.slider.js"></script>
  <link href="<hk:vhostCss/>/css/demos.css" rel="stylesheet" type="text/css"/>
	<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/demos.css">--%>
	<style>
	#demo-frame > div.demo { padding: 10px !important; };
	</style>
	<script>
	$(function() {
		$( "#slider-range" ).slider({
			range: true,
			min: 0,
			max: 500,
			values: [ 75, 300 ],
			slide: function( event, ui ) {
				$( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
			}
		});
		$( "#amount" ).val( "$" + $( "#slider-range" ).slider( "values", 0 ) +
			" - $" + $( "#slider-range" ).slider( "values", 1 ) );
	});
	</script>
</head>
<body>

<div class="demo">

<p>
	<label for="amount">Price range:</label>
	<input type="text" id="amount" style="border:0; color:#f6931f; font-weight:bold;" />
</p>

<div id="slider-range"></div>

</div><!-- End demo -->



<div class="demo-description">
<p>Set the <code>range</code> option to true to capture a range of values with two drag handles.  The space between the handles is filled with a different background color to indicate those values are selected.</p>
</div><!-- End demo-description -->

</body>
</html>