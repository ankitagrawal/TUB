<%@ page import="com.hk.helper.CatalogFilter" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dto.ProductOptionDto" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.dto.PriceRangeDto" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-definition>

	<%
		CatalogFilter catalogFilter = (CatalogFilter) ServiceLocatorFactory.getService("CatalogFilter");
		Map<String, Set<ProductOptionDto>> filterMap = catalogFilter.getFilterOptions(ca.getChildCategorySlug());
		pageContext.setAttribute("filterMap", filterMap);

		PriceRangeDto priceRange = catalogFilter.getPriceRange(ca.getChildCategorySlug());
		pageContext.setAttribute("priceRange", priceRange);

		PriceRangeDto filteredPriceRange = null;
		if (ca.getMinPrice() != null && ca.getMaxPrice() != null) {
			filteredPriceRange = new PriceRangeDto(ca.getMinPrice(), ca.getMaxPrice());
		}else{
			filteredPriceRange = priceRange;
		}
		pageContext.setAttribute("filteredPriceRange", filteredPriceRange);

	%>

	<style type="text/css">
		#demo-frame > div.demo {
			padding: 10px !important;
		}

		.ui-slider .ui-slider-range {
			background-color: blue;
		}
	</style>
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
	<link href="<hk:vhostCss/>/css/jquery-ui.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var min = ${filteredPriceRange.minPrice};
			var max = ${filteredPriceRange.maxPrice};
			$("#amount").html("Rs" + min + " - Rs." + max);

			$("#slider-range").slider({
				range: true,
				min: ${priceRange.minPrice},
				max: ${priceRange.maxPrice},
				step:50,
				values: [ min, max ],
				change: function(event, ui) {
					$("#amount").html("Rs." + ui.values[ 0 ] + " - Rs." + ui.values[ 1 ]);
					var href = $('#pricingFilterLink').attr('href');
					href += "&minPrice=" + ui.values[ 0 ];
					href += "&maxPrice=" + ui.values[ 1 ];
					window.location.href = href;
				}
			});

		});
	</script>
	<div class='box'>
		<c:set var="ctr" value="0"/>
		<c:forEach items="${filterMap}" var="filter">
			<h5 class='heading1'>
				Filter by ${filter.key}
			</h5>
			<ul style="padding-left:0px;">
				<c:forEach items="${filter.value}" var="option">
					<li><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction">
						<s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
						<s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
						<c:if test="${ca.secondaryChildCategorySlug != null}">
							<s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
						</c:if>
						<c:if test="${ca.tertiaryChildCategorySlug != null}">
							<s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
						</c:if>
						<s:param name="filterOptions[${ctr+1}]" value="${option.id}"/>
						<s:param name="minPrice" value="${filteredPriceRange.minPrice}"/>
						<s:param name="maxPrice" value="${filteredPriceRange.maxPrice}"/>
						${option.value} (${option.qty})</s:link></li>
				</c:forEach>
			</ul>
		</c:forEach>

		<h5 class='heading1'>
			Filter by Price
		</h5>

		<div style="display: none;"><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction"
		                                    id="pricingFilterLink">
			<s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
			<s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
			<s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
			<s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
		</s:link>
		</div>

		<div class="demo">
			<div style="padding-bottom:10px;">
				<label for="amount">Range:</label>
				<label id="amount" style="border:0; color:#f6931f; font-weight:bold;"></label>
			</div>
			<div id="slider-range" style="background-color:red"></div>
		</div>
		<!-- End demo -->

	</div>


</s:layout-definition>
