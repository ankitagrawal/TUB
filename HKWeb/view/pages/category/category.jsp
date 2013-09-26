<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.catalog.category.CategoryConstants" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.marketing.EnumProductReferrer" %>
<%@ page import="com.hk.domain.MapIndia" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.dao.location.MapIndiaDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="java.util.List" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="java.util.Date" %>
<%@ include file="/layouts/_userData.jsp" %>
<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CategoryAction" var="categoryBean" event="pre"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="${categoryBean.seoData.title}">
<%
    DateTime dateTime = new DateTime();
    Date startOfOfferDate = new Date(new DateTime(2013, 9, 17, 00, 00, 00, 00).getMillis());
    Date endOfOfferDate = new Date(new DateTime(2013, 9, 24, 23, 59, 59, 00).getMillis());
%>
<c:if test="${categoryBean.category.name == 'services'}">
    <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment=""/>
    <%
        CategoryDao categoryDao = (CategoryDao) ServiceLocatorFactory.getService(CategoryDao.class);
        Category services = categoryDao.getCategoryByName("services");
        pageContext.setAttribute("services", services);

        boolean isSecure = WebContext.isSecure();
        pageContext.setAttribute("isSecure", isSecure);

        MapIndiaDao mapIndiaDao = (MapIndiaDao) ServiceLocatorFactory.getService(MapIndiaDao.class);
        Cookie preferredZoneCookie = BaseUtils.getCookie(WebContext.getRequest(), HealthkartConstants.Cookie.preferredZone);
        if (preferredZoneCookie != null && preferredZoneCookie.getValue() != null) {
            MapIndia mapIndia = mapIndiaDao.findByCity(preferredZoneCookie.getValue());
            List<MapIndia> targetCitiesList = mapIndiaDao.getTargetCitiesList();
            pageContext.setAttribute("targetCitiesList", preferredZoneCookie.getValue());
            if (preferredZoneCookie.getValue().equals("All") || (mapIndia != null && mapIndia.isTargetCity() != null && mapIndia.isTargetCity())) {
                pageContext.setAttribute("preferredZone", preferredZoneCookie.getValue());
            }
        }
    %>
</c:if>

<s:layout-component name="htmlHead">
    <%--<!-- YAHOO marketing -->--%>
    <%--<s:layout-render name="/layouts/embed/_yahooMarketing.jsp" pageType="category" topLevelCategory="${categoryBean.category.name}"/>--%>
    <%--<s:layout-render name="/layouts/embed/_ozoneMarketing.jsp" pageType="category" topLevelCategory="${categoryBean.category.name}"/>--%>
    <c:if test="${categoryBean.category.name == 'services'}">
        <script type="text/javascript">
            $(document).ready(function () {
                $('#selectCityWindow').jqm();
                $('#selectCityWindow').jqmShow();

                $('.jqmOverlay').click(function () {
                    //        alert("Cliking w/o selecting" + $('#topLevelCategory').val());
                    if ($('#topLevelCategory').val() == 'services') {
                        //          alert("Inside");
                        $.getJSON(
                                $('#setDefaultZoneLink').attr('href')
                        );
                    }
                });
            });
        </script>
    </c:if>
</s:layout-component>

<s:layout-component name="breadcrumbs">

    <div class='crumb_outer'>
        <a href="${pageContext.request.contextPath}/" class="crumb">Home</a>
        &gt;
        <span class="crumb last" style="font-size: 12px;">${categoryBean.category.displayName}</span>

        <h1 class="title"> ${categoryBean.seoData.h1} </h1>

        <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
            <div align="center">
                <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank"
                        class="popup">Edit MetaData
                    <s:param name="seoData" value="${categoryBean.seoData.id}"/>
                </s:link>
            </div>

        </shiro:hasPermission>

    </div>
</s:layout-component>

