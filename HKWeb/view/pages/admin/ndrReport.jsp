<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="NDR Report">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" var="consignmentAction"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        NDR Live Report
    </s:layout-component>

    <s:layout-component name="content">
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${consignmentAction}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${consignmentAction}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Hub</th>
                <th>AWB</th>
                <th>Hub Receiving Date</th>
                <th>Aging</th>
                <th>Non Delivery Reason</th>
                <th>Number of Attempts</th>
                <th>Pending With</th>
                <th>Current Status</th>
                <th>NDR Action</th>
                <th>Future Delivery Date</th>
                <th>Remarks</th>
                <th> Save Button !</th>
            </tr>
            </thead>
            <c:forEach items="${consignmentAction.consignmentsForCRM}" var="crmConsignment" varStatus="ctr">
                <tr>
                    <td>${crmConsignment.hub.name}</td>
                    <td>${crmConsignment.awbNumber}</td>
                    <td>${crmConsignment.createDate}</td>
                    <td>${consignmentAction.currentDate - crmConsignment.createDate}</td>
                    <td>${consignmentAction.latestRemarkForCRM[ctr]}</td>
                    <td>${consignmentAction.crmAttempts[ctr]}</td>
                    <td>${crmConsignment.owner}</td>
                    <td>${crmConsignment.consignmentStatus}</td>
                    <td>
                        <c:forEach items="${consignmentAction.consignmentTrackingListCRM[ctr]}" var="consignmentTracking">
                            <s:select name=
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </s:layout-component>
</s:layout-render>