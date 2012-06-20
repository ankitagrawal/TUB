<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
  if (AnalyticsConstants.analytics) {
%>
<script type="text/javascript">
  var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
  document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
  var pageTracker = _gat._getTracker("<%=AnalyticsConstants.gaCode%>");
y</script>
<%

  String signupDate = (String) session.getAttribute(HealthkartConstants.Session.signupDate);
  if (StringUtils.isNotBlank(signupDate)) {
    session.removeAttribute(HealthkartConstants.Session.signupDate);
%>
<script type="text/javascript">
  pageTracker._setCustomVar(
       <%=AnalyticsConstants.CustomVarSlot.signUpDate%>, // This custom var is set to slot #3.  sign_up_date
      "signupDate", // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=signupDate%>", // This value of the custom variable.  Required parameter.
      <%=AnalyticsConstants.CustomVarScope.visitorLevel%>                   // Sets the scope to visitor-level. Optional parameter.
      );
</script>
<%
  }
%>
<%
  String userId = (String) session.getAttribute(HealthkartConstants.Session.userId);
  if (StringUtils.isNotBlank(userId)) {
    session.removeAttribute(HealthkartConstants.Session.userId);
%>
<script type="text/javascript">
  pageTracker._setCustomVar(
      <%=AnalyticsConstants.CustomVarSlot.userId%>, // This custom var is set to slot #4.  user_id
      "userId", // The name acts as a kind of category for the user activity.  Required parameter.
      "<%=userId%>", // This value of the custom variable.  Required parameter.
      <%=AnalyticsConstants.CustomVarScope.visitorLevel%>                    // Sets the scope to visitor-level. Optional parameter.
      );
</script>

<%
  }
%>

<script type="text/javascript">
  pageTracker._trackPageview();
</script>
<%
  }
%>
