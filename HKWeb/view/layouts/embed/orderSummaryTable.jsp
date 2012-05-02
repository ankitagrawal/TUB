<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="web.action.core.order.OrderSummaryAction" %>
<%@ page import="mhc.common.dto.PricingDto" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--
Pass an attribute called pricingDto to render a table with pricing details
--%>
<s:layout-definition>
  <%
    PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");
    // this line does not mean anything, but simply gets idea to understand pricingDto's type, autocompletion works now.
    pageContext.setAttribute("pricingDto", pricingDto);
    if (pricingDto != null) {
  %>
  <table id="orderSummaryTable">
    <tr>
      <td>
        <strong>Healthkart Price</strong>
      </td>
      <td>
        <span id="summaryProductsMrpSubTotal" class="markedPrice" style="text-decoration:line-through">
        <fmt:formatNumber value="${pricingDto.payableMrpSubTotal}" type="currency" currencySymbol="Rs. "/>
        </span><br/>
        <span id="summaryProductsHkSubTotal">
        <fmt:formatNumber value="${pricingDto.payableHkSubTotal}" type="currency" currencySymbol="Rs. "/>
        </span>
      </td>
    </tr>
    <tr>
      <td>
        <strong>Shipping</strong>
      </td>
      <td>
        <span id="summaryShippingSubTotal">
        <fmt:formatNumber value="${pricingDto.shippingSubTotal - pricingDto.shippingDiscount}" type="currency" currencySymbol="Rs. "/>
        </span>
      </td>
    </tr>
    <tr style="${pricingDto.payableDiscount  - pricingDto.shippingDiscount > 0 ? '' : 'display:none;'}">
      <td><strong>Discount</strong></td>
      <td>
        <span id="summaryTotalDiscount" class="grn">
        (<fmt:formatNumber value="${pricingDto.payableDiscount  - pricingDto.shippingDiscount}" type="currency" currencySymbol="Rs. "/>)
        </span>
      </td>
    </tr>
    <tfoot>
    <tr>
      <td><strong>Grand Total</strong></td>
      <td>
        <strong>
          <span id="summaryGrandTotal"><fmt:formatNumber value="${pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/></span>
        </strong>
      </td>
    </tr>
    </tfoot>
  </table>
  <c:if test="${pricingDto.postpaidServicesTotal > 0}">
  <table id="orderSummaryTable">
    <tr>
      <td>
        <strong>Healthkart Price</strong>
      </td>
      <td>
        <span id="summaryProductsMrpSubTotalPostpaid" class="markedPrice" style="text-decoration:line-through">
        <fmt:formatNumber value="${pricingDto.postpaidServiceMrpSubTotal}" type="currency" currencySymbol="Rs. "/>
        </span><br/>
        <span id="summaryProductsHkSubTotalPostpaid">
        <fmt:formatNumber value="${pricingDto.postpaidServiceHkSubTotal}" type="currency" currencySymbol="Rs. "/>
        </span>
      </td>
    </tr>
    <tr style="${pricingDto.payableDiscount > 0 ? '' : 'display:none;'}">
      <td><strong>Discount</strong></td>
      <td>
        <span id="summaryTotalDiscountPostpaid" class="grn">
        (<fmt:formatNumber value="${pricingDto.postpaidServiceDiscount}" type="currency" currencySymbol="Rs. "/>)
        </span>
      </td>
    </tr>
    <tfoot>
    <tr>
      <td><strong>Grand Total</strong></td>
      <td>
        <strong>
          <span id="summaryGrandTotalPostpaid"><fmt:formatNumber value="${pricingDto.postpaidServicesTotal}" type="currency" currencySymbol="Rs. "/></span>
        </strong>
      </td>
    </tr>
    </tfoot>
  </table>
  </c:if>
  <%
  } else {
  %>
  Pass an attribute called pricingDto with a <%=PricingDto.class.getName()%> object to render a table with pricing details
  <%
    }
  %>
</s:layout-definition>
