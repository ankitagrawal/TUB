<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.product.EnumProductVariantPaymentType" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<c:set var="lineItem_Service_Postpaid" value="<%=EnumProductVariantPaymentType.Postpaid.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa" event="pre"/>
<c:set var="product" value="${pa.product}"/>
<%
	boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/cartLayout.jsp" pageTitle="Shopping Cart">

<s:layout-component name="topHeading">Shopping Cart</s:layout-component>

<s:layout-component name="htmlHead">
<script src="${pageContext.request.contextPath}/js/handlebars.js"></script>
<script src="${pageContext.request.contextPath}/js/ember.js"></script>
<script src="${pageContext.request.contextPath}/js/loader.js"></script>

  <script type="text/javascript">
    var timeout; //Set globally as it needs to be reset when removeLink is clicked.
    var timespan = 3000;
    HK = Ember.Application.create({});
    HK.contextPath = "${pageContext.request.contextPath}";
    function showCouponDetails(){
        $("#couponPopUp").toggle();
        $(".appliedOfferDetails").toggle();
    }

    $(document).ready(function() {

        $("#learnMore").click(function(){
            $('html, body').animate({scrollTop: $(".products_container").height() + 400}, 1000);
        });

        $("#dispatchDateQuesMark").click(function(){
            $("#popUpDDate").toggle();
        });

        $("#crossNew").click(function(){
            $("#popUpDDate").hide();
        });

        $('.lineItemQty').blur(function() {
        var lineItemRow = $(this).parents('.lineItemRow');
        var lineItemId = lineItemRow.find('.lineItemId').val();
        var lineItemQty = $(this).val();
        var elm = $(this);
        $.getJSON(
            $('#lineItemUpdateLink').attr('href'), {cartLineItem: lineItemId, "cartLineItem.qty": lineItemQty},
            function(responseData) {
              if(responseData.code == '<%=HealthkartResponse.STATUS_OK%>'){
              _updateTotals(responseData);
              _updateLineItem(responseData, lineItemRow);
              //document.getElementById("freebieBanner").src = responseData.message;
              //$(".freebieBanner").attr("src", responseData.message);
              }else{
                elm.val(responseData.data);
              }
            }
         );
      });

      $('.comboQty').blur(function() {
        var lineItemRow = $(this).parents('.lineItemRow');
        var lineItemId = lineItemRow.find('.lineItemId').val();
        var comboQty = $(this).val();
        $.getJSON(
            $('#lineItemUpdateLink').attr('href'), {cartLineItem: lineItemId, "comboInstance.qty": comboQty},
            function(responseData) {
              _updateTotals(responseData);
              _updateLineItem(responseData, lineItemRow);
            }
            );
      });

      $('#couponWindow').jqm({trigger: '#couponLink', ajax: '@href'});
      $('#couponCode').keyup(function() {
        $('#couponLink').attr('href', $('#couponBaseLink').attr('href') + "?couponCode=" + $(this).val());
      });
      $('#couponCode').blur(function() {
        $('#couponLink').attr('href', $('#couponBaseLink').attr('href') + "?couponCode=" + $(this).val());
      });

      // available offers window
      $('#availableOffersWindow').jqm({trigger: '#availableOffersLink', ajax: '@href'});

      $('.lineItemQty').jqStepper(1);
      $('.comboQty').jqStepper(1);

      $('.removeLink').click(function() {
        clearTimeout(timeout);
        var itemContainer = $(this).parents('.product');
        var lineItemId = itemContainer.find('.lineItemId').val();
        var lineItemStyleId = itemContainer.find('.lineItemId').attr('id');
        $('#undoLineItemId').val(lineItemId);
        $('#undoQty').val(itemContainer.find('.lineItemQty').val());
        itemContainer.find('.lineItemQty').val(0);

        $.getJSON($('#lineItemUpdateLink').attr('href'), {'cartLineItem':lineItemId,'cartLineItem.qty':0}, function(responseData) {
          removeItem(lineItemStyleId);
          count = Math.round($('#productsInCart').html());
          simpleProductCount = Math.round($('#simpleProductsInCart').html())
          if (count == 1 || simpleProductCount==1) {
	        $('#productsInCart').html(0);
			//$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart_empty.png");
            location.reload();
          }
          else if (count > 1) {
            $('#productsInCart').html(count - 1);
            $('#simpleProductsInCart').html(simpleProductCount-1);
          }
          $('#numProdTitle').html(count - 1);
          $('.cartButton').glow('#f99', 500, 10);
          _updateTotals(responseData);
          /*if(responseData.message){
             $(".freebieBanner").attr("src", responseData.message);
          }else{
             $(".freebieBanner").attr("src", "");
          }*/
        });
        return false;
      });

      $('.removeComboLink').click(function() {
        clearTimeout(timeout);
        var itemContainer = $(this).parents('.product');
        var lineItemId = itemContainer.find('.lineItemId').val();
        var lineItemStyleId = itemContainer.find('.lineItemId').attr('id');
        $('#undoLineItemId').val(lineItemId);
        $('#undoQty').val(itemContainer.find('.comboQty').val());
        itemContainer.find('.comboQty').val(0);

        $.getJSON($('#lineItemUpdateLink').attr('href'), {'cartLineItem':lineItemId,'comboInstance.qty':0}, function(responseData) {
          removeItem(lineItemStyleId);
          count = Math.round($('#productsInCart').html());
          if (count == 1) {
            location.reload();
          }
          else if (count > 2) {
            $('#productsInCart').html(count - 1);
          }
          else {
            $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/>&nbsp;<span class='num' id='productsInCart'>1</span> item in<br/>your shopping cart");
          }
          $('#numProdTitle').html(count - 1);
          $('.cartButton').glow('#f99', 500, 10);
          _updateTotals(responseData);
        });
        return false;
      });

      function removeItem(lineItemStyleId) {
        $('#' + lineItemStyleId).parents('.lineItemRow').fadeOut();
        $('#undo_msg').html('Item has been removed. <a href="#" id="undoLink">Undo</a>').show();
        timeout = setTimeout(hideAlert, timespan); //Hiding the undo link after 2 seconds
        $('#undoLink').click(undoLastRemove);
      }

      function hideAlert() {
        $('#undo_msg').fadeOut();
      }

      function undoLastRemove() {
        var lineItemField = $('.lineItemRow .lineItemId[value="' + $('#undoLineItemId').val() + '"]');
        lineItemField.parents('.lineItemRow').find('.lineItemQty').val($('#undoQty').val());
        lineItemField.parents('.lineItemRow').fadeIn();
        $.getJSON($('#lineItemUpdateLink').attr('href'), {'cartLineItem':lineItemField.val(),'cartLineItem.qty':$('#undoQty').val()}, function(responseData) {
          _updateTotals(responseData)
        });
        $(this).parent('#undo_msg').html('Item restored', timeout = setTimeout(hideAlert, timespan)); //Hide the item restored message
        return false;
      }
    });

    function _updateLineItem(responseData, lineItemRow) {
      lineItemRow.find('.lineItemSubTotalMrp').html(responseData.data.pricingLineItemDto.subTotalMrp);
      lineItemRow.find('.lineItemSubTotalHkDiscount').html(responseData.data.pricingLineItemDto.subTotalHkDiscount);
      lineItemRow.find('.lineItemHkTotal').html(responseData.data.pricingLineItemDto.subTotalHkPrice);
    }

    function _updateTotals(responseData) {
      HK.CartOfferController.getOffer();
      $('#summaryProductsMrpSubTotal').html('Rs. ' + responseData.data.totalMrpSubTotal);
      $('#summaryProductsHkpSubTotal').html('Rs. ' + responseData.data.totalHkSubTotal);
      $('#summaryHkDiscount').html('Rs. ' + responseData.data.totalHkDiscount);
      if (responseData.data.totalPromoDiscount == "0.00") {
        $('#summaryPromoDiscountContainer').hide();
      } else {
        $('#summaryPromoDiscountContainer').show();
      }
      $('#summaryPromoDiscount').html('Rs. ' + responseData.data.totalPromoDiscount);
      if (responseData.data.subscriptionDiscount == "0.00") {
        $('#summarySubscriptionDiscountContainer').hide();
      } else {
        $('#summarySubscriptionDiscountContainer').show();
      }
      $('#totalSubscriptionDiscount').html('Rs. ' + responseData.data.subscriptionDiscount);

      if (responseData.data.totalCashback == "0.00") {
        $('#summaryTotalCashbackContainer').hide();
      } else {
        $('#summaryTotalCashbackContainer').show();
      }
      $('#summaryTotalCashback').html('Rs. ' + responseData.data.totalCashback);

      if (responseData.data.shippingTotal == "0.00") {
        $('#summaryShippingSubTotal .checkoutContainerTextRight').html('Free!');
      } else {
        $('#summaryShippingSubTotal .checkoutContainerTextRight').html((responseData.data.shippingTotal));
      }
      var mrp = 0.0;
      mrp = Math.round((responseData.data.productsMrpSubTotal).replace(',', ''));
      var hkp = 0.0;
      hkp = Math.round((responseData.data.productsHkSubTotal).replace(',', ''));
      //      if (responseData.data.totalDiscount == responseData.data.shippingDiscount) {
      //        $('#summaryTotalDiscount').html('(Rs. ' + (mrp - hkp));
      //      } else {
      //        $('#summaryTotalDiscount').html('(Rs. ' + Math.round((mrp - hkp) + Math.round(responseData.data.totalDiscount - responseData.data.shippingDiscount)));
      //      }

      $('#summaryGrandTotalPayable').html('Rs. ' + responseData.data.grandTotalPayable);
      $('#summaryGrandTotalPospaidServices').html('Rs. ' + responseData.data.postpaidServicesTotal);
    }

  </script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jquery.glow.js"></script>
