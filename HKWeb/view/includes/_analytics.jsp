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
<%
  }
%>