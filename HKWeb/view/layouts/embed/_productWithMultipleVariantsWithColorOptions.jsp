<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
<c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
<c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
<s:layout-definition>
  <%
      Product product = (Product) pageContext.getAttribute("product");
      pageContext.setAttribute("product", product);

  %>
  <div class='variants' itemprop="offerDetails" itemscope itemtype="http://data-vocabulary.org/Offer-aggregate">
    <div class='add_to_cart'>
      <div class='tip'>
        <div class='prices' style="font-size: 14px;">
          <c:if test="${product.productVariants[0].discountPercent > 0}">
            <div class='cut' style="font-size: 14px;">
                      <span class='num' style="font-size: 14px;">
                        Rs <fmt:formatNumber value="${product.productVariants[0].markedPrice}"
                                             maxFractionDigits="0"/>
                      </span>
            </div>
            <div class='hk' style="font-size: 14px;">
              Our Price
                      <span class='num' style="font-size: 18px;">
                        Rs <fmt:formatNumber
                          value="${hk:getApplicableOfferPrice(product.productVariants[0]) + hk:getPostpaidAmount(product.productVariants[0])}"
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
            <div class='hk' style="font-size: 14px;">
              Our Price
                        <span class='num' style="font-size: 18px;">
                          Rs <fmt:formatNumber
                            value="${hk:getApplicableOfferPrice(product.productVariants[0]) + hk:getPostpaidAmount(product.productVariants[0])}"
                            maxFractionDigits="0"/>
                        </span>
            </div>
          </c:if>
        </div>
        <c:if test="${hk:isNotBlank(product.productVariants[0].productOptionsWithoutColor)}">
          ${product.productVariants[0].productOptionsWithoutColor}
           <c:if test="${product.service}" >
               <br/>
               ${product.productVariants[0].variantName}
           </c:if>
        </c:if>
      </div>
    </div>
  </div>
  <br>
  <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
    <div style="display: none;">
      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction" id="updateProductVariantImageLink"
              event="changeProductLink"/>
    </div>
    <div>
      <div style="float:left;">
          <ul class="thumblist">
              <c:forEach items="${product.productVariants}" var="variant" varStatus="ctr">
                  <c:if test="${!variant.outOfStock && !variant.deleted}">

                          <input type="hidden" class="variantMainImageId" value="${variant.mainProductImageId}"/>
                          <c:choose>
                              <c:when test="${hk:isNotBlank(variant.colorHex)}">
                                  <div class="color_box">
                                      <c:choose>
                                          <c:when test="${variant.mainProductImageId != null}">
                                              <li><a href='javascript:void(0);'
                                                     rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, variant.mainProductImageId)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, variant.mainProductImageId)}'}">
                                                  <div style="height: 40px; width: 40px; background-color:${variant.colorHex};"
                                                       title="${variant.colorOptionsValue}">
                                                      &nbsp;</div>
                                              </a>
                                                  <s:checkbox name="productVariantList[${ctr.index}].selected"
                                                              class="checkbox"
                                                              style="align:center;padding-left:2px;"/>
                                              </li>
                                          </c:when>
                                          <c:otherwise>
                                              <div style="height: 40px; width: 40px; background-color:${variant.colorHex};"
                                                   title="${variant.colorOptionsValue}">
                                                  &nbsp;</div>
                                              <s:checkbox name="productVariantList[${ctr.index}].selected"
                                                          class="checkbox" style="align:center;padding-left:2px;"/>
                                          </c:otherwise>
                                      </c:choose>
                                  </div>
                              </c:when>
                              <c:otherwise>
                                  <c:if test="${variant.mainImageId != null}">
                                      <c:choose>
                                          <c:when test="${variant.mainProductImageId != null}">
                                              <li><a href='javascript:void(0);'
                                                     rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, variant.mainProductImageId)}',largeimage: '${hk:getS3ImageUrl(imageLargeSize, variant.mainProductImageId)}'}"><img
                                                      src='${hk:getS3ImageUrl(imageSmallSize, variant.mainImageId)}'
                                                      title="${variant.colorOptionsValue}"></a>
                                                  <s:checkbox name="productVariantList[${ctr.index}].selected"
                                                              class="checkbox" style="align:center;padding-left:2px;"/>
                                              </li>
                                          </c:when>
                                          <c:otherwise>
                                              <div class="color_box">
                                                  <img src="${hk:getS3ImageUrl(imageSmallSize, variant.mainImageId)}"
                                                       alt="${product.name}"
                                                       title="${variant.colorOptionsValue}">
                                                  <s:checkbox name="productVariantList[${ctr.index}].selected"
                                                              class="checkbox" style="align:center;padding-left:2px;"/>
                                              </div>
                                          </c:otherwise>
                                      </c:choose>
                                  </c:if>
                              </c:otherwise>
                          </c:choose>
                      <s:hidden name="productVariantList[${ctr.index}]" value="${variant.id}"/>
                      <s:hidden name="productVariantList[${ctr.index}].qty" value="1" class="lineItemQty"/>
                  </c:if>
              </c:forEach>
          </ul>
      </div>
      <div class='add' style="float:right;">
        <c:choose>
          <c:when test="${product.outOfStock}">
            <span class="outOfStock">Sold Out</span>

            <div align="center"><s:link beanclass="com.hk.web.action.core.user.NotifyMeAction"
                                        class="notifyMe button_orange"><b>Notify
              Me!!</b>
              <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link></div>
          </c:when>
          <c:otherwise>
            <div class="checkboxError" style="background-color:salmon; font-size:0.9em;"></div>  
            <s:submit name="addToCart" value="Place Order"
                      class="addToCartButton cta button_green"
                      style="float:right;"/>
          </c:otherwise>
        </c:choose>

      </div>
    </div>
    <div class="progressLoader"><img
        src="${pageContext.request.contextPath}/images/ajax-loader.gif"/>
    </div>
    <div class='floatfix'></div>
  </s:form>
  <script type="text/javascript">
    validateCheckbox = 1;
  </script>
</s:layout-definition>
