<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="mhc.service.dao.AffiliateDao" %>
<%@ page import="mhc.domain.Affiliate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <%
    ProductDao productDao = InjectorFactory.getInjector().getInstance(ProductDao.class);
    String productId = (String) pageContext.getAttribute("productId");
    Product product = productDao.find(productId);
    pageContext.setAttribute("product", product);

    AffiliateDao affiliateDao = InjectorFactory.getInjector().getInstance(AffiliateDao.class);
    Affiliate affiliate = null;
    Long affiliateId = (Long) pageContext.getAttribute("affiliateId");
    if (affiliateId != null) {
      affiliate = affiliateDao.find(affiliateId);
    }
    pageContext.setAttribute("affiliate", affiliate);
  %>
  <div class="jqmWindow" style="width:700px;" id="getBannerLinkWindow">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Customize Link</s:layout-component>
      <script type="text/javascript">
        //not working the intended way
        $(document).ready(function() {
          $('#productBannerTextArea').html($.trim($('.productBannerTextArea').html()));
        });
      </script>
      <s:layout-component name="content">
        <h4 class="strikeline">Product Name:- ${product.name}</h4>
        <s:form beanclass="com.hk.web.action.ProductAction">
          <s:errors/>
          <div class="round-cont" style="width:610px;margin-top: 20px;">
            <div class="hk_banner" style="margin-left:200px">
              <div>
                <a href="http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}"
                   title="${product.name}" target="_blank">
                  <c:choose>
                    <c:when test="${product.mainImageId != null}">
                      <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"/>
                    </c:when>
                    <c:otherwise>
                      <img class='prod320'
                           src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
                           alt="${product.slug}" title="${product.name}">
                    </c:otherwise>
                  </c:choose>
                </a>
              </div>
              <div>
                <a href="http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}"
                   title="${product.name}" target="_blank">${product.name}</a>
                <a href="http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}"
                   title="${product.name}" target="_blank">
                  <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
                    <div class='cut' style="font-size: 12px;">
                      <span class='num' style="font-size: 12px;">
                        Rs <fmt:formatNumber value="${product.productVariants[0].markedPrice}"
                                             maxFractionDigits="0"/>
                      </span>
                    </div>
                    <div class='hk' style="font-size: 12px;margin-top:0px">
                      <span class='num' style="font-size: 12px;;text-decoration:none">
                        Rs <fmt:formatNumber
                          value="${hk:getApplicableOfferPrice(product.productVariants[0]) + hk:getPostpaidAmount(product.productVariants[0])}"
                          maxFractionDigits="0"/>
                      </span>
                    </div>
                    <div class="special green" style="font-size: 12px;margin-top:0px">
                      you save
                                      <span style="font-weight: bold;;text-decoration:none"><fmt:formatNumber
                                          value="${product.productVariants[0].discountPercent*100}"
                                          maxFractionDigits="0"/>%</span>
                    </div>
                  </div>
                </a>
                <a href="http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}"
                   target='_blank' target="_blank"><img
                    src="${pageContext.request.contextPath}/images/icons/buy_button_1.png"
                    alt="Buy from HealthKart"/></a>
              </div>
            </div>

            <div class="label">
              <label>Copy the following code and paste it in your website</label>
              <a href="javascript:void(0);"
                 onclick="document.getElementById('productBannerTextArea').focus();document.getElementById('productBannerTextArea').select();">(Select
                                                                                                                                               This
                                                                                                                                               Code)</a>
              <textarea id="productBannerTextArea" readonly="readonly" class="productBannerTextArea"
                        name="productBannerTextArea" rows="3" cols="50"
                        style="margin-top:20px;height:80px;word-wrap:normal;white-space:pre-wrap;">
                <iframe
                    src="http://www.healthkart.com/product?productBanner=&product=${product.id}&affid=${affiliate.code}"
                    width="180" height="325" scrolling="no"></iframe>
              </textarea>
            </div>
          </div>
          <s:hidden name="product" value="${product.id}"/>
        </s:form>
      </s:layout-component>
    </s:layout-render>
  </div>
</s:layout-definition>