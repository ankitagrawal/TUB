<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count">
	<s:layout-component name="heading">
		CYCLE COUNT
	</s:layout-component>
	<s:layout-component name="htmlHead">
		<script type="text/javascript">
		 $(document).ready(function(){
		$('.scannedBarcode').live("change", function(){
			return $('.saveform').submit();
		});
		 });
		</script>
	</s:layout-component>
	<s:layout-component name="content">
		<table>
		<thead>
		<tr>
			<th>VariantID</th>
			<th>Details</th>
			<th>Hk Barcode</th>
			<th>Scanned Qty</th>
			<th>Total Inventory</th>

		</tr>
		</thead>
		<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction" >
			<s:text name="hkBarcode" class="scannedBarcode"/>
			<c:if test="${cycle.cycleCountItems != null}">
				<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
					<tr class="ccItemRow">
						<td>
								${cycle.cycleCountItems[ctr.index].skuGroup.sku.productVariant.id}
							<s:hidden name="cycleCountItems[${ctr.index}].skuGroup"
							          value="${cycle.cycleCountItems[ctr.index].skuGroup.id}"/>
						</td>
						<td> ${cycle.cycleCountItems[ctr.index].skuGroup.sku.productVariant.variantName} </td>
						<td> ${cycle.cycleCountItems[ctr.index].skuGroup.barcode} </td>
						<td>
								${cycle.cycleCountItems[ctr.index].scannedQty}
							<s:hidden name="cycleCountItems[${ctr.index}].scannedQty"
							          value="${cycle.cycleCountItems[ctr.index].scannedQty}"/>
						</td>
						<td>
								${cycle.cycleCountItems[ctr.index].pviQuantity}
							<s:hidden name="cycleCountItems[${ctr.index}].pviQuantity"
							          value="${cycle.cycleCountItems[ctr.index].pviQuantity}"/>
						</td>
					</tr>
				</c:forEach>
			</c:if>
			<tr>
				<td>
					<div style="display:none;">
					<input type="submit" class="saveform" name="saveScanned"/>
					</div>
				</td>
				<td>
				<s:submit name="save" value="done"/>
				</td>
			</tr>
			</table>
		</s:form>
		</s:layout-component>

</s:layout-render>	