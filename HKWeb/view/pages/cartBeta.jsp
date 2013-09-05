<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.product.EnumProductVariantPaymentType" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<c:set var="lineItem_Service_Postpaid" value="<%=EnumProductVariantPaymentType.Postpaid.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa" event="pre"/>
<c:set var="product" value="${pa.product}"/>
<%
    boolean isSecure = WebContext.isSecure();
    pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/cartLayoutBeta.jsp" pageTitle="Shopping Cart">

<s:layout-component name="topHeading">Shopping Cart</s:layout-component>

<s:layout-component name="htmlHead">
<script src="<hk:vhostImage/>/js/handlebars.js"></script>
<script src="<hk:vhostImage/>/js/ember.js"></script>
<script src="<hk:vhostImage/>/js/loader.js"></script>

<script type="text/javascript">
jQuery.fn.jqStepper = function(minValue, maxValue) {
    jQuery.fn.jqStepper.minValue = minValue;
    jQuery.fn.jqStepper.maxValue = maxValue;
    return this.each(jqStepper_addStepper);
    function jqStepper_addStepper() {
        $(this).wrap('<span class="jqStepper"/>').before('<a href="#" class="jqStepper-down">-</a>').after('<a href="#" class="jqStepper-up">+</a>');
        $(this).parents('.jqStepper').find('.jqStepper-up').click(jqStepper_increaseValue);
        $(this).parents('.jqStepper').find('.jqStepper-down').click(jqStepper_decreaseValue);
    }

    function jqStepper_increaseValue() {
        var inputElem = $(this).parents('.jqStepper').find('input');
        var val = eval(inputElem.val() + '+' + 1);
        if (!jQuery.fn.jqStepper.maxValue) inputElem.val(val);
        else if (val <= jQuery.fn.jqStepper.maxValue) inputElem.val(val);
        inputElem.blur();
        return false;
    }

    function jqStepper_decreaseValue() {
        var inputElem = $(this).parents('.jqStepper').find('input');
        var val = eval(inputElem.val() + '-' + 1);
        if (!jQuery.fn.jqStepper.minValue) inputElem.val(val);
        else if (val >= jQuery.fn.jqStepper.minValue) inputElem.val(val);
        inputElem.blur();
        return false;
    }
};
var timeout; //Set globally as it needs to be reset when removeLink is clicked.
var timespan = 3000;
HK = Ember.Application.create({});
HK.contextPath = "${pageContext.request.contextPath}";
function showCouponDetails() {
    $("#couponPopUp").toggle();
    $(".appliedOfferDetails").toggle();
}

