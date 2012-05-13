<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Beauty Products | Cosmetics | Buy Branded Beauty Care Products and Cosmetics in India">

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
      <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt;
      <span class="crumb last" style="font-size: 12px;">Beauty</span>

      <h1 class="title">
        Beauty and Cosmetic products in India
      </h1>
    </div>
  </s:layout-component>

  <s:layout-component name="metaDescription">Widest range of branded makeup, cosmetics and skin care products. Home delivered anywhere in India - shop now from India's best e-health store.</s:layout-component>
  <s:layout-component name="metaKeywords">Beauty Products, Beauty Products India, Beauty Store, Beauty Shop India, Online Beauty Store, Beauty Products Store in India</s:layout-component>

  <s:layout-component name="mainBanner">
    <div class='promotional'>

      <ul id="categoryBanner" class="grid_18">

	       <li>
          <a href="${pageContext.request.contextPath}/brand/beauty/Revlon">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_revlon_ex.jpg" alt="Revlon Absolute" title=""/>
          </a>
        </li>
        
        <li>
          <a href="${pageContext.request.contextPath}/brand/beauty/Absolute">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_lakme.jpg" alt="Lakme Absolute" title=""/>
          </a>
        </li>
        <%--<li>
          <a href="http://www.facebook.com/healthkart?sk=app_130363280399851" target="_blank">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_fb.jpg" alt="Beauty Hat trick" title=""/>
          </a>
        </li>--%>
        <li>
          <a href="${pageContext.request.contextPath}/brand/beauty/Fabindia">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_fabindia.jpg" alt="25% off on Organic Surge" title=""/>
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/brand/beauty/NYX">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_nyx.jpg" alt="Nyx" title=""/>
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/brand/beauty/Saint+Pure">
            <img src="<hk:vhostImage/>/images/banners/Beauty/banner_beauty_saintpure.jpg" alt="Sanitpure" title=""/>
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

  <s:layout-component name="topCategory">beauty</s:layout-component>
  <s:layout-component name="product_rows">

    <div class="products">
      <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
        <br/>
        <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
          <s:param name="category" value="beauty"/>
        </s:link>
          &nbsp;|&nbsp;
          <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
            <s:param name="category" value="beauty"/>
            Manage Images
          </s:link>
        </div>
      </shiro:hasPermission>
      <h2>
        <a href='${pageContext.request.contextPath}/beauty/make-up'>
          Top Selling Make Up Products
            <span class='small'>
              (view more products)
            </span>
        </a>
        <a class='faq' href="${pageContext.request.contextPath}/pages/aboutBeauty.jsp" title='read FAQs related to Beauty'>
          read FAQs
          <div class='icon'></div>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="LAKABS1"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="LAKABS5"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="OPI1"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY429"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NYX26"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NYX37"/>
      <a class='more' href='${pageContext.request.contextPath}/beauty/make-up'>
        view more products &rarr;
      </a>
    </div>

    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/beauty/face-care'>
          Top Selling Face Care Products
            <span class='small'>
              (view more products)
            </span>
        </a>
        <a class='faq' href="${pageContext.request.contextPath}/pages/aboutBeauty.jsp" title='read FAQs related to Beauty'>
          read FAQs
          <div class='icon'></div>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="OLY001"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="VNCP08"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY145"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="FABIN32"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NGENA3"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY550"/>
      <a class='more' href='${pageContext.request.contextPath}/beauty/face-care'>
        view more products &rarr;
      </a>
    </div>

    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/beauty/hair-care'>
          Top Selling Hair Care Products
            <span class='small'>
              (view more products)
            </span>
        </a>

      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY115"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY511"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY409"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="LORMAS2"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PHILI09"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY652"/>
      <a class='more' href='${pageContext.request.contextPath}/beauty/hair-care'>
        view more products &rarr;
      </a>
    </div>

    <div class="products">
      <h2>
        <a href='${pageContext.request.contextPath}/beauty/bath-body'>
          Top Selling Bath & Body Care
        <span class='small'>
              (view more products)
            </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY309"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="FABIN16"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="H2O004"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SNTPR59"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BTY048"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="ZEPTER1"/>
      <a class='more' href='${pageContext.request.contextPath}/beauty/bath-body'>
        view more products &rarr;
      </a>
    </div>

  </s:layout-component>

</s:layout-render>

