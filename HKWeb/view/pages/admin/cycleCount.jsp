<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.hk.domain.cycleCount.CycleCountItem" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count">
	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function() {
				$('.scannedBarcode').live("change", function() {
					var value = $(this).val();
					if( value == null || value.trim() == ''){
						return false;
					}
					else {
					$(this).attr("disable","disable");
					return $('.saveform').click();
					}
				});
			});
		</script>
	</s:layout-component>
	<s:layout-component name="content">

		<div style=" text-align:center;">
			<c:choose>
				<c:when test="${cycle.error}">
					<span style="color:red;">${cycle.message}</span>
				</c:when>
				<c:otherwise>
					<span style="color:green;">${cycle.message}</span>
				</c:otherwise>
			</c:choose>
		</div>
		<br/>

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
					<th>Mrp</th>
					<th>Mfg Date</th>
					<th>Expiry Date/th>
					<th>Scanned Qty</th>
					<th>Total Inventory</th>

				</tr>
				</thead>
				<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
				<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
				<s:hidden name="message" value="${cycle.message}"/>
				<s:hidden name="error" value="${cycle.error}"/>
				<s:hidden name="cycleCountPVImapString" class="cycleItem" value="${cycle.cycleCountPVImapString}"/>
				Scan Here <s:text name="hkBarcode" class="scannedBarcode"/>
				<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
					<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
						<s:hidden name="cycleCountItems[${ctr.index}]" value="${cCItem.id}"/>
						<tr class="ccItemRow">
							<td> ${cCItem.skuGroup.sku.productVariant.id}
								<s:hidden name="cycleCountItems[${ctr.index}].skuGroup" value="${cCItem.skuGroup.id}"/>
							</td>

							<td> ${cCItem.skuGroup.sku.productVariant.product.name} </td>
							<td> ${cCItem.skuGroup.barcode} </td>
							<td> ${cCItem.skuGroup.mrp}</td>
							<td><fmt:formatDate value="${cCItem.skuGroup.mfgDate}" type="date"/></td>
							<td><fmt:formatDate value="${cCItem.skuGroup.expiryDate}" type="date"/></td>
							<td>${cCItem.scannedQty}</td>

							<td>
								<c:set value="${cycle.cycleCountPVImap}" var="item"/>
									${item[cCItem.id]}
							</td>

						</tr>
						<br/>
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

			<div>
				<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
					<s:submit name="save" value="Freeze"/>
				</c:if>
				</s:form>
			</div>
		</div>








		<div>
			<fieldset class="right_label">
				<legend>Upload Cycle Count Notepad</legend>
				<ul>
					<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
					<s:hidden name="cycleCount"	value="${cycle.cycleCount.id}" />
						<li><label>File to Upload</label>
							 <s:file name="fileBean" size="30"/>
						</li>
						<li>
							<div class="buttons">
								<s:submit name="uploadCycleCountNotepad" value="Upload"/>
							</div>
						</li>
						<li>
							Notepad contains only Hkbarcode , 1 in each line

						</li>
					</s:form>
			</ul>
				</fieldset>
		</div>


	</s:layout-component>

</s:layout-render>	