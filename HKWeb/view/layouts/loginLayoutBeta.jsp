<%@ page import="com.hk.constants.marketing.TagConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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


        <meta name="Keywords" content="<s:layout-component name="metaKeywords"/>"/>
        <meta name="Description" content="<s:layout-component name="metaDescription"/>"/>


        <jsp:include page="/includes/_styleBeta.jsp"/>
        <s:layout-component name="analytics">
            <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="login" allCategories="login" brand=""
                             isProd="<%=false%>"/>
        </s:layout-component>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
        <script type="text/javascript" src="<hk:vhostJs/>/otherScripts/jquery.glow.js"></script>
        <script type="text/javascript" src="<hk:vhostJs/>/scripts/field-validation.js"></script>
        <s:layout-component name="htmlHead"/>
    </head>
    <body id="${bodyId}">
    <s:layout-render name="/layouts/embed/tagManager.jsp"
                   pageType="<%=TagConstants.PageType.LOGIN%>"
      />
    <s:layout-component name="modal"/>
    <div id="container">
            <%--<s:layout-component name="header">
            <s:layout-render name="/layouts/embed/_header.jsp" attachRedirectParam="false"/>
            </s:layout-component>--%>
            <%--<s:layout-component name="menu">
            <s:layout-render name="/includes/_menuBeta.jsp" topCategory="${topCategory}"/>
            </s:layout-component>--%>
        <div id="logoBoxContainer" style="cursor:default; width: 960px; margin: 50px auto 20px;">
            <div class='logoBox'>
                <s:link href="/" title='go to healthkart home'>
                    <img src='<hk:vhostImage/>/images/HK-logo.png' alt="Healthkart logo"/>
                </s:link>
            </div>
        </div>
        <div class="main_container">
            <s:layout-component name="checkoutStep"/>
            <div class="floatfix"></div>
        </div>
            <%-- <s:layout-render name="/includes/_footer.jsp"/>--%>
    </div>
    <s:layout-component name="endScripts"/>

</s:layout-definition>
</body>
</html>