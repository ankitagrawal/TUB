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
			<th>Hk Barcode</th>
			<th>Scanned Qty</th>
			<th>Total System Qty</th>
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
							<c:set value="${cycle.cycleCountPVImap}" var="item"/>
							<td>${item[cCItem.id]}</td>
							<td>${(item[cCItem.id]) - (cCItem.scannedQty)}</td>
						</tr>

					</c:forEach>
					</table>

					<div style="text-align: center;">
						<c:set value="<%= EnumCycleCountStatus.RequestForApproval.getId()%>" var="pendingForApproval"/>
						<c:if test="${cycle.cycleCount.cycleStatus == pendingForApproval}">
							<s:submit name="saveVariance" value="Approved"/>
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
					<s:submit name="closeCycleCount" value="Close"/>
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