</s:layout-component>

<s:layout-component name="modal">
  <div class="jqmWindow" id="couponWindow">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Loading..</s:layout-component>
      <s:layout-component name="content">Please wait</s:layout-component>
    </s:layout-render>
  </div>
  <div class="jqmWindow" id="availableOffersWindow" style="display:none">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Loading..</s:layout-component>
      <s:layout-component name="content">Please wait</s:layout-component>
    </s:layout-render>
  </div>
</s:layout-component>

<s:layout-component name="cart_title">

  <%--<div class="prom yellow help" style="margin-bottom:20px; padding:5px;">
    <p style="font-size:1.2em"> We are undergoing system upgrades over the weekend.
    As a result, dispatch may be delayed by 2 days.
    <br>We apologize for the inconvenience.</p>
  </div>--%>

  <h2>
    <c:choose>
      <c:when test="${cartAction.pricingDto.productLineCount == 1}">
        There is
        <span class='num arialBold'>${cartAction.pricingDto.productLineCount}</span>
        item in your shopping cart
      </c:when>
      <c:when test="${cartAction.pricingDto.productLineCount > 1}">
        There are
        <span class='num arialBold' id="numProdTitle">${cartAction.pricingDto.productLineCount}</span>
        items in your shopping cart
      </c:when>
      <c:otherwise>
        Your cart is empty. Please go back to add products to it.
      </c:otherwise>
    </c:choose>
  </h2>

 <shiro:lacksRole name="<%=RoleConstants.B2B_USER%>">
  <c:if test="${cartAction.pricingDto.productLineCount > 0}">
    <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add more products</a>
  </c:if>
  <c:if test="${cartAction.pricingDto.productLineCount == 0}">
    <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add products to your shopping cart</a>
  </c:if>
  </shiro:lacksRole>
  <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
  <c:if test="${cartAction.pricingDto.productLineCount > 0}">
    <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add more products</a>
  </c:if>
  <c:if test="${cartAction.pricingDto.productLineCount == 0}">
    <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add products to your shopping cart</a>
  </c:if>
  </shiro:hasRole>
 <div id="offerTextOnTop"></div>
