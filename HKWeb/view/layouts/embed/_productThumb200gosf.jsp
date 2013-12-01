<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
    <%
        Product product_productThumb = (Product) pageContext.getAttribute("product");
        String product_productDesc = (String) pageContext.getAttribute("productDesc");
        if (product_productThumb == null) {
            ProductDao productDao = ServiceLocatorFactory.getService(ProductDao.class);
            String product_productThumbId = (String) pageContext.getAttribute("productId");
            product_productThumb = productDao.getProductById(product_productThumbId);
        }

        pageContext.setAttribute("product", product_productThumb);
        pageContext.setAttribute("productDesc", product_productDesc);

        Combo combo = null;
        if (product_productThumb instanceof Combo) {
            combo = (Combo) product_productThumb;
        }
        pageContext.setAttribute("combo", combo);
    %>
    
    <c:choose>
        <c:when test="${product.googleAdDisallowed || product.deleted}">
        </c:when>
        <c:otherwise>
            <div class='product'>
                <img class="gosf-logo" src="${pageContext.request.contextPath}/images/GOSF/gosf-price.jpg"/>

                <div class="img">
                    <div class='img180 ${product.outOfStock ? 'opaque' : ''}' style="margin-bottom:20px;">
                        <s:link href="${product.productURL}?gosf=true" class="prod_link" title="${product.name}">
                            <hk:productImage style="max-height:180px;max-width:180px;"
                                             imageId="${product.mainImageId}"
                                             size="<%=EnumImageSize.MediumSize%>"
                                             alt="${product.name}"/>
                        </s:link>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${combo != null}">
                        <p class='price'>
							<span>

                    Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>

								</span>
                            Rs. <fmt:formatNumber
                                value="${combo.hkPrice}"
                                maxFractionDigits="0"/>

                        </p>

                        <h3>
                            <s:link href="${product.productURL}" title="${product.name}">
                                ${product.name}
                            </s:link>

                        </h3>

                        <p class="description"> ${productDesc} </p>

                        <s:link href="${product.productURL}" class="buynow">BUY NOW</s:link>
                        <div class="cl"></div>
                        <img src="${pageContext.request.contextPath}/images/GOSF/product-divider.jpg"/>

                        <p class="bid">
                            <c:if test="${combo.discountPercent >= .33}">

                                <fmt:formatNumber value="${combo.discountPercent*100}"
                                                  maxFractionDigits="0"/>%
                                off

                            </c:if>
                            <c:if test="${combo.discountPercent < .33}">
                                <fmt:formatNumber value="${combo.discountPercent*100}"
                                                  maxFractionDigits="0"/>% off
                            </c:if>
                        </p>

                    </c:when>
                    <c:otherwise>

                            <c:if test="${product.minimumMRPProducVariant.discountPercent > 0}">
							<p class='price'>	<span>

                    Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
                  </span>


                                Rs. <fmt:formatNumber
                                    value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                                    maxFractionDigits="0"/>
                            </p>
                                <h3>
                            <s:link href="${product.productURL}" title="${product.name}">
                                ${product.name}
                            </s:link>

                        </h3>

                               <p class="description"> ${productDesc} </p>

                                                            <s:link href="${product.productURL}" class="buynow">BUY NOW</s:link>
                                                            <div class="cl"></div>
                                                            <img src="${pageContext.request.contextPath}/images/GOSF/product-divider.jpg"/>

                            </c:if>
                        
                            <c:if test="${product.minimumMRPProducVariant.discountPercent == 0}">
                             <p class='price'>
                                Rs. <fmt:formatNumber
                                    value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                                    maxFractionDigits="0"/>
                                  </p>

                                <h3>
                            <s:link href="${product.productURL}" title="${product.name}">
                                ${product.name}
                            </s:link>

                        </h3>
                                    <p class="description"> ${productDesc} </p>

                                                            <s:link href="${product.productURL}" class="buynow">BUY NOW</s:link>
                                                            <div class="cl"></div>
                                                            <img src="${pageContext.request.contextPath}/images/GOSF/product-divider.jpg"/>

                            </c:if>

                            
                        <c:if test="${product.maximumDiscountProducVariant.discountPercent > 0}">
                            <c:choose>
                                <c:when
                                        test="${product.maximumDiscountProducVariant.discountPercent > product.minimumMRPProducVariant.discountPercent}">
                                    

                                    <p class="bid"
                                       style="${product.maximumDiscountProducVariant.discountPercent >= .33 ? '' : ''}">
                                        <c:choose>
                                            <c:when test="${product.minimumMRPProducVariant.discountPercent > 0}">
                                                <fmt:formatNumber
                                                        value="${product.minimumMRPProducVariant.discountPercent*100}"
                                                        maxFractionDigits="0"/>% to
                                            </c:when>
                                            <c:otherwise>
                                                upto
                                            </c:otherwise>
                                        </c:choose>
                                        <fmt:formatNumber
                                                value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                                maxFractionDigits="0"/>%
                                        off
                                    </p>
                                </c:when>
                                <c:otherwise>

                                   


                                    <p class="bid">
                                        <c:if test="${product.maximumDiscountProducVariant.discountPercent >= .33}">

                                            <fmt:formatNumber
                                                    value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                                    maxFractionDigits="0"/>%
                                            off

                                        </c:if>
                                        <c:if test="${product.maximumDiscountProducVariant.discountPercent < .33}">
                                            <fmt:formatNumber
                                                    value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                                    maxFractionDigits="0"/>% off
                                        </c:if>
                                    </p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>

                    </c:otherwise>
                </c:choose>

            </div>

        </c:otherwise>
    </c:choose>
</s:layout-definition>