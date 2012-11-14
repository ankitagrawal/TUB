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
    <%=AnalyticsConstants.CustomVarSlot.signUpDate%>, // This custom var is set to slot #3.  sign_up_date
    "signupDate", // The name acts as a kind of category for the user activity.  Required parameter.
    "<%=signupDate%>", // This value of the custom variable.  Required parameter.
    <%=AnalyticsConstants.CustomVarScope.visitorLevel%>                   // Sets the scope to visitor-level. Optional parameter.
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
    <%=AnalyticsConstants.CustomVarSlot.userId%>, // This custom var is set to slot #4.  user_id
    "userId", // The name acts as a kind of category for the user activity.  Required parameter.
    "<%=userId%>", // This value of the custom variable.  Required parameter.
    <%=AnalyticsConstants.CustomVarScope.visitorLevel%>                    // Sets the scope to visitor-level. Optional parameter.
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
<%
  }
%>
