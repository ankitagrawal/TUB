<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Store Admin Home">
	<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction"
	                 event="getUserWarehouse"/>

	<s:layout-component name="heading">Store</s:layout-component>
	<s:layout-component name="content">
		<style type="text/css">
			.float {
				float: left;
				position: relative;
				height: 200px;
			}
		</style>
		<div class="float roundBox">
			<h2>Sales</h2>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.pos.POSAction">Order Screen</s:link>
			</h3>

			<h3>
				<s:link class="invert"
				        beanclass="com.hk.web.action.admin.order.search.SearchOrderAction">Search Base Order</s:link>
			</h3>

			<h3>
				<s:link class="invert"
				        beanclass="com.hk.web.action.admin.pos.POSReportAction">Reports</s:link>
			</h3>

		</div>

		<div class="cl"></div>

		<div class="float roundBox">
			<h2>Purchasing</h2>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.inventory.POAction">PO List</s:link>
			</h3>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">Supplier List<br/> <span
						class="sml gry">(Create PO or Raise a Debit Note)</span></s:link>
			</h3>

		</div>

		<div class="cl"></div>

		<div class="float roundBox">
			<h2>Receiving Issues</h2>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.inventory.GRNAction">GRN List <span
						class="sml gry">(Checkin against GRN)</span></s:link>
			</h3>

		</div>

		<div class="cl"></div>

		<div class="float roundBox">
			<h2>Inventory</h2>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.pos.PosProductSearchAction">Product Search</s:link>
			</h3>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.inventory.CreateInventoryFileAction">Create Inventory File</s:link>
			</h3>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction">Cycle Count List</s:link>
			</h3>

			<h3>
				<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction">Stock Transfer List</s:link>
			</h3>

		</div>

	</s:layout-component>
</s:layout-render>
