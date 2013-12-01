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
      <%--<a href='${pageContext.request.contextPath}/pages/offers/eye-page/eye.jsp'>--%>
    <img src="${pageContext.request.contextPath}/images/banners/top/eye-strip-banner1.jpg" alt="Need eye care advice? Request a callback." usemap="#Map">
      <map name="Map" id="Map">
              <area shape="rect" coords="262,-7,453,94" href="${pageContext.request.contextPath}/eye/sunglasses?filterOptions[0]=11947&filterOptions[1]=18707&filterOptions[2]=12009&filterOptions[3]=16708&filterOptions[4]=12712&filterOptions[5]=17992&filterOptions[6]=17632&filterOptions[7]=16703&filterOptions[8]=12480&filterOptions[9]=11183&filterOptions[10]=14419&filterOptions[11]=11942&minPrice=60&maxPrice=5000" />
              <area shape="rect" coords="468,-1,655,98" href="${pageContext.request.contextPath}/eye/sunglasses?filterOptions%5b0%5d=11938&filterOptions%5b1%5d=11907&filterOptions%5b2%5d=19153&minPrice=60&maxPrice=5000" />
              <area shape="rect" coords="667,1,874,80" href="${pageContext.request.contextPath}/eye/sunglasses?filterOptions%5b0%5d=11907&filterOptions%5b1%5d=11941&filterOptions%5b2%5d=11183&minPrice=60&maxPrice=5000" />

            </map>



  <%--</s:link>--%>
  <script type="text/javascript">
    var params = {};
    params['srcUrl']=document.location.href;
    params['topLevelCategory']=$('#topCategoryContainer').html();
    $('#sendCouponLink').attr('href', $('#sendCouponLink').attr('href')+'?'+$.param(params));

    $('#discountCouponModal').jqm({trigger: '#sendCouponLink', ajax: '@href'});

  </script>
</div>
