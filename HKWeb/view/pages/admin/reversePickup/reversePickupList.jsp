<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.reversePickup.*" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction" var="revList"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Pickup List Screen">

<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.addawb').click(function () {
                var x = prompt("Enter AWB Number");
                if (!x) return false;
                $('.trackingnum').attr('value', x);

            });
        });
    </script>

</s:layout-component>

<s:layout-component name="content">
<style type="text/css">
    .heading p {
        text-align: center;
        font-size: 20px;
        color: #3379BB;
        font-weight: bolder;
        line-height: 18px;
    }

    .rline-items {
        border-collapse: collapse;
        margin: 1em auto;
    }

    .rp-bottom-border-solid {
        border-bottom: 1px solid #002737;
    }

    .rline-bottom-border-dashed {
        border-bottom: 1px dashed #6495ed;
    }

    .rline-items td {
        border-right: 1px solid #002737;
    }

    .filter label {
        font-size: 18px;
        color: #000000;
        margin-left: 50px;
    }

    .filter {
        margin-top: 2em;
    }

    .margin-top {
        margin-top: 1em;
    }

    .margin-top-2 {
        margin-top: 2em;
        text-align: center;
        margin-left: 70px;
    }

    .link-button-edit {
        color: green;;
        font-weight: bolder;
        font-size: 13px;

    }

    .link-button-cancel {
        color: red;
        font-weight: bolder;
        font-size: 13px;
        margin-top: 1em;
    }

    .courier-detail {
        width: 12%;
    }

    .courier-detail span {
        font-weight: bolder;
    }

    .design:hover {
        color: #fff !important;
        border: 0 !important;
        background: #3379bb !important;
    }

    .action-link {
        width: 10%;
    }

    .action-link span {
        margin-top: 2em;
    }

    .approve {
        font-size: 13px;
        font-weight: bolder;
        color: #ff8213;
    }

    .design {
        padding: 0;
        background-color: #008000

    }


</style>

<div class="heading" style="height: 30px;">
    <p>Reverse PickUp List</p>
</div>


<div style="margin: 0em auto; font-size:13px;">
    <div style="color:red;">${revList.errorMessage}</div>

    <div><label style="font-size:20px; font-weight: bolder;color: #3379BB; float:left "> Filter By - </label></div>
    <s:form beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction">
        <div class="filter">
            <div style="text-align: center;">
                <label> SO ID</label><s:text name="shippingOrder"/>

               <span> <label>PickUp Start Date</label>
                        <s:text class="date_input startDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/></span>

                    <span> <label>PickUp End Date</label>
                        <s:text class="date_input endDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/> </span>
            </div>
            <div class="margin-top-2">
                <label> RP Id</label><s:text name="reversePickupId"/>
              <span> <label>Courier</label>
                <s:select name="courierName">
                    <s:option value="">--ALL--</s:option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                               serviceProperty="couriersForReversePickup"
                                               value="name" label="name"/> </s:select></span>
                     <span> <label>Reverse PickUp Status</label>
                        <s:select name="reversePickupStatus">
                            <s:option value="">--ALL--</s:option>
                            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                       serviceProperty="allReversePickUpStatus"
                                                       value="id" label="status"/>
                        </s:select></span>
            <span><label>CS Action Status</label>
                             <s:select name="customerActionStatus">
                                 <s:option value="">--Select-- </s:option>
                                 <c:forEach items="<%=EnumReverseAction.getAllCustomerActionStatus()%>"
                                            var="actionTakenStatus">
                                     <s:option value="${actionTakenStatus.id}">${actionTakenStatus.name}</s:option>
                                 </c:forEach>
                             </s:select> </span>
            </div>
        </div>
        <div style="text-align: center ;margin-top: 1em;">
            <s:submit name="pre" value="Search"/>
        </div>
    </s:form>
