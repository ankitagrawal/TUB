<%@ page import="mhc.domain.Product" %>
<%@ page import="mhc.service.dao.ProductDao" %>
<%@ page import="app.bootstrap.guice.InjectorFactory" %>
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
            <s:form beanclass="web.action.AddToCartAction" class="addToCartForm">

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
                                                    value="${variant.discountPercent*100}"
                                                    maxFractionDigits="0"/>%</span>
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
                      <%--<c:choose>--%>
                        <%--<c:when test="${variant.product.brand == 'Absolute'}">--%>
                          <%--<div class="outOfStock">Coming soon...</div>--%>
                          <%--<br/>--%>
                        <%--</c:when>--%>
                        <%--<c:otherwise>--%>
                          <span class="outOfStock">Sold Out</span><br/>

                          <div align="center">
                            <s:link beanclass="web.action.NotifyMeAction" class="notifyMe button_orange"><b>Notify
                                                                                                                Me!!</b>
                              <s:param name="productVariant" value="${variant}"/> </s:link></div>
                        <%--</c:otherwise>--%>
                      <%--</c:choose>--%>

                    </c:when>
                    <c:otherwise>
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