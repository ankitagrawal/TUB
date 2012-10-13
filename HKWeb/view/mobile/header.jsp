<%@ include file="/includes/_taglibInclude.jsp" %>
<c:set var="httpPath" value="" />
<link	href="${httpPath}css/custom-theme/jquery-ui-1.8.21.custom.css"	rel="stylesheet" type="text/css" />
<script type="text/javascript"	src="${httpPath}js/jquery-1.7.1.min.js"></script>
<script type="text/javascript"	src="${httpPath}js/jquery-ui-1.8.23.custom.min.js"></script>
<script>
var wSURL = '/healthkart/rest/api/';
var wSURLCart = 'http://122.176.33.182:9090/healthkart/core/cart/AddToCart.action';
var httpPath = '${httpPath}';
</script>
<script src="${httpPath}js/json2.js"></script>
<script src="${httpPath}js/underscore-min.js"></script>
<script src="${httpPath}js/backbone.js"></script>

<script src='${httpPath}js/general.js'></script>



<meta name="viewport" content="minimum-scale=1.0, width=device-width, maximum-scale=1.0, user-scalable=no"  />
<link  rel="stylesheet" href="${httpPath}css/themes/default/jquery.mobile-1.1.1.css"/>
<link  rel="stylesheet" href="${httpPath}css/themes/default/jquery.mobile.structure-1.1.1.css"/>
<script>
$(document).bind("mobileinit", function(){
$.mobile.page.prototype.options.headerTheme = "c";  // Page header only
//$.mobile.pushStateEnabled=false;
$.mobile.changePage.defaults.allowSamePageTransition = true;
	$('div[data-role=page]').live('scrollstart',function(){
			$('input').blur();
		});	
			jQuery.support.cors = true;
		
	_.templateSettings = {
			evaluate : /\{\{(.+?)\}\}/g
		};
		
	Backbone.emulateJSON = true;
	$(document).on('keypress',".searchContainer input",function(e){
				
				if(e.keyCode == 13)
				{
				
				/*	if($.mobile.activePage.attr('id')=='product'){
						location.href='product.jsp?query='+encodeURIComponent($(this).val())+'&pageNo=1&perPage=10';
					}
					else
					{
					$.mobile.changePage('product.jsp?query='+encodeURIComponent($(this).val())+'&pageNo=1&perPage=10');
					}
					*/
					$.mobile.changePage('productTemp.jsp?query='+encodeURIComponent($(this).val())+'&pageNo=1&perPage=10');
				}
		});
	});
</script>
<script src="${httpPath}js/jquery.mobile-1.1.1.min.js"></script>
<script>

</script>
<link href="${httpPath}css/jquery.validate.css" rel="stylesheet" type="text/css" />
<script src="${httpPath}js/jquery.validate.js" type="text/javascript"></script>
  
 <link rel="stylesheet" href="${httpPath}css/default.css" type="text/css"  /> 
	
	
	