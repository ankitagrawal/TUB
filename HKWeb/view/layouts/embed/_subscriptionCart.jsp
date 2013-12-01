<%@ page import="com.hk.domain.subscription.SubscriptionProduct" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.domain.subscription.Subscription" %>
<%@ page import="java.util.Set" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 5:14 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
<%
  Set<Subscription> subscriptions = (Set<Subscription>) pageContext.getAttribute("subscriptions");
  pageContext.setAttribute("subscriptions", subscriptions);
%>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
  <div class='tabletitle'>
    <div class='name'>
      Subscription
    </div>
    <div class='quantity'>
      Quantity
    </div>
    <div class='price'>
      Price
    </div>
    <div class='floatfix'></div>
  </div>
  <c:forEach items="${subscriptions}" var="subscription" varStatus="ctr">
    <div class="lineItemRow product">
      <input type="hidden" value="${subscription.id}" class="lineItemId" id="item_${subscription.id}"/>

      <div style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
        <c:choose>
          <c:when test="${subscription.productVariant.product.mainImageId != null}">
            <hk:productImage imageId="${subscription.productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
          </c:when>
          <c:otherwise>
            <img class="prod48"
                 src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${subscription.productVariant.product.id}.jpg"
                 alt="${subscription.productVariant.product.name}"/>
          </c:otherwise>
        </c:choose>
      </div>
      <div class="name" style="font-size: 14px; line-height: 21px;" :>
          ${subscription.productVariant.product.name}
          ${subscription.productVariant.variantName}<br/>
        <table style="display: inline-block; font-size: 12px;">
          <c:forEach items="${subscription.productVariant.productOptions}" var="productOption" varStatus="ctr">
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

        </table>
      </div>
      <div class="quantity">
        <div class="subscriptionQty" > ${subscription.qty}</div>
    <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" class="remove editSubscriptionLink">(edit)
      <s:param name="subscription" value="${subscription}"/>
    </s:link>


        <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" class="remove removeSubscriptionLink" event="abandon">(remove)
          <s:param name="subscription" value="${subscription}"/>
        </s:link>

      </div>
      <div class="price">
        <c:choose>
          <c:when test="${subscription.markedPriceAtSubscription == subscription.subscriptionPrice}">
            <div class="hk">
              <div class="num"> Rs
              <span class="lineItemSubTotalMrp"><fmt:formatNumber
                  value="${subscription.subscriptionPrice * subscription.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
              </div>
            </div>
          </c:when>
          <c:otherwise>
            <div class="cut">
              <div class="num lineItemSubTotalMrp">
                <fmt:formatNumber value="${subscription.markedPriceAtSubscription * subscription.qty}"
                                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
            </div>
            <div class='special green'>
              (saved
                    <span class='num '>
                      Rs  <span class="lineItemSubTotalHkDiscount"><fmt:formatNumber

                        value="${(subscription.markedPriceAtSubscription - subscription.subscriptionPrice - subscription.productVariant.postpaidAmount) * subscription.qty}"
                        pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
                    </span>
            </div>
            <div class="hk">
              <div class="num"> Rs
              <span class="lineItemHkTotal"><fmt:formatNumber
                  value="${subscription.subscriptionPrice * subscription.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
              </div>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
      <div class="floatfix"></div>
    </div>
  </c:forEach>

    <%--  <s:layout-render name="/layouts/embed/_subscription.jsp" subscriptionProduct="${subscriptionProduct}"/> --%>
    <div class="jqmWindow" style="display:none;" id="subscriptionWindow"></div>

    <script type="text/javascript">
      $(document).ready(function(){
        $('#subscriptionWindow').jqm({trigger: '.editSubscriptionLink', ajax: '@href',ajaxText:'<br/><div style="text-align: center;">loading... please wait..</div> <br/>'});
      });

    </script>


  <script type="text/javascript">
    $(document).ready(function(){
      $('.removeSubscriptionLink').click(function() {
        clearTimeout(timeout);
        var itemContainer = $(this).parents('.product');
        var lineItemId = itemContainer.find('.lineItemId').val();
        var lineItemStyleId = itemContainer.find('.lineItemId').attr('id');
        $('#undoLineItemId').val(lineItemId);
        $('#undoQty').val(itemContainer.find('.subscriptionQty').val());
        itemContainer.find('.lineItemQty').val(0);

        $.getJSON($(this).attr('href'), {}, function(responseData) {
          removeSubscriptionItem(lineItemStyleId);
          count = Math.round($('#productsInCart').html());
          subscriptionCount = Math.round($('#subscriptionsInCart').html()) ;
          if (count == 1 || subscriptionCount==1) {
            location.reload();
          }
          else if (count > 2) {
            $('#productsInCart').html(count - 1);
            $('#subscriptionsInCart').html(subscriptionCount-1);
          }
          else {
            $('#subscriptionsInCart').html(subscriptionCount-1);
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
        /*$.getJSON($(this).attr('href'), {}, function(responseData) {
          location.reload();
        });
        return false;*/
      });
      function removeSubscriptionItem(lineItemStyleId) {
        $('#' + lineItemStyleId).parents('.lineItemRow').fadeOut();
      }



    });
  </script>

</s:layout-definition>