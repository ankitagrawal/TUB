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
</head>
<link href="<hk:vhostCss/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootbox.js"></script>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyProductAction" var="lpa"/>
	<c:set var="lp" value="${lpa.loyaltyProduct}" />
	<c:set var="prodVariant" value="${lpa.prodVariant}" />
	<c:set var="product" value="${lpa.product}"/>
  	<div class="mainContainer container_16 clearfix embedMarginTop20">
    	    <script type="text/javascript">
      $(document).ready(function() {

    	  $(".jspDrag").css("height","30px");
        $('#successToolTipBtn').click(function (resp) {
        	
          $('#successToolTip').attr('style', 'display: none;');
        });

        $('#errorToolTipBtn1').click(function (resp) {
        	
          $('#errorToolTip').attr('style', 'display: none;');
        });
	
        
        $('.cartFormm').submit(function(event) {
          event.preventDefault();
          var form = $(this);
          var redeemButton = form.find('.buyNowButton');
          
          $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/core/loyaltypg/Cart.action?addToCart=',
            data: form.serialize(),
            success: function(resp) {
              if (resp.code == '<%=com.hk.web.HealthkartResponse.STATUS_OK%>') {
                $("#" + form.context.id + ' input').attr('class', 'btnDisabled');
                redeemButton.attr('value', 'Added to Cart');
                redeemButton.removeClass();
                redeemButton.disabled =true;
                //$("#" + form.context.id + ' input').disabled = true;

                $('#successToolTip').attr('style', 'display:block;');
                $('#errorToolTip').attr('style', 'display: none;');
       		    $('html, body').animate({scrollTop:$('#nav').offset().top - 20}, 'fast');
              } else {
			  $('#successToolTip').attr('style', 'display: none;');
			  //  $("#errorMsg").attr('style', 'display:block;');
	            $("#errorMsg").text(resp.message);
                $('#errorToolTip').attr('style', 'display:block;');
	      		$('html, body').animate({scrollTop:$('#nav').offset().top - 20}, 'fast');

              }
            }            
          });
        });
      });
    </script>
    <div id="successToolTip" class="row" style="display: none;">
      <div class="span7">
        <div class="alert alert-success">
          <button id="successToolTipBtn" type="button" class="close">x</button>
          Product added to Cart!&nbsp;&nbsp;&nbsp;<strong><a href="${pageContext.request.contextPath}/core/loyaltypg/Cart.action">View
          your Cart</a></strong>
        </div>
      </div>
    </div>
    <div id="errorToolTip" class="row" style="display: none;">
      <div class="span7">
        <div class="alert alert-error">
          <button id="errorToolTipBtn1" type="button" class="close" >x</button>
          <strong>Couldn't add to cart!&nbsp;&nbsp;&nbsp;</strong><span id="errorMsg" ></span>
        </div>
      </div>
    </div>

    	
    	<!-- Main Title starts -->
    	<div class="titleLine grid_16 embedMarginBottom40">
    		<div class="lineAtLeftRight"></div>
    		<c:choose>
    			<c:when test = "${not empty prodVariant.productOptions }" >
		    		<div class="titleLineText">
    					${product.name}  
    						<c:if test = "${not empty prodVariant.variantName}">
					    	    , ${prodVariant.variantName}
		        		     </c:if>
    					
    					<c:forEach items="${prodVariant.productOptions}" var="prodOption">
    						<c:if test = "${not empty prodOption.value}">
		        		      , ${prodOption.value}
		        		     </c:if>
		    	        </c:forEach>
		    	     </div>
    			</c:when>
    			<c:otherwise>
    				<div class="titleLineText">
    					${product.name}  
  						<c:if test = "${not empty prodVariant.variantName}">
				    	   , ${prodVariant.variantName}
	        		     </c:if>

    				</div>
    			</c:otherwise>
    		</c:choose>
    		<div class="lineAtLeftRight"></div>
    	</div>
    	<!-- Main Title ends -->

    	<!-- Main Content starts -->
    	<div class="content grid_16">

    		<!-- Left Block -->
    		<div class="leftBlock grid_7">
    			<c:choose>
	    			<c:when test= "${prodVariant.mainImageId gt 0}">
						<c:set var="imageId" value = "${prodVariant.mainImageId }" />
    				</c:when>
    				<c:otherwise>
    					<c:set var="imageId" value = "${product.mainImageId }" />
    				</c:otherwise>
    			</c:choose>
				<div class="productImageLarge">
    				<img src="${hk:getS3ImageUrl(imageMediumSize, imageId)}" alt="${product.name}" title="${product.name}" class="productImage-Large" >

    			</div>
    			<div class="smallImages">
            		<c:forEach items="${prodVariant.productImages}" var="productImage">
					 <a href='javascript:void(0);' rel="{gallery: 'gal1', smallimage: '${hk:getS3ImageUrl(imageMediumSize, productImage.id)}',
								largeimage: '${hk:getS3ImageUrl(imageLargeSize, productImage.id)}'}"> 
              		<img src='${hk:getS3ImageUrl(imageSmallSizeCorousal, productImage.id)}' class="productImage-small">
              		 </a>
            		
					</c:forEach>
            		
            		
          		</div>
    		</div>

    		<!-- Right Block -->
    		<div class="rightBlock grid_8">
    			<div class="productInformation">
    <%-- 			<c:if test="${not empty product.productFeatures}">
    				<ul>
						<c:forEach var="feature" items="${product.productFeatures}">
	    					<li>${feature.value}</li>
						</c:forEach>    				
    				</ul>
    			</c:if> --%>
    				${product.overview}
    			</div>
    			<div class="productAttrs grid_8">
    				<div class="grid_4">
		    			<c:if test = "${not empty prodVariant.productOptions }" >
    						<c:forEach items="${prodVariant.productOptions}" var="prodOption">
		    	    		  <label><strong>${prodOption.name}</strong></label>
							  <label>${prodOption.value}</label>
							  <br>
			    	        </c:forEach>
						</c:if>
    				</div>
    		<div class="grid_3">
             	<div class="points">${hk:roundNumberForDisplay(lp.points)} POINTS</div>
				<s:form method="post" action="${pageContext.request.contextPath}/core/loyaltypg/Cart.action" id="${prodVariant.id}-cartForm" class="cartFormm">
					<s:hidden value="${prodVariant.id}" name="productVariantId" />
					<s:hidden value="1" name="qty" id="qty"/>
					<s:submit class="buyNowButton" name="addToCart" value="REDEEM NOW" />

				</s:form>
   			</div>
   		</div>
   	</div>
    	<!-- Main Content ends -->

	</stripes:layout-component>
</stripes:layout-render>