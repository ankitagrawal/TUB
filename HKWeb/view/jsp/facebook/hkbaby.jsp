<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.hk.util.FacebookRequest"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="javax.crypto.Mac"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.InvalidKeyException"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<html>
<head>
<title>HealthKart : HealtkKart Baby</title>
<jsp:include page="/jsp/includes.jsp"></jsp:include>
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
<style>
.gotoButton {
	position: absolute;
	top: 55px;
	left: 180px;
}
</style>
<script>
function getGrowthStatus(){
	var dob = document.getElementById("dob").value;
	var gender = document.getElementById("gender").value;
	var height = document.getElementById("height").value;
	var weight = document.getElementById("weight").value;
	var hc = document.getElementById("hc").value;
	if(dob == ''){
		alert("Mandatory fields are required to generate status.")
		return;		
	}else{
		xmlHttp=CreateXmlHttpObject();
		if (xmlHttp==null){
			alert ("Browser does not support HTTP Request")
			return
		} 
		var url="<%=request.getContextPath()%>/jsp/ajax/GetChildGrowthStatus.jsp"
		url=url+"?dob="+dob
		url=url+"&gender="+gender
		url=url+"&height="+height
		url=url+"&weight="+weight
		url=url+"&hc="+hc
		url=url+"&sid="+Math.random()
		xmlHttp.onreadystatechange = (function afterResponse(){
										if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){ 
											document.getElementById("growthStatus").innerHTML = xmlHttp.responseText; 
											document.getElementById("promo").style.display = "block";
										} })
		xmlHttp.open("GET",url,true)
		xmlHttp.send(null)
	}
}
</script>
</head>
<body id="">
<div id='step1-2' style="display: block">
<%
	if (fbRequest.getPage() != null) {
		if (fbRequest.getPage().isLiked()) {
%>
<div class="home-block">
<h1>Growth Tracker</h1>
<table class="sectionoutline">
	<tr>
		<td align="left" class="label" width="100px">DOB:<em>*</em><br>
		<i>(DD/MM/YYYY)</i></td>
		<td align="left"><input class="inputBox" size="10px"
			onchange="isValidDDMMYYYY(this);" id="dob" name="dob"></td>
	</tr>
	<tr>
		<td align="left" class="label">Gender:<em>*</em></td>
		<td align="left"><select name="gender" id="gender">
			<option value="M">Male</option>
			<option value="F">Female</option>
		</select></td>
	</tr>
	<tr>
		<td align="left" class="label">Height:</td>
		<td align="left"><input class="inputBox" size="1px"
			onchange="isDouble(this);" id="height" name="height"> (cms)</td>
	</tr>
	<tr>
		<td align="left" class="label">Weight:</td>
		<td align="left"><input class="inputBox" size="1px"
			onchange="isDouble(this);" id="weight" name="weight"> (Kgs)</td>
	</tr>
	<tr>
		<td align="left" class="label">Head Circum:</td>
		<td align="left"><input class="inputBox" size="1px"
			onchange="isDouble(this);" id="hc" name="hc"> (cms)</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>

		<td align="left"><input type="button" class="button orange small"
			value="Submit" onclick="getGrowthStatus();"></input></td>
	</tr>

	<tr>
		<td colspan="2" id="growthStatus" valign="top"
			style="text-align: justify"></td>
	</tr>
</table>
</div>
<br></br>
<div id="promo" style="display: none;">
<font style='font-size: 14px'>Receive 100 Rs cash discount @ <a class='link' target="_blank" href='${pageContext.request.contextPath}/baby/'>www.healthkart.com/baby</a> on your first purchase.</font>
<br>
<font class="heading">Your promo code is </font> <font style='font-size: 20px'>HK100CD</font>
<div>
<%
	} else {
%> <img width="510px"
	src="<%=request.getContextPath() %>/images/facebook/HealthKartBaby-PreLike.png" />
<%
	}
	}
%>
</div>
</body>
</html>
