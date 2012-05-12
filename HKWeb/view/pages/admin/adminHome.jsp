<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.manager.LinkManager" %>
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
            <s:option value="${wh.id}">${wh.city}</s:option>
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

<div class="left roundBox">

  <h2>Basic Functionalities</h2>


  <h3><s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction">Search Base Orders</s:link></h3>
  <h3><s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction">Search Shipping Orders</s:link></h3>

  <h3><s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">Search Users</s:link></h3>

  <h3><s:link beanclass="com.hk.web.action.core.menu.MenuRefreshAction">Refresh Menu</s:link></h3>

  <h3><s:link beanclass="com.hk.web.action.core.menu.DataIndexRefreshAction">Refresh Data Indexes</s:link></h3>

  <c:if test="${whAction.setWarehouse == null}">
  <h3><s:link beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction">Action Awaiting Queue</s:link></h3>
  </c:if>
  <h3><s:link beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction"> Notify Me List </s:link></h3>
  <%--<h3><s:link beanclass="mhc.web.action.admin.PaymentHistoryAction"> Check Payment History </s:link></h3>--%>  
</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Category Managers</h2>

  <h3>
    <s:link
        beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">Generate Catalog Excel by Category<br/><span class="sml gry">(also shows Inventory status)</span></s:link>
  </h3>

  <h3><s:link beanclass="com.hk.web.action.admin.catalog.ParseExcelAction">Upload Catalog Excel<br/><span class="sml gry" style="color:red">(SKUs need to be created manually for new variants)</span></s:link></h3>

  <h3><s:link beanclass="com.hk.web.action.admin.marketing.AmazonParseExcelAction">Upload Amazon Excel</s:link></h3>

  <h3>
    <s:link beanclass="mhc.web.action.admin.BulkEditProductAction">Bulk Edit Product And Variant Attributes</s:link></h3>

  <h3>
    <s:link
        beanclass="mhc.web.action.admin.CreateOrSelectProductAction">Create new product and product variant</s:link></h3>

  <h3>
    <s:link beanclass="mhc.web.action.admin.CreateEditComboAction">Create Combo</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.RecentlyAddedProductsAction">Recently Added Products</s:link></h3>
  <h3><s:link beanclass="mhc.web.action.admin.RelatedProductAction">Update Related Products</s:link></h3>
  <h3><s:link beanclass="mhc.web.action.admin.GenerateReconcilationReportAction">Generate Reconcilation Report</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Warehouse</h2>
  <c:if test="${whAction.setWarehouse != null}">
  <h3><s:link beanclass="mhc.web.action.admin.AssignBinAction">Assign Bin</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.queue.PackingAwaitingQueueAction">Packing Awaiting Queue</s:link></h3>
  <h3>
    <s:link
        beanclass="mhc.web.action.admin.InventoryCheckoutAction">Search Shipping Order & Checkout</s:link></h3>
  <h3>
    <s:link
        beanclass="mhc.web.action.admin.SearchOrderAndEnterCourierInfoAction">Search Shipping Order & Enter Courier</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.queue.ShipmentAwaitingQueueAction">Shipment Awaiting Queue</s:link></h3>   
  <h3><s:link
      beanclass="mhc.web.action.admin.SearchOrderAndReCheckinRTOInventoryAction">Search Shipping Order & Checkin RTO</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.queue.DeliveryAwaitingQueueAction">Delivery Awaiting Queue</s:link></h3>

  <h3>
  </c:if>
    <s:link
        beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction" event="downloadWHInventorySnapshot">WH Inventory Excel
    </s:link></h3>
  <h3><s:link beanclass="mhc.web.action.admin.CreateInventoryFileAction">Create Inventory File</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.SearchHKBatchAction">Search HK Batch</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Courier and Services</h2>
  
  <h3><s:link beanclass="mhc.web.action.admin.MasterPincodeAction">Update Master Pincode List</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.courier.CourierServiceInfoAction">Update Courier Service Info</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.StateCourierServiceAction">State Courier Service Info</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.courier.CourierAWBAction">Update Courier AWB numbers</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.courier.ChangeDefaultCourierAction">Change Default Courier</s:link></h3>

    <h3><s:link beanclass="mhc.web.action.admin.UpdateAFLChhotuDeliveryStatusAction">Update Delivery Status of AFL/CHHOTU</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.ParseDTDCDeliveryStatusExcelAction">Upload Delivery Status Excel of DTDC</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.ParseCourierCollectionChargeExcelAction">Upload Courier Collection Charge Excel</s:link></h3>

	<h3><s:link beanclass="mhc.web.action.admin.ParseEstimatedCourierExpensesExcelAction">Upload Estimated Courier Collection Charges</s:link> </h3>

  <h3><s:link beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">Change shipment details</s:link></h3>

