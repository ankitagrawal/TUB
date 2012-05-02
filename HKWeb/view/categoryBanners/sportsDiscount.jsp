<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 2, 5, 23, 59, 59, 59).getMillis());
%>
  <div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #ffffcc; border: 1px solid #cccc00; color: #ffffff; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa;">
  <a href="http://www.facebook.com/healthkart?sk=app_130363280399851" target="_blank">Claim your <span style="font-weight:bolder;">Introductory 10% Cash Back </span> on sports and fitness accessories via our facebook page</a>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <%
      }
    %>
</div>