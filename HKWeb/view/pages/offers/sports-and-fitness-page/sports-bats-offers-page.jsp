<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="sports offer">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/pages/offers/sports-and-fitness-page/css/bats-offers-stylesheet.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">sports Offers</span>

		<h1 class="title">sports Offers</h1>
	</div>

</s:layout-component>
    <s:layout-component name="content">
    <div id="container">
      	<div class="nRow">&nbsp;</div>
        <div class="nRow">
        	<div class="eyeComboLeftBar">
            		<h5>Browse By Category</h5>
                    <ul>
                    	<li><a href="http://www.healthkart.com/sports/sports-equipment/cricket/cricket-wear">Cricket Wear</a></li>
                    </ul>
            </div>
            <div class="eyeComboRightBar">
            	<div class="nRow">
                	<div class="cricketWearMsg">
               	  <h1>Purchase any of these bats to get <span>30% off</span> on our <span>Cricket Wear</span> range</h1>
                </div>
                	<div class="cWBtn"><a href="http://www.healthkart.com/sports/sports-equipment/cricket/cricket-wear"  target="_blank">CLICK HERE</a></div>
                </div>
            	<div class="nRow">
                	<div class="cpnCodeMsgBx">To avail the offer, use this coupon code</div>
                    <div class="cpnCodeBx">HKCRICKET</div>
                </div>
            	<div class="nRow">
                	<div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT315"><img src="images/bats/SPT315.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT315" class="proName">SS Yuvi 20/20 Cricket Bat</a></p>
                        <p class="Price"><span>Rs.5,670</span> Rs.4,275</p>
                        <p class="discount">25% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2554"><img src="images/bats/SPT2554.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2554" class="proName">SG Nexus Xtreme English Willow Cricket Bat</a></p>
                        <p class="Price"><span>Rs.3,699</span> Rs.3,145</p>
                        <p class="discount">15% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2552"><img src="images/bats/SPT2552.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2552" class="proName">SG Reliant Xtreme English Willow Cricket Bat</a></p>
                        <p class="Price"><span>Rs.4,499</span> Rs.3,825</p>
                        <p class="discount">15% off</p>
                    </div>
              	</div>
              	<div class="cl"></div>
                <div class="nRow">
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2456"><img src="images/bats/SPT2456.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2456" class="proName">Slazenger V-100 Performance English Willow Cricket Bat</a></p>
                        <p class="Price"><span>Rs.4,499</span> Rs.3,150</p>
                        <p class="discount">30% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT319"><img src="images/bats/SPT319.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT319" class="proName">SS Power Cricket Bat</a></p>
                        <p class="Price"><span>Rs.2,690</span> Rs.2,008</p>
                        <p class="discount">25% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2255"><img src="images/bats/SPT2255.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2255" class="proName">Adidas Mblaster League Cricket Bat</a></p>
                        <p class="Price"><span>Rs.4,499</span> Rs.3,595</p>
                        <p class="discount">20% off</p>
                    </div>
              	</div>
              	<div class="cl"></div>
              	<div class="nRow">
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2550"><img src="images/bats/SPT2550.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2550" class="proName">SG VS 319 Super English Willow Cricket Bat</a></p>
                        <p class="Price"><span>Rs.4,499</span> Rs.3,825</p>
                        <p class="discount">15% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2557"><img src="images/bats/SPT2557.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2557" class="proName">SG Cobra Gold Kashmir Willow Cricket Bat</a></p>
                        <p class="Price"><span>Rs.2,149</span> Rs.1,825</p>
                        <p class="discount">15% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2256"><img src="images/bats/SPT2256.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT2256" class="proName">Adidas Mblasater Rookie Cricket Bat</a></p>
                        <p class="Price"><span>Rs.3,999</span> Rs.3,599</p>
                        <p class="discount">10% off</p>
                    </div>
              	</div>
              	<div class="cl"></div>
                <div class="nRow">
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT324"><img src="images/bats/SPT324.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT324" class="proName">SS Sangakara Cricket Bat</a></p>
                        <p class="Price"><span>Rs.1,563</span> Rs.1,244</p>
                        <p class="discount">20% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT321"><img src="images/bats/SPT321.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT321" class="proName">SS Gladiator Cricket Bat</a></p>
                        <p class="Price"><span>Rs.2,268</span> Rs.1,695</p>
                        <p class="discount">25% off</p>
                    </div>
                    <div class="comboBxs">
                    	<p class="img"><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT1347"><img src="images/bats/SPT1347.jpg"/></a></p>
                        <p><a href="http://www.healthkart.com/product/body-balance-ion-wristbands/SPT1347" class="proName">Cosco 2000 Cricket Bat</a></p>
                        <p class="Price"><span>Rs.4,725</span> Rs.3,550</p>
                        <p class="discount">25% off</p>
                    </div>
              	</div>
              	<div class="nRow">&nbsp;</div>
            </div>
        </div>
        <div class="nRow">&nbsp;</div>
    </div>

</s:layout-component>

</s:layout-render>