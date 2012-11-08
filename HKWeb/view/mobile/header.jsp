<%@ include file="/includes/_taglibInclude.jsp"%>
<c:set var="httpPath" value="${pageContext.request.contextPath}/mobile" />

<!--link href="${httpPath}/css/custom-theme/jquery-ui-1.8.21.custom.css"
	rel="stylesheet" type="text/css" /-->
<script type="text/javascript" src="${httpPath}/js/jquery-1.7.1.min.js"></script>
<!--script type="text/javascript"
	src="${httpPath}/js/jquery-ui-1.8.23.custom.min.js"></script-->
<script type="text/javascript">
var wSURL = '${pageContext.request.contextPath}/rest/api/';
//var wSURL = 'http://mbeta.healthkart.com/rest/api/';
	var httpPath = '${httpPath}';
</script>
<script src="${httpPath}/js/json2.js"></script>
<script src="${httpPath}/js/underscore-min.js"></script>
<script src="${httpPath}/js/backbone-min.js"></script>
<script src='${httpPath}/js/general.js'></script>



<meta name="viewport"
	content="minimum-scale=1.0, width=device-width, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet"
	href="${httpPath}/css/themes/default/jquery.mobile-theme-1.1.1.min.css" />
<link rel="stylesheet"
	href="${httpPath}/css/themes/default/jquery.mobile.structure-1.1.1.min.css" />
<script>
	$(document).bind(
					"mobileinit",
					function() {
						$.mobile.page.prototype.options.headerTheme = "c"; // Page header only
						//$.mobile.pushStateEnabled=false;
						$.mobile.changePage.defaults.allowSamePageTransition = true;
						$.mobile.defaultPageTransition = "none";
						$('div[data-role=page]').live('scrollstart',
								function() {
									$('input').blur();
								});
						jQuery.support.cors = true;

						_.templateSettings = {
							evaluate : /\{\{(.+?)\}\}/g
						};

						Backbone.emulateJSON = true;
						$(document).on(
										'keypress',
										".searchContainer input",
										function(e) {

											if (e.keyCode == 13) {
												$.mobile.changePage('${httpPath}/productTemp.jsp?query='
																+ encodeURIComponent($(this).val()));
											}
										});
					});
</script>
<script src="${httpPath}/js/jquery.mobile-1.1.1.min.js"></script>
<script>
	function logout() {
		$.ajax({
			url : wSURL + "mLogin/logout",
			type : 'get',
			async : false,
			success : function(data) {
				$.mobile.urlHistory.stack = [];
				setTimeout(function() {
					location.href = '${httpPath}/home.jsp'
				}, 500);
			},
			error : function(data) {
				$.mobile.urlHistory.stack = [];
				setTimeout(function() {
					location.href = '${httpPath}/home.jsp'
				}, 500);
			}
		});
	}
</script>
<link href="${httpPath}/css/jquery.validate.css" rel="stylesheet"
	type="text/css" />
<script src="${httpPath}/js/jquery.validate.js" type="text/javascript"></script>

<link rel="stylesheet" href="${httpPath}/css/default.css"
	type="text/css" />


