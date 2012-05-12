<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa" event="pre"/>
<c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
<c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
<c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
<%
  CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
  Category eyeGlass = categoryDao.getCategoryByName("eyeglasses");
  pageContext.setAttribute("eyeGlass", eyeGlass);

%>

<s:layout-render name="/layouts/productLayout.jsp" pageTitle="${pa.seoData.title}">
<%--<s:layout-render name="/layouts/default.jsp" pageTitle="${pa.seoData.title}">--%>


<s:layout-component name="htmlHead">

  <meta name="keywords" content="${pa.seoData.metaKeyword}"/>
  <meta name="description" content="${pa.seoData.metaDescription}"/>
  <style type="text/css">
    .progressLoader {
      display: none;
    }

    ul#thumblist {
      display: block;
    }

    ul#thumblist li {
      float: left;
      margin-right: 2px;
      list-style: none;
    }

    ul#thumblist li a {
      display: block;
      border: 1px solid #CCC;
    }

    ul#thumblist li a.zoomThumbActive {
      border: 1px solid red;
    }

    .jqzoom {

      text-decoration: none;
      float: left;
    }
  </style>
  <link href="${pageContext.request.contextPath}/css/jquery.jqzoom.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jqzoom-core.js"></script>

  <script type="text/javascript">
    $(document).ready(function() {
      // stepper not working for some reason for form submit. field is not getting picked up for post request.
      //    $('.lineItemQty').jqStepper(1);
      $('.popup').popupWindow({
        height: 600,
        width: 900
      });


      //Click and change image
      $('.color_box').click(function() {
        var variantMainImageId = $(this).find('.variantMainImageId').val();
        //var url = "http://healthkart-prod.s3.amazonaws.com/1/"+variantMainImageId+"_t.jpg";
        //$(".img320").html("<img class='icon' src='"+url+"'/>") ;
        $.getJSON(
            $('#updateProductVariantImageLink').attr('href'), {mainProductImageId: variantMainImageId},
            function(resurl) {
              //                alert(resurl.data);
              $(".img320").html("<img class='icon' src='" + resurl.data + "'/>");
            }
            );
      });

      $('.jqzoom').jqzoom({
        zoomType: 'standard',
        lens:true,
        preloadImages: false,
        alwaysOn:false
      });

      $('#notifyMeWindow').jqm({trigger: '.notifyMe', ajax: '@href'});

    });


  </script>

</s:layout-component>

<s:layout-component name="modal">

  <c:if test="${!empty pa.product.productVariants[0].productExtraOptions}">
    <s:layout-render name="/pages/modal/productWithExtraOptions.jsp" productId="${pa.product.id}"/>
  </c:if>
  <%--<c:if test="${hk:collectionContains(pa.product.categories, eyeGlass)}">--%>
  <%--<s:layout-render name="/pages/modal/eyeGlasses.jsp" productId="${pa.product.id}"/>--%>
  <%--</c:if>--%>

  <c:if test="${pa.combo == null}">
    <shiro:hasPermission name="<%=PermissionConstants.GET_PRODUCT_LINK%>">
      <s:layout-render name="/pages/modal/productLink.jsp" productId="${pa.product.id}"
                       affiliateId="${pa.affiliate.id}"/>

      <s:layout-render name="/pages/modal/productBannerLink.jsp" productId="${pa.product.id}"
                       affiliateId="${pa.affiliate.id}"/>
    </shiro:hasPermission>
  </c:if>

  <div class="jqmWindow" style="display:none;" id="notifyMeWindow"></div>

</s:layout-component>

<s:layout-component name="breadcrumbs">
  <s:layout-render name="/layouts/embed/catalogBreadcrumb.jsp" breadcrumbProduct="${pa.product}" lastLink="true"
                   topHeading="${pa.seoData.h1}"/>
</s:layout-component>

<s:layout-component name="topCategory">${pa.topCategoryUrlSlug}</s:layout-component>
<s:layout-component name="allCategories">${pa.allCategories}</s:layout-component>
<s:layout-component name="brand">${pa.product.brand}</s:layout-component>
<s:layout-component name="productNameLabel">${pa.product.name}</s:layout-component>

<s:layout-component name="topBanner">
  <s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" topCategories="${pa.topCategoryUrlSlug}"/>
  <div class="clear"></div>
  <c:if test="${pa.product.service}">
    <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment="${pa.urlFragment}"/>
  </c:if>