</s:layout-component>

<s:layout-component name="cart_items">

<c:if test="${cartAction.pricingDto.productLineCount >= 1}">

<div id="appliedOfferDiv"></div>
<div class='products_container' style="min-height: 300px;">

<div style="display: none;">
    <s:link beanclass="com.hk.web.action.core.order.CartLineItemUpdateAction" id="lineItemUpdateLink"></s:link>
    <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" style="display:none;" id="couponBaseLink"></s:link>
    <s:link beanclass="com.hk.web.action.core.cart.CartAction" style="display:none;" id="updatePricingLink" event="pricing"></s:link>
</div>

<c:if test="${cartAction.pricingDto.subscriptionLineCount>=1}">
  <span id="subscriptionsInCart" style="display: none;">${cartAction.pricingDto.subscriptionLineCount}</span>
  <s:layout-render name="/layouts/embed/_subscriptionCart.jsp"
                   subscriptions="${cartAction.subscriptions}" />
</c:if>
<c:if test="${(cartAction.pricingDto.productLineCount-cartAction.pricingDto.subscriptionLineCount)>=1}">
<span id="simpleProductsInCart" style="display: none;">${(cartAction.pricingDto.productLineCount-cartAction.pricingDto.subscriptionLineCount)}</span>
<div class='tabletitle tableTitleNew' style="border-radius: 0px;">
  <div class='name' style="width: 35%;left: 5px;">
    Product
  </div>
  <div class='name' style="width: 25%;left: 30px">
    <span class="dispatchDateText2">Dispatch Days</span>
      <span id="dispatchDateQuesMark" class="dispatchDateQuesMark">?</span>
      <div class="popUpDDate" id="popUpDDate">The dispatch date is when the product will be shipped from our warehouse. The delivery time would be extra and will vary according to your location.
      <span id="learnMore" class="learnMore">learn more</span>
      <span id="crossNew" style="position: relative;float: right;top: 12px;cursor: pointer;">X</span>
      <span class="arrowNew"></span>
      </div>
  </div>
  <div class='quantity' style="width: 15%;left: 0px;">
    Quantity
  </div>
  <div class='price' style="width: 15%;left: 35px;">
    Price
  </div>
  <div class='floatfix'></div>
