<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
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
                <label>Hub:</label>
                <s:select name="hub" class="hubName">
                    <s:option value="-Select Hub-">-Select Hub-</s:option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                               serviceProperty="hubList" value="id"
                                               label="name"/>
                </s:select>
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
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
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
                                              maxFractionDigits="0"/></td>
                        <td>${consignment.hub.name}</td>
                        <td>${consignment.paymentMode}</td>
                        <td>${consignment.runsheet.id}</td>
                        <td>${consignment.consignmentStatus.status}</td>
                        <td>${consignment.hkdeliveryPaymentReconciliation.id}</td>
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
                            <s:checkbox name="consignmentListForPaymentReconciliation[]" value="${consignment.id}"
                                        class="purchaseLineItemCheckBox"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </s:form>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>

    </s:layout-component>
</s:layout-render>