<c:if test="${preferredZone == null && categoryBean.category.name == 'services'}">
    <s:layout-component name="modal">
        <div class="jqmWindow" style="width:700px" id="selectCityWindow">
            <s:layout-render name="/layouts/modal.jsp">
                <s:layout-component name="heading">Select City</s:layout-component>
                <s:layout-component name="content">
                    <s:form beanclass="com.hk.web.action.core.catalog.category.ServiceAction">
                        <s:errors/>
                        <div class="round-cont" style="width:650px;margin-top: 20px;">
                            <label>To find the relevant deals, Please select your city</label>
                            <s:select name="preferredZone">
                                <s:option value="Delhi">Delhi-NCR</s:option>
                                <s:option value="Mumbai">Mumbai</s:option>
                                <s:option value="Chennai">Chennai</s:option>
                                <s:option value="Kolkatta">Calcutta</s:option>
                                <s:option value="Bangalore">Bangalore</s:option>
                                <s:option value="Chandigarh">Chandigarh</s:option>
                                <s:option value="Hyderabad">Hyderabad</s:option>
                                <s:option value="Jaipur">Jaipur</s:option>
                                <s:option value="Calcutta">Kolkata</s:option>
                                <s:option value="Pune">Pune</s:option>
                                <s:option value="Ahmedabad">Ahmedabad</s:option>
                                <s:option value="All">Rest Of India</s:option>
                            </s:select>
                        </div>
                        <s:submit name="setCookie" value="Select"/>
                    </s:form>
                </s:layout-component>
            </s:layout-render>
        </div>
    </s:layout-component>
</c:if>

<s:layout-component name="topCategory">${categoryBean.category.name}</s:layout-component>

<s:layout-component
        name="metaDescription"> ${categoryBean.seoData.metaDescription} </s:layout-component>
<s:layout-component
        name="metaKeywords">${categoryBean.seoData.metaKeyword}</s:layout-component>


