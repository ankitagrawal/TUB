<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.inventory.EnumReconciliationType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
 <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create Reconciliation Voucher">
	<%
		int subtractRecontype = EnumReconciliationType.Subtract.getId().intValue();
		pageContext.setAttribute("subtractRecontype", subtractRecontype);
	%>
<jsp:useBean id="now" class="java.util.Date" scope="request" />
	<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
<s:layout-component name="content">
	    <h2>Create Reconciliation Voucher</h2>
	<s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
		<s:hidden name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
		<input type="hidden" name="reconciliationVoucher.reconciliationType" value="20"/>
		<table>
			<tr>
				<td>Reconciliation Date</td>
				<td>
					<s:text class="date_input" formatPattern="yyyy-MM-dd"
					        name="reconciliationVoucher.reconciliationDate"
					        value="${pa.reconciliationVoucher.reconciliationDate != null ? pa.reconciliationVoucher.reconciliationDate : now}"/>
				</td>
			</tr>
			<tr>
				<td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
				<td><s:textarea name="reconciliationVoucher.remarks" style="height:50px;"
				                value="${pa.reconciliationVoucher.remarks}"/></td>
			</tr>
			
			<tr>
				<td>For Warehouse</td>
				<td>
					<s:hidden name="reconciliationVoucher.warehouse" value="${whAction.setWarehouse.id}"/>
						${whAction.setWarehouse.city}
				</td>
			</tr>
		</table>
		<s:submit name="create" value="Continue"/>
	</s:form>
</s:layout-component>
</s:layout-render>

