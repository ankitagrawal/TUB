<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 9, 15, 23, 59, 59, 59).getMillis());
%>
<%
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount - 10% cashback on prepaid orders">
  <%
    }
  %>
</div>