</s:layout-component>
<s:layout-component name="prod_title">


  <div>
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
      <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank" class="popup">Edit MetaData
        <s:param name="seoData" value="${pa.seoData.id}"/>
      </s:link>
      &nbsp;|&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editProductVariantDetails"
              target="_blank" class="popup">
        Edit Variant Attributes
        <s:param name="product" value="${pa.product}"/>
      </s:link>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editProductDetails" target="_blank"
              class="popup">
        Edit Product Attributes
        <s:param name="product" value="${pa.product}"/>
      </s:link>
      <s:link beanclass="com.hk.web.action.admin.sku.SkuAction" event="searchSKUs" target="_blank" class="popup">
        Edit Sku Attributes
        <s:param name="productId" value="${pa.product.id}"/>
      </s:link>
    </shiro:hasPermission>
  </div>

</s:layout-component>

<s:layout-component name="prod_slideshow">
  <div class='product_slideshow'>
    <div class="img320">
      <a href="${hk:getS3ImageUrl(imageLargeSize, pa.product.mainImageId)}" class="jqzoom" rel='gal1'
         title="${pa.product.name}">
        <img src="${hk:getS3ImageUrl(imageMediumSize, pa.product.mainImageId)}" alt="${pa.product.name}"
             title="${pa.product.name}">
      </a>
      <c:if test="${fn:length(pa.productImages) > 1}">
        <ul id="thumblist">
          <c:forEach items="${pa.productImages}" var="productImage">
            <li><a href='javascript:void(0);'
                   rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, productImage.id)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, productImage.id)}'}"><img
                src='${hk:getS3ImageUrl(imageSmallSize, productImage.id)}'></a></li>
          </c:forEach>
        </ul>
      </c:if>
    </div>
    <div><br/><br/><br/></div>
    <div>
      <shiro:hasPermission name="<%=PermissionConstants.GET_PRODUCT_LINK%>">
        <a name="showProductLink" class="linkbutton"
           onclick="$('#getProductLinkWindow').jqm(); $('#getProductLinkWindow').jqmShow();" style="cursor:pointer">Get
          Links</a>
        <a name="showProductLink" class="linkbutton"
           onclick="$('#getBannerLinkWindow').jqm(); $('#getBannerLinkWindow').jqmShow();" style="cursor:pointer">Get
          Banners</a>
      </shiro:hasPermission>
    </div>
    <shiro:hasPermission name="<%=PermissionConstants.MANAGE_IMAGE%>">
      <br/>

      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="product" value="${pa.product.id}"/>
      </s:link>
        <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="manageProductImages" target="_blank"
                class="popup">Manage
          Images
          <s:param name="productId" value="${pa.product.id}"/>
        </s:link>
      </div>
    </shiro:hasPermission>
    <c:if test="${pa.product.videoEmbedCode != null}">
      <s:layout-render name="/layouts/embed/_productVideo.jsp" productId="${pa.product.id}"/>
    </c:if>
  </div>

  <c:if test="${pa.product.id == 'NUT904'}">
    <div id="gulal" style="position:absolute; left: 600px; display: none">
      <img src="<hk:vhostImage/>/images/gulal-spray.png"/>
    </div>
  </c:if>

</s:layout-component>

<s:layout-component name="product_detail_links">
  <h2 class='prod_title'>
      ${pa.product.name}
  </h2>

  <div class='infos'>
    <c:if test="${hk:isNotBlank(pa.product.brand)}">
          <span class='title'>
            Brand:
          </span>
          <span class='info'>
            <s:link beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" class="bl">
              ${pa.product.brand}
              <s:param name="brand" value="${fn:toLowerCase(pa.product.brand)}"/>
              <s:param name="topLevelCategory" value="${pa.topCategoryUrlSlug}"/>
            </s:link>
          </span>
    </c:if>
    <c:if test="${hk:isNotBlank(pa.product.manufacturer)}">
      |
          <span class='title'>
            Manufacturer:
          </span>
          <span class='info'>
              ${pa.product.manufacturer.name}
          </span>
    </c:if>
    |
    <span class='title'>
      Dispatched in:
    </span>
    <span class='info orange' title="Delivery time is extra depending on the location">
      <c:choose><c:when test="${hk:isNotBlank(pa.product.maxDays)}">
        ${pa.product.minDays} - ${pa.product.maxDays} working days
      </c:when>
        <c:otherwise>
          <c:choose>
            <c:when test="${pa.topCategoryUrlSlug == 'beauty'}">
              2 - 7 working days
            </c:when>
            <c:otherwise>
              1 - 3 working days
            </c:otherwise>
          </c:choose>
        </c:otherwise>
      </c:choose>
    </span>

  </div>

  <div class='top_links'>
    <c:if test="${!empty pa.product.description}">
      <a class='top_link' href='#description'>
        Description &darr;
      </a>
    </c:if>
    <c:if test="${!empty pa.product.productFeatures}">
      <a class='top_link' href='#features'>
        Technical Specs &darr;
      </a>
    </c:if>
      <%--<a class='top_link' href='#link3'>
            FAQs
          </a>
          <a class='top_link' href='#link4'>
            User Reviews
          </a>
          <a class='top_link' href='#link5'>
            Payment Options
          </a>
        <a class='top_link' href='#related_products'>
          Related Products &darr;
        </a>
      </div>

      <%--<c:if test="${pa.product.id == 'BAB152'}">
        <p>
          <a href="${pageContext.request.contextPath}/partners/pediasure.jsp" class="pediasureCounselling" target="_blank">Free
            Nutrition Counseling </a>
        </p>
      </c:if>--%>
  </div>
  <c:if test="${hk:isNotBlank(pa.product.overview)}">
    <p class="overview">
        ${pa.product.overview}
    </p>
  </c:if>
  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
    <div>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editOverview" class="popup">
        Edit Overview
        <s:param name="productId" value="${pa.product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>

