<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.service.catalog.ProductService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
	<%
		Product product = (Product) pageContext.getAttribute("product");
		ProductService productService = (ProductService) ServiceLocatorFactory.getService(ProductService.class);
		pageContext.setAttribute("similarProducts", productService.getSimilarProducts(product));

	%>
	<div class="prod_table">
		<div style="font-size: .9em; margin-bottom:15px; font-weight:bold;" align="center">
			We also have the following similar products
		</div>
		<c:forEach items="${similarProducts}" var="sProduct">
			<s:link href="${sProduct.productURL}" class="prod_link" title="${sProduct.name}">
				<div class='img128' style="float:left;width:110px;height:110px;">
					<c:choose>
						<c:when test="${sProduct.mainImageId != null}">
							<hk:productImage style="max-width:100px;max-height:100px;" imageId="${sProduct.mainImageId}"
							                 size="<%=EnumImageSize.SmallSize%>"/>
						</c:when>
						<c:otherwise>
							<img style="max-width:100px;max-height:100px;"
							     src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${sProduct.id}.jpg'
							     alt="${sProduct.name}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</s:link>
		</c:forEach>
	</div>
</s:layout-definition>