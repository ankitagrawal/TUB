<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2011, 8, 31, 23, 59, 59, 59).getMillis());
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
<div style="margin-left: auto; margin-right: auto; width:960px;">
  <s:link beanclass="com.hk.web.action.core.discount.SendDiscountCouponAction" id="sendCouponLink">
    <img src="${pageContext.request.contextPath}/images/banners/top/beauty_strip_banner.jpg" alt="Introductory Beauty Discount - 10% off">
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <div style="text-align: center; padding-bottom: 3px;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining</div>
    <%
      }
    %>
  </s:link>
  <script type="text/javascript">
    var params = {};
    params['srcUrl']=document.location.href;
    params['topLevelCategory']=$('#topCategoryContainer').html();
    $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href')+'?'+$.param(params));

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});
    
  </script>
</div>
