<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2013, 10, 20, 23, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
     <img src="${pageContext.request.contextPath}/images/banners/Lakme1_beauty.jpg"
            alt="Beauty Offers"  usemap="#Map">
          <map name="Map" id="Map">
            <area shape="rect" coords="0,-7,310,94" href="${pageContext.request.contextPath}/brand/beauty/Lakme" />
            <area shape="rect" coords="325,-1,645,98" href="${pageContext.request.contextPath}/product/lakme-perfect-radiance-4-week-intense-whitening-treatment-capsules/LAKNTF11?productReferrerId=1&productPosition=3/16" />
            <area shape="rect" coords="650,1,995,80" href="${pageContext.request.contextPath}/product/keep-your-eyes-on-the-stars/CMBEYCNC?productReferrerId=13&productPosition=1/9" />

          </map>
</div>
<%
  } else {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <a href="${pageContext.request.contextPath}/pages/offers/beauty/beauty-offers.jsp"> <img
            src="${pageContext.request.contextPath}/images/banners/top/Beauty-strip_banner_offer.jpg"
            alt="Beauty Offers"> </a>
</div>
<%
  }
%>








