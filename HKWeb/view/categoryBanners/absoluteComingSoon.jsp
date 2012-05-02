<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2011, 12, 15, 23, 59, 59, 59).getMillis());
%>
<%
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
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
<a href="http://www.facebook.com/healthkart?sk=app_130363280399851" target="_blank" style="color:white">
    <div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #333333; border: 1px solid #333333; color: #ffffff; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa;">
  <h5>Offer for Lakme Absolute: <span style="font-weight:bolder;"> FREE SHIPPING </span> all over India and cashback for our Facebook fans</h5>
      <div style="text-align: center; padding-bottom: 3px;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining</div>
</div>
</a>
<%
  }
%>
