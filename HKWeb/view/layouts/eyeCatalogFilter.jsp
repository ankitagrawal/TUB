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
		String category = ca.getChildCategorySlug();
		if(ca.getSecondaryChildCategorySlug() != null){
			category = ca.getSecondaryChildCategorySlug();
		}
		if(ca.getTertiaryChildCategorySlug() != null){
			category = ca.getTertiaryChildCategorySlug();
		}
		Map<String, Set<ProductOptionDto>> filterMap = catalogFilter.getFilterOptions(category);
		pageContext.setAttribute("filterMap", filterMap);

		PriceRangeDto priceRange = catalogFilter.getPriceRange(category);
		pageContext.setAttribute("priceRange", priceRange);

		PriceRangeDto filteredPriceRange = null;
		if (ca.getMinPrice() != null && ca.getMaxPrice() != null) {
			filteredPriceRange = new PriceRangeDto(ca.getMinPrice(), ca.getMaxPrice());
		} else {
			filteredPriceRange = priceRange;
		}
		pageContext.setAttribute("filteredPriceRange", filteredPriceRange);

	%>

	<style type="text/css">
		#demo-frame > div.demo {
			padding: 10px !important;
		}

		.ui-slider .ui-slider-range {
			background-color: #1D456B;
		}
	</style>
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
	<link href="<hk:vhostCss/>/css/jquery-ui.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var min = ${filteredPriceRange.minPrice};
			var max = ${filteredPriceRange.maxPrice};
			$("#amount").html("Rs." + min + " - Rs." + max);

			$("#slider-range").slider({
				range: true,
				min: ${priceRange.minPrice},
				max: ${priceRange.maxPrice},
				step:50,
				values: [ min, max ],
				change: function(event, ui) {
					$("#amount").html("Rs." + ui.values[ 0 ] + " - Rs." + ui.values[ 1 ]);
					/*var href = $('#pricingFilterLink').attr('href');
					 href += "&minPrice=" + ui.values[ 0 ];
					 href += "&maxPrice=" + ui.values[ 1 ];
					 window.location.href = href;*/
					$("#minPrice").val(ui.values[ 0 ]);
					$("#maxPrice").val(ui.values[ 1 ]);
				}
			});

		});
	</script>
	<div class='box'>
		<c:set var="ctr" value="0"/>
		<s:form beanclass="com.hk.web.action.core.catalog.category.CatalogAction">
			<s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
			<s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
			<c:if test="${ca.secondaryChildCategorySlug != null}">
				<s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
			</c:if>
			<c:if test="${ca.tertiaryChildCategorySlug != null}">
				<s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
			</c:if>
			<s:hidden name="minPrice" id="minPrice" value="${filteredPriceRange.minPrice}"/>
			<s:hidden name="maxPrice" id="maxPrice" value="${filteredPriceRange.maxPrice}"/>
			<c:forEach items="${filterMap}" var="filter">
				<h5 class='heading1'>
					Filter by ${filter.key}
				</h5>
				<ul style="padding-left:0px;">
					<c:forEach items="${filter.value}" var="option">
						<li><s:checkbox name="filterOptions[${ctr}]" checked="${option.id}" value="${option.id}"/>
								${option.value} (${option.qty})
						</li>
						<c:set var="ctr2" value="${ctr}"/>
						<c:set var="ctr" value="${ctr2+1}"/>
					</c:forEach>
				</ul>
			</c:forEach>

			<h5 class='heading1'>
				Filter by Price
			</h5>

			<div class="demo">
				<div style="padding-bottom:10px;">
					<label id="amount" style="font-weight:bold;"></label>
				</div>
				<div id="slider-range" style="background-color:#f6931f"></div>
			</div>
			<div align="right">
				<s:submit name="pre" value="Filter"/>
			</div>
		</s:form>
	</div>


</s:layout-definition>
