<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<%
  Double cashBackPercentage = Double.parseDouble((String) ServiceLocatorFactory.getProperty(Keys.Env.cashBackPercentage));
  Long defaultGateway = Long.parseLong((String) ServiceLocatorFactory.getProperty(Keys.Env.defaultGateway));
  String orderConfirmRoute = (String) ServiceLocatorFactory.getProperty(Keys.Env.codRoute);
%>
<c:set var="paymentStatusPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
<c:set var="paymentModeCOD" value="<%=EnumPaymentMode.COD.getId()%>"/>
<c:set var="paymentModeOnline" value="<%=EnumPaymentMode.ONLINE_PAYMENT.getId()%>"/>
<c:set var="paymentModeId_DefaultGateway" value="<%=defaultGateway%>"/>
<c:set var="cashBackPercentage" value="<%=cashBackPercentage%>"/>
<c:set var="codPaymentModeId" value="<%=EnumPaymentMode.COD.getId()%>"/>
<c:set var="neftMode" value="<%=EnumPaymentMode.NEFT.getId()%>"/>
<c:set var="orderConfirmRoute" value="<%=orderConfirmRoute%>"/>


<s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentSuccessAction" var="actionBean"/>


<s:layout-render name="/layouts/paymentSuccessBeta.jsp"
                 pageTitle="Payment Successful"
                 paymentSuccessBean="${actionBean}"
    >
<s:layout-component name="htmlHead">
  <script type="text/javascript">
    $(document).ready(function () {

      $("#dispatchDateQuesMark").click(function () {
        $("#popUpDDate").toggle();
      });

      $("#crossNew").click(function () {
        $("#popUpDDate").hide();
      });
      /* $(".learnMore").click(function(){
       $('html, body').animate({scrollTop: $(".products_container").offset().top}, 1000);
       });*/
    });
  </script>
</s:layout-component>

<%--<s:layout-component name="menu"> </s:layout-component>--%>
<s:layout-component name="steps">
  <s:layout-render name="/layouts/embed/_checkoutStripBeta.jsp" index="5"/>
</s:layout-component>


<c:if test="${actionBean.payment.paymentMode.id != paymentModeCOD}">
  <s:layout-component name="heading">
    <c:set var="city" value="${actionBean.order.address.pincode.city.name}"/>
    <div>
        <c:set var="eventStart" value="12"/>
        <c:set var="eventEnd" value="22"/>
        <c:set var="eventDate" value="20130512"/>
        <c:set var="hr">
            <fmt:formatDate value="<%=new Date()%>" pattern="HH"/>
        </c:set>
        <c:set var="dt">
            <fmt:formatDate value="<%=new Date()%>" pattern="yyyyddMM"/>
        </c:set>

        <c:choose>
            <c:when test="${dt == eventDate && hr >= eventStart && hr <eventEnd}">
                <a href="http://www.healthkartplus.com?src=hk" target="_blank" style="text-decoration:none;">
                    <img src="${pageContext.request.contextPath}/images/banners/killbill.png"
                         alt="Get your shopping cart for free"/>
                </a>

            </c:when>
            <c:otherwise>
              <a href="http://www.healthkartplus.com?src=hk" target="_blank" style="text-decoration:none;">
                <c:choose>
                  <c:when test="${city == 'DELHI' || city == 'NEW DELHI'  || city == 'GURGAON' || city == 'NOIDA'}">
                    <img src="${pageContext.request.contextPath}/images/banners/banner01A.png"
                         alt="HealthKartPlus 15% Off"/>
                  </c:when>
                  <c:when test="${city == 'BANGALORE' || city == 'BENGALURU'  || city == 'BANGALURU'}">
                    <img src="${pageContext.request.contextPath}/images/banners/banner02B.png"
                         alt="HealthKartPlus 25% Off"/>
                  </c:when>
                  <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/banners/hkplus-app2.jpg" alt="HealthKartPlus App"/>
                  </c:otherwise>
                </c:choose>
              </a>
            </c:otherwise>
        </c:choose>
    </div>
  </s:layout-component>

