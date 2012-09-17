<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 4, 30, 23, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #333333; border: 1px solid #333333; color: #ffffff; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa;">
  <h5>Offer on Soulflower Products: <span style="font-weight:bolder;"> Rs 100 off </span> on purchase above Rs 500/-, Use coupon HKSF100</h5>
</div>
<%
  }
%>
