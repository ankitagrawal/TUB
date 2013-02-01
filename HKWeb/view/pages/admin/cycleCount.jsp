<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.hk.domain.cycleCount.CycleCountItem" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<style>
	.scannedBarcode {

		width: 125px
	}

	#heading li {
		margin-left: 0px
	}
</style>
</sttyle>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count">
	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function() {

				$('.scannedBarcode').focus();
				$('.scannedBarcode').keydown(function() {
					$('#errordiv').hide();
				});


				$('#uploadnotepad').live("click", function() {
					var filebean = $('#filebean').val();
					if (filebean == null || filebean == '') {
						alert('choose file');
						return false;
					}
				});


				$('.scannedBarcode').live("change", function() {
					var value = $(this).val();
					if (value == null || value.trim() == '') {
						return false;
					}
					else {
						$(this).attr("disable", "disable");
						return $('.saveform').click();
					}
				});
			});
		</script>
	</s:layout-component>
	<s:layout-component name="content">

		<div id="heading" style="margin:0px auto;width:1200px;margin-bottom: 30px;">
			<ul style="padding-left:0px">
				<li>CYCLE COUNT # ${cycle.cycleCount.id}</li>
		<c:set value="" var="auditOn"/>
		<c:set value="" var="cycleCountTypeV"/>
		<c:if test="${cycle.cycleCount.brandsToAudit != null}">
			<c:set value="${cycle.cycleCount.brandsToAudit.brand}" var="auditOn"/>
			<c:set value="1" var="cycleCountTypeV"/>
			<li>BRAND : ${auditOn} </li>
		</c:if>
		<c:if test="${cycle.cycleCount.product != null}">
			<c:set value="${cycle.cycleCount.product.id}" var="auditOn"/>
			<c:set value="2" var="cycleCountTypeV"/>
			<li>Product : ${auditOn} </li>
		</c:if>

		<c:if test="${cycle.cycleCount.productVariant != null}">
			<c:set value="${cycle.cycleCount.productVariant.id}" var="auditOn"/>
			<c:set value="3" var="cycleCountTypeV"/>
			<li>Product Variant : ${auditOn} </li>
		</c:if>
			</ul>
		</div>


		<div id="errordiv" style="font-weight: bold;font-size: 12px;margin-bottom: 30px;">
			<c:choose>
				<c:when test="${cycle.error}">
					<span style="color:red;">${cycle.message}</span>
				</c:when>
				<c:otherwise>
					<span style="color:green;">${cycle.message}</span>
				</c:otherwise>
			</c:choose>
		</div>


		<s:form id="ccform" beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
			<s:hidden name="cycleCountType" value="${cycleCountTypeV}"/>
			<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
			<s:hidden name="message" value="${cycle.message}"/>
			<s:hidden name="error" value="${cycle.error}"/>			
			<s:hidden name="cycleCountPVImapString" class="cycleItem" value="${cycle.cycleCountPVImapString}"/>
			<div style="width:1200px;margin:0px auto">
				<div style="display: inline-block;">
					Scan Here HKBarcode<s:text name="hkBarcode" class="scannedBarcode"/>
				</div>
				<table style="float: right;margin-top:0px">
					<thead>
					<tr>
						<th>VariantID</th>
						<th>Details</th>
						<th>Hk Barcode</th>
						<th>Mrp</th>
						<th>Mfg Date</th>
						<th>Expiry Date</th>
						<th>Scanned Qty</th>
						<th>Total Inventory</th>


					</tr>
					</thead>


					<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
						<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
							<s:hidden name="cycleCountItems[${ctr.index}]" value="${cCItem.id}"/>
							<tr class="ccItemRow">
								<td> ${cCItem.skuGroup.sku.productVariant.id}
									<s:hidden name="cycleCountItems[${ctr.index}].skuGroup"
									          value="${cCItem.skuGroup.id}"/>
								</td>

								<td> ${cCItem.skuGroup.sku.productVariant.product.name} </td>
								<td> ${cCItem.skuGroup.barcode} </td>
								<td> ${cCItem.skuGroup.mrp}</td>
								<td><fmt:formatDate value="${cCItem.skuGroup.mfgDate}" type="date"/></td>
								<td><fmt:formatDate value="${cCItem.skuGroup.expiryDate}" type="date"/></td>
								<c:set value="${cycle.cycleCountPviMap}" var="item"/>
								<c:choose>
									<c:when test="${(cCItem.scannedQty) > (item[cCItem.id])}">
										<td><span style="color:red"> ${cCItem.scannedQty} </span></td>
									</c:when>
									<c:otherwise>
										<td>${cCItem.scannedQty}</td>
									</c:otherwise>
								</c:choose>


								<td>

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

				<div style="margin-top: 60px;margin-bottom: 40px;">
					<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
						<s:submit name="save" value="Freeze"/>
					</c:if>

				</div>
			</div>
		</s:form>

		<br/><br/>

		<div style="margin:0px auto;width:1200px;margin-top: 42px;">
			<fieldset class="right_label">
				<legend>Upload Cycle Count Notepad</legend>
				<ul>
					<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
						<s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
						 <s:hidden name="cycleCountType" value="${cycleCountTypeV}"/>
						<li><label>File to Upload</label>
							<s:file id="filebean" name="fileBean" size="30"/>
						</li>
						<li>
							<s:submit id="uploadnotepad" name="uploadCycleCountNotepad" value="Upload"/>
						</li>
					</s:form>
				</ul>
			</fieldset>
		</div>


	</s:layout-component>

</s:layout-render>	