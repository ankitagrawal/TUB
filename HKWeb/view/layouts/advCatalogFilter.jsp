<%@ page import="com.hk.helper.CatalogFilter" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dto.ProductOptionDto" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.dto.PriceRangeDto" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-definition>

<%
	MenuHelper menuHelper = (MenuHelper) ServiceLocatorFactory.getService("MenuHelper");
	CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);

	String urlFragment = (String) pageContext.getAttribute("filterUrlFragment");

	MenuNode menuNode = menuHelper.getMenuNode(urlFragment);
	pageContext.setAttribute("currentMenuNode", menuNode);
	MenuNode topParentNode = menuHelper.getTopParentMenuNode(menuNode);
	List<MenuNode> firstChildMenuNodes = menuHelper.getSiblings(topParentNode.getChildNodes().get(0).getUrl());
	pageContext.setAttribute("firstChildMenuNodes", firstChildMenuNodes);
	List<MenuNode> hierarchicalMenuNodes = menuHelper.getHierarchicalMenuNodes(menuNode);
	pageContext.setAttribute("hierarchicalMenuNodes", hierarchicalMenuNodes);

	// jugaad for IDE autocompletion in EL
	// this is passed through an attribute in layout-render tag
	pageContext.setAttribute("filterUrlFragment", urlFragment);

	Category category = categoryDao.getCategoryByName(menuNode.getSlug());
	pageContext.setAttribute("filterCategory", category);

	List<String> categoryNames = new ArrayList<String>();
	if (category != null) {
		categoryNames.add(category.getName());
	}
	Category secondaryCategory = null;
	if (menuNode.getParentNode() != null) {
		secondaryCategory = categoryDao.getCategoryByName(menuNode.getParentNode().getSlug());
		if (secondaryCategory != null) {
			categoryNames.add(secondaryCategory.getName());
		}
	}
	Category primaryCategory = null;
	if (menuNode.getParentNode() != null && menuNode.getParentNode().getParentNode() != null) {
		primaryCategory = categoryDao.getCategoryByName(menuNode.getParentNode().getParentNode().getSlug());
		if (primaryCategory != null) {
			categoryNames.add(primaryCategory.getName());
		}
	}


	CatalogFilter catalogFilter = (CatalogFilter) ServiceLocatorFactory.getService("CatalogFilter");
	/*String childCategory = ca.getChildCategorySlug();
	if (ca.getSecondaryChildCategorySlug() != null) {
		childCategory = ca.getSecondaryChildCategorySlug();
	}
	if (ca.getTertiaryChildCategorySlug() != null) {
		childCategory = ca.getTertiaryChildCategorySlug();
	}*/
	Map<String, List<ProductOptionDto>> filterMap = catalogFilter.getFilterOptions(categoryNames);
	pageContext.setAttribute("filterMap", filterMap);

	PriceRangeDto priceRange = catalogFilter.getPriceRange(categoryNames);
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
		width: 90%;
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
			step:1,
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
<div class='' style="border:1px solid #CCC">
	<div class="">
		<h5 class='heading1' style="background-color:#DDD;padding:3px;">
			Browse By Category
		</h5>
		<ul style="padding-left:10px;">
			<c:forEach items="${firstChildMenuNodes}" var="firstChildMenuNode">
				<li class="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) ? 'active' : ''}">
					<a href="${pageContext.request.contextPath}${firstChildMenuNode.url}"
					   style="font-size:1.2em;color:#444444;">
							${firstChildMenuNode.name}
					</a>
					<c:if test="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) && fn:length(firstChildMenuNode.childNodes) > 0}">
						<ul style="padding-left:20px;">
							<c:forEach items="${firstChildMenuNode.childNodes}" var="secondChildMenuNode">
								<li class="lvl2">
									<a href="${pageContext.request.contextPath}${secondChildMenuNode.url}"
									   style="${currentMenuNode == secondChildMenuNode ? 'font-weight:bold;font-size:1.1em;color:#444444;' : 'font-size:1.1em;color:#444444;'}">
											${secondChildMenuNode.name}
									</a>
									<c:if test="${(hk:collectionContains(hierarchicalMenuNodes,secondChildMenuNode)) && fn:length(secondChildMenuNode.childNodes) > 0}">
										<ul style="padding-left:30px;">
											<c:forEach items="${secondChildMenuNode.childNodes}"
											           var="thirdChildMenuNode">
												<li class="lvl3">
													<a href="${pageContext.request.contextPath}${thirdChildMenuNode.url}"
													   style="${currentMenuNode == thirdChildMenuNode ? 'font-weight:bold;font-size:1.0em;color:#444444;' : 'font-size:1.0em;color:#444444;'}">
															${thirdChildMenuNode.name}
													</a>
												</li>
											</c:forEach>
										</ul>
									</c:if>
								</li>
							</c:forEach>
						</ul>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
<div class='' style="margin-top:10px;border:1px solid #CCC">
	<div class="">
		<h5 class='heading1' style="background-color:#DDD;padding:3px;">
			Filter By
			<c:if test="${!empty ca.filterOptions || (filteredPriceRange.minPrice != priceRange.minPrice) || (filteredPriceRange.maxPrice != priceRange.maxPrice)}">
				<a class="removeFilters"
				   style="cursor:pointer;float:right;margin-right:10px;color:red;font-size:.9em;font-weight:bold;">(Clear
				                                                                                                        All)</a>
			</c:if>
		</h5>
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
		<div class="separator" style="margin-top:10px;">
			<h5 class='heading1' style="padding-left:5px;">
				PRICE <label id="amount" style="font-weight:normal; font-size:.9em;"></label>
			</h5>

			<div class="demo" style="padding:15px;">
				<%--<div style="padding-bottom:10px;">
					<label id="amount" style="font-weight:bold;"></label>
				</div>--%>
				<div id="slider-range" style="background-color:#f6931f"></div>
			</div>
		</div>
		<c:forEach items="${filterMap}" var="filter">
			<div class="">
				<h5 class='heading1' style="padding-left:5px;">
						${filter.key}
				</h5>
				<ul style="padding-left:10px;">
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
