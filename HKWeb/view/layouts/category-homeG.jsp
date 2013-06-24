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
    <s:layout-component name="htmlHead"/>
    
    <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
    <link href="<hk:vhostCss/>/css/new.dev.css" rel="stylesheet" type="text/css"/>
    <!--[if IE 8]>
        <link href="<hk:vhostCss/>/css/ie8.css" rel="stylesheet" type="text/css" />
    <![endif]-->
    <!--[if IE 7]>
        <link href="<hk:vhostCss/>/css/ie7.css" rel="stylesheet" type="text/css" />
    <![endif]-->
    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="${topCategory}" allCategories="${topCategory}" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>
    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>

  </head>
  <body id="${bodyId}">
  <div class="jqmWindow" id="discountCouponModal"></div>
  <s:layout-component name="modal"/>

  <div id="container" class="container_24">
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
      <s:layout-component name="mainBanner"/>
      <script type="text/javascript">
        $('#categoryBanner li').css('height', '220px'); // hack to make this work in chrome. not sure of the exact reasons. will eventually rewrite all of it.
        $('#categoryBanner').bxSlider({
          controls: false,
          speed: 300,
          pause: 5000,
          auto: true,
          pager: true,
          mode: "fade",
          captions: true
        });
      </script>

      <s:layout-component name="content">
      </s:layout-component>
      <div class="floatfix"></div>
    </div>

    <s:layout-render name="/includes/_footer.jsp"/>

    <s:layout-render name="/layouts/embed/remarketing.jsp" labels="${topCategory}"/>

    <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>
    <s:layout-render name="/includes/_zopim.jsp" topCategory="${topCategory}"/>
    
  </div>

  </body>
  </html>
</s:layout-definition>