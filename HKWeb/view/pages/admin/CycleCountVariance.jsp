<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Variance Report">

	<s:layout-component name="content">

		<table style="margin: 80px auto 20px;">
		<thead>
		<tr>
			<th>VariantID</th>
			<th>Hk Barcode</th>
			<th>Scanned Qty</th>
			<th>Total Inventory</th>
			<th>Variance</th>

		</tr>
		</thead>

		<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
			<s:hidden name="cycleCountStatus" value="${cycle.cycleCountStatus}"/>
			<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
				<c:set value="<%= EnumCycleCountStatus.Approved.getId()%>" var="approved"/>
				<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
					<s:hidden name="cycleCountItems[${ctr.index}].skuGroup" value="${cCItem.skuGroup.id}"/>
					<tr>
						<td>${cCItem.skuGroup.sku.productVariant.id}</td>
						<td>${cCItem.skuGroup.barcode}</td>
						<td>

							<c:choose>
								<c:when test="${cycle.cycleCountStatus == approved}">
									${cCItem.scannedQty}
								</c:when>
								<c:otherwise>
									<shiro:hasPermission name="<%=EnumPermission.RECON_VOUCHER_MANAGEMENT%>">
										<input type="text" name="cycleCountItems[${ctr.index}].scannedQty"
										       value="${cCItem.scannedQty}">
									</shiro:hasPermission>
									<shiro:lacksPermission name="<%=EnumPermission.RECON_VOUCHER_MANAGEMENT%>">
										${cCItem.scannedQty}
									</shiro:lacksPermission>
								</c:otherwise>
							</c:choose>
						</td>

						<td><c:set value="${cycle.cycleCountPVImap}" var="item"/> ${item[cCItem.id]}  </td>
					</tr>

				</c:forEach>
				</table>
				<c:if test="${cycle.cycleCountStatus < approved}">
					<s:submit name="saveVariance" value="Approved"/>
				</c:if>
			</c:if>
		</s:form>

		<c:if test="${cycle.cycleCountStatus == approved}">
	    //download excel sheet


			

		</c:if>


	</s:layout-component>

</s:layout-render>