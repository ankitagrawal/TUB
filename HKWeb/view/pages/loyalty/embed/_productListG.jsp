<%@ page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.hk.domain.catalog.product.Product"%>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao"%>
<%@ page import="com.hk.domain.loyaltypg.LoyaltyProduct" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page import="com.hk.loyaltypg.dao.LoyaltyProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:layout-definition>

	<div>
		<div class="grid_4">
			<s:link class="img128" href="${lpv.productUrl}"
				title="${lpv.variant.product.name}">
				<c:choose>
					<c:when test="${lpv.variant.product.mainImageId != null}">
						<hk:productImage imageId="${lpv.variant.product.mainImageId}"
							size="<%=EnumImageSize.SmallSize%>" alt="${lpv.variant.product.name}"
							class='prod128' />
					</c:when>
					<c:otherwise>
						<img
							src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lpv.variant.product.id}.jpg'
							alt="${product.name}" class='prod128' />
					</c:otherwise>
				</c:choose>
			</s:link>
		</div>
		<div class="grid_13">
			<s:link href="${lpv.productUrl}" title="${lpv.variant.product.name}"
				class="prod_top_link">
				<h3>${lpv.variant.product.name}</h3>
			</s:link>
			<div>
				<div class='prices'>
					<div class='hk'>
						 <span class='num'> ${lpv.points} Points </span>
					</div>
				</div>
				<div class='prod_desc'>
					${lpv.variant.product.overview}
					<div class='more'>
						<s:link href="${lpv.productUrl}" title="${lpv.variant.product.name}">
                read more and place order &rarr;
              </s:link>
					</div>
					<div class="floatfix"></div>
				</div>
				<div class="floatfix"></div>
			</div>
			<div class="floatfix"></div>
		</div>
</s:layout-definition>