<%@ page import="java.security.InvalidKeyException" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="javax.crypto.Mac" %>
<%@ page import="javax.crypto.spec.SecretKeySpec" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="com.hk.constants.FbConstants" %>
<%@ page import="com.hk.util.FacebookRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%
	String signed_request = request.getParameter("signed_request");
//	System.out.println("signed_request: " + signed_request);
	/* split the string into signature and payload */
	int idx = signed_request.indexOf(".");
	byte[] sig = new Base64(true).decode(signed_request.substring(0,
			idx).getBytes());
	String rawpayload = signed_request.substring(idx + 1);
	String payload = new String(new Base64(true).decode(rawpayload));

//	System.out.println("payload = " + payload);

	/* parse the JSON payload and do the signature check */
	FacebookRequest fbRequest = new Gson().fromJson(payload, FacebookRequest.class);
	/* check if it is HMAC-SHA256 */
	if (!fbRequest.getAlgorithm().equals("HMAC-SHA256")) {
		/* note that this follows facebooks example, as published on 2010-07-21 (I wonder when this will break) */
		System.err.println("ERROR: algorithm not nmatched");
	}

	try {
		SecretKeySpec secretKeySpec = new SecretKeySpec(
				FbConstants.promoApiSecret.getBytes(),
				"HMACSHA256");
		Mac mac = Mac.getInstance("HMACSHA256");
		mac.init(secretKeySpec);
		byte[] mysig = mac.doFinal(rawpayload.getBytes());
		if (!Arrays.equals(mysig, sig)) {
			System.err.println("Non-matching signature for request");
		}
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		System.err.println("Unknown hash algorithm ");
	} catch (InvalidKeyException e) {
		e.printStackTrace();
		System.err.println("Wrong key for ");
	}
%>

<s:layout-render name="/layouts/fbDefault.jsp">
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      FB.init({
        appId  : '<%=FbConstants.promoAppId%>',
        status : false, // check login status
        cookie : true,  // enable cookies to allow the server to access the session
        xfbml  : true   // parse XFBML
      });

      function publish() {
        FB.ui({
          method: 'stream.publish',
          message: 'Mother\'s Day is here and HealthKart is giving 25% off on all type of mugs - classic coffee mugs, two tone mugs, magic mugs, animal handle mugs. Create one for your Mom now. I have taken my coupon from HealthKart, Get yours too.',
          attachment: {
            name: 'Claim your\'s now. Till stocks last.',
            caption: '',
            description: (
                'HealthKart offers discounts and freebies on various personalized products like mugs, calendars, photobooks, greeting cards, t-shirts etc. Share with your friends and let them know about the deals too.'
                ),
            href: 'http://www.facebook.com/healthkart?sk=app_205394542818322',
            media: [{
              type: "image",
              src: "http://simg.healthkart.com/common/images/fb/Favicon-25OffMugsMD.jpg",
              href: 'http://www.facebook.com/healthkart?sk=app_205394542818322'
            }]
          },
          user_message_prompt: 'Share this with your friends by clicking on "Publish" to reveal the fan deal coupon code'
        }, function(response) {
          if (response && response.post_id) {
            <%-- make ajax request to get the bonus coupon --%>
            $('.couponContainer').show();
            $('.shareContainer').hide();
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
  <s:layout-component name="heading"></s:layout-component>
  <s:layout-component name="content">
    <%
      if (fbRequest.getPage() != null) {
        if (fbRequest.getPage().isLiked()) {
    %>
    <div class="shareContainer">
      <a class="shareLink" href="#"><img src="${pageContext.request.contextPath}/images/fb/FanDeal-2.jpg"/></a>.
    </div>
    <div style="display:none;" class="couponContainer">
        <img src="${pageContext.request.contextPath}/common/images/fb/FanDeal-ITMD11.jpg"/>
        <a href="${pageContext.request.contextPath}/" target="_blank"><img src="${pageContext.request.contextPath}/common/images/fb/FanDeal-4.jpg"/></a>
    </div>
    <%
      }else{
    %>
    <img src="${pageContext.request.contextPath}/common/images/fb/FanDeal-0.jpg"/>
    <%
      }
      }
    %>
  </s:layout-component>
</s:layout-render>
