<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.subscription.SubscriptionProduct" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
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
  <div class="buy_prod" itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer">
    <meta itemprop="currency" content="INR" />
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
                  Rs <span itemprop="price"><fmt:formatNumber
                    value="${hk:getApplicableOfferPrice(product.productVariants[0])+ hk:getPostpaidAmount(product.productVariants[0])}"
                    maxFractionDigits="0"/></span>
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
                  Rs <span itemprop="price"><fmt:formatNumber
                    value="${hk:getApplicableOfferPrice(product.productVariants[0]) + hk:getPostpaidAmount(product.productVariants[0])}"
                    maxFractionDigits="0"/></span>
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
    <div class="right_col" itemprop="availability" content="${product.productVariants[0].outOfStock ? '' : 'in_stock'}">
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
                     &nbsp;&nbsp;
                      <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionAction" class="addSubscriptionButton"><b>Subscribe</b>
                          <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link>
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

	    <c:choose>
		    <c:when test="${product.productVariants[0].outOfStock && !empty product.similarProducts}">
			    <div style="font-size: 1.1em;margin-bottom:5px;">We also have the following similar products from other brands :</div>
			    <c:forEach items="${product.similarProducts}" var="similarProduct">
				    <c:set var="sProduct" value="${similarProduct.similarProduct}"/>
				    <s:link href="${sProduct.productURL}" class="prod_link" title="${sProduct.name}">
					    <div class='img128' style="float:left; width:100px;height:100px;padding-right:5px;">
						    <c:choose>
							    <c:when test="${sProduct.mainImageId != null}">
								    <hk:productImage width="100px" height="100px" imageId="${sProduct.mainImageId}"
								                     size="<%=EnumImageSize.SmallSize%>"/>
							    </c:when>
							    <c:otherwise>
								    <img height="100px" width="100px" src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${sProduct.id}.jpg'
								         alt="${sProduct.name}"/>
							    </c:otherwise>
						    </c:choose>
					    </div>
				    </s:link>
			    </c:forEach>
		    </c:when>
		    <c:otherwise>
			    <s:layout-render name="/layouts/embed/_hkAssistanceMessageForSingleVariant.jsp"/>
		    </c:otherwise>
	    </c:choose>

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