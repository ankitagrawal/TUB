<%@ taglib prefix="shirp" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.core.EnumRole" %>
<%@ page import="com.hk.pact.service.store.StoreService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Admin Home">

<s:layout-component name="heading">Admin Home</s:layout-component>
<s:layout-component name="content">
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_MULTIPLE_WAREHOUSE%>">
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
    %>
    <table>
        <s:form beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" id="selectWHForm">
            <tr>
                <td>
                    get users from product Ids
                </td>
                <td>
                    <s:textarea name="params"></s:textarea>
                </td>
                <td>
                    get projected info: 1 for projected info 0 for entire users
                </td>
                <td>
                    <s:text name="minimum"></s:text>
                </td>

            </tr>
            <tr>
                <td>
                    get users from product variant Ids
                </td>
                <td>
                    <s:textarea name="pvs"></s:textarea>
                </td>
            </tr>
            <tr>
                <td>
                    cities
                </td>
                <td>
                    <s:textarea name="cities"></s:textarea>
                </td>
            </tr>
            <tr>
                <td>
                    zones
                </td>
                <td>
                    <s:textarea name="zones"></s:textarea>
                </td>
            </tr>
            <tr>
                <td>
                    states
                </td>
                <td>
                    <s:textarea name="states"></s:textarea>
                </td>
            </tr>
            <tr>
                <td>
                    categories
                </td>
                <td>
                    <s:textarea name="categories"></s:textarea>
                </td>
            </tr>
            <tr>
                <td>
                    <s:submit name="search"></s:submit>
                </td>
            </tr>
            <tr>
                <td>
                    number of results ${whAction.result}
                </td>
                <td>
                    time taken (ms) ${whAction.time}
                </td>
            </tr>
        </s:form>

        <tr>
            <td><b>Select a WH: </b></td>
            <td><s:form beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" id="selectWHForm">


                <s:select name="setWarehouse" style="height:30px;font-size:1.2em;padding:1px;">
                    <s:option value="0">-None-</s:option>
                    <c:forEach items="${whList}" var="wh">
                        <s:option value="${wh.id}">${wh.identifier}</s:option>
                    </c:forEach>
                </s:select>
                <s:submit class="button_orange" name="bindUserWithWarehouse" value="Save"/>
            </s:form></td>
            <td>
                <shiro:hasRole name="<%=RoleConstants.GOD%>">
                    <s:link beanclass="com.hk.web.action.admin.order.UpdateOrderStatusAndSendEmailAction"
                            style="color:red; font-size:1.3em; padding:3px;">Send shipping emails</s:link>
                </shiro:hasRole>
            </td>
        </tr>
    </table>
</shiro:hasAnyRoles>

<shiro:hasPermission name="<%=PermissionConstants.STORE_MANAGER%>">
    <c:if test="${whAction.userService.warehouseForLoggedInUser.store != null}">
        <table>
            <tr>
                <td><s:link beanclass="com.hk.web.action.admin.pos.POSAction"
                            style="color:red; font-size:1.3em; padding:3px;">Store</s:link>
                </td>
            </tr>
        </table>
    </c:if>
</shiro:hasPermission>