<s:layout-component name="mainBanner">
    <div class="promotional">
        <ul id="categoryBanner" class="grid_18">
            <c:forEach var="image" items="${categoryBean.categoryImages}">
                <c:choose>
                    <c:when test="${hk:isNotBlank(image.link)}">
                        <li>
                            <a href="${pageContext.request.contextPath}${image.link}">
                                <hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/>
                            </a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li><hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/></li>
                    </c:otherwise>

                </c:choose>
            </c:forEach>
        </ul>
    </div>
    <div class='grid_6'>
        <c:set var="sportsNutrition" value="<%=CategoryConstants.SPORTS_NUTRITION%>"/>
        <c:set var="healthNutrition" value="<%=CategoryConstants.HEALTH_NUTRITION%>"/>
        <c:set var="homeLiving" value="<%=CategoryConstants.HOME_LIVING%>"/>
        <c:set var="personalCare" value="<%=CategoryConstants.PERSONAL_CARE%>"/>
        <c:set var="healthDevices" value="<%=CategoryConstants.HEALTH_DEVICES%>"/>
        <c:set var="eye" value="<%=CategoryConstants.EYE%>"/>
        <c:set var="parenting" value="<%=CategoryConstants.BABY%>"/>
        <c:set var="beauty" value="<%=CategoryConstants.BEAUTY%>"/>
        <c:set var="diabetes" value="<%=CategoryConstants.DIABETES%>"/>
        <c:choose>
            <c:when test="${categoryBean.category.name eq sportsNutrition}">
                <%-- <a href="${pageContext.request.contextPath}/pages/offers/sports-nutrition/offers.jsp">--%>
                <a class="bulkOrder" href="#" style="cursor: pointer;">
                    <img src="${pageContext.request.contextPath}/images/banners/nutrition_bulk_order.jpg"
                         alt="Bulk Order above 25000/-" class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/pages/campaign/clearance/sale.jsp">
                    <img src="${pageContext.request.contextPath}/images/banners/Clearance-sale-sports.jpg"
                         alt="Twinlab Sale"
                         class="small_banner"/>
                </a>
              <script type="text/javascript">
                  $(".bulkOrder").live('click', function () {
                  $('html, body').animate({scrollTop: $("#bulkOrderModal").offset().top - 50}, 1000);
                      $('#bulkOrderModal').jqm({trigger: '.bulkOrder'});
                      $("#bulkOrderModal").append($('<iframe id="raj_frame" class="bulkModalFrame" src="https://docs.google.com/a/healthkart.com/spreadsheet/viewform?formkey=dDdiX1pReU8zSXk4Y1pqcVMxQU43bnc6MQ#gid=0" width="760" height="760" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>'));
                  });
              </script>
            </c:when>
            <c:when test="${categoryBean.category.name eq healthNutrition}">
                <a class="dietPlan" href="#" style="cursor: pointer;">
                                    <img src="${pageContext.request.contextPath}/images/banners/GET-FREE-DIET-PLAN.jpg"
                                         alt="DIET_PLAN" class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/pages/offers/top-nutrition-brands/top-nut-brands.jsp">
                    <img src="${pageContext.request.contextPath}/images/banners/finalHNbanner.jpg"
                         alt="Vitamin Shoppe"
                         class="small_banner"/>
                </a>
                <script type="text/javascript">
                    $(".dietPlan").live('click', function () {
                        $('html, body').animate({scrollTop: $("#bulkOrderModal").offset().top - 50}, 1000);
                        $('#bulkOrderModal').jqm({trigger: '.dietPlan'});
                        $("#bulkOrderModal").append($('<iframe id="raj_frame" class="bulkModalFrame" src="https://docs.google.com/a/healthkart.com/forms/d/1phY4o8X4jlgx3Td5ijRgV5CYhcMFdqJqaoMdndVrCZ4/viewform#gid=0" width="760" height="760" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>'));
                    });
                </script>
            </c:when>
            <c:when test="${categoryBean.category.name eq homeLiving}">
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <img src="<hk:vhostImage/>/images/banners/free-shipping-500.jpg" alt="Free shipping and COD"
                     class="small_banner"/>
            </c:when>

            <c:when test="${categoryBean.category.name eq personalCare}">
                <a href="${pageContext.request.contextPath}/personal-care/pain-relief/supports?brand=HealthViva">
                    <img src="<hk:vhostImage/>/images/banners/healthviva-small-static-banner.jpg" alt="healthviva"
                         class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/personal-care/women/self-defense">
                    <img src="<hk:vhostImage/>/images/banners/Self-Defense1.jpg" alt="Sexual-wellness`"
                         class="small_banner"/>
                </a>
            </c:when>
            <c:when test="${categoryBean.category.name eq healthDevices}">
                <a href="${pageContext.request.contextPath}/brand/health-devices/Ginni?camp=ginni">
                    <img src="<hk:vhostImage/>/images/banners/ginni-banner.jpg" alt="self-defence"
                         class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7203/HB005?camp=7200">
                    <img src="<hk:vhostImage/>/images/banners/product-of-the-week-omron-BP2.jpg" alt="Omron"
                         class="small_banner"/>
                </a>
            </c:when>

            <c:when test="${categoryBean.category.name eq eye}">
                <%
                    if (dateTime.isAfter(startOfOfferDate.getTime()) && dateTime.isBefore(endOfOfferDate.getTime())) {
                %>
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>

                <a href="${pageContext.request.contextPath}/eye/eyeglasses?filterOptions[0]=13619&minPrice=153&maxPrice=2146 ">
                    <img src="<hk:vhostImage/>/images/banners/SunGlassbanner.jpg" alt="eye"
                         class="small_banner"/>
                </a>
                <%
                } else { %>
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <img src="<hk:vhostImage/>/images/banners/free-shipping-500.jpg" alt="Free shipping and COD"
                     class="small_banner"/>
                <%
                    }
                %>

            </c:when>
            <c:when test="${categoryBean.category.name eq parenting}">
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <img src="<hk:vhostImage/>/images/banners/free-shipping-500.jpg" alt="Free shipping and COD"
                                     class="small_banner"/>
            </c:when>

            <c:when test="${categoryBean.category.name eq beauty}">
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/brand/beauty/Nurture">
                    <img src="<hk:vhostImage/>/images/banners/Nurture--whole-range.jpg" alt="beauty"
                         class="small_banner"/>
                </a>

            </c:when>
            <c:when test="${categoryBean.category.name eq diabetes}">
                <a href="${pageContext.request.contextPath}/product/contour-super-saver-pack/DM019?ContourPack14Aug">
                    <img src="<hk:vhostImage/>/images/banners/Contour-super-saver-pack.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <a href="${pageContext.request.contextPath}/product/quanto-diab/HNUT46?camp=qunat">
                    <img src="<hk:vhostImage/>/images/banners/quanto-diab.jpg" alt="diabetes"
                         class="small_banner"/>
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/pages/returnAndCancellations.jsp">
                    <img src="<hk:vhostImage/>/images/banners/14-days-return.jpg" alt="14 Days Return Policy"
                         class="small_banner"/>
                </a>
                <img src="<hk:vhostImage/>/images/banners/free-shipping-500.jpg" alt="Free shipping and COD"
                     class="small_banner"/>
            </c:otherwise>
        </c:choose>
    </div>