</c:if>


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

    <c:if test="${fn:length(actionBean.order.user.orders) eq 1}">
    _gaq.push(['_setCustomVar',
      <%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getSlot()%>,
      "<%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getName()%>",
      "${actionBean.purchaseDate}",
      <%=AnalyticsConstants.CustomVarSlot.firstPurchaseDate.getScope().getLevel()%>
    ]);
    </c:if>
    <c:if test="${actionBean.couponCode !=null}">
    //track couponcode
    var couponAmount =${actionBean.couponAmount};
    couponAmount = Math.round(couponAmount);    //event value needs to be an integer
    _gaq.push(['_trackEvent', 'purchase', 'coupon', '${actionBean.couponCode}', couponAmount]);
    </c:if>


    //track purchase date
    _gaq.push(['_trackEvent', 'purchase', 'purchaseDate', '${actionBean.purchaseDate}']);
    //payment mode tracking
    var amount =${actionBean.payment.amount};
    amount = Math.round(amount);            //event value takes only integer input in ga
    _gaq.push(['_trackEvent', 'purchase', 'paymentType', '${actionBean.paymentMode.name}', amount]);

    (function () {
      var ga = document.createElement('script');
      ga.type = 'text/javascript';
      ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
      var s = document.getElementsByTagName('script')[0];
      s.parentNode.insertBefore(ga, s);
    })();

  </script>

  <!-- Start AdRoll (FB Retargetting Conversion Tracking Code -->
  <script type="text/javascript">
    adroll_segments = "conversion"
  </script>
  <script type="text/javascript">
    adroll_adv_id = "SKDGP6YYENHVJCJDIKHUF7";
    adroll_pix_id = "JLZMDLGRYBFDFHEIKFE456";
    (function () {
      var oldonload = window.onload;
      window.onload = function () {
        __adroll_loaded = true;
        var scr = document.createElement("script");
        var host = (("https:" == document.location.protocol) ? "https://s.adroll.com" : "http://a.adroll.com");
        scr.setAttribute('async', 'true');
        scr.type = "text/javascript";
        scr.src = host + "/j/roundtrip.js";
        ((document.getElementsByTagName('head') || [null])[0] ||
            document.getElementsByTagName('script')[0].parentNode).appendChild(scr);
        if (oldonload) {
          oldonload()
        }
      };
    }());
  </script>

  <%
    }
  %>
  <!-- Google Code for Payment Success Conversion Page -->
  <s:layout-render
      name="/layouts/embed/paymentSuccessConversionTags.jsp"
      conversion_value="${hk:decimal2(actionBean.pricingDto.grandTotal)}"
      order_id="${actionBean.payment.gatewayOrderId}"
      order="${actionBean.order}"
      pageType="<%=HealthkartConstants.Remarketing.PageType.paymentSuccess%>"
      />
</c:if>

<c:choose>
<c:when test="${actionBean.payment != null}">

<style>


  .hdr-qstn {
    background: #f0f0f0;
    color: #1b75bb;
    padding: 5px 5px;
    font-size: 0.9em;
    margin-top: 10px;
    cursor: pointer;
  }

  .cntnt-answer {
    font-size: 0.9em;
    padding: 5px 4px;
    border-left: 1px solid #ccc;
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
  }

  .span5 {
    width: 280px;
    float: left;
    min-height: 1px;
    margin-left: 20px;
  }

  .span16 {
    width: 950px;
    float: left;
    min-height: 1px;
  }

  .prc-ofr {
    color: #090;
    font-size: 1.2em;
  }

    /** order success page css begins   **/
  .shipping-add-cntnr {
    min-height: 160px;
  }

  .loyalty-cntnr {
    width: 520px;
    margin: 0 auto;
  }

  .loyalty-cntnr .loyalty-text {
    border-bottom: 1px solid #ccc;
    padding-bottom: 20px;
    margin-bottom: 20px;

  }

    /** order success page css ends   **/
