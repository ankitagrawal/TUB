<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<link href="<hk:vhostCss/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootbox.js"></script>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyProductAction" var="lpa"/>
  	<div class="mainContainer container_16 clearfix embedMarginTop20">
    	
    	<!-- Main Title starts -->
    	<div class="titleLine grid_16 embedMarginBottom40">
    		<div class="lineAtLeftRight"></div>
    		<div class="titleLineText">Optimum Nutrition 100% Whey Gold Standard, Double Rich Chocolate, 1KG</div>
    		<div class="lineAtLeftRight"></div>
    	</div>
    	<!-- Main Title ends -->

    	<!-- Main Content starts -->
    	<div class="content grid_16">

    		<!-- Left Block -->
    		<div class="leftBlock grid_7">
    			<div class="productImageLarge">
    				<img class="productImage-Large" alt="productImage-Large" src="stellarCatalo_files/badaDabba.png">
    			</div>
    			<div class="smallImages">
            <img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            <img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            <img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            <img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
          </div>
    		</div>

    		<!-- Right Block -->
    		<div class="rightBlock grid_8">
    			<div class="productInformation">
    				<ul>
    					<li>Meet 50% of your Protein Requirements in just 1 Serving of 30 gms</li>
						<li>Explosive Muscular Pumps from IntraPro AC6 Pure Whey Isolate</li>
						<li>Tastes way better than any other Whey Isolate</li>
    				</ul>
    			</div>
    			<div class="productAttrs grid_8">
    				<div class="grid_4">
    					<label>Flavors</label>
    					<select>
    						<option value="chocolate">Chocolate</option>
    					</select>
    					<label>Weight</label>
    					<select>
    						<option value="1KG">1KG</option>
    					</select>
    				</div>
    				<div class="grid_3">
              <div class="points">120 POINTS</div>
              <div class="buyNowButton">BUY NOW</div>
              <div class="dpOrCod">Discreet packaging | Cash on delivery</div>
    				</div>
    			</div>
    			<div class="belowButtons grid_6 embedMarginTop20">
            <ul>
              <li class="addTOCmpre">+/- ADD TO COMPARE</li>
              <li class="speak">SPEAK TO A NUTRITIONIST</li>
              <li class="wishlist">ADD TO WISHLIST</li>
            </ul>
          </div>
    		</div>

    	</div>
    	<!-- Main Content starts -->

	</stripes:layout-component>
</stripes:layout-render>