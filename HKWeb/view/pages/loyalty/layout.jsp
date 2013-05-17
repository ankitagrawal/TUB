<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
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

    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/grid.css" rel="stylesheet">
    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/style.css" rel="stylesheet">
    <link href="<hk:vhostJs/>/pages/loyalty/resources/css/jquery.jscrollpane.css" rel="stylesheet">
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery.jscrollpane.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery.jcarousel.min.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js" > </script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jquery-ui.min.js"></script>
	<script type="text/javascript">
    $(document).ready(function(){
        $('.brandsContainer').jScrollPane({verticalDragMaxHeight:30, positionDragY:30});
        $(".jspDrag").css("width","7px");
        $(".jspTrack").css("left","-5px");


        //Top Drop Down
        $('#searchButton').hover(function(){
          $("#box1").css("display","block");
          $("#searchButton").addClass("searchButton-hover");
        },
        function(){
          $("#box1").css("display","none");
          $("#searchButton").removeClass("searchButton-hover");
        }
        );
        $('#topToDown').hover(function(){
          $("#box2").css("display","block");
          $("#topToDown").addClass("topToDown-hover");
        },
        function(){
          $("#box2").css("display","none");
          $("#topToDown").removeClass("topToDown-hover");
        }
        );
        $('#box1').hover(function(){
          $("#box1").css("display","block");
          $("#searchButton").addClass("searchButton-hover");
        },
        function(){
          $("#box1").css("display","none");
          $("#searchButton").removeClass("searchButton-hover");
        }
        );
      });
</script>

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

    <div class="embedMargin" id="header">
        <div class="container_16 clearfix">
	<c:set var="badge" value="${hk:getBadgeInfoForUser(userId)}" />
        
		<div id="logo" class="grid_6">
            <div class="logo-block">
              <p class="hkpolicy"><span> <s:link href="/" title='go to healthkart home' target='_blank'>healthkart</s:link></span>
			  </p>
              <div class="logoSubblock">
			  <a title="go to stellar home" href="${pageContext.request.contextPath}/loyaltypg">
                <img width="283" height="83" alt="healthkart logo" src="<hk:vhostJs/>/pages/loyalty/resources/images/hkStellar.png">
              </a>
              </div>
  
            </div>
        </div>

        <div class="grid_10">
			<div id="nav" class="nav-menu">
				<div>
				<p class="section2" style="float:right; font-weight: bold;">Hello 
                  <shiro:hasAnyRoles name="<%=roles%>">
                    <shiro:principal property="firstName"/>
                  </shiro:hasAnyRoles>
				  ,</p>
				<p class="section1 rewardText"> ${badge.badgeName} member</p>
				<p class="section1 rewardText">you have ${hk:roundNumberForDisplay(hk:getLoyaltyKarmaPointsForUser(userId))} points</p> 
            </div>
           <div>
              <p class="section2 ">
			  <s:link beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" event="pre" title="View my account">
			  my <span class="boldfont">account</span> 
			  </s:link>
			  </p>
            </div>

            <div>
              <p class="section2">
				<s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="aboutLoyaltyProgram">
				about <span class="boldfont">  stellar </span>
				</s:link>
				</p> 
            </div>
             <div>
              <p class="section2 font-caps"> <s:link beanclass="com.hk.web.action.core.auth.LogoutAction" rel="noFollow">signout</s:link></p>
            </div>

            <div>
              <p class="section2 font-caps"> <a href="<hk:vhostJs/>/core/loyaltypg/Cart.action">cart</a></p>
            </div>

        </div>
        
<!--         <div id="search" class="search">
          <input type="text" class="searchText" placeholder="Search">
          <img alt="search" class="searchLense" width="11" height="13" src="../images/search.png">
     
        </div>
 -->
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
              info@healthkart.com | <a href= "${pageContext.request.contextPath}/pages/loyalty/info/stellarTerms.jsp" target="_blank">Terms and Conditions </a> | Connect with Us:
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