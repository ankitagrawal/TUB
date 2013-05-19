<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.search.SearchAction" var="ca"/>
<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/catalogLayoutG.jsp">
	<s:layout-component name="htmlHead">

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

	<s:layout-component name="catalog">
		<div class='catalog_header'>

			<div class="content">
				<c:choose>
					<c:when test="${hk:isNotBlank(ca.searchSuggestion)}">
						<span>your search <strong>"${ca.query}"</strong> did not match any product</span>
						<br/>
						<span>showing results for <strong>"${ca.searchSuggestion}"</strong></span>
					</c:when>
					<c:otherwise>
						<span>search results for <strong>"${ca.query}"</strong></span>
					</c:otherwise>
				</c:choose>

				<p>
					<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ca}"/>
					<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ca}"/>
				</p>

			</div>
		</div>

		<div class='catalog_filters'>
			<div class='catalog_filters grid_5 alpha'>
				<div class=''>
					<h5 class='heading1' style="background-color:#DDD;padding:5px;">
						Product Categories
					</h5>
					<s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
					<ul>
						<c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
							<li><a href="${pageContext.request.contextPath}${topMenuNode.url}"
							       style="font-size:1.2em;color:#444444;">${topMenuNode.name}</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>


		<div class="catalog">

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
		</div>

		<div style="height:75px"></div>

		<c:if test="${not isSecure }">
			 <iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e300&pid=${ca.productList[0].id}&catid=&subcat1id=&subcat2id=&pname=&image=&lp=&old=&new=&misc=&section=1&level=2&uid=${user_hash}"
			         scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0">

			 </iframe>
		</c:if>

	</s:layout-component>

</s:layout-render>
