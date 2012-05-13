<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Adult Diapers, Barbasol, Pepper Sprays, Foot Care Products in India">

<s:layout-component name="metaDescription">Shop online for a wide range of personal care products ranging from women's hygiene products, condoms, adult diapers, oral care products and more</s:layout-component>
<s:layout-component name="metaKeywords">condoms online, adult diapers online, personal care products, pepper spray</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Personal Care</span>

    <h1 class="title">
      Sexual Care, Adult Diapers, Women Hygiene, Foot Care Products etc.
    </h1>
  </div>
</s:layout-component>
<s:layout-component name="mainBanner">
  <div class="promotional">
    <ul id="categoryBanner" class="grid_18">

	     <li>
        <a href="${pageContext.request.contextPath}/brand/personal-care/trisa">
          <img src="<hk:vhostImage/>/images/banners/PersonalCare/banner_trisa.jpg" alt="Peronal hygiene products for women"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/personal-care/misc/wipes-tissues">
          <img src="<hk:vhostImage/>/images/banners/PersonalCare/banner_kara.jpg" alt="Peronal hygiene products for women"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/personal-care/women">
          <img src="<hk:vhostImage/>/images/banners/PersonalCare/2.jpg" alt="Peronal hygiene products for women"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/brand/personal-care/Tepe">
          <img src="<hk:vhostImage/>/images/banners/PersonalCare/banner_personal_tepe.jpg" alt="Tepe"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/personal-care/sexual-care">
          <img src="<hk:vhostImage/>/images/banners/PersonalCare/1.jpg" alt="Buy condoms and sexual care accessories online"/>
        </a>
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

<s:layout-component name="topCategory">personal-care</s:layout-component>
<s:layout-component name="product_rows">

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/personal-care'>
        Top selling Personal  Care products
            <span class='small'>
              (view more products)
            </span>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PA006"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PA012"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="OH030"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PW006"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP002"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PF059"/>
    <a class='more' href='${pageContext.request.contextPath}/personal-care'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="personal-care"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="personal-care"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/personal-care/sexual-care'>
        Top selling Sexual Care products
            <span class='small'>
              (view more products)
            </span>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS037"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PM011"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS016"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS035"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS033"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS052"/>
    <a class='more' href='${pageContext.request.contextPath}/personal-care/sexual-care'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/personal-care/women'>
        Top selling Women Personal Care products
            <span class='small'>
              (view more products)
            </span>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PW008"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PW016"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PW010"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PPS008"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PS041"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PW009"/>
    <a class='more' href='${pageContext.request.contextPath}/personal-care/women'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/personal-care/misc'>
        Top selling Misc Personal Care products
            <span class='small'>
              (view more products)
            </span>
      </a>
      <a class='faq' href='${pageContext.request.contextPath}/pages/aboutPepperSpray.jsp' title='read FAQs related to Pepper Spray'>
        read FAQs
        <div class='icon'></div>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP015"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP028"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP001"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PA005"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP021"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP007"/>
    <a class='more' href='${pageContext.request.contextPath}/personal-care/misc'>
      view more products &rarr;
    </a>
  </div>
</s:layout-component>
</s:layout-render>

