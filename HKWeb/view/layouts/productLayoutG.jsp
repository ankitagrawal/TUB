<%@ page import="com.hk.constants.marketing.TagConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
    <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
    <link href="<hk:vhostCss/>/css/new.dev.css" rel="stylesheet" type="text/css"/>
    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="${topCategory}" allCategories="${allCategories}" brand="${brand}" isProd="<%=true%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/otherScripts/jquery.glow.js"></script>

    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <div class="jqmWindow" id="discountCouponModal"></div>
  <s:layout-component name="modal"/>

  <div id="container" class="container_24">
    <s:layout-component name="header">
      <s:layout-render name="/layouts/embed/_header.jsp"/>
    </s:layout-component>
    <s:layout-component name="menu">
      <s:layout-render name="/includes/_menu.jsp" topCategory="${topCategory}"/>
    </s:layout-component>
    <s:layout-component name="breadcrumbs">
      <jsp:include page="/includes/_breadcrumbs.jsp"/>
    </s:layout-component>

    <s:layout-component name="topBanner"></s:layout-component>

    <div class="main_container">

      <s:layout-component name="prod_title"></s:layout-component>

      <s:layout-component name="prod_slideshow">
      </s:layout-component>

      <div class='product_details grid_16'>
        <s:layout-component name="product_detail_links">
        </s:layout-component>
        <s:layout-component name="product_variants">
        </s:layout-component>

      </div>
      <s:layout-component name="product_description">
      </s:layout-component>

      <s:layout-component name="related_products">
      </s:layout-component>

      <div class="floatfix"></div>
      <s:layout-component name="foot_price"/>

    </div>

    <s:layout-render name="/includes/_footer.jsp"/>
  </div>

  <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>

  <s:layout-component name="endScripts"/>
  </body>
  </html>
</s:layout-definition>