<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.subscription.SubscriptionProduct" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
<c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
<c:set var="imageSmallSize" value="<%=EnumImageSize.SmallSize%>"/>
<s:layout-definition>
  <%
      Product product = (Product) pageContext.getAttribute("product");
      pageContext.setAttribute("product", product);

      boolean isSecure = pageContext.getRequest().isSecure();
      pageContext.setAttribute("isSecure", isSecure);

      SubscriptionProduct subscriptionProduct = (SubscriptionProduct) pageContext.getAttribute("subscriptionProduct");
      pageContext.setAttribute("subscriptionProduct", subscriptionProduct);
  %>
<div class='variants' itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer-aggregate">
    <span style="font-style: italic; font-size: 16px;;"> ${hk:getNonDeletedVariants(product)}</span>
    variants
    available.
    <span>Please select Desired Product Variant(s) &darr;</span>

    <div class='prod_table'>
      <div class='row header'>
        <div class='prod' style="text-align: center;">
          Variant
        </div>
        <div class='desc'>
          Description
        </div>
        <div class='prices'>
          Price
        </div>
        <div class='add'>
          Order
        </div>
        <div class='floatfix'></div>
      </div>
      <div class='table_body'>

        <c:forEach items="${product.productVariantsWithOutOfStockListedBelow}" var="variant" varStatus="ctr">
          <c:if test="${!variant.deleted}">
            <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">

              <div class='row prods'>
                <div class='prod' style="text-align: center;">
                  <c:choose>
                      <c:when test="${variant.mainImageId != null}">
                          <c:choose>
                              <c:when test="${variant.mainProductImageId != null}">
                                  <a href='javascript:void(0);'
                                     rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, variant.mainProductImageId,isSecure)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, variant.mainProductImageId,isSecure)}'}"><img
                                          src='${hk:getS3ImageUrl(imageSmallSize, variant.mainImageId,isSecure)}' style="height:75px;width: 75px;"></a>
                              </c:when>
                              <c:otherwise>
                                  <img src="${hk:getS3ImageUrl(imageSmallSize, product.mainImageId,isSecure)}" alt="${product.name}"
                                       title="${product.name}"  style="height:75px;width:75px;">
                              </c:otherwise>
                          </c:choose>
                          <br/>
                          <br/>
                          <c:if test="${hk:isNotBlank(variant.variantName) && hk:topLevelCategory(variant.product).name != 'eye'}">
                              ${variant.variantName}
                          </c:if>
                      </c:when>
                      <c:otherwise>
                          <c:choose>
                              <c:when
                                      test="${hk:isNotBlank(variant.variantName) && hk:topLevelCategory(variant.product).name != 'eye'}">
                                  ${variant.variantName}
                                  <br/>
                              </c:when>
                              <c:otherwise>
                                  <c:forEach items="${variant.productOptions}" var="variantOption">
                                      <span style="font-size: 12px; line-height:18px;">${variantOption.value}</span>
                                      <br/>
                                  </c:forEach>
                              </c:otherwise>
                          </c:choose>
                      </c:otherwise>
                  </c:choose>
                    <br/><br/>
                </div>
                <div class='desc'>
                    <c:forEach items="${variant.productOptions}" var="variantOption">
                        <span style="font-size: 12px; line-height:18px;"> ${variantOption.name}</span><span>: ${variantOption.value}</span><br/>
                    </c:forEach>
                </div>
                <c:if test="${variant.discountPercent > 0}">
                    <div class='prices'>
                        <div class='cut'>
                <span class='num'>
                  Rs <fmt:formatNumber value="${variant.markedPrice}" maxFractionDigits="0"/>
                </span>
                        </div>
                        <div class='hk'>
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${hk:getApplicableOfferPrice(variant) + hk:getPostpaidAmount(variant)}"
                                       maxFractionDigits="0"/>
                </span>
	                        <c:if test="${variant.discountPercent > 0}">
                              <div class="special green" style="text-align: center;font-size:1.1em;">
                                                <span style="font-weight: bold;"><fmt:formatNumber
                                                        value="${variant.discountPercent*100}"
                                                        maxFractionDigits="0"/>%</span>
                                  off
                              </div>
                          </c:if>
                        </div>
                        <br/><br/>
	                    <%-- Commented below as it was showing discount twice--%>
                        <%--<c:if test="${variant.mainImageId != null}">
                            <div class="special green" style="text-align: center;font-size: 12px">
                                                <span style="font-weight: bold;"><fmt:formatNumber
                                                        value="${variant.discountPercent*100}"
                                                        maxFractionDigits="0"/>%</span>
                                off
                            </div>
                        </c:if>--%>
                    </div>
                </c:if>
                <c:if test="${variant.discountPercent == 0}">
                  <div class='prices'>
                    <div class='hk'>
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${hk:getApplicableOfferPrice(variant) + hk:getPostpaidAmount(variant)}"
                                       maxFractionDigits="0"/>
                </span>
                    </div>
                  </div>
                </c:if>

                <div class='add' itemprop="availability" content="${variant.outOfStock ? '' : 'in_stock'}">
                  <c:choose>
                    <c:when test="${variant.outOfStock}">
                      <%--<c:choose>--%>
                        <%--<c:when test="${variant.product.brand == 'Absolute'}">--%>
                          <%--<div class="outOfStock">Coming soon...</div>--%>
                          <%--<br/>--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>
                          <span class="outOfStock">Sold Out</span><br/>

                          <div align="center">
                            <s:link beanclass="com.hk.web.action.core.user.NotifyMeAction" class="notifyMe button_orange"><b>Notify
                                                                                                                Me!!</b>
                              <s:param name="productVariant" value="${variant}"/> </s:link></div>
                        <%--</c:otherwise>--%>
                      <%--</c:choose>--%>

                    </c:when>
                    <c:otherwise>
                      <c:if test="${!empty subscriptionProduct}">
                        <div style="text-align: center">
                        <s:link beanclass="com.hk.web.action.core.subscription.SubscriptionAction" class="addSubscriptionButton"><b>Subscribe</b>
                          <s:param name="productVariant" value="${variant}"/> </s:link>
                        </div>
                       </c:if>
                      <s:submit name="addToCart" value="Place Order"
                                class="addToCartButton cta button_green"
                                style="float:right;"/>
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="progressLoader"><img
                    src="${pageContext.request.contextPath}/images/ajax-loader.gif"/>
                </div>
                <s:hidden name="productVariantList[0]" value="${variant.id}"/>
                <s:hidden name="productVariantList[0].qty" value="1" class="lineItemQty"/>
                <s:hidden name="productVariantList[0].selected" value="true"/>
                <div class='floatfix'></div>
              </div>
            </s:form>
          </c:if>
        </c:forEach>
        <script type="text/javascript">
          $('.addToCartButton').click(function() {
            $(this).parent().parent().children('.progressLoader').show();
          });
        </script>

      </div>
    </div>
  </div>

</s:layout-definition>