<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.order.OrderDetailsAction" event="pre" var="oa"/>
<c:set var="shippingOrderStatus_shipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatus_delivered" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatus_returned" value="<%=EnumShippingOrderStatus.SO_Returned.getId()%>"/>
<c:set var="shippingOrderStatus_lost" value="<%=EnumShippingOrderStatus.SO_Lost.getId()%>"/>
<c:set var="shippingOrderStatus_cancelled" value="<%=EnumShippingOrderStatus.SO_Cancelled.getId()%>"/>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Order Details</s:layout-component>
  <s:layout-component name="left_col">
    <div id="error"></div>
    <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
      <table class="cont footer_color" style="font-size: 14px;">
        <tr>
          <th><s:checkbox name="" id="markAll"/>Mark All</th>
          <th>Item</th>
          <th>Quantity</th>
          <th>Tracking Details</th>
        </tr>
        <c:choose>
          <c:when test="${!empty oa.lineItems}">
            <c:forEach items="${oa.lineItems}" var="lineItem" varStatus="ctr">
              <c:set var="cartLineItem" value="${lineItem.cartLineItem}"/>
              <c:set var="variant" value="${lineItem.sku.productVariant}"/>
              <tr>
                <td>
                  <c:if test="${!(variant.deleted || variant.product.deleted || variant.outOfStock)}">
                    <s:hidden name="productVariantList[${ctr.index}]" value="${variant.id}"/>
                    <s:checkbox name="productVariantList[${ctr.index}].selected" class="lineItemCheckBox"/>
                    <s:hidden name="productVariantList[${ctr.index}].qty" value="1" class="lineItemQty"/>
                  </c:if>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${variant.deleted || variant.product.deleted}">
                      <strong>${variant.product.name}</strong><span class="gry" id="message">&nbsp;&nbsp;(Product not available)</span>
                    </c:when>
                    <c:otherwise>
                      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link" id="productLink">
                        <s:param name="productId" value="${variant.product.id}"/>
                        <s:param name="productSlug" value="${variant.product.slug}"/>
                        <strong>${variant.product.name}</strong>
                      </s:link>
                      <c:if test="${variant.outOfStock}">
                        <span class="gry" id="message">&nbsp;&nbsp;(Product out of stock)</span>
                      </c:if>
                    </c:otherwise>
                  </c:choose>
                <span class="gry">
                  <p id="options">
                      ${variant.optionsPipeSeparated}

                    <c:if test="${!empty cartLineItem.cartLineItemExtraOptions}">
                      ${hk:getExtraOptionsAsString(cartLineItem, "|")}
                    </c:if>
                    <c:if test="${cartLineItem.cartLineItemConfig != null}">
                      ${hk:getExtraOptionsAsString(cartLineItem, "|")}
                    </c:if>
                  </p>
                </span>
                </td>
                <td style="border-bottom:none; padding-bottom: 0;">
                  <fmt:formatNumber value="${lineItem.qty}" maxFractionDigits="0"/>
                </td>
                <td align="right" style="border-bottom:none; padding-bottom: 0;">
                  <c:set var="shipment" value="${lineItem.shippingOrder.shipment}"/>
                  <c:set var="shippingOrder" value="${lineItem.shippingOrder}"/>

                  <c:choose>
                    <c:when
                        test="${shippingOrder.orderStatus.id != shippingOrderStatus_shipped && shippingOrder.orderStatus.id != shippingOrderStatus_delivered
                        && shippingOrder.orderStatus.id != shippingOrderStatus_returned && shippingOrder.orderStatus.id != shippingOrderStatus_lost
                        && shippingOrder.orderStatus.id != shippingOrderStatus_cancelled}">
                      ${shippingOrder.baseOrder.orderStatus.name}
                    </c:when>
                    <c:when test="${shippingOrder.orderStatus.id == shippingOrderStatus_shipped && shipment != null}">
                      Shipped by ${shipment.awb.courier.name} - ${shipment.awb.awbNumber} on <fmt:formatDate
                        value="${shipment.shipDate}"/><br/>
                      <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction" target="_blank">
                        <s:param name="courierId" value="${shipment.awb.courier.id}"/>
                        <s:param name="trackingId" value="${shipment.awb.awbNumber}"/>
                        <s:param name="shippingOrder"  value="${shippingOrder.id}"/>
                        (Track this)
                      </s:link>
                    </c:when>
                    <c:otherwise>
                      ${shippingOrder.orderStatus.name}
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <c:forEach items="${oa.cartLineItems}" var="cartLineItem" varStatus="ctr">
              <tr>
                <td>
                  <c:if
                      test="${!(cartLineItem.productVariant.deleted || cartLineItem.productVariant.product.deleted || cartLineItem.productVariant.outOfStock)}">
                    <s:hidden name="productVariantList[${ctr.index}]" value="${cartLineItem.productVariant.id}"/>
                    <s:checkbox name="productVariantList[${ctr.index}].selected" class="lineItemCheckBox"/>
                    <s:hidden name="productVariantList[${ctr.index}].qty" value="1" class="lineItemQty"/>
                  </c:if>
                </td>
                <td>
                  <c:choose>
                    <c:when
                        test="${cartLineItem.productVariant.deleted || cartLineItem.productVariant.product.deleted}">
                      <strong>${cartLineItem.productVariant.product.name}</strong><span class="gry" id="message">&nbsp;&nbsp;(Product not available)</span>
                    </c:when>
                    <c:when test="${cartLineItem.productVariant.outOfStock}">
                      <strong>${cartLineItem.productVariant.product.name}</strong><span class="gry" id="message">&nbsp;&nbsp;(Product out of stock)</span>
                    </c:when>
                    <c:otherwise>
                      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link" id="productLink">
                        <s:param name="productId" value="${variant.product.id}"/>
                        <s:param name="productSlug" value="${variant.product.slug}"/>
                        <strong>${cartLineItem.productVariant.product.name}</strong>
                      </s:link>
                    </c:otherwise>
                  </c:choose>
                  <span class="gry">
                  <p id="options">
                      ${cartLineItem.productVariant.optionsPipeSeparated}

                    <c:if test="${!empty cartLineItem.cartLineItemExtraOptions}">
                      ${hk:getExtraOptionsAsString(cartLineItem, "|")}
                    </c:if>
                    <c:if test="${cartLineItem.cartLineItemConfig != null}">
                      ${hk:getExtraOptionsAsString(cartLineItem, "|")}
                    </c:if>
                  </p>
                </span>
                </td>
                <td style="border-bottom:none; padding-bottom: 0;">
                  <fmt:formatNumber value="${cartLineItem.qty}" maxFractionDigits="0"/>
                </td>
                <td align="right" style="border-bottom:none; padding-bottom: 0;">
                    ${cartLineItem.order.orderStatus.name}
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </table>
      <div align="center">
        <s:submit style="box-sizing:content-box;" name="addToCart" value="Place Order"
                  class="addToCartButton"/>
      </div>
    </s:form>
  </s:layout-component>
  <s:layout-component name="footer"> </s:layout-component>
