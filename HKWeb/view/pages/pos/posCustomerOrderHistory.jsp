<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="HealthKart.com Store : Customer History">
<s:useActionBean beanclass="com.hk.web.action.admin.pos.POSAction" var="pos"/>
<%
    UserService userService = ServiceLocatorFactory.getService(UserService.class);
    pageContext.setAttribute("warehouse", userService.getWarehouseForLoggedInUser());
%>
<s:layout-component name="htmlHead">
	<style type="text/css">
		table tr th {
			text-align: left;
		}
		
		table th {
		background-color: #6D7B8D;
    	color: white;
		}
		table {
		margin-left: auto;
		margin-right: auto;
		}
		.apply-border td {
			border: 1px solid #DDD;
		}
	</style>
</s:layout-component>
<s:layout-component name="content">
<body>
<div style ="text-align:center; font-size: 28px;" >Customer History</div>

 <c:choose>
      <c:when test="${!empty pos.customerKarmaList}">
  
<div id="historyTable" style="">
	<table>
		<tr>
			<th>S No.</th>
			<th>Order Id</th>
			<th style="width:200px;">Order Details</th>
			<th>Order Date</th>
			<th>Total Amount</th>
			<th>Customer badge</th>
			<th>Loyalty Points</th>
			<th style="width:180px;">Points Status</th>
		</tr>
		<%int count=0; %>
		<c:forEach items="${pos.customerKarmaList}" var="karmaProfile">
		<tr>
		<td><%=++count %></td>
		<td>${karmaProfile.order.id}</td>
		<td>
			<c:forEach items="${karmaProfile.order.cartLineItems}" var="items">
   					${items.productVariant.product.name} 
            </c:forEach>
		</td>
		<td><fmt:formatDate value="${karmaProfile.creationTime}" /></td>
		<td>${hk:roundNumberForDisplay(karmaProfile.order.payment.amount)}</td>
		<td>
			<c:if test="${karmaProfile.badge != null }">
			${karmaProfile.badge.badgeName}
			</c:if>
		</td>
		<td>
		<c:choose>
              <c:when test="${karmaProfile.karmaPoints >= 0.0}">
                     ${hk:roundNumberForDisplay(karmaProfile.karmaPoints)}
              </c:when>
              <c:otherwise>
                   ${hk:roundNumberForDisplay(0-karmaProfile.karmaPoints)}
              </c:otherwise>
        </c:choose>
        </td>
		<td>${karmaProfile.statusForHistory}</td>
		</tr>
		</c:forEach>
	</table>
	</c:when>
	<c:otherwise>
	      You haven't ordered anything from healthkart yet as a loyalty member. 
	</c:otherwise>
</c:choose>
	
</div>
</body>
</s:layout-component>
</s:layout-render>
</html>