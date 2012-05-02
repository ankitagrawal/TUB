<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
    ProductDao productDao = InjectorFactory.getInjector().getInstance(ProductDao.class);
    String productId = (String) pageContext.getAttribute("productId");
    Product product = productDao.find(productId);
    pageContext.setAttribute("product", product);
  %>
  <div class='variants'>
    <span
        style="font-style: italic; font-size: 16px;;"> ${fn:length(product.productVariants[0].variantConfig.variantConfigOptions)}</span>
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
        <c:set value="${product.productVariants[0]}" var="variant"/>
        <s:form beanclass="com.hk.web.action.AddToCartAction" class="addToCartForm">

          <div class='row prods'>
            <div class='prod' style="text-align: center;">
              <c:choose>
                <c:when
                    test="${hk:isNotBlank(variant.variantName) && hk:topLevelCategory(variant.product).name != 'eye'}">
                  ${variant.variantName}
                  <br/>
                  <br/>
                </c:when>
                <c:otherwise>
                  <c:forEach items="${variant.productOptions}" var="variantOption">
                    <c:if
                        test="${variantOption.name == 'TYPE' || variantOption.name == 'type' || variantOption.name == 'Type' || variantOption.name == 'BABY WEIGHT' || variantOption.name == 'baby weight' || variantOption.name == 'Baby Weight' || variantOption.name == 'SIZE' || variantOption.name == 'Size' || variantOption.name == 'size' || variantOption.name == 'FLAVOR' || variantOption.name == 'flavor' || variantOption.name == 'Flavor'}">
                      ${variantOption.value}
                      <br/>
                      <br/>
                    </c:if>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
              <c:if test="${variant.discountPercent > 0}">
                <div class="special green" style="text-align: center;">
                                    <span style="font-weight: bold;"><fmt:formatNumber
                                        value="${variant.discountPercent*100}" maxFractionDigits="0"/>%</span>
                  off
                </div>
              </c:if>
            </div>
            <div class='desc'>
              <c:forEach items="${variant.productOptions}" var="variantOption">
                <span
                    style="/*text-transform:lowercase;*/ font-size: 12px; line-height:18px;"> ${variantOption.name}</span><span>: ${variantOption.value}</span><br/>
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
                  Our Price <br/>
                <span class='num' style="font-size: 14px;" :>
                  Rs <fmt:formatNumber value="${hk:getApplicableOfferPrice(variant) + hk:getPostpaidAmount(variant)}"
                                       maxFractionDigits="0"/>
                </span>
                </div>
              </div>
            </c:if>
            <c:if test="${variant.discountPercent == 0}">
              <div class='prices'>
                <div class='hk'>
                <span class='num' style="font-size: 14px;" :>
                  Rs <fmt:formatNumber value="${hk:getApplicableOfferPrice(variant) + hk:getPostpaidAmount(variant)}"
                                       maxFractionDigits="0"/>
                </span>
                </div>
              </div>
            </c:if>

            <div class='add'>
              <c:choose>
                <c:when test="${variant.outOfStock}">
                  <span class="outOfStock">Sold Out</span>
                  <div align="center"><s:link beanclass="com.hk.web.action.NotifyMeAction"
                                              class="notifyMe button_orange"><b>Notify Me!!</b>
                    <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link></div>
                </c:when>
                <c:otherwise>
                  <input type="button" name="showPrescription" class="button_green" value="Buy now"
                         onclick="$('#cartWindow2').jqm(); $('#cartWindow2').jqmShow();"/>
                </c:otherwise></c:choose>
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

        <script type="text/javascript">
          $('.addToCartButton').click(function() {
            $(this).parents().find('.progressLoader').show();
          });
        </script>

      </div>
    </div>
  </div>

</s:layout-definition>