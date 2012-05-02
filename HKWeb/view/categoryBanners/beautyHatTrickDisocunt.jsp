<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2011, 11, 27, 23, 59, 59, 59).getMillis());
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
<div style="margin-left: auto; margin-right: auto; padding: 2px 0; width:960px; text-align: center; border-top: 3px solid #00CCFF; border-bottom: 3px solid #00CCFF;">
    Special <strong>cashback offer</strong> for facebook fans : <a href="http://www.facebook.com/healthkart?sk=app_130363280399851" target="_blank">Beauty Hat-trick - click to claim your free discount voucher</a>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <%--<span style="background-color: yellow;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining.</span>--%>
    <%
      }
    %>
  <script type="text/javascript">
    var params = {};
    params['srcUrl']=document.location.href;
    params['topLevelCategory']=$('#topCategoryContainer').html();
    $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href')+'?'+$.param(params));

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

  </script>
</div>
