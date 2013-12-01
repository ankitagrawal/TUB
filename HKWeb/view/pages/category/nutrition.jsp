<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Supplements Store - Buy Optimum, Ultimate, Muscletech & Other branded nutrition products online in India">

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Nutrition</span>

    <h1 class="title">
      Optimum, Ultimate Nutrition Supplements Store in India
    </h1>
  </div>
</s:layout-component>

<s:layout-component name="metaDescription">Authentic supplements for body building, weight loss, weight gain and wellness. Contact us now for free nutrition counselling.</s:layout-component>
<s:layout-component name="metaKeywords">online supplement store, body building supplements, weight loss supplements, weight gainers, multivitamins, sports nutrition, whey protein, creatine</s:layout-component>

<s:layout-component name="mainBanner">
  <div class="promotional">
  <ul id="categoryBanner" class="grid_18" >

    <li>
      <div>
        <a href="${pageContext.request.contextPath}/brand/nutrition/EAS">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_sports_eas_isopure.jpg" alt="Sports nutrition EAS, Isopure"/>
        </a>
      </div>
    </li>
    <%--<li>--%>
      <%--<div>--%>
        <%--<a href="${pageContext.request.contextPath}/nutrition/fitness-accessories">--%>
          <%--<img src="<hk:vhostImage/>/images/banners/Nutrition/banner_gym_acc_02.jpg" alt="Fitness Accessories"/>--%>
        <%--</a>--%>
      <%--</div>--%>
    <%--</li>--%>
    <li>
      <div>
        <a href="${pageContext.request.contextPath}/brand/nutrition/Ultimate+Nutrition">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_ultimate_nutrition_offer.jpg" alt="Ultimate Nutrition"/>
        </a>
      </div>
    </li>
    <li>
      <div>
        <a href="${pageContext.request.contextPath}/nutrition/specialty-nutrition/gluten-free-food">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_sports_specialty_food.jpg" alt="Gluten Free Food"/>
        </a>
      </div>
    </li>
    <li>
      <div>
        <a href="${pageContext.request.contextPath}/product/pediasure/BAB152">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_pediasure2.jpg" alt="Pediasure"/>
        </a>
      </div>
    </li>
 <%--   <li>
      <div>
        <a href="${pageContext.request.contextPath}/nutrition/sports-nutrition">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_nutrition_sports2.jpg" alt="Sports nutrition"/>
        </a>
      </div>
    </li>--%>
    <%--<li>--%>
      <%--<div>--%>
        <%--<a href="${pageContext.request.contextPath}/nutrition/herbs/herbal-tea">--%>
          <%--<img src="<hk:vhostImage/>/images/banners/Nutrition/banner_herbal-tea.jpg" alt="Herbal Tea"/>--%>
        <%--</a>--%>
      <%--</div>--%>
    <%--</li>--%>
    <%--<li>
      <div>
        <a href="${pageContext.request.contextPath}/brand/nutrition/ranbaxy">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/2.jpg" alt="Revital"/>
        </a>
      </div>
    </li>--%>
    

  </ul>
    <div class='grid_6'>
        <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" event="pre">
          <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg" alt="refer a friend and earn" class="small_banner"/>
        </s:link>
        <img src="<hk:vhostImage/>/images/banners/freeshipping_cod_250.jpg" alt="cash on delivery" class="small_banner"/>
      </div>
      <div class="floatfix"></div>
  </div>
</s:layout-component>

<s:layout-component name="topCategory">nutrition</s:layout-component>

<s:layout-component name="product_rows">
  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="nutrition"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="nutrition"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/nutrition/sports-nutrition'>
        Top selling Sports Nutrition Supplements
        <span class='small'>
          (view more products)
        </span>
      </a>
        <%-- <a class='faq' href="${pageContext.request.contextPath}/pages/standard-diet-plan.jsp" title='customize your diet plan as per your need'>
          Diet Planner
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT304"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT419"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT127"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT131"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT130"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT679"/>
    <a class='more' href='${pageContext.request.contextPath}/nutrition/sports-nutrition'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/nutrition/health-wellness/multi-vitamins'>
        Top Selling Multivitamins
        <span class='small'>
          (view more products)
        </span>
      </a>
        <%--<a class='faq' href="${pageContext.request.contextPath}/pages/standard-diet-plan.jsp" title='customize your diet plan as per your need'>
          Diet Planner
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT410"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT333"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT421"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT334"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT423"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT467"/>
    <a class='more' href='${pageContext.request.contextPath}/nutrition/health-wellness/multi-vitamins'>
      view more products &rarr;
    </a>
  </div>
  
</s:layout-component>

</s:layout-render>

