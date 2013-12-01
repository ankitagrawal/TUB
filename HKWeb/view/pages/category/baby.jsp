<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Baby Products | Shop Branded Baby Care Products Online in India">

  <s:layout-component name="metaKeywords">Baby Products, Baby Products India, Buy Baby Products, Baby Care Products, Baby Products Shop, Baby Products Store Online in India</s:layout-component>
  <s:layout-component name="metaDescription">Buy from a comprehenive range of baby and mother care products. Best prices and home delivery all over India.</s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
      <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt;
      <span class="crumb last" style="font-size: 12px;">Baby Products</span>

      <h1 class="title">
        Baby products in India
      </h1>
    </div>
  </s:layout-component>
  <s:layout-component name="mainBanner">
    <div class='promotional'>
      <ul id="categoryBanner" class="grid_18">
        <li>
          <div>
            <a href="${pageContext.request.contextPath}/brand/baby/medela">
              <img src="<hk:vhostImage/>/images/banners/Baby/banner_medela.jpg" alt="Full range of Medela products"/>
            </a>
          </div>
        </li>
        <li>
          <div>
            <a href="${pageContext.request.contextPath}/brand/baby/Philips">
              <img src="<hk:vhostImage/>/images/banners/Baby/banner_baby_philips_avent.jpg" alt="Full range of Philips Avent products"/>
            </a>
          </div>
        </li>
        <li>
          <div>
            <a href="#">
              <img src="<hk:vhostImage/>/images/banners/Baby/2.jpg" alt="Full range of baby care products"/>
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

  <s:layout-component name="topCategory">baby</s:layout-component>

  <s:layout-component name="product_rows">

    <div class="products">
      <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
        <br/>
        <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
          <s:param name="category" value="baby"/>
        </s:link>
          &nbsp;|&nbsp;
          <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
            <s:param name="category" value="baby"/>
            Manage Images
          </s:link>
        </div>
      </shiro:hasPermission>
      <h2>
        <a href='${pageContext.request.contextPath}/baby/diapering'>
          Top selling Diapering Products
            <span class='small'>
              (view more products)
            </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB002"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB005"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB017"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB013"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB020"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB015"/>
      <a class='more' href='${pageContext.request.contextPath}/baby/diapering'>
        view more products &rarr;
      </a>
    </div>
    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/baby/baby-food'>
          Top selling Baby Food products
            <span class='small'>
              (view more products)
            </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB027"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB032"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB152"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB148"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB151"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB149"/>
      <a class='more' href='${pageContext.request.contextPath}/baby/baby-food'>
        view more products &rarr;
      </a>
    </div>
    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/baby/bath-skincare'>
          Top selling Baby Gear Products
            <span class='small'>
              (view more products)
            </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB235"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB303"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB302"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB301"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB147"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB283"/>
      <a class='more' href='${pageContext.request.contextPath}/baby/bath-skincare'>
        view more products &rarr;
      </a>
    </div>
    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/baby/bath-skincare'>
          Top selling Combo Offers
            <span class='small'>
              (view more products)
            </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB372"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB368"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB371"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB305"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB108"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BAB359"/>
      <a class='more' href='${pageContext.request.contextPath}/baby/bath-skincare'>
        view more products &rarr;
      </a>
    </div>
  </s:layout-component>
</s:layout-render>

