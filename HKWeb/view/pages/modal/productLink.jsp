<%@ page import="com.hk.domain.affiliate.Affiliate" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.affiliate.AffiliateDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
      Product product = (Product) pageContext.getAttribute("product");
      pageContext.setAttribute("product", product);

      AffiliateDao affiliateDao = (AffiliateDao) ServiceLocatorFactory.getService(AffiliateDao.class);
      Affiliate affiliate = null;
      Long affiliateId = (Long) pageContext.getAttribute("affiliateId");
      if (affiliateId != null) {
          affiliate = affiliateDao.getAffiliateById(affiliateId);
      }
      pageContext.setAttribute("affiliate", affiliate);
	  boolean isSecure = WebContext.isSecure();
      pageContext.setAttribute("isSecure", isSecure);
  %>

  <div class="jqmWindow" style="width:700px" id="getProductLinkWindow">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Customize Link</s:layout-component>
      <s:layout-component name="content">
        <script type="text/javascript">
          $(document).ready(function() {
            $('.buttonType')[0].checked = true;
            $('#productLinkTextArea').html($.trim($('.buy_button_0').html()));
            $('#productLinkLabel').html($.trim($('.buy_button_0').html()));

            $('.buttonType').change(function() {
              var embedTagClass = $('.buttonType:checked').val();
              $('#productLinkTextArea').html($.trim($('.' + embedTagClass).html()));
              $('#productLinkLabel').html($.trim($('.' + embedTagClass).html()));
            });
            $('#anchorTextBox').change(copyAnchorText);
            $('#anchorTextBox').keyup(copyAnchorText);
          });
          function copyAnchorText() {
            $('.buy_button_0_a').html($('#anchorTextBox').val());
            var embedTagClass = $('.buttonType:checked').val();
            $('#productLinkTextArea').html($.trim($('.' + embedTagClass).html()));
          }
        </script>
        <h4 class="strikeline">Product Name: ${product.name}</h4>
        <s:form beanclass="com.hk.web.action.core.catalog.product.ProductAction">
          <s:errors/>
          <div class="round-cont" style="width:650px;margin-top: 20px;">

            <div class="label">
              Choose one of the Text/Button Links to get the html code
            </div>

              <c:if test="${not isSecure }">
                  <div class="label">
                      <input type="radio" value="buy_button_0" name="anchor" class="buttonType"/>
                      <label>Enter Anchor Tag</label>
                      <input type="text" value="Buy ${product.name} from HealthKart.com" id="anchorTextBox"/>
				<span style="display: none;" class="buy_button_0"> <a
                        class="buy_button_0_a"
                        href='http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}'>Buy
                        ${product.name} from HealthKart.com</a> </span>
                  </div>

                  <div class="label">
                      Button Link
                      <input type="radio" value="buy_button_1" name="anchor" class="buttonType"/>
                      <img src='http://www.healthkart.com/images/icons/buy_button_1.png'/>
            <span style="display:none;" class="buy_button_1">
            <a href='http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}'><img
                    src='http://www.healthkart.com/images/icons/buy_button_1.png'/></a>
            </span>
                      <input type="radio" value="buy_button_2" name="anchor" class="buttonType"/>
                      <img src='http://www.healthkart.com/images/icons/buy_button_2.png'/>
            <span style="display:none;" class="buy_button_2">
              <a href='http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}'><img
                      src='http://www.healthkart.com/images/icons/buy_button_2.png'/></a>
            </span>
                      <input type="radio" value="buy_button_3" name="anchor" class="buttonType"/>
                      <img src='http://www.healthkart.com/images/icons/buy_button_3.png'/>
            <span style="display:none;" class="buy_button_3">
            <a href='http://www.healthkart.com/product/${product.slug}/${product.id}?affid=${affiliate.code}'><img
                    src='http://www.healthkart.com/images/icons/buy_button_3.png'/></a>
            </span>
                  </div>
              </c:if>
              <div class="label">
              <label>Copy the following code and paste it in your website</label>
              <a href="javascript:void(0);"
                 onclick="document.getElementById('productLinkTextArea').focus();document.getElementById('productLinkTextArea').select();">(Select
                                                                                                                                           This
                                                                                                                                           Code)</a>
              <textarea id="productLinkTextArea" name="productLinkTextArea" readonly="readonly" rows="3" cols="50"
                        style="margin-top:20px;height:80px"></textarea>
            </div>
            <div class="label">
              Sneak View
            </div>
            <div class="label">
              <label id="productLinkLabel"></label>
            </div>
            <div class="label">
            </div>
          </div>
          <s:hidden name="product" value="${product.id}"/>
        </s:form>
      </s:layout-component>
    </s:layout-render>
  </div>
</s:layout-definition>