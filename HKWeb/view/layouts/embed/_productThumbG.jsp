<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    Product product_productThumb = (Product) pageContext.getAttribute("product");
    if (product_productThumb == null) {
      ProductDao productDao = ServiceLocatorFactory.getService(ProductDao.class);
      String product_productThumbId = (String) pageContext.getAttribute("productId");
      product_productThumb = productDao.getProductById(product_productThumbId);
    }

    pageContext.setAttribute("product", product_productThumb);

    ComboDao comboDao = ServiceLocatorFactory.getService(ComboDao.class);
    Combo combo = comboDao.get(Combo.class, product_productThumb.getId());
    pageContext.setAttribute("combo", combo);
  %>

  <div class='grid_4 product'>
    <c:choose>
      <c:when test="${product.googleAdDisallowed || product.deleted || product.outOfStock}">
      </c:when>
      <c:otherwise>
        <h3>
          <s:link href="${product.productURL}" title="${product.name}" class="prod_link">
            <s:param name="productId" value="${product.id}"/>
            <s:param name="productSlug" value="${product.slug}"/>
            ${product.name}
          </s:link>
        </h3>
        <s:link href="${product.productURL}" class="prod_link" title="${product.name}">
          <s:param name="productId" value="${product.id}"/>
          <s:param name="productSlug" value="${product.slug}"/>
          <div class='img128'>
              <%--<img src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg" alt="${product.name}"/>--%>
            <c:choose>
              <c:when test="${product.mainImageId != null}">
                <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"
                                 alt="${product.name}"/>
              </c:when>
              <c:otherwise>
                <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg'
                     alt="${product.name}"/>
              </c:otherwise>
            </c:choose>
          </div>
          <c:choose>
            <c:when test="${combo != null}">
              <div class='prices'>
                <div class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                  </span>
                </div>
                <div class='hk'>
                  Our Price
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${combo.hkPrice}"
                      maxFractionDigits="0"/>
                  </span>
                </div>
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
                <c:if test="${product.minimumMRPProducVariant.discountPercent > 0}">
                  <div class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
                  </span>
                  </div>
                  <div class='hk'>
                    Our Price
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                      maxFractionDigits="0"/>
                  </span>
                  </div>
                </c:if>
                <c:if test="${product.minimumMRPProducVariant.discountPercent == 0}">
                  <div class='cut' style="min-width: 1px; height: 12px;">
                  </div>
                  <div class="hk">
                    Our Price

                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                      maxFractionDigits="0"/>
                  </span>
                  </div>
                </c:if>
              </div>
              <c:if test="${product.maximumDiscountProducVariant.discountPercent > 0}">
                <c:choose>
                  <c:when
                      test="${product.maximumDiscountProducVariant.discountPercent > product.minimumMRPProducVariant.discountPercent}">
                    <div class="special green"
                         style="${product.maximumDiscountProducVariant.discountPercent >= .33 ? 'font-style:bold;' : ''}">
                      <c:choose>
                        <c:when test="${product.minimumMRPProducVariant.discountPercent > 0}">
                          <fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}"
                                            maxFractionDigits="0"/>% to
                        </c:when>
                        <c:otherwise>
                          upto
                        </c:otherwise>
                      </c:choose>
                      <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                        maxFractionDigits="0"/>%
                                                               off
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="special green">
                      <c:if test="${product.maximumDiscountProducVariant.discountPercent >= .33}">
                        <strong>
                          <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                            maxFractionDigits="0"/>%
                                                                   off
                        </strong>
                      </c:if>
                      <c:if test="${product.maximumDiscountProducVariant.discountPercent < .33}">
                        <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                          maxFractionDigits="0"/>% off
                      </c:if>
                    </div>
                  </c:otherwise>
                </c:choose>
              </c:if>
            </c:otherwise>
          </c:choose>
        </s:link>
      </c:otherwise>
    </c:choose>
    <div class="floatfix"></div>
  </div>
</s:layout-definition>