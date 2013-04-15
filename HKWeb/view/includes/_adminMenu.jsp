<%@ page import="com.hk.constants.ticket.EnumTicketStatus" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_ADMINS%>">
    <div class="menuBar adminMenuBar" style="width:100%; margin:0px;">
        <ul class='lvl1'>
            <li class='lvl1 menuItem trimPadding' title="">
                <a href="${pageContext.request.contextPath}/pages/admin/adminHome.jsp">Admin Home</a>
            </li>
            <c:choose>
                <c:when test="${whAction.setWarehouse == null}">
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction">Action Q</s:link></li>
<%--
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.action.JITManagementQueueAction">JIT Action Q</s:link></li>
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.action.DropShipManagementQueueAction">Drop Action Q</s:link></li>
--%>
                </c:when>
                <c:otherwise>
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.PackingAwaitingQueueAction">Packing Queue</s:link></li>
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.ChooseOrdersForPrintPickAction">Print/Pick Orders</s:link></li>
                    <li class='lvl1 menuItem trimPadding' title="">
                        <s:link class="invert"
                                beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction">Shipment Queue</s:link></li>
                </c:otherwise>
            </c:choose>
            <li class='lvl1 menuItem trimPadding' title="">
                <s:link class="invert"
                        beanclass="com.hk.web.action.admin.order.search.SearchOrderAction">Search BO</s:link></li>
            <li class='lvl1 menuItem trimPadding' title="">
                <s:link class="invert"
                        beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction">Search SO</s:link></li>
            <li class='lvl1 menuItem trimPadding' title="">
                <a href="${pageContext.request.contextPath}/pages/admin/courier/courierAdminHome.jsp">OPS</a>
            </li>
            <li class='lvl1 menuItem trimPadding' title="">
                <a href="${pageContext.request.contextPath}/pages/admin/categoryAdminHome.jsp">Category</a>
            </li>
            <li class='lvl1 menuItem trimPadding' title="">
                <s:link class="invert" beanclass="com.hk.web.action.admin.user.SearchUserAction">Search Users</s:link></li>
            <li class='lvl1 menuItem trimPadding' title="">
                <s:link class="invert" beanclass="com.hk.web.action.report.ReportAction">Report Manager</s:link></li>

            <li class='lvl1 menuItem trimPadding' title="" style="float:right;">
                <s:link beanclass="com.hk.web.action.HomeAction">Site Home</s:link>
            </li>
            <div class="floatfix"></div>
        </ul>
    </div>
    <c:if test="${whAction.setWarehouse != null}">
        <div align="center" class="prom yellow help" style="height:16px; font-size:16px; color:red; font-weight:bold;">
            ATTENTION: Selected Warehouse -> ${whAction.setWarehouse.name}
        </div>
    </c:if>
    <div class="right">

    </div>

    <script type="text/javascript">
        //  $('li.lvl1').click(function() {
        //    url = $(this).children('a').attr('href');
        //    document.location = url;
        //    return false;
        //  });
    </script>
</shiro:hasAnyRoles>