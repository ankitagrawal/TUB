<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" var="loyaltyAdminAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <script type="text/javascript">
	$(document).ready(function(e) {
		$('#addPointsRow').hide();
		$('#addPoints').click(function(e) {
			e.preventDefault();
			$('#addPointsRow').show();
		});
		
		$('.save').click(function(e) {
			var order_id = $(this).parent().parent().find('.order').text();
			var productPoints = parseFloat($(this).parent().parent().find('.points').val());
			if (productPoints > 0) {
				// do nothing
			} else {
				e.preventDefault();
				alert ("Invalid value given as points for Order Id " + order_id);
			}
		});
		
		$('.add').click(function(e) {
			var order_id = parseInt($(this).parent().find('.order').val(),10);
			var order_amount = parseFloat($(this).parent().find('.amount').val());
			if (order_id > 0 && order_amount > 0 ) {
				$('#newEntry').val(true);
			} else {
				e.preventDefault();
				alert("Please enter valid details for search parameters order Id as well as order amount.");
			}
		});
		
		$('.search').click(function(e) {
			var order_id = parseInt($('.orderID').val(),10);
			var user_id = parseInt($('.userID').val(),10);
			if (order_id > 0 || user_id > 0 ) {
				// do nothing
			} else {
				e.preventDefault();
				alert("Please enter valid details for order Id as well as user Id.");
			}
		});

	});
	</script>
  	<div style="background: #FAFAEE;">
  		<c:if test="${! empty loyaltyAdminAction.errorMessages}">
  			<div style="color: red;">Following errors were found while performing the operation :
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
  				<h2>Search Loyalty User History </h2>
  				<br>Enter User ID  <s:text name="userId" placeholder="User Id" class="userID"></s:text>
  				Enter base order ID  <s:text name="orderId" placeholder="Order Id" class="orderID"></s:text>
  				<s:submit name="searchKarmaProfile" class="search"  value="Search"/>
  			</s:form>
  			<br><br>
  		</div>
  		<div style="clear:both;">
			<p><input type="button" id="addPoints" value="Add Loyalty Points for an order"/></p>
		</div>
		<div id="addPointsRow">
		<s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction"  id="addPointsForm">
			Order Id 
			<s:text name="profileOrderID" class="order"/>&nbsp;&nbsp;&nbsp;&nbsp;
			Amount in Rs.   
			<s:text name="amount" class="amount"/>&nbsp;&nbsp;&nbsp;&nbsp;
			Points Status
			<s:select name="profileStatus" value="${profile.status}">
				<c:forEach items="${loyaltyAdminAction.statusArray}" var="statusOption">
					<s:option value="${statusOption}">${statusOption}</s:option>
				</c:forEach>
			</s:select>&nbsp;&nbsp;&nbsp;&nbsp;
			User Badge    
			<s:select name="profileBadgeId" >
						<s:options-collection collection="${loyaltyAdminAction.badges}" value="id" label="badgeName" />
					</s:select>
			<s:hidden name="newEntry" id="newEntry"/>
			<s:submit name="saveOrUpdateUserProfile" value="Add" class="add"/>
		</s:form>
		</div>
  		<div>
			<c:if test="${not empty loyaltyAdminAction.profiles}">
				<div id="productsTable" style="clear:both;">
					<table style="width:100%;">
						<thead><tr>
						<th>S No.</th>
						<th>Order ID</th>
						<th>User ID</th>
						<th>Loyalty Points</th>
						<th>Points Status</th>
						<th>Badge Name</th>
						<th> &nbsp;</th>
					</tr></thead>
				<%int count=0; %>
				<tbody><c:forEach items="${loyaltyAdminAction.profiles}" var="profile">
					<tr><s:form beanclass="com.hk.web.action.core.loyaltypg.LoyaltyAdminAction" >
					<td><%=++count %></td>
					<td class="order">${profile.order.id}</td>
					<s:hidden name="profileOrderID" value="${profile.order.id}" />
					<td>${profile.user.id}</td>
					<td><input type="text" name="points" class="points" value="${profile.karmaPoints}" /></td>
					<td><s:select name="profileStatus" value="${profile.status}">
						<c:forEach items="${loyaltyAdminAction.statusArray}" var="statusOption">
							<s:option value="${statusOption}">${statusOption}</s:option>
						</c:forEach>
					</s:select>
					</td>
					<td><s:select name="profileBadgeId" value="${profile.badge.id}">
						<s:options-collection collection="${loyaltyAdminAction.badges}" value="id" label="badgeName" />
					</s:select>
					</td>
					<td style="font-size: 16px;"><s:submit name="saveOrUpdateUserProfile" value="Save" class="save" /> 
					</td>
					</s:form></tr>
				</c:forEach></tbody>
				</table>
			</div>
		</c:if>
	</div>
		
	</div>
  </s:layout-component>
</s:layout-render>
