<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageType" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.EyeGlassesFeedAction" var="eyeglassBean"/>
<c:set var="frontFacingEyeImageTypeId" value="<%=EnumImageType.FrontFacingEye.getId()%>"/>
<c:set var="sideFacingEyeImageTypeId" value="<%=EnumImageType.SideFacingEye.getId()%>"/>
<Glasses>
    <Category>
       <c:forEach items="${eyeglassBean.productVariants}" var="productVariant">
        <c:set var="product" value="${productVariant.product}"/>
        <c:if test="${!productVariant.deleted && !productVariant.outOfStock && product.mainImageId != null}">
            <%--COLOR:Black|Size:Small|Material:Plastic|Gender:Kids|Brand:Blue Wine.T|Style:Rectangular--%>
            <%--<Glass type="Large" gender="Male" color="Black" id="1011">--%>
            <c:forEach items="${productVariant.productOptions}" var="productOption">
                <c:if test="${hk:equalsIgnoreCase(productOption.name,'Size')}">
                    <c:set var="type" value="${productOption.value}"/>
                </c:if>
                <c:if test="${hk:equalsIgnoreCase(productOption.name,'Gender')}">
                    <c:set var="gender" value="${productOption.value}"/>
                </c:if>
                <c:if test="${hk:equalsIgnoreCase(productOption.name,'COLOR')}">
                    <c:set var="color" value="${productOption.value}"/>
                </c:if>
            </c:forEach>
            <c:set var="frontFacingEyeImageId"
                   value="${hk:searchProductImages(product,productVariant,frontFacingEyeImageTypeId,false,null)}"/>
            <c:set var="sideFacingEyeImageId"
                   value="${hk:searchProductImages(product,productVariant,sideFacingEyeImageTypeId,false,null)}"/>
            <c:if test="${frontFacingEyeImageId != null && sideFacingEyeImageId != null && type != '' && gender != '' && color != ''}">
                <%Long frontFacingEyeImageId = (Long) pageContext.getAttribute("frontFacingEyeImageId");%>
                <%Long sideFacingEyeImageId = (Long) pageContext.getAttribute("sideFacingEyeImageId");%>
                <Glass type="${type}" gender="${gender}" color="${color}" id="${productVariant.id}">
                    <imgPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, frontFacingEyeImageId)%></imgPath>
                    <thumbPath><%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, sideFacingEyeImageId)%></thumbPath>
                    <price>${productVariant.hkPrice}</price>
                    <name>${hk:escapeHtml(product.name)}</name>
                    <producturl>${hk:getProductURL(product,0)}</producturl>
                    <desc></desc>
                </Glass>
            </c:if>
            <c:set var="color" value=""/>
            <c:set var="type" value=""/>
            <c:set var="gender" value=""/>
        </c:if>
    </c:forEach>
    </Category>
</Glasses>