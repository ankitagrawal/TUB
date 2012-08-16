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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.CompareAction" var="compareBean"/>

<s:layout-render name="/layouts/catalogLayoutG.jsp"
                 pageTitle="${ca.seoData.title}">
<s:layout-component name="topCategory">${ca.topCategoryUrlSlug}</s:layout-component>
<s:layout-component name="allCategories">${ca.allCategories}</s:layout-component>
<s:layout-component name="brand">${ca.brand}</s:layout-component>
<s:layout-component name="htmlHead">
  <meta name="keywords" content="${ca.seoData.metaKeyword}"/>
  <meta name="description" content="${ca.seoData.metaDescription}"/>
  <%
    CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
    List<Category> applicableCategories = new ArrayList<Category>();
    applicableCategories.add(categoryDao.getCategoryByName("bp-monitor"));
    pageContext.setAttribute("applicableCategories", applicableCategories);
    pageContext.setAttribute("eyeglasses", categoryDao.getCategoryByName("eyeglasses"));

    boolean isSecure = pageContext.getRequest().isSecure();
    pageContext.setAttribute("isSecure", isSecure);
    
    Category services = categoryDao.getCategoryByName("services");
    pageContext.setAttribute("services", services);
    if (ca.getRootCategorySlug().equals("services")) {
      MapIndiaDao mapIndiaDao = ServiceLocatorFactory.getService(MapIndiaDao.class);
      Cookie preferredZoneCookie = BaseUtils.getCookie(request, HealthkartConstants.Cookie.preferredZone);
      if (preferredZoneCookie != null && preferredZoneCookie.getValue() != null) {
        MapIndia mapIndia = mapIndiaDao.findByCity(preferredZoneCookie.getValue());
        if (preferredZoneCookie.getValue().equals("All") || (mapIndia != null && mapIndia.isTargetCity() != null && mapIndia.isTargetCity())) {
          pageContext.setAttribute("preferredZone", preferredZoneCookie.getValue());
        }
      }
    }
  %>

  <script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jquery.session.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      var perPage = $('.perPage-span').html();
      if (perPage) {
        $('.per_page').removeClass('active');
        $('.per_page').each(function(index) {
          if ($(this).text() == perPage) {
            $(this).addClass('active');
          }
        });
      }
      else {
        $('.per_page').first().addClass('active');
      }

      $('.compare_checkbox').click(function() {
        var selected = $('.compare_checkbox').filter(':checked').length;
        if (selected > 4) {
          alert("You can't select more than 4 products at a time.");
          return false;
        }
      });
      $('.checkSubmit').click(function() {
        var selected = $('.compare_checkbox').filter(':checked').length;
        if (selected < 2) {
          alert("Select at least 2 products for comparison.");
          return false;
        }
      });

      $('.sortBy').change(function() {
        $('#sorter').submit();
      });

      var sortOrderDeterminer = 0;
      $('.sortBy').click(function() {
        sortOrderDeterminer++;
        function DescIfEven(sortOrderDeterminer) {
          return (sortOrderDeterminer % 2) ? "desc" : "asc";
        }
      });

    });
    $(document).ready(function() {
      $('#selectCityWindow').jqm();
      $('#selectCityWindow').jqmShow();

      $('.jqmOverlay').click(function() {
        //        alert("Cliking w/o selecting" + $('#topLevelCategory').val());
        if ($('#topLevelCategory').val() == 'services') {
          //          alert("Inside");
          $.getJSON(
              $('#setDefaultZoneLink').attr('href')
              );
        }
      });
    });

  </script>
</s:layout-component>

<s:layout-component name="breadcrumb">
  <s:layout-render name="/layouts/embed/catalogBreadcrumb.jsp" breadcrumbUrlFragment="${ca.urlFragment}"
                   topHeading="${ca.seoData.h1}"/>
  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
    <div align="center">
      <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank" class="popup">Edit MetaData
        <s:param name="seoData" value="${ca.seoData.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>

  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
    <div align="center">
      <s:link beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction" event="pre" target="_blank"
              class="popup">Edit All Product Variants
        <s:param name="category" value="${ca.category.name}"/>
      </s:link>
    </div>
  </shiro:hasPermission>