</s:layout-component>

<s:layout-component name="product_variants">
  <c:choose>
    <c:when test="${empty pa.product.productVariants[0].productExtraOptions}">
      <c:choose>
        <c:when test="${fn:length(pa.product.productVariants) > 1}">
          <c:choose>
            <c:when test="${!pa.product.productHaveColorOptions}">
              <s:layout-render name="/layouts/embed/_productWithMultipleVariantsWithNoColorOptions.jsp"
                               productId="${pa.product.id}"/>
              <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
            </c:when>
            <c:otherwise>
              <s:layout-render name="/layouts/embed/_productWithMultipleVariantsWithColorOptions.jsp"
                               productId="${pa.product.id}"/>
              <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
            </c:otherwise>
          </c:choose>
        </c:when>
        <c:otherwise>
          <c:choose>
            <c:when test="${pa.combo != null}">
              <s:layout-render name="/layouts/embed/_comboProduct.jsp" productId="${pa.product.id}"/>
            </c:when>
            <c:when test="${hk:collectionContains(pa.product.categories, eyeGlass)}">
              <s:layout-render name="/layouts/embed/glasses.jsp" productId="${pa.product.id}"/>
            </c:when>
            <c:otherwise>
              <s:layout-render name="/layouts/embed/_productWithSingleVariant.jsp" productId="${pa.product.id}"/>
            </c:otherwise>
          </c:choose>
        </c:otherwise>
      </c:choose>
    </c:when>
    <c:otherwise>
      <s:layout-render name="/layouts/embed/_productWithExtraOptions.jsp" productId="${pa.product.id}"/>
      <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
    </c:otherwise>
  </c:choose>
  <div class="floatfix"></div>
</s:layout-component>


<s:layout-component name="product_description">
  <c:if test="${hk:isNotBlank(pa.product.description)}">
    <div class="content" id="description">
      <h4>
        Description
      </h4>

      <p>
          ${pa.product.description}

      </p>
    </div>
  </c:if>

  <c:if test="${pa.addressDistanceDtos != null && fn:length(pa.addressDistanceDtos) > 0}">
      <h3> Service Centres Available Near to You : <br>
        <ul>
          <c:forEach items="${pa.addressDistanceDtos}" var="addressDto">
            <li>  ${addressDto.localityMap.address.line1}, ${addressDto.localityMap.address.line2}<br></li>
          </c:forEach>
        </ul>
      </h3>
    </c:if>
  <p>
      <a class='go_to_top' href='#top' style="float:right;">go to top &uarr;</a>
  </p>

  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
    <div>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editDescription" class="popup">
        Edit Description
        <s:param name="productId" value="${pa.product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>


  <c:if test="${!empty (pa.product.productFeatures)}">
    <div class="content" id="features">
      <h4>
        Product Features
      </h4>

      <table class="">
        <tbody>
        <c:forEach var="feature" items="${pa.product.productFeatures}">
          <tr>
            <td>
                ${feature.name}
            </td>
            <td>
                ${feature.value}
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <a class='go_to_top' href='#top'>go to top &uarr;</a>

    </div>
  </c:if>

  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
    <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editFeatures" class="popup">
      Edit Features
      <s:param name="product" value="${pa.product}"/>
    </s:link>
  </shiro:hasPermission>

</s:layout-component>

