<% response.setContentType("text/plain"); %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.SitemapTxtAction" var="sitemapBean"/>
http://www.healthkart.com
http://www.healthkart.com/pages/aboutCompany.jsp
<c:forEach items="${sitemapBean.categoryUrls}" var="categoryUrl">
${categoryUrl}
</c:forEach>
<c:forEach items="${sitemapBean.productUrls}" var="productUrl">
${productUrl}
</c:forEach>
