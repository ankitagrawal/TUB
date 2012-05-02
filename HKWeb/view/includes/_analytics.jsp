<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%
  if (AnalyticsConstants.analytics) {
%>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
pageTracker._trackPageview();
</script>

<%--Ohana--%>
<script type ="text/javascript" src="http://static.adohana.com/oa.js?usr=1edbc47d5a62f6f01609bb23f923f082"></script>

<!-- begin Marin Software Tracking Script -->
<script type='text/javascript'>
    var _mTrack = _mTrack || [];
    _mTrack.push(['trackPage']);

    (function() {
        var mClientId = '3035ucg15805';
        var mProto = ('https:' == document.location.protocol ? 'https://' : 'http://');
        var mHost = 'pro.marinsm.com';
        var mt = document.createElement('script'); mt.type = 'text/javascript'; mt.async = true;
        mt.src = mProto + mHost + '/tracker/async/' + mClientId + '.js';
        var fscr = document.getElementsByTagName('script')[0]; fscr.parentNode.insertBefore(mt, fscr);
    })();
</script>
<!-- end Copyright Marin Software -->

<%
  }
%>