<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageType" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.EyeGlassesFeedAction" var="eyeglassBean"/>
<accessories>
    <c:forEach items="${eyeglassBean.productVariants}" var="productVariant">
        <c:set var="product" value="${productVariant.product}"/>
        <c:set var="frontFacingEyeImageTypeId" value="<%=EnumImageType.FrontFacingEye.getId()%>"/>/>
        <c:set var="sideFacingEyeImageTypeId" value="<%=EnumImageType.SideFacingEye.getId()%>"/>/>
        <c:if test="${!productVariant.deleted && !productVariant.outOfStock && product.mainImageId != null}">
            <c:forEach items="${productVariant.productOptions}" var="productOption">
                <c:if test="${hk:equalsIgnoreCase(productOption.name,'size')}">
                    <c:set var="sizeType" value="${productOption.value}"/>
                </c:if>
            </c:forEach>
            <c:set var="frontFacingEyeImageId"
                   value="${hk:searchProductImages(frontFacingEyeImageTypeId,product,productVariant,true,false)}"/>
            <c:set var="sideFacingEyeImageId"
                   value="${hk:searchProductImages(sideFacingEyeImageTypeId,product,productVariant,true,false)}"/>
            <%Long frontFacingEyeImageId = (Long) pageContext.getAttribute("frontFacingEyeImageId");%>
            <%Long sideFacingEyeImageId = (Long) pageContext.getAttribute("sideFacingEyeImageId");%>
            <c:if test="${frontFacingEyeImageId != null && sideFacingEyeImageId != null && sizeType != null}">
                <Glasses id="${productVariant.id}" type="${sizeType}" price="${productVariant.hkPrice}"
                         mrp="${productVariant.markedPrice}" name="${hk:escapeHtml(product.name)}">
                    desc="">
                    <imgPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, frontFacingEyeImageId, false)%>
                    </imgPath>
                    <thumbPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, sideFacingEyeImageId, false)%>
                    </thumbPath>
                </Glasses>
            </c:if>
        </c:if>
    </c:forEach>
</accessories>
