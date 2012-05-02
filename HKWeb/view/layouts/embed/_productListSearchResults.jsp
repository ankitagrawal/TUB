<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    ProductDao productDao = (ProductDao)ServiceLocatorFactory.getService("ProductDao");
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product_productThumb = productDao.getProductById(product_productThumbId);
    pageContext.setAttribute("product", product_productThumb);
  %>

  <div class='product'>
    <h2>
      <s:link beanclass="com.hk.web.action.ProductAction" title="${product.name}" class="prod_top_link">
        <s:param name="productId" value="${product.id}"/>
        <s:param name="productSlug" value="${product.slug}"/>
        ${product.name}
        <c:choose>
          <c:when test="${hk:getCombo(product.id) != null}">
            <c:set var="combo" value="${hk:getCombo(product.id)}"/>
             <span class="special green">
          <fmt:formatNumber value="${combo.discountPercent*100}" maxFractionDigits="0"/>% off
      </span>
          </c:when>
          <c:otherwise>
            <c:if
                test="${product.minimumMRPProducVariant.markedPrice > hk:getApplicableOfferPrice(product.minimumMRPProducVariant)  + hk:getPostpaidAmount(product.minimumMRPProducVariant)}">
      <span class="special green">
          <fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}" maxFractionDigits="0"/>% off
      </span>
            </c:if>
          </c:otherwise>
        </c:choose>
      </s:link>
    </h2>
    <s:link class="img128" beanclass="com.hk.web.action.ProductAction" title="${product.name}">
      <s:param name="productId" value="${product.id}"/>
      <s:param name="productSlug" value="${product.slug}"/>
      <c:choose>
        <c:when test="${product.mainImageId != null}">
          <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>" alt="${product.name}" class='prod128'/>
        </c:when>
        <c:otherwise>
          <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg' alt="${product.name}" class='prod128'/>
        </c:otherwise>
      </c:choose>
      <c:choose>
        <c:when test="${hk:getCombo(product.id) != null}">
          <c:set var="combo" value="${hk:getCombo(product.id)}"/>
           <span class="special green">
        <fmt:formatNumber value="${combo.discountPercent*100}" maxFractionDigits="0"/>% off
    </span>
        </c:when>
        <c:otherwise>
          <c:if
              test="${product.minimumMRPProducVariant.markedPrice > hk:getApplicableOfferPrice(product.minimumMRPProducVariant)  + hk:getPostpaidAmount(product.minimumMRPProducVariant)}">
    <span class="special green">
        <fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}" maxFractionDigits="0"/>% off
    </span>
          </c:if>
        </c:otherwise>
      </c:choose>
    </s:link>
    <div class='prod_desc'>
      <c:choose>
        <c:when test="${hk:getCombo(product.id) != null}">
          <div class='prices'>
            <c:set var="combo" value="${hk:getCombo(product.id)}"/>
            <span class='cut'>
              <span class='num'>
                Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
              </span>
            </span>

          <span class='hk'>
            Our Price
              <span class='num'>
                Rs. <fmt:formatNumber
                  value="${combo.hkPrice}" maxFractionDigits="0"/>
              </span>
          </span>
          </div>
        </c:when>
        <c:otherwise>
          <div class='prices'>
        <span class='cut'>
          <span class='num'>
            Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
          </span>
        </span>
        <span class='hk'>
          Our Price
          <span class='num'>
            Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.hkPrice}" maxFractionDigits="0"/>
          </span>
        </span>
          </div>
        </c:otherwise>
      </c:choose>
        ${product.overview}
      <div class='more'>
        <s:link beanclass="com.hk.web.action.ProductAction" title="${product.name}">
          <s:param name="productId" value="${product.id}"/>
          <s:param name="productSlug" value="${product.slug}"/>
          read more and place order &rarr;
        </s:link>
      </div>
    </div>
    <div class="floatfix"></div>
  </div>

</s:layout-definition>