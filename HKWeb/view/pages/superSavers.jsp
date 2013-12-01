<%@ page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.hk.constants.core.PermissionConstants"%>
<%@ page import="com.hk.pact.service.catalog.CategoryService"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean
	beanclass="com.hk.web.action.core.catalog.SuperSaversAction"
	var="comboBean" />

<s:layout-render name="/layouts/default100.jsp" pageTitle="Super Savers">
	<s:layout-component name="htmlHead">
		<style type="text/css">
div.heading {
	text-align: center;
	padding: 5px;
	margin: 5px;
}

input[type="text"].offerQuery {
	height: 14px;
	font-size: .8em;
	width: 90%;
	margin-top: 5px;
}

input[type="image"]#searchImage {
	position: absolute;
	right: 10px;
	top: 7px;
}

input[type="submit"].btn {
	width: 70px;
	margin: 0px 6px 5px;
	padding: 4px;
	font-size: 0.8em;
	font-weight: normal;
	font-style: italic;
	font-family: "Lucida Grande", "Lucida Sans Unicode", "Lucida Sans",
		Geneva, Verdana, sans-serif;
	color: #222288;
	-webkit-border-radius: 1em;
	-moz-border-radius: 1em;
	border-radius: 2em;
	border: 1px solid lightblue;
	text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.4);
	-webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.4), 0 1px 1px
		rgba(0, 0, 0, 0.2);
	-moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.4), 0 1px 1px
		rgba(0, 0, 0, 0.2);
	box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.4), 0;
	-webkit-transition-duration: 0.2s;
	-moz-transition-duration: 0.2s;
	transition-duration: 0.2s;
	background-color: #F27506;
	background: -moz-linear-gradient(rgba(255, 255, 255, 0.1),
		rgba(255, 255, 255, 0) 100% ) repeat scroll 0 0 #F27506;
}

input[type="submit"].btn:hover,input[type="submit"].btn:focus {
	position: relative;
	z-index: 1;
	text-shadow: 1px 1px 5px #999; /*background: #2A4E77;*/
	/*border: solid 1px #222288;*/ /*color: lightblue;*/
	border: solid 1px orangered;
}

input[type="submit"].btn:active {
	-webkit-box-shadow: inset 0 1px 4px rgba(0, 0, 0, 0.6);
	-moz-box-shadow: inset 0 1px 4px rgba(0, 0, 0, 0.6);
	box-shadow: inset 0 1px 4px rgba(0, 0, 0, 0.6);
	/*background: #2E5481;*/ /*border: solid 1px #203E5F;*/
	background: -moz-linear-gradient(rgba(255, 255, 255, 0.1),
		rgba(255, 255, 255, 0) 100% ) repeat scroll 0 0 orangered;
}

img.superSaverBanner {
	width: 100%;
}

div#filterDiv {
	float: left;
	width: 20%;
	background: none repeat scroll 0 0 #FAFCFE;
}

div#bannersDiv {
	float: right;
	width: 80%;
}

div#searchDiv {
	cursor: default;
	width: 30%;
	float: right;
}

a#hrefLink {
	display: none;
}

fieldset#categoryField {
	margin: 20px 10px;
	border: 1px solid lightblue;
	border-radius: 0.3em;
	color: #222288;
}

fieldset#categoryField  legend {
	font-size: 0.8em;
	padding: 5px;
	font-style: italic;
	color: #222288;
}

body {
	color: #222288;
}

p.category {
	margin: 5px 2px;
	padding: 2px;
}