$(document).ready(function() {

    $("#learnMore").click(function() {
        $('html, body').animate({scrollTop: $(".products_container").height() + 400}, 1000);
    });

    $("#dispatchDateQuesMark").click(function() {
        $("#popUpDDate").toggle();
    });

    $("#crossNew").click(function() {
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
                    if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        _updateTotals(responseData);
                        _updateLineItem(responseData, lineItemRow);
                        //document.getElementById("freebieBanner").src = responseData.message;
                        //$(".freebieBanner").attr("src", responseData.message);
                    } else {
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
            if (count == 1 || simpleProductCount == 1) {
                $('#productsInCart').html(0);
                //$('.cartIcon').attr("src", "${pageContext.request.contextPath}/images/icons/cart_empty.png");
                location.reload();
            }
            else if (count > 1) {
                $('#productsInCart').html(count - 1);
                $('#simpleProductsInCart').html(simpleProductCount - 1);
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

            </c:when>
            <c:when test="${cartAction.pricingDto.productLineCount > 1}">

            </c:when>
            <c:otherwise>
                Your cart is empty. Please go back to add products to it.
            </c:otherwise>
        </c:choose>
    </h2>

    <shiro:lacksRole name="<%=RoleConstants.B2B_USER%>">
        <c:if test="${cartAction.pricingDto.productLineCount == 0}">
            <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add products
                to your shopping cart</a>
        </c:if>
    </shiro:lacksRole>
    <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
        <c:if test="${cartAction.pricingDto.productLineCount == 0}">
            <a href="/" class="back" style="position: relative;float: left;width: 100%;"> &larr; go back to add products
                to your shopping cart</a>
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
    <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" style="display:none;"
            id="couponBaseLink"></s:link>
    <s:link beanclass="com.hk.web.action.core.cart.CartAction" style="display:none;" id="updatePricingLink"
            event="pricing"></s:link>
</div>

<c:if test="${cartAction.pricingDto.subscriptionLineCount>=1}">
    <span id="subscriptionsInCart" style="display: none;">${cartAction.pricingDto.subscriptionLineCount}</span>
    <s:layout-render name="/layouts/embed/_subscriptionCart.jsp"
                     subscriptions="${cartAction.subscriptions}"/>
</c:if>
<c:if test="${(cartAction.pricingDto.productLineCount-cartAction.pricingDto.subscriptionLineCount)>=1}">
<span id="simpleProductsInCart"
      style="display: none;">${(cartAction.pricingDto.productLineCount-cartAction.pricingDto.subscriptionLineCount)}</span>

<div class='tabletitle tableTitleNew' style="border-radius: 0px;">
    <div class='name' style="width: 35%;left: 5px;text-transform:uppercase;">
        You are buying
    </div>
    <div class='name' style="width: 25%;left: 30px">
        DISPATCH
    </div>
    <div class='quantity' style="width: 15%;left: 0px;">
        QTY
    </div>
    <div class='price' style="width: 15%;left: 35px;">
        Price
    </div>
    <div class='floatfix'></div>
</div>
<c:forEach items="${cartAction.order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
    <c:set var="storeVariantBasic" value="${hk:getStoreVariantBasicDetails(cartLineItem.productVariant.id)}"/>

    <div class="lineItemRow product" style="border: 1px solid #ddd;border-width: 0px 0px 1px 0px;">
        <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

        <a href="${storeVariantBasic.url}"
           style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;position: relative;float: left;">
            <%--<c:choose>
                <c:when test="${cartLineItem.productVariant.product.mainImageId != null}">
                    <hk:productImage imageId="${cartLineItem.productVariant.product.mainImageId}"
                                     size="<%=EnumImageSize.TinySize%>"/>
                </c:when>
                <c:otherwise>
                    <img class="prod48"
                         src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.productVariant.product.id}.jpg"
                         alt="${cartLineItem.productVariant.variantName}"/>
                </c:otherwise>
            </c:choose>--%>
            <img class="prod48" src="${storeVariantBasic.primaryImage.mlink}" alt="${storeVariantBasic.name}"/>
        </a>

        <div class="name" style="width: 200px;position: relative;float: left;" :>
            <a href="${storeVariantBasic.url}">${storeVariantBasic.name} </a>
            <%--<a href="${pageContext.request.contextPath}${cartLineItem.productVariant.url}">${cartLineItem.productVariant.variantName} </a>--%>
                <%--${cartLineItem.productVariant.variantName}<br/>--%>

        </div>

            <%--HTML code for dispatch date--%>
        <div class="dispatchedDateNew">
            <div>${cartLineItem.productVariant.product.minDays} - ${cartLineItem.productVariant.product.maxDays} Days
            </div>
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
                <a style="" class='remove removeLink' href='#'>
                    X
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
              <span class="lineItemSubTotalMrp fnt-sz16">Rs <fmt:formatNumber
                      value="${cartLineItem.hkPrice * cartLineItem.qty}"
                      pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="hk">
                        <div class="num">
              <span class="lineItemHkTotal ">Rs <fmt:formatNumber
                      value="${cartLineItem.hkPrice * cartLineItem.qty}"
                      pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
                        </div>
                    </div>
                    <div class="cut">
                        <div class="num lineItemSubTotalMrp fnt-light"> Rs
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


                </c:otherwise>
            </c:choose>

            <c:if test="${lineItem_Service_Postpaid == cartLineItem.productVariant.paymentType.id}">
        <span class="special" style="text-align: center; font-size: 10px; font-weight: bold;">
          to the service provider
        </span>
            </c:if>
                <%--Subscription Commented--%>
                <%--<c:if test="${cartLineItem.productVariant.product.subscribable}">
                 <br/> <div style="font-style: italic; font-size: 11px; font-weight: normal; font-family: Georgia, Cambria, serif;">
                    <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionAction" class="addSubscriptionLink"><b>subscribe and save more</b>
                        <s:param name="productVariant" value="${cartLineItem.productVariant}"/>
                    <s:param name="fromCart" value="true"/> </s:link>
              </div>
                </c:if>--%>
        </div>
        <div class="floatfix"></div>

    </div>
</c:forEach>
<div class="jqmWindow" style="display:none;" id="addSubscriptionWindow"></div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#addSubscriptionWindow').jqm({trigger: '.addSubscriptionLink', ajax: '@href',ajaxText:'<br/><div style="text-align: center;">loading... please wait..</div> <br/>'});
    });

</script>

<%--<c:forEach items="${cartAction.order.exclusivelyComboCartLineItems}" var="cartLineItem" varStatus="ctr1">
    <c:set var="storeVariantBasic" value="${hk:getStoreVariantBasicDetails(cartLineItem.productVariant.id)}"/>
    <div class="lineItemRow product">
        <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

        <a href="${pageContext.request.contextPath}${cartLineItem.comboInstance.combo.productURL}"
           style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;position: relative;float: left;">
            <c:choose>
                <c:when test="${cartLineItem.comboInstance.combo.mainImageId != null}">
                    <hk:productImage imageId="${cartLineItem.comboInstance.combo.mainImageId}"
                                     size="<%=EnumImageSize.TinySize%>"/>
                </c:when>
                <c:otherwise>
                    <img class="prod48"
                         src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.comboInstance.combo.id}.jpg"
                         alt="${cartLineItem.comboInstance.combo.name}"/>
                </c:otherwise>
            </c:choose>
        </a>

        <div class="name" style="width: 200px;position: relative;float: left;">
            <a href="${pageContext.request.contextPath}${cartLineItem.comboInstance.combo.productURL}">${cartLineItem.comboInstance.combo.name}</a><br/>
            <c:forEach items="${cartLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">
            <span style="font-size:10px;">
            ${comboVariant.qty} x
            </span>

            <span style="font-size:10px;">
            ${comboVariant.productVariant.variantName} - ${comboVariant.productVariant.optionsCommaSeparated}
            </span>
                <br/>
            </c:forEach>
        </div>
        <div class="dispatchedDateNew">
            <div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays}
                working days
            </div>
        </div>
        <div class="quantity" style="width: 80px;left: 35px;">
            <input value="${hk:getComboCount(cartLineItem)}" size="1" class="comboQty"
                   style="width: 20px; height: 18px;"/>
            <a style="" class='remove removeComboLink' href='#'>
                X
            </a>
        </div>
        <div class="price">
            <c:choose>
                <c:when test="${cartLineItem.markedPrice == cartLineItem.hkPrice}">
                    <div class="hk">
                        <div class="num">
      <span class="lineItemSubTotalMrp arialBlackBold">Rs <fmt:formatNumber
              value="${cartLineItem.comboInstance.combo.markedPrice}"
              pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cut">
                        <div class="num lineItemSubTotalMrp arialGrayBold"> Rs
                            <fmt:formatNumber
                                    value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem)}"
                                    pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
                    </div>
                    --%><%--<div class='special green'>
                      (saved
                <span class='num '>
                Rs <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber
                    value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem) - cartLineItem.comboInstance.combo.hkPrice * hk:getComboCount(cartLineItem)}"
                    pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
                </span>
                    </div>--%><%--
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
</c:forEach>--%>
<%--<s:layout-render name="/layouts/embed/_cartFreebies.jsp" freebieBanner="${cartAction.freebieBanner}"/>--%>
<!--google remarketing-->
<s:layout-render name="/layouts/embed/googleremarketing.jsp" pageType="cart" order="${cartAction.order}"/>
<!--BLADe marketing-->
<s:layout-render name="/layouts/embed/_bladeMarketing.jsp" pageType="cart"/>

