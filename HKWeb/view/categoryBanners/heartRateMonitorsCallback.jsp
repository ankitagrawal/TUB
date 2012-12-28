<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
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
  <s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">
    <img src="${pageContext.request.contextPath}/images/banners/top/Strip-Banners-Heart-Rate-Monitor.jpg" alt="Choose the right heart rate monitor that does exactly what you want it to, request a callback from our experts">
  </s:link>
  <script type="text/javascript">
    var params = {};
    params['srcUrl']=document.location.href;
    params['topLevelCategory']=$('#topCategoryContainer').html();
    $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href')+'?'+$.param(params));

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

  </script>
</div>
