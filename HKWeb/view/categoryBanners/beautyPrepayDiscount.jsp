<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2013, 9, 21, 11, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount">--%>
     <img src="${pageContext.request.contextPath}/images/banners/stripbeautyBanner.jpg"
            alt="Beauty Offers">
</div>
<%
  } else {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount">--%>
    <a href="${pageContext.request.contextPath}/pages/offers/beauty/beauty-offers.jsp"> <img
            src="${pageContext.request.contextPath}/images/banners/top/Beauty-strip_banner_offer.jpg"
            alt="Beauty Offers"> </a>
</div>
<%
  }
%>








