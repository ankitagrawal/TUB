<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HealthKart | Loyalty Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="<hk:vhostJs/>/bootstrap/css/bootstrap.css" rel="stylesheet">

	<style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
		padding-right: 20px;
		padding-left: 20px;
      }
    </style>
  </head>

  <body>
	<script src="<hk:vhostJs/>/bootstrap/js/jquery.js"></script>
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
    <div class="container">
      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li><a href="">Home</a></li>
          <li><a href="">Visit Healthkart</a></li>
          <li><a href="core/loyaltypg/Cart.action">View Cart</a></li>
          <li><a href="">Sign In</a></li>
        </ul>
        <h3 class="muted">HealthKart</h3>
      </div>

      <hr>
			<stripes:layout-component name="contents"/>
	  <hr>
      <div class="footer">
        <p>© Footer goes here!!</p>
      </div>
    </div>
</body>
</html>
</stripes:layout-definition>