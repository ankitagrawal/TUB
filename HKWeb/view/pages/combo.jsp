<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.marketing.TagConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
   response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    response.setHeader("pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="productBean"/>

<c:set var="productCombo" value="${productBean.product}"/>
<c:set var="seoData" value="${productBean.seoData}"/>
<s:layout-render name="/layouts/genericG.jsp" pageTitle="${seoData.title}" pageType="<%=TagConstants.PageType.COMBO%>">

<s:layout-component name="htmlHead">
    <meta name="keywords" content="${seoData.metaKeyword}"/>
    <meta name="description" content="${seoData.metaDescription}"/>
    <%-- The styling was getting overidden by that in new.dev.css
    To avoid so, it is written at the end of the page.--%>
</s:layout-component>

<s:layout-component name="topBanner">
    <div class='crumb_outer'>
        <a href="${pageContext.request.contextPath}/" class="crumb">Home</a>
        &gt;
        <a href="${pageContext.request.contextPath}/super-savers" class="crumb">Super Savers</a>

        <h1 class="title">${productBean.seoData.h1}</h1>
    </div>

    <c:if test="${productCombo.service}">
        <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment="${productBean.urlFragment}"/>
    </c:if>

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
                <s:param name="product" value="${productCombo}"/>
            </s:link>
            <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
                    event="editProductDetails" target="_blank" class="popup">
                Edit Product Attributes
                <s:param name="product" value="${productCombo}"/>
            </s:link>
            <s:link beanclass="com.hk.web.action.admin.sku.SkuAction" event="searchSKUs" target="_blank" class="popup">
                Edit Sku Attributes
                <s:param name="productId" value="${productCombo.id}"/>
            </s:link>
        </shiro:hasPermission>
    </div>
</s:layout-component>

<s:layout-component name="content">
<div style="text-align:center; margin:10px 0;">
    <hk:superSaverImage imageId="${productBean.superSaverImageId}" size="<%=EnumImageSize.Original%>"
                        alt="${productCombo.name}"/>
</div>

<div class="clear"></div>
<div style="margin-top:10px;"></div>

<div class="contentDiv">
<shiro:hasPermission name="<%=PermissionConstants.MANAGE_IMAGE%>">
    <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="pre" target="_blank"
                 class="popup"> Upload
        <s:param name="product" value="${productCombo.id}"/>
    </s:link>
        <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
                event="manageProductImages" target="_blank"
                class="popup">&nbsp;&nbsp;Manage
            Images
            <s:param name="productId" value="${productCombo.id}"/>
        </s:link>
    </div>
</shiro:hasPermission>
<shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
    <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction" event="pre" target="_blank"
            class="popup">&nbsp;&nbsp;Edit Combo
        <s:param name="combo" value="${productCombo.id}"/>
    </s:link>
</shiro:hasPermission>
<h2 class='prod_title'>
        ${productCombo.name}
</h2>

<div style="margin-top:5px;"></div>

<div class='infos' style="border-bottom:0;">
    <c:if test="${hk:isNotBlank(productCombo.brand)}">
          <span class='title'>
            Brand:
          </span>
          <span class='info'>
            <s:link beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" class="bl">
                ${productCombo.brand}
                <s:param name="brand" value="${fn:toLowerCase(productCombo.brand)}"/>
                <s:param name="topLevelCategory" value="${productBean.topCategoryUrlSlug}"/>
            </s:link>
          </span>
    </c:if>
    <c:if test="${hk:isNotBlank(productCombo.manufacturer)}">
        |
          <span class='title'>
            Manufacturer:
          </span>
          <span class='info'>
                  ${productCombo.manufacturer.name}
          </span>
    </c:if>
    |
    <span class='title'>
      Dispatched in:
    </span>
    <span class='info orange' title="Delivery time may vary depending on the location">
      <c:choose>
          <c:when test="${hk:isNotBlank(productCombo.maxDays)}">
              ${productCombo.minDays} - ${productCombo.maxDays} working days
          </c:when>
          <c:otherwise>
              <c:choose>
                  <c:when test="${productBean.topCategoryUrlSlug == 'beauty'}">
                      2 - 7 working days
                  </c:when>
                  <c:otherwise>
                      1 - 3 working days
                  </c:otherwise>
              </c:choose>
          </c:otherwise>
      </c:choose>
    </span>

    <div style="margin-top:5px;"></div>

    <c:if test="${!empty productBean.userReviews}">
        <div style="float:right;margin-right:5px;margin-bottom:3px;">
            <a class="top_link" href='#user_reviews' style="border-bottom:0;">
                    ${productBean.totalReviews} Reviews &darr;
            </a>

            <div class="rating_bar">
                <div class="blueStarTop" id="blueStarTop">
                    <script type="text/javascript">
                        var averageRating = ${productBean.averageRating};
                        averageRating = (averageRating * 20) + "%";
                        $('.blueStarTop').width(averageRating);
                    </script>
                </div>
            </div>
        </div>
    </c:if>

    <div class='top_links'>
        <c:if test="${!empty productCombo.description}">
            <a class='top_link' href='#description'>
                Description &darr;
            </a>
        </c:if>
        <c:if test="${!empty productCombo.productFeatures}">
            <a class='top_link' href='#features'>
                Technical Specs &darr;
            </a>
        </c:if>
        <c:if test="${!empty productBean.relatedCombos}">
            <a class='top_link' href='#related_combos' id="related_combo_link" style="font-weight:bold;">
                Special Offers &darr;
            </a>
        </c:if>
        <c:if test="${!empty productCombo.relatedProducts}">
            <a class='top_link' id="related_product_link" href='#related_products'>
                Related Products &darr;
            </a>
        </c:if>
    </div>

</div>

<div class="clear"></div>
<div style="margin-top:10px;"></div>

<div class="container_24 containerDiv">
    <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
        <c:set var="combo" value="${productBean.combo}"/>
        <s:hidden name="combo" value="${combo}"/>
        <s:hidden name="combo.qty" value="1"/>

        <c:set var="globalCtr" value="-1"/>
        <c:forEach items="${combo.comboProducts}" var="comboProduct" varStatus="productCtr">
            <c:set var="product" value="${comboProduct.product}"/>
            <c:set var="productQty" value="${comboProduct.qty}"/>

            <c:forEach var="qty" begin="0" end="${productQty - 1}" step="1" varStatus="qtyCtr">
                <div class="grid_24 productDiv">
                    <div class="grid_23 comboProduct">
                        <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction">
                            <s:param name="productId" value="${product.id}"/>
                            <s:param name="productSlug" value="${product.slug}"/>
                            <h5>${product.name}</h5>
                        </s:link>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-top:10px;"></div>

                    <c:choose>
                        <c:when test="${!empty comboProduct.allowedProductVariants}">
                            <c:set var="inStockVariants" value="${comboProduct.allowedProductVariants}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="inStockVariants" value="${comboProduct.product.inStockVariants}"/>
                        </c:otherwise>
                    </c:choose>

                    <div class="grid_23 optionsDiv">
                        <fieldset>
                            <legend>&nbsp;&nbsp;Available Options</legend>
                            <div class="parentAvail">
                                <c:forEach items="${inStockVariants}" var="variant" varStatus="variantCtr">
                                    <c:if test="${!variant.deleted}">
                                        <c:set var="globalCtr" value="${globalCtr + 1}"/>

                                        <c:choose>
                                            <c:when test="${fn:length(product.productVariants) == 1}">
                                                <c:set var="idForImage" value="${product.mainImageId}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="idForImage" value="${variant.mainImageId}"/>
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="grid_4 options">
                                            <div class="imageDiv">
                                                <c:choose>
                                                    <c:when test="${idForImage != null}">
                                                        <hk:productImage imageId="${idForImage}"
                                                                         size="<%=EnumImageSize.SmallSize%>"
                                                                         alt="${product.name}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/test/${variant.id}.jpg'
                                                             alt="${product.name}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="productOptions">
                                                <c:forEach items="${variant.productOptions}" var="option">
                                                    <p class="productOptions"
                                                       style="word-wrap:break-word;">${option.name}:${option.value}</p>
                                                </c:forEach>
                                            </div>
                                            <c:if test="${variant.outOfStock}">
                                                <div class="soldOut" style="font-style:italic; color:red;">
                                                    Sold Out
                                                </div>
                                            </c:if>
                                            <input type="hidden" class="variantIdValue" idx="${globalCtr}"
                                                   value="${variant.id}"/>
                                            <input type="hidden" class="selValue" idx="${globalCtr}" value="true"/>
                                            <input type="hidden" class="qtyValue" idx="${globalCtr}" value="0"/>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </fieldset>

                        <div style="float:right;">
                            <div class="grid_1 arrowDiv">
                                <img src="<hk:vhostImage/>/images/arrow/black.jpg" alt="chosen"
                                     style="height:20px; width:25px;"/>
                            </div>

                            <fieldset style="float:right;margin-right:10px;">
                                <legend>&nbsp;&nbsp;Selected Option</legend>
                                <div class="grid_4 result">
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:forEach>

        <div class="clear"></div>
        <div style="margin-top:15px;"></div>

        <div class="grid_24">
            <div class="grid_15 errorDiv"></div>

            <div class="grid_8 buyDiv">
                <div class="progressLoader"><img src="${pageContext.request.contextPath}/images/ajax-loader.gif"/></div>

                <div class="clear"></div>

                <div class="left_col" style="float:left;">
                    <div class='prices' style="font-size: 14px;">
                        <div class='cut' style="font-size: 14px;">
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                </span>
                        </div>
                        <div class='hk' style="font-size: 16px;">
                            Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
                        value="${combo.hkPrice}"
                        maxFractionDigits="0"/>
                </span>
                        </div>
                        <div class="special green" style="font-size: 14px;">
                            you save
              <span style="font-weight: bold;"><fmt:formatNumber
                      value="${combo.discountPercent*100}"
                      maxFractionDigits="0"/>%</span>
                        </div>
                    </div>
                </div>
                <div class="right_col">
                    <c:choose>
                        <c:when test="${!combo.outOfStock}">
                            <s:submit name="addToCart" value="Buy Now" class="addToCartButton cta"
                                      style="vertical-align:text-bottom;"/>
                        </c:when>
                        <c:otherwise>
                            <div align="center"><a class="button_orange" style="vertical-align:text-bottom;"><b>Sold
                                Out</b></a></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <div style="margin-top:10px;"></div>
    </s:form>
</div>

<div class="productDescription">
    <c:if test="${hk:isNotBlank(productCombo.overview)}">
        <h4>Overview</h4>

        <p class="overview">
                ${productCombo.overview}
        </p>
    </c:if>
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
        <div>
            <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editOverview"
                    class="popup">
                Edit Overview
                <s:param name="productId" value="${productCombo.id}"/>
            </s:link>
        </div>
    </shiro:hasPermission>
    <c:if test="${hk:isNotBlank(productCombo.description)}">
        <div class="content" id="description">
            <h4> Description </h4>

            <p> ${productCombo.description} </p>
        </div>
    </c:if>

    <c:if test="${productBean.addressDistanceDtos != null && fn:length(productBean.addressDistanceDtos) > 0}">
        <h3> Service Centres Available Near to You : <br>
            <ul>
                <c:forEach items="${productBean.addressDistanceDtos}" var="addressDto">
                    <li>  ${addressDto.localityMap.address.line1}, ${addressDto.localityMap.address.line2}<br></li>
                </c:forEach>
            </ul>
        </h3>
    </c:if>

    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
        <div>
            <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
                    event="editDescription"
                    class="popup">
                Edit Description
                <s:param name="productId" value="${productCombo.id}"/>
            </s:link>
        </div>
    </shiro:hasPermission>


    <c:if test="${!empty (productCombo.productFeatures)}">
        <div class="content" id="features">
            <h4>
                Product Features
            </h4>

            <table class="">
                <tbody>
                <c:forEach var="feature" items="${productCombo.productFeatures}">
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

        </div>
    </c:if>

    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_DESCRIPTIONS%>">
        <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" event="editFeatures"
                class="popup">
            Edit Features
            <s:param name="product" value="${productCombo}"/>
        </s:link>
    </shiro:hasPermission>
</div>

<div class="relatedProducts">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
        <div>
            <s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
                    event="editRelatedProducts" class="popup">
                Edit Related Products
                <s:param name="productId" value="${productCombo.id}"/>
            </s:link>
        </div>
    </shiro:hasPermission>
    <c:if test="${!empty productBean.relatedCombos}">
        <c:set var="check_related_combos" value="0"/>
        <div class='products content' id="related_combos">
            <h4>
                Special Offers on ${productCombo.name}
            </h4>
            <c:forEach items="${productBean.relatedCombos}" var="relatedCombo">
                <c:if test="${!relatedCombo.outOfStock and !relatedCombo.hidden and !relatedCombo.deleted and !relatedCombo.googleAdDisallowed}">
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="${relatedCombo.id}"/>
                    <c:set var="check_related_combos" value="1"/>
                </c:if>
            </c:forEach>

            <div class="floatfix"></div>
        </div>
         <c:if test="${hk:equalsIgnoreCase(check_related_combos,'0')}">
                 <script type="text/javascript">
                     $(document).ready(function(){
                        $("#related_combos").remove();
                         $("#related_combo_link").remove();
                     });
                 </script>
        </c:if>
    </c:if>
    <c:set var="relatedProducts" value="${productCombo.relatedProducts}"/>
    <c:if test="${!empty relatedProducts}">
        <c:set var="check_related_products" value="0"/>
        <div class='products content' id="related_products">
            <h4>
                People who bought this also bought these products
            </h4>

            <c:forEach items="${relatedProducts}" var="relatedProduct">
                <c:if test="${!relatedProduct.outOfStock and !relatedProduct.deleted and !relatedProduct.hidden and !relatedProduct.googleAdDisallowed}">
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" product="${relatedProduct}"/>
                    <c:set var="check_related_products" value="1"/>
                </c:if>
            </c:forEach>

            <div class="floatfix"></div>
            <a class='go_to_top' href='#top'>go to top &uarr;</a>

        </div>
           <c:if test="${hk:equalsIgnoreCase(check_related_products,'0')}">
          <script type="text/javascript">
              $(document).ready(function(){
                 $("#related_products").remove();
                  $("#related_product_link").remove();
              });
          </script>
        </c:if>
    </c:if>
</div>

<div class="userReviews">
    <div class='products content' id="user_reviews">
        <hr style="color:#F0F0F0;border-style:hidden"/>

        <c:choose>
        <c:when test="${!empty productBean.userReviews}">
        <table width="950" class="reviewLinksTable">
            <tr height="40">
                <td style="font-size:14px;font-weight:bold;border-style:none">Reviews of ${productCombo.name}</td>
                <td style="border-style:none">
                    <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction"
                            event="writeNewReview">
                    <s:param name="product" value="${productCombo.id}"/>
                    <strong>Write a Review<strong>
                        </s:link>
                </td>
            </tr>

            <tr height="50">
                <td colspan="2" style="border-style:none">
                    Average Rating :
                    <strong><fmt:formatNumber value="${productBean.averageRating}" maxFractionDigits="1"/>/5</strong>
                    <br/>
                    (based on ${productBean.totalReviews} reviews)
                    <br/>

                    <div class="rating_bar">
                        <div id="blueStar" class="blueStar">
                            <script type="text/javascript">
                                var averageRating = ${productBean.averageRating};
                                averageRating = (averageRating * 20) + "%";
                                $('.blueStar').width(averageRating);
                            </script>
                        </div>
                    </div>
                </td>
            </tr>
        </table>

        <hr style="color:#F0F0F0;border-style:dotted"/>
        <c:if test="${productBean.totalReviews > 5}">
            <strong>&nbsp;Showing 5 of ${productBean.totalReviews} reviews</strong>
            <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction">
                <s:param name="product" value="${productCombo.id}"/>
                <strong>(Read All Reviews)</strong>
            </s:link>
        </c:if>

        <c:forEach items="${productBean.userReviews}" var="review" varStatus="ctr">

            <table width="950" class="reviewContentTable" style="border-style:none">
                <tr style="border-style:none">
                    <td width="150" style="border-style:none">
                        <strong>${review.postedBy.name}</strong>

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
                    <s:param name="product" value="${productCombo.id}"/>
                    <strong>Write a Review</strong>
                </s:link>
                </td>
            </tr>
            <tr>
            </tr>
        </table>
    </c:otherwise>
    </c:choose>
</div>

<div class="goToTop" style="float:right;text-align:right; padding:5px;margin:15px;">
    <a class='go_to_top' href='#top'>go to top &uarr;</a>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        //        $('.options').each(function selectFirst() {
        //            var resultDiv = $(this).parents('.optionsDiv').find('.result');
        //            resultDiv.html($(this).html());
        //            var idx = resultDiv.find('.variantIdValue').attr("idx");
        //            resultDiv.find('.variantIdValue').attr("name", "productVariantList[" + idx + "]");
        //            resultDiv.find('.selValue').attr("name", "productVariantList[" + idx + "].selected");
        //            resultDiv.find('.qtyValue').attr("name", "productVariantList[" + idx + "].qty");
        //
        //            resultDiv.find('.qtyValue').val("1");
        //        });


        $('.progressLoader').hide();

        $('.comboProduct').css({
            marginLeft:($('.grid_24').width() - $('.comboProduct').outerWidth()) / 2
        });

        $('.right_col').css({
            height:$('.left_col').height()
        });

        $('.optionsDiv').css({
            marginLeft:($('.grid_24').width() - $('.optionsDiv').outerWidth()) / 2
        });

        $('.options').click(function() {
            if ($(this).find('div.soldOut').size() == 0) {
                var resultDiv = $(this).parents('.optionsDiv').find('.result');
                resultDiv.html($(this).html());

                var idx = resultDiv.find('.variantIdValue').attr("idx");
                resultDiv.find('.variantIdValue').attr("name", "productVariantList[" + idx + "]");
                resultDiv.find('.selValue').attr("name", "productVariantList[" + idx + "].selected");
                resultDiv.find('.qtyValue').attr("name", "productVariantList[" + idx + "].qty");

                resultDiv.find('.qtyValue').val("1");
            }
        });

        function _addToCart(res) {
            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
                $('#productsInCart').html(res.data.itemsInCart);
                $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
                $('.progressLoader').hide();
                $('.addToCartButton').remove();
                $(".right_col").append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
                show_message();
            } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                alert(res.message);
                $('.progressLoader').hide();
                location.reload();
            }
        }

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

        $('.addToCartButton').click(function() {
            $('.progressLoader').show();
            /*var isResultEmpty = 0 ;
             $('.result').each(function() {
             if ($(this).find('img').size() == 0) {
             isResultEmpty = 1;
             $('.errorDiv').html('Please select one of the options available for all the products in the combo!');
             $('.errorDiv').css({
             paddingTop:$('.left_col').height() / 3
             });
             $('.errorDiv').show();
             return false;
             }
             });
             return !isResultEmpty;    */

            var selectedVariants = $('[name^= "productVariantList"][name$="\\]"]');
            var variantIds = new Array();
            for (var i = 0; i < selectedVariants.size(); i++) {
                variantIds.push(selectedVariants[i].value);
            }

            $('[name^= "productVariantList"]').attr("name", "abandoned");

            var variantCtr = -1;

            for (var j = 0; j < variantIds.length; j++) {
                variantCtr++;
                var target = variantIds[j];
                var qty = $.grep(variantIds, function (elem) {
                    return elem === target;
                }).length;

                $('<input class="variantIdValue" type="hidden" value="' + target + '" idx="' + variantCtr +
                  '" name="productVariantList[' + variantCtr + ']">').appendTo('.addToCartForm');

                $('<input class="selValue" type="hidden" value="true" idx="' + variantCtr +
                  '" name="productVariantList[' + variantCtr + '].selected">').appendTo('.addToCartForm');

                $('<input class="qtyValue" type="hidden" value="' + qty + '" idx="' + variantCtr +
                  '" name="productVariantList[' + variantCtr + '].qty">').appendTo('.addToCartForm');

                if (qty > 1) {
                    j = j + qty - 1;
                }
            }
            return true;
        });

        $('.optionsDiv').each(function() {
            var availableOptions = $(this).find('.options');
            var image = $(this).find('.imageDiv');
            var arrow = $(this).find('.arrowDiv');

            var optionsHeight = new Array();
            $(this).find('div.productOptions').each(function() {
                optionsHeight.push($(this).height());
            });

            var soldOutHeight = $('.soldOut').height() == null ? 0 : $('.soldOut').height();

            var maxHtOfOptions = $(image).height() + Math.max.apply(Math, optionsHeight) + soldOutHeight;
            $(availableOptions).css({
                height: maxHtOfOptions
            });

            $(arrow).css({
                paddingTop:10 + maxHtOfOptions / 2
            });

            var resultDiv = $(this).find('.result');
            $(resultDiv).css({
                height: maxHtOfOptions
            });

            $(availableOptions).each(function() {
                if ($(this).find('.soldOut').size() == 0) {
                    resultDiv.html($(this).html());
                    var idx = resultDiv.find('.variantIdValue').attr("idx");
                    resultDiv.find('.variantIdValue').attr("name", "productVariantList[" + idx + "]");
                    resultDiv.find('.selValue').attr("name", "productVariantList[" + idx + "].selected");
                    resultDiv.find('.qtyValue').attr("name", "productVariantList[" + idx + "].qty");

                    resultDiv.find('.qtyValue').val("1");
                    return false;
                }
            });
        });
    });