</div>

<div class="cl"></div>


<div class="left roundBox">
  <h2>Inventory Management</h2>
  <h3>
    <s:link beanclass="mhc.web.action.admin.SupplierManagementAction">Supplier List<br/> <span
        class="sml gry">(Create PO or Raise a Debit Note)</span></s:link>
  </h3>
  <h3>
    <s:link beanclass="mhc.web.action.admin.POAction">PO List</s:link>
  </h3>

  <h3>
    <s:link beanclass="mhc.web.action.admin.GRNAction">GRN List <span
        class="sml gry">(Checkin against GRN)</span></s:link>
  </h3>
	  <h3>
    <s:link beanclass="mhc.web.action.admin.PurchaseInvoiceAction">Purchase Invoice List</s:link>
  </h3>
  <c:if test="${whAction.setWarehouse != null}">
  <h3>
    <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">Debit Note List</s:link>
  </h3>
  </c:if>
  <h3>
    <s:link beanclass="mhc.web.action.admin.ReconciliationVoucherAction">Reconciliation Voucher List</s:link>
  </h3>

	 <h3>
    <s:link beanclass="mhc.web.action.admin.StockTransferAction">Stock Transfer List</s:link>
  </h3>

  <h3>
    <s:link beanclass="mhc.web.action.admin.SearchSkuBatchesAction">Search Available Batches </s:link></h3>

  <h3>
    <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction">Low Inventory List</s:link></h3>

  <h3>
    <s:link beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction" event="listOutOfStock">Out of Stock List</s:link></h3>

	<shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
		<h3>
    <s:link beanclass="mhc.web.action.admin.InventoryCheckinAction" event="downloadPrintBarcodeFile">Download Print Barcode File</s:link></h3>

	<h3>
    <s:link beanclass="mhc.web.action.admin.InventoryCheckinAction" event="clearPrintBarcodeFile">Clear Print Barcode File</s:link></h3>
	</shiro:hasPermission>

	<h3>
    <s:link beanclass="mhc.web.action.admin.SkuAction">Add/Edit SKUs</s:link></h3>

<h3>
    <s:link beanclass="mhc.web.action.admin.SkuParseExcelAction">Upload SKU Excel</s:link></h3>


</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Site Admin</h2>

  <h3><s:link beanclass="mhc.web.action.admin.SMSHomeAction">Send SMS</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.ReportAction">Report Manager</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.GoogleBannedWordAction">Google Banned Words Report</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.PendingRewardPointQueueAction">Pending Reward Points</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.PendingProductReviewAction">Pending Product Reviews</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Affiliate</h2>

  <h3><s:link beanclass="mhc.web.action.VerifyAffiliateAction">Verify Affiliates</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.affiliate.AffiliatePaymentAction">Affiliate Account</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.CategoryLevelDiscountAction">Category Level Discount</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Services</h2>

  <h3><s:link beanclass="mhc.web.action.admin.ServiceQueueAction">Service Queue</s:link></h3>

  <h3>
    <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAction">View/Edit Merchant Details</s:link></h3>

  <h3>
    <s:link
        beanclass="mhc.web.action.admin.BulkUploadMerchantAddressAction">Bulk Upload Merchant Address</s:link></h3>
</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Marketing</h2>

  <h3><s:link beanclass="mhc.web.action.admin.offer.OfferAdminAction">Offer Admin</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.facebook.app.coupon.FanCouponAdminAction">Fan Coupon Admin</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.EmailListByCategoryAction">Mailing List By Category</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.newsletter.EmailNewsletterAdmin">Email Newsletter Admin</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.AdsProductMetaDataAction">Ads product meta data</s:link></h3>

  <h3><s:link beanclass="mhc.web.action.admin.MarketingExpenseAction"> Marketing Expense List</s:link></h3>

</div>

<div class="cl"></div>

<div class="left roundBox">
  <h2>Site Content Management</h2>

  <h3>
    <s:link beanclass="mhc.web.action.BulkSeoAction"
            title="Change MetaData by Category">Change MetaData</s:link></h3>

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
