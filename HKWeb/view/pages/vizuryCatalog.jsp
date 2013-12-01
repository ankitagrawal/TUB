<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.util.HKImageUtils" %>
<%@ page import="com.hk.util.ImageManager" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.VizuryCatalogAction" var="vizuryBean"/>
<products>
  <c:forEach items="${vizuryBean.products}" var="product">
  <c:if test="${fn:length(product.productVariants) > 0 && product.minimumMRPProducVariant.hkPrice != null && product.maximumMRPProducVariant.hkPrice != null}">
    <product id="${product.id}" stock="${!product.outOfStock}">
      <name><![CDATA[${hk:escapeXML(product.name)}]]></name>
      <url>http://www.healthkart.com/product/${product.slug}/${product.id}</url>
      <priceMin marked="${product.minimumMRPProducVariant.markedPrice}" hk="${product.minimumMRPProducVariant.hkPrice}"/>
      <priceMax marked="${product.maximumMRPProducVariant.markedPrice}" hk="${product.maximumMRPProducVariant.hkPrice}"/>
      <categories>
        <c:set var="node" value="${hk:getMenuNodeForProduct(product)}"/>
        <c:if test="${node != null}"> ${node.slug} <c:if test="${node.parentNode != null}"> , ${node.parentNode.slug} <c:if test="${node.parentNode.parentNode != null}"> , ${node.parentNode.parentNode.slug} <c:if test="${node.parentNode.parentNode.parentNode != null}"> , ${node.parentNode.parentNode.parentNode.slug} </c:if> </c:if> </c:if> </c:if>
      </categories>
      <images>
        <c:choose>
          <c:when test="${product.mainImageId != null}">
            <c:set var="imageId" value="${product.mainImageId}"/>
            <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
            <image
                srcSmall="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.SmallSize, imageId )%>"
                srcMedium="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.MediumSize, imageId )%>"
                srcLarge="<%=HKImageUtils.getS3ImageUrl(EnumImageSize.LargeSize, imageId )%>"
                />
          </c:when>
          <c:otherwise>
            <image
                srcSmall="<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
                srcMedium="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${product.id}.jpg"
                srcLarge="<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${product.id}.jpg"
                />
          </c:otherwise>
        </c:choose>
      </images>
      <variants>
        <c:forEach items="${product.productVariants}" var="variant">
          <variant id="${variant.id}" stock="${!variant.outOfStock}">
            <price marked="${variant.markedPrice}" hk="${variant.hkPrice}"/>
            <options>
              <c:forEach items="${variant.productOptions}" var="productOption">
                <option name="${hk:escapeXML(productOption.name)}"><![CDATA[${hk:escapeXML(productOption.value)}]]></option>
              </c:forEach>
            </options>
          </variant>
        </c:forEach>
      </variants>
    </product>
  </c:if>
  </c:forEach>
</products>