</s:layout-component>

<s:layout-component name="topBanner">
  <s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" categories="${ca.menuNode.slug}"
                   topCategories="${ca.topCategoryUrlSlug}" brandList="${ca.brandList}"/>
  <c:if test="${ca.topCategoryUrlSlug == 'services'}">
    <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment="${ca.urlFragment}"/>
  </c:if>
</s:layout-component>
<c:if test="${preferredZone == null && ca.rootCategorySlug == 'services'}">
  <s:layout-component name="modal">
    <div class="jqmWindow" style="width:700px;" id="selectCityWindow">
      <s:layout-render name="/layouts/modal.jsp">
        <s:layout-component name="heading"><h3>Select City</h3></s:layout-component>
        <s:layout-component name="content">
          <s:form beanclass="com.hk.web.action.core.catalog.category.ServiceAction">
            <s:errors/>
            <div class="round-cont" style="width:650px;margin-top: 20px;">
              <label>To find the relevant deals, Please select your city</label>
              <s:select name="preferredZone">
                <s:option value="Delhi">Delhi-NCR</s:option>
                <s:option value="Mumbai">Mumbai</s:option>
                <s:option value="Chandigarh">Chandigarh</s:option>
                <s:option value="Bangalore">Bangalore</s:option>
                <s:option value="Hyderabad">Hyderabad</s:option>
                <s:option value="Chennai">Chennai</s:option>
                <s:option value="Jaipur">Jaipur</s:option>
                <s:option value="Calcutta">Kolkata</s:option>
                <s:option value="Pune">Pune</s:option>
                <s:option value="Ahmedabad">Ahmedabad</s:option>
                <s:option value="">Rest Of India</s:option>
              </s:select>
            </div>
            <s:submit name="setCookie" value="Select"/>
            <s:hidden name="redirectUrl" value="${pageContext.request.contextPath}${ca.urlFragment}"/>
          </s:form>
        </s:layout-component>
      </s:layout-render>
    </div>
  </s:layout-component>
</c:if>
<s:layout-component name="catalog">
<input type="hidden" id="topLevelCategory" value="${ca.topCategoryUrlSlug}">

<div style="display: none;">
  <s:link beanclass="com.hk.web.action.core.catalog.category.ServiceAction" id="setDefaultZoneLink" event="setDefaultCookie"/>
</div>
<div class='catalog_header'>

  <div class="content">
    <h2><strong>${ca.seoData.h1}</strong></h2>

    <p>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ca}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ca}"/>
    </p>

  </div>
</div>
<c:choose>
	<c:when test="${ca.childCategorySlug == 'eyeglasses'}">
		<div class='catalog_filters grid_6 alpha'>
		<s:layout-render name="/layouts/advCatalogFilter.jsp" filterUrlFragment="${ca.urlFragment}"/>
		</div>
	</c:when>
	<c:otherwise>
		<div class='catalog_filters grid_6 bk_blue alpha'>
		<s:layout-render name="/layouts/catalogFilter.jsp" filterUrlFragment="${ca.urlFragment}"/>
		</div>
	</c:otherwise>
