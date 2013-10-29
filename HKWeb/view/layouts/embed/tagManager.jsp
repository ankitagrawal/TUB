<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.hk.constants.marketing.TagConstants" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.web.action.core.cart.CartAction" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.hk.domain.order.CartLineItem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="searchString" value="'" />
<c:set var="replaceString" value="\\'" />
<s:layout-definition>
  <%
    PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    if (principal != null) {
      pageContext.setAttribute(TagConstants.TagVars.USER_HASH, principal.getUserHash());
      pageContext.setAttribute(TagConstants.TagVars.USER_GENDER, principal.getGender());
      pageContext.setAttribute(TagConstants.TagVars.ORDER_COUNT, principal.getOrderCount());
    } else {
      pageContext.setAttribute(TagConstants.TagVars.USER_HASH, "guest");
      pageContext.setAttribute(TagConstants.TagVars.USER_GENDER, "n.a.");
      pageContext.setAttribute(TagConstants.TagVars.ORDER_COUNT, "0");
    }
    String projectEnvTagManager = (String) ServiceLocatorFactory.getProperty(Keys.Env.projectEnv);
    CartAction cartAction = (CartAction) pageContext.getAttribute("cartAction");
    // for autocomplete
    pageContext.setAttribute("cartAction", cartAction);
  %>
  <script type="text/javascript">
    dataLayer = [{
      '<%=TagConstants.TagVars.USER_HASH%>': '<%=pageContext.getAttribute(TagConstants.TagVars.USER_HASH)%>',
      '<%=TagConstants.TagVars.USER_GENDER%>': '<%=pageContext.getAttribute(TagConstants.TagVars.USER_GENDER)%>',
      '<%=TagConstants.TagVars.ORDER_COUNT%>': '<%=pageContext.getAttribute(TagConstants.TagVars.ORDER_COUNT)%>',
      'pageType': '${pageType}',
      'primaryCategory':'${fn:replace(primaryCategory, searchString, replaceString)}',
      'secondaryCategory':'${fn:replace(secondaryCategory, searchString, replaceString)}',
      'tertiaryCategory':'${fn:replace(tertiaryCategory, searchString, replaceString)}',
      'brand':'${fn:replace(brand, searchString, replaceString)}',
      'variantId':'${variantId}',
      'productId':'${productId}',
      'variantName':'${fn:replace(variantName, searchString, replaceString)}',
      'oldVariantId':'${oldVariantId}',
      'primaryMenu':'${fn:replace(primaryMenu, searchString, replaceString)}',
      'secondaryMenu':'${fn:replace(secondaryMenu, searchString, replaceString)}',
      'tertiaryMenu':'${fn:replace(tertiaryMenu, searchString, replaceString)}',
      'navKey':'${navKey}',
      'oos' : '${oos}',
      'env' : '<%=projectEnvTagManager%>',
      'signup' : '${param["signup"]}',
      'login' : '${param["login"]}',
      'errorCode' : '${errorCode}'
    }];
  </script>
  <%
    Boolean newSession = (Boolean) session.getAttribute(TagConstants.Session.newSession);
    //out.write("new session = "+newSession);
    if (newSession == null) {
      newSession = false;
			session.setAttribute(TagConstants.Session.newSession, newSession);
      // this is a new session!
        String originalUrlHeader = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (originalUrlHeader == null) {
          originalUrlHeader = request.getRequestURI();
        }
        pageContext.setAttribute("originalUrlHeader", originalUrlHeader);
        // the landing page URL gives us info on the context of this page
        // or we can also use the variables passed in
  %>
  <script type="text/javascript">
    dataLayer.push({'newSession' : 'true'});
    dataLayer.push({
      '<%=TagConstants.AnalyticsConstants.lpBrand%>' : '${fn:replace(brand, searchString, replaceString)}',
      '<%=TagConstants.AnalyticsConstants.lpPrimCat%>' : '${fn:replace(primaryCategory, searchString, replaceString)}',
      '<%=TagConstants.AnalyticsConstants.lpPrimMenu%>' : '${fn:replace(primaryMenu, searchString, replaceString)}'
    });
  </script>
  <%
    }
    if (cartAction != null) {
      List<String> productIdList = new ArrayList<String>();
      List<String> variantIdList = new ArrayList<String>();
      for (CartLineItem cartLineItem : cartAction.getPricingDto().getProductLineItems()) {
        productIdList.add("'"+cartLineItem.getProductVariant().getId()+"'");
        variantIdList.add("'"+cartLineItem.getProductVariant().getProduct().getId()+"'");
      }
      String cartProductIds = StringUtils.join(productIdList.iterator(), ",");
      String cartVariantIds = StringUtils.join(variantIdList.iterator(), ",");
  %>
  <script type="text/javascript">
    dataLayer.push({
      'cartTotal' : '${cartAction.pricingDto.grandTotalPayable}',
      'cartItemCount' : '${cartAction.itemsInCart}',
      'cartShippingTotal' : '${cartAction.pricingDto.shippingTotal}',
      'cartProductIds' : [<%=cartProductIds%>],
      'cartVariantIds' : [<%=cartVariantIds%>],
      'cartDiscountTotal' : '${cartAction.pricingDto.totalDiscount}'
    });
  </script>
  <%
    }
  %>
  <!-- Google Tag Manager -->
  <noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-3PX8"
  height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
  <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
  new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
  j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
  '//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
  })(window,document,'script','dataLayer','GTM-3PX8');</script>
  <!-- End Google Tag Manager -->
</s:layout-definition>