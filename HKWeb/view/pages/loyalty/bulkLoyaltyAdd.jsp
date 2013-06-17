<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction" var="bulkAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
  <div style="background: #FAFAEE;">
  	<c:if test="${! empty bulkAction.errorMessages}">
  	<div style="color: red;">Upload Failed! Following errors were found in the file :
  	<br><br>
  	    <c:forEach items="${bulkAction.errorMessages}" var="message">
       		<div style="color: red; font-size: 12px;">
       			 ${message}.
        	</div>
       	</c:forEach>
  	</div>
  	</c:if>
  	
  	<div style="color: green;">
  	${bulkAction.successMessage}
  	</div>
  	<div style="float: left; clear:both; width:100%;">
	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction" id="searchForm">
  		<h2>Search Loyalty Products </h2>
  		<br>Enter Product ID  <s:text name="productId" placeholder="Product Id"></s:text>
  		Enter variantID  <s:text name="variantId" placeholder="Variant Id"></s:text>
  		<s:submit name="searchLoyaltyProducts"  value="Search"/>
  	</s:form>
  	<br><br>
  	</div>
  	<div style="clear:both;">
  	
  		<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction">
      	<h2>File to Upload for Loyalty Products</h2>
     		 <div style="margin-left: 20px;">
     			 <s:file name="csvFileBean" size="30"/> &nbsp;&nbsp;&nbsp;&nbsp; <s:submit name="uploadPorducts" value="Upload"/>
      		</div>
    	</s:form>
    	<p>Columns in CSV - PRODUCT_VARIANT_ID, LOYALTY_POINTS </p>
		<br>
    	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction">
      	<h2>File to Upload for User card numbers </h2>
      		<div >
        		<s:file name="badgeCsvFileBean" size="30"/> &nbsp;&nbsp;&nbsp;&nbsp; <s:submit name="uploadBadges" value="Upload"/>
      		</div>
    	</s:form>
		<p>Columns in CSV - USER_ID, USER_CARD_NUMBER </p>
		</div>
	</div>
	<div>
		<c:if test="${not empty bulkAction.loyaltyProducts}">
			<div id="productsTable">
				<table class="cont">
					<tr>
					<th>S No.</th>
					<th>Variant ID</th>
					<th>Product ID</th>
					<th style="width:200px;">Product Name</th>
					<th>Loyalty Points</th>
					<th> &nbsp;</th>
					</tr>
				<%int count=0; %>
				<c:forEach items="${bulkAction.loyaltyProducts}" var="lp">
					<tr>
					<td><%=++count %></td>
					<td>${lp.variant.id}</td>
					<td>${lp.variant.product.id}</td>
					<td>${lp.variant.product.name}</td>
					<td>${lp.points}</td>
					<td>Save | Delete</td>
					</tr>
				</c:forEach>
				</table>
			</div>
		</c:if>
	</div>
  </s:layout-component>
</s:layout-render>
