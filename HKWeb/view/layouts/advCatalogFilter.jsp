<%@ page import="com.hk.helper.CatalogFilter" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dto.ProductOptionDto" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.dto.PriceRangeDto" %>
<%@ page import="java.util.List" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-definition>

	<%
		CatalogFilter catalogFilter = (CatalogFilter) ServiceLocatorFactory.getService("CatalogFilter");
		String category = ca.getChildCategorySlug();
		if (ca.getSecondaryChildCategorySlug() != null) {
			category = ca.getSecondaryChildCategorySlug();
		}
		if (ca.getTertiaryChildCategorySlug() != null) {
			category = ca.getTertiaryChildCategorySlug();
		}
		Map<String, List<ProductOptionDto>> filterMap = catalogFilter.getFilterOptions(category);
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

		.separator {
			border-bottom: 1px dotted #CCC;
			padding-bottom: 10px; /*padding-top: 5px;*/
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
				slide: function(event, ui) {
					$("#amount").html("Rs." + ui.values[ 0 ] + " - Rs." + ui.values[ 1 ]);
				},
				change: function(event, ui) {
					//$("#amount").html("Rs." + ui.values[ 0 ] + " - Rs." + ui.values[ 1 ]);
					$("#minPrice").val(ui.values[ 0 ]);
					$("#maxPrice").val(ui.values[ 1 ]);
					$(".filterCatalogForm").submit();
				}
			});

			$(".filterOption").click(function() {
				var ctr = 0;
				$(".filterOption").each(function() {
					if ($(this).attr("checked")) {
						$('<input type="hidden" value="' + $(this).val() + '" name="filterOptions[' + ctr + ']">').appendTo('.filterCatalogForm');
						ctr++;
					}
				});
				$(".filterCatalogForm").submit();
			});

			$(".removeFilters").click(function() {
				window.location.reload();
			});

		});
	</script>
	<div class='box'>
		<div class="separator">
			<label style="font-size:1.3em;font-weight:bold;">Filter By</label>
			<c:if test="${!empty ca.filterOptions || (filteredPriceRange.minPrice != priceRange.minPrice) || (filteredPriceRange.maxPrice != priceRange.maxPrice)}">
				<a class="removeFilters"
				   style="cursor:pointer;float:right;margin-right:10px;color:#FF8219;font-size:1.1em;font-weight:bold;">(Remove
				                                                                                                        All)</a>
			</c:if>
		</div>
		<c:set var="ctr" value="0"/>
		<s:form beanclass="com.hk.web.action.core.catalog.category.CatalogAction" class="filterCatalogForm">
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
			<div class="separator">
				<h5 class='heading1'>
					PRICE
				</h5>

				<div class="demo">
					<div style="padding-bottom:10px;">
						<label id="amount" style="font-weight:bold;"></label>
					</div>
					<div id="slider-range" style="background-color:#f6931f"></div>
				</div>
			</div>
			<c:forEach items="${filterMap}" var="filter">
				<div class="separator">
					<h5 class='heading1'>
							${filter.key}
					</h5>
					<ul style="padding-left:0px;">
						<c:forEach items="${filter.value}" var="option">
							<li>
								<c:choose>
									<c:when test="${hk:collectionContains(ca.filterOptions, option.id)}">
										<input type="checkbox" class="filterOption" value="${option.id}"
										       checked="checked"/>
									</c:when>
									<c:otherwise>
										<input type="checkbox" class="filterOption" value="${option.id}"/>
									</c:otherwise>
								</c:choose>
									${option.value} (${option.qty})
							</li>
							<c:set var="ctr2" value="${ctr}"/>
							<c:set var="ctr" value="${ctr2+1}"/>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		</s:form>
	</div>


</s:layout-definition>
