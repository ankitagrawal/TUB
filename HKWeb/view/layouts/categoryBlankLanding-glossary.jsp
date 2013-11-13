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



    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="${topCategory}" allCategories="${topCategory}" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.bxSlider.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>
    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>

    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <s:layout-render name="/layouts/embed/tagManager.jsp"
                   pageType="<%=TagConstants.PageType.GLOSSARY%>"
      />
  <div class="jqmWindow" id="discountCouponModal"></div>
  <s:layout-component name="modal"/>

  <div id="container" class="container_24">


    <s:layout-component name="content"/>


    <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>
  </div>

  </body>
  </html>
</s:layout-definition>