</style>
<div class="mrgn-t-35" style="width: 940px;float: left;">

  <div class="span5" style="margin-left:0;">
    <p class="mrgn-b-10 fnt-caps" style="font-weight: 700;">order details:</p>

    <p class="mrgn-b-10"><span class="fnt-caps" style="font-size: 0.9em;">status:</span>
      <c:choose>
      <c:when test="${actionBean.payment.paymentStatus.id == paymentStatusPending}">
      <c:choose>
      <c:when test="${actionBean.payment.paymentMode.id == paymentModeCOD}">
      <span class="prc-ofr" style="color: #ffb000;"> Verification Pending</span></p>

    <p class="mrgn-b-10"><span class="fnt-caps">order id:</span> ${actionBean.payment.order.gatewayOrderId} </p>
    </c:when>
    <c:otherwise>
      <span class="prc-ofr" style="color:#ffb000 ;">Authorization Pending</span></p>
      <p class="mrgn-b-10"><span class="fnt-caps">order id:</span> ${actionBean.payment.order.gatewayOrderId}</p>
    </c:otherwise>
    </c:choose>
    </c:when>
      <%--your order confirm ka message--%>
    <c:otherwise>
      <span class="prc-ofr">Order Confirmed</span></p>
      <p class="mrgn-b-10"><span class="fnt-caps"
                                 style="font-size: 0.9em;">order id:</span> ${actionBean.payment.order.gatewayOrderId}
      </p>
    </c:otherwise>
    </c:choose>


    <p class="mrgn-b-10"><span class="fnt-caps"
                               style="font-size: 0.9em;">total items:</span> ${fn:length(actionBean.pricingDto.productLineItems)}
    </p>
    <c:if test="${actionBean.payment.paymentMode.id == paymentModeCOD}">
      <p class="mrgn-b-10"><span class="fnt-caps"
                                 style="font-size: 0.9em;">payment mode:</span> Cash On Delivery
      </p>
    </c:if>
    <c:if test="${actionBean.payment.paymentMode.id == paymentModeOnline}">
      <p class="mrgn-b-10"><span class="fnt-caps"
                                 style="font-size: 0.9em;">payment mode:</span> Online Payment
      </p>
    </c:if>

    <p class="mrgn-b-10"><span class="fnt-caps"
                               style="font-size: 0.9em;">you ${actionBean.payment.paymentMode.id eq paymentModeCOD or actionBean.payment.paymentMode.id eq neftMode ? 'pay': 'paid'}:</span>
        <span class="youPayValue" style="color: #090;float: none;">
          <fmt:formatNumber value="${actionBean.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/>
        </span>
    </p>
  </div>


  <div
      style="border-left: 1px solid #ccc; width:310px;margin-left:0;border-right: 1px solid #ccc;padding-left: 15px;padding-right: 15px;min-height: 160px;"
      class="span5">

    <c:choose>
      <c:when test="${actionBean.payment.paymentStatus.id == paymentStatusPending}">
        <c:choose>
          <c:when test="${actionBean.payment.paymentMode.id == paymentModeCOD}">
            <p class="fnt-caps mrgn-b-10" style="font-weight: 700;">Action Required:</p>

            <%--message for cod ver Pending--%>
            <p>
              Please verify your order by giving us a missed call on <span style="color: #0091d7;">0124-4616414 </span>
              from ${actionBean.payment.contactNumber} which you have given as Cash On Delivery number
            </p>

          </c:when>
          <c:when test="${actionBean.payment.paymentMode.id == neftMode}">
            <p class="fnt-caps mrgn-b-10" style="font-weight: 700;">important information:</p>
            <%--message for NEFT pending --%>
            <p>
              Your order will be processed after we receive your NEFT transfer confirmation.
            </p>
          </c:when>
          <c:otherwise>
            <p class="fnt-caps mrgn-b-10" style="font-weight: 700;">important information:</p>
            <%--message for online auth pending --%>
            <p>
              We would update you with the status of your payment within 48hours. Once authorized, your order will go
              into processing.
            </p>
          </c:otherwise>
        </c:choose>
      </c:when>
      <%--your order confirm ka message--%>
      <c:otherwise>
        <p class="fnt-caps mrgn-b-10" style="font-weight: 700;">important information:</p>

        <p>
          Your order is confirmed. We will soon send you a confirmation mail. The estimated dispatch days for each
          product are mentioned below. </p>
      </c:otherwise>
    </c:choose>
      <%--
      <p>
        The estimated dispatch time for each
        product is mentioned below. The
        delivery time would be extra and will
        vary according to your location.
      </p>

      <p><span class="icn icn-sqre-blue"></span>
        <span id="learnMore" class="learnMore pad5" style="margin: 0px;color:#0091d7; float: none;" >learn more</span>
      </p>
      --%>

  </div>

  <div class="span5 shipping-add-cntnr alpha" style="width: 295px;">
    <p class="fnt-caps mrgn-b-10" style="font-weight: 700;">order shipped to:</p>
    <c:set var="address" value="${actionBean.payment.order.address}"/>
    <p><strong>${address.name}</strong></p>

    <p>
        ${address.line1},
      <c:if test="${not empty address.line2}">
        ${address.line2},
      </c:if>
    </p>

    <p>  ${address.city} - ${address.pincode.pincode}</p>

    <p>${address.state}, <span class="upc">INDIA</span></p>

    <p><span class="sml lgry upc">Phone </span> ${address.phone}</p>

  </div>


