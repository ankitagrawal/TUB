<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dao.catalog.category.CategoryDao" %>
<%@ page import="java.util.List" %>
<%@ page import="mhc.common.dto.MenuNode" %>
<%@ page import="mhc.service.MenuHelper" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.category.CatalogAction" var="ca"/>
<s:layout-definition>

  <%
    MenuHelper menuHelper = InjectorFactory.getInjector().getInstance(MenuHelper.class);
    CategoryDao categoryDao = InjectorFactory.getInjector().getInstance(CategoryDao.class);

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

    Category category = categoryDao.find(menuNode.getSlug());
    pageContext.setAttribute("filterCategory", category);

    String currentBrand = request.getParameter("brand");
    pageContext.setAttribute("currentBrand", currentBrand);

    List<String> categoryNames = new ArrayList<String>();
    if (category != null) {
      categoryNames.add(category.getName());
    }
    Category secondaryCategory = null;
    if (menuNode.getParentNode() != null) {
      secondaryCategory = categoryDao.find(menuNode.getParentNode().getSlug());
    }
    if (secondaryCategory != null) {
      categoryNames.add(secondaryCategory.getName());
    }
    List<String> brandList = categoryDao.getBrandsByCategory(categoryNames);

    pageContext.setAttribute("brandList", brandList);

  %>
  <div class='box'>
    <h5 class='heading1'>
      Categories
    </h5>
    <ul>
      <c:forEach items="${firstChildMenuNodes}" var="firstChildMenuNode">
        <li class="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) ? 'active' : ''}">
          <a href="${pageContext.request.contextPath}${firstChildMenuNode.url}">
              ${firstChildMenuNode.name}
          </a>
          <c:if test="${(hk:collectionContains(hierarchicalMenuNodes,firstChildMenuNode)) && fn:length(firstChildMenuNode.childNodes) > 0}">
            <ul>
              <c:forEach items="${firstChildMenuNode.childNodes}" var="secondChildMenuNode">
                <li class="lvl2">
                  <a href="${pageContext.request.contextPath}${secondChildMenuNode.url}" style="${currentMenuNode == secondChildMenuNode ? 'font-weight:bold;' : ''}">
                      ${secondChildMenuNode.name}
                  </a>
                  <c:if test="${(hk:collectionContains(hierarchicalMenuNodes,secondChildMenuNode)) && fn:length(secondChildMenuNode.childNodes) > 0}">
                    <ul>
                      <c:forEach items="${secondChildMenuNode.childNodes}" var="thirdChildMenuNode">
                        <li class="lvl3">
                          <a href="${pageContext.request.contextPath}${thirdChildMenuNode.url}" style="${currentMenuNode == thirdChildMenuNode ? 'font-weight:bold;' : ''}">
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
    <h5 class='heading1'>
      Brands
    </h5>
    <ul>
      <c:forEach items="${brandList}" var="navBrand">
        <li>
          <s:link beanclass="com.hk.web.action.category.CatalogAction" style="${currentBrand == navBrand ? 'font-weight:bold;' : ''}">
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
    <h5 class='heading1'>
      Filter by Price
    </h5>
    <ul>
      <li>
        <s:link beanclass="com.hk.web.action.category.CatalogAction">
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
      <li><s:link beanclass="com.hk.web.action.category.CatalogAction">
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
      <li><s:link beanclass="com.hk.web.action.category.CatalogAction">
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
      <li><s:link beanclass="com.hk.web.action.category.CatalogAction">
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
