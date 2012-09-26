<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">


    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction" var="paymentReconciliationAction"/>
    <s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        HKDelivery Reports
    </s:layout-component>
    <s:layout-component name="content">
	    <div class="reportBox">
	        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction" target="_blank">
	            <s:errors/>
	            <fieldset class="right_label">
	                <legend>Generate HKDelivery Payment Slip:</legend>
	                <ul>

	                    <li>
	                        <label>Start
	                            date</label><s:text class="date_input startDate" style="width:150px"
	                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
	                    </li>
	                    <li>
	                        <label>End
	                            date</label><s:text class="date_input endDate" style="width:150px"
	                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
	                    </li>
		                <li>
			                <label><strong>Hub:</strong></label>
			                <shiro:hasPermission name="<%=PermissionConstants.SELECT_HUB%>">
				                <s:select name="hub" class="hubName">
					                <s:option value="-Select Hub-">-Select Hub-</s:option>
					                <hk:master-data-collection service="<%=MasterDataDao.class%>"
					                                           serviceProperty="hubList" value="id"
					                                           label="name"/>
				                </s:select>
			                </shiro:hasPermission>
			                <shiro:hasPermission name="<%=PermissionConstants.VIEW_HUB%>">
				                <shiro:lacksPermission name="<%=PermissionConstants.SELECT_HUB%>" >
									<c:set var="hub" value="${hk:getHubForHkdeliveryUser(paymentReconciliationAction.loggedOnUser)}"/>
									<s:hidden name="hub" value="${hub.id}"/>${hub.name}
				                </shiro:lacksPermission>
			                </shiro:hasPermission>
		                </li>

	                    <li>
	                        <s:submit name="generatePaymentReconciliation" value="Generate Payment Reconciliation"/>
	                            <%--&nbsp;<s:submit name="generateShipmentPerformaceReport" value="Generate Shipment Performace Report"/>--%>
	                            <%-- &nbsp;<s:submit name="generateCODConfirmationReport" value="Generate CoD Confirmation Report"/>--%>

	                    </li>
	                </ul>
	            </fieldset>
	        </s:form>
	    </div>


<%--
	    <div class="reportBox">
	        <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
	            <s:errors/>
	            <fieldset class="right_label">
	                <legend>CRM Report</legend>
	                <ul>
	                    <li>
	                        <label>Start
	                            date</label><s:text class="date_input startDate" style="width:150px"
	                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
	                    </li>
	                    <li>
	                        <label>End
	                            date</label><s:text class="date_input endDate" style="width:150px"
	                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
	                    </li>

	                    <li><label>Top Level Category</label><s:select name="topLevelCategory">
	                        <option value="">All categories</option>
	                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
	                                                   value="name" label="displayName"/>
	                    </s:select></li>

	                    <li><s:submit name="generateCRMReport" value="Generate CRM Report"/>
	                    </li>
	                </ul>
	            </fieldset>
	        </s:form>
	    </div>--%>

    </s:layout-component>
</s:layout-render>