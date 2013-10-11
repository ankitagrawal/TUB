<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.order.Order" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <%
        if (pageContext.getAttribute("pageType") != null) {
            String pageType = (String) pageContext.getAttribute("pageType");
            pageContext.setAttribute("pageType", pageType);
        }

        if (pageContext.getAttribute("allCategories") != null) {
            String categories = (String) pageContext.getAttribute("allCategories");
            pageContext.setAttribute("allCategories", categories);
        }

        if (pageContext.getAttribute("primaryCategory") != null) {
            String primaryCategory = (String) pageContext.getAttribute("primaryCategory");
            pageContext.setAttribute("primaryCategory", primaryCategory);
        }

        if (pageContext.getAttribute("secondaryCategory") != null) {
            String secondaryCategory = (String) pageContext.getAttribute("secondaryCategory");
            pageContext.setAttribute("secondaryCategory", secondaryCategory);
        }

        if (pageContext.getAttribute("tertiaryCategory") != null) {
            String tertiaryCategory = (String) pageContext.getAttribute("tertiaryCategory");
            pageContext.setAttribute("tertiaryCategory", tertiaryCategory);
        }

        if (pageContext.getAttribute("brand") != null) {
            String brand = (String) pageContext.getAttribute("brand");
            pageContext.setAttribute("brand", brand);
        }

        if (pageContext.getAttribute("order") != null) {
            Order order = (Order) pageContext.getAttribute("order");
            pageContext.setAttribute("order", order);
        }

        if (pageContext.getAttribute("product") != null) {
            Product product = (Product) pageContext.getAttribute("product");
            pageContext.setAttribute("product", product);
        }
    %>
  <c:set var="searchString" value="'" />
  <c:set var="replaceString" value="\\'" />

  <script type="text/javascript">
    var google_tag_params = {
      pageType: '${pageType}',
      pId: '${product.id}',
      vId: '',
      mType: '',                                                  <%-- member type --%>
      g: '',                                                      <%-- gender --%>
      pCat: '${fn:replace(primaryCategory, searchString, replaceString)}',
      sCat: '${fn:replace(secondaryCategory, searchString, replaceString)}',
      tCat: '${fn:replace(tertiaryCategory, searchString, replaceString)}',
      allCats: '${fn:replace(allCategories, searchString, replaceString)}',
      brand: '${fn:replace(brand, searchString, replaceString)}',
      pName: '${fn:replace(product.name, searchString, replaceString)}',
      hkp: '',
      mrp: '',
      stock: ''
    };
    if (window.orderCount) {google_tag_params.orderCount = window.orderCount;}
    if (window.lpBrand) {google_tag_params.lpBrand = window.lpBrand;}
    if (window.lpPrimCat) {google_tag_params.lpPrimCat = window.lpPrimCat;}
    if (window.orderCount) {google_tag_params.lpCatTree = window.lpCatTree;}
  </script>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'sports-nutrition') || hk:equalsIgnoreCase(primaryCategory, 'health-nutrition')}">
    <%-- Nutrition account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="980691662"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'beauty')}">
    <%-- Beauty account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="949650175"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'diabetes')}">
    <%-- Diabetes account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="1009959035"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'health-devices')}">
    <%-- Health Devices account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="949530774"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'home-living')}">
    <%-- Home Living account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="990452499"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'personal-care')}">
    <%-- Personal Care account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="1009976885"/>
  </c:if>

  <c:if test="${hk:equalsIgnoreCase(primaryCategory, 'sports')}">
    <%-- Sports Care account new remarketing tag --%>
    <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="1008616035"/>
  </c:if>


  <%-- DYPLA / dynamic remarketing account new remarketing tag --%>
  <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="988370835"/>
  <%-- Brand account new remarketing tag --%>
  <%--<s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="1018305592"/>--%>
  <%-- aqua bright hk common new remarketing tag --%>
  <s:layout-render name="/layouts/embed/_remarketingCodeAdwords.jsp" id="981492453"/>

  <%-- Script for Facebook Retargeting --%>
	<script type="text/javascript">
	adroll_adv_id = "SKDGP6YYENHVJCJDIKHUF7";
	adroll_pix_id = "JLZMDLGRYBFDFHEIKFE456";
	(function () {
	var oldonload = window.onload;
	window.onload = function(){
	   __adroll_loaded=true;
	   var scr = document.createElement("script");
	   var host = (("https:" == document.location.protocol) ? "https://s.adroll.com" : "http://a.adroll.com");
	   scr.setAttribute('async', 'true');
	   scr.type = "text/javascript";
	   scr.src = host + "/j/roundtrip.js";
	   ((document.getElementsByTagName('head') || [null])[0] ||
	    document.getElementsByTagName('script')[0].parentNode).appendChild(scr);
	   if(oldonload){oldonload()}};
	}());
	</script>

</s:layout-definition>
