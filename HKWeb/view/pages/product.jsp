<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.service.catalog.ProductService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="com.hk.cache.CategoryCache" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
 <s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa" event="pre"/>
 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    response.setHeader("pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    CategoryDao categoryDao = ServiceLocatorFactory.getService(CategoryDao.class);
    Category eyeGlass = categoryDao.getCategoryByName("eyeglasses");
    ProductService productService = ServiceLocatorFactory.getService(ProductService.class);
    pageContext.setAttribute("productService", productService);
    pageContext.setAttribute("eyeGlass", eyeGlass);

    boolean isSecure = WebContext.isSecure();
    pageContext.setAttribute("isSecure", isSecure);
    Category stethoscope = categoryDao.getCategoryByName("stethoscope");
    pageContext.setAttribute("stethoscope", stethoscope);

    String gosf = request.getParameter("gosf");
    pageContext.setAttribute("gosf", gosf);
%>
 <c:set var="product" value="${pa.product}"/>
 <c:set var="seoData" value="${pa.seoData}"/>
 <c:set var="subscriptionProduct" value="${pa.subscriptionProduct}"/>
 <s:layout-render name="/layouts/productLayout.jsp" pageTitle="${seoData.title}" isOutOfStockPage="${pa.outOfStockPage}">
<%--<s:layout-render name="/layouts/default.jsp" pageTitle="${seoData.title}">--%>


<s:layout-component name="htmlHead">

	<meta name="keywords" content="${seoData.metaKeyword}"/>
	<meta name="description" content="${seoData.metaDescription}"/>
	<style type="text/css">
		.progressLoader {
			display: none;
		}

		ul.thumblist {
			display: block;
		}

		ul.thumblist li {
			float: left;
			margin-right: 2px;
			list-style: none;
		}

		ul.thumblist li a {
			display: block;
			border: 1px solid #CCC;
		}

		ul.thumblist li a.zoomThumbActive {
			border: 1px solid red;
		}

		.jqzoom {

			text-decoration: none;
			float: left;
		}

        #mycarousel {
            display:none;
        }

		.rating_bar {
			width: 80px;
			background: url('${pageContext.request.contextPath}/images/img/star-off.png') 0 0 repeat-x;
		}

		.rating_bar div {
			height: 16px;
			background: url('${pageContext.request.contextPath}/images/img/star-on.png') 0 0 repeat-x;
		}

		.frameSize td {
			border: 1px solid #DDD;
			padding: 5px;
		}
	</style>

	<link href="${pageContext.request.contextPath}/css/jquery.jqzoom.css" rel="stylesheet" type="text/css"/>	
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.jqzoom-core.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.jcarousel.min.js"></script>

    <c:if test="${!empty pa.productReferrerId}">
			<link rel="canonical" href="${hk:getProductURL(product,null)}">
	</c:if>

	<script type="text/javascript">
		$(document).ready(function () {
			// stepper not working for some reason for form submit. field is not getting picked up for post request.
			//    $('.lineItemQty').jqStepper(1);
			$('.popup').popupWindow({
				height:600,
				width:900
			});

			//$("#frameChart").hide();

			$("#frameChartVM").click(function showVM() {
				$("#frameChart").show();
			});

			$("#sizeGuide").click(function toggleVM() {
				$("#frameChart").toggle();
			});
			
			 $("#similarProducts").click(function toggleVM(){
           		 $("#similarProductsVM").toggle();
        	});

			//Click and change image
			$('.color_box').click(function () {
				var variantMainImageId = $(this).find('.variantMainImageId').val();
				//var url = "http://hk-prod.s3.amazonaws.com/1/"+variantMainImageId+"_t.jpg";
				//$(".img320").html("<img class='icon' src='"+url+"'/>") ;
				$.getJSON($('#updateProductVariantImageLink').attr('href'), {mainProductImageId:variantMainImageId}, function (resurl) {
					//                alert(resurl.data);
					$(".img320").html("<img class='icon' src='" + resurl.data + "'/>");
				});
			});

			$('.jqzoom').jqzoom({
				zoomType:'standard',
                zoomWidth:400,
                zoomHeight:400,
				lens:true,
				preloadImages:false,
				alwaysOn:false
			});

            $('#mycarousel').css('display', 'block');

            jQuery(document).ready(function() {
                jQuery('#mycarousel').jcarousel();
            });

            $('#notifyMeWindow').jqm({trigger:'.notifyMe', ajax:'@href'});

		});

	</script>

  <c:if test="${product.primaryCategory.name == 'eye'}">
    <script src="http://resources.flixstock.com/flixstock.js"></script>
  </c:if>

