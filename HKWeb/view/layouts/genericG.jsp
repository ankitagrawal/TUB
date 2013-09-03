<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%
String hybridReleaseStr = (String) ServiceLocatorFactory.getProperty(Keys.Env.hybridRelease);
boolean hybridRelease = Boolean.parseBoolean(hybridReleaseStr);
pageContext.setAttribute("hybridRelease", hybridRelease);
%>
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
    <jsp:include page="/includes/_static_css.jsp"/>
    <jsp:include page="/includes/_static_js.jsp"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.responsiveslides.min.js"></script>
    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>

    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="${topCategory}" allCategories="${topCategory}" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    
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

    <s:layout-render name="/includes/_footerBeta.jsp"/>

  </div>

  </body>
  </html>
</s:layout-definition>