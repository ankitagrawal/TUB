<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.hk.util.FacebookRequest"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="javax.crypto.Mac"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.InvalidKeyException"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<html>
<head>
<title>HealthKart : FB Fan Page</title>
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
	FacebookRequest fbRequest = new Gson().fromJson(payload,
			FacebookRequest.class);
	/* check if it is HMAC-SHA256 */
	if (!fbRequest.getAlgorithm().equals("HMAC-SHA256")) {
		/* note that this follows facebooks example, as published on 2010-07-21 (I wonder when this will break) */
		System.err.println("ERROR: algorithm not nmatched");
	}

	try {
		SecretKeySpec secretKeySpec = new SecretKeySpec(
				"d3d3821640274636d8350555d1c80cb7".getBytes(),
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
<script type="text/javascript"
	src="http://connect.facebook.net/en_US/all.js"></script>
<style type="text/css">
.gotoButton {
	position: absolute;
	top: 55px;
	left: 180px;
}
</style>
<script type="text/javascript">
function showPormotionCode(){ 
	document.getElementById('step1-2').style.display = "none";
	document.getElementById('step3').style.display = "block";
}
</script>
</head>

<body id="">
<div id="fb-root"></div>
<div id='step1-2' style="display: block">
<%
	if (fbRequest.getPage() != null) {
		if (fbRequest.getPage().isLiked()) {
%>
  <a href='http://www.healthkart.com/' target="_blank"><img
	style="cursor: pointer;" width="510px"
	title="Click to go to HealthKart.com"
	src="<%=request.getContextPath() %>/images/facebook/hKart_fanpage02.jpg" /></a>
<%
	} else {
%>
  <img width="510px"
	src="<%=request.getContextPath() %>/images/facebook/hKart_fanpage01.jpg" />
<%
	}
	}
%>
</div>

</body>
</html>
