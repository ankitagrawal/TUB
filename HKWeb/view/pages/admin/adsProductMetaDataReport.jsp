<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.AdsProductMetaDataAction" var="adBean"/>
<html>
<head><title>Ads Product Meta Data : ${adBean.category.displayName}</title></head>
<body>
Product Id | Name | Category | Catgeory URL | Parent Category | Parent Category URL | Product URL | MRP | Sale Price | Discount | Discount % |Max Discount Stock|Discount(In Stock)|Discount %(In Stock)|Max Discount Stock(In Stock)<br/>
<c:forEach items="${adBean.adsProductMetaDataDtos}" var="adsProductMetaDataDto">
  <c:if test="${adsProductMetaDataDto.product.deleted != null && !adsProductMetaDataDto.product.deleted}">
    ${adsProductMetaDataDto.product.id} |
    ${adsProductMetaDataDto.product.name} |
    ${adsProductMetaDataDto.menuNode.name} |
    http://www.healthkart.com${adsProductMetaDataDto.menuNode.url} |
    ${adsProductMetaDataDto.menuNode.parentNode.name} |
    http://www.healthkart.com${adsProductMetaDataDto.menuNode.parentNode.url} |
    http://www.healthkart.com/product/${adsProductMetaDataDto.product.slug}/${adsProductMetaDataDto.product.id} |
    ${adsProductMetaDataDto.product.maximumDiscountProducVariant.markedPrice} |
    <fmt:formatNumber value="${adsProductMetaDataDto.product.maximumDiscountProducVariant.hkPrice}" maxFractionDigits="0"/>|
    ${adsProductMetaDataDto.product.maximumDiscountProducVariant.markedPrice - adsProductMetaDataDto.product.maximumDiscountProducVariant.hkPrice} |
    <fmt:formatNumber value="${(adsProductMetaDataDto.product.maximumDiscountProducVariant.markedPrice - adsProductMetaDataDto.product.maximumDiscountProducVariant.hkPrice)/adsProductMetaDataDto.product.maximumDiscountProducVariant.markedPrice}" type="percent"/> |
    ${adsProductMetaDataDto.product.maximumDiscountProducVariant.outOfStock ? 'Out of stock' : 'In stock'} |
	  <c:choose>
		  <c:when test="${adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant != null}">
			  ${adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice - adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.hkPrice} |
	  <fmt:formatNumber value=" ${(adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice-adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.hkPrice)/adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice}" type="percent"/> |
	  In Stock
		  </c:when>
		  <c:otherwise>Out of Stock |Out of Stock |Out of Stock</c:otherwise>
	  </c:choose>
	  <%--<c:if test="${adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant != null}">--%>
	  <%--${adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice - adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.hkPrice} |--%>
	  <%--<fmt:formatNumber value=" ${(adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice-adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.hkPrice)/adsProductMetaDataDto.product.inStockMaximumDiscountProductVariant.markedPrice}" type="percent"/> |--%>
	  <%--In Stock--%>
		  <%--</c:if>--%>
	  <%--<c:otherwise>Out of Stock |Out of Stock |Out of Stock</c:otherwise>--%>
    <br/>
  </c:if>
</c:forEach>
</body>
</html>
