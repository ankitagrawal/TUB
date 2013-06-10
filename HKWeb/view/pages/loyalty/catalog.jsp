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
                   <div class="brandsContainer" >
					<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="0"/>
                  <s:param name="maxPoints" value="100"/>
                  less than 100</s:link>
                  </span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="101"/>
                  <s:param name="maxPoints" value="200"/>
                  101-200</s:link></span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="201"/>
                  <s:param name="maxPoints" value="300"/>
                  201-300</s:link></span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="301"/>
                  <s:param name="maxPoints" value="10000"/>
                  301 and above</s:link></span></div>
				  </div>
			</div>
			
		<div class="priceFilterContainerOne">
			<div class="sorting">SORT BY CATEGORY</div>
              <div class="brandsContainer " style ="height: 245px;">
               <div class="priceRange">
			   <span  id= "categoryNameSpan" >
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" >
                  clear all </s:link></span>
			     </div>
			 
			<c:forEach items="${lca.categories}" var="loyaltyCategory">  
			 <div class="priceRange">
			   <span  id= "categoryNameSpan" class="font-small">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByCategory" >
                  <s:param name="categoryName" value="${loyaltyCategory.name}"/>
                    <c:choose>
                      <c:when test="${lca.categoryName == loyaltyCategory.name}">
                        <strong>
                            ${loyaltyCategory.displayName} ( ${loyaltyCategory.prodCount} )</strong>
                      </c:when>
                      <c:otherwise>
                        ${loyaltyCategory.displayName} ( ${loyaltyCategory.prodCount} )
                      </c:otherwise>
                    </c:choose>
                   </s:link></span>
			     </div>
			 </c:forEach>
			</div>
			</div>
			</div>
            
        
	</stripes:layout-component>
  
  <stripes:layout-component name="rhsContent">

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
          var redeemButton = form.find('.redeemBtn');
          
          $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/core/loyaltypg/Cart.action?addToCart=',
            data: form.serialize(),
            success: function(resp) {
              if (resp.code == '<%=com.hk.web.HealthkartResponse.STATUS_OK%>') {
                $("#" + form.context.id + ' input').attr('class', 'btnDisabled');
                redeemButton.attr('value', 'Added to Cart');
                redeemButton.removeClass()
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
                <img src="<hk:vhostImage/>/pages/loyalty/resources/images/stellarLogo.png" class="stellarLogo" alt="1">
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
                <div class="productDescription embedMargin">${product.name}</div>
                <div class="stellarPoints">${hk:roundNumberForDisplay(lp.points)} PTS</div>
                <form method="post" action="${pageContext.request.contextPath}/core/loyaltypg/Cart.action" id="${variant.id}-cartForm" class="cartFormm">
				<input type="hidden" value="${variant.id}" name="productVariantId">
				<input type="hidden" value="1" name="qty">
				<input type="submit" class="addToCompare font-caps embedMargin5 redeemBtn" name="addToCart" value="REDEEM">
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

