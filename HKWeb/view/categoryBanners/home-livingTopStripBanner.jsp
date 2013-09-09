<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount">--%>
     <img
            src="${pageContext.request.contextPath}/images/banners/home-banner.jpg"
            alt="Flat 25% Off" usemap="#Map">
      <map name="Map" id="Map">
          <s:link beanclass="com.hk.web.action.core.user.RequestCallbackAction" id="sendCouponLink">
              <area shape="rect" coords="62,-7,253,92" href="javascript:void(0);"/>
          </s:link>
        <area shape="rect" coords="262,-7,453,94" href="${pageContext.request.contextPath}/home-living/cook-fry/induction-cooker" />
        <area shape="rect" coords="468,-1,655,98" href="${pageContext.request.contextPath}/home-living/mix-grind-chop/juicer-mixer-grinder" />
        <area shape="rect" coords="667,1,874,80" href="${pageContext.request.contextPath}/home-living/mix-grind-chop/food-processor" />

      </map>

        <script type="text/javascript">
            var params = {};
            params['srcUrl'] = document.location.href;
            params['topLevelCategory'] = $('#topCategoryContainer').html();
            $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href') + '?' + $.param(params));

            $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

          </script>
</div>





