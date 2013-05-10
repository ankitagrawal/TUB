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
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <%
        if (pageContext.getAttribute("pageType") != null) {
            String pageType = (String) pageContext.getAttribute("pageType");
            pageContext.setAttribute("pageType", pageType);
        }

        if (pageContext.getAttribute("topLevelCategory") != null) {
            String topLevelCategory = (String) pageContext.getAttribute("topLevelCategory");
            pageContext.setAttribute("topLevelCategory", topLevelCategory);
        }
    %>
    <input type="hidden" value="${pageType}" id="pageType">
    <input type="hidden" value="${topLevelCategory}" id="topLevelCategory">
    <%
        if (AnalyticsConstants.analytics) {
    %>
        <c:if test = "${pageType == 'category' ||  pageType == 'product'}">
            <c:if test = "${(topLevelCategory == 'sports-nutrition')
            || (topLevelCategory == 'diabetes') || (topLevelCategory == 'health-nutrition')
            || (topLevelCategory == 'health-devices') || (topLevelCategory == 'personal-care')
            || (topLevelCategory == 'home-living') || (topLevelCategory == 'sports')
            || (topLevelCategory == 'eye') ||  (topLevelCategory == 'parenting')}">
                <!-- Advertiser 'BRIGHT LIFECARE PVT LTD',  Include user in segment 'BRIGHT LIFECARE PVT LTD_SRT_PIXEL_040213' - DO NOT MODIFY THIS PIXEL IN ANY WAY -->
                <!--<img src="https://ad.yieldmanager.com/pixel?id=2354094&t=2" width="1" height="1" />-->
                <!-- End of segment tag -->
                
                <!-- Advertiser 'Aquamarine Healthcare Pvt. Ltd.',  Include user in segment '1532137_Aquamarine_SegPixel_041513' - DO NOT MODIFY THIS PIXEL IN ANY WAY -->
					<img src="https://ad.yieldmanager.com/pixel?id=2358977&t=2" width="1" height="1" />
				<!-- End of segment tag -->
            </c:if>
        </c:if>

        <c:if test = "${pageType == 'purchase'}">
            <!-- Yahoo conversion tracking code -->
            <!-- Advertiser 'BRIGHT LIFECARE PVT LTD',  Conversion tracking 'BRIGHT LIFECARE PVT LTD_CONVERSION_PIXEL_040213' - DO NOT MODIFY THIS PIXEL IN ANY WAY -->
            <!--<img  src="https://ad.yieldmanager.com/pixel?id=2354092&t=2" width="1" height="1" />-->
            <img src="https://ad.yieldmanager.com/pixel?id=2358976&t=2" width="1" height="1" />
            <!-- End of conversion tag -->
        </c:if>
    <%
        }
    %>
    <%--</c:otherwise>--%>
</s:layout-definition>