</s:layout-render>
<script type="text/javascript">
  $(document).ready(function() {
    $('.addToCartButton').click(function(e) {
      var check = 0;
      $('.lineItemCheckBox').each(function() {
        if ($(this).attr("checked") == "checked") {
          check = 1;
        }
      });
      if (!check) {
        alert("Please select the product(s) to be ordered!");
        return false;
      }
      else {
        show_message();
        e.stopPropagation();
      }
    });

    $(".message .close").click(function() {
      hide_message();
      location.reload(true);
    });

    $(document).click(function() {
      hide_message();
    });

    $('.addToCartForm').ajaxForm({dataType: 'json', success: _addToCart});

    $('#markAll').click(function() {
      if ($(this).attr("checked") == "checked") {
        $('.lineItemCheckBox').each(function() {
          if ($(this).attr("disabled") != "disabled") {
            this.checked = true;
          }
        });
      } else {
        $('.lineItemCheckBox').each(function() {
          this.checked = false;
        });
      }
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

    function _addToCart(res) {
      if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
        $('.lineItemCheckBox').each(function() {
          if ($(this).attr("checked") == "checked") {
            this.checked = false;
            this.disabled = true;
          }
        });
        $('.message .line1').html("<strong>" + res.data.addedProducts + "</strong> has been added to your shopping cart");
        $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
        $('.progressLoader').hide();
      } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
        alert(res.data);
      }
    }
  });
</script>
<style type="text/css">
  p {
    margin-top: 2px;
    margin-bottom: 2px;
    padding: 0;
  }

  table {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 5px;
    border: 1px solid;
    border-collapse: separate;
  }

  table th {
    background: #f0f0f0;
    padding: 5px;
    text-align: left;
  }

  table td {
    padding: 5px;
    text-align: left;
    font-size: small;
  }

  #message, p#options {
    font-size: 0.9em;
  }

  #productLink {
    color: darkslategray;
    font-size: 1.1em;
    height: 0;
  }
</style>