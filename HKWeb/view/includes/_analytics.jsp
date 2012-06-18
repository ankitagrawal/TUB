<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%
  if (AnalyticsConstants.analytics) {
%>
<script type="text/javascript">
  var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
  document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<%

  if (session.getAttribute(HealthkartConstants.Session.signupDate) != null && session.getAttribute(HealthkartConstants.Session.signupDate) != "") {
    String signupDate = session.getAttribute(HealthkartConstants.Session.signupDate).toString();
    session.removeAttribute("signupDate");
%>
<script type="text/javascript">
  var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
  pageTracker._setCustomVar(
      3, // This custom var is set to slot #3.  sign_up_date
      "signupDate", // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=signupDate%>", // This value of the custom variable.  Required parameter.
      1                   // Sets the scope to visitor-level. Optional parameter.
      );
</script>
<%
  }
%>
<%
  if (session.getAttribute(HealthkartConstants.Session.userId) != null && session.getAttribute(HealthkartConstants.Session.userId) != "") {
    String userId = session.getAttribute(HealthkartConstants.Session.userId).toString();
    session.removeAttribute("userId");
%>
<script type="text/javascript">
  var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
  pageTracker._setCustomVar(
      4, // This custom var is set to slot #4.  user_id
      "userId", // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=userId%>", // This value of the custom variable.  Required parameter.
      1                   // Sets the scope to visitor-level. Optional parameter.
      );
</script>

<%
  }
%>

<script type="text/javascript">
  var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
  pageTracker._trackPageview();
</script>
<%
  }
%>
