<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
  if (AnalyticsConstants.analytics) {
%>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '<%=AnalyticsConstants.gaCode%>']);
<%
  String signupDate = (String) session.getAttribute(HealthkartConstants.Session.signupDate);
  if (StringUtils.isNotBlank(signupDate)) {
    session.removeAttribute(HealthkartConstants.Session.signupDate);
%>
  _gaq.push(['_setCustomVar',
    <%=AnalyticsConstants.CustomVarSlot.signUpDate.getSlot()%>,
    "<%=AnalyticsConstants.CustomVarSlot.signUpDate.getName()%>",
    "<%=signupDate%>",
    <%=AnalyticsConstants.CustomVarSlot.signUpDate.getScope().getLevel()%>
  ]);
<%
  }
%>
<%
  String userId = (String) session.getAttribute(HealthkartConstants.Session.userId);
  if (StringUtils.isNotBlank(userId)) {
    session.removeAttribute(HealthkartConstants.Session.userId);
%>
  _gaq.push(['_setCustomVar',
    <%=AnalyticsConstants.CustomVarSlot.userId.getSlot()%>,
    "<%=AnalyticsConstants.CustomVarSlot.userId.getName()%>",
    "<%=userId%>",
    <%=AnalyticsConstants.CustomVarSlot.userId.getScope().getLevel()%>
  ]);
<%
  }
%>
<%
  Boolean orderCountSet = (Boolean) session.getAttribute(HealthkartConstants.Session.orderCountSetBoolean);
  Integer orderCount = (Integer) session.getAttribute(HealthkartConstants.Session.orderCount);
  if ((orderCountSet == null || !orderCountSet) && (orderCount != null)) {
    session.setAttribute(HealthkartConstants.Session.orderCountSetBoolean, true);
%>
  _gaq.push(['_setCustomVar',
    <%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getSlot()%>,
    "<%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getName()%>",
    "<%=orderCount%>",
    <%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getScope().getLevel()%>
  ]);
<%
  }
%>
  _gaq.push(['_trackPageview']);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>

<!-- Start Visual Website Optimizer Asynchronous Code -->
<script type='text/javascript'>
	var _vwo_code=(function(){
	var account_id=34756,
	settings_tolerance=2000,
	library_tolerance=1500,
	use_existing_jquery=false,
	// DO NOT EDIT BELOW THIS LINE
	f=false,d=document;return{use_existing_jquery:function(){return use_existing_jquery;},library_tolerance:function(){return library_tolerance;},finish:function(){if(!f){f=true;var a=d.getElementById('_vis_opt_path_hides');if(a)a.parentNode.removeChild(a);}},finished:function(){return f;},load:function(a){var b=d.createElement('script');b.src=a;b.type='text/javascript';b.innerText;b.onerror=function(){_vwo_code.finish();};d.getElementsByTagName('head')[0].appendChild(b);},init:function(){settings_timer=setTimeout('_vwo_code.finish()',settings_tolerance);this.load('//dev.visualwebsiteoptimizer.com/j.php?a='+account_id+'&u='+encodeURIComponent(d.URL)+'&r='+Math.random());var a=d.createElement('style'),b='body{opacity:0 !important;filter:alpha(opacity=0) !important;background:none !important;}',h=d.getElementsByTagName('head')[0];a.setAttribute('id','_vis_opt_path_hides');a.setAttribute('type','text/css');if(a.styleSheet)a.styleSheet.cssText=b;else a.appendChild(d.createTextNode(b));h.appendChild(a);return settings_timer;}};}());_vwo_settings_timer=_vwo_code.init();
</script>
<!-- End Visual Website Optimizer Asynchronous Code -->

<%
  }
%>
