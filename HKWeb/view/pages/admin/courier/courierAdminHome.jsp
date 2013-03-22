<%@ taglib prefix="shirp" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Operations Home">

<s:layout-component name="heading">Operations</s:layout-component>
<s:layout-component name="content">

<div class="left roundBox">
    <h2>Pincode / Couriers</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
            Pincode Crud Operations
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
            Pincode Courier Mapping Screen
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
            Pincode Default Courier
        </s:link>
    </h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Shipment Associated Tasks</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction">
            Shipment Resolution Screen
        </s:link>
    </h3>

    <c:if test="${whAction.setWarehouse != null}">
        <h3>
            <s:link beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">
                Update Shipment Details Screen
            </s:link>
        </h3>
    </c:if>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Delivery Mark Related Tasks</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">
            Pincode Default Courier
        </s:link>
    </h3>

    <h3><s:link beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
        Delivery Status Update Cron Task
    </s:link>
    </h3>

    <h3><s:link beanclass="com.hk.web.action.admin.shipment.ParseCourierDeliveryStatusExcelAction">
        Update Delivery Status using Excel
    </s:link></h3>

    <c:if test="${whAction.setWarehouse != null}">
        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction">Delivery Awaiting Queue</s:link></h3>
    </c:if>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Courier Awb Actions</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.AddCourierAction">Add Courier and Courier Group</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.CourierAWBAction">Update Courier AWB numbers</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction">Change Courier Pricing details</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Courier Intelligence (Futuristic)</h2>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">State Courier Service Info</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.CityCourierTatAction">Upload City Courier TAT</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Courier Shipment Analytics</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction"
                class="calculator">
            Shipment Cost Calculator Action
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.warehouse.VariantPreferredWarehouseAction"
                class="warehouseDecider">
            Variant/SO Preferred Warehouse Decider
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction"
                class="pseudoSplitBaseOrder">
            Base Order Split Analytics
        </s:link>
    </h3>
</div>

<div class="cl"></div>


<div class="left roundBox">
    <h2>Shipment Estimated/Actual Cost Upload/Download</h2>

    <h3><s:link
            beanclass="com.hk.web.action.admin.shipment.ParseCourierCollectionChargeExcelAction">Upload Courier Collection Charge Excel</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.shipment.ParseEstimatedCourierExpensesExcelAction">Upload Estimated Courier Collection Charges</s:link></h3>

</div>

<div class="cl"></div>


<div class="left roundBox">
    <h2>Courier Shipment Reporting</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction"
                event="generateCourierReport">Download Courier Excel
        <s:param name="courierDownloadFunctionality" value="false"/>
    </s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Healthkart Delivery</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction"
                title="Download Healthkart Delivery Worksheet">Mark Shipments Inward at Hub</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction"
                title="Download Healthkart Delivery Runsheet" event="previewRunsheet">Download Delivery Runsheet
            <s:param name="runsheetPreview" value="false"/>
        </s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction"
                title="View Runsheets">View/Edit Runsheets
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction"
                title="View Consignments" event="searchConsignments">View/Edit Consignments
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                title="View Payments History" event="searchPaymentReconciliation">View Payments History
        </s:link>
    </h3>


    <shiro:hasPermission name="<%=PermissionConstants.DISPATCH_LOT_OPERATIONS%>">
        <h3><s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction">Create New Dispatch Lot
        </s:link>
        </h3>

        <h3><s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction" event="showDispatchLotList">
            Dispatch Lot List</s:link></h3>
    </shiro:hasPermission>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction"
                title="Track Consignment" event="trackConsignment">Track Consignment
            <s:param name="doTracking" value="false"/>
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                title="View Payments History" event="hkDeliveryreports">HKDelivery Reports
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction"
                title="Add/Edit Hub">Add/Edit Hub
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction" event="addUserToHub"
                title="Add agent">Add/Remove agent to hub
        </s:link>
    </h3>


</div>
</s:layout-component>
</s:layout-render>