</s:layout-component>

<s:layout-component name="modal">


	<c:if test="${!empty product.productVariants[0].productExtraOptions}">
		<s:layout-render name="/pages/modal/productWithExtraOptions.jsp" product="${product}"/>
	</c:if>
	<%--<c:if test="${hk:collectionContains(product.categories, eyeGlass)}">--%>
	<%--<s:layout-render name="/pages/modal/eyeGlasses.jsp" product="${product}"/>--%>
	<%--</c:if>--%>

		<shiro:hasPermission name="<%=PermissionConstants.GET_PRODUCT_LINK%>">
			<s:layout-render name="/pages/modal/productLink.jsp" product="${product}"
			                 affiliateId="${pa.affiliate.id}"/>

			<s:layout-render name="/pages/modal/productBannerLink.jsp" product="${product}" combo="${pa.combo}"
			                 affiliateId="${pa.affiliate.id}"/>
		</shiro:hasPermission>

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
<s:layout-component name="urlFragment">${pa.menuNodeUrlFragment}</s:layout-component>

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
		<shiro:hasPermission name="<%=PermissionConstants.VIEW_VARIANT_INFO%>">
			<s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
			        event="editProductVariantDetails"
			        target="_blank" class="popup">
				Edit Variant Attributes
				<s:param name="product" value="${product}"/>
			</s:link>
        </shiro:hasPermission>
        <shiro:hasPermission name="<%=PermissionConstants.VIEW_PRODUCT_INFO%>">
			<s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
			        event="editProductDetails"
			        target="_blank"
			        class="popup">
				Edit Product Attributes
				<s:param name="product" value="${product}"/>
			</s:link>
        </shiro:hasPermission>
        <shiro:hasPermission name="<%=PermissionConstants.VIEW_SKU_INFO%>">
			<s:link beanclass="com.hk.web.action.admin.sku.SkuAction" event="searchSKUs" target="_blank" class="popup">
				Edit Sku Attributes
				<s:param name="productId" value="${product.id}"/>
			</s:link>
		</shiro:hasPermission>
	</div>

</s:layout-component>

