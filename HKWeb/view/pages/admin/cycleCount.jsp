<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.hk.domain.cycleCount.CycleCountItem" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count">
	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function() {
				$('.scannedBarcode').live("change", function() {
					return $('.saveform').click();
				});
			});
		</script>
	</s:layout-component>
	<s:layout-component name="content">

		<div style="color:red; text-align:center;">
			<%--<c:forEach items="${cycle.hkBarcodeErrorsMap}" var="map">--%>
				<%--${map.key} ${map.value}--%>
			<%--</c:forEach>--%>
			${cycle.message}
		</div>

		<div style="margin: 0px auto;text-align: center;">
			<div>
				<h5>CYCLE COUNT # ${cycle.cycleCount.id}</h5><br/>
				<h5>BRAND : ${cycle.cycleCount.brandsToAudit.brand} </h5><br/><br/>
			</div>
			<table style="margin: 80px auto 20px;">
				<thead>
				<tr>
					<th>VariantID</th>
					<th>Details</th>
					<th>Hk Barcode</th>
					<th>Scanned Qty</th>
					<th>Total Inventory</th>

				</tr>
				</thead>
				<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
				<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
				<s:hidden name="message" value="${cycle.message}"/>
				<s:hidden name="cycleCountPVImapString"  class="cycleItem" value="${cycle.cycleCountPVImapString}"/>
				Scan Here <s:text name="hkBarcode" class="scannedBarcode"/>
				<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
					<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
						<s:hidden name="cycleCountItems[${ctr.index}]" value="${cCItem.id}"/>
						<s:hidden name="cycleCountItems[${ctr.index}].cycleCount" value="${cCItem.cycleCount.id}"/>
						<tr class="ccItemRow">
							<td> ${cCItem.skuGroup.sku.productVariant.id}
								<s:hidden name="cycleCountItems[${ctr.index}].skuGroup" value="${cCItem.skuGroup.id}"/>
							</td>

							<td> ${cCItem.skuGroup.sku.productVariant.product.name} </td>
							<td> ${cCItem.skuGroup.barcode} </td>
							<td>${cCItem.scannedQty}
								<s:hidden name="cycleCountItems[${ctr.index}].scannedQty" value="${cCItem.scannedQty}"/>
							</td>

							<td>
								<c:set value="${cycle.cycleCountPVImap}" var="item"/>
									${item[cCItem.id]}
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

				</tr>
			</table>
			<s:submit name="save" value="done"/>
			</s:form>
		</div>
	</s:layout-component>

</s:layout-render>	