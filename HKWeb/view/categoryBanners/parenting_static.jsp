<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<style type="text/css">
  #sendCouponLink {
    text-decoration: none;
    border-bottom: 0;
  }

  #sendCouponLink :hover {
    text-decoration: none;
    border-bottom: 0;
  }
</style>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2013, 9, 21, 11, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
     <img src="${pageContext.request.contextPath}/images/banners/stripbeautyBanner.jpg"
            alt="Beauty Offers">
</div>
<%
  } else {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <img src="${pageContext.request.contextPath}/images/banners/nuby-strip-banner.jpg" alt="nuby-strip">
 </div>
<%
  }
%>