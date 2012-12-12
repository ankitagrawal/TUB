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
<%
  }
%>