<s:layout-component name="related_products">
  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
    <br/>

    <div>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editRelatedProducts" class="popup">
        Edit Related Products
        <s:param name="productId" value="${pa.product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>
  <c:if test="${!empty pa.product.relatedProducts}">
    <div class='products content' id="related_products">
      <h4>
        People who bought this also bought these products
      </h4>
      <c:forEach items="${pa.product.relatedProducts}" var="relatedProduct">
        <c:if test="${!relatedProduct.deleted}">
          <s:layout-render name="/layouts/embed/_productThumb.jsp" productId="${relatedProduct.id}"/>
        </c:if>
      </c:forEach>

      <div class="floatfix"></div>
      <a class='go_to_top' href='#top'>go to top &uarr;</a>

    </div>
  </c:if>
</s:layout-component>

<%--<s:layout-component name="foot_price">
  <h5 style="text-align:center; padding: 0.5em; margin-right: 5px; color: #888;">
      ${pa.product.name} price in
    India <span class="num" style="font-weight: bold;">Rs.
        <c:choose>
          <c:when test="${pa.combo != null}">
            <fmt:formatNumber
                value="${pa.combo.hkPrice}" maxFractionDigits="0"/>
          </c:when>
          <c:otherwise>
            <fmt:formatNumber
                value="${pa.product.minimumMRPProducVariant.hkPrice + hk:getPostpaidAmount(variant)}"
                maxFractionDigits="0"/>
          </c:otherwise>
        </c:choose>
      <c:if
          test="${pa.product.minimumMRPProducVariant.hkPrice != pa.product.maximumMRPProducVariant.hkPrice}">- Rs.
          <fmt:formatNumber value="${pa.product.maximumMRPProducVariant.hkPrice + hk:getPostpaidAmount(variant)}"
                            maxFractionDigits="0"/></span></c:if>
  </h5>
</s:layout-component>--%>

<%--<s:layout-component name="user_reviews">
  <div class='products content' id="user_reviews">
  <hr/>
  <h3>Reviews of ${pa.product.name}</h3>
  <c:choose>
    <c:when test="${!empty pa.userReviews}">


      Average Rating : ${pa.starRating}/5

      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" event="writeNewReview">
        <s:param name="product" value="${pa.product.id}"/>
        Write a Review
      </s:link>

      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction">
        <s:param name="product" value="${pa.product.id}"/>
        All Review
      </s:link>
      <hr/>

      <c:forEach items="${pa.userReviews}" var="review">
        <div class="grid_4">${review.postedBy.name}</div>

        <div class="grid_12">
          <pre>${review.review}</pre>
        </div>
        <hr/>
      </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <h3>No Reviews!!</h3>
      Be the first one to <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" event="writeNewReview">
        <s:param name="product" value="${pa.product.id}"/>
        <strong>Write</strong>
      </s:link> the product review
    </c:otherwise>
  </c:choose>

</s:layout-component>--%>

<s:layout-component name="endScripts">
  <script type="text/javascript">
    $(document).ready(function() {

      function _addToCart(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
          $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
          $('.progressLoader').hide();

          show_message();
        }
        $('#gulal').show();
      }

      function _addToCart2(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          $('.message .line1').html("<strong>" + res.data.name + "</strong> is added to your shopping cart");
        } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
          $('#cart_error1').html(getErrorHtmlFromJsonResponse(res))
              .slowFade(3000, 2000);
        } else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
          window.location.replace(res.data.url);
        }
        $('.progressLoader').hide();
      }

    <c:if test="${pa.combo == null}">
      $('.addToCartButton').click(function(e) {
        $(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
        $(this).hide();
        e.stopPropagation();
      });
    </c:if>

      $(".message .close").click(function() {
        hide_message();
      });
      $(document).click(function() {
        hide_message();
      });

      function hide_message() {
        $('.message').animate({
          top: '-170px',
          opacity: 0
        }, 100);
      }

      function show_message() {
        $('.message').css("top", "70px");
        $('.message').animate({
          opacity: 1
        }, 500);
      }

      $('.addToCartForm').ajaxForm({dataType: 'json', success: _addToCart});

      $(".top_link, .go_to_top").click(function(event) {
        event.preventDefault();
        $('html,body').animate({scrollTop:($(this.hash).offset().top - 45)}, 300);
      });

      $('#productBannerTextArea').val($('#productBannerTextArea').val().replace(/\s+/g, " "));

    });
  </script>
  <iframe
      src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e300&pid=${pa.product.id}&catid=${pa.product.primaryCategory.name}&subcat1id=&subcat2id=&section=1&level=1"
      scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</s:layout-component>
</s:layout-render>