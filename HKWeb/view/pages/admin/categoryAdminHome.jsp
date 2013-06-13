<%@ taglib prefix="shirp" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Category Admin Home">
    <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction"
                     event="getUserWarehouse"/>

    <s:layout-component name="heading">Category</s:layout-component>
    <s:layout-component name="content">
        <style type="text/css">
            .float {
                float: left;
                position: relative;
                height: 200px;
            }
        </style>
        <div class="float roundBox">
            <h2>Purchasing</h2>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.inventory.POAction">PO List</s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">Supplier List<br/> <span
                        class="sml gry">(Create PO or Raise a Debit Note)</span></s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">Purchase Invoice List</s:link>
            </h3>
        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Receiving Issues</h2>

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

        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Pricing Management</h2>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.product.UpdatePvPriceAction">Update Variant Price
                    <br/><span class="sml gry" style="color:red">(List of Variants - MRP Mismatch)</span>
                </s:link>
            </h3>

            <h3><s:link beanclass="com.hk.web.action.admin.store.StorePricingAction">Store pricing</s:link>
            </h3>
        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>New Products Cataloging</h2>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.catalog.ParseExcelAction">Upload Catalog Excel<br/><span class="sml gry" style="color:red">(SKUs need to be created manually for new variants)</span></s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.sku.SkuParseExcelAction">Upload SKU Excel</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction">Create new product and product variant</s:link>
            </h3>

            <h3><s:link
                    beanclass="com.hk.web.action.core.loyaltypg.LoyaltyBulkUploadAction">Add new loyalty Products/ User Badges</s:link>
            </h3>

        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Catalog Maintenance</h2>

            <h3>
                <s:link
                        beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">Generate Catalog Excel by Category<br/><span class="sml gry">(also shows Inventory status)</span></s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction">Bulk Edit Product And Variant Attributes</s:link></h3>

            <h3>
                <h3>
                    <s:link beanclass="com.hk.web.action.admin.sku.SkuAction">Add/Edit SKUs</s:link></h3>

                </h3>

                    <h3><s:link
                            beanclass="com.hk.web.action.admin.catalog.product.BulkUploadRelatedProductAction">Upload bulk related product</s:link>
                    </h3>

        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Combos and Subscriptions</h2>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction">Create Combo</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.catalog.subscription.CreateEditSubscriptionProductAction">Create/edit Subscription(s)
                <br/><span class="sml gry">(includes excels)</span></s:link></h3>

        </div>

        <div class="cl"></div>


        <div class="float roundBox">
            <h2>Action Queue</h2>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction">Action Awaiting Queue</s:link></h3>
        </div>

        <div class="cl"></div>


        <div class="float roundBox">
            <h2>Marketing Related Tasks</h2>

            <h3><s:link beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction"> Notify Me List </s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.catalog.product.RelatedProductAction">Update Related Products</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.inventory.EditSimilarProductsAction">Similar Products</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.marketing.AmazonParseExcelAction">Upload Amazon Excel</s:link></h3>
        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Eye Category Config</h2>

            <h3><s:link beanclass="com.hk.web.action.admin.catalog.product.AddEyeConfigAction">Add Eye Config</s:link></h3>
            <h3><s:link beanclass="com.hk.web.action.admin.catalog.product.AddVirtualTryOnAction">Add Virtual Try on Filter</s:link></h3>
            <h3><s:link beanclass="com.hk.web.action.admin.catalog.TryOnXmlsUploadAction" event="pre">Generate Update Eye Try On</s:link></h3>
        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Inventory</h2>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction">Search Available Batches </s:link></h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction"
                        event="listOutOfStock">Out of Stock List</s:link></h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction">Low Inventory List</s:link></h3>
        </div>

        <div class="cl"></div>

        <div class="left roundBox">
            <h2>Services</h2>

            <h3><s:link beanclass="com.hk.web.action.admin.queue.ServiceQueueAction">Service Queue</s:link></h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAction">View/Edit Merchant Details</s:link></h3>

        </div>


    </s:layout-component>
</s:layout-render>
