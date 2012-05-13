<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Contact Lens and Eye Care Products in India">

  <s:layout-component name="metaKeywords">Contact lenses india, sunglasses india, rayban online, contact lens online, shop contact lenses</s:layout-component>
  <s:layout-component name="metaDescription">Buy contact lenses and other eye care products in India from all top brands at best prices. Home delivery and cash on delivery available.</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Eye</span>

    <h1 class="title">
      Contact Lenses and Eye Care Products
    </h1>
  </div>
</s:layout-component>
<s:layout-component name="mainBanner">
  <div class="promotional">
  <ul id="categoryBanner" class="grid_18">
    <li>
      <a href="${pageContext.request.contextPath}/product/bausch-lomb-purevision2-hd/EYE007">
        <img src="<hk:vhostImage/>/images/banners/Eye/banner_eye4.jpg" alt="Bausch+Lomb Pure Vision"/>
      </a>
    </li>
    <li>
      <a href="${pageContext.request.contextPath}/eye/sunglasses">
        <img src="<hk:vhostImage/>/images/banners/Eye/banner_eye_sunglasses2.jpg" alt="Fashion wear for your eyes"/>
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

<s:layout-component name="topCategory">eye</s:layout-component>

<s:layout-component name="product_rows">

  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="eye"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="eye"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/eye/lenses'>
        Top selling Contact Lenses
              <span class='small'>
                (view more products)
              </span>
      </a>
      <a class='faq' href="${pageContext.request.contextPath}/pages/aboutContactLens.jsp" title='read FAQs related to Contact Lenses'>
        read FAQs
        <div class='icon'></div>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE005"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE039"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE002"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE004"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE010"/>
    <a class='more' href='${pageContext.request.contextPath}/eye/lenses'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/eye/lenses/color-lenses'>
        Top selling Color Lenses
              <span class='small'>
                (view more products)
              </span>
      </a>
        <%--<a class='faq' title='read FAQs related to Color Lenses products'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE035"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE034"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE037"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE038"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE036"/>
    <a class='more' href='${pageContext.request.contextPath}/eye/lenses/color-lenses'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/eye/sunglasses'>
        Top selling Sunglasses
              <span class='small'>
                (view more products)
              </span>
      </a>
        <%--<a class='faq' title='read FAQs related to Color Lenses products'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE274"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE281"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE297"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE277"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE281"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE288"/>
    <a class='more' href='${pageContext.request.contextPath}/eye/sunglasses'>
      view more products &rarr;
    </a>
  </div>

</s:layout-component>


</s:layout-render>