<s:layout-component name="prod_slideshow">
	<div class='product_slideshow'>

		<div class="img320" style="position:relative;">
			<a href="${hk:getS3ImageUrl(imageLargeSize, product.mainImageId)}" class="jqzoom" rel='gal1'
			   title="${product.name}">
				<img itemprop="image" src="${hk:getS3ImageUrl(imageMediumSize, product.mainImageId)}" alt="${product.name}"
				     title="${product.name}">
				<c:if test="${gosf == 'true'}">
					<img style="position:absolute;right:0px;bottom:0px;z-index:100" class="gosf-logo"
					     src="${pageContext.request.contextPath}/images/GOSF/gosf-price.jpg"/>
				</c:if>
			</a>
		</div>
        <%--<div id="tryOnLink" class="content">
            <c:if test="${pa.validTryOnProductVariant != null}">
                <a href="${hk:getTryOnImageURL(pa.validTryOnProductVariant)}"><img src="${pageContext.request.contextPath}/images/try-it-now.jpg" alt="Virtual Try On"></a>
            </c:if>
        </div>--%>

        <div>
			<c:if test="${fn:length(pa.productImages) > 1 && !pa.product.productHaveColorOptions}">
				<%--<ul class="thumblist">--%>
				<ul id="mycarousel" class="jcarousel-skin-tango">
					<c:forEach items="${pa.productImages}" var="productImage">
						<li><a href='javascript:void(0);' rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, productImage.id)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, productImage.id)}'}">
              <img itemprop="image" style="height:75px;" src='${hk:getS3ImageUrl(imageSmallSizeCorousal, productImage.id)}'></a>
            </li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<div class="clear"></div>
		<div style="padding-top: 15px">
			<shiro:hasPermission name="<%=PermissionConstants.GET_PRODUCT_LINK%>">
			 	 <a name="showProductLink" class="linkbutton"
				   onclick="$('#getProductLinkWindow').jqm(); $('#getProductLinkWindow').jqmShow();"
				   style="cursor:pointer">Get
					Links</a>
				 <a name="showProductLink" class="linkbutton"
				   onclick="$('#getBannerLinkWindow').jqm(); $('#getBannerLinkWindow').jqmShow();"
				   style="cursor:pointer">Get
					Banners</a>
			</shiro:hasPermission>
		</div>
		<div class="clear"></div>
		<div><shiro:hasPermission name="<%=PermissionConstants.MANAGE_IMAGE%>">
			<s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="pre" target="_blank"
			        class="popup"> Upload
				<s:param name="product" value="${product.id}"/>
			</s:link>
			<s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
			        event="manageProductImages" target="_blank"
			        class="popup">Manage
				Images
				<s:param name="productId" value="${product.id}"/>
			</s:link>
		</shiro:hasPermission>
		</div>
		<c:if test="${product.videoEmbedCode != null}">
			<s:layout-render name="/layouts/embed/_productVideo.jsp" productId="${product.id}"/>
		</c:if>
	</div>

</s:layout-component>

<s:layout-component name="product_detail_links">
	<h2 class='prod_title' itemprop="name">
			${product.name}
	</h2>

	<div class='infos' style="border-bottom:0px;">
		<c:if test="${hk:isNotBlank(product.brand)}">
          <span class='title'>
            Brand:
          </span>
          <span class='info' itemprop="brand">
            <s:link beanclass="com.hk.web.action.core.catalog.BrandCatalogAction" class="bl">
	            ${product.brand}
	            <s:param name="brand" value="${fn:toLowerCase(product.brand)}"/>
	            <s:param name="topLevelCategory" value="${product.primaryCategory.name}"/>
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
		<c:choose>
			<c:when test="${product.codAllowed != null && !product.codAllowed}">
				<span style="color:red;font-weight:bold;">COD Not Available</span>
			</c:when>
			<c:otherwise>
				<span style="color:green;font-weight:bold;">COD Available</span>
			</c:otherwise>
		</c:choose>  		
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
			<a class='top_link' href='#related_combos' id="related_combo_link" style="font-weight:bold;">
				Special Offers &darr;
			</a>
		</c:if>
		<c:if test="${!empty product.relatedProducts}">
			<a class='top_link' id="related_product_link" href='#related_products'>
				Recommended Products &darr;
			</a>
		</c:if>
<%--
        <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_ADMINS%>">
            <div id="tryOnLink" class="content">
                <c:if test="${pa.validTryOnProductVariant != null}">
                    <a href="${hk:getTryOnImageURL(pa.validTryOnProductVariant)}" style="float:right;color:black;font-size:1.2em;background: #DDD;border:1px solid black;padding:5px;"> TRY IT NOW </a>
                </c:if>
            </div>
        </shiro:hasAnyRoles>
