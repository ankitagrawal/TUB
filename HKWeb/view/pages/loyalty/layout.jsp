<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<html lang="en">
<head>
<%--	<% PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
	principal.ge%>--%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HealthKart | Loyalty Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="<hk:vhostJs/>/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="<hk:vhostJs/>/bootstrap/css/prettify.css" rel="stylesheet">

	<style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
		padding-right: 20px;
		padding-left: 20px;
      }
    </style>
	<%
		PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
		if(principal != null){
	        pageContext.setAttribute("userId", principal.getId());
		}
		else{
			pageContext.setAttribute("userId", null);
		}

		 String roles = RoleConstants.HK_USER + "," + RoleConstants.HK_UNVERIFIED;
	%>
  </head>


  <body>
	<script src="<hk:vhostJs/>/bootstrap/js/jquery.js"></script>
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
	<link href="<hk:vhostCss/>/css/style.css" rel="stylesheet" type="text/css" />
    <div class="container">
      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li><a href="<hk:vhostJs/>/loyaltypg">Home</a></li>
          <li><a href="http://www.healthkart.com">Visit Healthkart</a></li>
          <li><a href="<hk:vhostJs/>/core/loyaltypg/Cart.action">View Cart</a></li>	      
        </ul>
        <h4 class="muted"><img src="<hk:vhostJs/>/images/logo.png" alt="healthkart logo"> Loyalty Program</h4>
      </div>
	     <br>
	    <span style="float:left">
		    Total available loyalty points: ${hk:getLoyaltyKarmaPointsForUser(userId)}s
	    </span>
	    
	    <span class="pull-right">
	    Welcome,
	      <strong>
                  <shiro:hasAnyRoles name="<%=roles%>">
                    <shiro:principal property="firstName"/>
                  </shiro:hasAnyRoles>
                </strong>
		    &nbsp;&nbsp;&nbsp;
		</span>


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