</div>
<c:forEach items="${cartAction.order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
  <div class="lineItemRow product" style="border: 1px solid #ddd;border-top: none;">
    <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

    <a href="${pageContext.request.contextPath}${cartLineItem.productVariant.product.productURL}" style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;position: relative;float: left;">
      <c:choose>
        <c:when test="${cartLineItem.productVariant.product.mainImageId != null}">
          <hk:productImage imageId="${cartLineItem.productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
        </c:when>
        <c:otherwise>
          <img class="prod48"
               src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.productVariant.product.id}.jpg"
               alt="${cartLineItem.productVariant.product.name}"/>
        </c:otherwise>
      </c:choose>
    </a>

    <div class="name" style="font-size: 10px; line-height: 21px;width: 200px;position: relative;float: left;" :>
        <a href="${pageContext.request.contextPath}${cartLineItem.productVariant.product.productURL}">${cartLineItem.productVariant.product.name}  </a>
        ${cartLineItem.productVariant.variantName}<br/>
      <table style="display: inline-block; font-size: 11px;">
        <c:forEach items="${cartLineItem.productVariant.productOptions}" var="productOption" varStatus="ctr">
	        <tr>
		        <c:if test="${hk:showOptionOnUI(productOption.name)}">
			        <td style="text-align: left;  padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fafafa;">${productOption.name}</td>
			        <td style="text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fff;">
				        <c:if test="${fn:startsWith(productOption.value, '-')}">
					        ${productOption.value}
				        </c:if>
				        <c:if test="${!fn:startsWith(productOption.value, '-')}">
					        &nbsp;${productOption.value}
				        </c:if>
			        </td>
		        </c:if>
	        </tr>
        </c:forEach>
	      <c:forEach items="${cartLineItem.cartLineItemExtraOptions}" var="extraOption">
		      <tr>
			      <td style="text-align: left;  padding: 0.3em 2em; border: 1px solid #f0f0f0;background: #fafafa;">${extraOption.name}</td>
			      <td style="text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0;background: #fff;">
				      <c:if test="${fn:startsWith(extraOption.value, '-')}">
					      ${extraOption.value}
				      </c:if>
				      <c:if test="${!fn:startsWith(extraOption.value, '-')}">
					      &nbsp;${extraOption.value}
				      </c:if>
			      </td>
		      </tr>
	      </c:forEach>
        <c:set var="TH" value="TH"/>
        <c:set var="THBF" value="THBF"/>
        <c:set var="CO" value="CO"/>
        <c:set var="COBF" value="COBF"/>
        <c:if test="${not empty cartLineItem.cartLineItemConfig.cartLineItemConfigValues}">
          <tr>
            <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${cartLineItem.productVariant.product.name}</td>
            <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
              Rs. ${cartLineItem.productVariant.hkPrice}</td>
          </tr>
          <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}" var="configValue">
            <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
            <tr>
              <c:set var="addParam" value="${variantConfigOption.additionalParam}"/>
              <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${variantConfigOption.displayName}
                : ${configValue.value}
                <c:if test="${(addParam ne TH) or (addParam ne THBF) or (addParam ne CO) or (addParam ne COBF) }">
                  <c:if
                      test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                    (R)
                  </c:if>
                    <c:if
                      test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                    (L)
                  </c:if>
                </c:if>
              </td>
              <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
                  <%--<c:set var="addParam" value="${variantConfigOption.additionalParam}"/>--%>
                <c:choose>
                  <c:when test="${configValue.additionalPrice eq 0}">
                    included
                  </c:when>
                  <c:otherwise>
                    +Rs. ${configValue.additionalPrice}
                    <%--<c:if test="${(addParam eq TH) or (addParam eq THBF) or (addParam eq CO) or (addParam eq COBF) }">--%>
                    <%--/Eye--%>
                    <%--</c:if>--%>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </c:if>

      </table>
    </div>

      <%--HTML code for dispatch date--%>
      <div class="dispatchedDateNew">
          <div>${cartLineItem.productVariant.product.minDays} - ${cartLineItem.productVariant.product.maxDays} working days</div>
      </div>


    <div class="quantity" style="width:80px;left:${cartLineItem.hkPrice != 0.0 ? 35 : 10}px;">
      <c:choose>
        <c:when test="${cartLineItem.hkPrice == 0.0}">
           ${cartLineItem.qty}
        </c:when>
        <c:otherwise>
           <input value="${cartLineItem.qty}" size="1" class="lineItemQty" style="width: 20px; height: 18px;"/>
        </c:otherwise>
      </c:choose>
      <c:if test="${cartLineItem.productVariant.id != cartAction.order.offerInstance.offer.offerAction.freeVariant.id}">
      <a style="position: relative;float:left;" class='remove removeLink' href='#'>
        (remove)
      </a>
      </c:if>
    </div>
    <div class="price">
      <c:choose>
        <c:when test="${cartLineItem.hkPrice == 0.0}">
          Free!
        </c:when>
        <c:when test="${cartLineItem.markedPrice == cartLineItem.hkPrice}">
          <div class="hk">
            <div class="num">
              <span class="lineItemSubTotalMrp arialBlackBold">Rs <fmt:formatNumber
                  value="${cartLineItem.hkPrice * cartLineItem.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <div class="cut">
            <div class="num lineItemSubTotalMrp arialGrayBold"> Rs
              <fmt:formatNumber value="${cartLineItem.markedPrice * cartLineItem.qty}"
                                pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
          </div>
          <%--<div class='special green'>
            (saved
                    <span class='num '>
                      Rs  <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber

                        value="${(cartLineItem.markedPrice - cartLineItem.hkPrice - cartLineItem.productVariant.postpaidAmount) * cartLineItem.qty}"
                        pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
                    </span>
          </div>--%>
          <div class="hk">
            <div class="num">
              <span class="lineItemHkTotal arialBlackBold">Rs <fmt:formatNumber
                  value="${cartLineItem.hkPrice * cartLineItem.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>

        </c:otherwise>
      </c:choose>

      <c:if test="${lineItem_Service_Postpaid == cartLineItem.productVariant.paymentType.id}">
        <span class="special" style="text-align: center; font-size: 10px; font-weight: bold;">
          to the service provider
        </span>
      </c:if>
      <c:if test="${cartLineItem.productVariant.product.subscribable}">
       <br/> <div style="font-style: italic; font-size: 11px; font-weight: normal; font-family: Georgia, Cambria, serif;">
          <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionAction" class="addSubscriptionLink"><b>subscribe and save more</b>
              <s:param name="productVariant" value="${cartLineItem.productVariant}"/>
          <s:param name="fromCart" value="true"/> </s:link>
    </div>
      </c:if>
    </div>
    <div class="floatfix"> </div>

  </div>
</c:forEach>
<div class="jqmWindow" style="display:none;" id="addSubscriptionWindow"></div>

<script type="text/javascript">
    $(document).ready(function(){
        $('#addSubscriptionWindow').jqm({trigger: '.addSubscriptionLink', ajax: '@href',ajaxText:'<br/><div style="text-align: center;">loading... please wait..</div> <br/>'});
    });

</script>

<c:forEach items="${cartAction.order.exclusivelyComboCartLineItems}" var="cartLineItem" varStatus="ctr1">
  <div class="lineItemRow product">
    <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

    <a href="${pageContext.request.contextPath}${cartLineItem.comboInstance.combo.productURL}" style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;position: relative;float: left;">
      <c:choose>
        <c:when test="${cartLineItem.comboInstance.combo.mainImageId != null}">
          <hk:productImage imageId="${cartLineItem.comboInstance.combo.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
        </c:when>
        <c:otherwise>
          <img class="prod48"
               src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.comboInstance.combo.id}.jpg"
               alt="${cartLineItem.comboInstance.combo.name}"/>
        </c:otherwise>
      </c:choose>
    </a>

    <div class="name" style="font-size: 10px; line-height: 21px;width: 200px;position: relative;float: left;">
        <a href="${pageContext.request.contextPath}${cartLineItem.comboInstance.combo.productURL}">${cartLineItem.comboInstance.combo.name}</a><br/>
      <c:forEach items="${cartLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">
            <span style="font-size:10px;">
            ${comboVariant.qty} x
            </span>

            <span style="font-size:10px;">
            ${comboVariant.productVariant.product.name} - ${comboVariant.productVariant.optionsCommaSeparated}
            </span>
        <br/>
      </c:forEach>
    </div>
      <div class="dispatchedDateNew"><div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays} working days</div></div>
    <div class="quantity" style="width: 80px;left: 35px;">
      <input value="${hk:getComboCount(cartLineItem)}" size="1" class="comboQty" style="width: 20px; height: 18px;"/>
      <a style="position: relative;float:left;" class='remove removeComboLink' href='#'>
        (remove)
      </a>
    </div>
    <div class="price">
      <c:choose>
        <c:when test="${cartLineItem.markedPrice == cartLineItem.hkPrice}">
          <div class="hk">
            <div class="num">
      <span class="lineItemSubTotalMrp arialBlackBold">Rs <fmt:formatNumber value="${cartLineItem.comboInstance.combo.markedPrice}"
                                                          pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <div class="cut">
            <div class="num lineItemSubTotalMrp arialGrayBold">  Rs
              <fmt:formatNumber value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem)}"
                                pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
          </div>
          <%--<div class='special green'>
            (saved
      <span class='num '>
      Rs <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber
          value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem) - cartLineItem.comboInstance.combo.hkPrice * hk:getComboCount(cartLineItem)}"
          pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
      </span>
          </div>--%>
          <div class="hk">
            <div class="num">
      <span class="lineItemHkTotal arialBlackBold"> Rs <fmt:formatNumber
          value="${cartLineItem.comboInstance.combo.hkPrice * hk:getComboCount(cartLineItem)}"
          pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="floatfix"></div>
  </div>
