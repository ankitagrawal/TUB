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
	<c:set var="lp" value="${lpa.loyaltyProduct}" />
	<c:set var="prodVariant" value="${lpa.prodvariant}" />
	<c:set var="product" value="${prodvariant.product}"/>
  	<div class="mainContainer container_16 clearfix embedMarginTop20">
    	
    	<!-- Main Title starts -->
    	<div class="titleLine grid_16 embedMarginBottom40">
    		<div class="lineAtLeftRight"></div>
    		<c:choose>
    		<div class="titleLineText">
    			<c:when test = "${not empty prodVariant.productOptions }" >
    					${product.name} , ${prodVariant.variantName}
    					<c:forEach items="${prodVariant.productOptions}" var="prodOption">
		        		      , ${prodOption.value}
		    	        </c:forEach>
    			</c:when>
    			<c:otherwise>
    				${product.name} , ${prodVariant.variantName}
    			</c:otherwise>
    		</div>
    		</c:choose>
    		<div class="lineAtLeftRight"></div>
    	</div>
    	<!-- Main Title ends -->

    	<!-- Main Content starts -->
    	<div class="content grid_16">

    		<!-- Left Block -->
    		<div class="leftBlock grid_7">
				<c:set var="imageId" value = "${product.mainImageId }" />
				<div class="imgContainer">
					<img src="${hk:getS3ImageUrl(imageMediumSize, imageId)}" alt="${product.name}"
					     title="${product.name}" class="productImage-Large" >
				</div>
    		
    			<div class="productImageLarge">
    				<img src="${hk:getS3ImageUrl(imageMediumSize, imageId)}" alt="${product.name}" title="${product.name}" class="productImage-Large" >

    			</div>
    			<div class="smallImages">
            		<img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            		<img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            		<img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            		<img class="productImage-small" alt="productImage-small" src="stellarCatalo_files/chhotaDabba.png">
            		
            		<c:forEach items="${product.productImages}" var="productImage">
					<%-- <a href='javascript:void(0);' rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, productImage.id)}',
								largeimage: '${hk:getS3ImageUrl(imageLargeSize, productImage.id)}'}"> --%>
              		<img src='${hk:getS3ImageUrl(imageSmallSizeCorousal, productImage.id)}' class="productImage-small">
              		<!-- </a> -->
            		
					</c:forEach>
            		
            		
          		</div>
    		</div>

    		<!-- Right Block -->
    		<div class="rightBlock grid_8">
    			<div class="productInformation">
    			<c:if test="${!empty (product.productFeatures)}">
    				<ul>
						<c:forEach var="feature" items="${product.productFeatures}">
	    					<li>${feature.value}</li>
						</c:forEach>    				
    				</ul>
    			</c:if>
    			</div>
    			<div class="productAttrs grid_8">
    				<div class="grid_4">
		    			<c:when test = "${not empty prodVariant.productOptions }" >
    						<c:forEach items="${prodVariant.productOptions}" var="prodOption">
		    	    		  <label>${prodOption.name}</label>
							  <label>${prodOption.value}</label>
			    	        </c:forEach>
						</c:when>
    				</div>
    		<div class="grid_3">
              <div class="points">${hk:roundNumberForDisplay(lp.points)} POINTS</div>
              <div class="buyNowButton">REDEEM NOW</div>
              <div class="dpOrCod">Discreet packaging</div>
    				</div>
    			</div>
    		</div>

    	</div>
    	<!-- Main Content ends -->

	</stripes:layout-component>
</stripes:layout-render>