<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.MapIndia" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.dao.location.MapIndiaDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" var="ca"/>
<s:layout-render name="/layouts/catalogLayoutG.jsp"
                 pageTitle="${ca.seoData.title}">
<s:layout-component name="brand">${ca.brand}</s:layout-component>
<s:layout-component name="htmlHead">
	<meta name="keywords" content="${ca.seoData.metaKeyword}"/>
	<meta name="description" content="${ca.seoData.metaDescription}"/>

	<script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jquery.session.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var perPage = $('.perPage-span').html();
			if (perPage) {
				$('.per_page').removeClass('active');
				$('.per_page').each(function(index) {
					if ($(this).text() == perPage) {
						//$(this).addClass('active');
					}
				});
			} else {
				$('.per_page').first().addClass('active');
			}
		});

	</script>
</s:layout-component>

<s:layout-component name="topBanner">
	<s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" categories="${ca.brand}"
	                 topCategories="${ca.brand}"/>
</s:layout-component>

<s:layout-component name="breadcrumb">
	<div class='crumb_outer'>
		<a href="${pageContext.request.contextPath}/" class="crumb">Home</a>
		&gt;
		<a href="${pageContext.request.contextPath}/brands" class="crumb">Brands</a>
		&gt;
		<span class="crumb last" style="font-size: 12px;">${ca.brand}</span>

		<h1 class="title">${ca.seoData.h1}</h1>
	</div>
</s:layout-component>

<s:layout-component name="catalog">
	<div class='catalog_header'>

		<div class="content">
            <div>
                <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
                    <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank" class="popup">Edit MetaData
                        <s:param name="seoData" value="${ca.brand}||${ca.topLevelCategory}"/>
                    </s:link>
                </shiro:hasPermission>
                <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
                    <s:link beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction" event="pre" target="_blank"
                            class="popup">Edit All Product Variants
                        <s:param name="brand" value="${ca.brand}"/>
                    </s:link>
                </shiro:hasPermission>
            </div>

			<h2><strong>${ca.seoData.h1}</strong></h2>

			<p>
				<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ca}"/>
				<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ca}"/>
			</p>

		</div>
	</div>

	<div class='catalog_filters'>
		<div class='catalog_filters grid_5 alpha'>
			<s:layout-render name="/layouts/brandCatalogFilter.jsp" menuNodeList="${ca.menuNodes}"/>
		</div>
	</div>


	<div class="catalog container_24">

		<div class='controls grid_18 alpha'>
			<a class='control' title='switch to grid view' id="grid-control" href="#">
				<div class="icon"></div>
				Grid View
			</a>
			<a class='control' href='#' title='switch to list view' id="list-control">
				<div class="icon"></div>
				List View
			</a>

		</div>

		<div id="prod_grid" class="grid_19">
			<c:forEach items="${ca.productList}" var="product" varStatus="ctr">
				<c:if test="${!product.googleAdDisallowed}">
					<div class="product_box grid_6">
						<s:layout-render name="/layouts/embed/_productThumb200.jsp" product="${product}" position="${ca.pageNo}/${ctr.index+1}"/>
						<div class="clear"></div>
					</div>
				</c:if>
			</c:forEach>
			<div class="floatfix"></div>
		</div>
		<div id="prod_list" class="grid_18">
			<c:forEach items="${ca.productList}" var="product">
				<c:if test="${!product.googleAdDisallowed}">
					<div class="product_list_box">
						<s:layout-render name="/layouts/embed/_productListG.jsp"
						                 productId="${product.id}"></s:layout-render>
					</div>
					<div class="floatfix"></div>
				</c:if>
			</c:forEach>
		</div>

		<div class='catalog_header'>
			<div class="content">
				<p>
					<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ca}"/>
					<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ca}"/>
				</p>
			</div>
		</div>

		<script type="text/javascript">
			$(document).ready(function() {
				$('#prod_list').hide();
				$('#prod_grid').show();
				$("#grid-control").addClass("active");

				$('#grid-control').click(function() {
					$('#grid-control, #list-control').removeClass("active");
					$('#prod_list').hide();
					$('#prod_grid').show();
					$(this).addClass("active");
					$.session("catalog-view", "grid");
				});
				$('#list-control').click(function() {
					$('#grid-control, #list-control').removeClass("active");
					$('#prod_grid').hide();
					$('#prod_list').show();
					$(this).addClass("active");
					$.session("catalog-view", "list");
				});

				if ($.session("catalog-view")) {
					if ($.session("catalog-view") == "list") {
						$('#grid-control, #list-control').removeClass("active");
						$('#prod_grid').hide();
						$('#prod_list').show();
						$('#list-control').addClass("active");
					} else   if ($.session("catalog-view") == "grid") {
						$('#grid-control, #list-control').removeClass("active");
						$('#prod_list').hide();
						$('#prod_grid').show();
						$('#grid-control').addClass("active");
					}
				}

			});

		</script>

		<c:if test="${hk:isNotBlank(ca.seoData.description)}">
			<div style="margin-top: 45px; background-color: #FAFCFE; padding: 10px; float: none; clear: both;">
				<h2><i>${ca.seoData.descriptionTitle}</i>: </h2>
					${ca.seoData.description}
			</div>
		</c:if>
	</div>

	<div style="height:75px"></div>
</s:layout-component>

</s:layout-render>