</c:forEach>
<%--<s:layout-render name="/layouts/embed/_cartFreebies.jsp" freebieBanner="${cartAction.freebieBanner}"/>--%>
<!--google remarketing-->
<s:layout-render name="/layouts/embed/googleremarketing.jsp" pageType="cart" order="${cartAction.order}"/>

<shiro:lacksRole name="<%=RoleConstants.B2B_USER%>">
<c:if test="${cartAction.pricingDto.productLineCount > 0}">
  <s:link beanclass="com.hk.web.action.HomeAction" class="back"> &larr; go back to add more products</s:link>
</c:if>
<c:if test="${cartAction.pricingDto.productLineCount == 0}">
  <s:link beanclass="com.hk.web.action.HomeAction"
          class="back"> &larr; go back to add products to your shopping cart</s:link>
</c:if>
</shiro:lacksRole>
<shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
<c:if test="${cartAction.pricingDto.productLineCount > 0}">
  <s:link beanclass="com.hk.web.action.core.b2b.B2BCartAction" class="back"> &larr; go back to add more products</s:link>
</c:if>
<c:if test="${cartAction.pricingDto.productLineCount == 0}">
  <s:link beanclass="com.hk.web.action.core.b2b.B2BCartAction"
          class="back"> &larr; go back to add products to your shopping cart</s:link>
