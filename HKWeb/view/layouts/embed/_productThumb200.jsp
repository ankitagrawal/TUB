<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
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
	<c:if test="${!product.googleAdDisallowed && !product.deleted && !product.hidden}">
			<div class='grid_6 product' style="width:240px;height:300px;">
        <c:set var="urlParameter" value="<%=HealthkartConstants.URL.productPosition%>"/>
				<div class='img180 ${product.outOfStock ? 'opaque' : ''}' style="margin-bottom:20px;">
					<s:link href="${hk:getAppendedURL(product.productURL, urlParameter, position)}" class="prod_link" title="${product.name}">
						<img style="max-height:180px;max-width:180px;" src="${hk:getS3ImageUrl(imageMediumSize, product.mainImageId)}" alt="${product.name}"
				     title="${product.name}">
					</s:link>
				</div>
				<div>
					<span style="height:20px;max-width:240px;">
						<s:link href="${hk:getAppendedURL(product.productURL, urlParameter, position)}" title="${product.name}" class="prod_link">
							${product.name}
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
							</span>>
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
								<span class='cut'>
                  <span class='num'>
                    Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
                  </span>
				</span>
				<span class='hk'>
                  <span class='num'>
                    Rs. <fmt:formatNumber
		                  value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
		                  maxFractionDigits="0"/>
                  </span>
								</span>
							</c:if>
							<c:if test="${product.minimumMRPProducVariant.discountPercent == 0}">
								<div class="hk">
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
									</div>
								</c:when>
								<c:otherwise>
									<div class="special green">
										<c:if test="${product.maximumDiscountProducVariant.discountPercent >= .33}">
											<strong>
												<fmt:formatNumber
														value="${product.maximumDiscountProducVariant.discountPercent*100}"
														maxFractionDigits="0"/>%
												                               off
											</strong>
										</c:if>
										<c:if test="${product.maximumDiscountProducVariant.discountPercent < .33}">
											<fmt:formatNumber
													value="${product.maximumDiscountProducVariant.discountPercent*100}"
													maxFractionDigits="0"/>% off
										</c:if>
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:otherwise>
				</c:choose>

				<div class="floatfix"></div>
			</div>
	</c:if>
</s:layout-definition>