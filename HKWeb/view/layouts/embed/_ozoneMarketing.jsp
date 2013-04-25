
<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.order.Order" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <%
        if (pageContext.getAttribute("pageType") != null) {
            String pageType = (String) pageContext.getAttribute("pageType");
            pageContext.setAttribute("pageType", pageType);
        }

        if (pageContext.getAttribute("categories") != null) {
            String categories = (String) pageContext.getAttribute("categories");
            pageContext.setAttribute("categories", categories);
        }

        if (pageContext.getAttribute("topLevelCategory") != null) {
            String topLevelCategory = (String) pageContext.getAttribute("topLevelCategory");
            pageContext.setAttribute("topLevelCategory", topLevelCategory);
        }

        if (pageContext.getAttribute("secondaryLevelCategory") != null) {
            String topLevelCategory = (String) pageContext.getAttribute("secondaryLevelCategory");
            pageContext.setAttribute("secondaryLevelCategory", topLevelCategory);
        }

        if (pageContext.getAttribute("order") != null) {
            Order order = (Order) pageContext.getAttribute("order");
            pageContext.setAttribute("order", order);
        }

        if (pageContext.getAttribute("googleProduct") != null) {
            Product product = (Product) pageContext.getAttribute("googleProduct");
            pageContext.setAttribute("googleProduct", product);
        }
    %>
    <input type="hidden" value="${pageType}" id="pageType">
    <input type="hidden" value="${topLevelCategory}" id="topLevelCategory">
    <input type="hidden" value="${secondaryLevelCategory}" id="secondaryLevelCategory">
    <input type="hidden" value="${googleProduct.id}" id="googleProductId">

    <c:set var="excludeCategories" value=""/>
    <c:set var="googleProductsSelected" value=""/>

        <c:if test = "${pageType == 'category'}">
        <c:if test = "${topLevelCategory == 'diabetes' || topLevelCategory == 'health-devices'}">
            <iframe src="http://px.ozonemedia.com/data?px_id=000033&type=2&adv_id=ADV000029&section=2&cat_id=${topLevelCategory}&sub_cat1_id=${secondaryLevelCategory}" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0">
             </iframe>

        </c:if>
    </c:if>

    <c:if test = "${pageType == 'product'}">
        <c:if test = "${topLevelCategory == 'diabetes' || topLevelCategory == 'health-devices'}">
            <iframe src="http://px.ozonemedia.com/data?px_id=000033&type=2&adv_id=ADV000029&section=3&cat_id=${topLevelCategory}&sub_cat1_id=${secondaryLevelCategory}&pid=${googleProduct.id}" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0">
            </iframe>
        </c:if>
    </c:if>

    <c:choose>
    <c:when test = "${pageType == 'cart' || pageType == 'purchase'}">
        <c:forEach items="${order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
            <c:if test = "${cartLineItem.productVariant.product.primaryCategory == 'diabetes' || cartLineItem.productVariant.product.primaryCategory == 'health-devices'}">
                <c:choose>
                    <c:when test="${not empty googleProductsSelected}">
                        <c:set var="googleProductsSelected" value="${googleProductsSelected}/${cartLineItem.productVariant.product.id}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="googleProductsSelected" value="${cartLineItem.productVariant.product.id}"/>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
        <c:forEach items="${order.exclusivelyComboCartLineItems}" var="cartLineItem" varStatus="ctr">

            <c:if test = "${cartLineItem.productVariant.product.primaryCategory == 'diabetes' || cartLineItem.productVariant.product.primaryCategory == 'health-devices'}">
                <c:choose>
                    <c:when test="${not empty googleProductsSelected}">
                            <c:set var="googleProductsSelected" value="${googleProductsSelected}/${cartLineItem.productVariant.product.id}"/>
                    </c:when>
                    <c:otherwise>
                            <c:set var="googleProductsSelected" value="${cartLineItem.productVariant.product.id}"/>
                    </c:otherwise>
                </c:choose>
                <c:set var="canGoogleRemarket" value="true"/>
            </c:if>

        </c:forEach>

        <c:if test = "${pageType == 'cart'}">
            <c:if test="${not empty googleProductsSelected}">
                <iframe src="http://px.ozonemedia.com/data?px_id=000033&type=2&adv_id=ADV000029&section=5&pid=${googleProductsSelected}" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0">
                </iframe>
            </c:if>
        </c:if>
        <c:if test = "${pageType == 'purchase'}">
            <c:if test="${not empty googleProductsSelected}">
                <iframe src="http://px.ozonemedia.com/data?px_id=000033&type=2&adv_id=ADV000029&section=6&orderID=${order.id}&saleValue=${order.payment.amount}&paymentMode=${order.payment.paymentMode.name}&currency=INR&pid=${googleProductsSelected}" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0">
                </iframe>
            </c:if>
        </c:if>

    </c:when>
    </c:choose>
    <%--</c:otherwise>--%>
</s:layout-definition>
