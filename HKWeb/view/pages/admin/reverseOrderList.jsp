<%@ page import="com.hk.constants.inventory.EnumReconciliationStatus" %>
<%@ page import="com.hk.constants.courier.EnumPickupStatus" %>
<%@ page import="com.hk.constants.courier.AdviceProposedConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" var="pickupManage"/>
<%
    pageContext.setAttribute("pickupStatusList", EnumPickupStatus.getPickupStatusList());
%>
<c:set var="reconDone" value="<%=EnumReconciliationStatus.DONE.getId()%>"/>
<c:set var="reconPending" value="<%=EnumReconciliationStatus.PENDING.getId()%>"/>
<c:set var="pickupOpen" value="<%=EnumPickupStatus.OPEN.getId()%>"/>
<c:set var="adviceList" value="<%=AdviceProposedConstants.getAdviceList()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Order List">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            
         $(document).ready(function() {
            $('.markPicked').click(function() {
                if(!confirm("Are you sure you want to mark it picked ?")){
                    return false;
                }

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

            $('.markReconciled').click(function() {

                if($('.markPicked').is(':visible')  || $('.markReceived').is(':visible')){
                    alert("Please mark the order picked/ received first");
                    return false;
                }
                
                if(!confirm("Are you sure you want to mark it reconciled ?")){
                   return false;
                }
                 var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

             $('.markReceived').click(function() {

                 if($('.markPicked').is(':visible')){
                    alert("Please mark the order picked first");
                    return false;
                 }

                if(!confirm("Are you sure you want to mark it received ?")){
                   return false;
                }
                  var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

             $('.track').click(function(){
               var x = prompt("Enter Tracking no");
               if(!x) return false;

               $('.trackingNo').attr('value',x);
             });

              $('.confNo').click(function(){
               var x = prompt("Enter Pickup Confirmation no");
               if(!x) return false;

               $('.confirmationNo').attr('value',x);
             });

              $('.adviceButton').click(function(){
                 if($('.markPicked').is(':visible')  || $('.markReceived').is(':visible')){
                    alert("Please mark the order picked/ received first");
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
                </br>
                <label>SO Gateway Order Id:</label><s:text name="shippingOrderId" value="${pickupManage.shippingOrderId}" style="width:150px"/>
                &nbsp;
                <label>Pickup Status:</label>
                <s:select name="pickupStatusId">
                    <s:option value="" selected="true">-ALL-</s:option>
                    <c:forEach items="${pickupStatusList}" var="pickupStatus">
                        <s:option value="${pickupStatus.id}">${pickupStatus.name}</s:option>
                    </c:forEach>
                </s:select>
                &nbsp;
                <label>Reconciliation Status:</label>
                <s:select name="reconciliationStatusId">
			        <s:option value="" selected="true">-ALL-</s:option>
			        <s:option value="${reconDone}">Yes</s:option>
			        <s:option value="${reconPending}">No</s:option>
	            </s:select>
                &nbsp;
                <label>Courier:</label>
                <s:select name="courierId" class="courierService">
                  <s:option value="">All Couriers</s:option>
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id"
                                                        label="name"/>
                </s:select>


                <s:submit name="pre" value="Search"/>
                <br/>
                <s:submit name="searchUnscheduled" value="Search Unscheduled cases" class="format-button"  />
                <s:submit name="generateExcelForReversePickup" value="Download to Excel" class="format-button"/>
            </fieldset>
        </s:form>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupManage}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupManage}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Reverse Order Id</th>
                <th>SO Gateway Id</th>
                <th>Reason for return</th>
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
                    <td>${reverseOrderRequest.returnReason}</td>
                    <td>${reverseOrderRequest.courierPickupDetail.courier.name}</td>
                    <s:form beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction">
                           <s:hidden name="orderRequestId" value="${reverseOrderRequest.id}"/>
                           <s:hidden name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                    <td>
                           <c:choose>
                            <c:when test="${reverseOrderRequest.courierPickupDetail != null && reverseOrderRequest.courierPickupDetail.pickupConfirmationNo == null}">
                                <s:hidden name="confirmationNo" id="confirmationNo" class="confirmationNo" />
                                <s:submit value="Add" name="editConfirmationNo" class="confNo design" />
                            </c:when>
                            <c:otherwise>
                                ${reverseOrderRequest.courierPickupDetail.pickupConfirmationNo}
                            </c:otherwise>
                           </c:choose>
                    </td>
                    <td>
                           <c:choose>
                            <c:when test="${reverseOrderRequest.courierPickupDetail != null && reverseOrderRequest.courierPickupDetail.trackingNo == null}">
                                <s:hidden name="trackingNo" id="trackingNo" class="trackingNo" />
                                <s:submit value="Add" name="editTrack" class="track design" />
                            </c:when>
                            <c:otherwise>
                                ${reverseOrderRequest.courierPickupDetail.trackingNo}
                            </c:otherwise>
                           </c:choose>
                    </td>
                    <td>${reverseOrderRequest.courierPickupDetail.pickupDate}</td>
                    <td>${reverseOrderRequest.courierPickupDetail.pickupStatus.name}</td>
                    <td>${reverseOrderRequest.receivedDate}</td>
                    <td>${reverseOrderRequest.reconciliationStatus.name}</td>
                    <td>${reverseOrderRequest.user.name}</td>

                    <td>
                        <c:if test="${reverseOrderRequest.courierPickupDetail.pickupStatus.id == pickupOpen}">
                            <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="markPicked" class="markPicked">Mark Picked
                                <s:param name="orderRequestId" value="${reverseOrderRequest.id}" />
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
                         <s:link beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction" event="reschedulePickup" target="_blank">Reschedule Pickup
			                    <s:param name="orderRequestId" value="${reverseOrderRequest.id}"/>
                                <s:param name="shippingOrderId" value="${pickupManage.shippingOrderId}"/>
                         </s:link>
                        </c:if>                      

                    </td>
                    <td>
                            <s:select name="advice" value="${reverseOrderRequest.actionProposed}">
                                <s:option value="">-Select-</s:option>
                                <c:forEach items="${adviceList}" var="advice">
                                    <s:option value="${advice}">${advice}</s:option>
                                </c:forEach>
                            </s:select>                                
                            <s:submit name="adviceProposed" value="save" class="format-button adviceButton"/>
                        </s:form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupManage}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupManage}"/>

                <style type="text/css">
                    .format-button{
                        padding: 2px !important;
                        font-weight: normal !important;
                        font-size: 1.1em !important;
                    }
                    .design {
                        color: #889 !important;
                        border: 0 !important;
                        background: none !important;
                        border-radius: 0 !important;
                        font-size: 11px !important;
                        font-weight: normal !important;
                        width: 30px !important;
                    }
                    .design:hover {
                        color: #fff !important;
                        border: 0 !important;
                        background: #3379bb !important;
                    }

                </style>
    </s:layout-component>
</s:layout-render>