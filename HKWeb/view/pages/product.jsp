<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.catalog.ProductService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa" event="pre"/>
<c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
<c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
<c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
<%
  CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
  Category eyeGlass = categoryDao.getCategoryByName("eyeglasses");
  ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
  pageContext.setAttribute("productService", productService);
  pageContext.setAttribute("eyeGlass", eyeGlass);
  
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);

%>
<c:set var="product" value="${pa.product}"/>
<c:set var="seoData" value="${pa.seoData}"/>
<c:set var="subscriptionProduct" value="${pa.subscriptionProduct}" />
<s:layout-render name="/layouts/productLayout.jsp" pageTitle="${seoData.title}">
<%--<s:layout-render name="/layouts/default.jsp" pageTitle="${seoData.title}">--%>


<s:layout-component name="htmlHead">

  <meta name="keywords" content="${seoData.metaKeyword}"/>
  <meta name="description" content="${seoData.metaDescription}"/>
  <style type="text/css">
    .progressLoader {
      display: none;
    }

    .hidden {
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

    .rating_bar {
      width: 80px;
      background: url('${pageContext.request.contextPath}/images/img/star-off.png') 0 0 repeat-x;
    }

    .rating_bar div {
      height: 16px;
      background: url('${pageContext.request.contextPath}/images/img/star-on.png') 0 0 repeat-x;
    }
  </style>

  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.jqzoom-core.js"></script>

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
        $.getJSON($('#updateProductVariantImageLink').attr('href'), {mainProductImageId: variantMainImageId}, function(resurl) {
          //                alert(resurl.data);
          $(".img320").html("<img class='icon' src='" + resurl.data + "'/>");
        });
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

  <c:if test="${!empty product.productVariants[0].productExtraOptions}">
    <s:layout-render name="/pages/modal/productWithExtraOptions.jsp" product="${product}"/>
  </c:if>
  <%--<c:if test="${hk:collectionContains(product.categories, eyeGlass)}">--%>
  <%--<s:layout-render name="/pages/modal/eyeGlasses.jsp" product="${product}"/>--%>
  <%--</c:if>--%>

  <c:if test="${pa.combo == null}">
    <shiro:hasPermission name="<%=PermissionConstants.GET_PRODUCT_LINK%>">
      <s:layout-render name="/pages/modal/productLink.jsp" product="${product}"
                       affiliateId="${pa.affiliate.id}"/>

      <s:layout-render name="/pages/modal/productBannerLink.jsp" product="${product}"
                       affiliateId="${pa.affiliate.id}"/>
    </shiro:hasPermission>
  </c:if>

  <div class="jqmWindow" style="display:none;" id="notifyMeWindow"></div>

</s:layout-component>

<s:layout-component name="breadcrumbs">
  <s:layout-render name="/layouts/embed/catalogBreadcrumb.jsp" breadcrumbProduct="${product}" lastLink="true"
                   topHeading="${seoData.h1}"/>
</s:layout-component>

<s:layout-component name="topCategory">${pa.topCategoryUrlSlug}</s:layout-component>
<s:layout-component name="allCategories">${pa.allCategories}</s:layout-component>
<s:layout-component name="brand">${product.brand}</s:layout-component>
<s:layout-component name="productNameLabel">${product.name}</s:layout-component>

<s:layout-component name="topBanner">
  <s:layout-render name="/layouts/embed/_categoryTopBanners.jsp" topCategories="${pa.topCategoryUrlSlug}"/>
  <div class="clear"></div>
  <c:if test="${product.service}">
    <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment="${pa.urlFragment}"/>
  </c:if>

</s:layout-component>
<s:layout-component name="prod_title">


  <div>
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
      <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank"
              class="popup">Edit MetaData
        <s:param name="seoData" value="${seoData.id}"/>
      </s:link>
      &nbsp;|&nbsp;
    </shiro:hasPermission>
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
              event="editProductVariantDetails"
              target="_blank" class="popup">
        Edit Variant Attributes
        <s:param name="product" value="${product}"/>
      </s:link>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editProductDetails"
              target="_blank"
              class="popup">
        Edit Product Attributes
        <s:param name="product" value="${product}"/>
      </s:link>
      <s:link beanclass="com.hk.web.action.admin.sku.SkuAction" event="searchSKUs" target="_blank" class="popup">
        Edit Sku Attributes
        <s:param name="productId" value="${product.id}"/>
      </s:link>
    </shiro:hasPermission>
  </div>

</s:layout-component>

<s:layout-component name="prod_slideshow">
  <div class='product_slideshow'>
    <div class="img320">
      <a href="${hk:getS3ImageUrl(imageLargeSize, product.mainImageId,isSecure)}" class="jqzoom" rel='gal1'
         title="${product.name}">
        <img src="${hk:getS3ImageUrl(imageMediumSize, product.mainImageId,isSecure)}" alt="${product.name}"
             title="${product.name}">
      </a>
      <c:if test="${fn:length(pa.productImages) > 1}">
        <ul id="thumblist">
          <c:forEach items="${pa.productImages}" var="productImage">
            <li><a href='javascript:void(0);'
                   rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, productImage.id,isSecure)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, productImage.id,isSecure)}'}"><img
                src='${hk:getS3ImageUrl(imageSmallSize, productImage.id,isSecure)}'></a></li>
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

      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="pre" target="_blank"
                   class="popup"> Upload
        <s:param name="product" value="${product.id}"/>
      </s:link>
        <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
                event="manageProductImages" target="_blank"
                class="popup">Manage
                              Images
          <s:param name="productId" value="${product.id}"/>
        </s:link>
      </div>
    </shiro:hasPermission>
    <c:if test="${product.videoEmbedCode != null}">
      <s:layout-render name="/layouts/embed/_productVideo.jsp" productId="${product.id}"/>
    </c:if>
  </div>

  <c:if test="${product.id == 'NUT904'}">
    <div id="gulal" style="position:absolute; left: 600px; display: none">
      <img src="<hk:vhostImage/>/images/gulal-spray.png"/>
    </div>
  </c:if>

