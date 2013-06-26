<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>



<stripes:layout-render name="/pages/loyalty/layout.jsp">

<stripes:layout-component name="lhsContent">

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca"/>

  
        <div class="grid_4 leftBlock">
          <div class="embedMarginTop119"></div>
          <div class="priceFilterContainerOne" style="margin-bottom:30px;">
            <div class="sorting">SORT BY POINTS</div>
                  <div class="priceRange optionBottom">
                 <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" style="width:100%; float:left;">
			   		<strong><span class="categoryNameSpan" style="font-size: 16px;">
                  		clear all </span></strong>
			 	  </s:link></div>
                  <c:set var="rangeSelected" value="${lca.rangeSelected}"/>
                  <div class="brandsContainer" >
	   			  <div class="priceRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" style="width:100%; float:left;">
                  <s:param name="minPoints" value="0"/>
                  <s:param name="maxPoints" value="100"/>
                    <c:choose>
                      <c:when test="${rangeSelected == 1}">
		                  <span class="pointRange selectedCategory">less than 100</span>
                      </c:when>
                      <c:otherwise>
							<span class="pointRange">less than 100</span>
                      </c:otherwise>
                    </c:choose>
                  </s:link>
                  </div>
				  <div class="priceRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" style="width:100%; float:left;">
                  <s:param name="minPoints" value="101"/>
                  <s:param name="maxPoints" value="200"/>
                    <c:choose>
                      <c:when test="${rangeSelected == 2}">
		                  <span class="pointRange selectedCategory">101-200</span>
                      </c:when>
                      <c:otherwise>
							<span class="pointRange">101-200</span>
                      </c:otherwise>
                    </c:choose>
                  </s:link>
				  </div>
				<div class="priceRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" style="width:100%; float:left;">
                  <s:param name="minPoints" value="201"/>
                  <s:param name="maxPoints" value="300"/>
                  <c:choose>
                      <c:when test="${rangeSelected == 3}">
		                  <span class="pointRange selectedCategory">201-300</span>
                      </c:when>
                      <c:otherwise>
							<span class="pointRange">201-300</span>
                      </c:otherwise>
                    </c:choose>
                  </s:link>
                  </div>
				<div class="priceRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" style="width:100%; float:left;">
                  <s:param name="minPoints" value="301"/>
                  <s:param name="maxPoints" value="10000"/>
                  <c:choose>
                      <c:when test="${rangeSelected == 4}">
		                  <span class="pointRange selectedCategory">301 and above</span>
                      </c:when>
                      <c:otherwise>
							<span class="pointRange">301 and above</span>
                      </c:otherwise>
                    </c:choose>
                  </s:link></div>
				  </div>
			</div>
			
		<div class="priceFilterContainerOne">
			<div class="sorting">SORT BY CATEGORY</div>
              <div class="priceRange optionBottom">
              <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" style="width:100%; float:left;">
			   <strong><span class="categoryNameSpan" style="font-size: 16px;">
                  clear all </span></strong>
			 </s:link></div>
              <div class="brandsContainer " style ="height: 245px;">
			<c:forEach items="${lca.categories}" var="loyaltyCategory">  
			 <div class="priceRange">
            <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByCategory"  style="width:100%; float:left;">
			   <span class="categoryNameSpan font-small">
                    <c:choose>
                      <c:when test="${lca.categoryName == loyaltyCategory.name}">
                        <span class="selectedCategory">
                            ${loyaltyCategory.displayName} (${loyaltyCategory.prodCount})</span>
                      </c:when>
                      <c:otherwise>
                        ${loyaltyCategory.displayName} (${loyaltyCategory.prodCount})
                      </c:otherwise>
                    </c:choose>
                   </span>
        	     <s:param name="categoryName" value="${loyaltyCategory.name}"/>
			   </s:link>
			     </div>
			 </c:forEach>
			</div>
			</div>
			</div>
            
        
	</stripes:layout-component>
  
  <stripes:layout-component name="rhsContent">

    <script type="text/javascript">
      $(document).ready(function() {

    	  $(".jspDrag").css("height","80px");
        $('#successToolTipBtn').click(function (resp) {
        	
          $('#successToolTip').attr('style', 'display: none;');
        });

        $('#errorToolTipBtn1').click(function (resp) {
        	
          $('#errorToolTip').attr('style', 'display: none;');
        });
	
        
        $('.cartFormm').submit(function(event) {
          event.preventDefault();
          var form = $(this);
          var redeemButton = form.find('.redeemBtn');
          
          $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/core/loyaltypg/Cart.action?addToCart=',
            data: form.serialize(),
            success: function(resp) {
              if (resp.code == '<%=com.hk.web.HealthkartResponse.STATUS_OK%>') {
                $("#" + form.context.id + ' input').attr('class', 'btnDisabled');
                redeemButton.attr('value', 'Added to Cart');
                redeemButton.removeAttr('href');
                redeemButton.removeClass('redeemBtn');
                redeemButton.attr('disabled','disabled');
                $('#productsInCart').html(resp.data.itemsInCart);
                $('#successToolTip').attr('style', 'display:block;');
                $('#errorToolTip').attr('style', 'display: none;');
       		    $('html, body').animate({scrollTop:$('#nav').offset().top - 20}, 'fast');
              } else {
			  $('#successToolTip').attr('style', 'display: none;');
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
          <button id="successToolTipBtn" type="button" class="close">×</button>
          Product added to Cart!&nbsp;&nbsp;&nbsp;<strong><a href="${pageContext.request.contextPath}/core/loyaltypg/Cart.action">View
          your Cart</a></strong>
        </div>
      </div>
    </div>
    <div id="errorToolTip" class="row" style="display: none;">
      <div class="span7">
        <div class="alert alert-error">
          <button id="errorToolTipBtn1" type="button" class="close" >×</button>
          <strong>Couldn't add to cart!&nbsp;&nbsp;&nbsp;</strong><span id="errorMsg" ></span>
        </div>
      </div>
    </div>

    <div class="grid_12 mainContent">
            <div class="grid_12 embedMarginBottom40" id="productCategory">
              <div class="dottedLine"></div>
              <div class="productCategoryText">
                <img src="<hk:vhostImage/>/pages/loyalty/resources/images/loyaltyLogo.png" class="loyaltyLogo" alt="1">
              </div>
              <div class="dottedLine"></div>
            </div>        
               
        <c:choose>
			<c:when test="${not empty lca.productList}">
			
			<% int rowCount=0; int colCount=0;%>
			<c:forEach items="${lca.productList}" var="lp">
			<% if (colCount %3==0) { colCount++; rowCount++; %>
			<div class="grid_12 embedMargin autoHeight" id="productsRow<%=rowCount%>">
			<%
			} else { colCount++; }
			%>
				<div class="product clickable">
				<s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyProductAction">
				<c:set var="variant" value="${lp.variant}"/>
				<c:set var="product" value="${variant.product}"/>
				<c:set var="imageId" value = "${variant.product.mainImageId }" />
					<div class="imgContainer">
						<img src="${hk:getS3ImageUrl(imageMediumSize, imageId)}" alt="${product.name}"
					    	 title="${product.name}" class="productImage" >
						<s:param name ="prodVariantId" value="${variant.id}" />
					</div>
				</s:link>
                <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyProductAction"><div class="productDescription embedMargin">
                ${product.name}<s:param name ="prodVariantId" value="${variant.id}" />
                </div></s:link>
                <div class="loyaltyPoints">${hk:roundNumberForDisplay(lp.points)} PTS</div>
                <form method="post" action="${pageContext.request.contextPath}/core/loyaltypg/Cart.action" id="${variant.id}-cartForm" class="cartFormm">
				<input type="hidden" value="${variant.id}" name="productVariantId">
				<input type="hidden" value="1" name="qty">
				<input type="submit" class="buyNowButton font-caps embedMargin5 redeemBtn" name="addToCart" value="REDEEM">
				</form>
				</div>
			<% if (colCount %3==0) { %>
			</div>
			<% } %>
			
			</c:forEach>
            </c:when>
		<c:otherwise>
			<div class="row">
				<div class="span12">
					<h4>No products to display in this category. Please choose some other category. </h4>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
              
    </div>
    <s:form  beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" autocomplete="off">
    	<s:hidden name = "minPoints"></s:hidden>
    	<s:hidden name = "maxPoints"></s:hidden>
    	<s:hidden name = "categoryName"></s:hidden>
         <s:layout-render name="/pages/loyalty/pagination.jsp" paginatedBean="${lca}"/>
	</s:form>
  </stripes:layout-component>
  
</stripes:layout-render>

