<%@ page import="com.hk.constants.FbConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.facebook.app.coupon.FanCouponClaimAction" var="couponClaimBean"/>

<s:layout-render name="/layouts/fbDefault.jsp">

  <s:layout-component name="htmlHead">

    <script type="text/javascript">
      FB.init({
        appId  : '<%=FbConstants.appId%>',
        status : false, // check login status
        cookie : true,  // enable cookies to allow the server to access the session
        xfbml  : true   // parse XFBML
      });

      function publish() {
        FB.ui({
          method: 'stream.publish',
          message: 'HealthKart is giving away a free photo personalized greeting card',
          attachment: {
            name: 'Claim your\'s now. Till stocks last.',
            caption: '',
            description: (
                'HealthKart\'s greeting cards are the perfect gift for this Valentine\'s day. 5"x7" in size, printed on premium textured paper, it has love written all over it :) (not literally ofcourse!)'
                ),
            href: 'http://www.facebook.com/healthkart?v=app_4949752878',
            media: [{
              type: "image",
              src: "http://simg.healthkart.com/common/images/prod_greeting_3_thumb.jpg",
              href: 'http://www.facebook.com/healthkart?v=app_4949752878'
            }]
          },
          user_message_prompt: 'Share this with your friends by clicking on "Publish" to reveal the free calendar coupon code'
        }, function(response) {
          if (response && response.post_id) {
            <%-- make ajax request to get the bonus coupon --%>
            <c:if test="${!couponClaimBean.fbcouponUserCampaign.shared}">
            $.getJSON($('#bonusLink').attr('href'), function(healthkartRes) {
              $('.bonusContainer').show();
              $('.shareContainer').hide();
            });
            </c:if>
          } else {
            // not published
          }
        });

      }

      $(document).ready(function() {
        $('.shareLink').click(function() {publish(); return false;});
      });

    </script>

  </s:layout-component>

  <s:layout-component name="header">
    <img src="${pageContext.request.contextPath}/common/images/fb/fbAppTitle.jpg"/>
  </s:layout-component>

  <s:layout-component name="content">
    <%--<h3><span style="color:#009900; background-color: #ccff99; padding-left: 5px; padding-right: 5px;">Offer 1)</span> <span style="color:gray;">Your Unique Coupon Code for </span>15% off on desk and wall calendars:</h3>--%>
    <%--<div style="background-color: #ffff99; padding: 10px; border: 1px dashed black;">--%>
      <%--<h2>${couponClaimBean.fbcouponUserCampaign.fbcouponCoupon.coupon}</h2>--%>
      <%--<h4>15% off on desk and wall calendars</h4>--%>
      <%--Valid till 31st January 2011. Applicable to only one user account on HealthKart.--%>
    <%--</div>--%>
    <%--<br/><br/>--%>

    <h3><span style="color:#009900; background-color: #ccff99; padding-left: 5px; padding-right: 5px;">Offer)</span> <span style="color:gray;">Your Unique Coupon Code for </span>1 free greeting card:</h3>
    <c:if test="${couponClaimBean.fbcouponUserCampaign.shared == false}">
      <h3>(Click on the share button to reveal this coupon code)</h3>
    </c:if>
    <div style="background-color: #ffff99; padding: 10px; border: 1px dashed black;">
      <c:choose>
        <c:when test="${couponClaimBean.fbcouponUserCampaign.shared}">
          <div>
          <h2>${couponClaimBean.fbcouponUserCampaign.fbcouponCoupon.bonusCoupon}</h2>
          <h4>1 free greeting card</h4>
          Valid till 14th Feb 2011. Applicable to only one user account on HealthKart.
          </div>
        </c:when>
        <c:otherwise>
          <div class="shareContainer">
            <a class="shareLink" href="#"><img src="${pageContext.request.contextPath}/common/images/fb/shareButtonLarge.png"/></a>.
          </div>
          <div style="display:none;" class="bonusContainer">
            <h2>${couponClaimBean.fbcouponUserCampaign.fbcouponCoupon.bonusCoupon}</h2>
            <h4>1 free greeting card</h4>
            Valid till 14th Feb 2011. Applicable to only one user account on HealthKart.
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <br/>
    <h4>Copy this coupon code and use it on your shopping cart to avail discount</h4>
    <br/>
    <h2>Important Note:</h2>
    <div style="font:normal 1em Courier, sans-serif;">
      - If you cannot see the popup after clicking on the share button, please try using a different browser or system. This is a problem with facebook and unfortunately we cannot fix it at our end.<br/>
      - The two offers cannot be combined, if you need to place orders for both poster calendars and desk calendars, please do so separately.
    </div>
    <br/>

    <div class="grid_8">
      <div class="grid_3 alpha">&nbsp;</div>
      <div class="grid_5 omega">
        go to landing page
      </div>
    </div>

    <div style="display:none;">
      <a target="_top"></a>
      <s:link id="bonusLink" beanclass="com.hk.web.action.facebook.app.coupon.FanCouponClaimBonusAction">
        <s:param name="fbcouponCampaign" value="${couponClaimBean.fbcouponCampaign}"/>
        <s:param name="fbcouponUser" value="${couponClaimBean.fbcouponUser}"/>
        Bonus
      </s:link>
    </div>


  </s:layout-component>

</s:layout-render>
