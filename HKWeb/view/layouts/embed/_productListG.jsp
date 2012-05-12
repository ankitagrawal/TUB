<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>


  <%
    ProductDao productDao = ServiceLocatorFactory.getService(ProductDao.class);
    String product_productThumbId = (String) pageContext.getAttribute("productId");
    Product product_productThumb = productDao.getProductById(product_productThumbId);
    pageContext.setAttribute("product", product_productThumb);

    ComboDao comboDao = ServiceLocatorFactory.getService(ComboDao.class);
    Combo combo = comboDao.getComboById(product_productThumbId);
    pageContext.setAttribute("combo", combo);
  %>

  <div>
    <c:choose>
      <c:when test="${product.googleAdDisallowed}">
      </c:when>
      <c:otherwise>
        <div class="grid_4">
          <s:link class="img128" beanclass="com.hk.web.action.core.catalog.product.ProductAction" title="${product.name}">
            <s:param name="productId" value="${product.id}"/>
            <s:param name="productSlug" value="${product.slug}"/>
            <c:choose>
              <c:when test="${product.mainImageId != null}">
                <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"
                                 alt="${product.name}" class='prod128'/>
              </c:when>
              <c:otherwise>
                <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg'
                     alt="${product.name}" class='prod128'/>
              </c:otherwise>
            </c:choose>
          </s:link>
        </div>
        <div class="grid_13">
          <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" title="${product.name}" class="prod_top_link">
            <s:param name="productId" value="${product.id}"/>
            <s:param name="productSlug" value="${product.slug}"/>
            <h3>
                ${product.name}
            </h3>
          </s:link>

             <div>
              <c:choose>
                <c:when test="${combo != null}">
                  <span class='cut'>
          <span class='num'>
            Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
          </span>
        </span>
        <span class='hk'>
          Our Price
          <span class='num'>
            Rs. <fmt:formatNumber
              value="${combo.hkPrice}"
              maxFractionDigits="0"/>
          </span>
        </span>
                </c:when>
                <c:otherwise>
                  <div class='prices'>
                              <c:if test="${product.minimumMRPProducVariant.discountPercent > 0}">
                                <div class='cut'>
                                    <span class='num'>
                                      Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>
                                    </span>
                                </div>
                                <div class='hk'>
                                  Our Price
                                    <span class='num'>
                                      Rs. <fmt:formatNumber
                                        value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                                        maxFractionDigits="0"/>
                                    </span>
                                </div>
                              </c:if>
                              <c:if test="${product.minimumMRPProducVariant.discountPercent == 0}">
                                <div class='cut' style="min-width: 1px; height: 12px;">
                                </div>
                                <div class="hk">
                                  Our Price

                                    <span class='num'>
                                      Rs. <fmt:formatNumber
                                        value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"
                                        maxFractionDigits="0"/>
                                    </span>
                                </div>
                              </c:if>
                            </div>
          
                  <%--<span class='cut'>--%>
                  <%--<span class='num'>--%>
                  <%--Rs. <fmt:formatNumber value="${product.minimumMRPProducVariant.markedPrice}" maxFractionDigits="0"/>--%>
                  <%--</span>--%>
                  <%--</span>--%>
                  <%--<span class='hk'>--%>
                  <%--Our Price--%>
                  <%--<span class='num'>--%>
                  <%--Rs. <fmt:formatNumber--%>
                  <%--value="${hk:getApplicableOfferPrice(product.minimumMRPProducVariant) + hk:getPostpaidAmount(product.minimumMRPProducVariant)}"--%>
                  <%--maxFractionDigits="0"/>--%>
                  <%--</span>--%>
                  <%--</span>--%>
                </c:otherwise>
              </c:choose>
            </div>



          <c:choose>
            <c:when test="${combo != null}">
                 <span class="special green">
              <fmt:formatNumber value="${combo.discountPercent*100}" maxFractionDigits="0"/>% off
          </span>
            </c:when>
            <c:otherwise>
              <c:if
                  test="${product.minimumMRPProducVariant.markedPrice > hk:getApplicableOfferPrice(product.minimumMRPProducVariant)  + hk:getPostpaidAmount(product.minimumMRPProducVariant)}">
                <%--<span class="special green">--%>
                <%--<fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}" maxFractionDigits="0"/>% off--%>
                <%--</span>--%>
                <c:choose>
                  <c:when
                      test="${product.maximumDiscountProducVariant.discountPercent > product.minimumMRPProducVariant.discountPercent}">
                    <div class="special green"
                         style="${product.maximumDiscountProducVariant.discountPercent >= .33 ? 'font-style:bold;' : ''}">
                      <c:choose>
                        <c:when test="${product.minimumMRPProducVariant.discountPercent > 0}">
                          <fmt:formatNumber value="${product.minimumMRPProducVariant.discountPercent*100}"
                                            maxFractionDigits="0"/>% to
                        </c:when>
                        <c:otherwise>
                          upto
                        </c:otherwise>
                      </c:choose>
                      <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                        maxFractionDigits="0"/>%
                      off
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="special green">
                      <c:if test="${product.maximumDiscountProducVariant.discountPercent >= .33}">
                        <strong>
                          <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                            maxFractionDigits="0"/>%
                          off
                        </strong>
                      </c:if>
                      <c:if test="${product.maximumDiscountProducVariant.discountPercent < .33}">
                        <fmt:formatNumber value="${product.maximumDiscountProducVariant.discountPercent*100}"
                                          maxFractionDigits="0"/>% off
                      </c:if>
                    </div>
                  </c:otherwise>
                </c:choose>
              </c:if>
            </c:otherwise>
          </c:choose>

          <div class='prod_desc'>
              ${product.overview}
            <div class='more'>
              <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" title="${product.name}">
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
      </c:otherwise>
    </c:choose>

  </div>

</s:layout-definition>