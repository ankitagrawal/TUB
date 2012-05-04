<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.google.inject.name.Names" %>

<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.SearchAction" var="searchAction"/>
<s:layout-render name="/layouts/catalogLayout.jsp">
  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${searchAction}"/>
  <s:layout-component name="catalog">
    <c:if test="${searchAction.resultCount > 0}">
      <div class='catalog_header'>
        <div class="content">
          <h2>
            <span>search Results for <strong>${searchAction.query}</strong></span>
          </h2>

          <h3><s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${searchAction}"/></h3>
          <h3><s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${searchAction}"/></h3>
        </div>
      </div>
      <div class='catalog_filters'>
        <div class='box'>
          <h5 class='heading1'>
            Product Categories
          </h5>
          <s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
          <ul>
            <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
              <li><a href="${pageContext.request.contextPath}${topMenuNode.url}">${topMenuNode.name}</a></li>
            </c:forEach>
          </ul>
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
            show
            <s:link beanclass="web.action.SearchAction" class="per_page active">
              20
              <s:param name="query" value="${searchAction.query}"/>
              <s:param name="perPage" value="20"/>
            </s:link>
            |
            <s:link beanclass="web.action.SearchAction" class="per_page">
              40
              <s:param name="query" value="${searchAction.query}"/>
              <s:param name="perPage" value="40"/>
            </s:link>
            |
            <s:link beanclass="web.action.SearchAction" class="per_page">
              60
              <s:param name="query" value="${searchAction.query}"/>
              <s:param name="perPage" value="60"/>
            </s:link>
            |
            <s:link beanclass="web.action.SearchAction" class="per_page">
              80
              <s:param name="query" value="${searchAction.query}"/>
              <s:param name="perPage" value="80"/>
            </s:link>
            items per page.
          </div>
          <script type="text/javascript">
            var perPage = getUrlVars()["perPage"];
            if (perPage) {
              $('.active').removeClass('active');
              $('.per_page').each(function(index) {
                if ($(this).text() == perPage) {
                  $(this).addClass('active');
                }
              });
            }
            else {
              $('.per_page').first().addClass('active');
            }
            function getUrlVars()
            {
              var vars = [], hash;
              var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
              for (var i = 0; i < hashes.length; i++)
              {
                hash = hashes[i].split('=');
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
              }
              return vars;
            }
          </script>
        </div>

        <div id="prod_grid">
          <c:forEach items="${searchAction.productList}" var="product">
            <div class="product_box">
              <s:layout-render name="/layouts/embed/_productThumbSearchResults.jsp" productId="${product.id}"></s:layout-render>
            </div>
          </c:forEach>
        </div>
        <div id="prod_list" style="display:none;">
          <c:forEach items="${searchAction.productList}" var="product">
            <div class="product_list_box" id="prod_list">
              <s:layout-render name="/layouts/embed/_productListSearchResults.jsp" productId="${product.id}"></s:layout-render>
            </div>
          </c:forEach>
        </div>
        <script type="text/javascript">
          $(document).ready(function() {
            $('#prod_list').hide();
            $('#grid-control').click(function() {
              $('#grid-control, #list-control').removeClass("active");
              $('#prod_list').hide();
              $('#prod_grid').show();
              $(this).addClass("active");
            });
            $('#list-control').click(function() {
              $('#grid-control, #list-control').removeClass("active");
              $('#prod_grid').hide();
              $('#prod_list').show();
              $(this).addClass("active");
            });
          })

        </script>


        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${searchAction}"/>
      </div>
    </c:if>
    <c:if test="${searchAction.resultCount == 0}">
      <div class="catalog_header" style="line-height: 21px;">
        <h2 class="red">
          Sorry, no results were found for your query
        <span class="special">
          "${searchAction.query}"
        </span>
        </h2>

        <p>
        <h6>
          Please either
        </h6>
        <ul>
          <li>
            Modify your query
          </li>
          <li>
            OR use the navigation menu to find the product of your choice.
          </li>
        </ul>
        </p>
      </div>
    </c:if>
  </s:layout-component>
</s:layout-render>
