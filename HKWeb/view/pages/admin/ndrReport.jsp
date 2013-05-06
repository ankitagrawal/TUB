<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.hkDelivery.EnumNDRAction" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" %>
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
                    <th>Number of Attempts</th>
                    <th>Pending With</th>
                    <th>Current Status</th>
                    <th>NDR Action</th>
                    <th>Future Delivery Date</th>
                    <th>Remarks</th>
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
                        <td>${ndrDto.createDate}</td>
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
                            <s:select class="future_date" name="ndrDtoList[${ctr.index}].ndrResolution"
                                      value="${ndrDto.ndrResolution}"
                                      disabled="${ndrDto.owner == customerSupport ? 'false' : 'true'}">
                                <s:option value="" label="--NDR Action--"/>
                                <s:options-enumeration enum="com.hk.constants.hkDelivery.EnumNDRAction"
                                                       label="ndrAction"/>
                            </s:select>
                        </td>

                        <td>
                            <div class="show_date" style="width: 135px;">
                                <s:text formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" class="date_input" value="${ndrDto.futureDate}"
                                        disabled="${ndrDto.owner == customerSupport ? 'false' : 'true'}"
                                        name="ndrDtoList[${ctr.index}].futureDate"/>
                            </div>
                            <c:if test="${ndrDto.owner == hubManager}">
                                ${ndrDto.futureDate}
                            </c:if>
                        </td>

                        <td>
                            <s:text name="ndrDtoList[${ctr.index}].remarks"  style="width: 150px;"
                                    disabled="${ndrDto.owner == customerSupport ? 'false' : 'true'}"/>
                        </td>

                        <td>
                            <shiro:hasRole name="<%=RoleConstants.CUSTOMER_SUPPORT%>">
                                <s:submit name="saveNdr" value="Save NDR"/>
                                <s:hidden name="ndrDtoList[${ctr.index}].consignmentid"
                                          value="${ndrDto.consignmentId}"/>
                                <s:hidden name="ndrDtoList[${ctr.index}].consignmentTrackingId"
                                          value="${ndrDto.consignmentTrackingId}"/>
                            </shiro:hasRole>
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
            }
        }

        futureDate.change(function () {
            showDateForNdr($(this).parent().parent());
        });
    });
</script>