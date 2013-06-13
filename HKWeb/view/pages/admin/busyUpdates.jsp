<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Populate Busy data">
	<s:useActionBean beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" var="busyAction"/>
	<s:layout-component name="content">
		<s:layout-component name="heading">
			Populate Busy
		</s:layout-component>
	 <ul>
		 <li><s:link beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" event="populateSales">Populate Retail Sales from Aqua</s:link> </li>
		 <li><s:link beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" event="populateServiceSales">Populate Service Sales from Aqua</s:link> </li>
		 <li><s:link beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" event="populateB2BSales">Populate B2B Sales</s:link> </li>
		 <li><s:link beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" event="populateRto">Populate Sales return from Aqua</s:link> </li>
		 <%--<li><s:link beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction" event="populatePurchases">Populate Purchases</s:link> </li>--%>
	 </ul>
	</s:layout-component>
</s:layout-render>