</c:if>
</shiro:hasRole>
</c:if>
</div>


<div class="offerContainer">
<shiro:lacksRole name="<%=RoleConstants.COUPON_BLOCKED%>">
    <div class='right_container coupon gotACoupon'>
        <shiro:hasAnyRoles name="<%=RoleConstants.HK_USER%>">
            <div class="appliedOfferHead" style=" left: 0;">Got a discount coupon?</div>

            <input class="couponInput" placeholder='Enter discount code' type='text' id="couponCode"/>
            <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" id="couponLink" onclick="return false;"
                    class="button_grey new_button_grey">APPLY</s:link>
        </shiro:hasAnyRoles>

        <shiro:hasAnyRoles name="<%=RoleConstants.TEMP_USER%>">
            Got a discount coupon?
            <br/>
            <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="lrg" event="pre"> login / signup
                <s:param name="redirectUrl" value="${pageContext.request.contextPath}/core/cart/Cart.action"/>
            </s:link>
            to redeem it.
            <br/>
        </shiro:hasAnyRoles>
        <shiro:hasAnyRoles name="<%=RoleConstants.HK_UNVERIFIED%>">
            Got a discount coupon?
            <br/>
            <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" class="lrg" event="pre"> Verify your Account
            </s:link>
            to redeem it.
            <br/>
        </shiro:hasAnyRoles>

    </div>
</shiro:lacksRole>
</div>


<div class='right_container total checkoutContainer' style="font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif !important;">
<h5 class="checkoutHead5">Checkout</h5>
<br/>

<div style="font-weight: normal; color: #888; text-align: center; line-height: 21px;">
  <c:if
      test="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal + cartAction.pricingDto.postpaidServiceMrpSubTotal > cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal + cartAction.pricingDto.postpaidServiceHkSubTotal}">
      <span class="checkoutContainerText">Total MRP:</span>
      <span class="cut checkoutContainerTextRight" style="text-align: center;">
              <span id="summaryProductsMrpSubTotal" style="text-decoration: line-through;"> <fmt:formatNumber
              value="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal + cartAction.pricingDto.postpaidServiceMrpSubTotal}"
              type="currency" currencySymbol="Rs. "/></span>
          </span>
    <br/>
  </c:if>
    <span class="checkoutContainerText">Our Price:</span>
    <span class="checkoutContainerTextRight" style="text-align: center;">
          <span id="summaryProductsHkpSubTotal"> <fmt:formatNumber
            value="${cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal + cartAction.pricingDto.postpaidServiceHkSubTotal}"
            type="currency" currencySymbol="Rs. "/></span>
        </span>
  <br/>
  <c:if
      test="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal > cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal +cartAction.pricingDto.postpaidServiceHkSubTotal}">
       <%-- <span class="special" style="font-style: initial;">
          <span class="checkoutContainerText" style="font-size: 11px;">HealthKart Discount:</span><strong><span id="summaryHkDiscount"
                                                                                         class="green checkoutContainerTextRight">(<fmt:formatNumber
            value="${cartAction.pricingDto.totalHkProductsDiscount + cartAction.pricingDto.totalHkPrepaidServiceDiscount + cartAction.pricingDto.totalHkPostpaidServiceDiscount - cartAction.pricingDto.totalPostpaidAmount}"
            type="currency" currencySymbol="Rs. "/>)</span></strong>
        </span><br/>--%>
  </c:if>
        <span id="summaryShippingSubTotal">
          <c:if test="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount >= .005}">
            <span class="checkoutContainerText">Shipping:</span>
            <span class="checkoutContainerTextRight"><fmt:formatNumber value="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount}"
                              type="currency" currencySymbol="Rs. "/>  </span>
          </c:if>
          <c:if test="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount == 0}">
              <span class="checkoutContainerText">Shipping:</span> <span class="checkoutContainerTextRight">Free!</span>
          </c:if>
        </span>
  <br/>

      <span class="special" id="summarySubscriptionDiscountContainer"
            style="display: ${cartAction.pricingDto.subscriptionDiscount > 0 ? 'block':'none'};font-style: initial;">
        <span class="checkoutContainerText" style="font-size: 12px;">Subscription Discount:</span> <strong><span id="totalSubscriptionDiscount"
                                                                                  class="green checkoutContainerTextRight">(<fmt:formatNumber
          value="${cartAction.pricingDto.subscriptionDiscount}" type="currency" currencySymbol="Rs. "/>)</span></strong>
      </span>
      <span class="special" id="summaryPromoDiscountContainer"
            style="display: ${cartAction.pricingDto.totalPromoDiscount > 0 ? 'block':'none'};font-style: initial;">
        <span class="checkoutContainerText lightBlue" style="font-size: 11px;">Promo Discount:</span> <strong><span id="summaryPromoDiscount"
                                                                                  class="green lightBlue checkoutContainerTextRight">(<fmt:formatNumber
          value="${cartAction.pricingDto.totalPromoDiscount}" type="currency" currencySymbol="Rs. "/>)</span></strong>
      </span>