</script>
<style type="text/css">
    div.comboProduct {
        text-align: left;
        background: none repeat scroll 0 0 #EEEEEE;
        padding: 5px;
    }

    div.result {
        border: 2px solid darkgray;
        border-radius: 0.5em;
        background: #EEEEEE;
        padding: 10px 0;
        float: none;
        display: inline-block;
        margin-bottom: 10px;
        margin-top: 10px;
        text-align:center;
    }

    div.arrowDiv {
        text-align: center;
        height: 20px;
        width: 25px;
        opacity: 0.4;
    }

    fieldset {
        border: 1px solid gainsboro;
        float: left;
        padding: 5px;
        border-radius: 0.5em;
    }

    div.productDiv {
        text-align: center;
        margin: 10px 0;
    }

    div.containerDiv {
        text-align: center;
    }

    div.contentDiv {
        margin: 10px 5px;
    }

    div.options {
        border: 1px solid darkgray;
        border-radius: 0.5em;
        background: #EEEEEE;
        padding: 10px 0;
        float: none;
        display: inline-block;
        margin-bottom: 10px;
        margin-top: 10px;
        text-align:center;

        -webkit-transition-duration: 0.2s;
        -moz-transition-duration: 0.2s;
        transition-duration: 0.2s;
    }

    div.options:hover {
        border: solid 1px #CCC;
        -moz-box-shadow: 1px 1px 5px #999;
        -webkit-box-shadow: 1px 1px 5px #999;
        box-shadow: 1px 1px 5px #999;
        -webkit-transform: scale(1.05);
        -moz-transform: scale(1.05);
        position: relative;
        z-index: 1;
    }

    div.options img {
        -webkit-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
        -moz-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
    }

    div.result img {
        -webkit-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
        -moz-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
    }

    div.imageDiv {
        min-width: 130px;
        min-height: 130px;
        cursor: pointer;
    }

    div.buyDiv {
        font-size: 1em;
        text-align: right;
        float: right;
    }

    div.buyDiv div.right_col {
        text-align: right;
        float: right;
    }

    p.productOptions {
        padding: 0;
        margin: 0;
        text-align: center;
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

    div.rating_bar {
        width: 80px;
        background: url('${pageContext.request.contextPath}/images/img/star-off.png') 0 0 repeat-x;
    }

    div.rating_bar div {
        height: 16px;
        background: url('${pageContext.request.contextPath}/images/img/star-on.png') 0 0 repeat-x;
    }

    div.special, div.hk, div.cut {
        margin: 5px;
    }

    div.errorDiv {
        margin: 10px;
        padding: 5px;
        display: none;
        color: #E80000;
        float: left;
        font-size: 1.05em;
    }

    div.parentAvail {
        width: 670px;
        overflow-x: auto;
        overflow-y: hidden;
        white-space: nowrap;
        text-align: left;
    }
</style>
</s:layout-component>
</s:layout-render>