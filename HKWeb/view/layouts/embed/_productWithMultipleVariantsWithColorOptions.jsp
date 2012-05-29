<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
    ProductDao productDao = ServiceLocatorFactory.getService(ProductDao.class);
    String productId = (String) pageContext.getAttribute("productId");
    Product product = productDao.getProductById(productId);
    pageContext.setAttribute("product", product);
  %>
  <div class='variants'>
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
        </c:if>
      </div>
    </div>
  </div>

  <br>

  <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
    <div style="display: none;">
      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction" id="updateProductVariantImageLink"
              event="changeProductLink"/>
        <%--<s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" id="updateProductImageLink" event="changeProductImage"/>--%>
    </div>
    <c:forEach items="${product.productVariants}" var="variant" varStatus="ctr">
      <c:if test="${!variant.outOfStock && !variant.deleted}">

        <div class="color_box">
          <input type="hidden" class="variantMainImageId" value="${variant.mainProductImageId}"/>
          <c:choose>
            <c:when test="${hk:isNotBlank(variant.colorHex)}">
              <%--<div style="height: 48px; width: 48px;-moz-border-radius: 5px; border-radius: 5px; background-color:${variant.colorHex};" title="${variant.colorOptionsValue}">--%>
              <div style="height: 40px; width: 40px; background-color:${variant.colorHex};"
                   title="${variant.colorOptionsValue}">
                &nbsp;</div>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${variant.mainImageId != null}">
                  <hk:productImage imageId="${variant.mainImageId}" size="<%=EnumImageSize.TinySize%>"
                                   title="${variant.colorOptionsValue}"/>
                </c:when>
                <c:otherwise>
                  <img
                      src='${pageContext.request.contextPath}/images/ProductImages/ProductImagesOriginal/${variant.id}.jpg'
                      height="35px" width="35px" title="${variant.colorOptionsValue}"/>
                </c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
          <s:checkbox name="productVariantList[${ctr.index}].selected" class="checkbox"/>
        </div>

        <s:hidden name="productVariantList[${ctr.index}]" value="${variant.id}"/>
        <s:hidden name="productVariantList[${ctr.index}].qty" value="1" class="lineItemQty"/>
      </c:if>
    </c:forEach>
    <div class='add'>
      <c:choose>
        <c:when test="${product.outOfStock}">
          <span class="outOfStock">Sold Out</span>

          <div align="center"><s:link beanclass="com.hk.web.action.core.user.NotifyMeAction"
                                      class="notifyMe button_orange"><b>Notify
            Me!!</b>
            <s:param name="productVariant" value="${product.productVariants[0]}"/> </s:link></div>
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
    <div class='floatfix'></div>
  </s:form>
  <script type="text/javascript">   
    validateCheckbox =1;
  </script>

  <shiro:hasPermission name="<%=PermissionConstants.MANAGE_IMAGE%>">
    <div>
      <s:link beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" event="uploadVariantImage"
              target="_blank"
              class="popup"> Upload
        <s:param name="productVariant" value="${product.productVariants[0]}"/>
      </s:link>
      &nbsp;|&nbsp;
      <s:link beanclass="com.hk.web.action.core.catalog.product.ProductVariantAction" event="renderManageImages"
              target="_blank" class="popup">Manage
        Images
        <s:param name="productVariant" value="${product.productVariants[0]}"/>
      </s:link>
    </div>
  </shiro:hasPermission>

</s:layout-definition>