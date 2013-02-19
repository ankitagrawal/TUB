<%@ page import="com.hk.constants.inventory.EnumReconciliationStatus" %>
<%@ page import="com.hk.constants.courier.EnumPickupStatus" %>
<%@ page import="com.hk.constants.courier.EnumAdviceProposed" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" var="pickupManage"/>
<%
    pageContext.setAttribute("pickupStatusList", EnumPickupStatus.getPickupStatusList());
%>
<c:set var="reconDone" value="<%=EnumReconciliationStatus.DONE.getId()%>"/>
<c:set var="reconPending" value="<%=EnumReconciliationStatus.PENDING.getId()%>"/>
<c:set var="pickupOpen" value="<%=EnumPickupStatus.OPEN.getId()%>"/>
<c:set var="adviceList" value="<%=EnumAdviceProposed.getAdviceList()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Order List">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            
         $(document).ready(function() {
            $('.markPicked').click(function() {
                if(!confirm("Are you sure you want to mark it picked ?")){
                    return false;
                }
            });

            $('.markReconciled').click(function() {
                if(!confirm("Are you sure you want to mark it reconciled ?")){
                   return false;
                }
            });

             $('.markReceived').click(function() {
                if(!confirm("Are you sure you want to mark it received ?")){
                   return false;
                }
            });
         });

        </script>
    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction">

            <fieldset>
                <legend>Search Reverse Pickup List</legend>

                <label>SO Gateway Order Id:</label><s:text name="shippingOrderId" value="${pickupManage.shippingOrderId}" style="width:150px"/>
                &nbsp; &nbsp;
                <label>Pickup Status:</label>
                <s:select name="pickupStatusId">
                    <s:option value="" selected="true">-ALL-</s:option>
                    <c:forEach items="${pickupStatusList}" var="pickupStatus">
                        <s:option value="${pickupStatus.id}">${pickupStatus.name}</s:option>
                    </c:forEach>
                </s:select>

                <label>Reconciliation Status:</label>
                <s:select name="reconciliationStatusId">
			        <s:option value="" selected="true">-ALL-</s:option>
			        <s:option value="${reconDone}">Yes</s:option>
			        <s:option value="${reconPending}">No</s:option>
	            </s:select>

                <s:submit name="pre" value="Search"/>
                <%--<s:submit name="generateExcelReport" value="Download to Excel" />--%>
            </fieldset>
        </s:form>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupManage}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupManage}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Reverse Order Id</th>
                <th>SO Gateway Id</th>
                <th>Courier</th>
                <th>Pickup Conf No.</th>
                <th>Tracking No.</th>
                <th>Pickup Date</th>
                <th>Pickup Status</th>
                <th>Received Date</th>
                <th>Reconciled</th>
                <th>User</th>
                <th>Actions</th>
                <th>Advice</th>
            </tr>
            </thead>
            <c:forEach items="${pickupManage.orderRequestsList}" var="reverseOrderRequest" varStatus="ctr">
                <tr>
                    <td>
                        ${reverseOrderRequest.id} </br>
                         (<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="reverseOrderInvoice" target="_blank">
                            <s:param name="reverseOrder" value="${reverseOrderRequest}"/>
                            <s:param name="shippingOrder" value="${reverseOrderRequest.shippingOrder}"/>
                            Accounting Invoice
                          </s:link>)
                    </td>
                    <td>
                        <s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder" target="_blank">
                            <s:param name="shippingOrderGatewayId" value="${reverseOrderRequest.shippingOrder.gatewayOrderId}"/>
                            ${reverseOrderRequest.shippingOrder.gatewayOrderId}
                        </s:link>
                    </td>
                    <td>${reverseOrderRequest.courierPickupDetail.courier.name}</td>
                    <td>${reverseOrderRequest.courierPickupDetail.pickupConfirmationNo}</td>                     
                    <td>${reverseOrderRequest.courierPickupDetail.trackingNo}</td>
                    <td>${reverseOrderRequest.courierPickupDetail.pickupDate}</td>
                    <td>${reverseOrderRequest.courierPickupDetail.pickupStatus.name}</td>
                    <td>${reverseOrderRequest.receivedDate}</td>
                    <td>${reverseOrderRequest.reconciliationStatus.name}</td>
                    <td>${reverseOrderRequest.user.name}</td>

                    <td>
                        <c:if test="${reverseOrderRequest.courierPickupDetail.pickupStatus.id == pickupOpen}">
                            <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="markPicked" class="markPicked">Mark Picked
                                <s:param name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:param name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                            </s:link>
                        </c:if>

                        <c:if test="${reverseOrderRequest.receivedDate == null}">
		                    <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="markReceived" class="markReceived">Mark Received
			                    <s:param name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:param name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                            </s:link>
		                    <br/>
                        </c:if>

                        <c:if test="${reverseOrderRequest.reconciliationStatus.id == reconPending}">
		                    <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="markReconciled" class="markReconciled">Mark Reconciled
			                    <s:param name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:param name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                            </s:link>
		                    <br/>
                        </c:if>
                        <c:if test="${reverseOrderRequest.courierPickupDetail.pickupStatus.id == pickupOpen || reverseOrderRequest.courierPickupDetail == null}">
                         <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="reschedulePickup">Reschedule Pickup
			                    <s:param name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:param name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                         </s:link>
                        </c:if>

                    </td>
                    <td>
                        <c:set var="activeName" value="${reverseOrderRequest.actionProposed}" />
                        <s:form beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction">
                            <s:select name="adviceId"><%-- value="<%=EnumAdviceProposed.getAdviceProposedByName('')%>">--%>
                                <s:option value="">-ALL-</s:option>
                                <c:forEach items="${adviceList}" var="advice">
                                    <s:option value="${advice.id}">${advice.name}</s:option>
                                </c:forEach>
                            </s:select>
                            <%--<script type="text/javascript">--%>
                                <%--$('#advice${ctr.index}').val(${});--%>
                            <%--</script>--%>
                                <s:hidden name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:hidden name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                            <s:submit name="adviceProposed" value="save" style="" />
                        </s:form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupManage}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupManage}"/>


    </s:layout-component>
</s:layout-render>