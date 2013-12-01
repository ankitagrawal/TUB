<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2011, 12, 31, 23, 59, 59, 59).getMillis());
%>
    <div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #333333; border: 1px solid #333333; color: #ffffff; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa;">
  <h4><span style="font-weight:bolder;"> 10% Additional OFF </span> on all Services, Use Coupon code HKSERVICE10 to avail this offer</h4>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <%
      }
    %>
</div>