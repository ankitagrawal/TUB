<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.Keys"%>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode"%>
<%@ page import="com.hk.constants.core.RoleConstants"%>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants"%>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%
    Double cashBackPercentage = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.cashBackPercentage));
    Long defaultGateway = Long.parseLong((String)ServiceLocatorFactory.getProperty(Keys.Env.defaultGateway));
    
    boolean isSecure = pageContext.getRequest().isSecure();
    pageContext.setAttribute("isSecure", isSecure);
%>
<c:set var="paymentModeId_DefaultGateway" value="<%=defaultGateway%>"/>
<c:set var="cashBackPercentage" value="<%=cashBackPercentage%>"/>
<c:set var="codPaymentModeId" value="<%=EnumPaymentMode.COD.getId()%>"/>

<s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentSuccessAction" var="actionBean"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment Successful">

<%--<s:layout-component name="htmlHead">
  <script type="text/javascript">
    $(document).ready(function() {
      $('#publishOnFBWindow').jqm({trigger: '#publishOnFBLink', ajax: '@href'});
    <c:if test="${!hk:alreadyPublishedDeal(actionBean.payment.order) && hk:getTopDealVariant(actionBean.payment.order) != null && actionBean.payment.amount >= 500}">
      $('#publishOnFBLink').click();   // For amount greater than 500 only.
    </c:if>
    });
  </script>
</s:layout-component>
<s:layout-component name="modal">
  <div class="jqmWindow" id="publishOnFBWindow" style="display: none; width:auto;padding:10px;">
    <s:link beanclass="com.hk.web.action.core.user.PublishOnFBAction" id="publishOnFBLink" style="visibility:hidden;">
      <s:param name="order" value="${actionBean.payment.order.id}"/>
      Publish on facebook
    </s:link>
  </div>
</s:layout-component>--%>

<%--<s:layout-component name="menu"> </s:layout-component>--%>
<s:layout-component name="heading">
    <div style="margin-top: 50px;">
        <h1 class="green" style="font-size: 1.2em;">
            Payment Successful
        </h1>
    </div>
</s:layout-component>

<s:layout-component name="left_col">


<c:if test="${actionBean.payment != null}">
  <%
    if (AnalyticsConstants.analytics) {
  %>

  <script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '<%=AnalyticsConstants.gaCode%>']);
  _gaq.push(['_trackPageview']);

  _gaq.push(['_addTrans',
    "${actionBean.payment.gatewayOrderId}", <%-- order ID - required --%>
    "HealthKart.com", <%-- affiliation or store name --%>
    "${hk:decimal2(actionBean.pricingDto.grandTotal)}", <%-- total - required --%>
    "0.0", <%-- tax --%>
    "${hk:decimal2(actionBean.pricingDto.shippingSubTotal - actionBean.pricingDto.shippingDiscount)}", <%-- shipping --%>
    "${hk:convertToLettersNumbersUnderscore(actionBean.pricingDto.city)}", <%-- city--%>
    "${hk:convertToLettersNumbersUnderscore(actionBean.pricingDto.state)}", <%-- state or province --%>
    "India"                                                                                             <%-- country--%>
  ]);

  <%--Item data--%>
  <c:forEach items="${actionBean.pricingDto.aggregateProductLineItems}" var="productLineItem">
  _gaq.push(['_addItem',
    "${actionBean.payment.gatewayOrderId}", <%-- order ID - required --%>
    "${productLineItem.productVariant.id}", <%-- SKU/code --%>
    "${productLineItem.productVariant.product.name}", <%-- product name --%>
    "<c:forEach items="${productLineItem.productVariant.product.categories}" var="category" varStatus="optionCtr">${category.name}${!optionCtr.last?',':''}</c:forEach>", <%-- category or variation --%>
    "${hk:decimal2(productLineItem.hkPrice)}", <%-- unit price - required --%>
    "${productLineItem.qty}"                   <%-- quantity - required --%>
  ]);
  </c:forEach>

  <%--COD--%>
  <c:if test="${actionBean.pricingDto.codSubTotal > 0}">
  _gaq.push(['_addItem',
    "${actionBean.payment.gatewayOrderId}", <%-- order ID - required --%>
    "COD", <%-- SKU/code --%>
    "COD", <%-- product name --%>
    "", <%-- category or variation --%>
    "${actionBean.pricingDto.codSubTotal - actionBean.pricingDto.codDiscount}", <%-- unit price - required --%>
    "1"                                                                         <%-- quantity - required --%>
  ]);
  </c:if>

  _gaq.push(['_trackTrans']); //submits transaction to the Analytics servers

  //track order count
  _gaq.push(['_setCustomVar',
    <%=AnalyticsConstants.CustomVarSlot.orderCount.getSlot()%>,                   // This custom var is set to slot #5.  order_count.
    "<%=AnalyticsConstants.CustomVarSlot.orderCount.getName()%>",     // The name acts as a kind of category for the user activity.  Required parameter.
    "${fn:length(actionBean.order.user.orders)}",               // This value of the custom variable.  Required parameter.
    <%=AnalyticsConstants.CustomVarSlot.orderCount.getScope().getLevel()%>                    // Sets the scope to session-level. Optional parameter.
  ]);

  <c:if test="${fn:length(actionBean.order.user.orders) eq 1}">
  _gaq.push(['_setCustomVar',
    <%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getSlot()%>,                   // This custom var is set to slot #2.  first_order_date
    "<%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getName()%>",     // The name acts as a kind of category for the user activity.  Required parameter.
    "${actionBean.purchaseDate}",               // This value of the custom variable.  Required parameter.
    <%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getScope().getLevel()%>                    // Sets the scope to visitor-level. Optional parameter.
  ]);
  </c:if>
  <c:if test="${actionBean.couponCode !=null}">
     //track couponcode
   var couponAmount=${actionBean.couponAmount};
   couponAmount=Math.round(couponAmount);    //event value needs to be an integer
   _gaq.push(['_trackEvent','purchase','coupon','${actionBean.couponCode}',couponAmount]);
  </c:if>


   //track purchase date
  _gaq.push(['_trackEvent','purchase','purchaseDate','${actionBean.purchaseDate}']);
   //payment mode tracking
   var amount=${actionBean.payment.amount};
   amount=Math.round(amount);            //event value takes only integer input in ga
  _gaq.push(['_trackEvent','purchase','paymentType','${actionBean.paymentMode.name}',amount]);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>

  <%
    }
  %>
  <!-- Google Code for Payment Success Conversion Page -->
  <s:layout-render name="/layouts/embed/_adwordsConversionCode.jsp" conversion_value="${hk:decimal2(actionBean.pricingDto.grandTotal)}" order_id="${actionBean.payment.gatewayOrderId}"/>

