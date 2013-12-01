<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 3, 4, 23, 59, 59, 59).getMillis());
%>
<%
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <a href="${pageContext.request.contextPath}/product/organic-india-gulal/NUT904"><img src="${pageContext.request.contextPath}/images/banners/top/gulal_strip_banner.jpg" alt="Happy Holi!"></a>
</div>
<%
  }
%>