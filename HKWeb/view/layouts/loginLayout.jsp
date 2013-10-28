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
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="login" allCategories="login" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/otherScripts/jquery.glow.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>
    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <s:layout-component name="modal"/>
  <div id="container">
    <s:layout-component name="header">
      <s:layout-render name="/layouts/embed/_header.jsp" attachRedirectParam="false"/>
    </s:layout-component>
	<s:layout-component name="menu">
      <s:layout-render name="/includes/_menu.jsp" topCategory="${topCategory}" showNewHKLink = "false"/>
    </s:layout-component>
    <div class="main_container">
      <s:layout-component name="checkoutStep"/>
      <div class="floatfix"></div>
    </div>
    <s:layout-render name="/includes/_footer.jsp"/>
  </div>
  <s:layout-component name="endScripts"/>

</s:layout-definition>
</body>
</html>