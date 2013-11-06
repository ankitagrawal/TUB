<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.marketing.EnumProductReferrer" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.HomeAction" var="homeBean" event="pre"/>

<%
	boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
  UserService userService = ServiceLocatorFactory.getService(UserService.class);
  User loggedInUser = userService.getLoggedInUser();
  if (loggedInUser != null) {
    pageContext.setAttribute("userRoles", loggedInUser.getRoleStrings());
  }
  pageContext.setAttribute("role_god", RoleConstants.GOD);
%>

<s:layout-render name="/layouts/genericG.jsp"
                 pageTitle="HealthKart.com: Buy Nutrition, Health Care, Beauty & Personal Care Products Online in India"
                 topCategory="home"
                 allCategories="home"  showNewHKLink ="false"
    >

<s:layout-component name="htmlHead">
  <meta name="description"
        content="Online Shopping for Nutrition, Health, Beauty & Personal Care Products in India: Buy Nutrition Supplements, Health Equipments, Diabetes supplies, Lenses, Home Devices & Other Products online at Lowest Price & Free Shipping in India – Healthkart.com"/>
  <meta name="keywords"
        content="Online Shopping, online shopping india, nutrition, healthcare products, buy health care health equipments, beauty care products, shop online, nutrition supplements, protein supplements, diabetes, skin care, eye care,  healthcart, healthkkart, healthkarts, price, india"/>

  <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
  <%--<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.responsiveslides.min.js"></script>--%>
</s:layout-component>

<s:layout-component name="homePageTopContent">
  <div style="visibility:hidden;">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico?v=2"/>
  </div>
  <div id="home_header02">
    <h1>the one stop shop for health, fitness and beauty</h1>

    <div class="banner01">
      <ul class="slides ulStyle">
        <c:forEach var="image" items="${homeBean.categoryImages}">
          <c:choose>
            <c:when test="${hk:isNotBlank(image.link) && fn:toLowerCase(image.position) eq('left')}">
              <li>
                <a href="${pageContext.request.contextPath}${image.link}">
                  <hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/>
                </a>
              </li>
            </c:when>

            <c:when test="${fn:toLowerCase(image.position) eq('left')}">
              <li><hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/></li>
            </c:when>
          </c:choose>
        </c:forEach>
      </ul>
    </div>

    <div class="banner01">
      <ul class="slides ulStyle">
        <c:forEach var="image" items="${homeBean.categoryImages}">
          <c:choose>
            <c:when test="${hk:isNotBlank(image.link) && fn:toLowerCase(image.position) eq('center')}">
              <li>
                <a href="${pageContext.request.contextPath}${image.link}">
                  <hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/>
                </a>
              </li>
            </c:when>

            <c:when test="${fn:toLowerCase(image.position) eq('center')}">
              <li><hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/></li>
            </c:when>
          </c:choose>
        </c:forEach>
      </ul>
    </div>

    <div class="banner01" style="margin-right: 0">
      <ul class="slides ulStyle">
        <c:forEach var="image" items="${homeBean.categoryImages}">
          <c:choose>
            <c:when test="${hk:isNotBlank(image.link) && fn:toLowerCase(image.position) eq('right')}">
              <li>
                <a href="${pageContext.request.contextPath}${image.link}">
                  <hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/>
                </a>
              </li>
            </c:when>
            <c:when test="${fn:toLowerCase(image.position) eq('right')}">
              <li><hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/></li>
            </c:when>
          </c:choose>
        </c:forEach>
      </ul>
    </div>
    <script>
      $(function () {
        $(".slides").responsiveSlides({speed: 6000});
      });
    </script>
  </div>
</s:layout-component>


<s:layout-component name="content">
  <div class="container_24">
    <c:if test="${hk:collectionContains(userRoles, role_god)}">
      <div class="grid_24 alpha omega">
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank"
                class="popup"> Upload
          <s:param name="category" value="${homeBean.category.name}"/>
        </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages"
                target="_blank"
                class="popup">
          <s:param name="category" value="${homeBean.category.name}"/>
          Manage Images
        </s:link>
      </div>


      <div class="grid_24 alpha omega">
        <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="editPrimaryCategoryHeadings"
                class="popup" style="font-size:larger; background-color:#003399; color:white;" target="_blank">
          Add/Edit Headings for home
          <s:param name="category" value="${homeBean.category.name}"/>
        </s:link>
      </div>


      <div class="clear"></div>
    </c:if>


    <c:forEach var="heading" items="${homeBean.headingsWithRankingSetSortedByRanking}">
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


        <c:if test="${hk:collectionContains(userRoles, role_god)}">
          <div class="grid_24 alpha omega" style="width: 950px;">
            <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                    event="editPrimaryCategoryHeadingProducts"
                    class="popup" style="background-color:#003399; color:white;" target="_blank">
              Add/Edit Products for Heading
              <s:param name="heading.id" value='${heading.id}'/>
            </s:link>
          </div>


          <div class="clear"></div>
        </c:if>


        <div class="grid_24" style="width: 950px;">
          <c:forEach var="headingProduct" items='${hk:getHeadingProductsSortedByRank(heading.id)}' begin="0" end="5">
            <div class="grid_4 alpha omega">
              <s:layout-render name="/layouts/embed/_productVOThumbG.jsp" productId='${headingProduct.productId}' productReferrerId="<%=EnumProductReferrer.homePage.getId()%>"/>
            </div>
          </c:forEach>
        </div>
        <div class="clear"></div>
      </c:if>


      <c:if test="${hk:collectionContains(userRoles, role_god)}">
        <c:if test="${empty hk:getHeadingProductsSortedByRank(heading.id)}">
          <div class="grid_24 alpha omega" style="width: 950px;">
            <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                    event="addPrimaryCategoryHeadingProducts"
                    class="popup" style="background-color:#003399; color:white;">
              <strong>ADD PRODUCTS FOR HEADING: ${heading.name} (Heading not visible as no products are set for
                it)</strong>
              <s:param name="heading.id" value='${heading.id}'/>
            </s:link>
          </div>

        </c:if>


        <div class="clear"></div>
      </c:if>


    </c:forEach>
  </div>


  <div class="clear"></div>
	<%--<!--google remarketing code-->--%>
	<s:layout-render name="/layouts/embed/remarketingWithCustomParams.jsp" pageType="<%=HealthkartConstants.Remarketing.PageType.home%>"/>

		<c:if test="${not isSecure }">
			<iframe
				src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=1&uid=${user_hash}"
				scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
				frameborder="0"></iframe>
		</c:if>

	</s:layout-component>
</s:layout-render>

<script type="text/javascript">
  $(document).ready(function() {
    $('#home_button').addClass("active");


  });
</script>
<style type="text/css">
  .ulStyle {
    list-style: none outside none;
    margin: 0 0 1.5em 0;
    overflow: hidden;
    padding: 0;
    position: relative;
    width: 100%;
  }
</style>










