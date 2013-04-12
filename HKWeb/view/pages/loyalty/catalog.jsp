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
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/css/style2.css" rel="stylesheet">
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/css/accordian_navs.css" rel="stylesheet">
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/css/grid.css" rel="stylesheet">
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyJunk/css/jquery.jscrollpane.css" rel="stylesheet">
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

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca"/>

  <stripes:layout-component name="lhsContent">
  <div class="embedMargin priceFilterContainer">
              <div class="priceFilterHeading">Sort By Points</div>
              <div class="brandsContainer jspScrollable" style="overflow: hidden; padding: 0px; width: 218px;" tabindex="0">
                
              <div class="jspContainer" style="width: 218px; height: 220px;">
              <div class="jspPane" style="padding: 20px 0px; width: 210px; top: 0px;">
			<%-- <c:forEach items="${lca.categories}" var="loyaltyCategory">
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "categoryName">${loyaltyCategory.displayName}</span>
			     </div>
			 </c:forEach>
			 --%>
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "pointRange">
                  <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" event="pre" title="Show History">less than 100</s:link>
                  </span>
			     </div>
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "pointRange">101-200</span>
			     </div>
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "pointRange">201-300</span>
			     </div>
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "pointRange">301 and above</span>
			     </div>
			 
			<div class="jspVerticalBar"><div class="jspCap jspCapTop"></div>
			<div class="jspTrack" style="height: 220px; left: -10px;">
			<div class="jspDrag" style="height: 30px; width: 7px;">
			<div class="jspDragTop"></div><div class="jspDragBottom"></div></div>
			</div>
			<div class="jspCap jspCapBottom"></div></div></div></div>
            </div>
  </div>
  
  <div class="embedMargin priceFilterContainer">
              <div class="priceFilterHeading">Sort By Category</div>
              <div class="brandsContainer jspScrollable" style="overflow: hidden; padding: 0px; width: 218px;" tabindex="0">
                
              <div class="jspContainer" style="width: 218px; height: 220px;">
              <div class="jspPane" style="padding: 20px 0px; width: 210px; top: 0px;"><div class="priceRange">

			<c:forEach items="${lca.categories}" var="loyaltyCategory">
				<div class="priceRange">
                  <input type="checkbox">
                  <span  id= "categoryName">${loyaltyCategory.displayName}</span>
			     </div>
			 </c:forEach>
			</div>
			<div class="jspVerticalBar"><div class="jspCap jspCapTop"></div>
			<div class="jspTrack" style="height: 220px; left: -10px;">
			<div class="jspDrag" style="height: 30px; width: 7px;">
			<div class="jspDragTop"></div><div class="jspDragBottom"></div></div>
			</div>
			<div class="jspCap jspCapBottom"></div></div></div></div>
            </div>
  </div>
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

    <p>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${lca}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${lca}"/>
    </p>

    <div class="row">
      <c:forEach items="${lca.productList}" var="lp">
        <c:set var="variant" value="${lp.variant}"/>
        <c:set var="product" value="${variant.product}"/>
          <div class="span3">
          	<a href="${hk:getS3ImageUrl(imageLargeSize, product.mainImageId,isSecure)}" class="jqzoom" rel='gal1'
			   title="${product.name}">
				<img itemprop="image" src="${hk:getS3ImageUrl(imageMediumSize, product.mainImageId,isSecure)}" alt="${product.name}"
				     title="${product.name}">
			</a>
           
            <h4>${product.name}</h4>
            <h6>${lp.points} Points</h6>

            <p>

            <form method="post" action="/core/loyaltypg/Cart.action"
                  id="${variant.id}-cartForm">
              <input type="hidden" value="${variant.id}"
                     name="productVariantId">
              <input type="hidden" value="1" name="qty">
              <input type="submit" class="btn" name="addToCart" value="Add to Cart »">
            </form>
          </div>
      </c:forEach>
    </div>
  </stripes:layout-component>
  
</stripes:layout-render>

