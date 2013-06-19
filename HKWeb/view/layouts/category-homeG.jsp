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
    <jsp:include page="/includes/_static_css.jsp"/>
    <jsp:include page="/includes/_static_js.jsp"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>
    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>
    
    <s:layout-component name="analytics">
      <jsp:include page="/includes/_analytics.jsp"/>
    </s:layout-component>

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

    <s:layout-render name="/includes/_zopim.jsp" topCategory="${topCategory}"/>
    
  </div>

  </body>
  </html>
</s:layout-definition>