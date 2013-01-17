<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
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
			<th>Total System Qty</th>
			<th>Variance</th>

		</tr>
		</thead>
		 <div style="margin: 0px auto;text-align: center;">
		<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
			<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
			<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
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
						<c:set value="${cycle.scannedPviVariance}" var="item"/>
						 <td>${cCItem.scannedQty + item[cCItem.id]}</td>
						<td> ${item[cCItem.id]}  </td>
					</tr>

				</c:forEach>
				</table>
				<div style="text-align: center;">
				<c:set value="<%= EnumCycleCountStatus.PendingForApproval.getId()%>" var="pendingForApproval"/>
				<c:if test="${cycle.cycleCount.cycleStatus == pendingForApproval}">
					<s:submit name="saveVariance" value="Approved"/>
				</c:if>
				</div>
			</c:if>
			</div>
		</s:form>

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

			<fieldset class="right_label"  style="display: inline-block;margin-left: 469px;">
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


			

		</c:if>


	</s:layout-component>

</s:layout-render>