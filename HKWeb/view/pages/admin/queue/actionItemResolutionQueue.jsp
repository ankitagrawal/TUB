<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.impl.service.queue.BucketService" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" var="actionItemQueueBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Action Item Resolution Queue">
    <s:layout-component name="htmlHead">
        <%
            ShippingOrderStatusService shippingOrderStatusService = ServiceLocatorFactory.getService(ShippingOrderStatusService.class);
            BucketService bucketService = ServiceLocatorFactory.getService(BucketService.class);
            pageContext.setAttribute("applicableOrderStatusList", shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForProcessingQueue()));
            pageContext.setAttribute("bucketList", bucketService.listAll());
        %>

        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
        <style type="text/css">
            .fieldLabel {
                font-size: .8em;
                color: #999999;
                padding-top: 2px;
            }

            table.align_top tr td {
                vertical-align: top;

            }

            table tr.row_border {
                outline: 1px solid #cccccc;
            }

            .row_shipped td {
                background-color: #b8ffcf;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="heading">Action Item Resolution Queue</s:layout-component>
    <s:layout-component name="content">

        <fieldset class="top_label">
            <ul>
                <div class="grouped grid_12">
                    <s:form beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" method="get"
                            autocomplete="false">
                        <div style="width:1100px; margin:10px;">
                            <label>SO Order ID </label> <s:text name="shippingOrderId"/>
                            <label>Buckets </label>
                            <s:select name="buckets">
                                <option value="">Any Bucket</option>
                                <c:forEach items="${bucketList}" var="bucket">
                                    <s:option value="${bucket.id}">${bucket.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </div>

                        <div style="width:1120px; margin:20px;">
                            <label>Push Start
                                Date </label><s:text class="date_input startDate" style="width:150px"
                                                     formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                     name="startPushDate"/>
                            <label>Push End
                                Date </label><s:text class="date_input endDate" style="width:150px"
                                                     formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                     name="endPushDate"/>
                        </div>

                        <s:submit style="margin:0 0 10px 25px;" name="pre" value="Search"/>
                    </s:form>
                </div>
            </ul>
        </fieldset>

        <s:form beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" autocomplete="off">


            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionItemQueueBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionItemQueueBean}"/>
            <div class="clear"></div>
            <div class="clear"></div>
            <table class="align_top" width="100%" cellspacing="1">
                <thead>
                <tr>
                    <th style="text-align:center">Action Item Details</th>
                    <th style="text-align:center">Shipping Order Details</th>
                </tr>
                </thead>
                <c:forEach items="${actionItemQueueBean.actionItems}" var="actionItem" varStatus="ctr">
                    <tr class="${ctr.index % 2 == 0 ? '' : 'alt'} addressRow orderRow">
                        <td width="40%" style="border:1px solid darkgoldenrod; padding:3px;">
                        </td>
                        <td width="60%" style="border:1px solid darkgreen; padding:3px;">
                            ${actionItem.shippingOrder.id}
                            <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp" shippingOrder = "${actionItem.shippingOrder}"
                                              isActionQueue="true"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div id="hiddenShippingIds"></div>

            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionItemQueueBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionItemQueueBean}"/>
            <div style="display:inline;float:left;">
                    <%--<s:submit name="moveToActionAwaiting" class="shippingOrderActionBtn" value="Move Back to Action Awaiting"/>--%>
            </div>
        </s:form>
        <script type="text/javascript">

            $('.shippingOrderActionBtn').click(function () {
                var index = 0;
                $('.shippingOrderDetailCheckbox').each(function () {
                    var shippingOrderDetailCheckbox = $(this);
                    var isChecked = shippingOrderDetailCheckbox.attr('checked');
                    if (isChecked) {
                        var reasonId = '.shippingOrderReason_' + $(this).attr('dataId');
                        var reason = $(reasonId);
                        $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[' + index + ']" value="' + $(this).attr('dataId') + '"/>');
                        $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[' + index + '].reason" value="' + reason.val() + '"/>');
                        index++;
                    }
                });
                return true;
            });
        </script>

    </s:layout-component>
</s:layout-render>
