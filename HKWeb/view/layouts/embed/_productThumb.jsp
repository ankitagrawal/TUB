<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="mhc.domain.Product" %>
<%@ page import="mhc.service.dao.ProductDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    ProductDao productDao = InjectorFactory.getInjector().getInstance(ProductDao.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product_productThumb = productDao.find(product_productThumbId);
    pageContext.setAttribute("product", product_productThumb);
  %>

  <div class='product'>
    <c:choose>
      <c:when test="${product.googleAdDisallowed}">
      </c:when>
      <c:otherwise>
        <h3>
          <s:link beanclass="web.action.ProductAction" title="${product.name}" class="prod_link">
            <s:param name="productId" value="${product.id}"/>
            <s:param name="productSlug" value="${product.slug}"/>
            ${product.name}
          </s:link>
        </h3>
        <s:link beanclass="web.action.ProductAction" class="prod_link" title="${product.name}">
          <s:param name="productId" value="${product.id}"/>
          <s:param name="productSlug" value="${product.slug}"/>
          <div class='img128'>
              <%--<img src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg" alt="${product.name}"/>--%>
            <c:choose>
              <c:when test="${product.mainImageId != null}">
                <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"/>
              </c:when>
              <c:otherwise>
                <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg'
                     alt="${product.name}"/>
              </c:otherwise>
            </c:choose>
          </div>
          <c:choose>
            <c:when test="${hk:getCombo(product.id) != null}">
              <div class='prices'>
                <c:set var="combo" value="${hk:getCombo(product.id)}"/>
                <c:if test="${combo.discountPercent > 0}">
                  <div class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                  </span>
                  </div>
                  <div class='hk'>
                    Our Price
                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${combo.hkPrice}" maxFractionDigits="0"/>
                  </span>
                  </div>
                </c:if>
                <c:if test="${combo.discountPercent == 0}">
                  <div class='cut' style="min-width: 1px; height: 12px;">
                  </div>
                  <div class="hk">
                    Our Price

                  <span class='num'>
                    Rs. <fmt:formatNumber
                      value="${combo.hkPrice}" maxFractionDigits="0"/>
                  </span>
                  </div>
                </c:if>
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
                      value="${product.minimumMRPProducVariant.hkPrice + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
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
                      value="${product.minimumMRPProducVariant.hkPrice + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
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
  </div>
</s:layout-definition>