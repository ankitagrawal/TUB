<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>HealthKart | Stellar Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="${pageContext.request.contextPath}/pages/loyalty/resources/css/grid.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/pages/loyalty/resources/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/pages/loyalty/resources/css/jquery-ui-1.9.2.custom.min.css" rel="stylesheet">
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-ui-1.9.2.custom.min.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js" > </script>
 </head>


  <body>

    <div class="embedMargin" id="header">
        <div class="container_16 clearfix">
        
		<div id="logo" class="grid_6">
            <div class="logo-block">
              <p class="hkpolicy"><span> <s:link href="/" title='go to healthkart home' target="_blank">healthKart</s:link></span>
			 </p>
              <div class="logoSubblock">
			  <a title="go to stellar home" href="${pageContext.request.contextPath}/loyaltypg">
                <img width="283" height="83" alt="healthkart logo" src="${pageContext.request.contextPath}/pages/loyalty/resources/images/hkStellar.png">
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
            <div class="footerRight">
            <div class="footerLeft" >
              info@healthkart.com |
               <a href= "${pageContext.request.contextPath}/pages/loyalty/info/stellarTerms.jsp" target="_blank">Terms and Conditions </a>| Connect with Us:
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