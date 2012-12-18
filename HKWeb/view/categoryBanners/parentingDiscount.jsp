<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 11, 21, 23, 59, 59, 59).getMillis());
%>
<%
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<%--
<div style="margin-left: auto; margin-right: auto; width:960px;">
  <img src="${pageContext.request.contextPath}/images/banners/top/parenting_deewalil-banner.jpg" alt="Get Flat 5% off on Baby Diapers. Offer Expires on 21st Nov, 2012.">
</div>
--%>
<%
  }
%>
