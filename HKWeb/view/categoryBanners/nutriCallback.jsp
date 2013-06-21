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
  <%--<s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">--%>
      <a href='${pageContext.request.contextPath}/pages/offers/sports-nutrition/sports-nutrition.html'>
    <img src="${pageContext.request.contextPath}/images/banners/top/nutrition-strip-banner.jpg" alt="Get Expert Advice on Diet for Fitness, Weight Loss & Bodybuilding">
          </a>
  <%--</s:link>--%>
  <%--<a href="${pageContext.request.contextPath}/faq" >--%>
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/nutrition_faq_banner.jpg" alt="All Your Nutrition Questions - Answered Here.">--%>
  <%--</a>--%>
  <script type="text/javascript">
    var params = {};
    params['srcUrl'] = document.location.href;
    params['topLevelCategory'] = $('#topCategoryContainer').html();
    $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href') + '?' + $.param(params));

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

  </script>
</div>