<div class="left roundBox">

    <h2>Basic Functionalities</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction">Search Base Orders</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction">Search Shipping Orders</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.shippingOrder.FixedShippingOrderAction">Search Fixed Shipping Orders</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.subscription.SearchSubscriptionAction">Search Subscriptions</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">Search Users</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.user.SearchB2BUserAction">Search B2B Users</s:link></h3>

    <h3><a href="${pageContext.request.contextPath}/pages/admin/b2bPriceUpdate.jsp">Update B2B Price</a></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" event="seekPayment">
            Seek Payment
        </s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.crm.MasterResolutionAction" event="pre">
            CRM Master Screen
        </s:link></h3>


    <h3><s:link beanclass="com.hk.web.action.core.menu.MenuRefreshAction">Refresh Menu</s:link></h3>

    <shiro:hasRole name="<%=RoleConstants.GOD%>">
        <h3><s:link beanclass="com.hk.web.action.core.menu.DataIndexRefreshAction">Refresh Data Indexes</s:link></h3>

        <h3><s:link beanclass="com.hk.web.action.core.RefreshCacheAction">Refresh Cache</s:link></h3>
    </shiro:hasRole>

    <c:if test="${whAction.setWarehouse == null}">
        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction">Action Awaiting Queue</s:link></h3>
    </c:if>

    <h3><s:link
            beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction">Action Item Resolution Queue</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction"> Notify Me List </s:link></h3>
    <shiro:hasRole name="<%=RoleConstants.DEVELOPER%>">
        <h3><s:link beanclass="com.hk.web.action.admin.TaskManagerAction">Run Ant Tasks </s:link></h3>
    </shiro:hasRole>
    <shiro:hasPermission name="<%=PermissionConstants.POPULATE_BUSY_DATA%>">
        <h3><s:link
                beanclass="com.hk.web.action.admin.accounts.PopulateBusyDataAction"> Populate Busy Data </s:link></h3>
    </shiro:hasPermission>
    <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
        <h3><s:link
                beanclass="com.hk.web.action.admin.user.PopulateUserDetailAction"> Populate User Detail Data </s:link></h3>
    </shiro:hasRole>
    <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
        <h3><s:link
                beanclass="com.hk.web.action.admin.user.PopulateUnsubscribeTokenAction"> Populate User Unsubscribe Token </s:link></h3>
    </shiro:hasRole>
    <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
        <h3><s:link
                beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction"> Add/Assign Roles and Permissions </s:link></h3>
    </shiro:hasRole>

    <shiro:hasPermission name="<%=PermissionConstants.VIEW_REVERSE_PICKUP%>">
        <h3><s:link
                beanclass="com.hk.web.action.admin.reversePickup.ReversePickupListAction"> New Reverse Pickup List</s:link></h3>
    </shiro:hasPermission>
        <%--
            <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
                <h3><s:link
                        beanclass="com.hk.web.action.admin.util.RunGenericGroovyScriptAction"> Run generic groovy script </s:link></h3>
            </shiro:hasRole>
        --%>

        <%--<h3><s:link beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction"> Check Payment History </s:link></h3>--%>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Warehouse</h2>
    <c:if test="${whAction.setWarehouse != null}">
        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction">Bin Allocation(Per Sku Item)</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction"
                event="uploadBinAllocationFile">Upload Bin Allocation File</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction">Inventory Audit(Per Sku Item)</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction"
                event="auditList">Inventory Audit List</s:link></h3>

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
                beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction">Search Shipping Order & Checkin RTO</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.DeliveryAwaitingQueueAction">Delivery Awaiting Queue</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction">Drop Shipping Queue</s:link></h3>

        <h3><s:link
                beanclass="com.hk.web.action.admin.queue.ShipmentInstallationAwaitingQueueAction">Installation Awaiting Queue</s:link></h3>
    </c:if>

    <h3>
        <s:link
                beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction"
                event="downloadWHInventorySnapshot">WH Inventory Excel
        </s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.inventory.CreateInventoryFileAction">Create Inventory File</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.inventory.SearchHKBatchAction">Search HK Batch</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction"
                event="reviewBatches">Batches To Review</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">Create Replacement Order</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction"
            event="searchReplacementOrders">Search Replacement Order</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.inventory.UniqueBarcodeAction">Make Unique Barcodes</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Inventory Management</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">Supplier List<br/> <span
                class="sml gry">(Create PO)</span></s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.POAction">PO List</s:link>
    </h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction"
                event="searchExtraInventory">ExtraInventory List</s:link>
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
            <s:link beanclass="com.hk.web.action.admin.inventory.CreditNoteAction">Credit Note List</s:link></h3>

        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction">Cycle Count List</s:link></h3>

            <%--   <h3><s:link
                   beanclass="com.hk.web.action.admin.courier.ReverseOrdersManageAction">Reverse Pickup List</s:link></h3>

                 <h3>--%>
        <s:link
                beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">Reconciliation Voucher List</s:link>
        </h3>

        <shiro:hasPermission name="<%=PermissionConstants.VIEW_REVERSE_PICKUP%>">


            <h3><s:link
                    beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction">RPWarehouse Checkin</s:link></h3>
        </shiro:hasPermission>

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

    <shiro:hasRole name="<%=RoleConstants.GOD%>">
        <h3>
            <s:link beanclass="com.hk.web.action.admin.inventory.GrnCloseAction">Close Grns</s:link>
        </h3>
            <%--
                    <h3>
                        <s:link beanclass="com.hk.web.action.admin.inventory.GrnCloseAction" event="closeGrn">Close Grns(2 Months)</s:link>
                    </h3>
            --%>
    </shiro:hasRole>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Site Admin</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.email.SMSHomeAction">Send SMS</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.report.ReportAction">Report Manager</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.report.AdvReportAction">Adv. Reports</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.marketing.GoogleBannedWordAction">Google Banned Words Report</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.user.PendingRewardPointQueueAction">Pending Reward Points</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction">Pending Product Reviews</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.hkbridge.UserCallResponseSummaryAction"
                event="pre">COD/Payment Failure Order Summary</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Affiliate</h2>

    <h3><s:link beanclass="com.hk.web.action.core.affiliate.VerifyRejectAffiliateAction">Verify Affiliates</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction">Affiliate Account</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.core.discount.CategoryLevelDiscountAction">Category Level Discount</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Marketing</h2>

    <h3><s:link beanclass="com.hk.web.action.admin.offer.OfferAdminAction">Offer Admin</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.facebook.app.coupon.FanCouponAdminAction">Fan Coupon Admin</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.email.EmailListByCategoryAction">Mailing List By Category</s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterAdmin">Email Newsletter Admin</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.marketing.AdsProductMetaDataAction">Ads product meta data</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction"> Marketing Expense List</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.marketing.MarketingProductFeedAction"> Add/Remove Products To Feeds </s:link></h3>

    <h3><s:link beanclass="com.hk.web.action.admin.clm.CustomerScoreAction">Upload CLM Score</s:link></h3>

    <h3><s:link
            beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction">Review Collection Framework</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Site Content Management</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.core.content.seo.BulkSeoAction"
                title="Change MetaData by Category">Change MetaData</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
    <h2>Finance</h2>

    <h3>
        <s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoicePdfAction"
                title="Download AccountingInvoice PDFs">Download AccountingInvoice PDFs</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">Debit Note List</s:link></h3>

    <h3>
        <s:link beanclass="com.hk.web.action.admin.accounts.SupplierTransactionAction"
                title="View Supplier Accounts" event="pre">View Supplier Accounts</s:link></h3>


</div>

<%--<script type="text/javascript">
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
</script>--%>

<!-- Script to render JIRA issue collector Form -->
<script type="text/javascript"
        src="https://healthkart.atlassian.net/s/en_US-pg27qm-418945332/6020/74/1.3.2/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=f0a8759b"></script>

</s:layout-component>
</s:layout-render>