</div>

<shiro:lacksRole name="<%=RoleConstants.HK_LOYALTY_USER%>">
  <div class='brdr mrgn-bt-20 pad10 cont-lft'>
    <div class="fnt-bold" style="padding-left: 5px">Did you know about our Loyalty Program yet?</div>
    <p class="pad5">It is an easy way to earn points and redeem goodies. To begin with, let us tempt you by passing on
      <strong>15 bonus</strong> loyalty points on joining now!
      <a href="${pageContext.request.contextPath}/core/loyaltypg/LoyaltyIntroduction.action" class="txt-blue"
         target="_blank">Click here</a>, to know more.
    </p>
  </div>
</shiro:lacksRole>

<shiro:hasRole name="<%=RoleConstants.HK_LOYALTY_USER%>">
  <div class='brdr mrgn-t-20 pad10 cont-lft'>
    <p class="pad5">
      <c:if test="${actionBean.loyaltyPointsEarned > 0}">
        You have earned
        <strong>${hk:roundNumberForDisplay(actionBean.loyaltyPointsEarned)}</strong> loyalty points. These loyalty points will be transferred to your loyalty account once your order has been delivered.
      </c:if>

      <c:if test="${actionBean.loyaltyPointsEarned <= 0}">
        Oops! You didn't earn any loyalty points on this order. Upgrade your status by shopping more and start earning loyalty points.
      </c:if>
        <%--<a href="${pageContext.request.contextPath}/core/loyaltypg/LoyaltyIntroduction.action" target="_blank">Click here</a>, to know more.--%>
    </p>

    <p class="pad5">
      <a href="${pageContext.request.contextPath}/core/loyaltypg/LoyaltyIntroduction.action" target="_blank"
         style="text-decoration: underline;cursor: pointer;margin: 0px;color: #0091d7;float: none;">Click here</a>, to
      know more.
    </p>
  </div>
</shiro:hasRole>

