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
    <title>HealthKart | Stellar Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="/healthkart/pages/loyalty/LoyaltyJunk/css/grid.css" rel="stylesheet">
    <link href="/healthkart/pages/loyalty/LoyaltyJunk/css/style.css" rel="stylesheet">
    <link href="/healthkart/pages/loyalty/LoyaltyJunk/css/jquery.jscrollpane.css" rel="stylesheet">
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/js/jquery.jscrollpane.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/js/jquery.jcarousel.min.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/js/jquery-ui.min.js"></script>
	<script type="text/javascript">

	$(document).ready(function() {
		$('.brandsContainer').jScrollPane({
			verticalDragMaxHeight : 30,
			positionDragY : 30
		});
		$(".jspDrag").css("width", "7px");
		$(".jspTrack").css("left", "-10px");
		//Top Drop Down
		$('#menuDropDown').hover(function() {
			$("#box1").css("display", "block");
			$("#menuDropDown").addClass("searchButton-hover");
		}, function() {
			$("#box1").css("display", "none");
			$("#menuDropDown").removeClass("searchButton-hover");
		});
		$('#topToDown').hover(function() {
			$("#box2").css("display", "block");
			$("#topToDown").addClass("topToDown-hover");
		}, function() {
			$("#box2").css("display", "none");
			$("#topToDown").removeClass("topToDown-hover");
		});
		$('#box1').hover(function() {
			$("#box1").css("display", "block");
			$("#menuDropDown").addClass("searchButton-hover");
		}, function() {
			$("#box1").css("display", "none");
			$("#menuDropDown").removeClass("searchButton-hover");
		});
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
        <div class="grid_7" id="logo">
		<c:set var="badge" value="${hk:getBadgeInfoForUser(userId)}" />
          <div class="headerNavs">
            <div class="headerNav"><s:link href="/" title='go to healthkart home'>healthKart</s:link></div>
            <div class="headerNav"><s:link href="http://www.healthkartplus.com" title='go to healthkartplus'>healthkartplus</s:link></div>
            <div class="headerNav">${badge.badgeName}</div>
          </div>
          <div class="logo-block">
            <div class="logoSubblock">
              <a title="healthkart" href="#">
                <img width="234" height="72" alt="healthkart logo" src="/healthkart/pages/loyalty/LoyaltyJunk/images/healthkartStellarLogo.png">
              </a>
            </div>
          </div>
        </div>
		<div class="grid_3">
          <div class="userName">
            <p class="section1 font-caps">
			Hello <strong>
                  <shiro:hasAnyRoles name="<%=roles%>">
                    <shiro:principal property="firstName"/>
                  </shiro:hasAnyRoles>
                </strong>
			</p>
          </div>
        </div>
        
		<div class="grid_6">
          <div class="topDropDown">
            <div class="topToDown">
              <div class="searchButton" id="menuDropDown" >
                <div class="searchButtonText">stellarstore</div>
                <img src="/healthkart/pages/loyalty/LoyaltyJunk/images/arrow.png" class="arrowDown">
              </div>
              <div class="box" id="box1">
                <div class="item">Redeem points</div>
                <div class="item">
				<s:link beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" event="pre" title="View History">View History</s:link>
				</div>
                <div class="seperator"></div>
                <div style="top: 0px;" class="item">
				<s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="aboutLoyaltyProgram">How stellar works</s:link>
				</div>
              </div>
            </div>
          </div>
          <div class="nav-menu" id="nav">
              <div>
                <p class="section1 font-caps"> <a href="<hk:vhostJs/>/core/loyaltypg/Cart.action"> Cart</a></p>
              </div>
          </div>
          
          <div class="search" id="search">
            <input type="text" placeholder="Search" class="searchText">
            <img width="11" height="13" src="/healthkart/pages/loyalty/LoyaltyJunk/images/searchIcon.png" class="searchLense" alt="search">
          </div>
        </div>
      </div>
    </div>
		  <!-- 
	    <span style="float:left">
		    Total available loyalty points: ${hk:getLoyaltyKarmaPointsForUser(userId)}
	    </span>
	    
	    
	    <span class="pull-right">
	    Welcome,
	      <strong>
                  <shiro:hasAnyRoles name="<%=roles%>">
                    <shiro:principal property="firstName"/>
                  </shiro:hasAnyRoles>
                </strong>
		    &nbsp;&nbsp;&nbsp; (${badge.badgeName})
		</span>


      <hr>
      -->
        <div class="mainContainer embedMarginTop100">
      <div class="container_16 clearfix">
      <s:layout-component name="lhsContent"/>      
			<stripes:layout-component name="contents"/>
        <s:layout-component name="rhsContent"/>
</div></div>
    <div id="footer">
        <div class="container_16 clearfix embedMarginTop100">
          <div class="grid_4">
            <div class="footerLeft">
              about | FAQs 
            </div>
          </div>
          <div class="grid_12">
            <div class="footerRight" style="width: 672px;">
            <div class="footerLeft" >
              info@healthkartplus.com | Established in India | Privacy Policy | Terms and Conditions | Connect with Us:
            </div>
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