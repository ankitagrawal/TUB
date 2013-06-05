<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.dto.pricing.PricingDto" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Set, java.util.HashSet" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--
Pass an attribute called pricingDto to render a table with pricing details
--%>
<%--<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>--%>
<s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" var="orderSummary" />
<s:layout-definition>
        <%
  PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");
  Date orderDate = (Date) pageContext.getAttribute("orderDate");
  // this line does not mean anything, but simply gets idea to understand pricingDto's type, autocompletion works now.
  pageContext.setAttribute("pricingDto", pricingDto);
  pageContext.setAttribute("orderDate", orderDate);
  if (pricingDto != null && orderDate != null) {
%>

    <c:if test="${pricingDto.grandTotalPayable > 0.00}">
    <div class='totals newTotals2'>
        <div class='left' style="width: 42%;left: 0px;">


            <c:if
                    test="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal > pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}">
                <div class='discount' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                    Total MRP:
                </div>
            </c:if>
            <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                Our Price:
            </div>

            <div class='shipping' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                Shipping:
            </div>
            <c:if test="${pricingDto.redeemedRewardPoints > 0}">
                <div style="font-size: 12px;font-weight: normal;" class="lightBlue">Reward Points</div>
            </c:if>
            <c:if test="${pricingDto.codLineCount > 0}">
                <div style="font-size: 12px;font-weight: normal;" class="shipping">COD Charges</div>
            </c:if>
            <c:if test="${pricingDto.totalCashback > 0.00}">
                <div class='special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);font-style: normal;">
                    Cashback:
                </div>
            </c:if>
        </div>
        <div class='right' style="width: 50%;left: 0px;text-align: right;font-size: 12px;">
            <c:if
                    test="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal > pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}">
                <div class='discount green'>
          <span style="text-decoration:line-through;font-size: 12px;color: rgb(68, 68, 68 );font-weight: normal;font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"><fmt:formatNumber
                  value="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal}"
                  type="currency" currencySymbol="Rs. "/></span>
                </div>
            </c:if>
            <div class='total' style="font-size: 14px;">
                <strong style="font-size: 12px;font-weight: normal;color: rgb(68,68,68);font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
                    <fmt:formatNumber value="${pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}" type="currency" currencySymbol="Rs. "/>
                </strong>
            </div>


            <div class='shipping' style="font-weight:normal;font-size: 12px;color: rgb(68, 68, 68);font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;">
                Rs ${pricingDto.shippingTotal}
            </div>
            <c:if test="${pricingDto.redeemedRewardPoints > 0}">
                <div class="green lightBlue" style="font-size: 12px;color: rgb(68, 68, 68);">
                    Rs <fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>
                </div>
            </c:if>
            <c:if test="${pricingDto.codLineCount > 0}">
                Rs ${pricingDto.codSubTotal}
            </c:if>

            <c:if test="${pricingDto.totalCashback > 0.00}">
                <div class='special' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);font-style: normal;">
                    <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
                </div>
            </c:if>
        </div>
    </div>
</c:if>
<c:if test="${pricingDto.grandTotalPayable == 0.00}">
    <div class='totals newTotals2'>
        <div class='left' style="width: 42%;left: 0px;">

            <c:if
                    test="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal > pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}">
                <div class='discount ' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                    Total MRP:
                </div>
            </c:if>

            <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                Our Price:
            </div>
            <div class='shipping' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                Shipping:
            </div>
            <c:if test="${pricingDto.redeemedRewardPoints > 0}">
                <div style="font-size: 12px;font-weight: normal;" class="lightBlue">Reward Points</div>
            </c:if>
            <c:if test="${pricingDto.totalCashback > 0.00}">
                <div class='discount ' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);font-style: normal;">
                    Cashback:
                </div>
            </c:if>
        </div>
        <div class='right' style="width: 50%;left: 0px;text-align: right;">

            <c:if
                    test="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal > pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}">
                <div class='discount green' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
          <span id="summaryTotalDiscount"><fmt:formatNumber
                  value="${pricingDto.productsMrpSubTotal + pricingDto.prepaidServiceMrpSubTotal + pricingDto.postpaidServiceMrpSubTotal}"
                  type="currency" currencySymbol="Rs. "/></span>
                </div>
            </c:if>

            <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">

                    <fmt:formatNumber value="${pricingDto.productsHkSubTotal + pricingDto.prepaidServiceHkSubTotal + pricingDto.postpaidServiceHkSubTotal}" type="currency" currencySymbol="Rs. "/>

            </div>

            <div class='shipping' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                Rs ${pricingDto.shippingTotal}
            </div>
            <c:if test="${pricingDto.redeemedRewardPoints > 0}">
                <div class="green lightBlue" style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                    Rs <fmt:formatNumber value="${pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=""/>
                </div>
            </c:if>

            <c:if test="${pricingDto.totalCashback > 0.00}">
                <div class='discount green' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);font-style: normal;">
                    <span><fmt:formatNumber value="${pricingDto.totalCashback}" type="currency" currencySymbol="Rs. "/></span>
                </div>
            </c:if>
        </div>
    </div>
</c:if>

    <c:if test="${pricingDto.postpaidServicesTotal > 0.00}">
        <div class='totals newTotals2'>
            <div class='left' style="width: 42%;left: 0px;">
                <div class='discount ' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                    You Saved:
                </div>
                <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
                    Total:
                </div>
            </div>
            <div class='right' style="width: 50%;left: 0px;text-align: right;">
                <div class='discount green ' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">
        <span id="summaryTotalDiscountPostpaid"><fmt:formatNumber
                value="${pricingDto.postpaidServiceMrpSubTotal - pricingDto.postpaidServiceHkSubTotal + pricingDto.postpaidServiceDiscount}"
                type="currency" currencySymbol="Rs. "/></span>
                </div>
                <div class='total' style="font-size: 12px;font-weight: normal;color: rgb(68, 68, 68);">

                        <fmt:formatNumber value="${pricingDto.postpaidServicesTotal}" type="currency" currencySymbol="Rs. "/>

                </div>
            </div>
        </div>
    </c:if>
    <%
        }
    %>

</s:layout-definition>