<shiro:hasRole name="<%=RoleConstants.HK_UNVERIFIED%>">
  <div class="cont-lft">
    <div class="span16 row hdr-qstn show-answer">
      <span class=" mrgn-lr-5">You have not activated your HealthKart account</span>
      <span class="icn icn-expand2 mrgn-bt-5 mrgn-lr-5 show-answer-icn"></span>
      <span class="icn icn-collapse2 mrgn-bt-5 mrgn-lr-5 hide-answer-icn hide"></span>
    </div>
    <div class="span16  cntnt-answer hide">
      <p class="lrg pad5">
        To activate your account, please click on the activation link sent in your email. By activating your
        account,we get to know that you have a valid email id and we can send special offers on your email.
      </p>

      <p class="pad5"><strong>If you haven't received the mail,
        <s:link beanclass="com.hk.web.action.core.user.ResendAccountActivationLinkAction" event="pre"
                class="resendActivationEmailLink txt-blue">Click here to resend it.</s:link>
      </strong>
        <span class="emailSendMessage alert" style="display: none; font-weight:600;"></span>
      </p>

      <p class="pad5" style="display:none;" class="emailNotReceived">
        If you do not receive this email, please check your spam/bulk folder. Write to us at info@healthkart.com if
        you face problems.
      </p>
    </div>
  </div>


  <script type="text/javascript">

    <%-- Re-Send Activation Link --%>
    $('.resendActivationEmailLink').click(function () {

      var clickedLink = $(this);
      var clickedP = clickedLink.parents('p');
      clickedP.find('.emailSendMessage').html($('#ajaxLoader').html()).show();
      $.getJSON(clickedLink.attr('href'), function (res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          clickedP.find('.emailSendMessage').html(res.data.message).show();
          $('.emailNotReceived').show();
        }
      });
      return false;
    });


    $('.hdr-qstn').click(function () {
      if ($(this).hasClass('show-answer')) {
        $(this).removeClass('show-answer').addClass('hide-answer');
        $(this).children('.show-answer-icn').hide();
        $(this).children('.hide-answer-icn').show();
        $(this).parent().children('.cntnt-answer').slideDown('slow');
      }
      else {
        $(this).addClass('show-answer').removeClass('hide-answer');
        $(this).children('.show-answer-icn').show();
        $(this).children('.hide-answer-icn').hide();
        $(this).parent().children('.cntnt-answer').slideUp('slow');
      }
    });


  </script>
</shiro:hasRole>


<div class="leftPS mrgn-t-30">

  <jsp:include page="/includes/checkoutNotice.jsp"/>
  <c:if
      test="${actionBean.payment.order.offerInstance != null && actionBean.payment.order.offerInstance.coupon != null && hk:isNotBlank(actionBean.payment.order.offerInstance.coupon.complimentaryCoupon)}">
    <div style="padding: 10px; border: 1px solid #dddddd;">
      <h2 style="color: #0091d7; font-weight:600;">You have won a Complementary Coupon!</h2>

      <p>
          ${actionBean.payment.order.offerInstance.offer.complimentaryCouponDescription}<br/>
        Your Complementary Coupon Code :
        <strong>${actionBean.payment.order.offerInstance.coupon.complimentaryCoupon}</strong>
      </p>

      <p class="gry sml">
        You can also find this coupon code in your invoice for later use.
      </p>
    </div>
  </c:if>

  <div class="step2 success_order_summary"
       style="padding: 5px 5px 5px 0; float: left; margin-right: 5px;margin-bottom: 20px;">
    <div class="itemSummaryNew">
      <s:layout-render name="/layouts/embed/itemSummaryTableBeta.jsp" pricingDto="${actionBean.pricingDto}"
                       youBoughtText="You Bought"
                       orderDate="${actionBean.payment.paymentDate}"/>
    </div>

  </div>

  <c:if test="${actionBean.pricingDto.totalCashback > 0.0}">
    <div style="padding: 10px; position:relative;float: left;width: 610px;border: 1px solid #ddd;">
      Cashback Pending <strong>(Rs. <fmt:formatNumber pattern="<%=FormatUtils.currencyFormatPattern%>"
                                                      value="${actionBean.pricingDto.totalCashback}"/>)</strong>

      <p>
        Your cashback will be automatically credited into your HealthKart account depending on the payment mode :<br/>
        - in case of online payment through credit card, debit card or internet banking, the cashback is credited to
        your account already.<br/>
        - in case of cash on delivery (COD) payment mode, the cashback is credited upon delivery of the order.<br/>
      </p>
    </div>
  </c:if>


  <div style="clear:both;"></div>

  <div class='brdr' style="margin-bottom: 50px;margin-top: 20px;padding: 10px;">
    <div class="deliveryDetails" style="padding-left: 7px;font-weight: 600"> DELIVERY DETAILS</div>
    <ol style="padding-left:25px;font-size: 12px;">
      <li>
        The time taken for delivery after dispatch from our warehouse varies with location.
      </li>
      <li>
        For Metros: 1-3 business days
      </li>
      <li>
        For Major Cities: 2-4 business days
      </li>
      <li>
        For Other Town/Cities: 3-6 business days
      </li>
      <li>
        For Rest of India Non Serviceable through Couriers: 7-15 business days (Delivery done by Indian Post)
      </li>
      <li>For any query, please call us: 0124-4616444 or you can drop us an email at <span class="txt-blue">info@healthkart.com</span>
        with your Order ID.
      </li>
    </ol>
  </div>
