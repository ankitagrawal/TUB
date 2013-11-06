<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html>
    <head>
        <link rel="shortcut icon" href="/favicon.ico" />
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
            <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="cart" allCategories="cart" brand="" isProd="<%=false%>"/>
        </s:layout-component>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
        <s:layout-component name="htmlHead"/>
    </head>
    <body id="${bodyId}">
    <div id="container" class="container_24">
        <s:layout-component name="modal"/>
        <s:layout-component name="header">
            <s:layout-render name="/layouts/embed/_headerBeta.jsp"/>
        </s:layout-component>
        <s:layout-component name="menu">
            <s:layout-render name="/includes/_menuBeta.jsp"/>
        </s:layout-component>
        <div class="main_container">
            <s:layout-component name="breadcrumbs">
                <jsp:include page="/includes/_breadcrumbs.jsp"/>
            </s:layout-component>
            <div class="current_step_content step2 shopping_cart">
                <s:layout-component name="checkout_notice">
                    <jsp:include page="/includes/checkoutNotice.jsp"/>
                </s:layout-component>
                <s:layout-component name="cart_title"/>
                <s:layout-component name="cart_items"/>
            </div>
            <div class="floatfix"></div>
        </div>
        <s:layout-render name="/includes/_footerBeta.jsp"/>

        <%--<s:layout-component name="zopim">
          <jsp:include page="/includes/_zopim.jsp"/>
        </s:layout-component>--%>

    </body>
    </html>
</s:layout-definition>