</s:layout-component>

<s:layout-component name="product_detail_links">
  <h2 class='prod_title'>
      ${product.name}
  </h2>

  <div class='infos' style="border-bottom:0px;">
    <c:if test="${hk:isNotBlank(product.brand)}">
          <span class='title'>
            Brand:
          </span>
          <span class='info'>
            <s:link beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" class="bl">
              ${product.brand}
              <s:param name="brand" value="${fn:toLowerCase(product.brand)}"/>
              <s:param name="topLevelCategory" value="${pa.topCategoryUrlSlug}"/>
            </s:link>
          </span>
    </c:if>
    <c:if test="${hk:isNotBlank(product.manufacturer)}">
      |
          <span class='title'>
            Manufacturer:
          </span>
          <span class='info'>
              ${product.manufacturer.name}
          </span>
    </c:if>
    |
    <span class='title'>
      Dispatched in:
    </span>
    <span class='info orange' title="Delivery time is extra depending on the location">
      <c:choose><c:when test="${hk:isNotBlank(product.maxDays)}">
        ${product.minDays} - ${product.maxDays} working days
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

    <c:if test="${!empty pa.userReviews}">

      <div style="float:right;margin-right:5px;margin-bottom:3px;">
        <a class="top_link" href='#user_reviews' style="border-bottom:0px;">
            ${pa.totalReviews} Reviews &darr;
        </a>

        <div class="rating_bar">
          <div class="blueStarTop" id="blueStarTop">
            <script type="text/javascript">
              var averageRating = ${pa.averageRating};
              averageRating = (averageRating * 20) + "%";
              $('.blueStarTop').width(averageRating);
            </script>
          </div>
        </div>
      </div>
    </c:if>
  </div>

  <div class='top_links'>
    <c:if test="${!empty product.description}">
      <a class='top_link' href='#description'>
        Description &darr;
      </a>
    </c:if>
    <c:if test="${!empty product.productFeatures}">
      <a class='top_link' href='#features'>
        Technical Specs &darr;
      </a>
    </c:if>
	<c:if test="${!empty pa.relatedCombos}">
      <a class='top_link' href='#related_combos' style="font-weight:bold;">
        Special Offers &darr;
      </a>
    </c:if>
	  <c:if test="${!empty product.relatedProducts}">
      <a class='top_link' href='#related_products'>
        Related Products &darr;
      </a>
    </c:if>
  </div>
  <c:if test="${!empty subscriptionProduct}">
    <%--  <s:layout-render name="/layouts/embed/_subscription.jsp" subscriptionProduct="${subscriptionProduct}"/> --%>
    <div class="jqmWindow" style="display:none;" id="subscriptionWindow"></div>

    <script type="text/javascript">
      $(document).ready(function(){
        $('#subscriptionWindow').jqm({trigger: '.addSubscriptionButton', ajax: '@href', ajaxText:'<label>testing   </label>'});
      });

    </script>
  </c:if>
  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
    <div>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editOverview"
              class="popup">
        Edit Overview
        <s:param name="productId" value="${product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>
  <c:if test="${hk:isNotBlank(product.overview)}">
    <p class="overview">
        ${product.overview}
    </p>
  </c:if>