</s:layout-component>
<s:layout-component name="content">
    <div class="container_24">
        <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
            <div class="grid_24 alpha omega">
                <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre"
                        target="_blank" class="popup"> Upload
                    <s:param name="category" value="${categoryBean.category.name}"/>
                </s:link>
                &nbsp;|&nbsp;
                <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction"
                        event="manageCategoryImages" target="_blank"
                        class="popup">
                    <s:param name="category" value="${categoryBean.category.name}"/>
                    Manage Images
                </s:link>
            </div>

            <div class="clear"></div>
            <%--</shiro:hasPermission>--%>

            <%--<shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">--%>
            <div class="grid_24 alpha omega">
                <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction"
                        event="editPrimaryCategoryHeadings"
                        class="popup" style="font-size:larger; background-color:#003399; color:white;" target="_blank">
                    Add/Edit Headings for ${categoryBean.category.name} category
                    <s:param name="category" value="${categoryBean.category.name}"/>
                </s:link>
            </div>

            <div class="clear"></div>
        </shiro:hasPermission>

        <c:forEach var="heading" items="${categoryBean.headingsWithRankingSetSortedByRanking}">
            <c:if test="${!empty hk:getHeadingProductsSortedByRank(heading.id)}">
                <div class="grid_24 alpha omega" style="width: 950px;">
                    <c:choose>
                        <c:when test="${hk:isNotBlank(heading.link)}">
                            <div class="products" style="margin-left: 0;">
                                <h2>
                                    <a href="${pageContext.request.contextPath}${heading.link}">
                                            ${heading.name}<span class='small'>   (view more products)</span>
                                    </a>
                                </h2>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="products"
                                 style="margin-left: 0;color: #228; font-size: 1.5em; border-bottom: 1px solid #228;">
                                <h2>
                                    <strong>
                                            ${heading.name}
                                    </strong>
                                </h2>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="clear"></div>

                <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
                    <div class="grid_24 alpha omega" style="width: 950px;">
                        <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                                event="editPrimaryCategoryHeadingProducts"
                                class="popup" style="background-color:#003399; color:white;" target="_blank">
                            Add/Edit Products for Heading
                            <s:param name="heading.id" value='${heading.id}'/>
                        </s:link>
                    </div>

                    <div class="clear"></div>
                </shiro:hasPermission>

                <div class="grid_24" style="width: 950px;">
                    <c:forEach var="headingProduct" items='${hk:getHeadingProductsSortedByRank(heading.id)}' begin="0"
                               end="5">
                        <div class="grid_4 alpha omega">
                            <s:layout-render name="/layouts/embed/_productVOThumbG.jsp"
                                             product='${headingProduct.product}'
                                             productReferrerId="<%=EnumProductReferrer.categoryHomePage.getId()%>"/>
                        </div>
                    </c:forEach>
                </div>
                <div class="clear"></div>
                <%--<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>--%>
            </c:if>

            <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
                <c:if test="${empty hk:getHeadingProductsSortedByRank(heading.id)}">
                    <div class="grid_24 alpha omega" style="width: 950px;">
                        <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                                event="addPrimaryCategoryHeadingProducts"
                                class="popup" style="background-color:#003399; color:white;">
                            <strong>ADD PRODUCTS FOR HEADING: ${heading.name} (Heading not visible as no products are
                                set for
                                it)</strong>
                            <s:param name="heading.id" value='${heading.id}'/>
                        </s:link>
                    </div>
                </c:if>

                <div class="clear"></div>
            </shiro:hasPermission>

        </c:forEach>
    </div>

    <div class="clear"></div>

    <c:if test="${hk:isNotBlank(categoryBean.seoData.description)}">
        <div style="margin-top: 45px; background-color: #FAFCFE; padding: 10px; float: none; clear: both;">
            <h2><i>${categoryBean.seoData.descriptionTitle}</i>: </h2>
                ${categoryBean.seoData.description}
        </div>
    </c:if>

    <%--<!--google remarketing page type-->--%>
    <s:layout-render
        name="/layouts/embed/remarketingWithCustomParams.jsp"
        pageType="<%=HealthkartConstants.Remarketing.PageType.category%>"
        primaryCategory="${categoryBean.category.name}"
        />

    <c:choose>
        <c:when test="${not isSecure}">
        </c:when>
        <c:otherwise>
            <iframe
                    src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e200&catid=${categoryBean.category.name}&subcat1id=&subcat2id=&section=1&level=1&uid=${user_hash}"
                    scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
                    frameborder="0"></iframe>
        </c:otherwise>
    </c:choose>
    <%--<c:if test="${not isSecure }">
        <iframe
            src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e200&catid=${categoryBean.category.name}&subcat1id=&subcat2id=&section=1&level=1"
            scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
            frameborder="0"></iframe>
    </c:if>--%>
</s:layout-component>
</s:layout-render>
