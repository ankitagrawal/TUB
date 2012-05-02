<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/logo/HealthKartLogo.png">
	
<!-- YUI CSS -->
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/yui/container/assets/skins/sam/container.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/yui/container/assets/skins/sam/container-skin.css" />

<!-- YUI Javascript -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/yui/element/element-min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/yui/container/container-min.js"></script>

<!-- HC CSS -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/hc.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/shadowbox.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/tab-style.css" />

<!-- HC Scripts -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/hc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/field-validation.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<script type="text/javascript">
function goToFacebookPage(){
	window.open("http://www.facebook.com/healthkart?sk=app_148326041894539");
}
</script>

<!-- Code for Google Analytics -->
<!-- Keep it commented for Local and Dev -->

<!-- 
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-21820217-1']);
  _gaq.push(['_trackPageview']);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
 -->