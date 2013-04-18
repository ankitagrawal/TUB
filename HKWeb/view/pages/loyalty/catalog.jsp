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


<%
boolean isSecure = pageContext.getRequest().isSecure();
pageContext.setAttribute("isSecure", isSecure);
%>

<stripes:layout-render name="/pages/loyalty/layout.jsp">

<stripes:layout-component name="lhsContent">

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca"/>

  
        <div class="grid_4 leftBlock">
          <div class="embedMarginTop119"></div>
          <div class="priceFilterContainerOne">
            <div class="sorting">SORT BY POINTS</div>
                   <div class="brandsContainer">
			<%-- <c:forEach items="${lca.categories}" var="loyaltyCategory">
			      <span  id= "categoryName">${loyaltyCategory.displayName}</span>
			     </div>
			 </c:forEach>
			 --%>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="0"/>
                  <s:param name="maxPoints" value="100"/>
                  <s:param name="testList" value="${lca.testList}"/>
                  less than 100</s:link>
                  </span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="101"/>
                  <s:param name="maxPoints" value="200"/>
                  <s:param name="testList" value="${lca.testList}"/>
                  101-200</s:link></span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="201"/>
                  <s:param name="maxPoints" value="300"/>
                  <s:param name="testList" value="${lca.testList}"/>
                  201-300</s:link></span></div>
				<div class="priceRange">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByPoints" >
                  <s:param name="minPoints" value="301"/>
                  <s:param name="maxPoints" value="10000"/>
                  <s:param name="testList" value="${lca.testList}"/>
                  301 and above</s:link></span></div>
				  </div>
			<div class="sorting">SORT BY CATEGORY</div>
              <div class="brandsContainer ">
               
			<%-- <c:forEach items="${lca.categories}" var="loyaltyCategory">  (${loyaltyCategory.count})
			 --%>
			 <c:forEach items="${lca.testList}" var="loyaltyCategory">
			 <div class="priceRange">
			   <span  id= "categoryNameSpan">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="listProductsByCategory" >
                  <s:param name="testList" value="${lca.testList}"/>
                  <s:param name="categoryName" value="${loyaltyCategory.name}"/>
                  ${loyaltyCategory.displayName}  </s:link></span>
			     </div>
			 </c:forEach>
			</div>
			</div></div>
            
        
	</stripes:layout-component>
  
  <stripes:layout-component name="rhsContent">

    <script type="text/javascript">
      $(document).ready(function() {
        $('#successToolTipBtn').click(function () {
          $('#successToolTip').attr('style', 'display: none;');
        });

        $('#errorToolTipBtn').click(function () {
          $('#errorToolTip').attr('style', 'display: none;');
        });

        $('form').submit(function(event) {
          event.preventDefault();
          var form = $(this);
          $.ajax({
            type: 'POST',
            url: 'core/loyaltypg/Cart.action?addToCart',
            data: form.serialize(),
            success: function(resp) {
              if (resp.code == '<%=com.hk.web.HealthkartResponse.STATUS_OK%>') {
                $("#" + form.context.id + ' input').attr('class', 'btn btn-success');
                $("#" + form.context.id + ' input').attr('value', 'Added to Cart »');
                $("#" + form.context.id + ' input').disabled = true;

                $('#successToolTip').attr('style', '');
                $('#errorToolTip').attr('style', 'display: none;');
              } else {
                $('#successToolTip').attr('style', 'display: none;');
                document.getElementById("errorMsg").innerHTML = resp.message;
                $('#errorToolTip').attr('style', '');
              }
            }
          });
        });
      });
    </script>
    <div id="successToolTip" class="row" style="display: none;">
      <div class="span12">
        <div class="alert alert-success">
          <button id="successToolTipBtn" type="button" class="close">×</button>
          <strong>Product added to Cart!&nbsp;&nbsp;&nbsp;</strong><a href="<hk:vhostJs/>/core/loyaltypg/Cart.action">View
          your Cart</a>
        </div>
      </div>
    </div>
    <div id="errorToolTip" class="row" style="display: none;">
      <div class="span12">
        <div class="alert alert-error">
          <button id="errorToolTipBtn" type="button" class="close" data-dismiss="alert">×</button>
          <strong>Couldn't add to cart!&nbsp;&nbsp;&nbsp;</strong><span id="errorMsg">xxxx</span>
        </div>
      </div>
    </div>

 <%--    <p>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${lca}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${lca}"/>
    </p>
 --%>
    <div class="grid_12 mainContent">
            <div class="grid_12 embedMarginBottom40" id="productCategory">
              <div class="dottedLine"></div>
              <div class="productCategoryText">
                <img src="/healthkart/pages/loyalty/LoyaltyJunk/images/stellarLogo.png" class="stellarLogo" alt="1">
              </div>
              <div class="dottedLine"></div>
            </div>           

			<div class="grid_12 embedMargin autoHeight" id="productsRow1">
			<c:forEach items="${lca.productList}" var="lp">
				<c:set var="variant" value="${lp.variant}"/>
				<c:set var="product" value="${variant.product}"/>
				<div class="product clickable">
	
				<a href="${hk:getS3ImageUrl(imageLargeSize, product.mainImageId,isSecure)}" class="jqzoom" rel='gal1'
					title="${product.name}">
				<img src="<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${product.id}.jpg" alt="${product.name}"
				     title="${product.name}" class="productImage" ></a>
                <div class="productDescription embedMargin">${product.name}</div>
                <div class="stellarPoints">${lp.points} PTS</div>
                <form method="post" action="/core/loyaltypg/Cart.action" id="${variant.id}-cartForm">
				<input type="hidden" value="${variant.id}" name="productVariantId">
				<input type="hidden" value="1" name="qty">
				<input type="submit" class="addToCompare font-caps embedMargin5" name="addToCart" value="REDEEM">
				</form></div>
			</c:forEach>
              </div>
    </div>

  </stripes:layout-component>
  
</stripes:layout-render>

