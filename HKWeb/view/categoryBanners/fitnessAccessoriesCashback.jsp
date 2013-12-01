<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2011, 12, 11, 23, 59, 59, 59).getMillis());
%>
<div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #ffff99; border: 1px solid #333333; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa; color: #333333;">
  <h3>Get <strong>additional 10% cash back</strong> on <a style="font-size: 1em;" href="${pageContext.request.contextPath}/nutrition/fitness-accessories">fitness accessories</a>. Use coupon code <strong>HKFIT10</strong></h3>
  <%--<h5><span style="font-weight:bolder;"> FREE SHIPPING </span> all over India and a surprise free gift for our Facebook fans</h5>--%>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <%
      }
    %>
</div>