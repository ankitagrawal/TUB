<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
    String brands = (String) pageContext.getAttribute("brands");
    pageContext.setAttribute("brands", brands);
    List<String> brandList = new ArrayList<String>();
    if (StringUtils.isNotBlank(brands)) {
      for (String s : StringUtils.split(brands, ',')) {
        brandList.add(s.trim());
      }
    }
    pageContext.setAttribute("brandList", brandList);

    String categoryNames = (String) pageContext.getAttribute("categories");
    pageContext.setAttribute("categories", categoryNames);

    List<String> categoryNameList = new ArrayList<String>();
    if (StringUtils.isNotBlank(categoryNames)) {
      for (String s : StringUtils.split(categoryNames, ',')) {
        categoryNameList.add(s.trim());
      }
    }
    pageContext.setAttribute("categoryNameList", categoryNameList);

    String topCategoryNames = (String) pageContext.getAttribute("topCategories");
    pageContext.setAttribute("topCategories", categoryNames);

    List<String> topCategoryNameList = new ArrayList<String>();
    if (StringUtils.isNotBlank(topCategoryNames)) {
      for (String s : StringUtils.split(topCategoryNames, ',')) {
        topCategoryNameList.add(s.trim());
      }
    }
    pageContext.setAttribute("topCategoryNameList", topCategoryNameList);
  %>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'home-living')}">
    <jsp:include page="/categoryBanners/home-livingTopStripBanner.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'beauty')}">
    <jsp:include page="/categoryBanners/beautyPrepayDiscount.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(categoryNameList, 'clinical-supplies')}">
    <jsp:include page="/categoryBanners/stethescopeStripBanner.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'eye')}">
    <jsp:include page="/categoryBanners/eyeCallback.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'health-nutrition')}">
    <jsp:include page="/categoryBanners/healthNutriCallback.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'sports-nutrition')}">
    <jsp:include page="/categoryBanners/nutriCallback.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'sports')}">
    <jsp:include page="/categoryBanners/sportsDiscount.jsp"/>
  </c:if>

  <c:if test="${hk:collectionContains(topCategoryNameList, 'personal-care')}">
    <jsp:include page="/categoryBanners/personalCareStripBanner.jsp"/>
  </c:if>
  <c:if test="${hk:collectionContains(categoryNameList, 'devices') ||
            hk:collectionContains(categoryNameList, 'diabetic-food') ||
            hk:collectionContains(brandList, 'Frio') }">
    <jsp:include page="/categoryBanners/diabetesStripBanner.jsp"/>
  </c:if>

</s:layout-definition>
