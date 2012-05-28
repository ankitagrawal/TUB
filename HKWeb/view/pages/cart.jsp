<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.catalog.product.EnumProductVariantPaymentType" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="lineItem_Service_Postpaid" value="<%=EnumProductVariantPaymentType.Postpaid.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>

<s:layout-render name="/layouts/cartLayout.jsp" pageTitle="Shopping Cart">

<s:layout-component name="topHeading">Shopping Cart</s:layout-component>

<s:layout-component name="htmlHead">
  <script type="text/javascript">
    var timeout; //Set globally as it needs to be reset when removeLink is clicked.
    var timespan = 3000;

    $(document).ready(function() {
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
              $(".freebieBanner").attr("src", responseData.message);
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
          if(responseData.message){
             $(".freebieBanner").attr("src", responseData.message);
          }else{
             $(".freebieBanner").attr("src", "");
          }
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
      $('#summaryProductsMrpSubTotal').html('Rs. ' + responseData.data.totalMrpSubTotal);
      $('#summaryProductsHkpSubTotal').html('Rs. ' + responseData.data.totalHkSubTotal);
      $('#summaryHkDiscount').html('Rs. ' + responseData.data.totalHkDiscount);
      if (responseData.data.totalPromoDiscount == "0.00") {
        $('#summaryPromoDiscountContainer').hide();
      } else {
        $('#summaryPromoDiscountContainer').show();
      }
      $('#summaryPromoDiscount').html('Rs. ' + responseData.data.totalPromoDiscount);

      if (responseData.data.totalCashback == "0.00") {
        $('#summaryTotalCashbackContainer').hide();
      } else {
        $('#summaryTotalCashbackContainer').show();
      }
      $('#summaryTotalCashback').html('Rs. ' + responseData.data.totalCashback);

      if (responseData.data.shippingTotal == "0.00") {
        $('#summaryShippingSubTotal').html('Shipping: Free!');
      } else {
        $('#summaryShippingSubTotal').html('Shipping: Rs. ' + (responseData.data.shippingTotal));
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
  <div class="jqmWindow" id="couponWindow" style="display:none">
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
        <span class='num'>${cartAction.pricingDto.productLineCount}</span>
        item in your shopping cart
      </c:when>
      <c:when test="${cartAction.pricingDto.productLineCount > 1}">
        There are
        <span class='num' id="numProdTitle">${cartAction.pricingDto.productLineCount}</span>
        items in your shopping cart
      </c:when>
      <c:otherwise>
        Your cart is empty. Please go back to add products to it.
      </c:otherwise>
    </c:choose>
  </h2>
  <c:if test="${cartAction.pricingDto.productLineCount > 0}">
    <a href="/" class="back"> &larr; go back to add more products</a>
  </c:if>
  <c:if test="${cartAction.pricingDto.productLineCount == 0}">
    <a href="/" class="back"> &larr; go back to add products to your shopping cart</a>
  </c:if>
</s:layout-component>
<c:if test="${cartAction.pricingDto.productLineCount >= 1}">
<s:layout-component name="cart_items">
<div class='products_container' style="min-height: 500px;">
<div class='tabletitle'>
  <div class='name'>
    Product
  </div>
  <div class='quantity'>
    Quantity
  </div>
  <div class='price'>
    Price
  </div>
  <div class='floatfix'></div>
</div>
<s:form beanclass="com.hk.web.action.core.cart.CartAction" id="cartForm">
<div style="display: none;">
  <s:link beanclass="com.hk.web.action.core.order.CartLineItemUpdateAction" id="lineItemUpdateLink"></s:link>
  <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" style="display:none;" id="couponBaseLink"></s:link>
  <s:link beanclass="com.hk.web.action.core.cart.CartAction" style="display:none;" id="updatePricingLink" event="pricing"></s:link>
</div>

<c:forEach items="${cartAction.order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
  <div class="lineItemRow product">
    <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

    <div style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
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
    </div>
    <div class="name" style="font-size: 14px; line-height: 21px;" :>
        ${cartLineItem.productVariant.product.name}
        ${cartLineItem.productVariant.variantName}<br/>
      <table style="display: inline-block; font-size: 12px;">
        <c:forEach items="${cartLineItem.productVariant.productOptions}" var="productOption" varStatus="ctr">
        <tr>
          <td style="text-align: right;  padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fafafa;">${productOption.name}</td>
          <td style="text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fff;">
            <c:if test="${fn:startsWith(productOption.value, '-')}">
              ${productOption.value}
            </c:if>
            <c:if test="${!fn:startsWith(productOption.value, '-')}">
              &nbsp;${productOption.value}
            </c:if>
          </td>
          </c:forEach>
          <br/>
          <c:forEach items="${cartLineItem.cartLineItemExtraOptions}" var="extraOption">
        <tr>
          <td style="text-align: left;  padding: 5px; border: 1px solid #f0f0f0;background: #fafafa;">${extraOption.name}</td>
          <td style="text-align: left; padding: 0px;border: 1px solid #f0f0f0;background: #fff;">
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
    <div class="quantity">
      <input value="${cartLineItem.qty}" size="1" class="lineItemQty" style="width: 20px; height: 18px;"/>
      <a class='remove removeLink' href='#'>
        (remove)
      </a>
    </div>
    <div class="price">
      <c:choose>
        <c:when test="${cartLineItem.markedPrice == cartLineItem.hkPrice}">
          <div class="hk">
            <div class="num"> Rs
              <span class="lineItemSubTotalMrp"><fmt:formatNumber
                  value="${cartLineItem.hkPrice * cartLineItem.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <div class="cut">
            <div class="num lineItemSubTotalMrp">
              <fmt:formatNumber value="${cartLineItem.markedPrice * cartLineItem.qty}"
                                pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
          </div>
          <div class='special green'>
            (saved
                    <span class='num '>
                      Rs  <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber

                        value="${(cartLineItem.markedPrice - cartLineItem.hkPrice + cartLineItem.productVariant.postpaidAmount) * cartLineItem.qty}"
                        pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
                    </span>
          </div>
          <div class="hk">
            <div class="num"> Rs
              <span class="lineItemHkTotal"><fmt:formatNumber
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
    </div>
    <div class="floatfix"></div>
  </div>
</c:forEach>

<c:forEach items="${cartAction.order.exclusivelyComboCartLineItems}" var="cartLineItem" varStatus="ctr1">
  <div class="lineItemRow product">
    <input type="hidden" value="${cartLineItem.id}" class="lineItemId" id="item_${cartLineItem.id}"/>

    <div style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
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
    </div>
    <div class="name" style="font-size: 14px; line-height: 21px;">
        ${cartLineItem.comboInstance.combo.name}<br/>
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
    <div class="quantity">
      <input value="${hk:getComboCount(cartLineItem)}" size="1" class="comboQty" style="width: 20px; height: 18px;"/>
      <a class='remove removeComboLink' href='#'>
        (remove)
      </a>
    </div>
    <div class="price">
      <c:choose>
        <c:when test="${cartLineItem.markedPrice == cartLineItem.hkPrice}">
          <div class="hk">
            <div class="num"> Rs
      <span class="lineItemSubTotalMrp"><fmt:formatNumber value="${cartLineItem.markedPrice}"
                                                          pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <div class="cut">
            <div class="num lineItemSubTotalMrp">
              <fmt:formatNumber value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem)}"
                                pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
          </div>
          <div class='special green'>
            (saved
      <span class='num '>
      Rs <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber
          value="${cartLineItem.comboInstance.combo.markedPrice * hk:getComboCount(cartLineItem) - cartLineItem.comboInstance.combo.hkPrice * hk:getComboCount(cartLineItem)}"
          pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
      </span>
          </div>
          <div class="hk">
            <div class="num"> Rs
      <span class="lineItemHkTotal"> <fmt:formatNumber
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

<c:if test="${cartAction.pricingDto.productLineCount > 0}">
  <s:link beanclass="com.hk.web.action.HomeAction" class="back"> &larr; go back to add more products</s:link>
</c:if>
<c:if test="${cartAction.pricingDto.productLineCount == 0}">
  <s:link beanclass="com.hk.web.action.HomeAction"
          class="back"> &larr; go back to add products to your shopping cart</s:link>
</c:if>
</div>

<shiro:lacksRole name="<%=RoleConstants.COUPON_BLOCKED%>">
    <div class='right_container coupon'>
  <shiro:hasAnyRoles name="<%=RoleConstants.HK_USER%>">
    <h6>Got a discount coupon?</h6>
    <br/>
    Enter Coupon Code

    <input placeholder='d-i-s-c-o-u-n-t' type='text' id="couponCode"/>
    <s:link beanclass="com.hk.web.action.core.discount.ApplyCouponAction" id="couponLink" onclick="return false;"
            class="button_grey">Apply Coupon</s:link>
    <s:link beanclass="com.hk.web.action.core.discount.AvailabeOfferListAction"
            id="availableOffersLink">(see previously applied offers)</s:link>
  </shiro:hasAnyRoles>
  <shiro:hasAnyRoles name="<%=RoleConstants.TEMP_USER%>">
    Got a discount coupon?
    <br/>
    <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="lrg" event="pre"> login / signup
      <s:param name="redirectUrl" value="${pageContext.request.contextPath}/Cart.action"/>
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
<div class='right_container total'>
<h5>Checkout</h5>
<br/>

<div style="font-weight: normal; color: #888; text-align: center; line-height: 21px;">
  <c:if
      test="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal + cartAction.pricingDto.postpaidServiceMrpSubTotal > cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal + cartAction.pricingDto.postpaidServiceHkSubTotal}">
          <span class="cut" style="text-align: center;">
            Total MRP: <span id="summaryProductsMrpSubTotal" style="text-decoration: line-through;"> <fmt:formatNumber
              value="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal + cartAction.pricingDto.postpaidServiceMrpSubTotal}"
              type="currency" currencySymbol="Rs. "/></span>
          </span>
    <br/>
  </c:if>
        <span class="" style="text-align: center;">
          Our Price: <span id="summaryProductsHkpSubTotal"> <fmt:formatNumber
            value="${cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal + cartAction.pricingDto.postpaidServiceHkSubTotal}"
            type="currency" currencySymbol="Rs. "/></span>
        </span>
  <br/>
  <c:if
      test="${cartAction.pricingDto.productsMrpSubTotal + cartAction.pricingDto.prepaidServiceMrpSubTotal > cartAction.pricingDto.productsHkSubTotal + cartAction.pricingDto.prepaidServiceHkSubTotal +cartAction.pricingDto.postpaidServiceHkSubTotal}">
        <span class="special">
          <span style="font-size: 11px;">HealthKart Discount</span>: <br/><strong>(<span id="summaryHkDiscount"
                                                                                         class="green"><fmt:formatNumber
            value="${cartAction.pricingDto.totalHkProductsDiscount + cartAction.pricingDto.totalHkPrepaidServiceDiscount + cartAction.pricingDto.totalHkPostpaidServiceDiscount - cartAction.pricingDto.totalPostpaidAmount}"
            type="currency" currencySymbol="Rs. "/></span>)</strong>
        </span><br/>
  </c:if>
        <span id="summaryShippingSubTotal">
          <c:if test="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount >= .005}">
            Shipping:
            <fmt:formatNumber value="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount}"
                              type="currency" currencySymbol="Rs. "/>
          </c:if>
          <c:if test="${cartAction.pricingDto.shippingSubTotal - cartAction.pricingDto.shippingDiscount == 0}">
            Shipping: Free!
          </c:if>
        </span>
  <br/>
      <span class="special" id="summaryPromoDiscountContainer"
            style="display: ${cartAction.pricingDto.totalPromoDiscount > 0 ? 'block':'none'};">
        <span style="font-size: 11px;">Promo Discount</span>: <br/><strong>(<span id="summaryPromoDiscount"
                                                                                  class="green"><fmt:formatNumber
          value="${cartAction.pricingDto.totalPromoDiscount}" type="currency" currencySymbol="Rs. "/></span>)</strong>
      </span>
</div>

<h1>
      <span class="special" style="text-align: center; font-size: 10px; font-weight: bold;">
        you pay
      </span>
  <br/>
  <strong>
        <span id="summaryGrandTotalPayable">
          <fmt:formatNumber value="${cartAction.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </span>
  </strong>
</h1>

<div class='small'>
  (inclusive of shipping, handling and taxes.)
</div>

<div style="text-align:center; padding-top: 10px; display: ${cartAction.pricingDto.totalCashback > 0 ? 'block':'none'};"
     id="summaryTotalCashbackContainer">
  <span class="special">
    <span style="font-size: 11px;">You will get cashback</span>: <br/><strong>(<span id="summaryTotalCashback"
                                                                                     class="green"><fmt:formatNumber
      value="${cartAction.pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>)</strong>
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

<s:hidden name="order" value="${cartAction.order}"/>
<s:submit name="checkout" value="Place order >" class="button" style="font-size: 1em"/>

</s:form>
<script type="text/javascript">
  $('#proceed').click(function() {
    $('#cartForm').submit();
  });
</script>
</div>

<s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="qbr7CMDf6QIQuLjI5QM" id="1018305592"/>

<iframe src="" id="vizuryTargeting" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
<script type="text/javascript">
  var vizuryLink = "http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e400";
  <c:forEach items="${cartAction.order.productCartLineItems}" var="cartLineItem" varStatus="liCtr">
  vizuryLink += "&pid${liCtr.count}=${cartLineItem.productVariant.product.id}&catid${liCtr.count}=${cartLineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${cartLineItem.qty}";
  </c:forEach>
  vizuryLink += "&currency=INR&section=1&level=1";
  document.getElementById("vizuryTargeting").src = vizuryLink;
</script>
</s:layout-component>
</c:if>

</s:layout-render>
