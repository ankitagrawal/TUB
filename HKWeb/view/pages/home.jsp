<%@ page import="com.hk.constants.EnumImageSize" %>
<%@ page import="com.hk.constants.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.HomeAction" var="homeBean" event="pre"/>
<s:layout-render name="/layouts/genericG.jsp"
                 pageTitle="HealthKart.com: Buy Nutrition, Health Care, Beauty & Personal Care Products Online in India">

  <s:layout-component name="htmlHead">
    <meta name="description"
          content="Online Shopping for Nutrition, Health, Beauty & Personal Care Products in India: Buy Nutrition Supplements, Health Equipments, Diabetes supplies, Lenses, Home Devices & Other Products online at Lowest Price & Free Shipping in India – Healthkart.com"/>
    <meta name="keywords"
          content="Online Shopping, online shopping india, nutrition, healthcare products, buy health care health equipments, beauty care products, shop online, nutrition supplements, protein supplements, diabetes, skin care, eye care,  healthcart, healthkkart, healthkarts, price, india"/>

    <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.responsiveslides.min.js"></script>
  </s:layout-component>

 

 <%-- <s:layout-component name="topBanner">
    <jsp:include page="/categoryBanners/holiBanner.jsp"/>
  </s:layout-component>--%>

  <s:layout-component name="homePageTopContent">
    <div style="visibility:hidden;"><link rel="shortcut icon" href="http://www.healthkart.com/favicon.ico?v=2" /></div>
    <div id="home_header02">
    <h1>the one stop shop for health, fitness and beauty</h1>

    <%--<a href="http://www.healthkart.com/services/health-checkups"><img class="banner01" src="<hk:vhostImage/>/images/banners/home/health_checkup_sbanner.jpg" border="0" alt="Health Checkups"/></a>--%>
    <a href="http://www.healthkart.com/product/onetouch-ultra-2-meter-w-free-omron-hem-7111-b.p.-monitor/CMB-DIA013"><img class="banner01" src="<hk:vhostImage/>/images/banners/home/accuchek_sbanner.jpg" border="0" alt="Accu-check offer"/></a>


    <a href="http://www.healthkart.com/sports/apparel?brand=STAG"><img class="banner01" src="<hk:vhostImage/>/images/banners/home/stag_sbanner.jpg" border="0" alt="Stag brand"/></a>


    <%--<a href="http://www.healthkart.com/sports/fitness-accessories/support-gear/hand-wrist"><img class="banner01" src="<hk:vhostImage/>/images/banners/home/gloves_sbanner.jpg" border="0" alt="Training Gloves : Harbinger, Adidas"/></a>--%>

    <%--<a href="http://www.healthkart.com/product/pediasure/BAB152"><img class="banner01" style="margin-right:0" src="<hk:vhostImage/>/images/banners/home/pediasure_banner.jpg" border="0" alt="Pediasure"/></a>--%>
      <div class="banner01" style="margin-right: 0">
       <%-- <ul id="slides">
          <li>--%>
            <a href="http://www.healthkart.com/nutrition/sports-nutrition?brand=Dymatize">
              <img src="<hk:vhostImage/>/images/banners/home/dymatize_sbanner.jpg" border="0" alt="Dymatize - free shaker"/>
            </a>
         <%-- </li>--%>
          <%--<li>
            <a href="http://www.healthkart.com/product/pediasure/BAB152">
              <img src="<hk:vhostImage/>/images/banners/home/pediasure_banner.jpg" border="0" alt="Pediasure"/>
            </a>
          </li>--%>
     <%-- </ul>--%>
      </div>
      <script>
        $(function () {
          //$("#slides").responsiveSlides();
        });
      </script>

    <img class="strip_banner01" src="<hk:vhostImage/>/images/banners/home/main_strip01.png" border="0" />
    <jsp:include page="/includes/_testimonials.jsp"/>
    </div>
  </s:layout-component>


  <s:layout-component name="content">
    <div class="container_24">
      <shiro:hasRole name="<%=RoleConstants.GOD%>">
        <%--<div class="grid_24 alpha omega">--%>
          <%--<s:link beanclass="web.action.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload--%>
            <%--<s:param name="category" value="${homeBean.category.name}"/>--%>
          <%--</s:link>--%>
          <%--&nbsp;|&nbsp;--%>
          <%--<s:link beanclass="web.action.UploadCategoryImageAction" event="manageCategoryImages" target="_blank"--%>
                  <%--class="popup">--%>
            <%--<s:param name="category" value="${homeBean.category.name}"/>--%>
            <%--Manage Images--%>
          <%--</s:link>--%>
        <%--</div>--%>

        <%--<div class="clear"></div>--%>

        <div class="grid_24 alpha omega">
          <s:link beanclass="web.action.CategoryAction" event="editPrimaryCategoryHeadings"
                  class="popup" style="font-size:larger; background-color:#003399; color:white;" target="_blank">
            Add/Edit Headings for home
            <s:param name="category" value="${homeBean.category.name}"/>
          </s:link>
        </div>

        <div class="clear"></div>
      </shiro:hasRole>

      <c:forEach var="heading" items="${homeBean.headingsWithRankingSetSortedByRanking}">
        <c:if test="${!empty heading.products}">
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

          <shiro:hasRole name="<%=RoleConstants.GOD%>">
            <div class="grid_24 alpha omega" style="width: 950px;">
              <s:link beanclass="web.action.PrimaryCategoryHeadingAction"
                      event="editPrimaryCategoryHeadingProducts"
                      class="popup" style="background-color:#003399; color:white;" target="_blank">
                Add/Edit Products for Heading
                <s:param name="heading.id" value='${heading.id}'/>
              </s:link>
            </div>

            <div class="clear"></div>
          </shiro:hasRole>

          <div class="grid_24" style="width: 950px;">
            <c:forEach var="product" items='${heading.productSortedByOrderRanking}'>
              <div class="grid_4 alpha omega">
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='${product.id}'/>
              </div>
            </c:forEach>
          </div>
          <div class="clear"></div>
        </c:if>

        <shiro:hasRole name="<%=RoleConstants.GOD%>">
          <c:if test="${empty heading.products}">
            <div class="grid_24 alpha omega" style="width: 950px;">
              <s:link beanclass="web.action.PrimaryCategoryHeadingAction"
                      event="addPrimaryCategoryHeadingProducts"
                      class="popup" style="background-color:#003399; color:white;">
                <strong>ADD PRODUCTS FOR HEADING: ${heading.name} (Heading not visible as no products are set for
                  it)</strong>
                <s:param name="heading.id" value='${heading.id}'/>
              </s:link>
            </div>
          </c:if>

          <div class="clear"></div>
        </shiro:hasRole>

      </c:forEach>
    </div>

    <div class="clear"></div>

    <iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=1"
            scrolling="no"
            width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  $(document).ready(function() {
    $('#home_button').addClass("active");
  });
</script>
