<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductVariantDao" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.FbConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="imageSize_thumb" value="<%=EnumImageSize.TinySize%>"/>
<s:layout-definition>
  <%
    Long orderId = (Long) pageContext.getAttribute("orderId");
//    System.out.println("orderId: " + orderId);
    pageContext.setAttribute("orderId", orderId);
    ProductVariantDao variantDao = (ProductVariantDao)ServiceLocatorFactory.getService(ProductVariantDao.class);
    String variantId = (String) pageContext.getAttribute("variantId");
    ProductVariant variant = variantDao.getVariantById(variantId);
    pageContext.setAttribute("topOrderedVariant", variant);
    DecimalFormat decimalFormat = new DecimalFormat("#");
    pageContext.setAttribute("discountPercentage", decimalFormat.format((variant.getMarkedPrice() - variant.getHkPrice(null)) / variant.getMarkedPrice() * 100));

    String name = "Bought " + variant.getProduct().getName() + " on HealthKart.com - Checkout for other exciting offers and deals";
    if (variant.getDiscountPercent() > 0.0) {
      name = "Saved Rs. " + decimalFormat.format(variant.getMarkedPrice() - variant.getHkPrice(null))
          + " (" + decimalFormat.format((variant.getMarkedPrice() - variant.getHkPrice(null)) / variant.getMarkedPrice() * 100) + "%)"
          + " on " + variant.getProduct().getName() + " at HealthKart.com";
    }
    pageContext.setAttribute("name", name);

  %>

  <div id="fb-root"></div>
  <script type="text/javascript">
    window.fbAsyncInit = function() {
      FB.init({
        appId: '<%=FbConstants.fbFanAppId%>',
        //148326041894539 - HK Prod    //230394047022049 - HK Local
        status: true,
        cookie: true,
        xfbml: true});
    };
    (function() {
      var e = document.createElement('script');
      e.async = true;
      e.src = document.location.protocol +
              '//connect.facebook.net/en_US/all.js';
      document.getElementById('fb-root').appendChild(e);
    }());

    function publish() {
      FB.ui(
      {
        method: 'feed',
        name: '${name}',
        link: 'www.healthkart.com/product/${topOrderedVariant.product.slug}/${topOrderedVariant.product.id}?fbshare=1',
        picture: '${topOrderedVariant.product.mainImageId == null ? "http://www.healthkart.com/images/logo.png": hk:getS3ImageUrl(imageSize_thumb, topOrderedVariant.product.mainImageId)}',
        description: 'One-stop shop for authentic Nutrition, Beauty, Medical Devices and Baby Care Products'
      },
          function(response) {
            if (response && response.post_id) {
              var orderShareLink = $('#orderShareLink').attr('href');
              $.getJSON(orderShareLink, {order: ${orderId}}, function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  //alert("Success");
                }
              });
            } else {
              // not published
            }
          });
    }

    $(document).ready(function() {
      $('#share_button').click(function() {
        $('#publishOnFBWindow').jqmHide();
        publish();
        return false;
      });
    });


  </script>

  <div style="display:none;">
    <s:link beanclass="com.hk.web.action.core.user.PublishOnFBAction" event="share" id="orderShareLink"></s:link>
  </div>

</s:layout-definition>