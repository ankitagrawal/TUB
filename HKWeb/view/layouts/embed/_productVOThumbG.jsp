<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.catalog.ProductService" %>
<%@ page import="com.hk.cache.vo.ProductVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="imageSmallSize" value="<%=EnumImageSize.SmallSize%>"/>
<s:layout-definition>
  <%
    System.out.println("---Ajeet VO---");
    Product product_productThumb = (Product) pageContext.getAttribute("product");
    ProductVO productVO = null;
    ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
    if (product_productThumb == null) {
      String product_productThumbId = (String) pageContext.getAttribute("productId");
      productVO = productService.getProductVO(product_productThumbId);
    } else {
      productVO = productService.getProductVO(product_productThumb.getId());
    }

    pageContext.setAttribute("productVO", productVO);

    if (product_productThumb instanceof Combo) {
      ComboDao comboDao = ServiceLocatorFactory.getService(ComboDao.class);
      Combo combo = comboDao.get(Combo.class, product_productThumb.getId());
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
    <div class='grid_4 product'>
      <div class='img128 ${productVO.outOfStock ? 'opaque' : ''}' style="margin-bottom:20px;margin-top:10px;">
        <s:link href="${productVO.productURL}?productReferrerId=${productReferrerId}" class="prod_link"
                title="${productVO.name}">
          <img src="${hk:getS3ImageUrl(imageSmallSize, productVO.mainImageId)}" alt="${productVO.name}"
               title="${productVO.name}">
        </s:link>
      </div>
      <div>
					<span style="height:20px;max-width:190px;">
						<s:link href="${productVO.productURL}?productReferrerId=${productReferrerId}" title="${productVO.name}"
                    class="prod_link">
              ${productVO.name}
            </s:link>
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
              upto
              <fmt:formatNumber
                  value="${productVO.maxDiscount*100}"
                  maxFractionDigits="0"/>%
              off
            </div>
          </c:if>
        </c:otherwise>
      </c:choose>

      <div class="floatfix"></div>
    </div>
  </c:if>
</s:layout-definition>