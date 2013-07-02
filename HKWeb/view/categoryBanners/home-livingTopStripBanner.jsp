<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 11, 21, 23, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>

<div style="margin-left: auto; margin-right: auto; width:960px;">
  <div style="height: 48px; padding-top: 6px; font-size: 1em; color: black; background-color: #ffcccc;text-align: center;">
    <strong>10% Off on beauty products*</strong> (only on prepaid orders above Rs 1000, TnC apply), Use Coupon Code <strong>HK10BTYNOV</strong><br/>
    <span style="font-size: 1.2em;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining</span>
  </div>
</div>
<%
  }
%>--%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount">--%>
     <img
            src="${pageContext.request.contextPath}/images/banners/home-living-banner.jpg"
            alt="Flat 20% Off on these 3 appliances from BAJAJ" usemap="#Map">
      <map name="Map" id="Map">
        <area shape="rect" coords="262,-7,453,94" href="${pageContext.request.contextPath}/product/bajaj-jx4-juicer-mixer-grinder/MIT002" />
        <area shape="rect" coords="468,-1,655,98" href="${pageContext.request.contextPath}/product/bajaj-jx4-juicer-mixer-grinder/WST022" />
        <area shape="rect" coords="667,1,874,80" href="${pageContext.request.contextPath}/product/bajaj-jx4-juicer-mixer-grinder/PAN016" />
      </map>
</div>








