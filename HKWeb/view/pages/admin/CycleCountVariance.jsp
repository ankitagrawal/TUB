<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Variance Report">
	<s:layout-component name="heading">
		<div style="text-align: center;">
		CYCLE COUNT # ${cycle.cycleCount.id}
		BRAND : ${cycle.cycleCount.brandsToAudit.brand}
	   </div>
	</s:layout-component>
	<s:layout-component name="content">

		<c:set value="<%= EnumCycleCountStatus.Closed.getId()%>" var="closed"/>
		<c:if test="${cycle.cycleCount.cycleStatus < closed }">
		<table style="margin: 80px auto 81px;">
		<thead>
		<tr>
			<th>VariantID</th>
            <th>Variant Option</th>
            <th>Batch</th>
            <th>MRP</th>
            <th>MFG Date</th>
            <th>Expiry</th>
			<th>Hk Barcode</th>
			<th>Scanned Qty</th>
			<th>System Quantity</th>
			<th>Variance</th>

		</tr>
		</thead>

			<div style="margin: 0px auto;text-align: center;">
			<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
				<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
					<c:set value="<%= EnumCycleCountStatus.Approved.getId()%>" var="approved"/>
					<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
						<s:hidden name="cycleCountItems[${ctr.index}]" value="${cCItem.id}"/>
						<tr>
							<td>${cCItem.skuGroup.sku.productVariant.id}</td>
                            <td>${cCItem.skuGroup.sku.productVariant.optionsPipeSeparated}</td>
                            <td>${cCItem.skuGroup.batchNumber}</td>
                            <td>${cCItem.skuGroup.mrp}</td>
                            <td>${cCItem.skuGroup.mfgDate}</td>
                            <td>${cCItem.skuGroup.expiryDate}</td>
							<td>${cCItem.skuGroup.barcode}</td>
							<td>

								<c:choose>
									<c:when test="${cycle.cycleCount.cycleStatus >= approved}">
										${cCItem.scannedQty}
									</c:when>
									<c:otherwise>
										<shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
											<input type="text" name="cycleCountItems[${ctr.index}].scannedQty"
											       value="${cCItem.scannedQty}">
										</shiro:hasPermission>
										<shiro:lacksPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
											${cCItem.scannedQty}
										</shiro:lacksPermission>
									</c:otherwise>
								</c:choose>
							</td>
							<c:set value="${cycle.cycleCountPviMap}" var="item"/>
							<td>${item[cCItem.id]}</td>

							<c:choose>
								<c:when test="${(cCItem.scannedQty) > (item[cCItem.id])}">
									<td><span style="color:red">${(item[cCItem.id]) - (cCItem.scannedQty)} </span></td>
								</c:when>
								<c:otherwise>
									<td>${(item[cCItem.id]) - (cCItem.scannedQty)}</td>
								</c:otherwise>
							</c:choose>

						</tr>

					</c:forEach>
                <tr style="text-align:center;">
                    <td colspan="10" style="font:bold;font-family:cursive;color:#ff0000;">
                        <label>Missed Scanned</label>
                    </td>
                </tr>

                    <c:forEach items="${cycle.missedSkuGroupList}" var="missedskugroup">
                        <tr>
                        <td>${missedskugroup.sku.productVariant.id}</td>
                        <td>${missedskugroup.sku.productVariant.optionsPipeSeparated}</td>
                        <td>${missedskugroup.batchNumber}</td>
                        <td>${missedskugroup.mrp}</td>
                        <td>${missedskugroup.mfgDate}</td>
                        <td>${missedskugroup.expiryDate}</td>
                        <td>${missedskugroup.barcode}</td>
                        <td> 0</td>
                        <c:set value="${cycle.skuGroupSystemInventoryMap}" var="item"/>
                        <td>${item[missedskugroup.id]}</td>
                        <td>${item[missedskugroup.id]}</td>
                        </tr>
                    </c:forEach>
                </table>

					<div style="text-align: center;">
						<c:set value="<%= EnumCycleCountStatus.RequestForApproval.getId()%>" var="pendingForApproval"/>
						<c:if test="${cycle.cycleCount.cycleStatus == pendingForApproval}">
							<shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
										<s:submit name="saveVariance" value="Approved"/>
										</shiro:hasPermission>							
						</c:if>
					</div>

			</s:form>
			</div>
		</c:if>


		<c:if test="${cycle.cycleCount.cycleStatus == approved}">
			<br/>

			<div class="clear">

			</div>
			<fieldset class="right_label" style="display: inline-block">
				<legend>Download Reconciliation Add Excel</legend>
				<ul>
					<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
						<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
						<li>
							<s:submit name="generateReconAddExcel" value="RvAdd"/>
						</li>
					</s:form>
				</ul>
			</fieldset>

			<fieldset class="right_label" style="display: inline-block;margin-left: 469px;float: right;">
				<legend>Download Complete Cycle Count Excel</legend>
				<ul>
					<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
						<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
						<li>
							<s:submit name="generateCompleteCycleExcel" value="VarianceReport"/>
						</li>
					</s:form>
				</ul>
			</fieldset>

			<div style="text-align: center;margin-top: 70px;">
				<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
					<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
					<c:if test="${cycle.cycleCount.brandsToAudit != null}">
						<s:hidden name="cycleCountType" value="1"/>	
					</c:if>
					<shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
										<s:submit name="closeCycleCount" value="Close"/>
										</shiro:hasPermission>
				</s:form>
			</div>
		</c:if>


		<c:if test="${cycle.cycleCount.cycleStatus == closed }">

			<table style="margin: 80px auto 20px;">
				<thead>
				<tr>
					<th>VariantID</th>
					<th>Hk Barcode</th>
					<th>Scanned Qty</th>
				</tr>
				</thead>
				<div style="margin: 0px auto;text-align: center;">
					<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
						<tr>
							<td>${cCItem.skuGroup.sku.productVariant.id}</td>
							<td>${cCItem.skuGroup.barcode}</td>
							<td>${cCItem.scannedQty}</td>
						</tr>
					</c:forEach>
				</div>
			</table>

		</c:if>

	</s:layout-component>

</s:layout-render>