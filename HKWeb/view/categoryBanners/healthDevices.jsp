<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2013, 10, 31, 23, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
  <img src="${pageContext.request.contextPath}/images/banners/HomeDevices/Healthviva-Strip-Banner.jpg"
       alt="Get 10% additional discount on High Quality Body Supports from HealthViva!">
</div>
<%
  }
%>
