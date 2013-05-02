<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.hkDelivery.EnumNDRAction" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
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

        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
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
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${consignmentAction.ndrDtoList}" var="ndrDto">
                    <tr>
                        <td>${ndrDto.hubName}}</td>
                        <td>${ndrDto.awbNumber}</td>
                        <td>${ndrDto.createDate}</td>
                        <td>${ndrDto.aging}</td>
                        <td>${ndrDto.nonDeliveryReason}</td>
                        <td>${ndrDto.numberOfAttempts}</td>
                        <td>${ndrDto.owner}</td>
                        <td>${ndrDto.status}</td>
                        <td>
                            <shiro:hasRole name="<%=RoleConstants.CUSTOMER_SUPPORT%>">
                                <s:select name="${ndrDto.ndrResolution}">
                                    <s:option value="" label="--NDR Action--"/>
                                    <s:options-enumeration enum="com.hk.constants.hkDelivery.EnumNDRAction"
                                                           label="ndrAction"/>
                                </s:select>
                            </shiro:hasRole>
                            <shiro:hasRole name="<%=RoleConstants.HK_DELIVERY_HUB_MANAGER%>">
                                ${ndrDto.ndrResolution}
                            </shiro:hasRole>
                        </td>
                        <td>
                            <c:set var="futureDateAction" value="<%=EnumNDRAction.FutureDeliveryDate.getNdrAction()%>"/>
                            <c:if test="${ndrDto.ndrResolution eq futureDateAction}">
                                <s:text style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                        name="ndrDto.futureDate"/>
                            </c:if>
                        </td>
                        <td>
                            <s:text name="ndrDto.remarks"/>
                        </td>
                        <td>
                            <shiro:hasRole name="<%=RoleConstants.CUSTOMER_SUPPORT%>">
                                <s:submit name="saveNdr" value="Save NDR"/>
                            </shiro:hasRole>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>