<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Product Search">
	<s:useActionBean beanclass="com.hk.web.action.admin.pos.PosProductSearchAction" var="ps"/>
	<s:layout-component name="htmlHead">
	</s:layout-component>
	<s:layout-component name="content">
		<table class="zebra_vert">
			<thead>
			<tr>
				<th>GRN/RV No.</th>
				<th>Batch No.</th>
				<th>Mfg. <br>Date</th>
				<th>Expiry<br> Date</th>
				<th>Cost Price</th>
				<th>MRP</th>
				<th>Checkin<br> Date</th>
				<th>Checked-In<br> Units</th>
				<th>In-stock<br> Units</th>
			</tr>
			</thead>
			<c:forEach items="${ps.skuGroupList}" var="skuGroup" varStatus="ctr">
				<c:set var="batchInv" value="${fn:length(hk:getInStockSkuItems(skuGroup))}"/>
				<tr>
					<td>
						<c:if test="${skuGroup.goodsReceivedNote != null}">
							<s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view"
							        target="_blank">
								<s:param name="grn" value="${skuGroup.goodsReceivedNote.id}"/>
								${skuGroup.goodsReceivedNote.id}
							</s:link>
						</c:if>
						<c:if test="${skuGroup.reconciliationVoucher != null}">
							<s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
							        event="view" target="_blank">
								<s:param name="reconciliationVoucher" value="${skuGroup.reconciliationVoucher.id}"/>
								${skuGroup.reconciliationVoucher.id}
							</s:link>
						</c:if>

					</td>
					<td>${skuGroup.batchNumber}</td>
					<td><fmt:formatDate value="${skuGroup.mfgDate}" pattern="MM/yyyy"/></td>
					<td><fmt:formatDate value="${skuGroup.expiryDate}" pattern="MM/yyyy"/></td>
					<td>${skuGroup.costPrice}</td>
					<td>${skuGroup.mrp}</td>
					<td><fmt:formatDate value="${skuGroup.createDate}" pattern="dd/MM/yyyy"/></td>
					<td>${fn:length(skuGroup.skuItems)}</td>
					<td>${batchInv}</td>
				</tr>
			</c:forEach>
		</table>
	</s:layout-component>
</s:layout-render>