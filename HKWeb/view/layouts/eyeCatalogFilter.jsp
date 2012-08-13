<%@ page import="com.hk.helper.CatalogFilter" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dto.ProductOptionDto" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-definition>

	<%
		CatalogFilter catalogFilter = (CatalogFilter) ServiceLocatorFactory.getService("CatalogFilter");
		Map<String, Set<ProductOptionDto>> filterMap = catalogFilter.getFilterOptions(ca.getChildCategorySlug());
		pageContext.setAttribute("filterMap", filterMap);

	%>
	<div class='box'>
		<c:set var="ctr" value="0"/>
		<c:forEach items="${filterMap}" var="filter">
			<h5 class='heading1'>
				Filter by ${filter.key}
			</h5>
			<ul>
				<c:forEach items="${filter.value}" var="option">
					<li><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction">
						<s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
						<s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
						<s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
						<s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
						<s:param name="filterOptions[${ctr+1}]" value="${option.id}"/>
						${option.value} (${option.qty})</s:link></li>
				</c:forEach>

			</ul>
		</c:forEach>

	</div>
</s:layout-definition>