</c:choose>


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

  <div class='per grid_10'>
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" id="sort-popularity" class='active control'
            rel="nofollow">
      Popularity
      <c:choose>
        <c:when test="${ca.sortOrder =='desc'}">&uarr;</c:when>
        <c:otherwise>&darr;</c:otherwise>
      </c:choose>
      <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.startRange)}"><s:param name="startRange" value="${ca.startRange}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.endRange)}"><s:param name="endRange" value="${ca.endRange}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug"
                                                                   value="${ca.childCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug"
                                                                  value="${ca.rootCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug"
                                                                            value="${ca.secondaryChildCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug"
                                                                           value="${ca.tertiaryChildCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
      <s:param name="sortBy" value="ranking"/>
      <s:param name="sortOrder" value="${ca.sortOrder =='asc' ? 'desc' : 'asc'}"/>
      <s:param name="perPage" value="${ca.perPage}"/>
    </s:link>
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" id="sort-price" class='control' rel="nofollow">
      Price
      <c:choose>
        <c:when test="${ca.sortOrder =='desc'}">&darr;</c:when>
        <c:otherwise>&uarr;</c:otherwise>
      </c:choose>
      <c:if test="${hk:isNotBlank(ca.brand)}"><s:param name="brand" value="${ca.brand}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.startRange)}"><s:param name="startRange" value="${ca.startRange}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.endRange)}"><s:param name="endRange" value="${ca.endRange}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.childCategorySlug)}"><s:param name="childCategorySlug"
                                                                   value="${ca.childCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.rootCategorySlug)}"><s:param name="rootCategorySlug"
                                                                  value="${ca.rootCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.secondaryChildCategorySlug)}"><s:param name="secondaryChildCategorySlug"
                                                                            value="${ca.secondaryChildCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.tertiaryChildCategorySlug)}"><s:param name="tertiaryChildCategorySlug"
                                                                           value="${ca.tertiaryChildCategorySlug}"/></c:if>
      <c:if test="${hk:isNotBlank(ca.preferredZone)}"><s:param name="preferredZone" value="${ca.preferredZone}"/></c:if>
      <s:param name="sortBy" value="hk_price"/>
      <s:param name="sortOrder" value="${ca.sortOrder =='desc' ? 'asc' : 'desc'}"/>
    </s:link>
  </div>

  <div class='per grid_11'>
    show
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" class="per_page active" rel="nofollow">
      20
      <s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
      <s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
      <s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
      <s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
      <s:param name="brand" value="${ca.brand}"/>
      <s:param name="startRange" value="${ca.startRange}"/>
      <s:param name="endRange" value="${ca.endRange}"/>
      <s:param name="perPage" value="20"/>
      <s:param name="pageNo" value="${ca.pageNo}"/>
      <s:param name="sortBy" value="${ca.sortBy}"/>
      <s:param name="sortOrder" value="${ca.sortOrder}"/>
    </s:link>
    |
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" class="per_page" rel="nofollow">
      40
      <s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
      <s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
      <s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
      <s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
      <s:param name="brand" value="${ca.brand}"/>
      <s:param name="startRange" value="${ca.startRange}"/>
      <s:param name="endRange" value="${ca.endRange}"/>
      <s:param name="perPage" value="40"/>
      <s:param name="pageNo" value="${ca.pageNo}"/>
      <s:param name="sortBy" value="${ca.sortBy}"/>
      <s:param name="sortOrder" value="${ca.sortOrder}"/>
    </s:link>
    |
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" class="per_page" rel="nofollow">
      60
      <s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
      <s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
      <s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
      <s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
      <s:param name="brand" value="${ca.brand}"/>
      <s:param name="startRange" value="${ca.startRange}"/>
      <s:param name="endRange" value="${ca.endRange}"/>
      <s:param name="perPage" value="60"/>
      <s:param name="pageNo" value="${ca.pageNo}"/>
      <s:param name="sortBy" value="${ca.sortBy}"/>
      <s:param name="sortOrder" value="${ca.sortOrder}"/>
    </s:link>
    |
    <s:link beanclass="com.hk.web.action.core.catalog.category.CatalogAction" class="per_page" rel="nofollow">
      80
      <s:param name="rootCategorySlug" value="${ca.rootCategorySlug}"/>
      <s:param name="childCategorySlug" value="${ca.childCategorySlug}"/>
      <s:param name="secondaryChildCategorySlug" value="${ca.secondaryChildCategorySlug}"/>
      <s:param name="tertiaryChildCategorySlug" value="${ca.tertiaryChildCategorySlug}"/>
      <s:param name="brand" value="${ca.brand}"/>
      <s:param name="startRange" value="${ca.startRange}"/>
      <s:param name="endRange" value="${ca.endRange}"/>
      <s:param name="perPage" value="80"/>
      <s:param name="pageNo" value="${ca.pageNo}"/>
      <s:param name="sortBy" value="${ca.sortBy}"/>
      <s:param name="sortOrder" value="${ca.sortOrder}"/>
    </s:link>
    items per page.
  </div>
