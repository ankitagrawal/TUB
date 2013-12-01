<%@ page import="java.security.InvalidKeyException" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="javax.crypto.Mac" %>
<%@ page import="javax.crypto.spec.SecretKeySpec" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.FbConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.facebook.app.coupon.FanCouponAction" var="fanCouponBean"/>
<%--

To update app, following tasks need to be done :

A) Update creatives
   - there is one banner that we usually put in images/facebook/fancoupon/offers . Find this and replace this banner everywhere on this JSP
   - there is copy and a thumbnail image that is showed up on the popup window when user shares this offer. update copy and the thumbnail image. these details are in function publish()

B) Update campaignCode variable.
  - this is very important. coupons will be delivered from the campaign code entered here. Before pushing the app to prod, this campaignCode must be created using the fan coupon admin in Admin Home
      Creation of campaign code involves going to fan coupon admin and entering a campaign name, campaign code and coupons.
      one coupon should be entered in each line, these will be delivered to the user one by one.
      NOTE - You need to create coupons for applicable offer in HK App first. And these coupons are then used as FB fan coupons.

C) Update endOfOfferFanCouponDate to the date the campaign is ending on. ideally
    the app should not display after this date and the screen should simply say that no fan deals are
    live yet. I have not implemented the functionality but it is a simple if then. just enclose the whole layout inside
    an if statement and render app only if end date has not been exceeded. else show a message that offer has expired

--%>
<%
  String signed_request = request.getParameter(FbConstants.Param.signed_request);

  /* split the string into signature and payload */
  int idx = signed_request.indexOf(".");
  String rawpayload = signed_request.substring(idx + 1);
  String payload = new String(new Base64(true).decode(rawpayload));


  /* parse the JSON payload and do the signature check */
//	FacebookRequest fbRequest = new Gson().fromJson(payload, FacebookRequest.class);

  String campaignCode = "lwdi-launch";

  DateTime dateTimeFanCoupon = new DateTime();
  Date endOfOfferFanCouponDate = new Date(new DateTime(2012, 6, 30, 23, 59, 59, 59).getMillis());