</div>

<h1 style="position: relative;float: left;width: 100%;padding: 0px;font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif !important">
      <span class="youPay">
        You pay
      </span>
  <strong>
        <span id="summaryGrandTotalPayable" class="youPayValue">
          <fmt:formatNumber value="${cartAction.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </span>
  </strong>
</h1>

<div class='newShippingHandling'>
  (inclusive of shipping, handling and taxes.)
</div>

<div style="text-align:center; padding-top: 10px; display: ${cartAction.pricingDto.totalCashback > 0 ? 'block':'none'};"
     id="summaryTotalCashbackContainer">

    <span class="checkoutContainerText lightBlue">You will get cashback:</span>
            <span class="checkoutContainerTextRight lightBlue">
                (<fmt:formatNumber
                        value="${cartAction.pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/>)
            </span>
</div>


<c:if test="${cartAction.pricingDto.postpaidServicesTotal > 0}">
  <h1>
      <span class="special" style="text-align: center; font-size: 10px; font-weight: bold;">
        you pay later
      </span>
    <br/>
    <strong>
        <span id="summaryGrandTotalPospaidServices">
          <fmt:formatNumber value="${cartAction.pricingDto.postpaidServicesTotal}" type="currency"
                            currencySymbol="Rs. "/>
        </span>
    </strong>
    <br/>
  </h1>

  <div class='small'>
    to the service provider
    (inclusive of shipping, handling and taxes.)
  </div>
</c:if>
<s:form beanclass="com.hk.web.action.core.cart.CartAction" id="cartForm">
<s:hidden name="order" value="${cartAction.order}"/>
<s:submit name="checkout" value="PLACE ORDER" class="placeOrderButtonNew" style="font-size: 1em"/>

</s:form>
<script type="text/javascript">
  $('#proceed').click(function() {
    $('#cartForm').submit();
  });
</script>
</div>
<div id="applicableOfferDiv"></div>

<div class='orderSummaryHeading'>
    <div class="deliveryDetails"> DELIVERY DETAILS</div>
    <ul>
        <li>
            - The time taken for delivery after dispatch from our warehouse varies with location.
        </li>
        <li>
            - For Metroes: 1-3 business days
        </li>
        <li>
            - For Major Cities: 2-4 business days
        </li>
        <li>
            - For Other Town/Cities: 3-6 business days
        </li>
        <li>
            - For Rest of India Non Serviceable through Couriers: 7-15 business days (Delivery done by Indian Post)
        </li>
    </ul>
</div>

<s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="qbr7CMDf6QIQuLjI5QM" id="1018305592"/>
<s:layout-render name="/layouts/embed/_ozoneMarketing.jsp" pageType="cart" order="${cartAction.order}"/>

			<c:if test="${not isSecure }">
				<iframe src="" id="vizuryTargeting" scrolling="no" width="1"
					height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>


				<script type="text/javascript">                                                
  var vizuryLink = "http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e400";
  var user_hash;
  <c:forEach items="${cartAction.order.productCartLineItems}" var="cartLineItem" varStatus="liCtr">
  vizuryLink += "&pid${liCtr.count}=${cartLineItem.productVariant.product.id}&catid${liCtr.count}=${cartLineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${cartLineItem.qty}";
  user_hash = "${user_hash}";
  </c:forEach>
  vizuryLink += "&currency=INR&section=1&level=1";
  document.getElementById("vizuryTargeting").src = vizuryLink+"&uid="+user_hash;
</script>
			</c:if>

</c:if>
     <c:set var="comboInstanceIds"  value="" />
     <c:set var="comboInstanceIdsName" value="" />
     <c:if test="${cartAction.trimCartLineItems!=null && fn:length(cartAction.trimCartLineItems) >0}">
         <script type="text/javascript">
             $(document).ready(function () {
                 ShowDialog(true);
//              e.preventDefault();
                 $('.button_green').live('click',function(){
                     $(this).hide();
                     HideDialog();
                 });

                 function ShowDialog(modal)
                 {
                     $("#overlay2").show();
                     $("#dialog2").fadeIn(300);

                     if (modal)
                     {
                         $("#overlay2").unbind("click");
                     }
                 }

                 function HideDialog()
                 {

                     $("#overlay2").hide();
                     $("#dialog2").fadeOut(300);
                 }
             });
         </script>
     </c:if>

</s:layout-component>
</s:layout-render>

<div id="overlay2" class="web_dialog_overlay"></div>
   <div id="dialog2" class="web_dialog">

       <table style="width:100%; border: 0px;" cellpadding="3" cellspacing="0">
           <tr>
               <td colspan="2" class="web_dialog_title" style="color:#444;">Oops! We are sorry.</td>
               <td class="web_dialog_title align_right">
                   <%--<a href="#" id="btnClose" class="classClose">Close</a>                   --%>
               </td>
           </tr>
           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
           <tr>
               <td colspan="3" style="padding-left: 15px;">
                   <b>The following items have been removed due to insufficient inventory</b>
               </td>
           </tr>
           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
               <c:forEach items="${cartAction.trimCartLineItems}" var="cartLineItem" varStatus="ctr1">
                   <tr>
                       <div class='product' style="border-bottom-style: solid;">
                         <td style="padding-left: 15px;">
                           <div class='img48' style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
                                 <c:choose>
                                     <c:when test="${cartLineItem.comboInstance!=null}">
                                         <c:if test="${!fn:contains(comboInstanceIds, cartLineItem.comboInstance.id)}">
                                          <c:set var="comboInstanceIds"  value="${cartLineItem.comboInstance.id}+','+${comboInstanceIds}"/>
                                             <c:choose>
                                                 <c:when test="${cartLineItem.comboInstance.combo.mainImageId != null}">
                                                     <hk:productImage imageId="${cartLineItem.comboInstance.combo.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
                                                 </c:when>
                                                 <c:otherwise>
                                                     <img class="prod48"
                                                          src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.comboInstance.combo.id}.jpg"
                                                          alt="${cartLineItem.comboInstance.combo.name}"/>
                                                 </c:otherwise>
                                             </c:choose>
                                         </c:if>
                                     </c:when>
                                     <c:otherwise>
                                         <c:choose>
                                             <c:when test="${cartLineItem.productVariant.product.mainImageId != null}">
                                                 <hk:productImage imageId="${cartLineItem.productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
                                             </c:when>
                                             <c:otherwise>
                                                 <img class="prod48"
                                                      src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.productVariant.product.id}.jpg"
                                                      alt="${cartLineItem.productVariant.product.name}"/>
                                             </c:otherwise>
                                         </c:choose>
                                     </c:otherwise>
                                 </c:choose>
                           </div>
                         </td>
                         <td>
                           <div class='name'>
                               <table width="100%">
                                   <tr>
                                       <td>
                                               <c:choose>
                                                  <c:when test="${cartLineItem.comboInstance!=null}">
                                                    <c:if test="${!fn:contains(comboInstanceIdsName, cartLineItem.comboInstance.id)}">
                                                        <c:set var="comboInstanceIdsName"  value="${cartLineItem.comboInstance.id}+','+${comboInstanceIdsName}"/>
                                                        ${cartLineItem.comboInstance.combo.name} <br/>
                                                     </c:if>
                                                  </c:when>
                                                  <c:otherwise>
                                                       ${cartLineItem.productVariant.product.name}
                                                  </c:otherwise>
                                               </c:choose>
                                       </td>
                                   </tr>
                               </table>
                           </div>
                         </td>

                       </div>
                   </tr>
                 </c:forEach>


           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
           <tr>

           </tr>
           <tr>
               <td colspan="2" style="text-align: center;">

                 <c:if test="${cartAction.sizeOfCLI > 0}">
                   <a class="button_green" style="width:120px; height: 18px;">Continue</a>
                     </td><td>
                   </c:if>
                   <s:link beanclass="com.hk.web.action.core.cart.CartAction" class=" button_green"
                           style="width: 160px; height: 18px;">Back to Shopping
                   </s:link>
               </td>
           </tr>
       </table>
   </div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
