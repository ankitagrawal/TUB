<%@ page import="com.hk.constants.marketing.TagConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <title>
      <c:choose>
        <c:when test="${hk:isNotBlank(pageTitle)}">
          ${pageTitle}
        </c:when>
        <c:otherwise>
          <c:if test="${hk:isNotBlank(topHeading)}">
            ${topHeading}
          </c:if>
        </c:otherwise>
      </c:choose>
      | HealthKart.com
    </title>


    <meta name="Keywords" content="<s:layout-component name="metaKeywords"/>"/>
    <meta name="Description" content="<s:layout-component name="metaDescription"/>"/>


    <jsp:include page="/includes/_style.jsp"/>
    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="${topCategory}" allCategories="${topCategory}" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>

    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <div class="jqmWindow" id="discountCouponModal"></div>
  
  <div id="container">
    <s:layout-component name="header">
      <s:layout-render name="/layouts/embed/_header.jsp"/>
    </s:layout-component>
    <s:layout-component name="menu">
      <s:layout-render name="/includes/_menu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>
    </s:layout-component>
    <s:layout-component name="breadcrumbs">
      <%--<s:layout-render name="/layouts/embed/catalogBreadcrumb.jsp" breadcrumbUrlFragment="${.urlFragment}"/>--%>
    </s:layout-component>

    <s:layout-component name="topBanner">
      <s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" categories="${topCategory}" topCategories="${topCategory}"/>
    </s:layout-component>
    
    <div class="main_container">
      <div class='promotional'>
          <s:layout-component name="mainBanner"/>
        <script type="text/javascript">
          $('#categoryBanner li').css('height', '220px'); // hack to make this work in chrome. not sure of the exact reasons. will eventually rewrite all of it.
          $('#categoryBanner').bxSlider({
            controls: false,
            speed: 300,
            pause: 5000,
            auto: true,
            pager: true,
            mode: "fade"
          });
        </script>
        <div class='small_banners'>
          <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" event="pre">
            <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg" alt="refer a friend and earn" class="small_banner"/>
          </s:link>
          <img src="<hk:vhostImage/>/images/banners/free-shipping-400.jpg" alt="free shipping" class="small_banner"/>
        </div>
        <div class="floatfix"></div>
      </div>
      <s:layout-component name="product_rows">
      </s:layout-component>
      <div class="floatfix"></div>
    </div>

    <s:layout-render name="/includes/_footer.jsp"/>


    <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>

  </div>

  </body>
  </html>
</s:layout-definition>