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
         <jsp:include page="/includes/_static_css.jsp"/>
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

    <jsp:include page="/includes/_static_js.jsp"/>
    <s:layout-render name="/includes/_dynaMenu.jsp" topCategory="${topCategory}" allCategories="${topCategory}"/>
    
    <%--<s:layout-component name="zopim">
      <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>--%>
    <s:layout-render name="/includes/_zopim.jsp" topCategory="${topCategory}" brand="${brand}" allCategories="${allCategories}"/>

    </body>
</s:layout-definition>