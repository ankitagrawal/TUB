<%@ page import="java.util.Set" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" var="ca"/>
<s:layout-definition>
	<%
		Set<MenuNode> menuNodes = (Set<MenuNode>) pageContext.getAttribute("menuNodeList");
		pageContext.setAttribute("menuNodeList", menuNodes);
	%>
	<div class='' style="border-right:1px dotted #ddd">
		<div class="">
			<h5 class='heading1' style="background-color:#DDD;padding:5px;">
				Browse By Category
			</h5>
			<ul style="padding-left:10px;">
				<c:forEach items="${menuNodeList}" var="menuNode">
					<li>
						<a href="${pageContext.request.contextPath}${menuNode.url}?brand=${hk:urlEncode(ca.brand)}"
						   style="font-size:1.2em;color:#444444;">
								${menuNode.name}
						</a>
					</li>
				</c:forEach>
			</ul>
		</div>
    <div class="">
      <h5 class='heading1' style="background-color:#DDD;padding:5px;">
				<a href="${pageContext.request.contextPath}/brand/all/${ca.brand}"
						   style="font-size:.9em;color:#444444;">
								All ${ca.brand} Products
						</a>
			</h5>
			<%--<ul style="padding-left:10px;">
					<li>
						<a href="${pageContext.request.contextPath}/brand/all/${ca.brand}"
						   style="font-size:1.2em;color:#444444;">
								All ${ca.brand} Products
						</a>
					</li>
			</ul>--%>
		</div>
	</div>
	<script type="text/javascript">
		$('.catalog_filters ul li.lvl2').click(function() {
			url = $(this).children("a").attr("href");
			document.location.href = url;
		});
	</script>
</s:layout-definition>
