<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.hk.domain.cycleCount.CycleCountItem" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<style type="text/css">
	.scannedBarcode {
		width: 125px
	}

	#heading li {
		margin-left: 0px
	}
</style>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count">
	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function() {

				$('.scannedBarcode').focus();
				$('.scannedBarcode').keydown(function() {
					$('#errordiv').hide();
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


                var scannedSum = 0;
                var systemSum = 0;
                var varianceSum = 0;
                $('.scannedQty').each(function() {
                    scannedSum = scannedSum + Number($(this).text());
                });
                $("#scannedValue").html(scannedSum);

                $('.systemQty').each(function() {
                    systemSum = systemSum + Number($(this).text());
                });
                $("#systemValue").html(systemSum);

                $('.varianceQty').each(function () {
                    varianceSum = varianceSum + Number($(this).text());
                });
                if (varianceSum < 0) {
                    $("#varianceValue").html(varianceSum).css("color", "red");
                } else {
                    $("#varianceValue").html(varianceSum);
                }

                $('#uploadnotepad').live("click", function() {
					var filebean = $('#filebean').val();
					if (filebean == null || filebean == '') {
						alert('choose file');
						return false;
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
		<c:if test="${cycle.cycleCount.brand != null}">
			<c:set value="${cycle.cycleCount.brand}" var="auditOn"/>
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
					Scan Here HKBarcode<s:text style="width:120px;height:24px;" name="hkBarcode" class="scannedBarcode"/>
				</div>  
              <div style ="float:right;width:900px;height:800px;overflow-y:scroll;">
				<table style="float: right;margin-top:0px;margin-left:15px;">
					<thead>
					<tr>
						<th>VariantID</th>
                        <th>Product Name</th>
                        <th>Variant Option</th>
						<th>Hk Barcode</th>
                        <th>Batch</th>
						<th>Mrp</th>
						<th>Mfg Date</th>
						<th>Expiry Date</th>
						<th>Scanned Qty</th>
						<th>System Quantity</th>
                        <th>Variance</th>

					</tr>
					</thead>

                    <c:set var="scannedsum" value="0"/>
                    <c:set var="skuItemInventory"  value="1" />
					<c:if test="${(cycle.cycleCountItems != null)&& (fn:length(cycle.cycleCountItems) > 0)}">
						<c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
							<s:hidden name="cycleCountItems[${ctr.index}]" value="${cCItem.id}"/>
							<tr class="ccItemRow">
                                <c:if test="${cCItem.skuItem != null && cCItem.skuGroup == null }">

                                 <td style="width:75px;"> ${cCItem.skuItem.skuGroup.sku.productVariant.id}</td>          

                                <td> ${cCItem.skuGroup.sku.productVariant.optionsPipeSeparated} </td>
                                <td> ${cCItem.skuItem.skuGroup.sku.productVariant.product.name} </td>
								<td> ${cCItem.skuItem.barcode} </td>
                                <td>${cCItem.skuGroup.batchNumber}</td>    
								<td> ${cCItem.skuItem.skuGroup.mrp}</td>
								<td><fmt:formatDate value="${cCItem.skuItem.skuGroup.mfgDate}" type="date"/></td>
								<td><fmt:formatDate value="${cCItem.skuItem.skuGroup.expiryDate}" type="date"/></td>
                                <td> <label class="scannedQty"> ${cCItem.scannedQty} </label></td>
                                <td><label class="systemQty"> ${skuItemInventory} </label></td>
                                <td> <label class="varianceQty">${cCItem.scannedQty - skuItemInventory } </label></td>

                               </c:if>
                                <c:if test="${cCItem.skuGroup != null && cCItem.skuItem == null}">
								<td> ${cCItem.skuGroup.sku.productVariant.id}
									<s:hidden name="cycleCountItems[${ctr.index}].skuGroup"
									          value="${cCItem.skuGroup.id}"/>
								</td>

								<td> ${cCItem.skuGroup.sku.productVariant.product.name} </td>
                                <td> ${cCItem.skuGroup.sku.productVariant.optionsPipeSeparated} </td>
								<td> ${cCItem.skuGroup.barcode} </td>
                                <td>${cCItem.skuGroup.batchNumber}</td>
								<td> ${cCItem.skuGroup.mrp}</td>
								<td><fmt:formatDate value="${cCItem.skuGroup.mfgDate}" type="date"/></td>
								<td><fmt:formatDate value="${cCItem.skuGroup.expiryDate}" type="date"/></td>

								<c:set value="${cycle.cycleCountPviMap}" var="item"/>

                                <c:choose>

                                    <c:when test="${(cCItem.scannedQty) > (item[cCItem.id])}">

                                        <td><span style="color:red"><label
                                                class="scannedQty"> ${cCItem.scannedQty}</label> </span></td>

                                    </c:when>
                                    <c:otherwise>
                                        <td><label class="scannedQty"> ${cCItem.scannedQty} </label></td>
                                    </c:otherwise>

                                </c:choose>
                                <td>
									<label class="systemQty">	${item[cCItem.id]}   </label>
								</td>

                                <td>
                                    <label class="varianceQty">${(item[cCItem.id]) - (cCItem.scannedQty)}</label>
                                </td>
                              </c:if>  
							</tr>

						</c:forEach>
					</c:if>

                    <tr>
                       
                        <td style="font-weight:bold;" colspan="8">Total</td>
                        <td class="totalQuantity">
                            <label id="scannedValue" style="font-weight:bold;"></label></td>
                        <td> <label style="font-weight:bold;" id="systemValue"></label></td>
                         <td><label style="font-weight:bold;" id="varianceValue"></label></td>
                    </tr>                  


							<div style="display:none;">
								<input type="submit" class="saveform" name="saveScanned"/>
							</div>

				</table>
            </div>  

				<div style="margin-top: 60px;margin-bottom: 40px;">
                    <shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
						<s:submit name="save" value="Freeze"/>
					</shiro:hasPermission>

				</div>
                 </div>
                </s:form>
        
                <div style="margin-top: 100px;margin-bottom: 40px;">
                <%--<div style="margin:0px auto;width:600px;margin-top: 42px;" align="left">--%>
                    <fieldset class="right_label" style="margin-left:0;">
                        <legend style="color:#000;margin-left:25px;background-color:#ebebeb; padding:10px;">Upload Cycle Count Notepad</legend>
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




        

        <div align="left">
            <s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
                <s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
                <s:submit name="downloadSkuGroupMissedInScanning" value="Download missed batch"/>
            </s:form>
        </div>
         <div style="height: 100px;">
        </div>

        <div align="left">
            <fieldset class="right_label" style="margin-left:0;">
                <legend>Delete All Scanned Batches For Product Variant in Cycle Count View Only</legend>
                <br/>
                <br/>
                <shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
                    <s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
                        <s:hidden name="cycleCount" value="${cycle.cycleCount.id}"/>
                        <s:text name="productVariantId"/>
                        <s:submit name="deleteAllScannedBatchForPVId" value="deleteAllScannedBatches"/>
                    </s:form>
                </shiro:hasPermission>
            </fieldset>
        </div>

	</s:layout-component>

</s:layout-render>	