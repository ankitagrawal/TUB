<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HealthKart | Stellar Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/grid.css" rel="stylesheet">
    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/style.css" rel="stylesheet">
    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet"> 
    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/jquery-ui-1.9.2.custom.min.css" rel="stylesheet">
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-ui-1.9.2.custom.min.js"></script>
 </head>


  <body>

    <div class="embedMargin" id="header">
        <div class="container_16 clearfix">
        
		<div id="logo" class="grid_6">
            <div class="logo-block">
              <p class="hkpolicy"><span> <s:link href="/" title='go to healthkart home'>healthKart</s:link></span>
			  <span class="embedMarginleft"><s:link href="http://www.healthkartplus.com" title='go to healthkartplus'>healthkartplus</s:link></span>
			  </p>
              <div class="logoSubblock">
			  <a title="go to stellar home" href="<hk:vhostJs/>/loyaltypg">
                <img width="283" height="83" alt="healthkart logo" src="<hk:vhostJs/>/pages/loyalty/resources/images/hkStellar.png">
              </a>
              </div>
  
            </div>
        </div>

	

      </div>
    </div>
	    

      <div class="mainContainer ">
      <div class="container_16 clearfix">
      <s:layout-component name="lhsContent"/>      
			<stripes:layout-component name="contents"/>
        <s:layout-component name="rhsContent"/>
</div></div>
    <div id="footer">
        <div class="container_16 clearfix embedMarginTop100">
          <div class="grid_12">
            <div class="footerRight" style="width: 672px;">
            <div class="footerLeft" >
              info@healthkartplus.com | Established in India | Terms and Conditions | Connect with Us:
            </div> &nbsp;
             	<a rel="nofollow" href="http://www.facebook.com/healthkart"
						target="_blank" class="connectLink"> <img
						src="<hk:vhostImage/>/images/banners/home/facebook.png"></a> <a
						rel="nofollow" href="http://www.twitter.com/healthkart"
						target="_blank" class="connectLink"> <img
						src="<hk:vhostImage/>/images/banners/home/twitter.png"
						alt="HealthKart Twitter">
					</a> <a
						href="https://plus.google.com/116027214366934328880?prsrc=3"
						rel="publisher" target="_blank" class="connectLink"> <img
						src="<hk:vhostImage/>/images/banners/home/googleplus.png"
						alt="HealthKart Google Plus">
					</a> <a href="${pageContext.request.contextPath}/blog"
						target="_blank" class="connectLink"> <img
						src="<hk:vhostImage/>/images/banners/home/Blog.png"
						alt="HealthKart Blog"></a>

				</div>
              
            </div>
          </div>
        </div>
     
	
    
</body>
</html>
</stripes:layout-definition>