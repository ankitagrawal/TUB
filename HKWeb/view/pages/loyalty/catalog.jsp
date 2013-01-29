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
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
<stripes:layout-component name="contents">
	
<script type="text/javascript">
	 $(document).ready(function() {
		$('#successToolTipBtn').click(function () {
	    	$('#successToolTip').attr('style','display: none;');
	    });
		
		$('#errorToolTipBtn').click(function () {
    		$('#errorToolTip').attr('style','display: none;');
    	});		
		
		$( 'form' ).submit(function( event ) {
			  event.preventDefault();
			  var form = $( this );
			  $.ajax({
			    type: 'POST',
			    url: 'core/loyaltypg/Cart.action?addToCart',
			    data: form.serialize(),
			    success: function( resp ) {
			    	if (resp.code == '<%=com.hk.web.HealthkartResponse.STATUS_OK%>') {
			    		$( "#" + form.context.id + ' input' ).attr('class', 'btn btn-success');
				    	$( "#" + form.context.id + ' input' ).attr('value', 'Added to Cart »');
				    	$( "#" + form.context.id + ' input' ).disabled = true;
				    	
				    	$('#successToolTip').attr('style','');
				    	$('#errorToolTip').attr('style','display: none;');
			    	} else {
			    		$('#successToolTip').attr('style','display: none;');
			    		document.getElementById("errorMsg").innerHTML = resp.message;
			    		$('#errorToolTip').attr('style','');
			    	}
			    }
			  });
			});
	});
</script>	
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca" />
		<div id="successToolTip" class="row" style="display: none;">
			<div class="span12">
				<div class="alert alert-success">
					<button id="successToolTipBtn" type="button" class="close">×</button>
					<strong>Product added to Cart!&nbsp;&nbsp;&nbsp;</strong><a href="<hk:vhostJs/>/core/loyaltypg/Cart.action">View your Cart</a>
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
				<div class="span3">
					<img class="img-rounded"
						src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/${lp.variant.product.id}.jpg'
						alt="${lp.variant.product.name}" />
					<h4>${lp.variant.product.name}</h4>
					<h6>${lp.points} Points</h6>
					<p>
					<form method="post" action="/core/loyaltypg/Cart.action"
						id="${lp.variant.id}-cartForm">
						<input type="hidden" value="${lp.variant.id}"
							name="productVariantId"> <input type="hidden" value="1"
							name="qty"> <input type="submit" class="btn"
							name="addToCart" value="Add to Cart »">
					</form>
					</p>
				</div>
			</c:forEach>
		</div>
</stripes:layout-component>
</stripes:layout-render>