</s:layout-component>

<s:layout-component name="product_variants">
  <c:choose>
    <c:when test="${empty product.productVariants[0].productExtraOptions}">
      <c:choose>
        <c:when test="${fn:length(product.productVariants) > 1}">
          <c:choose>
            <c:when test="${!product.productHaveColorOptions}">
              <s:layout-render name="/layouts/embed/_productWithMultipleVariantsWithNoColorOptions.jsp"
                               product="${product}" subscriptionProduct="${subscriptionProduct}"/>
              <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
            </c:when>
            <c:otherwise>
              <s:layout-render name="/layouts/embed/_productWithMultipleVariantsWithColorOptions.jsp"
                               product="${product}"/>
              <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
            </c:otherwise>
          </c:choose>
        </c:when>
        <c:otherwise>
          <c:choose>
            <c:when test="${pa.combo != null}">
              <s:layout-render name="/layouts/embed/_comboProduct.jsp" productId="${product.id}"/>
            </c:when>
            <c:when test="${hk:collectionContains(product.categories, eyeGlass)}">
              <s:layout-render name="/layouts/embed/glasses.jsp" product="${product}"/>
            </c:when>
            <c:otherwise>
              <s:layout-render name="/layouts/embed/_productWithSingleVariant.jsp" product="${product}"  subscriptionProduct="${subscriptionProduct}"/>
            </c:otherwise>
          </c:choose>
        </c:otherwise>
      </c:choose>
    </c:when>
    <c:otherwise>
      <s:layout-render name="/layouts/embed/_productWithExtraOptions.jsp" product="${product}"/>
      <s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>
    </c:otherwise>
  </c:choose>
  <div class="floatfix"></div>

	<shiro:hasPermission name="<%=PermissionConstants.MANAGE_IMAGE%>">
    <div>
      <s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="uploadVariantImage"
              target="_blank"
              class="popup"> Upload
        <s:param name="productVariant" value="${product.productVariants[0]}"/>
      </s:link>
      &nbsp;|&nbsp;
      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction" event="renderManageImages"
              target="_blank" class="popup">Manage
        Images
        <s:param name="productVariant" value="${product.productVariants[0]}"/>
      </s:link>
    </div>
  </shiro:hasPermission>
</s:layout-component>


