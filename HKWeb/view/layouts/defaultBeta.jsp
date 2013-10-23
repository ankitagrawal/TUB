<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <link rel="shortcut icon" href="<hk:vhostImage/>/favicon2.ico" />
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
    <jsp:include page="/includes/_styleBeta.jsp"/>
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
      <s:layout-render name="/layouts/embed/_headerBeta.jsp"/>
    </s:layout-component>
      <s:layout-component name="menu">
          <s:layout-render name="/includes/_menuBeta.jsp" topCategory="${topCategory}"/>
      </s:layout-component>
    <div class="main_container">
        <s:layout-component name="steps"/>
            <%-- central content should be used only when you don't use lhscontent and rhscontent--%>
        <div class="centralContent">
            <s:layout-component name="centralContent"/>

            <div class="floatfix"></div>
        </div>

        <s:errors/>
        <div class="ttl-cntnr">
            <span class="icn icn-sqre "></span>
            <h1 class=""><s:layout-component name="heading"/></h1>
            <span class="icn icn-sqre"></span>
        </div>

        <%--Error Messages Containers--%>
        <div class="alert messages"><s:messages key="generalMessages"/></div>


        <div class="lhsContent cont-lft">
        <s:layout-component name="lhsContent"/>

        <div class="floatfix"></div>
      </div>
      <s:layout-component name="left_col"/>

      <div class="rhsContent cont-lft">
        <s:layout-component name="rhsContent"/>

        <div class="floatfix"></div>
      </div>
      <div class="fullContent cont-lft">
            <s:layout-component name="fullContent"/>
      </div>
      <div class="floatfix"></div>
    </div>
    <s:layout-render name="/includes/_footerBeta.jsp"/>

    <s:layout-component name="remarketing"/>

    <s:layout-component name="zopim"/>

  </div>

  </body>

  </html>
</s:layout-definition>