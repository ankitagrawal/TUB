<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.StyleCrazeCatalogAction" var="googleBean"/>
<products>
    <title>Healthkart Google Product Feed</title>
    <link>http://www.healthkart.com</link>
    <description>Healthkart Online Health Store</description>
    <c:forEach items="${googleBean.products}" var="product">
        <c:if test="${fn:length(product.productVariants) > 0
                && product.mainImageId != null && !product.outOfStock && product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">
            <item>
                <title><![CDATA[${hk:escapeXML(product.name)}]]></title>
                <link>http://www.healthkart.com/product/${product.slug}/${product.id}</link>
                <description>![CDATA[${hk:stripHtml(product.description)}]]</description>
                <id>${product.id}</id>
                <markedPrice>${product.inStockMaximumDiscountProductVariant.markedPrice} INR</markedPrice>
                <hkPrice>${product.inStockMaximumDiscountProductVariant.hkPrice} INR</hkPrice>
                <image_link>
                    <c:set var="imageId" value="${product.mainImageId}"/>
                    <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
                    <%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId)%>
                </image_link>
            </item>
        </c:if>
    </c:forEach>
</products>
