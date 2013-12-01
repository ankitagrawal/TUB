<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Fitness and Sports Equipment">

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Sports</span>

    <h1 class="title">
      Fitness and Sports Equipment
    </h1>
  </div>
</s:layout-component>

<s:layout-component name="metaDescription">Buy sports and fitness accessories and equipment at best prices. Home delivery anywhere in India.</s:layout-component>
<s:layout-component name="metaKeywords">fitness accessories, sports equipment, cricket bats</s:layout-component>

<s:layout-component name="mainBanner">
  <div class="promotional">
  <ul id="categoryBanner" class="grid_18" >

    <li>
      <div>
        <img src="<hk:vhostImage/>/images/banners/Sports/banner_sports02.jpg" alt="Sports Accessories"/>
      </div>
    </li>
    <li>
      <div>
        <a href="${pageContext.request.contextPath}/sports/fitness-accessories">
          <img src="<hk:vhostImage/>/images/banners/Nutrition/banner_gym_acc_02.jpg" alt="Fitness Accessories"/>
        </a>
      </div>
    </li>
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

<s:layout-component name="topCategory">sports</s:layout-component>

<s:layout-component name="product_rows">
  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="sports"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="sports"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/sports/sports-equipment'>
        Top selling Sports Equipment
        <span class='small'>
          (view more products)
        </span>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT134"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT165"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT172"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT137"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT138"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT176"/>
    <a class='more' href='${pageContext.request.contextPath}/sports/sports-equipment'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/sports/fitness-accessories'>
        Top Selling Fitness Accessories
        <span class='small'>
          (view more products)
        </span>
      </a>
        <%--<a class='faq' href="${pageContext.request.contextPath}/pages/standard-diet-plan.jsp" title='customize your diet plan as per your need'>
          Diet Planner
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT145"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT089"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT104"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT115"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT056"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SPT005"/>
    <a class='more' href='${pageContext.request.contextPath}/sports/fitness-accessories'>
      view more products &rarr;
    </a>
  </div>

</s:layout-component>

</s:layout-render>