<s:layout-component name="product_description">

	<c:if test="${!empty pa.relatedCombos}">
		<div class='products content' id="related_combos">
			<h4>
				Special Offers on ${product.name}
			</h4>
			<c:forEach items="${pa.relatedCombos}" var="relatedCombo">
				<s:layout-render name="/layouts/embed/_productThumb.jsp" productId="${relatedCombo.id}"/>
			</c:forEach>

			<div class="floatfix"></div>
			<a class='go_to_top' href='#top'>go to top &uarr;</a>
		</div>
	</c:if>

  <c:if test="${hk:isNotBlank(product.description)}">
    <div class="content" id="description">
      <h4>
        Description
      </h4>

      <p>
          ${product.description}

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
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editDescription"
              class="popup">
        Edit Description
        <s:param name="productId" value="${product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>


  <c:if test="${!empty (product.productFeatures)}">
    <div class="content" id="features">
      <h4>
        Product Features
      </h4>

      <table class="">
        <tbody>
        <c:forEach var="feature" items="${product.productFeatures}">
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
    <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editFeatures"
            class="popup">
      Edit Features
      <s:param name="product" value="${product}"/>
    </s:link>
  </shiro:hasPermission>

</s:layout-component>

<s:layout-component name="related_products">
  <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
    <br/>

    <div>
      <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
              event="editRelatedProducts" class="popup">
        Edit Related Products
        <s:param name="productId" value="${product.id}"/>
      </s:link>
    </div>
  </shiro:hasPermission>

	<c:set var="relatedProducts" value="${product.relatedProducts}"/>
	<c:if test="${!empty relatedProducts}">
		<div class='products content' id="related_products">
			<h4>
				People who bought this also bought these products
			</h4>

			<c:forEach items="${relatedProducts}" var="relatedProduct">
				<s:layout-render name="/layouts/embed/_productThumbG.jsp" product="${relatedProduct}"/>
			</c:forEach>

			<div class="floatfix"></div>
			<a class='go_to_top' href='#top'>go to top &uarr;</a>

		</div>
	</c:if>
</s:layout-component>

<%--<s:layout-component name="foot_price">
  <h5 style="text-align:center; padding: 0.5em; margin-right: 5px; color: #888;">
      ${product.name} price in
    India <span class="num" style="font-weight: bold;">Rs.
        <c:choose>
          <c:when test="${pa.combo != null}">
            <fmt:formatNumber
                value="${pa.combo.hkPrice}" maxFractionDigits="0"/>
          </c:when>
          <c:otherwise>
            <fmt:formatNumber
                value="${product.minimumMRPProducVariant.hkPrice + hk:getPostpaidAmount(variant)}"
                maxFractionDigits="0"/>
          </c:otherwise>
        </c:choose>
      <c:if
          test="${product.minimumMRPProducVariant.hkPrice != product.maximumMRPProducVariant.hkPrice}">- Rs.
          <fmt:formatNumber value="${product.maximumMRPProducVariant.hkPrice + hk:getPostpaidAmount(variant)}"
                            maxFractionDigits="0"/></span></c:if>
  </h5>
</s:layout-component>--%>

