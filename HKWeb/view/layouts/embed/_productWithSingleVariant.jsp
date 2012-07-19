<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.subscription.SubscriptionProduct" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
      Product product = (Product) pageContext.getAttribute("product");
      pageContext.setAttribute("product", product);
      SubscriptionProduct subscriptionProduct = (SubscriptionProduct) pageContext.getAttribute("subscriptionProduct");
      pageContext.setAttribute("subscriptionProduct", subscriptionProduct);

    CategoryDao categoryDao = (CategoryDao)ServiceLocatorFactory.getService(CategoryDao.class);
    Category eyeGlass = categoryDao.getCategoryByName("eyeglasses");
    pageContext.setAttribute("eyeGlass", eyeGlass);

  %>
  <div class="buy_prod">
    <div class="left_col">
      <div class='prices' style="font-size: 14px;">
        <c:if test="${product.productVariants[0].discountPercent > 0}">
          <div class='cut' style="font-size: 14px;">
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${product.productVariants[0].markedPrice}" maxFractionDigits="0"/>
                </span>
          </div>
          <div class='hk' style="font-size: 16px;">
            Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
                    value="${hk:getApplicableOfferPrice(product.productVariants[0])+ hk:getPostpaidAmount(product.productVariants[0])}"
                    maxFractionDigits="0"/>
                </span>
          </div>
          <div class="special green" style="font-size: 14px;">
            you save
                                <span style="font-weight: bold;"><fmt:formatNumber
                                    value="${product.productVariants[0].discountPercent*100}"
                                    maxFractionDigits="0"/>%</span>
          </div>
        </c:if>
        <c:if test="${product.productVariants[0].discountPercent == 0}">
          <div class='hk' style="font-size: 16px;">
            Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
                    value="${hk:getApplicableOfferPrice(product.productVariants[0]) + hk:getPostpaidAmount(product.productVariants[0])}"
                    maxFractionDigits="0"/>
                </span>
          </div>
        </c:if>
      </div>
      <div style="font-size: 12px; text-align: right; margin-right: 5px;">
        <c:if test="${hk:isNotBlank(product.productVariants[0].optionsCommaSeparated)}">
          ${product.productVariants[0].optionsCommaSeparated}
        </c:if>
      </div>

    </div>
    <div class="right_col">
      <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
        <c:choose>
          <c:when test="${product.productVariants[0].outOfStock}">
            <%--<c:choose>--%>
              <%--<c:when test="${product.brand == 'Absolute'}">--%>
                <%--<div class="outOfStock">Coming soon...</div>--%>
              <%--</c:when>--%>
              <%--<c:otherwise> `--%>
                <span class="outOfStock">Sold Out</span>

                <div align="center"><s:link beanclass="com.hk.web.action.core.user.NotifyMeAction"
                                            class="notifyMe button_orange"><b>Notify
                                                                              Me!!</b>
                  <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link></div>
              <%--</c:otherwise>--%>
            <%--</c:choose>--%>


          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${hk:collectionContains(product.categories, eyeGlass)}">
                <s:button name="addToCart" value="Place Order + Add Prescription"
                          class="eyeGlass cta button_green"/>
              </c:when>
              <c:otherwise>
                  <c:if test="${!empty subscriptionProduct}">

                      <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionAction" class="addSubscriptionButton"><b>Subscribe</b>
                          <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link>
                      &nbsp; <s:link href="#" class="addSubscriptionButton" title="help"><b>?</b> </s:link>
                  </c:if>
                  <s:submit name="addToCart" value="Place Order" class="addToCartButton cta button_green"/>
              </c:otherwise>
            </c:choose>


          </c:otherwise>
        </c:choose>
        <s:hidden name="productVariantList[0]" value="${product.productVariants[0].id}"/>
        <s:hidden name="productVariantList[0].qty" value="1" class="lineItemQty"/>
        <s:hidden name="productVariantList[0].selected" value="true"/>
      </s:form>
      <script type="text/javascript">
        $('.addToCartButton').click(function() {
          $(this).parents().find('.progressLoader').show();
        });
        $('.eyeGlass').click(function() {
          $('#eyeGlassWindow').jqm();
          $('#eyeGlassWindow').jqmShow();
        });
      </script>
     <s:layout-render name="/layouts/embed/_hkAssistanceMessageForSingleVariant.jsp"/>
    </div>
    <script type="text/javascript">
      $(document).ready(function() {
        var l = $('.buy_prod .left_col').height();
        var r = $('.buy_prod .right_col').height();
        if (l > r) {
          $('.buy_prod .right_col').height(l);
        }
        if (l < r) {
          $('.buy_prod .left_col').height(r);
        }
      });
    </script>
  </div>

</s:layout-definition>