<shiro:lacksRole name="<%=RoleConstants.B2B_USER%>">

    <c:if test="${cartAction.pricingDto.productLineCount == 0}">
        <s:link beanclass="com.hk.web.action.HomeAction"
                class="back"> &larr; go back to add products to your shopping cart</s:link>
    </c:if>
</shiro:lacksRole>
<shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
    <c:if test="${cartAction.pricingDto.productLineCount == 0}">
        <s:link beanclass="com.hk.web.action.core.b2b.B2BCartAction"
                class="back"> &larr; go back to add products to your shopping cart</s:link>
    </c:if>
</shiro:hasRole>
</c:if>
</div>


<div class='right_container total checkoutContainer' style="">
    <div class="you-pay-container">
        <div style="width:48%;overflow:hidden;display:inline-block;float:left">
            <div class="fnt-light fnt-bold">
                YOU PAY
            </div>
            <div id="summaryGrandTotalPayable" class="fnt-sz14">
                <fmt:formatNumber value="${cartAction.pricingDto.grandTotalPayable}" type="currency"
                                  currencySymbol="Rs. "/>
            </div>
        </div>
        <div style="width:48%;overflow:hidden;display:inline-block">
            <s:form beanclass="com.hk.web.action.core.cart.CartAction" id="cartForm">
                <s:hidden name="order" value="${cartAction.order}"/>
                <s:submit name="checkout" value="PAY NOW" class="btn btn-blue"/>

            </s:form>

        </div>

        <strong>

        </strong>
    </div>
    <hr>
    <div class="offerContainer">
        <div class="fnt-light fnt-caps fnt-bold mrgn-b-10">
            APPLY COUPON
        </div>
        <shiro:lacksRole name="<%=RoleConstants.COUPON_BLOCKED%>">
            <div class=''>
                <shiro:hasAnyRoles name="<%=RoleConstants.HK_USER%>">

                    <input class="couponInput" placeholder='Enter discount code' type='text'
                           style="float:left;margin-right:5px;" id="couponCode"/>
                    <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" id="couponLink"
                            onclick="return false;"
                            class="btn btn-gray" style="display:inline-block">APPLY</s:link>
                </shiro:hasAnyRoles>

                <shiro:hasAnyRoles name="<%=RoleConstants.TEMP_USER%>">

                    <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="lrg fnt-caps btn btn-gray"
                            event="pre"> Login to apply
                        <s:param name="redirectUrl" value="${pageContext.request.contextPath}/core/cart/Cart.action"/>
                    </s:link>


                </shiro:hasAnyRoles>
                <shiro:hasAnyRoles name="<%=RoleConstants.HK_UNVERIFIED%>">

                    <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" class="lrg fnt-caps btn btn-gray"
                            event="pre"> Verify your Account
                    </s:link>


                </shiro:hasAnyRoles>

            </div>
        </shiro:lacksRole>
    </div>
    <hr>
    <div class="mrgn-b-20 fnt-light fnt-bold fnt-caps">CHECK OUT DETAILS</div>


    <div style="">
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
            <span class="checkoutContainerTextRight"><fmt:formatNumber
                    value="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount}"
                    type="currency" currencySymbol="Rs. "/>  </span>
          </c:if>
          <c:if test="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount == 0}">
              <span class="checkoutContainerText">Shipping:</span> <span class="checkoutContainerTextRight">Free!</span>
          </c:if>
        </span>
        <br/>

      <span class="special" id="summarySubscriptionDiscountContainer"
            style="display: ${cartAction.pricingDto.subscriptionDiscount > 0 ? 'block':'none'};font-style: initial;">
        <span class="checkoutContainerText" style="font-size: 12px;">Subscription Discount:</span> <strong><span
              id="totalSubscriptionDiscount"
              class="green checkoutContainerTextRight">(<fmt:formatNumber
              value="${cartAction.pricingDto.subscriptionDiscount}" type="currency"
              currencySymbol="Rs. "/>)</span></strong>
      </span>
      <span class="special" id="summaryPromoDiscountContainer"
            style="display: ${cartAction.pricingDto.totalPromoDiscount > 0 ? 'block':'none'};font-style: initial;">
        <span class="checkoutContainerText lightBlue" style="font-size: 11px;">Promo Discount:</span> <strong><span
              id="summaryPromoDiscount"
              class="green lightBlue checkoutContainerTextRight">(<fmt:formatNumber
              value="${cartAction.pricingDto.totalPromoDiscount}" type="currency"
              currencySymbol="Rs. "/>)</span></strong>
      </span>
    </div>


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
    <script type="text/javascript">
        $('#proceed').click(function() {
            $('#cartForm').submit();
        });
    </script>