</div>


    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${revList}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${revList}"/>


    <table class="rline-items">
        <thead>
        <tr>
            <th>S.No.</th>
            <th>RP Id.</th>
            <th>ShipOrder No.</th>
            <th>Unit No.</th>
            <th>Product</th>
            <th>Reason For Return</th>
            <th>CS Action</th>
            <th>Action On Status</th>
            <th>CS Comment</th>
            <th>CS Action Status</th>
            <th>Warehouse Condition</th>
            <th>Warehouse Comment</th>
            <th>Warehouse Remark</th>
            <th>Courier Pickup Detail</th>
            <th>Current Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <c:set value="1" var="unitNo"/>
        <c:forEach items="${revList.reversePickupOrderList}" var="reversePickup" varStatus="revCount">
            <c:set value="${fn:length(reversePickup.rpLineItems)}" var="length"/>
            <c:set value="0" var="prevLineId"/>
            <c:set value="1" var="unitNo"/>
            <c:forEach items="${reversePickup.rpLineItems}" var="rpLineitem" varStatus="ctr">
                <c:set value="${rpLineitem.lineItem.id}" var="currentLineId"/>
                <tr class="${ctr.last ? 'rp-bottom-border-solid':'rline-bottom-border-dashed'}">
                    <c:if test="${ctr.first}">
                        <td rowspan="${length}">${revCount.index + 1}</td>
                        <td rowspan="${length}">${reversePickup.reversePickupId}</td>
                        <td rowspan="${length}">${reversePickup.shippingOrder.id}</td>
                    </c:if>
                    <td>
                        <c:set value="flase" var="valueSet"/>
                        <c:if test="${prevLineId == currentLineId || ctr.first}">
                            ${unitNo}
                            <c:set value="true" var="valueSet"/>
                            <c:set value="${unitNo + 1}" var="unitNo"/>
                        </c:if>
                        <c:if test="${(prevLineId != currentLineId && !ctr.first)}">
                            <c:set value="1" var="unitNo"/>
                            <c:if test="${!valueSet}">
                                ${unitNo}
                            </c:if>
                        </c:if>
                        <c:set var="prevLineId" value="${currentLineId}"/>
                    </td>
                    <td>${rpLineitem.lineItem.sku.productVariant.product.name} ${rpLineitem.lineItem.sku.productVariant}
                            ${rpLineitem.lineItem.sku.productVariant.optionsPipeSeparated}   </td>
                    <td>${rpLineitem.customerReasonForReturn.classification.primary}</td>
                    <td>
                        <c:forEach items="<%=EnumReverseAction.getAllReversePickAction()%>"
                                   var="actionTaken">
                            <c:if test="${rpLineitem.actionTaken == actionTaken.id}">
                                ${actionTaken.name}
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach
                                items="<%=EnumReverseActionOnStatus.getAllReverseActionOnStatus()%>"
                                var="actionTakenOnStatus">
                            <c:if test="${rpLineitem.actionTakenOnStatus == actionTakenOnStatus.id}">
                                ${actionTakenOnStatus.name}
                            </c:if>
                        </c:forEach>

                    </td>
                    <td>
                            ${rpLineitem.customerComment}
                    </td>
                    <td>
                        <c:forEach
                                items="<%=EnumReverseAction.getAllCustomerActionStatus()%>"
                                var="actionStatus">
                            <c:if test="${rpLineitem.customerActionStatus == actionStatus.id}">
                                ${actionStatus.name}
                            </c:if>
                        </c:forEach>
                        <c:set value="<%=EnumReverseAction.Pending_Approval.getId()%>" var="pending"/>
                        <c:if test="${rpLineitem.customerActionStatus == pending}">
                            <s:link beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction"
                                    event="approveCSAction"><span class="approve">(Approve)</span>
                                <s:param name="rpLineitem" value="${rpLineitem.id}"/>
                                <s:param name="reversePickupOrder" value="${reversePickup.id}"/>
                            </s:link>
                        </c:if>
                    </td>
                    <td>
                            ${rpLineitem.warehouseReceivedCondition.classification.primary}
                    </td>
                    <td>
                        <c:forEach items="<%=EnumWarehouseQAStatus.values()%>"
                                   var="status">
                            <c:if test="${rpLineitem.warehouseComment == status.id}">
                                ${status.name}
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                            ${rpLineitem.warehouseRemark}
                    </td>
                    <c:if test="${ctr.first}">

                        <c:set value="<%=EnumReversePickupStatus.RPU_Picked.getId()%>" var="rpuPickedId"/>
                        <c:set value="<%=EnumReversePickupStatus.Return_QC_Checkin.getId()%>" var="returnCheckId"/>
                        <c:set value="<%=EnumCourierConstant.HealthKart_Managed.getId()%>" var="hkartCourierId"/>
                        <c:set value="<%=EnumCourierConstant.Customer_Managed.getId()%>" var="custCourierId"/>
                        <c:set value="${reversePickup.reversePickupStatus.id}" var="rpStatus"/>
                        <c:set value="${reversePickup.courierManagedBy}" var="rpCourier"/>
                        <c:set value="<%=EnumReverseAction.Approved.getId()%>" var="approved"/>
                        <%-- Courier details --%>
                        <td rowspan="${length}" class="courier-detail">

                            <s:form beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction">
                                <s:hidden name="reversePickupOrder" value="${reversePickup.id}"/>
                                Courier Name: <span>${reversePickup.courierName}</span><br>
                                <%-- mark packed link shown only on Healthkart managed courier --%>
                                <c:if test="${rpStatus < rpuPickedId}">
                                    <s:submit value="Mark Picked" name="markPicked"
                                              style="font:13px;padding:0em;background-color: #ffffff;color:#0000ff;font-weight :bolder;"
                                              class="markPicked"/><br>
                                </c:if>
                                <%-- Add and Edit AWB until RP order at any time --%>
                                <c:choose>
                                    <c:when test="${reversePickup.trackingNumber == null}">
                                        <s:hidden name="trackingNumber" class="trackingnum"/>
                                        <s:submit value="Add AWB" name="editTrackingNumber"
                                                  style="font:13px;padding:0em;background-color: #ffffff;color:#0000ff;font-weight :bolder;"
                                                  class="addawb"/>
                                        <br><br>
                                    </c:when>
                                    <c:otherwise>
                                        AWB : <span> ${reversePickup.trackingNumber}</span><br>
                                        <s:hidden name="trackingNumber" class="trackingnum"/>
                                        <s:submit value="Edit AWB" name="editTrackingNumber"
                                                  style="font:10px;padding:0em;background-color: #ffffff;color:#0000ff;font-weight :bolder;"
                                                  class="addawb"/><br>
                                    </c:otherwise>
                                </c:choose>
                                Pickup Time :<span> <fmt:formatDate
                                    value="${reversePickup.pickupTime}" type="both"
                                    timeStyle="short"/> </span>
                            </s:form>
                        </td>
                        <%-- Courier status --%>
                        <td rowspan="${length}">
                                ${reversePickup.reversePickupStatus.status}
                        </td>
                        <%-- RP Actions --%>
                        <td rowspan="${length}" class="action-link">
                            <c:set value="false" var="isRpApproved"/>
                            <c:forEach items="${reversePickup.rpLineItems}" var="rplineitem">
                                <c:if test="${rplineitem.customerActionStatus == approved}">
                                    <c:set value="true" var="isRpApproved"/>
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${(isRpApproved) || (rpStatus >= rpuPickedId) || (rpStatus >= returnCheckId)}">
                                    <s:link class="link-button-edit"
                                            beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction"
                                            event="editApprovedPickup">
                                        Edit RPU
                                        <s:param name="reversePickupOrder" value="${reversePickup.id}"/>
                                    </s:link> <br>
                                </c:when>
                                <c:otherwise>
                          <span>   <s:link class="link-button-edit"
                                           beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction"
                                           event="editReversePickup">
                              Edit RPU
                              <s:param name="reversePickupOrder" value="${reversePickup.id}"/>
                          </s:link>
                          </span> <br>
                           <span> <s:link class="link-button-cancel"
                                          beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction"
                                          event="deleteReversePickUp">
                               Delete RPU
                               <s:param name="reversePickupOrder" value="${reversePickup.id}"/>
                           </s:link>
                           </span> <br>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>


<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${revList}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${revList}"/>
</s:layout-component>
</s:layout-render>