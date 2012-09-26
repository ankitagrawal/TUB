<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-definition>

  <%
    MenuHelper menuHelper =(MenuHelper) ServiceLocatorFactory.getService("MenuHelper");
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

    String currentBrand = request.getParameter("brand");
    pageContext.setAttribute("currentBrand", currentBrand);

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

    List<String> brandList = categoryDao.getBrandsByCategory(categoryNames);

    pageContext.setAttribute("brandList", brandList);

  %>
  <div class=''>
    <h5 class='heading1' style="background-color:#DDD;padding:5px;">
      Browse By Category
    </h5>
    <ul>
      <c:forEach items="${firstChildMenuNodes}" var="firstChildMenuNode">
        <li class="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) ? 'active' : ''}">
          <a href="${pageContext.request.contextPath}${firstChildMenuNode.url}" style="font-size:1.2em;color:#444444;">
              ${firstChildMenuNode.name}
          </a>
          <c:if test="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) && fn:length(firstChildMenuNode.childNodes) > 0}">
            <ul>
              <c:forEach items="${firstChildMenuNode.childNodes}" var="secondChildMenuNode">
                <li class="lvl2">
                  <a href="${pageContext.request.contextPath}${secondChildMenuNode.url}" style="${currentMenuNode == secondChildMenuNode ? 'font-weight:bold;' : ''} font-size:1.1em;color:#444444;">
                      ${secondChildMenuNode.name}
                  </a>
                  <c:if test="${(hk:collectionContains(hierarchicalMenuNodes,secondChildMenuNode)) && fn:length(secondChildMenuNode.childNodes) > 0}">
                    <ul>
                      <c:forEach items="${secondChildMenuNode.childNodes}" var="thirdChildMenuNode">
                        <li class="lvl3">
                          <a href="${pageContext.request.contextPath}${thirdChildMenuNode.url}" style="${currentMenuNode == thirdChildMenuNode ? 'font-weight:bold;' : ''} font-size:1.0em;color:#444444;" >
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
    <h5 class='heading1' style="background-color:#DDD;padding:5px;">
      Browse By Brand
    </h5>
    <ul>
      <c:forEach items="${brandList}" var="navBrand">
        <li>
          <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" style="${currentBrand == navBrand ? 'font-weight:bold;' : ''} font-size:1.2em;color:#444444;">
            ${navBrand}
            <s:param name="brand" value="${navBrand}"/>
            <c:if test="${hk:isNotBlank(ca.startRange)}"><s:param name="startRange" value="${ca.startRange}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.endRange)}"><s:param name="endRange" value="${ca.endRange}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug" value="${ca.childCategorySlug}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.sortBy)}"><s:param name="sortBy" value="${ca.sortBy}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.sortOrder)}"><s:param name="sortOrder" value="${ca.sortOrder}"/></c:if>
            <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
          </s:link>
        </li>
      </c:forEach>
    </ul>
    <h5 class='heading1' style="background-color:#DDD;padding:5px;">
      Browse by Price
    </h5>
    <ul>
      <li>
        <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" style="font-size:1.2em;color:#444444;">
          Below Rs 500
          <s:param name="startRange" value="0"/>
          <s:param name="endRange" value="500"/>
          <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug" value="${ca.childCategorySlug}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.sortBy)}"><s:param name="sortBy" value="${ca.sortBy}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.sortOrder)}"><s:param name="sortOrder" value="${ca.sortOrder}"/></c:if>
          <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
          <s:param name="perPage" value="${ca.perPage}"/>
        </s:link>
      </li>
      <li><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" style="font-size:1.2em;color:#444444;">
        Between Rs 500-1000
        <s:param name="startRange" value="500"/>
        <s:param name="endRange" value="1000"/>
        <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug" value="${ca.childCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortBy)}"><s:param name="sortBy" value="${ca.sortBy}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortOrder)}"><s:param name="sortOrder" value="${ca.sortOrder}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
        <s:param name="perPage" value="${ca.perPage}"/>
      </s:link>
      </li>
      <li><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" style="font-size:1.2em;color:#444444;">
        Between Rs 1000-2500
        <s:param name="startRange" value="1000"/>
        <s:param name="endRange" value="2500"/>
        <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug" value="${ca.childCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortBy)}"><s:param name="sortBy" value="${ca.sortBy}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortOrder)}"><s:param name="sortOrder" value="${ca.sortOrder}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
      </s:link>
      </li>
      <li><s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" style="font-size:1.2em;color:#444444;">
        Above Rs 2500
        <s:param name="startRange" value="2500"/>
        <s:param name="endRange" value="1000000"/>
        <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug" value="${ca.childCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortBy)}"><s:param name="sortBy" value="${ca.sortBy}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.sortOrder)}"><s:param name="sortOrder" value="${ca.sortOrder}"/></c:if>
        <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
        <s:param name="perPage" value="${ca.perPage}"/>
      </s:link>
      </li>
    </ul>
  </div>

  <script type="text/javascript">
    $('.catalog_filters ul li.lvl2').click(function() {
      url = $(this).children("a").attr("href");
      document.location.href = url;
    });
  </script>
</s:layout-definition>
