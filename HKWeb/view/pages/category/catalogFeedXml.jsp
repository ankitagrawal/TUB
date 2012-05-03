<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="mhc.util.ImageManager" %>
<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="catalogBean"/>
<catalog>
  <page current="${catalogBean.pageNo}" total="${catalogBean.pageCount}" perPage="${catalogBean.perPage}"/>
  <c:forEach items="${catalogBean.productList}" var="product">
    <product id="${product.id}">
      <name>${hk:escapeHtml(product.name)}</name>
      <url>http://www.healthkart.com/product/${product.slug}/${product.id}</url>
      <hkPriceMin>${product.minimumMRPProducVariant.hkPrice}</hkPriceMin>
      <hkPriceMax>${product.maximumMRPProducVariant.hkPrice}</hkPriceMax>
    </product>

    <product id="${product.id}">
      <name>${hk:escapeHtml(product.name)}</name>
      <priceMin marked="${product.minimumMRPProducVariant.markedPrice}" hk="${product.minimumMRPProducVariant.hkPrice}"/>
      <priceMax marked="${product.maximumMRPProducVariant.markedPrice}" hk="${product.maximumMRPProducVariant.hkPrice}"/>
      <images>
        <c:choose>
          <c:when test="${product.mainImageId != null}">
            <c:set var="imageId" value="${product.mainImageId}"/>
            <%Long imageId = (Long) pageContext.getAttribute("imageId");%>
            <image
                srcSmall="<%=ImageManager.getS3ImageUrl(EnumImageSize.SmallSize, imageId)%>"
                srcMedium="<%=ImageManager.getS3ImageUrl(EnumImageSize.MediumSize, imageId)%>"
                srcLarge="<%=ImageManager.getS3ImageUrl(EnumImageSize.LargeSize, imageId)%>"
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
                <option name="${hk:escapeHtml(productOption.name)}">${hk:escapeHtml(productOption.value)}</option>
              </c:forEach>
            </options>
          </variant>
        </c:forEach>
      </variants>
    </product>
  </c:forEach>
</catalog>
