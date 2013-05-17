<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
    <%
        List<String> brandList = (List<String>) pageContext.getAttribute("brandList");
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
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'sports-nutrition') || hk:collectionContains(categoryNameList, 'creatine') || hk:collectionContains(categoryNameList, 'proteins')}">--%>
    <%--<jsp:include page="/categoryBanners/sportsNutrition.jsp"/>--%>
    <%--</c:if>--%>

    <%--  <c:if test="${hk:collectionContains(topCategoryNameList, 'beauty')}">
        --%><%--<jsp:include page="/categoryBanners/beautyIntroDiscount.jsp"/>--%><%--
    <jsp:include page="/categoryBanners/beautyHatTrickDisocunt.jsp"/>
  </c:if>--%>


  <c:if test="${hk:collectionContains(topCategoryNameList, 'home-living')}">
    <jsp:include page="/categoryBanners/home-livingTopStripBanner.jsp"/>
  </c:if>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'beauty')}">
        <jsp:include page="/categoryBanners/beautyPrepayDiscount.jsp"/>
    </c:if>

    <%--<c:if test="${hk:collectionContains(topCategoryNameList, 'beauty')}">--%>
    <%--<jsp:include page="/categoryBanners/beautyBrandDiscount.jsp"/>--%>
    <%--</c:if>--%>


    <c:if test="${hk:collectionContains(categoryNameList, 'clinical-supplies')}">
        <jsp:include page="/categoryBanners/stethescopeStripBanner.jsp"/>
    </c:if>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'eye')}">
        <jsp:include page="/categoryBanners/eyeCallback.jsp"/>
    </c:if>

    <%--<c:if test="${hk:collectionContains(categoryNameList, 'fitness-equipment')}">--%>
    <%--<jsp:include page="/categoryBanners/sportsDiscount.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'treadmills')}">--%>
    <%--<jsp:include page="/categoryBanners/treadmillsCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'crosstrainers')}">--%>
    <%--<jsp:include page="/categoryBanners/crosstrainersCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'exercise-bike')}">--%>
    <%--<jsp:include page="/categoryBanners/exerciseBikeCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'rowers')}">--%>
    <%--<jsp:include page="/categoryBanners/rowersCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'home-gym')}">--%>
    <%--<jsp:include page="/categoryBanners/homeGymCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'bench-equipments')}">--%>
    <%--<jsp:include page="/categoryBanners/benchEquipmentsCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'heart-rate-monitors')}">--%>
    <%--<jsp:include page="/categoryBanners/heartRateMonitorsCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'miscellaneous-equipments')}">--%>
    <%--<jsp:include page="/categoryBanners/miscellaneousEquipmentsCallback.jsp"/>--%>
    <%--</c:if>--%>
    <%--<c:if test="${hk:collectionContains(categoryNameList, 'tt-tables')}">--%>
    <%--<jsp:include page="/categoryBanners/ttTablesCallback.jsp"/>--%>
    <%--</c:if>--%>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'health-nutrition')}">
        <%--<jsp:include page="/categoryBanners/nutriTenDiscount.jsp"/>--%>
        <jsp:include page="/categoryBanners/healthNutriCallback.jsp"/>
        <%--<jsp:include page="/categoryBanners/fitnessAccessoriesCashback.jsp"/>--%>
    </c:if>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'sports-nutrition')}">
        <%--<jsp:include page="/categoryBanners/nutriTenDiscount.jsp"/>--%>
        <%--<jsp:include page="/categoryBanners/nutriCallback.jsp"/>--%>
        <%--<jsp:include page="/categoryBanners/fitnessAccessoriesCashback.jsp"/>--%>
        <jsp:include page="/categoryBanners/nutriCallback.jsp"/>
    </c:if>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'services')}">
        <%--<jsp:include page="/categoryBanners/servicesDiscount.jsp"/>--%>
    </c:if>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'sports')}">
        <%--<jsp:include page="/categoryBanners/holiBanner.jsp"/>--%>
        <jsp:include page="/categoryBanners/sportsDiscount.jsp"/>
    </c:if>
    <c:if test="${hk:collectionContains(topCategoryNameList, 'home-devices')}">
        <%--<jsp:include page="/categoryBanners/hhdStripBanner.jsp"/>--%>
        <%--<jsp:include page="/categoryBanners/sportsDiscount.jsp"/>--%>
    </c:if>
    <c:if test="${hk:collectionContains(topCategoryNameList, 'personal-care')}">
        <jsp:include page="/categoryBanners/personalCareStripBanner.jsp"/>
    </c:if>
    <c:if test="${hk:collectionContains(topCategoryNameList, 'diabetes')}">
        <%--<jsp:include page="/categoryBanners/diabetesStripBanner.jsp"/>--%>
        <%--<jsp:include page="/categoryBanners/sportsDiscount.jsp"/>--%>
    </c:if>
    <c:if test="${hk:collectionContains(topCategoryNameList, 'parenting')}">
        <%--<jsp:include page="/categoryBanners/holiBanner.jsp"/>--%>
        <%--<jsp:include page="/categoryBanners/sportsDiscount.jsp"/>--%>
       <%-- <jsp:include page="/categoryBanners/parentingDiscount.jsp"/>--%>
    </c:if>

    <%--<c:if test="${hk:collectionContains(categoryNameList, 'Soulflower')}">--%>
    <%--<jsp:include page="/categoryBanners/beautyBrandDiscount.jsp"/>--%>
    <%--</c:if>--%>

    <c:if test="${hk:collectionContains(topCategoryNameList, 'personal-care')}">
        <%--<jsp:include page="/categoryBanners/personalCareTopBanner.jsp"/>--%>
    </c:if>

    <%--<c:if test="${hk:collectionContains(brandList, 'Soulflower')}">--%>
    <%--<jsp:include page="/categoryBanners/beautyBrandDiscount.jsp"/>--%>
    <%--</c:if>--%>

    <c:if test="${hk:collectionContains(categoryNameList, 'fitness-accessories')}">
    </c:if>

</s:layout-definition>