--%>
	</div>
	<c:if test="${!empty subscriptionProduct}">
		<div class="jqmWindow" style="display:none;" id="subscriptionWindow"></div>

		<script type="text/javascript">
			$(document).ready(function () {
				$('#subscriptionWindow').jqm({trigger:'.addSubscriptionButton', ajax:'@href', ajaxText:'<br/><div style="text-align: center;">loading... please wait..</div> <br/>'});
				$('.addSubscriptionButton').mouseover(function () {
					var top = $(this).offset().top - $('#subscription-tooltip').outerHeight() - 20;
					var left = $(this).offset().left - $('#subscription-tooltip').outerWidth() / 2 + $(this).outerWidth() / 2;

					$('#subscription-tooltip').css({
						'top':top,
						'left':left
					});
					$('#subscription-tooltip').fadeIn();
				});
				var timer;
				$('.addSubscriptionButton').mouseleave(function () {
					timer = setTimeout(function () {
						$('#subscription-tooltip').fadeOut();
					}, 200);
				});
				$('.hk-tooltip').mouseover(function () {
					clearTimeout(timer);
				});
				$('.hk-tooltip').mouseleave(function () {
					$('#subscription-tooltip').fadeOut();
				});
			});

		</script>
		<div class="hk-tooltip" style="display: none;" id="subscription-tooltip">
			Subscribe and save <fmt:formatNumber value="${subscriptionProduct.subscriptionDiscount180Days}"
			                                     maxFractionDigits="2"/> to <fmt:formatNumber
				value="${subscriptionProduct.subscriptionDiscount360Days}" maxFractionDigits="2"/> &#37; extra.
			Save money and time.
			<br/><br/>
			<s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="pre"
			        target="_blank">(click here) </s:link> to know more..
			<div class="pointer">
				<div class="inner-pointer"></div>
			</div>
		</div>
	</c:if>
	<input type="hidden" id="productReferrerId" value="${pa.productReferrerId}"/>
	<input type="hidden" id="productPosition" value="${pa.productPosition}"/>
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

	<div class="configOptionsDiv" style="display:none;">
		<select name="configOptionValueMap" id="configOptionValueMap" multiple="multiple">
			<c:forEach items="${product.productVariants[0].variantConfig.variantConfigOptions}" var="configOption"
			           varStatus="configOptionCtr">
				<c:forEach items="${configOption.variantConfigValues}" var="configValue" varStatus="configValueCtr">
					<option value="${configOption.id}" valueId="${configValue.id}"></option>
				</c:forEach>
			</c:forEach>
			<option value="-999" valueId="-999" selected="selected"></option>
		</select>
	</div>
	<c:set var="ENG" value="ENG"/>
	<s:form partial="true" beanclass="com.hk.web.action.core.cart.AddToCartAction" method="get">
		<c:if test="${hk:isCollectionContainsObject(product.categories, stethoscope)}">
			<c:if test="${hk:isEngravingProvidedForProduct(product.productVariants[0])}">

				<c:forEach items="${product.productVariants[0].variantConfig.variantConfigOptions}" var="configOption">
					<c:if test="${configOption.additionalParam eq ENG}">
						<input type="hidden" id="stethoscopeConfigOption" value="${configOption.id}"/>
						<c:forEach items="${configOption.variantConfigValues}" var="configValue">
							<input type="hidden" id="stethoscopeConfigValue" value="${configValue.id}"/>
						</c:forEach>
					</c:if>
				</c:forEach>

				<input type="hidden" id="engravingPrice" value="${hk:getEngravingPrice(product.productVariants[0])}"/>
				<br>
				<strong>Do you want to Engrave: </strong>
				<s:checkbox name="engravingRequired" id="checkBoxEngraving" checked="false"/> (at an additional cost of
				Rs <fmt:formatNumber value="${hk:getEngravingPrice(product.productVariants[0])}"
				                     maxFractionDigits="0"/> )
				<div class="engraveDiv" style="display:none;">
					<br><strong>Specify name to be Engraved:</strong>
					<s:text name="nameToBeEngraved" id="engrave" style="width:140px" placeholder='Max 15 characters'
					        maxlength="15"/>
				</div>
			</c:if>
		</c:if>
	</s:form>

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
							<c:choose>
								<c:when test="${empty product.inStockVariants && !empty product.similarProducts}">
									<s:layout-render name="/layouts/embed/_hkSimilarProducts.jsp" product="${product}"/>
								</c:when>
								<c:otherwise>
									<%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>--%>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<s:layout-render name="/layouts/embed/_productWithMultipleVariantsWithColorOptions.jsp"
							                 product="${product}"/>
							<%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>--%>
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
							<s:layout-render name="/layouts/embed/_productWithSingleVariant.jsp" product="${product}"
							                 subscriptionProduct="${subscriptionProduct}"/>							
							<c:choose>
								<c:when test="${empty product.inStockVariants && !empty product.similarProducts}">
									<s:layout-render name="/layouts/embed/_hkSimilarProducts.jsp" product="${product}"/>
								</c:when>
								<c:otherwise>
									<%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>--%>
								</c:otherwise>
							</c:choose>

						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<s:layout-render name="/layouts/embed/_productWithExtraOptions.jsp" product="${product}"/>
			<%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForMultiVariants.jsp"/>--%>
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
         <c:set var="check_related_combos" value="0"/>
		<div class='products content' id="related_combos">
			<h4>
				Special Offers on ${product.name}
			</h4>
			<c:forEach items="${pa.relatedCombos}" var="relatedCombo">
                <c:if test="${!relatedCombo.outOfStock and !relatedCombo.deleted and !relatedCombo.hidden and !relatedCombo.googleAdDisallowed}">
				<s:layout-render name="/layouts/embed/_productVOThumbG.jsp" productId="${relatedCombo.id}"/>
                <c:set var="check_related_combos" value="1"/>
                </c:if>
			</c:forEach>

			<div class="floatfix"></div>
			<a class='go_to_top' href='#top'>go to top &uarr;</a>
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
	<c:if test="${hk:collectionContains(product.categories, eyeGlass)}">

			<c:if test="${!empty product.similarProducts}">
				<div class="content" id="similarProducts"
					style="background-color: #F2F2F2; padding: 5px; cursor: pointer; font-weight: bold; text-align: left;">
				Colors options</div>
				<div id="similarProductsVM" style="margin-top: 5px;">
				<table width="900px;" style="margin-left: 10px; margin-right: 10px;">
					<tr>
						<c:forEach items="${product.similarProducts}" var="similarProduct">
							<td width="160px">
							<div class="relatedGlass" style="float: left; margin-left: 3px;">
							<a href="${hk:getProductURL(similarProduct.similarProduct,null)}"
								style="text-decoration: none; cursor: pointer; border-bottom: none;">
							<hk:productImage
								imageId="${similarProduct.similarProduct.mainImageId}"
								size="<%=EnumImageSize.SmallSize%>"
								alt="${similarProduct.similarProduct.name}" />

							<p class="productOptions">${similarProduct.relationShip}</p>
							</a></div>
							</td>
						</c:forEach>
					</tr>
				</table>
				</div>
			</c:if>

			<div id="sizeGuide"
		     class="content"
		     style="background-color:#F2F2F2;padding:5px; cursor:pointer;font-weight:bold;text-align:left;">
			Size Guide
		</div>
		<div id="frameChart">
			<table width="900px;">
				<tr>
					<td>
						<img src="${pageContext.request.contextPath}/images/banners/frame_chart_new_1.jpg"/>
					</td>
					<td valign="top" class="content"
						style="vertical-align: top; padding-top: 5px;"><span
						style="font-weight: bold">How to choose the frame that fits
					you?</span><br />
					Frames are typically of 3 sizes – <span style="font-weight: bold">Large,
					Medium and Small.</span> 90% of adult population use medium sized
					frames. What you need to look at is at the measurement indicated on
					the frame. So for example the measurement mentioned in your current
					frame is <span style="font-weight: bold">52-16-130.</span> The
					first figure, 52, is the Eye Size of the frame. This is also
					indicated in the diagram on the side. The table below gives the
					size range for the different types of frames. <br />
					<br />
					<div align="center">
					<table style="text-align: center">
						<tr>
							<td><span style="font-weight: bold">Frame Type</span></td>
							<td><span style="font-weight: bold">Dimension Range</span></td>
						</tr>
						<tr>
							<td>Small</td>
							<td style="text-align: center;">40-48 mm</td>
						</tr>
						<tr>
							<td>Medium</td>
							<td style="text-align: center;">49-54 mm</td>
						</tr>
						<tr>
							<td>Large</td>
							<td style="text-align: center;">55-58 mm</td>
						</tr>
						<tr>
							<td>Extra Large</td>
							<td style="text-align: center;">58 mm and above</td>
						</tr>
					</table>
					</div>
					<a target="_blank"
						href="${pageContext.request.contextPath}/pages/lp/eye_glasses/choosing-eye-glasses.html">
					Read More.....</a></td>
				</tr>
			</table>

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
			<s:link beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction"
			        event="editDescription"
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
				Edit Recommended Products
				<s:param name="productId" value="${product.id}"/>
			</s:link>
		</div>
	</shiro:hasPermission>

	<c:set var="relatedProducts" value="${product.relatedProducts}"/>
	<c:if test="${!empty relatedProducts}">
        <c:set var="check_related_products" value="0"/>
		<div class='products content' id="related_products">
			<h4>
				People who bought this also bought these products
			</h4>

			<c:forEach items="${relatedProducts}" var="relatedProduct" begin="0" end="5">
                 <c:if test="${!relatedProduct.outOfStock and !relatedProduct.deleted and !relatedProduct.hidden and !relatedProduct.googleAdDisallowed}">
				<s:layout-render name="/layouts/embed/_productVOThumbG.jsp" product="${relatedProduct}"/>
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
	<div class='products content' id="user_reviews" itemscope itemtype="http://data-vocabulary.org/Review-aggregate">

	<hr style="color:#F0F0F0;border-style:hidden"/>

	<c:choose>
		<c:when test="${!empty pa.userReviews}">
			<table width="950" class="reviewLinksTable">
				<tr height="40">
					<td style="font-size:14px;font-weight:bold;border-style:none">Reviews of <span itemprop="itemreviewed">${product.name}</span></td>
					<td style="border-style:none">
						<s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" event="writeNewReview">
							<s:param name="product" value="${product.id}"/>
							<strong>Write a Review</strong>
						</s:link>
					</td>
				</tr>

				<tr height="50">
					<td colspan="2" style="border-style:none"><span itemprop="rating" itemscope itemtype="http://data-vocabulary.org/Rating">Average Rating : <strong>
            <span itemprop="rating"><fmt:formatNumber value="${pa.averageRating}" maxFractionDigits="1"/></span>/<span itemprop="best">5</span></strong> </span><br/>
						(based on <span itemprop="count">${pa.totalReviews}</span> reviews) <br/>

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
						Be the first one to <s:link
							beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction"
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
		$(document).ready(function () {
           
			var params = {};
			params.productReferrerId = $('#productReferrerId').val();
			function _addToCart(res) {
				if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
					$('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
					$('#productsInCart').html(res.data.itemsInCart);
					if(res.data.itemsInCart > 0){
						$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart.png");
					}else{
						$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart_empty.png");
					}
					$('.progressLoader').hide();

					show_message();
                    $('#gulal').show();
				}
                else if(res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                   alert(res.message);
                    location.reload();
                }

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
			$('.addToCartButton').click(function (e) {
				if ($("#checkBoxEngraving").is(":checked")) {
					if ($.trim($("#engrave").val()) == '') {
						alert("Please specify name to be engraved, or uncheck the engraving option");
						$('.progressLoader').hide();
						return false;
					}
				}

				var data = new Array();
				var idx = 0;
				var variantConfigProvided = 'false';
				$("#configOptionValueMap option:selected").each(function () {
					if ($(this).val() != -999) {
						variantConfigProvided = 'true';
						var dto = new Object();
						dto.optionId = $(this).val();
						dto.valueId = $(this).attr('valueId');
						data[idx] = dto;
						idx++;
					}
				});
				params.variantConfigProvided = (variantConfigProvided == 'true');
				var configValues = JSON.stringify(data);
				params.jsonConfigValues = configValues;
				params.nameToBeEngraved = $("#engrave").val();

				if (!window.validateCheckbox) {

					$(this).parents().find('.progressLoader').show();
					$(this).parent().append('<span class="add_message">added to <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link"><img class="icon16" src="${pageContext.request.contextPath}/images/icons/cart.png"> cart</s:link></span>');
					$(this).hide();
					e.stopPropagation();

				} else {
					var selected = 0;
					$('.checkbox').each(function () {
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
				/*$('.addToCartForm').ajaxForm({dataType: 'json', data: params, success: _addToCart});*/
			});
			</c:if>

			$(".message .close").click(function () {
				hide_message();
			});
			$(document).click(function () {
				hide_message();
			});

			function hide_message() {
				$('.message').animate({
					top:'-170px',
					opacity:0
				}, 100);
			}

			function show_message() {
				$('.message').css("top", "50px");
				$('.message').animate({
					opacity:1
				}, 500);
			}

			$('.addToCartForm').ajaxForm({dataType:'json', data:params, success:_addToCart});

			$(".top_link, .go_to_top").click(function (event) {
				event.preventDefault();
				$('html,body').animate({scrollTop:($(this.hash).offset().top - 45)}, 300);
			});

			if ($("#productBannerTextArea").length > 0) {
				$('#productBannerTextArea').val($('#productBannerTextArea').val().replace(/\s+/g, " "));
			}
			$(document).click(function () {
				$('.checkboxError').fadeOut();
			});

			$('.checkboxError').hide();
			$("#checkBoxEngraving").click(function () {
				var stethoscopeConfigOption = $("#stethoscopeConfigOption").val();
				if ($("#checkBoxEngraving").is(":checked")) {
					$('#configOptionValueMap option[value=' + stethoscopeConfigOption + ']').attr('selected', 'selected');
					$(".engraveDiv").show();
				} else {
					$('#configOptionValueMap option[value=' + stethoscopeConfigOption + ']').attr('selected', false);
					$("#engrave").val('');
					$("#checkBoxEngraving").val(0);
					$(".engraveDiv").hide();
				}
			});

		});
	</script>

	<c:if test="${not isSecure }">
		<iframe
				src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e300&pid=${product.id}&catid=${product.primaryCategory.name}&subcat1id=&subcat2id=&section=1&level=1&uid=${user_hash}"
				scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
				frameborder="0"></iframe>
	</c:if>

    <!--google remarketing-->
    <s:layout-render name="/layouts/embed/googleremarketing.jsp" pageType="product" googleProduct="${product}" topLevelCategory="${product.primaryCategory.name}" categories="${product.pipeSeparatedCategories}"/>
    <!--google remarketing-->
    <s:layout-render name="/layouts/embed/_yahooMarketing.jsp" pageType="product" topLevelCategory="${product.primaryCategory.name}"/>
    <!--Ozone remarketing-->
    <s:layout-render name="/layouts/embed/_ozoneMarketing.jsp" pageType="product" googleProduct="${product}" topLevelCategory="${product.primaryCategory.name}"
                     secondaryLevelCategory ="${product.secondaryCategory.name}"/>


</s:layout-component>
</s:layout-render>