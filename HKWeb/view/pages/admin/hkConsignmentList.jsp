<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Consignment List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" var="consignmentAction"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        Consignment List
    </s:layout-component>

    <s:layout-component name="content">

        <fieldset class="right_label">
            <legend>Search Consignment</legend>
            <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
                <label>Consignment ID:</label><s:text name="consignment"/>
                <label>Awb Number:</label><s:text name="consignmentNumber"/>
                <label>Start Date:</label><s:text style="width:150px" class="date_input startDate"
                                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                  name="startDate"/>
                <label>End Date:</label><s:text style="width:150px" class="date_input endDate"
                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                name="endDate"/>
                <br/>
                <label>Status:</label>
                <s:select name="consignmentStatus">
                    <s:option value="-Select Status-">-Select Status-</s:option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                               serviceProperty="consignmentStatusList" value="id"
                                               label="status"/>
                </s:select>
                &nbsp;&nbsp;
                <label>Hub:</label>
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
						<c:set var="hub" value="${hk:getHubForHkdeliveryUser(consignmentAction.loggedOnUser)}" />
						 <s:hidden name="hub" value="${hub.id}"/><strong>${hub.name}</strong>&nbsp;&nbsp;
	                </shiro:lacksPermission>
                </shiro:hasPermission>
                <label>Runsheet Id:</label><s:text name="runsheet"/>
                <label>Reconciled: </label>
                <s:select name="reconciled">
                    <s:option value="">--All--</s:option>
                    <s:option value="true">Yes</s:option>
                    <s:option value="false">No</s:option>
                </s:select>
                <s:submit name="searchConsignments" value="Search Consignments"/>
            </s:form>
        </fieldset>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction">
            <s:submit name="generatePaymentReconciliation" value="Generate Payment Reconciliation"/>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Create Date</th>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Hub</th>
                    <th>Payment Mode</th>
                    <th>Runseet Id</th>
                    <th>Status</th>
                    <th>Payment<br/>Reconciliation</th>
                    <th>Actions</th>
                    <th>Select<br/>Consignments</th>
                </tr>
                </thead>
                <c:forEach items="${consignmentAction.consignmentList}" var="consignment" varStatus="ctr">
                    <tr>
                        <td>${consignment.id}</td>
                        <td><fmt:formatDate value="${consignment.createDate}" type="both" timeStyle="short"/></td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="2"/></td>
                        <td>${consignment.hub.name}</td>
                        <td>${consignment.paymentMode}</td>
                        <td>${consignment.runsheet.id}</td>
                        <td>${consignment.consignmentStatus.status}</td>
                        <td>${consignment.hkdeliveryPaymentReconciliation.id}
                            <c:if test="${consignment.hkdeliveryPaymentReconciliation.id != null}" >
                                <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction" event="editPaymentReconciliation" target="_blank">
                                    <s:param name="hkdeliveryPaymentReconciliation" value="${consignment.hkdeliveryPaymentReconciliation.id}" />
                                    (View)
                                </s:link>
                            </c:if>
                        </td>
                        <td>
                            <%--<s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" event="pre"
                                    target="_blank">View/Edit
                                <s:param name="consignment" value="${consignment.id}"/></s:link>--%>
                            <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" event="trackConsignment">
                                <s:param name="consignmentNumber" value="${consignment.awbNumber}" />
                                <s:param name="doTracking" value="true" />Track Consignment
                            </s:link>
                        </td>
                        <td>
                            <c:if test="${consignment.hkdeliveryPaymentReconciliation == null}" >
                                <s:checkbox name="consignmentListForPaymentReconciliation[]" value="${consignment.id}"
                                            class="purchaseLineItemCheckBox"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </s:form>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>

    </s:layout-component>
</s:layout-render>