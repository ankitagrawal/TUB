<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 9, 30, 23, 59, 59, 59).getMillis());
%>
<%
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>

<div style="margin-left: auto; margin-right: auto; width:960px;">
  <img src="${pageContext.request.contextPath}/images/banners/top/parenting-promotional-banner.jpg" alt="On Orders of Rs. 1000.0 and above, get 10% cashback. Offer not valid on Diapers, Baby food & Supplements & Bottles.">
</div>

<%
  }
%>
