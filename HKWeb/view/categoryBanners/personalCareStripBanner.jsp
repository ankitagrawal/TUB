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
<%
  DateTime dateTime = new DateTime();
  Date endOfOfferDate = new Date(new DateTime(2013, 9, 28, 23, 59, 59, 59).getMillis());
  if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <a href='${pageContext.request.contextPath}/beauty'>
     <img src="${pageContext.request.contextPath}/images/banners/strip_Beauty_banner.jpg"
            alt="Beauty Offers">
        </a>
</div>
<%
  } else {
%>
<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">--%>
    <a href='${pageContext.request.contextPath}/pages/offers/personal-care-page/personal-care.jsp'>
        <img src="${pageContext.request.contextPath}/images/banners/top/personal-care-strip-banner.jpg"
             alt="persona-care"></a>
    </a>
    <%--</s:link>--%>
    <script type="text/javascript">
        var params = {};
        params['srcUrl'] = document.location.href;
        params['topLevelCategory'] = $('#topCategoryContainer').html();
        $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href') + '?' + $.param(params));

        $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

    </script>
</div>
<%
  }
%>