div.paginationDiv {
	margin: 10px;
	text-align: center;
}
</style>
    <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/>
	</s:layout-component>

	<s:layout-component name="content">
		<% CategoryService categoryService = ServiceLocatorFactory.getService(CategoryService.class); %>
		<div class="heading">
		<div style="width: 100%; float: left;">
		<h1>Super Savers</h1>
		</div>

		<%--<div id="searchDiv">--%> <%--<s:form beanclass="com.hk.web.action.core.search.SearchAction" method="get" renderFieldsPresent="false"--%>
		<%--renderSourcePage="false" autocomplete="off" style="position: relative;">--%>
		<%--<s:text name="query" id="searchbox" class="input_tip offerQuery" title='search our catalog'--%>
		<%--value="${param['query']}" placeholder='search by brand'/>--%> <%--<s:image name="search" src="/images/icons/search2.png" id="searchImage"/>--%>
		<%--</s:form>--%> <%--</div>--%></div>

		<div class="clear"></div>

		<shiro:hasPermission
			name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
			<div style="padding: 5px;">
			<div class="grid_24 alpha omega" style="padding: 0 10px;"><s:link
				beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
				<span>Upload</span>
			</s:link> &nbsp;|&nbsp; <s:link
				beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction"
				event="getSuperSaversForCategoryAndBrand">
				<span>Manage Images</span>
			</s:link></div>
			</div>

			<div class="clear"></div>
			<div style="margin-top: 15px;"></div>
		</shiro:hasPermission>

		<div><s:form
			beanclass="com.hk.web.action.core.catalog.SuperSaversAction"
			id="filterForm">
			<div id="filterDiv">
			<fieldset id="categoryField"><legend>Filter by
			Category</legend>

			<div><c:forEach
				items="<%=categoryService.getPrimaryCategories()%>"
				var="primaryCategory">
				<c:set var="categoryName" value="${primaryCategory.name}" />
				<c:set var="isSelected" value="false" />
				<c:forEach items="${comboBean.categories}" var="selectedCategory">
					<c:if test="${selectedCategory == categoryName}">
						<c:set var="isSelected" value="true" />
					</c:if>
				</c:forEach>

				<p class="category"><c:choose>
					<c:when test="${isSelected eq true}">
						<input type="checkbox" class="categoryCheck"
							value="${categoryName}" checked="checked" />
                                        ${primaryCategory.displayName}
                                    </c:when>
					<c:otherwise>
						<input type="checkbox" class="categoryCheck"
							value="${categoryName}" />
                                        ${primaryCategory.displayName}
                                    </c:otherwise>
				</c:choose></p>
			</c:forEach></div>

			<div class="clear"></div>

			<div style="text-align: center; margin-top: 10px;"><s:submit
				name="pre" id="filterBtn" class="btn" style="float:left;">
                            Filter
                        </s:submit> <s:submit name="pre" id="showAllBtn"
				class="btn" style="float:right;">
                            Show All
                        </s:submit></div>
			</fieldset>
			</div>

			<div id="bannersDiv">
			<div class="paginationDiv"><s:layout-render
				name="/layouts/embed/paginationResultCount.jsp"
				paginatedBean="${comboBean}" /> <s:layout-render
				name="/layouts/embed/pagination.jsp" paginatedBean="${comboBean}" />
			</div>

			<div class="clear"></div>

			<c:forEach items="${comboBean.superSaverImages}" var="image">
				<c:set var="product" value="${image.product}" />
				<c:set var="productName" value="${product.name}" />
				<div style="margin: 0px 5px 30px 5px;"><s:link
					beanclass="com.hk.web.action.core.catalog.product.ProductAction"
					class="prod_link" title="${productName}">
					<s:param name="productId" value="${product.id}" />
					<s:param name="productSlug" value="${product.slug}" />
					<hk:superSaverImage imageId="${image.id}"
						size="<%=EnumImageSize.Original%>" alt="${image.altText}"
						class="superSaverBanner" />
				</s:link></div>

				<div class="clear"></div>
			</c:forEach>

			<div class="paginationDiv"><s:layout-render
				name="/layouts/embed/paginationResultCount.jsp"
				paginatedBean="${comboBean}" /> <s:layout-render
				name="/layouts/embed/pagination.jsp" paginatedBean="${comboBean}" />
			</div>

			<div class="clear"></div>
			</div>
		</s:form></div>

		<script type="text/javascript">
        $(document).ready(function() {
            $('#super_savers_button').addClass("active");

            $('#filterBtn').click(function() {
                var ctr = -1;
                $('.categoryCheck').each(function() {
                    if ($(this).attr("checked") == "checked") {
                        ctr ++;
                        $(this).attr("name", "categories[" + ctr + "]");
                    }
                });
            });
        });
    </script>
	</s:layout-component>
</s:layout-render>