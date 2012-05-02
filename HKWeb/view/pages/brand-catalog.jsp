<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.BrandCatalogAction" var="sbbaBean"/>

<s:layout-render name="/layouts/catalogLayout.jsp" pageTitle="${sbbaBean.seoData.title}">

  <s:layout-component name="brand">${sbbaBean.brand}</s:layout-component>

  <s:layout-component name="htmlHead">
    <meta name="keywords" content="${sbbaBean.seoData.metaKeyword}"/>
    <meta name="description" content="${sbbaBean.seoData.metaDescription}"/>

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

        $('#brands_button').addClass("active");

      });
    </script>
  </s:layout-component>

  <s:layout-component name="topBanner">
    <s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" categories="${sbbaBean.brand}"
                     topCategories="${sbbaBean.brand}"/>
  </s:layout-component>

  <s:layout-component name="breadcrumb">
    <div class='crumb_outer'>
      <a href="${pageContext.request.contextPath}" class="crumb">Home</a>
      &gt;
      <a href="${pageContext.request.contextPath}/brands" class="crumb">Brands</a>
      &gt;
      <span class="crumb last" style="font-size: 12px;">${sbbaBean.brand}</span>

      <h1 class="title">${sbbaBean.seoData.h1}</h1>
    </div>
  </s:layout-component>
  <s:layout-component name="catalog">
    <div class='catalog_header'>
      <div>
        <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
            <s:link beanclass="com.hk.web.action.SeoAction" event="pre" target="_blank" class="popup">Edit MetaData
              <s:param name="seoData" value="${sbbaBean.brand}"/>
            </s:link>
        </shiro:hasPermission>
        <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
            <s:link beanclass="com.hk.web.action.admin.BulkEditProductAction" event="pre" target="_blank"
                    class="popup">Edit All Product Variants
              <s:param name="brand" value="${sbbaBean.brand}"/>
            </s:link>
        </shiro:hasPermission>
      </div>
      <div class="content">
        <h2><strong>${sbbaBean.brand} Products</strong></h2>

        <h3><s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sbbaBean}"/></h3>

        <h3><s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sbbaBean}"/></h3>

      </div>
    </div>
    <div class='catalog_filters'>
      <div class='catalog_filters grid_6 bk_blue alpha'>
        <s:layout-render name="/layouts/brandCatalogFilter.jsp" menuNodeList="${sbbaBean.menuNodes}"/>
      </div>
    </div>

    <div class="catalog">
      <div class='controls'>
        <a class='active control' title='switch to grid view' id="grid-control" href="#">
          <div class='icon'></div>
          Grid View
        </a>
        |
        <a class='control' href='#' title='switch to list view' id="list-control">
          <div class='icon'></div>
          List View
        </a>

        <div class='per'>

        </div>

      </div>

      <div id="prod_grid">
        <c:forEach items="${sbbaBean.productList}" var="product">
          <c:if test="${!product.googleAdDisallowed}">
            <div class="product_box">
              <s:layout-render name="/layouts/embed/_productThumb.jsp" productId="${product.id}"></s:layout-render>
            </div>
          </c:if>
        </c:forEach>
      </div>
      <div id="prod_list">
        <c:forEach items="${sbbaBean.productList}" var="product">
          <c:if test="${!product.googleAdDisallowed}">
            <div class="product_list_box" id="prod_list">
              <s:layout-render name="/layouts/embed/_productList.jsp" productId="${product.id}"></s:layout-render>
            </div>
          </c:if>
        </c:forEach>
      </div>
      <script type="text/javascript">
        $(document).ready(function() {
          $('#prod_list').hide();

          if ($.session("catalog-view")) {
            if ($.session("catalog-view") == "list") {
              $('#grid-control, #list-control').removeClass("active");
              $('#prod_grid').hide();
              $('#prod_list').show();
              $('#list-control').addClass("active");
            } else if ($.session("catalog-view") == "grid") {
              $('#grid-control, #list-control').removeClass("active");
              $('#prod_list').hide();
              $('#prod_grid').show();
              $('#grid-control').addClass("active");
            }
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
        })

      </script>

      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sbbaBean}"/>


    </div>
    <c:if test="${hk:isNotBlank(sbbaBean.seoData.description)}">
      <div style="margin-top: 45px; background-color: #FAFCFE; padding: 10px; float: none; clear: both;">
        <h2><i>${sbbaBean.seoData.descriptionTitle}</i>: </h2>
          ${sbbaBean.seoData.description}
      </div>
    </c:if>
  </s:layout-component>

  <s:layout-component name="zopim">
    <jsp:include page="/includes/_zopim.jsp"/>
  </s:layout-component>

</s:layout-render>
