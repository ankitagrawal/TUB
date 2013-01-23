<%@ taglib prefix="shirp" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Admin Home">

<s:layout-component name="heading">Admin Home</s:layout-component>
<s:layout-component name="content">
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_MULTIPLE_WAREHOUSE%>">
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getAllWarehouses());
    %>
    <table>
        <tr>
            <td><b>Select a WH: </b></td>
            <td><s:form beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" id="selectWHForm">
                <s:select name="setWarehouse" style="height:30px;font-size:1.2em;padding:1px;">
                    <s:option value="0">-None-</s:option>
                    <c:forEach items="${whList}" var="wh">
                        <s:option value="${wh.id}">${wh.name}</s:option>
                    </c:forEach>
                </s:select>
                <s:submit class="button_orange" name="bindUserWithWarehouse" value="Save"/>
            </s:form></td>
        </tr>
    </table>
</shiro:hasAnyRoles>

<div class="left roundBox">
    <h2>Category Managers</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction"
                class="pseudoSplitBaseOrder">
            Base Order Split Analytics
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.warehouse.VariantPreferredWarehouseAction"
                class="warehouseDecider">
            Variant/SO Preferred Warehouse Decider
        </s:link>
    </h3>
</div>










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


<div class="left roundBox">
    <h2>Shipment Associated Tasks</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction">
            Shipment Resolution Screen
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">
            Update Shipment Details Screen
        </s:link>
    </h3>

</div>

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

    <h3><s:link
            beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction">Delivery Awaiting Queue</s:link></h3>

</div>

<div class="left roundBox">
    <h2>Courier Awb Actions</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.AddCourierAction">Add Courier and Courier Group</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.CourierAWBAction">Update Courier AWB numbers</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction">Change Courier Pricing details</s:link></h3>

</div>

<div class="left roundBox">
    <h2>Courier Intelligence (Futuristic)</h2>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">State Courier Service Info</s:link></h3>
    <h3><s:link beanclass="com.hk.web.action.admin.courier.CityCourierTatAction">Upload City Courier TAT</s:link></h3>
</div>










<div class="cl"></div>

<div class="left roundBox">
    <h2>Warehouse</h2>
    <c:if test="${whAction.setWarehouse != null}">
        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction">Bin Allocation</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.PackingAwaitingQueueAction">Packing Awaiting Queue</s:link></h3>

        <h3>
            <s:link
                    beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">Search Shipping Order & Checkout</s:link></h3>

        <h3>
            <s:link
                    beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">Create Update Shipment</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction">Shipment Awaiting Queue</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinRTOInventoryAction">Search Shipping Order & Checkin RTO</s:link></h3>


        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction">Drop Shipping Queue</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.ShipmentInstallationAwaitingQueueAction">Installation Awaiting Queue</s:link></h3>

        <h3>
    </c:if>

    <h3><s:link
            beanclass="com.hk.web.action.admin.inventory.CreateInventoryFileAction">Create Inventory File</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.inventory.SearchHKBatchAction">Search HK Batch</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">Create Replacement Order</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction" event="searchReplacementOrders">Search Replacement Order</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Courier and Services</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction"
                class="calculator">
            Shipment Cost Calculator Action
        </s:link>
    </h3>

    <h3><s:link beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">Update Master Pincode List</s:link></h3>




    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">Change Default Courier</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">Pincode Courier Mapping</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">Change Update Shipment</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction">Shipment Resolution</s:link></h3>


    <h3><s:link
            beanclass="com.hk.web.action.admin.shipment.ParseCourierCollectionChargeExcelAction">Upload Courier Collection Charge Excel</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.shipment.ParseEstimatedCourierExpensesExcelAction">Upload Estimated Courier Collection Charges</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">Change shipment details</s:link></h3>





    <h3><s:link beanclass="com.hk.web.action.admin.courier.AddCourierAction">Add Courier and Courier Group</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction"
                event="generateCourierReport">Download Courier Excel
        <s:param name="courierDownloadFunctionality" value="false"/>
    </s:link></h3>

    <!--<shiro:hasRole name="<%=RoleConstants.HK_DELIVERY_ADMIN%>">
		<h3>
			<s:link beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction"
					event="generateCourierReport">Add/Edit Hub
				<s:param name="courierDownloadFunctionality" value="false"/>
			</s:link>
		</h3>
	</shiro:hasRole>

-->
    <shiro:hasPermission name="<%=PermissionConstants.DISPATCH_LOT_OPERATIONS%>">
        <h3><s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction">Create New Dispatch Lot
        </s:link>
        </h3>

        <h3><s:link beanclass="com.hk.web.action.admin.courier.DispatchLotAction" event="showDispatchLotList">
            Dispatch Lot List</s:link></h3>
    </shiro:hasPermission>

</div>

<div class="cl"></div>


<div class="left roundBox">
    <h2>Inventory Management</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">Supplier List<br/> <span
                class="sml gry">(Create PO or Raise a Debit Note)</span></s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.POAction">PO List</s:link>
    </h3>
    <h3>
        <s:link beanclass="com.hk.web.action.admin.rtv.RTVAction">RTV(Return To Vendor) List</s:link>
    </h3>
    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction">GRN List <span
                class="sml gry">(Checkin against GRN)</span></s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">Purchase Invoice List</s:link>
    </h3>
    <c:if test="${whAction.setWarehouse != null}">
        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">Debit Note List</s:link></h3>

        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">Brand Audit List</s:link></h3>

        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">Reconciliation Voucher List</s:link>
        </h3>

        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction">Stock Transfer List</s:link></h3>
    </c:if>
    <h3>
        <s:link beanclass="com.hk.web.action.report.GenerateReconcilationReportAction">Generate Reconcilation Report</s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction">Search Available Batches </s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction">Low Inventory List</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction"
                event="listOutOfStock">Out of Stock List</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction">Update Variant Price
            <br/><span class="sml gry" style="color:red">(List of Variants - MRP Mismatch)</span>
        </s:link></h3>

    <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction"
                    event="downloadPrintBarcodeFile">Download Print Barcode File</s:link></h3>

        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction"
                    event="clearPrintBarcodeFile">Clear Print Barcode File</s:link></h3>
    </shiro:hasPermission>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.sku.SkuAction">Add/Edit SKUs</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.sku.SkuParseExcelAction">Upload SKU Excel</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.ForecastExcelAction">Upload Forecast Demand Excel</s:link>
    </h3>


</div>

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
                title="View Runsheets" >View/Edit Runsheets
        </s:link>
    </h3>
    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction"
                title="View Consignments" event="searchConsignments" >View/Edit Consignments
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                title="View Payments History" event="searchPaymentReconciliation" >View Payments History
        </s:link>
    </h3>


    <%--<shiro:hasPermission name="<%=EnumPermission.VIEW_CONSIGNMENT_TRACKING%>" >--%>
    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction"
                title="Track Consignment" event="trackConsignment">Track Consignment
            <s:param name="doTracking" value="false"/>
        </s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.hkDelivery.HKDPaymentReconciliationAction"
                title="View Payments History" event="hkDeliveryreports" >HKDelivery Reports
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

<script type="text/javascript">
    $(document).ready(function() {
        var max_ht = 0;
        $('.roundBox').each(function() {
            if ($(this).height() > max_ht) {
                max_ht = $(this).height();
            }

        });
        $('.roundBox').each(function() {
            $(this).height(max_ht);
        });
    });
</script>
</s:layout-component>
</s:layout-render>
