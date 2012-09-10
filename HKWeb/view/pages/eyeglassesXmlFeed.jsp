<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.EyeGlassesFeedAction" var="eyeglassBean"/>
<c:forEach items="${eyeglassBean.productVariants}" var="productVariant">
    <c:set var="product" value="${productVariant.product}"/>
    <c:if test="${!productVariant.deleted && !productVariant.outOfStock && product.mainImageId != null}">
        <c:set var="imageId" value="${product.mainImageId}"/>
        <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
        <c:forEach items="${productVariant.productOptions}" var="productOption">
            <c:if test="${hk:equalsIgnoreCase(productOption.name,'size')}">
                <c:set var="sizeType" value="${productOption.value}"/>
            </c:if>
        </c:forEach>
        <c:if test="${sizeType != null}">
            <accessories>
                <Glasses id="${productVariant.id}" type="${sizeType}" price="${productVariant.hkPrice}"
                         mrp="${productVariant.markedPrice}" name="${hk:escapeHtml(product.name)}">
                         desc="${product.description}">
                    <imgPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, imageId, false)%>
                    </imgPath>
                    <thumbPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId, false)%>
                    </thumbPath>
                </Glasses>
            </accessories>
        </c:if>
    </c:if>
</c:forEach>