</div>
<div id="applicableOfferDiv"></div>

<!--div class='orderSummaryHeading'>
<div class="deliveryDetails"> DELIVERY DETAILS</div>
<ul>
<li>
- The time taken for delivery after dispatch from our warehouse varies with location.
</li>
<li>
- For Metros: 1-3 business days
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
</div-->

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
        document.getElementById("vizuryTargeting").src = vizuryLink + "&uid=" + user_hash;
    </script>
</c:if>

</c:if>
<c:set var="comboInstanceIds" value=""/>
<c:set var="comboInstanceIdsName" value=""/>
<c:if test="${cartAction.trimCartLineItems!=null && fn:length(cartAction.trimCartLineItems) >0}">
    <script type="text/javascript">
        $(document).ready(function () {
            ShowDialog(true);
            //              e.preventDefault();
            $('.button_green').live('click', function() {
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
                        <div class='img48'
                             style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
                            <c:choose>
                                <c:when test="${cartLineItem.comboInstance!=null}">
                                    <c:if test="${!fn:contains(comboInstanceIds, cartLineItem.comboInstance.id)}">
                                        <c:set var="comboInstanceIds"
                                               value="${cartLineItem.comboInstance.id}+','+${comboInstanceIds}"/>
                                        <c:choose>
                                            <c:when test="${cartLineItem.comboInstance.combo.mainImageId != null}">
                                                <hk:productImage
                                                        imageId="${cartLineItem.comboInstance.combo.mainImageId}"
                                                        size="<%=EnumImageSize.TinySize%>"/>
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
                                            <hk:productImage
                                                    imageId="${cartLineItem.productVariant.product.mainImageId}"
                                                    size="<%=EnumImageSize.TinySize%>"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="prod48"
                                                 src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.productVariant.product.id}.jpg"
                                                 alt="${storeVariantBasic.name}"/>
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
                                                    <c:set var="comboInstanceIdsName"
                                                           value="${cartLineItem.comboInstance.id}+','+${comboInstanceIdsName}"/>
                                                    ${cartLineItem.comboInstance.combo.name} <br/>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                ${storeVariantBasic.name}
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
            </td>
            <td>
                </c:if>
                <s:link beanclass="com.hk.web.action.core.cart.CartAction" class=" button_green"
                        style="width: 160px; height: 18px;">Back to Shopping
                </s:link>
            </td>
        </tr>
    </table>
</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>

<style type="text/css">
    .main_container {
        border: none;
        border-radius: 0px;
        box-shadow: none;
    }

    .jqStepper-down, .jqStepper-up {
        border: 1px solid #999;
        border-radius: 100%;
        height: 20px;
        width: 20px;
        margin-right: 5px;
        margin-top: 2px;
        background: transparent;
        font-size: 12px;
        padding-top: 1px;

    }

    .jqStepper-up {
        margin-right: 0px;
        margin-left: 5px;
    }

    .jqStepper a span {
        visibility: visible;
        font-size: inherit;
        line-height: inherit;
    }

    .remove.removeLink, .remove.removeComboLink {
        position: absolute;
        margin-left: 130px !important;
        height: 20px;
        width: 20px;
        background: transparent;
        margin-top: 0px !important;
        border: 1px solid #999 !important;
        border-radius: 100%;
        font-size: 12px !important;
        padding-top: 1px;
        text-transform: lowercase;
    }

    .tabletitle.tableTitleNew {
        padding-bottom: 4px;
        font-weight: 100;
        font-size: 1.2em;
    }

    .tableTitleNew {
        height: auto;
        border-width: 0px 0px 1px 0px;
    }

    .web_dialog_overlay {
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
        filter: alpha(opacity = 15);
        -moz-opacity: .15;
        z-index: 101;
        display: none;
    }

    .web_dialog {
        display: none;
        position: fixed;
        width: 450px; /*height: 400px;*/
        top: 50%;
        left: 50%;
        margin-left: -265px;
        margin-top: -180px; /*background-color: #ffffff;*/
        background-color: white; /*border: 2px solid #336699;*/
        padding: 0px;
        z-index: 102;
        font-family: Verdana;
        font-size: 10pt;
        color: #333;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.9), 0 0 5px rgba(0, 0, 0, 0.5), 0 0 10px rgba(0, 0, 0, 0.7), 0 0 25px rgba(0, 0, 0, 0.3);
    }

    .web_dialog_title {
    /*border-bottom: solid 2px #336699;*/
    /*background-color: #336699;*/
        font-size: 16px;
        font-weight: bold;
        padding: 5px;
        background-color: #f2f7fb;
        color: White;
        font-weight: bold;
    }

    .web_dialog_title a {
        color: White;
        text-decoration: none;
    }

    .align_right {
        text-align: right;
    }

</style>
