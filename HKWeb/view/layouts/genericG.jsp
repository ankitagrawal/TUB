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
    <s:layout-component name="htmlHead"/>
    <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
    <link href="<hk:vhostCss/>/css/new.dev.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <%--<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>    --%>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.responsiveslides.min.js"></script>

    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>
  </head>
  <body id="${bodyId}">
  <s:layout-component name="modal"/>
  <div id="container" class="container_24">

    <s:layout-component name="header">
      <s:layout-render name="/layouts/embed/_header.jsp"/>
    </s:layout-component>

    <s:layout-component name="menu">
      <s:layout-render name="/includes/_menu.jsp" topCategory="${topCategory}"/>
    </s:layout-component>

    <s:layout-component name="topBanner"></s:layout-component>

    <s:layout-component name="homePageTopContent"></s:layout-component>

    <div class="main_container" style="margin-top: 15px;">
      <s:layout-component name="content">
      </s:layout-component>
      <div class="floatfix"></div>
    </div>

    <s:layout-render name="/includes/_footer.jsp"/>

  </div>

  <s:layout-component name="analytics">
    <jsp:include page="/includes/_analytics.jsp"/>
  </s:layout-component>

  <s:layout-component name="zopim">
    <jsp:include page="/includes/_zopim.jsp"/>
  </s:layout-component>

  </body>
  </html>
</s:layout-definition>