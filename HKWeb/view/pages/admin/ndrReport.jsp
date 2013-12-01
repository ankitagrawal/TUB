<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.hkDelivery.EnumNDRAction" %>
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
        <c:set var="futureDateAction" value="<%=EnumNDRAction.FutureDeliveryDate.toString()%>"/>
        <c:set var="customerSupport" value="<%=RoleConstants.CUSTOMER_SUPPORT%>"/>
        <c:set var="hubManager" value="<%=RoleConstants.HK_DELIVERY_HUB_MANAGER%>"/>

        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
            <table class="zebra_vert align_top">
                <thead>
                <tr>
                    <th>Hub</th>
                    <th>AWB</th>
                    <th>Hub Receiving Date</th>
                    <th>Aging(in Days)</th>
                    <th>Non Delivery Reason</th>
                    <th>No. of Attempts</th>
                    <th>Pending With</th>
                    <th>Current Status</th>
                    <th>NDR Action</th>
                    <th>Future Delivery Date</th>
                    <th>Remarks</th>
                    <th>Action</th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${consignmentAction.ndrDtoList}" var="ndrDto" varStatus="ctr">
                    <tr>
                        <s:hidden name="ndrDtoList[${ctr.index}].hubName" value="${ndrDto.hubName}"/>
                        <td>${ndrDto.hubName}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].awbNumber" value="${ndrDto.awbNumber}"/>
                        <td>${ndrDto.awbNumber}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].createDate" value="${ndrDto.createDate}"/>
                        <td><fmt:formatDate value="${ndrDto.createDate}" type="date" timeStyle="short"/></td>
                        <s:hidden name="ndrDtoList[${ctr.index}].aging" value="${ndrDto.aging}"/>
                        <td>${ndrDto.aging}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].nonDeliveryReason"
                                  value="${ndrDto.nonDeliveryReason}"/>
                        <td>${ndrDto.nonDeliveryReason}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].numberOfAttempts" value="${ndrDto.numberOfAttempts}"/>
                        <td>${ndrDto.numberOfAttempts}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].owner" value="${ndrDto.owner}"/>
                        <td>${ndrDto.owner}</td>
                        <s:hidden name="ndrDtoList[${ctr.index}].status" value="${ndrDto.status}"/>
                        <td>${ndrDto.status}</td>

                        <td>
                            <shiro:hasPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                <s:select class="future_date" name="ndrDtoList[${ctr.index}].ndrResolution"
                                          value="${ndrDto.ndrResolution}">
                                    <s:option value="" label="--NDR Action--"/>
                                    <s:options-enumeration enum="com.hk.constants.hkDelivery.EnumNDRAction"
                                                           label="ndrAction"/>
                                </s:select>
                            </shiro:hasPermission>
                            <shiro:lacksPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                <shiro:hasPermission name="<%=PermissionConstants.VIEW_NDR%>">
                                    ${ndrDto.ndrResolution}
                                </shiro:hasPermission>
                            </shiro:lacksPermission>
                        </td>

                        <td style="width: 90px;">
                            <div class="show_date" style="width: 135px;">
                                <shiro:hasPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                    <s:text formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" class="date_input"
                                            value="${ndrDto.futureDate}"
                                            name="ndrDtoList[${ctr.index}].futureDate"/>
                                </shiro:hasPermission>
                            </div>

                            <shiro:hasPermission name="<%=PermissionConstants.VIEW_NDR%>">
                                <fmt:formatDate value="${ndrDto.futureDate}" type="date" timeStyle="short"/>
                            </shiro:hasPermission>
                        </td>

                        <td>
                            <shiro:lacksPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                <shiro:hasPermission name="<%=PermissionConstants.VIEW_NDR%>">
                                    ${ndrDto.remarks}
                                </shiro:hasPermission>
                            </shiro:lacksPermission>
                            <shiro:hasPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                <s:text name="ndrDtoList[${ctr.index}].remarks" style="width: 150px;" maxlength="100"/>
                            </shiro:hasPermission>
                        </td>

                        <td>
                            <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" event="trackConsignment" target="_blank">
                                <s:param name="consignmentNumber" value="${ndrDto.awbNumber}" />
                                <s:param name="doTracking" value="true" />Track Consignment
                            </s:link>
                        </td>

                        <td>
                            <shiro:hasPermission name="<%=PermissionConstants.EDIT_NDR%>">
                                <s:submit name="saveNdr" value="Save NDR" class="submit"/>
                                <s:hidden name="ndrDtoList[${ctr.index}].consignmentId"
                                          value="${ndrDto.consignmentId}"/>
                                <s:hidden name="ndrDtoList[${ctr.index}].consignmentTrackingId"
                                          value="${ndrDto.consignmentTrackingId}"/>
                                <s:hidden name="ndrIndex[${ctr.index}]" class="index" value="NO"/>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                </c:forEach>

            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    $(document).ready(function () {
        var futureDate = $('.future_date');
        var showDate = $('.show_date');
        var futureDateAction = "<%=EnumNDRAction.FutureDeliveryDate.toString()%>";

        $('td').parent().find('.show_date').hide();

        function showDateForNdr(parent) {
            if (parent.find('.future_date').val().trim() === futureDateAction) {
                parent.find('.show_date').show();
            } else {
                parent.find('.show_date').hide();
            }
        }

        futureDate.change(function () {
            showDateForNdr($(this).parent().parent());
        });

        $('.submit').click(function () {
            $(this).parent().parent().find('.index').val('YES');
        });
    });
</script>