<s:layout-component name="user_reviews">
  <div class='products content' id="user_reviews">
  <hr style="color:#F0F0F0;border-style:hidden"/>

  <c:choose>
    <c:when test="${!empty pa.userReviews}">
      <table width="950" class="reviewLinksTable">
        <tr height="40">
          <td style="font-size:14px;font-weight:bold;border-style:none">Reviews of ${product.name}</td>
          <td style="border-style:none">
            <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" event="writeNewReview">
            <s:param name="product" value="${product.id}"/>
            <strong>Write a Review<strong>
              </s:link>
          </td>
        </tr>

        <tr height="50">
          <td colspan="2" style="border-style:none">Average Rating : <strong><fmt:formatNumber
              value="${pa.averageRating}"
              maxFractionDigits="1"/>/5</strong> <br/>
                                                    (based on ${pa.totalReviews} reviews) <br/>

              <div class="rating_bar">
                  <div id="blueStar" class="blueStar">
                      <script type="text/javascript">
                          var averageRating = ${pa.averageRating};
                          averageRating = (averageRating * 20) + "%";
                          $('.blueStar').width(averageRating);
                      </script>
                  </div>
              </div>
          </td>
        </tr>
      </table>

      <hr style="color:#F0F0F0;border-style:dotted"/>
      <c:if test="${pa.totalReviews > 5}">
        <strong>&nbsp;Showing 5 of ${pa.totalReviews} reviews</strong>
        <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction">
          <s:param name="product" value="${product.id}"/>
          <strong>(Read All Reviews)</strong>
        </s:link>
      </c:if>

      <c:forEach items="${pa.userReviews}" var="review" varStatus="ctr">

        <table width="950" class="reviewContentTable" style="border-style:none">
          <tr style="border-style:none">
            <td width="150" style="border-style:none"><strong>${review.postedBy.name}</strong>
                <%--<td style="border-style:none"><h4>${review.title}</h4></td>--%>
              <div class="rating_bar">
                <div class="blueStarRating${ctr.index}"></div>
                  <script type="text/javascript">
                  var index = ${ctr.index};
                  var rating = ${review.starRating};
                  rating = (rating * 20) + "%";
                  $('.blueStarRating' + index).width(rating);
                </script>
              </div>
            </td>
            <td style="border-style:none">
              <div style="word-wrap:break-word">
                  ${review.review}
              </div>
            </td>
          </tr>
          <tr style="border-style:none">
            <td colspan="2" style="border-style:none">
              <hr style="color:#F0F0F0;border-style:dotted"/>
            </td>
          </tr>
        </table>
        <br/>
      </c:forEach>
      <a class='go_to_top' href='#top'>go to top &uarr;</a>
      </div>
    </c:when>
    <c:otherwise>
      <table width="950" style="border:none">
        <tr height="40">
          <td colspan="2" style="font-size:14px;font-weight:bold;">No Reviews!!</td>
        </tr>
        <tr>
          <td>
            Be the first one to <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction"
                                        event="writeNewReview">
            <s:param name="product" value="${product.id}"/>
            <strong>Write a Review</strong>
          </s:link>
          </td>
        </tr>
        <tr>
          <td><a class='go_to_top' href='#top'>go to top &uarr;</a></td>
        </tr>
      </table>
    </c:otherwise>
  </c:choose>
</s:layout-component>

<s:layout-component name="endScripts">
  <script type="text/javascript">
    var validateCheckbox;
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
          $('#cart_error1').html(getErrorHtmlFromJsonResponse(res)).slowFade(3000, 2000);
        } else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
          window.location.replace(res.data.url);
        }
        $('.progressLoader').hide();
      }

    <c:if test="${pa.combo == null}">
      $('.addToCartButton').click(function(e) {
        if (!window.validateCheckbox) {
          $(this).parents().find('.progressLoader').show();
          $(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
          $(this).hide();
          e.stopPropagation();
        } else {
          var selected = 0;
          $('.checkbox').each(function() {
            if ($(this).attr("checked") == "checked") {
              selected = 1;
            }
          });
          if (!selected) {
          <c:choose>
          <c:when test="${product.primaryCategory == 'eye'}">
            $(this).parents().find('.checkboxError').html("Please select atleast one lens!");
          </c:when>
          <c:when test="${product.primaryCategory == 'beauty'}">
            $(this).parents().find('.checkboxError').html("Please select a shade!");
          </c:when>
          </c:choose>
            $('.checkboxError').fadeIn();
            return false;
          } else {
            $(this).parents().find('.progressLoader').show();
            $(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
            $(this).hide();
            e.stopPropagation();
            return true;
          }
        }
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

      $(document).click(function() {
        $('.checkboxError').fadeOut();
      });

      $('.checkboxError').hide();
    });
  </script>

		<c:if test="${not isSecure }">
			<iframe
				src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e300&pid=${product.id}&catid=${product.primaryCategory.name}&subcat1id=&subcat2id=&section=1&level=1"
				scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
				frameborder="0"></iframe>
		</c:if>

	</s:layout-component>
</s:layout-render>