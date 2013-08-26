<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.pact.service.catalog.ProductService" %>
<%@ page import="com.hk.cache.vo.ProductVO" %>
<%@ page import="com.hk.constants.marketing.EnumProductReferrer" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="imageMediumSize" value="<%=EnumImageSize.BigThumbSize%>"/>
<s:layout-definition>
  <%
    ProductVO productVO = null;
    ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    if(product_productThumbId != null){
       productVO = productService.getProductVO(product_productThumbId);
    }else{
       Product product_productThumb = (Product) pageContext.getAttribute("product");
      if(product_productThumb !=  null){
         productVO = productService.createProductVO(product_productThumb);
      }
    }
    pageContext.setAttribute("productVO", productVO);

    if (productVO != null && productVO.isCombo()) {
      ComboDao comboDao = ServiceLocatorFactory.getService(ComboDao.class);
      Combo combo = comboDao.get(Combo.class, productVO.getId());
      pageContext.setAttribute("combo", combo);
    }

  %>
  <style type="text/css">
    .opaque {
      opacity: 0.4;
      filter: alpha(opacity = 40);

    }

    .opaque:hover {
      opacity: 1.0;
      filter: alpha(opacity = 100);
    }
  </style>
  <c:if test="${!productVO.googleAdDisallowed && !productVO.deleted && !productVO.hidden}">

    <div class='grid_6 product' style="width:240px;height:300px;">
      <c:set var="param_ref" value="<%=HealthkartConstants.URL.productReferrerId%>"/>
      <c:set var="productURL" value="${hk:getAppendedURL(productVO.productURL, param_ref, productReferrerId)}"/>
      <c:set var="param_pos" value="<%=HealthkartConstants.URL.productPosition%>"/>
      <c:set var="productURL" value="${hk:getAppendedURL(productURL, param_pos, position)}"/>

      <div class='img180 ${productVO.outOfStock ? 'opaque' : ''}' style="margin-bottom:20px;">
        <a href="${productURL}" class="prod_link"
                title="${productVO.name}">
          <img style="max-height:180px;max-width:180px;"
               src="${hk:getS3ImageUrl(imageMediumSize, productVO.mainImageId)}" alt="${productVO.name}"
               title="${productVO.name}">
        </a>
        <shiro:hasAnyRoles name="<%=RoleConstants.CATEGORY_MANAGER%>">
          Missing Image - <a href="${pageContext.request.contextPath}/rest/api/product/resizeImage/${productVO.id}/m/bt" target="_blank">Click here</a>
        </shiro:hasAnyRoles>
      </div>
      <div>
					<span style="height:20px;max-width:240px;">
						<a href="${productURL}" title="${productVO.name}"
                    class="prod_link">
              ${productVO.name}
            </a>
					</span>
      </div>
      <c:choose>
        <c:when test="${combo != null}">
          <div class='prices'>
							<span class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                  </span>
								</span><span class="hk">
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${combo.hkPrice}"
                      maxFractionDigits="0"/>
                  </span>
							</span>
          </div>
          <div class="special green">
            <c:if test="${combo.discountPercent >= .33}">
              <strong>
                <fmt:formatNumber value="${combo.discountPercent*100}"
                                  maxFractionDigits="0"/>%
                off
              </strong>
            </c:if>
            <c:if test="${combo.discountPercent < .33}">
              <fmt:formatNumber value="${combo.discountPercent*100}"
                                maxFractionDigits="0"/>% off
            </c:if>
          </div>
        </c:when>
        <c:otherwise>
          <div class='prices'>
            <c:if test="${productVO.maxDiscount > 0}">
								<span class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${productVO.maxDiscountMRP}" maxFractionDigits="0"/>
                  </span>
				</span>
				<span class='hk'>
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${productVO.maxDiscountHKPrice}"
                      maxFractionDigits="0"/>
                  </span>
								</span>
            </c:if>
            <c:if test="${productVO.maxDiscount == 0}">
              <div class="hk">
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${productVO.maxDiscountHKPrice}"
                      maxFractionDigits="0"/>
                  </span>
              </div>
            </c:if>
          </div>
          <c:if test="${productVO.maxDiscount > 0}">
            <div class="special green">
              <fmt:formatNumber
                  value="${productVO.maxDiscount*100}"
                  maxFractionDigits="0"/>%
              off
            </div>
          </c:if>
        </c:otherwise>
      </c:choose>
        <c:if test="${fn:length(productVO.freebieDesc) > 3}">
            <div class="freebie-cntnr with-variants">
                <h6 >${productVO.freebieDesc} FREE</h6>
            </div>
        </c:if>
      <div class="floatfix"></div>
    </div>
  </c:if>
</s:layout-definition>