</div>

<div id="prod_grid" class="grid_18" style="${ca.rootCategorySlug == "services"?"display:none":""}">
  <s:form beanclass="com.hk.web.action.core.catalog.CompareAction" target="_blank">
    <c:forEach items="${ca.productList}" var="product">
      <c:if test="${!product.googleAdDisallowed}">
        <div class="product_box grid_4">
          <s:layout-render name="/layouts/embed/_productThumbG.jsp" product="${product}"/>
          <div class="clear"></div>

          <div class="compareDiv">
            <c:if test="${hk:collectionContainsCollection(product.categories, applicableCategories)}">
              <s:checkbox name="products[]" value="${product.id}" class="compare_checkbox"/>
              <s:submit name="createTable" value="Compare" class="checkSubmit"
                        style="display:inline;padding:0;margin:0;font-size:10px;"/>
            </c:if>
          </div>
        </div>
      </c:if>
    </c:forEach>
    <script type="text/javascript">
      $('.compare_checkbox').each(function(i, el) {
        var x = $(this).siblings('.product');
        $(el).appendTo(x)
      });
    </script>
  </s:form>
  <div class="floatfix"></div>
</div>
<div id="prod_list" class="grid_18" style="${ca.rootCategorySlug == "services"?"":"display:none"}">
  <c:forEach items="${ca.productList}" var="product">
    <c:if test="${!product.googleAdDisallowed}">
      <div class="product_list_box">
        <s:layout-render name="/layouts/embed/_productListG.jsp" productId="${product.id}"></s:layout-render>
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
    if ($('#topLevelCategory').val() == 'services') {
      $('#prod_grid').hide();
      $('#prod_list').show();
      $("#list-control").addClass("active");
    } else {
      $('#prod_list').hide();
      $('#prod_grid').show();
      $("#grid-control").addClass("active");
    }

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
    if ($.session("sort-view")) {
      if ($.session("sort-view") == "popularity") {
        $('#sort-popularity, #sort-price').removeClass("active");
        $('#sort-popularity').addClass("active");
      } else if ($.session("sort-view") == "price") {
        $('#sort-popularity, #sort-price').removeClass("active");
        $('#sort-price').addClass("active");
      }
    }
    $('#sort-popularity').click(function() {
      $('#sort-popularity, #sort-price').removeClass("active");
      $(this).addClass("active");
      $.session("sort-view", "popularity");
    });
    $('#sort-price').click(function() {
      $('#sort-popularity, #sort-price').removeClass("active");
      $(this).addClass("active");
      $.session("sort-view", "price");
    });
  });

</script>

<c:if test="${hk:isNotBlank(ca.seoData.description)}">
  <div style="margin-top: 45px; background-color: #FAFCFE; padding: 10px; float: none; clear: both;">
    <h2><i>${ca.seoData.descriptionTitle}</i>: </h2>
      ${ca.seoData.description}
  </div>
</c:if>
</div>

		<c:if test="${not isSecure }">
			<iframe
				src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e200&pid=&catid=${ca.rootCategorySlug}&subcat1id=${ca.childCategorySlug}&subcat2id=${ca.secondaryChildCategorySlug}&section=1&level=1"
				scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
				frameborder="0"></iframe>
		</c:if>
		

		<div style="height:75px"></div>
</s:layout-component>

</s:layout-render>