</c:if>


    <c:choose>
        <c:when test="${actionBean.payment != null}">
            <%--<c:if test="${actionBean.payment.paymentMode.id == codPaymentModeId && actionBean.payment.amount < 1500}">
                <div>
                    <s:link beanclass="com.hk.web.action.core.payment.RegisterOnlinePaymentAction">
                        <s:param name="order" value="${actionBean.order}"/>
                        <img src="${pageContext.request.contextPath}/images/banners/pay_online_banner5.jpg">
                    </s:link>
                </div>
            </c:if>--%>
            <%--<div class="right" style="float: right;">
                <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction">
                    <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg">
                </s:link>
            </div>--%>
            <h2 style="font-size: 1em; padding-left: 15px;margin-top: 20px;">
                Your order ID is <strong>${actionBean.payment.order.gatewayOrderId}</strong>.</h2>
            <br/>
            <shiro:hasRole name="<%=RoleConstants.HK_UNVERIFIED%>">
                <div class='promos'>
                    <div class='prom yellow help' style="width: 95%; padding:5px;">
                        <p class="lrg"><strong>You have not activated your HealthKart account.</strong><br/>
                            To activate your account, please click on the activation link sent in your email. By activating your
                            account,
                            we get to know that you have a valid email id and we can send special offers on your email.</p>

                        <p><strong>If you haven't received the mail,
                            <s:link beanclass="com.hk.web.action.core.user.ResendAccountActivationLinkAction" event="pre" class="resendActivationEmailLink">click here to resend it.</s:link>
                        </strong>
                            <br/><br/>
                            <span class="emailSendMessage alert" style="display: none; font-weight:bold;"></span>
                        </p>

                        <p style="display:none;" class="emailNotReceived">
                            If you do not receive this email, please check your spam/bulk folder. Write to us at info@healthkart.com if
                            you face problems.
                        </p>
                    </div>
                </div>
                <script type="text/javascript">

                    <%-- Re-Send Activation Link --%>
                    $('.resendActivationEmailLink').click(function() {

                        var clickedLink = $(this);
                        var clickedP = clickedLink.parents('p');
                        clickedP.find('.emailSendMessage').html($('#ajaxLoader').html()).show();
                        $.getJSON(clickedLink.attr('href'), function(res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                clickedP.find('.emailSendMessage').html(res.data.message).show();
                                $('.emailNotReceived').show();
                            }
                        });
                        return false;
                    });

                </script>
            </shiro:hasRole>


            <%--<c:if test="${actionBean.payment.paymentMode.id == paymentModeId_DefaultGateway}">
              <p> With this order you have earned reward points worth
                <span class="orange">
                   <strong>
                     <fmt:formatNumber value="${actionBean.payment.amount * cashBackPercentage}" type="currency" currencySymbol="Rs. "/>
                   </strong>
                  </span>
              </p>
            </c:if>--%>
            <br/>

            <h2 class="paymentH2">Shipping & Delivery</h2>

            <p>Your order will be dispatched within ${hk:getDispatchDaysForOrder(actionBean.payment.order)}. Additional time will be taken by the courier company.</p>

            <h2 class="paymentH2">Customer Support</h2>

            <p><s:link beanclass="com.hk.web.action.pages.ContactAction">Write to us</s:link> with your Order ID if you have any questions or call us on 0124-4551616</p>

            <c:if test="${actionBean.payment.order.offerInstance != null && actionBean.payment.order.offerInstance.coupon != null && hk:isNotBlank(actionBean.payment.order.offerInstance.coupon.complimentaryCoupon)}">
                <div style="background-color: lightgoldenrodyellow;">
                    <h2>You have won a Complementary Coupon!</h2>
                    <p>
                            ${actionBean.payment.order.offerInstance.offer.complimentaryCouponDescription}<br/>
                        Your Complementary Coupon Code : <strong>${actionBean.payment.order.offerInstance.coupon.complimentaryCoupon}</strong>
                    </p>
                    <p class="gry sml">
                        You can also find this coupon code in your invoice for later use.
                    </p>
                </div>
            </c:if>

            <c:if test="${actionBean.pricingDto.totalCashback > 0.0}">
                <div style="padding: 10px; border: 1px solid gray; background-color: lightgoldenrodyellow;">
                    <h2>Cashback Pending <strong>(Rs. <fmt:formatNumber pattern="<%=FormatUtils.currencyFormatPattern%>" value="${actionBean.pricingDto.totalCashback}"/>)</strong></h2>
                    <p>
                        Your cashback will be automatically credited into your HealthKart account depending on the payment mode :<br/>
                        - in case of online payment through credit card, debit card or internet banking, the cashback is credited to your account already.<br/>
                        - in case of cash on delivery (COD) payment mode, the cashback is credited upon delivery of the order.<br/>
                    </p>
                </div>
            </c:if>

            <div class="step2 success_order_summary" style="padding: 5px; float: left; margin-right: 5px;">
                <h2 class="paymentH2">Order Summary</h2>

                <s:layout-render name="/layouts/embed/orderSummaryTableDetailed.jsp" pricingDto="${actionBean.pricingDto}"
                                 orderDate="${actionBean.payment.paymentDate}"/>
                <div class="floatfix"></div>

            </div>
          
          <div style="clear:both;"></div>
             <div style="margin-top: 10px; float: left; margin-right: 5px;">
                <h2 class="paymentH2">Shipping address${actionBean.pricingDto.shippingLineCount > 1 ? 'es' : ''}</h2>

                <p>
                    <c:set var="address" value="${actionBean.payment.order.address}"/>
                    <strong>${address.name}</strong> <br/>
                        ${address.line1},
                    <c:if test="${not empty address.line2}">
                        ${address.line2},
                    </c:if>
                        ${address.city} - ${address.pin}<br/>
                        ${address.state}, <span class="upc">INDIA</span><br/>
                    <span class="sml lgry upc">Phone </span> ${address.phone}<br/>
                </p>
            </div>
              <div class="floatfix"></div>



        </c:when>
        <c:otherwise>
            Invalid request!
        </c:otherwise>
    </c:choose>

</s:layout-component>

<s:layout-component name="analytics">
    <iframe src="" id="vizuryTargeting" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
    <script type="text/javascript">
        var vizuryLink = "https://ssl.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e500&orderid=${actionBean.gatewayOrderId}&orderprice=${actionBean.payment.amount}";
        <c:forEach items="${actionBean.payment.order.cartLineItems}" var="lineItem" varStatus="liCtr">
        vizuryLink += "&pid${liCtr.count}=${lineItem.productVariant.product.id}&catid${liCtr.count}=${lineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${lineItem.qty}";
        </c:forEach>
        vizuryLink += "&currency=INR&section=1&level=1";
        document.getElementById("vizuryTargeting").src = vizuryLink;
    </script>
</s:layout-component>

</s:layout-render>