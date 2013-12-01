<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 6, 30, 23, 59, 59, 59).getMillis());
%>
<%--<div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #ffff99; border: 1px solid #333333; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa; color: #333333;">--%>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
        <img src="${pageContext.request.contextPath}/images/banners/top/eye_promotional_strip_banner.jpg" alt="Buy a Frame and get Standard Anti Glare Lenses Free">
      <%--<h4><span style="font-weight:bolder;">Rs. 500 Off </span> on all <a href="${pageContext.request.contextPath}/eye/eyeglasses">prescription glasses</a> and standard anti-glare lenses Free! Use coupon code <strong>GLASS500</strong></h4>--%>
    <%
      }
    %>
<%--</div>--%>