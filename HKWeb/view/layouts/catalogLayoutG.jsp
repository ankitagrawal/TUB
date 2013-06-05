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
        <s:layout-component name="htmlHead"/>
        <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
        <link href="<hk:vhostCss/>/css/new.dev.css" rel="stylesheet" type="text/css"/>
        <!--[if IE 8]>
            <link href="<hk:vhostCss/>/css/ie8.css" rel="stylesheet" type="text/css" />
        <![endif]-->
        <!--[if IE 7]>
            <link href="<hk:vhostCss/>/css/ie7.css" rel="stylesheet" type="text/css" />
        <![endif]-->
      <s:layout-component name="analytics">
        <jsp:include page="/includes/_analytics.jsp"/>
      </s:layout-component>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>

        <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>

    </head>
    <body id="${bodyId}">
    <div class="jqmWindow" id="discountCouponModal"></div>
    <s:layout-component name="modal"/>

    <div id="container" class="container_24">
        <s:layout-component name="header">
            <s:layout-render name="/layouts/embed/_header.jsp"/>
        </s:layout-component>
        <s:layout-component name="menu">
            <s:layout-render name="/includes/_menu.jsp" topCategory="${topCategory}" allCategories="${allCategories}"/>
        </s:layout-component>
        <s:layout-component name="breadcrumb"/>

      <s:layout-component name="topBanner"/>
        <div class="main_container">

            <s:layout-component name="catalog">
                <div class="catalog grid_18">
                    <s:layout-component name="catalog_controls">
                    </s:layout-component>
                </div>
            </s:layout-component>

            <div class="floatfix"></div>
        </div>
        <s:layout-render name="/includes/_footer.jsp"/>
    </div>

    <s:layout-render name="/layouts/embed/remarketing.jsp" labels="${allCategories}" brandLabel="${brand}"/>

    <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>
    <s:layout-render name="/includes/_zopim.jsp" topCategory="${topCategory}"/>

    </body>
</s:layout-definition>