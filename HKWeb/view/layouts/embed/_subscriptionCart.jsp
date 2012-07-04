<%@ page import="com.hk.domain.subscription.SubscriptionProduct" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 5:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
<%
  List<SubscriptionProduct> subscriptions = (List<SubscriptionProduct>) pageContext.getAttribute("subscriptions");
  pageContext.setAttribute("subscriptions", subscriptions);
%>
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
        <a class='edit editSubscriptionLink' href='#'>
          (edit)
        </a>
        <s:link class='remove removeSubscriptionLink' beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" event="abandonSubscription">
          (remove)
        <s:param name="subscription" value="${subscription}"/> </s:link>

      </div>
      <div class="price">
        <c:choose>
          <c:when test="${subscription.markedPriceAtSubscription == subscription.hkPriceAtSubscription}">
            <div class="hk">
              <div class="num"> Rs
              <span class="lineItemSubTotalMrp"><fmt:formatNumber
                  value="${subscription.hkPrice * subscription.qty}"
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

                        value="${(subscription.markedPriceAtSubscription - subscription.hkPriceAtSubscription - subscription.productVariant.postpaidAmount) * subscription.qty}"
                        pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>)
                    </span>
            </div>
            <div class="hk">
              <div class="num"> Rs
              <span class="lineItemHkTotal"><fmt:formatNumber
                  value="${subscription.hkPriceAtSubscription * subscription.qty}"
                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
              </div>
            </div>
          </c:otherwise>
        </c:choose>

        <c:if test="${lineItem_Service_Postpaid == subscription.productVariant.paymentType.id}">
        <span class="special" style="text-align: center; font-size: 10px; font-weight: bold;">
          to the service provider
        </span>
        </c:if>
      </div>
      <div class="floatfix"></div>
    </div>
  </c:forEach>
  <div style="display: none;">
    <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" event="abandonSubscription" id="removeSubscriptionLink"></s:link>
  </div>
  <script type="text/javascript">
    $(document).ready(function(){
      $('.removeSubscriptionLink').click(function() {
        $.getJSON($(this).attr('href'), {}, function(responseData) {
          location.reload();
        });
        return false;
      });
    });
  </script>

</s:layout-definition>