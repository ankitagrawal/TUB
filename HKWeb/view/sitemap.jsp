<?xml version="1.0" encoding="UTF-8"?>
<% response.setContentType("text/xml"); %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.menu.SitemapAction" var="sitemapBean"/>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>http://www.healthkart.com</loc>
    <priority>1.0</priority>
    <changefreq>weekly</changefreq>
  </url>
  <url>
    <loc>http://www.healthkart.com/pages/aboutCompany.jsp</loc>
  </url>
  <url>
    <loc>http://www.healthkart.com/contact</loc>
  </url>
  <c:forEach items="${sitemapBean.categoryUrls}" var="categoryUrl">
    <url>
      <loc>${categoryUrl}</loc>
      <priority>0.7</priority>
      <changefreq>weekly</changefreq>
    </url>
  </c:forEach>
  <c:forEach items="${sitemapBean.productUrls}" var="productUrl">
    <url>
      <loc>${productUrl}</loc>
      <priority>0.5</priority>
      <changefreq>weekly</changefreq>
    </url>
  </c:forEach>
</urlset>