</div>

<div class="rightPS mrgn-t-30" style="top:20px;">
  <h2 style="font-size: 1.2em;font-weight: 600;border-bottom: 1px solid #ddd;padding-bottom: 10px;" class="paymentH2 ">
    Payment Details</h2>

  <div class="orderSummaryNew" style="width: 100%;left:0;margin-bottom: 20px;">
    <s:layout-render name="/layouts/embed/orderSummaryTableBeta.jsp" pricingDto="${actionBean.pricingDto}"
                     orderDate="${actionBean.payment.paymentDate}"/>
  </div>

    <%--your cod ka message--%>
  <h1 class="youPaid" style="border-bottom: 1px solid #ddd;width: 100%;">
    <c:choose>
      <c:when test="${actionBean.payment.paymentMode.id == paymentModeCOD}">
        <span class="youPay fnt-light fnt-bold">
        Pay on delivery:
        </span>
      </c:when>
      <c:when test="${actionBean.payment.paymentMode.id == neftMode}">
        <span class="youPay fnt-light fnt-bold">
        YOU PAY:
        </span>
      </c:when>
      <%--your non cod ka message--%>
      <c:otherwise>
        <span class="youPay fnt-light fnt-bold">
        YOU PAID:
        </span>
      </c:otherwise>
    </c:choose>
    <strong>
      <span id="summaryGrandTotalPayable" class="youPayValue" style="color:#090;">
                              <fmt:formatNumber value="${actionBean.pricingDto.grandTotalPayable}" type="currency"
                                                currencySymbol="Rs. "/>
                            </span>
    </strong>
    <div class='newShippingHandling'>
      (inclusive of discounts, shipping, handling and taxes.)
    </div>
  </h1>
  <s:link class="btn btn-blue" href="/" title='go to healthkart home'>
    GO BACK TO HOME Page
  </s:link>
</div>

<div class="floatfix" style="margin-bottom: 40px;"></div>
</c:when>
<c:otherwise>
  Invalid request!
</c:otherwise>
</c:choose>

</s:layout-component>

<s:layout-component name="analytics">
  <iframe src="" id="vizuryTargeting" scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
          frameborder="0"></iframe>
  <script type="text/javascript">
    var vizuryLink = "https://ssl.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e500&orderid=${actionBean.gatewayOrderId}&orderprice=${actionBean.payment.amount}&uid=${user_hash}";
    <c:forEach items="${actionBean.payment.order.cartLineItems}" var="lineItem" varStatus="liCtr">
    vizuryLink += "&pid${liCtr.count}=${lineItem.productVariant.product.id}&catid${liCtr.count}=${lineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${lineItem.qty}";
    </c:forEach>
    vizuryLink += "&currency=INR&section=1&level=1";
    document.getElementById("vizuryTargeting").src = vizuryLink;
  </script>
</s:layout-component>

</s:layout-render>