%>
<s:layout-render name="/layouts/fbDefault510.jsp">

  <s:layout-component name="content">

    <script type="text/javascript">

      var payload = <%=payload%>;

      window.fbAsyncInit = function() {
        FB.init({
          appId      : '<%=FbConstants.appId%>', // App ID
          channelURL : '//www.healthkart.com/channel.html', // Channel File
          status     : true, // check login status
          cookie     : true, // enable cookies to allow the server to access the session
          oauth      : true, // enable OAuth 2.0
          xfbml      : true  // parse XFBML
        });

        // Additional initialization code here
        FB.getLoginStatus(function(response) {
          if (response.authResponse) {
            // logged in and connected user, someone you know
            initLoggedInUser(response.authResponse);
          } else {
            initNewUser();
          }
        });
      };

      function login() {
        FB.login(function(response) {
          if (response.authResponse) {
            initLoggedInUser(response.authResponse);
          } else {
            initNewUser();
          }
        }, {scope: 'email'});
      }

      function initNewUser() {
        if (payload.page.liked) {
          $('#fanContainer').hide();
          $('#loginButtonContainer').show();
        } else {
          $('#fanContainer').show();
          $('#loginButtonContainer').hide();
        }
      }

      var params = {};

      function initLoggedInUser(authResponse) {
        $('#fanContainer').hide();
        $('#loginButtonContainer').hide();
        $('#loggedInContainer').show();
        $('#preloader').show();

        // make ajax request to server to capture authResponse.userID, authResponse.accessToken and authResponse.email
        // response will contain coupon code and status details
        params.fbuid = authResponse.userID;
        params.accessToken = authResponse.accessToken;
        params.appId = "<%=FbConstants.appId%>";
        params.fbcouponCampaign = "<%=campaignCode%>";

        var couponClaimUrl = $('#couponClaimLink').attr('href') + '?' + $.param(params);
        $.getJSON(couponClaimUrl, function(res) {
          if (res.code == "<%=HealthkartResponse.STATUS_OK%>") {
            $('#couponCodeContainer').html(res.data.coupon);
            $('#preloader').hide();
            $('#offerContainer').show();
            $('#hiddenCode').show();
            $('#revealedCode').show();
          } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
            $('#errorCode').html('ERROR: ' + res.message);
            $('#preloader').hide();
            $('#hiddenCode').hide();
            $('#offerContainer').show();
          }
        });

        FB.api('/me', function(response) {
          $('#userNameSpan').html('Hi ' + response.name + '!');
        });
      }

      function publish() {
        FB.ui(
        {
          method: 'feed',
          display: 'page',
          name: 'Are you a diabetic? Then join India\'s largest community for diabetes management.',
          link: 'http://www.facebook.com/livingwithdiabetesinindia',
          picture: 'http://img.healthkart.com/images/facebook/fancoupon/offers/lwdi_logo_90x90.png',
          caption: 'Valid till <fmt:formatDate value="<%=endOfOfferFanCouponDate%>"/>',
          description: 'Join India\'s largest diabetes community and avail special prices on diabetic care products from top brands like AccuChek, Onetouch, Omron, Abbott, Dr. Morepen, Splenda and many more'
        },
        function(response) {
          if (response && response.post_id) {
            $('#hiddenCode').hide();
            $('#revealedCode').show();

            var couponShareLink = $('#couponShareLink').attr('href') + '&' + $.param(params);
            $.getJSON(couponShareLink, function(res) {});
          } else {
            // not published
          }
        });

      }

      $(document).ready(function() {
        $('.shareLink').click(function() {
          publish();
          return false;
        });
      });


    </script>

    <div style="display:none;">
      <s:link beanclass="com.hk.web.action.facebook.app.coupon.FanCouponClaimAction" id="couponClaimLink"></s:link>
      <s:link beanclass="com.hk.web.action.facebook.app.coupon.FanCouponClaimAction" event="share" id="couponShareLink"></s:link>
    </div>

    <%--
      This section is seen when the user is NOT a fan and has NOT added the app
      It prompts the user to become a fan first
    --%>
    <div class="grid_8" style="display:none;" id="fanContainer">
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/likeThisPageTop.png"/>

      <div style="position: absolute; width: 500px; height: 600px; opacity:0.7; filter:alpha(opacity=70); -moz-opacity: 0.7; z-index: 100; background-color: white;">

      </div>
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/fanCouponMasthead2.png"/>

      <h2>Current Deal :</h2>
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/offers/lwd_banner-2.jpg"/>

      <div style="text-align: center; padding: 10px;">
        <img src="<hk:vhostImage/>/images/facebook/fancoupon/joinNowButton.png"/>
      </div>
      <%--<img src="<hk:vhostImage/>/images/facebook/fancoupon/likeThisPageBottom.png"/>--%>
    </div>

    <%--
      This section is seen when the user is a fan and has NOT added the app
      It prompts the user to "get free coupon code", which opens a facebook login popup
    --%>
    <div class="grid_8" style="display:none;" id="loginButtonContainer">
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/fanCouponMasthead2.png"/>

      <h2>Current Deal :</h2>
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/offers/lwd_banner-2.jpg"/>

      <div style="text-align: center; padding: 10px; cursor: pointer;" onclick="login();">
        <img src="<hk:vhostImage/>/images/facebook/fancoupon/joinNowButton.png"/>
      </div>
    </div>

    <%--
      This section is seen when the user is a fan and has added the app
    --%>
    <div class="grid_8" style="display:none;" id="loggedInContainer">
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/fanCouponMastheadSmall2.png"/>

      <div id="userNameSpan" class="right"></div>
      <br/>
    </div>

    <div style="text-align: center; display: none;" id="preloader">
      <img src="<hk:vhostImage/>/images/ajax-loader.gif">
      Please wait ..
    </div>

    <%--
      This section shows the unique coupon code
      User is prompted to share the discount with friends to get the free coupon
    --%>
    <div class="grid_8" id="offerContainer" style="display: none;">
      <%-- if user has already shared.. he/she will straight away see the code --%>
      <div id="revealedCode" style="display:none; background-color: lightgoldenrodyellow; border: 2px dashed darkblue; height: 70px; padding: 10px; text-align: center;">
        Your unique coupon code is
        <div id="couponCodeContainer" style="font-size: 20px; font-weight: bold;">
        </div>
        <p style="font-size: 1.5em; margin: 0;">
          <a href="${pageContext.request.contextPath}/diabetes?utm_source=facebook&utm_medium=app&utm_campaign=2012-05-lwdi&affid=PrafulAkalPBU8J" target="_blank">Click to redeem coupon code &gt;&gt;</a>
        </p>
      </div>
      <%-- if user has NOT shared.. he/she will be prompted to share the discount with friends --%>
      <div id="hiddenCode" style="text-align: center;">
        <h3>Final step! <br/>Share this discount with your friends and help us spread awareness</h3><br/>
        <a class="shareLink" href="#"><img src="<hk:vhostImage/>/images/facebook/fancoupon/shareButtonLarge.png" height="40"/></a>
      </div>
      <div id="errorCode" style="background-color: yellow; padding: 10px; text-align: center; color: darkred; font-weight: bold; font-size: 1.1em;">
      </div>
      <p class="sml">
        offer valid till <fmt:formatDate value="<%=endOfOfferFanCouponDate%>"/> or till stocks last. <br/>
        discount applicable on already best prices from HealthKart.com :)<br/>
      </p>

      <h2>Current Deal:</h2>
      <img src="<hk:vhostImage/>/images/facebook/fancoupon/offers/lwd_banner-2.jpg"/>
    </div>

  </s:layout-component>
</s:layout-render>
