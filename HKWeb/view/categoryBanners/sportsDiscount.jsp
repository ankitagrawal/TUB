<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%-- <%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2012, 2, 5, 23, 59, 59, 59).getMillis());
%> --%>

<%--<style type="text/css">
  #sendCouponLink {
    text-decoration: none;
    border-bottom: 0;
  }
  #sendCouponLink :hover {
    text-decoration: none;
    border-bottom: 0;
  }
</style>--%>

<div style="margin-left: auto; margin-right: auto; width:960px;">
    <a href="${pageContext.request.contextPath}/pages/offers/sports-and-fitness-page/index.jsp"> <img
                src="${pageContext.request.contextPath}/images/banners/top/sports-strip-banner.jpg"
                alt="sports Offers"> </a>


    <%--<s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">
    <img src="${pageContext.request.contextPath}/images/banners/top/portable-home-gyms-banner.jpg" alt="Build Your Dream Gym at Home, Starting From Rs. 10,000.00">
  </s:link>  
  <script type="text/javascript">
    var params = {};
    params['srcUrl'] = document.location.href;
    params['topLevelCategory'] = $('#topCategoryContainer').html();

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink'});
    $("#discountCouponModal").append($('<iframe id="raj_frame" class="sports_frameNew" src="https://docs.google.com/spreadsheet/embeddedform?formkey=dFFBeGVKUzR6dl9NTkJsVUNlQWRYZnc6MQ" width="760" height="760" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>'));
    $("#discountCouponModal").addClass("raj_modal2");

  </script>--%>
</div>

<%--   <div style="font-family: Lucida Grande, Lucida Sans Unicode, Lucida Sans, Geneva, Verdana, sans-serif; text-align:center; background-color: #ffffcc; border: 1px solid #cccc00; color: #ffffff; width: 950px; margin-left: auto; margin-right: auto; margin-bottom: 5px; border-radius: 5px; padding: 5px; line-height: 1.5em; box-shadow: inset 0 0 3px #aaa;">
  <a href="http://www.facebook.com/healthkart?sk=app_130363280399851" target="_blank">Claim your <span style="font-weight:bolder;">Introductory 10% Cash Back </span> on sports and fitness accessories via our facebook page</a>
    <%
      if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <%
      }
    %>
</div> --%>