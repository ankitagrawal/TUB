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
  	<div style="float: left; clear:both; width:30%;">
	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction" id="searchForm">
  		<h2>Search Loyalty Products </h2>
  		<br>Enter variantID  <s:text name="variantID" placeholder="Variant ID"></s:text>
  		<s:submit name="searchLoyaltyProduct"  value="Search Loyalty Product"/>
  	</s:form>
  	</div>
  	<div style="float: right; clear:both; width:70%;">
  	
  		<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction">
      	<h2>File to Upload for Loyalty Products
        	<s:file name="csvFileBean" size="30"/></h2>
     		 <div class="buttons">
        		<s:submit name="uploadPorducts" value="Upload"/>
      		</div>
    	</s:form>
    	<p>Columns in CSV - PRODUCT_VARIANT_ID, LOYALTY_POINTS </p>
		<br>
    	<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction">
      	<h2>File to Upload for User card numbers
        	<s:file name="badgeCsvFileBean" size="30"/></h2>
      		<div class="buttons">
        		<s:submit name="uploadBadges" value="Upload"/>
      		</div>
    	</s:form>
		<p>Columns in CSV - USER_ID, USER_CARD_NUMBER </p>
		</div>
	</div>
  </s:layout-component>
</s:layout-render>
