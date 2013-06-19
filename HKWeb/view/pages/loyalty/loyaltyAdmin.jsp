<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" var="loyaltyAdminAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
  <script type="text/javascript">
	$(document).ready(function(e) {
		
		$('.save').click(function(e) {
			e.preventDefault();
			var variant_id = $(this).parent().parent().find('.variant').text();
			var productPoints = parseFloat($(this).parent().parent().find('.points').val());
			var url = $(this).attr("href");
			$.ajax({
				type: 'POST',
				url: url,
				data: {points:productPoints, variantId:variant_id},
				dataType: "json",
				success: function(resp) {
					if (resp.code == '<%=HealthkartResponse.STATUS_OK%>') {
						alert(resp.message);
					} else {
						alert(resp.message);
					}
				}
			});
		});
		
		$('.remove').click(function(e) {
			e.preventDefault();
			var variant_id = $(this).parent().parent().find('.variant').text();
			var url = $(this).attr("href");
			$.ajax({
				type: 'POST',
				url: url,
				data: {variantId:variant_id},
				dataType: "json",
				success: function(resp) {
					if (resp.code == '<%=HealthkartResponse.STATUS_OK%>') {
						alert(resp.message);
					} else {
						alert(resp.message);
					}
				}
			});
			$(this).parent().parent().hide();
		});
		
	});
  </script>
  <div style="background: #FAFAEE;">
  	<c:if test="${! empty loyaltyAdminAction.errorMessages}">
  	<div style="color: red;">Upload Failed! Following errors were found in the file :
  	<br><br>
  	    <c:forEach items="${loyaltyAdminAction.errorMessages}" var="message">
       		<div style="color: red; font-size: 12px;">
       			 ${message}.
        	</div>
       	</c:forEach>
  	</div>
  	</c:if>
  	
  	<div style="color: green;">
  	${loyaltyAdminAction.successMessage}
  	</div>
  	<div style="float: left; clear:both; width:100%;">
	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" id="searchForm">
  		<h2>Search Loyalty Products </h2>
  		<br>Enter Product ID  <s:text name="productId" placeholder="Product Id"></s:text>
  		Enter variantID  <s:text name="variantId" placeholder="Variant Id"></s:text>
  		<s:submit name="searchLoyaltyProducts"  value="Search"/>
  	</s:form>
  	<br><br>
  	</div>
  	<div style="clear:both;">
  	
  		<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction">
      	<h2>File to Upload for Loyalty Products</h2>
     		 <div style="margin-left: 20px;">
     			 <s:file name="csvFileBean" size="30"/> &nbsp;&nbsp;&nbsp;&nbsp; <s:submit name="uploadPorducts" value="Upload"/>
      		</div>
    	</s:form>
    	<p>Columns in CSV - PRODUCT_VARIANT_ID, LOYALTY_POINTS </p>
		<br>
    	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction">
      	<h2>File to Upload for User card numbers </h2>
      		<div >
        		<s:file name="badgeCsvFileBean" size="30"/> &nbsp;&nbsp;&nbsp;&nbsp; <s:submit name="uploadBadges" value="Upload"/>
      		</div>
    	</s:form>
		<p>Columns in CSV - USER_ID, USER_CARD_NUMBER </p>
		</div>
	</div>
	<div>
		<c:if test="${not empty loyaltyAdminAction.loyaltyProducts}">
			<div id="productsTable">
				<table style="width:100%;">
					<thead><tr>
					<th>S No.</th>
					<th>Variant ID</th>
					<th>Product ID</th>
					<th style="width:200px;">Product Name</th>
					<th>Loyalty Points</th>
					<th> &nbsp;</th>
					</tr></thead>
				<%int count=0; %>
				<c:forEach items="${loyaltyAdminAction.loyaltyProducts}" var="lp">
					<tbody><tr>
					<td><%=++count %></td>
					<td class="variant">${lp.variant.id}</td>
					<td>${lp.variant.product.id}</td>
					<td>${lp.variant.product.name}</td>
					<td><input type="text" name="points" class="points" value="${lp.points}" /></td>
					<td style="font-size: 16px;"><s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" event="saveLoyaltyProduct" class="save">Save</s:link> / 
					<s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" event="removeLoyaltyProduct" class="remove">Remove</s:link></td>
					</tr></tbody>
				</c:forEach>
				</table>
			</div>
		</c:if>
	</div>
  </s:layout-component>
</s:layout-render>
