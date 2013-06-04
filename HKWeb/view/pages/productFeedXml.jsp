<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="productBean"/>
<product id="${productBean.product.id}">
  <name>${hk:escapeHtml(productBean.product.name)}</name>
  <url>http://www.healthkart.com/product/${productBean.product.slug}/${productBean.product.id}</url>
  <priceMin marked="${productBean.product.minimumMRPProducVariant.markedPrice}" hk="${productBean.product.minimumMRPProducVariant.hkPrice}"/>
  <priceMax marked="${productBean.product.maximumMRPProducVariant.markedPrice}" hk="${productBean.product.maximumMRPProducVariant.hkPrice}"/>
  <categories>${productBean.allCategories}</categories>
  <images>
    <c:choose>
      <c:when test="${productBean.product.mainImageId != null}">
        <c:set var="imageId" value="${productBean.product.mainImageId}"/>
        <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
        <image
            srcSmall="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId )%>"
            srcMedium="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, imageId )%>"
            srcLarge="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.LargeSize, imageId )%>"
            />
      </c:when>
      <c:otherwise>
        <image
            srcSmall="<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${productBean.product.id}.jpg"
            srcMedium="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${productBean.product.id}.jpg"
            srcLarge="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${productBean.product.id}.jpg"
            />
      </c:otherwise>
    </c:choose>
  </images>
  <variants>
    <c:forEach items="${productBean.product.productVariants}" var="variant">
      <variant id="${variant.id}" stock="${!variant.outOfStock}">
        <price marked="${variant.markedPrice}" hk="${variant.hkPrice}"/>
        <options>
          <c:forEach items="${variant.productOptions}" var="productOption">
            <option name="${hk:escapeHtml(productOption.name)}">${hk:escapeHtml(productOption.value)}</option>
          </c:forEach>
        </options>
      </variant>
    </c:forEach>
  </variants>
</product>
