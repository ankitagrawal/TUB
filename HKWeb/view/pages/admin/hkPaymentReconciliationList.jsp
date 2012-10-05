<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Reconciliation List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction" var="consignmentAction"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        HKDelivery Payment Reconciliation List
    </s:layout-component>

    <s:layout-component name="content">

        <fieldset class="right_label">
            <legend>Search Payment Reconciliation</legend>
            <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction">
                <label>Start Date:</label><s:text style="width:150px" class="date_input startDate"
                                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                  name="startDate"/>
                <label>End Date:</label><s:text style="width:150px" class="date_input endDate"
                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                name="endDate"/>
                <s:submit name="searchPaymentReconciliation" value="Search Payment Reconciliation"/>
            </s:form>
        </fieldset>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>SL No.</th>
                    <th>Create Date</th>
                    <th>Expected Amount</th>
                    <th>Actual Amount Collected</th>
                    <th>Reconciliation Done By </th>
                    <th>Remarks</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <c:forEach items="${consignmentAction.paymentReconciliationList}" var="paymentReconciliation" varStatus="ctr">
                    <tr>
                        <td>${ctr.count}</td>
                        <td><fmt:formatDate value="${paymentReconciliation.createDate}" type="both" timeStyle="short"/></td>
                        <td>${paymentReconciliation.expectedAmount}</td>
                        <td>${paymentReconciliation.actualAmount}</td>
                        <td>${paymentReconciliation.user.name}</td>
                        <td>${paymentReconciliation.remarks}</td>
                        <td>
                            <c:if test="${paymentReconciliation.id != null}">
                                <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                                        event="editPaymentReconciliation" target="_blank">
                                    <s:param name="hkdeliveryPaymentReconciliation"
                                             value="${paymentReconciliation.id}"/>
                                    (View)
                                </s:link>
                                <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                                        event="downloadPaymentReconciliation">
                                    <s:param name="hkdeliveryPaymentReconciliation"
                                             value="${paymentReconciliation.id}"/>
                                    (Download)
                                </s:link>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>

    </s:layout-component>
</s:layout-render>