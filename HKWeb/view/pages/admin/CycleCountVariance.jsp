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

    <script type="text/javascript">
        $(document).ready(function () {
            var scannedSum = 0;
            var systemSum = 0;
            var varianceSum = 0;
            $('.scannedQty').each(function () {
                if($(this).attr('type')=='text'){
                    scannedSum = scannedSum + Number($(this).val());
                } else {
                    scannedSum = scannedSum + Number($(this).text());
                }
            });
            $("#scannedValue").html(scannedSum);
            $("#scannedValueClosed").html(scannedSum);

            $('.systemQty').each(function () {
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

        });
    </script>
</s:layout-component>
<s:layout-component name="content">

    <c:set value="<%= EnumCycleCountStatus.Closed.getId()%>" var="closed"/>
    <c:if test="${cycle.cycleCount.cycleStatus < closed }">
        <table style="margin: 80px auto 81px;">
        <thead>
        <tr>
            <th>VariantID</th>
            <th>Product Name</th>
            <th>Variant Option</th>
            <th>Batch</th>
            <th>Hk Barcode</th>
            <th>Mrp</th>
            <th>Mfg Date</th>
            <th>Expiry Date</th>
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
                    <td>${cCItem.skuGroup.sku.productVariant.product.name}</td>
                    <td>${cCItem.skuGroup.sku.productVariant.optionsPipeSeparated}</td>
                    <td>${cCItem.skuGroup.batchNumber}</td>
                    <td>${cCItem.skuGroup.barcode}</td>
                    <td> ${cCItem.skuGroup.mrp}</td>
                    <td><fmt:formatDate value="${cCItem.skuGroup.mfgDate}" type="date"/></td>
                    <td><fmt:formatDate value="${cCItem.skuGroup.expiryDate}" type="date"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${cycle.cycleCount.cycleStatus >= approved}">
                                <label class="scannedQty"> ${cCItem.scannedQty} </label>
                            </c:when>
                            <c:otherwise>
                                <shiro:hasPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
                                    <input type="text" name="cycleCountItems[${ctr.index}].scannedQty" class="scannedQty"  value="${cCItem.scannedQty}"/>
                                </shiro:hasPermission>
                                <shiro:lacksPermission name="<%=PermissionConstants.RECON_VOUCHER_MANAGEMENT%>">
                                    <label class="scannedQty"> ${cCItem.scannedQty} </label>
                                </shiro:lacksPermission>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:set value="${cycle.cycleCountPviMap}" var="item"/>
                    <td><label class="systemQty">${item[cCItem.id]}</label></td>

                    <c:choose>
                        <c:when test="${(cCItem.scannedQty) > (item[cCItem.id])}">
                            <td><span style="color:red"><label
                                    class="varianceQty">${(item[cCItem.id]) - (cCItem.scannedQty)}</label> </span></td>
                        </c:when>
                        <c:otherwise>
                            <td><label class="varianceQty">${(item[cCItem.id]) - (cCItem.scannedQty)}</label></td>
                        </c:otherwise>
                    </c:choose>

                </tr>

            </c:forEach>

            <c:forEach items="${cycle.missedSkuGroupList}" var="missedGroup">
                <tr>
                    <td>${missedGroup.sku.productVariant.id}</td>
                    <td>${missedGroup.sku.productVariant.product.name}</td>
                    <td>${missedGroup.sku.productVariant.optionsPipeSeparated}</td>
                    <td>${missedGroup.batchNumber}</td>
                    <td>${missedGroup.barcode}</td>
                    <td> ${missedGroup.mrp}</td>
                    <td><fmt:formatDate value="${missedGroup.mfgDate}" type="date"/></td>
                    <td><fmt:formatDate value="${missedGroup.expiryDate}" type="date"/></td>
                    <td>
                         <c:set var="scannedqty" value="0" />
                        <label class="scannedQty" style="color: #ff0000;">${scannedqty}</label>
                    </td>
                    <c:set value="${cycle.skuGroupSystemInventoryMap}" var="item"/>
                    <td><label class="systemQty">${item[missedGroup.id]}</label></td>
                    <td><label class="varianceQty">${item[missedGroup.id]}</label></td>
                </tr>
            </c:forEach>


            <tr>
                &nbsp; &nbsp;
                <td style="font-weight:BOLD;" colspan="8">Total</td>
                <td class="totalQuantity"><label id="scannedValue" style="font-weight:BOLD;"></label></td>
                <td><label style="font-weight:BOLD;" id="systemValue"></label></td>
                <td><label style="font-weight:BOLD;" id="varianceValue"></label></td>

            </tr>
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
                <th>Product Name</th>
                <th>Variant Option</th>
                <th>Scanned Qty</th>

            </tr>
            </thead>
            <div style="margin: 0px auto;text-align: center;">
                <c:forEach items="${cycle.cycleCountItems}" var="cCItem" varStatus="ctr">
                    <tr>
                        <td>${cCItem.skuGroup.sku.productVariant.id}</td>
                        <td>${cCItem.skuGroup.barcode}</td>
                        <td>${cCItem.skuGroup.sku.productVariant.product.name}</td>
                        <td>${cCItem.skuGroup.sku.productVariant.optionsCommaSeparated}</td>
                        <td><label class="scannedQty">${cCItem.scannedQty} </label></td>
                    </tr>
                </c:forEach>

                <c:forEach items="${cycle.missedSkuGroupList}" var="missedGroup">
                <tr>
                    <td>${missedGroup.sku.productVariant.id}</td>
                    <td>${missedGroup.barcode}</td>
                    <td>${missedGroup.sku.productVariant.product.name}</td>
                    <td>${missedGroup.sku.productVariant.optionsPipeSeparated}</td>
                    <td><c:set var="scannedqty" value="0" />
                        <label class="scannedQty" style="color: #ff0000;">${scannedqty}</label></td>
                 </tr>
                </c:forEach>
                <tr>
                    &nbsp; &nbsp;
                    <td style="font-weight:BOLD;" colspan="4">Total</td>
                    <td class="totalQuantity"><label id="scannedValueClosed" style="font-weight:BOLD;"></label></td>

                    </td>
                </tr>
            </div>
        </table>

    </c:if>

</s:layout-component>

</s:layout-render>