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
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <s:layout-component name="htmlHead"/>
    <style type="text/css">
      h3 {
        line-height: 27px;
      }

      ul {
        line-height: 18px;
      }
    </style>
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


    <div class="main_container">
      <h1 style="margin-bottom: 0.5em; margin-top: 0.5em;text-align: center;">
        <s:layout-component name="heading"/>
      </h1>

      <s:errors/>
      <div class="alert messages"><s:messages key="generalMessages"/></div>

        <s:layout-component name="lhsContent"/>
      <s:layout-component name="left_col"/>

      <div class="rhsContent">
        <s:layout-component name="rhsContent"/>

        <div class="floatfix"></div>
      </div>
      <div class="floatfix"></div>
    </div>
    <s:layout-render name="/includes/_footer.jsp"/>

    <s:layout-component name="remarketing"/>

    <s:layout-component name="zopim"/>

  </div>

  </body>

  </html>
</s:layout-definition>