<!-- BLADE pseudo conversion code -->
<script type="text/javascript">
<!--
var blade_co_account_id='4184';
var blade_group_id='convtrack14700';

(function() {
var host = (location.protocol == 'https:') ? 'https://d-cache.microadinc.com' : 'http://d-cache.microadinc.com';
var path = '/js/bl_track_others.js';

var bs = document.createElement('script');
bs.type = 'text/javascript'; bs.async = true;
bs.charset = 'utf-8'; bs.src = host + path;

var s = document.getElementsByTagName('script')[0];
s.parentNode.insertBefore(bs, s);
})();
-->
</script>

<style type="text/css">

     .web_dialog_overlay
   {
      position: fixed;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      height: 100%;
      width: 100%;
      margin: 0;
      padding: 0;
      background: #000000;
      opacity: .15;
      filter: alpha(opacity=15);
      -moz-opacity: .15;
      z-index: 101;
      display: none;
   }
   .web_dialog
   {
      display: none;
      position: fixed;
      width: 450px;
      /*height: 400px;*/
      top: 50%;
      left: 50%;
      margin-left: -265px;
      margin-top: -180px;
      /*background-color: #ffffff;*/
      background-color: white;
      /*border: 2px solid #336699;*/
      padding: 0px;
      z-index: 102;
      font-family: Verdana;
      font-size: 10pt;
      color: #333;
      box-shadow: 0 0 15px rgba(0, 0, 0, 0.9), 0 0 5px rgba(0, 0, 0, 0.5), 0 0 10px rgba(0, 0, 0, 0.7), 0 0 25px rgba(0, 0, 0, 0.3);
   }
   .web_dialog_title
   {
      /*border-bottom: solid 2px #336699;*/
      /*background-color: #336699;*/
     font-size: 16px;
     font-weight: bold;
     padding: 5px;
      background-color: #f2f7fb;
      color: White;
      font-weight:bold;
   }
   .web_dialog_title a
   {
      color: White;
      text-decoration: none;
   }
   .align_right
   {
      text-align: right;
   }

   </style>
