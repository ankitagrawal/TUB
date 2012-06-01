<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    ProductDao productDao = (ProductDao)ServiceLocatorFactory.getService(ProductDao.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product_productThumb = productDao.getProductById(product_productThumbId);
    pageContext.setAttribute("product", product_productThumb);
  %>

  <div>
    <div class="grid_4">
      <s:link class="img128" beanclass="com.hk.web.action.core.catalog.product.ProductAction" title="${product.name}">
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
      </s:link>
    </div>
    <div class="grid_13">
      <s:link href="${product.productURL}" title="${product.name}" class="prod_top_link">
        <s:param name="productId" value="${product.id}"/>
        <s:param name="productSlug" value="${product.slug}"/>
        <h3>
            ${product.name}
        </h3>
        <c:if test="${product.minimumMRPProducVariant.markedPrice > hk:getApplicableOfferPrice(product.minimumMRPProducVariant)  + hk:getPostpaidAmount(product.minimumMRPProducVariant)}">
          <span class="special green">
              <fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}" maxFractionDigits="0"/>% off
          </span>
        </c:if>
      </s:link>
      <div class='prod_desc'>
        <div>
        <span class='cut'>
          <span class='num'>
            Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
          </span>
        </span>
        <span class='hk'>
          Our Price
          <span class='num'>
            Rs. <fmt:formatNumber value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}" maxFractionDigits="0"/>
          </span>
        </span>
        </div>
          ${product.overview}
        <div class='more'>
          <s:link href="${product.productURL}" title="${product.name}">
            <s:param name="productId" value="${product.id}"/>
            <s:param name="productSlug" value="${product.slug}"/>
            read more and place order &rarr;
          </s:link>
        </div>
        <div class="floatfix"></div>

      </div>
      <div class="floatfix"></div>
    </div>

    <div class="floatfix"></div>

  </div>

</s:layout-definition>