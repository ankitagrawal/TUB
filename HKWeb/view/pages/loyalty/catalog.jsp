<!DOCTYPE html>
<%@ page import="com.akube.framework.util.BaseUtils"%>
<%@ page import="com.hk.constants.core.HealthkartConstants"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.hk.taglibs.Functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca"/>

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HealthKart | Loyalty Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="<hk:vhostJs/>/bootstrap/css/bootstrap.css" rel="stylesheet">

	<style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
		padding-right: 20px;
		padding-left: 20px;
      }
    </style>
  </head>

  <body>
	<script src="<hk:vhostJs/>/bootstrap/js/jquery.js"></script>
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript">
	 $(document).ready(function() {
		$( 'form' ).submit(function( event ) {
			  event.preventDefault();
			  var form = $( this );
			  $.ajax({
			    type: 'POST',
			    url: 'core/loyaltypg/Cart.action?addToCart',
			    data: form.serialize(),
			    success: function( resp ) {
			    	$( "#" + form.context.id + ' input' ).attr('class', 'btn btn-success');
			    	$( "#" + form.context.id + ' input' ).attr('value', 'Added to Cart >>');
			    	$( "#" + form.context.id + ' input' ).disabled = true;
			    }
			  });
			});
	});
	</script>
	
    <div class="container">
      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li class="active"><a href="">Home</a></li>
          <li><a href="">Visit Healthkart</a></li>
          <li><a href="core/loyaltypg/Cart.action">View Cart</a></li>
          <li><a href="">Sign In</a></li>
        </ul>
        <h3 class="muted">HealthKart</h3>
      </div>

      <hr>
      <div class="row">
	      <c:forEach items="${lca.productList}" var="lp">
			<div class="span3">
				<img class="img-rounded" src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lp.variant.product.id}.jpg' alt="${lp.variant.product.name}"/>
				<h4>${lp.variant.product.name}</h4>
				<h6>${lp.points} Points</h6>
				<p>
					<form method="post" action="/core/loyaltypg/Cart.action" id="${lp.variant.id}-cartForm">
						<input type="hidden" value="${lp.variant.id}" name="productVariantId">
						<input type="hidden" value="1" name="qty">
						<input type="submit" class="btn" name="addToCart" value="Add to Cart »">
					</form>
				</p>
			</div>
	 	  </c:forEach>
	  </div>
	  <hr>
      <div class="footer">
        <p>© Footer goes here!!</p>
      </div>
    </div>
</body>
</html>
