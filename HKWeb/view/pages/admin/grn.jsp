<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.inventory.EnumGrnStatus" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.core.EnumPermission" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.GRNAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="GRN">
<c:set var="GrnCloseId" value="<%=EnumGrnStatus.Closed.getId()%>"/>
<c:set var="PIForGrnCreatedId" value="<%=EnumGrnStatus.PICreated.getId()%>"/>
<%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
%>
<c:set var="inCheckedIn" value="<%=EnumGrnStatus.InventoryCheckedIn.getId()%>"/>
<s:layout-component name="htmlHead">

<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.lightbox-0.5.css" media="screen" />

<script type="text/javascript">
$(document).ready(function() {
    function checkvariantWeightonBlur(){
        var receivedQuantity = $(this).parents('.lineItemRow');
        var receivedQuantityValue   = receivedQuantity.find(".receivedQuantity").val();
        var variantRegex=/^[0-9]+$/;
        varWeightLength = $(this).val().length;
        varWeightValue = $(this).val();
        if(receivedQuantityValue!='0'){
            //alert(receivedQuantityValue);
                if (varWeightLength == 0){
                    alert("Variant weight cannot be empty!");
                    return false;

                }else if (isNaN(varWeightValue)) {
                    //alert(varWeightValue);
                    alert("Please enter variant weight in correct format!");
                    return false;
                }
                else if ((varWeightLength < 2) || (!variantRegex.test(varWeightValue))) {
                    alert(" Weight should be in grams!");
                    return false;
                }
                else{
                    return true;
                }
        }
    }
    function checkvariantWeightonSubmit(){

        var variantRegex=/^[0-9]+$/;
        var success = true;
            $(".weight").each(function() {
                    var receivedQuantity = $(this).parents('.lineItemRow');
                    var receivedQuantityValue   = receivedQuantity.find(".receivedQuantity").val();
                    //alert(receivedQuantityValue);
                    varWeightLength = $(this).val().length;
                    varWeightValue = $(this).val();
                    if(receivedQuantityValue!='0'){
                        if (varWeightLength == 0){
                            alert("Variant weight cannot be empty!");
                            success = false;

                        }else if (isNaN(varWeightValue)) {
                            //alert(varWeightValue);
                            alert("Please enter variant weight in correct format!");
                            success = false;
                        }
                        else if((varWeightLength < 2) || (!variantRegex.test(varWeightValue))) {
                            alert(" Weight should be in grams!");
                            success = false;
                        }
                        return success;
                    }
            });

            return success;
        }
    $('.weight').blur(checkvariantWeightonBlur);
    $('a.lightbox').lightBox({conPath:"${pageContext.request.contextPath}/"});

    $('.hkProductLightbox').each(function(){
        var valueChangeRow = $(this).parents('.lineItemRow');
        this.href = valueChangeRow.find('#mediumImage').attr('src');
    }).lightBox({conPath:"${pageContext.request.contextPath}/"});

    $('.addRowButton').click(function() {

        var lastIndex = $('.lastRow').attr('count');
        if (!lastIndex) {
            lastIndex = -1;
        }
        $('.lastRow').removeClass('lastRow');

        var nextIndex = eval(lastIndex + "+1");
        var newRowHtml =
                '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                        '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                        '<td></td>' +
                        '  <td>' +
                        '    <input type="hidden" name="grnLineItems[' + nextIndex + '].id" />' +
                        '    <input type="text" class="variant" name="grnLineItems[' + nextIndex + '].productVariant"/>' +
                        '  </td>' +
                        '<td></td>' +
                        '<td></td>' +
                        '<td></td>' +
                        '  <td class="pvDetails"></td>' +
                        '<td><input type="text" class="taxCategory" readonly="readonly" name="poLineItems[' + nextIndex + '].taxCategory"/></td>' +
                        '<td></td>' +
                        '  <td>' +
                        '    <input type="text" name="grnLineItems[' + nextIndex + '].qty" class="receivedQuantity valueChange" />' +
                        '  </td>' +
                        '  <td>' +
                        '    <input class="costPrice valueChange" type="text" name="grnLineItems[' + nextIndex + '].costPrice" />' +
                        '  </td>' +
                        '  <td>' +
                        '    <input class="mrp" type="text" name="grnLineItems[' + nextIndex + '].mrp" />' +
                        ' </td>'+
                        '  <td>' +
                        '    <input class="discountPercentage valueChange" type="text" name="grnLineItems[' + nextIndex + '].discountPercent" />' +
                        '  </td>'+
                        '  <td></td>'+
                        '  <td ><input class="taxableAmount" type="text" readonly="readonly" name="grnLineItems['+nextIndex+'].taxableAmount"/></td>' +
                        '  <td ><input class="taxAmount" type="text" readonly="readonly" name="grnLineItems['+nextIndex+'].taxAmount"></td>' +
                        '  <td ><input class="surchargeAmount" type="text" readonly="readonly" name="grnLineItems['+nextIndex+'].surchargeAmount"/></td>' +
                        '  <td ><input class="payableAmount" type="text" readonly="readonly" name="grnLineItems['+nextIndex+'].payableAmount"/></td>' +
                    /*'  <td ><label class="taxableAmount"></label></td>' +
                     '  <td ><label class="taxAmount"></label></td>' +
                     '  <td ><label class="surchargeAmount"></label></td>' +*/
                        '  </tr>';

        $('#poTable').append(newRowHtml);
        return false;
    });

    $('.variant').live("change", function() {
        var variantRow = $(this).parents('.lineItemRow');
        var productVariantId = variantRow.find('.variant').val();
        var productVariantDetails = variantRow.find('.pvDetails');
        $.getJSON(
                $('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${pa.grn.warehouse.id}},
                function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        variantRow.find('.mrp').val(res.data.variant.markedPrice);
                        variantRow.find('.costPrice').val(res.data.variant.costPrice);
                        productVariantDetails.html(
                                res.data.product + '<br/>' +
                                        res.data.options
                        );
                        variantRow.find('.taxCategory').val(res.data.tax);
                    } else {
                        $('.variantDetails').html('<h2>'+res.message+'</h2>');
                    }
                }
        );
    });
    $('.valueChange').live("change", function() {
        var valueChangeRow = $(this).parents('.lineItemRow');
        var costPrice = valueChangeRow.find('.costPrice').val();
        var mrp = valueChangeRow.find('.mrp').val();
        var qty = valueChangeRow.find('.receivedQuantity').val();
        var taxIdentifier = valueChangeRow.find('.taxIdentifier').val();
        if(qty=="" || costPrice=="" ){
            alert("All fields are compulsory.");
            return false;
        }
        if(isNaN(qty) || isNaN(costPrice) || qty<0 || costPrice<0){
            alert("Enter values in correct format.");
            return false;
        }
        var taxCategory = valueChangeRow.find('.taxCategory').val();
        if(taxCategory == null) {
            taxCategory = 0;
        }
        /*if (taxIdentifier == 'finance') {
         var taxCat = valueChangeRow.find('.taxCategory');
         var selectedTax = $(taxCat).find('option:selected');
         taxCategory = selectedTax.text();
         } else {
         taxCategory = parseFloat(valueChangeRow.find('.taxCategory').html().trim());
         }
         */
        if (isNaN(qty)) {
            alert("Enter Valid Quantity.");
            return;
        }
        var surchargeCategory = 0.0;
        var stateIdentifier = $('.state').html();
        if (stateIdentifier == 'CST' && taxCategory != 0) {
            surchargeCategory = 0.0;
            taxCategory = 0.02;
        } else {
            surchargeCategory = 0.05;
        }
        var taxable = costPrice * qty;
        var discountPercentage = valueChangeRow.find('.discountPercentage').val();
        var discountedAmount = 0.0;
        if (discountPercentage!=null && (isNaN(discountPercentage) || discountPercentage < 0)) {
            alert("Enter valid discount");
            return;
        }
        if(discountPercentage > 0) {
            discountedAmount = (discountPercentage / 100) * taxable;
        }
        taxable -= discountedAmount;
        var tax = taxable * taxCategory;
        var surcharge = tax * surchargeCategory;
        var payable = surcharge + taxable + tax;

        valueChangeRow.find('.taxableAmount').val(taxable.toFixed(2));
        valueChangeRow.find('.taxAmount').val(tax.toFixed(2));
        valueChangeRow.find('.surchargeAmount').val(surcharge.toFixed(2));
        valueChangeRow.find('.payableAmount').val(payable.toFixed(2));
        updateTotal('.receivedQuantity','.totalQuantity',1);
        updateTotal('.payableAmount', '.totalPayable',0);
        updateTotal('.taxableAmount','.totalTaxable',0);
        updateTotal('.taxAmount','.totalTax',0);
        updateTotal('.surchargeAmount','.totalSurcharge',0);

    });

    function updateTotal(fromTotalClass,toTotalClass,toHtml){
        //alert(fromTotalClass);
        var total=0;
        $.each($(fromTotalClass),function(index,value){
            var eachRow=$(value);
            var eachRowValue=eachRow.val().trim();
            total+=parseFloat(eachRowValue);
        });
        if(toHtml == 1){
            $(toTotalClass).html(total);
        } else {
            $(toTotalClass).val(total.toFixed(2));
        }
    };

    $('.validateWeight').click(function(event) {
       // console.log(checkvariantWeightonSubmit());
       
       var id = event.target.id;
       var value;
       $("#saveValue").val("");
       if(id == "saveAndContinue"){
    	   var el = document.getElementById('saveValue');
    	   el.value = 1;
    	   //<c:set var="saveValue" scope="page" value="1"/>;
       }else if(id=="saveAndClose"){
    	   var el = document.getElementById('saveValue');
    	   el.value = 2;
    	   //<c:set var="saveValue" scope="page" value="2"/>;
       }
        if(checkvariantWeightonSubmit()){
            $('.requiredFieldValidator').click();
        }else{
            return false;
        }

   });
    $('.requiredFieldValidator').click(function() {
        var invoice = $('.invoiceNumber').val();
        if(invoice == "-"){
            alert("Enter Invoice Number");
            return false;
        }

        var qty = $('.receivedQuantity').val();
        var costPrice = $('.costPrice').val();
        if(qty=="" || costPrice=="" || qty<0 || costPrice<0){
            alert("Enter values in correct format.");
            return false;
        }

        var invoiceDateString = $('#invoice-date').val();
        var dateValues = invoiceDateString.split("-");
        var invoiceDate = new Date();

        invoiceDate.setYear(parseInt(dateValues[0]));
        invoiceDate.setMonth(parseInt(dateValues[1]) - 1);
        invoiceDate.setDate(parseInt(dateValues[2]));
        if (invoiceDate > new Date()) {
            alert("Invoice date cannot be in future");
            return false;
        }


        var returnFalse = false;
        $.each($('.receivedQuantity'), function(index, value){
            var valueChangeRow = $(this).parents('.lineItemRow');
            var eachRow=$(value);

            var receivedQuantity = parseFloat(eachRow.val().trim());
            var alreadyGrnQty = parseFloat(valueChangeRow.find('#alreadyGRNQty').val());
            var poLineItemQty = parseFloat(valueChangeRow.find('#poLineItemQty').val());
            if( receivedQuantity + alreadyGrnQty > poLineItemQty){
                alert("GRN for " + valueChangeRow.find('#variantHidden').val() + " is more than PO. Please change it to save the GRN" );
                returnFalse = true;
                return false;
            }
        });
        if(returnFalse) {
            return false;
        }

        $(this).css("display", "none");
    } );

    updateTotal('.receivedQuantity','.totalQuantity',1);
});
</script>
</s:layout-component>
<s:layout-component name="heading">
    Edit GRN # ${pa.grn.id} of PO # ${pa.grn.purchaseOrder.id}
</s:layout-component>
<s:layout-component name="content">
<div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
</div>

<s:form beanclass="com.hk.web.action.admin.inventory.GRNAction">
<s:hidden name="grn" value="${pa.grn.id}"/>
<table>
    <tr>
        <td>Supplier Name</td>
        <td>${pa.grn.purchaseOrder.supplier.name}</td>

        <td>Supplier State</td>
        <td>${pa.grn.purchaseOrder.supplier.state}</td>

        <td>Tax</td>
        <td>
            <c:choose>
                <c:when test="${hk:equalsIgnoreCase(pa.grn.purchaseOrder.supplier.state, pa.grn.warehouse.state)}">
                    <label class="state">NON-CST</label>
                </c:when>
                <c:otherwise>
                    <label class="state">CST</label>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td>For Warehouse</td>
        <td>
            <s:hidden name="grn.warehouse" value="${pa.grn.warehouse}"/>
                ${pa.grn.warehouse.identifier}
        </td>
        <td>Credit Days</td>
        <td>${pa.grn.purchaseOrder.supplier.creditDays}</td>
    </tr>

    <tr>
        <td>GRN Date</td>
        <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="grn.grnDate"/></td>
        <td>Received By</td>
        <td>
                ${pa.grn.receivedBy.name}</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>Invoice Date <em class="mandatory">*</em></td>
        <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="grn.invoiceDate" id="invoice-date"/></td>
        <td>Invoice Number <em class="mandatory">*</em></td>
        <td><s:text name="grn.invoiceNumber" class="invoiceNumber"/></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>Payable</td>
        <td class="payable">
            <fmt:formatNumber value="${actionBean.grnDto.totalPayable}" type="currency" currencySymbol=" "
                              maxFractionDigits="0"/></td>
        <td>Est Payment Date</td>
        <td><fmt:formatDate value="${pa.grn.estPaymentDate}"/></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
            <%--<td>Reconciled</td>
                                <td><s:checkbox name="grn.reconciled"/></td>--%>
        <td>Status</td>
        <td>${pa.grn.grnStatus.name}
            <shiro:hasRole name="<%=RoleConstants.GOD%>">
                <s:select name="grn.grnStatus" value="${pa.grn.grnStatus.id}">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                               serviceProperty="grnStatusList"
                                               value="id" label="name"/>
                </s:select>
            </shiro:hasRole>
        </td>
        <td>Remarks</td>
        <td><s:textarea name="grn.remarks" style="height:50px;"/></td>
    </tr>
    <tr><td colspan="6" style="text-align:right;"><em class="mandatory">*</em> marked fields are mandatory</td></tr>

</table>

<table border="1">
    <thead>
    <tr>
        <th>S.No.</th>
        <th></th>
        <th>VariantID</th>
        <th>Supplier Code</th>
        <th>Remarks</th>
        <th>Details</th>
        <th>Tax<br/>Category</th>
        <th>Asked Qty</th>
        <th>Other<br> GRN qty</th>
        <th>Received Qty<br/>(Adjust -)</th>
        <th>Checkedin Qty</th>
        <th>Cost Price<br/>(Without TAX)</th>
        <th>MRP</th>
        <th>Discount<br/>(%)</th>
        <th>Margin(MRP vs CP)</th>
        <th>Taxable</th>
        <th>Tax</th>
        <th>Surcharge</th>
        <th>Payable</th>
        <th>Weight <br/>(in gms)</th>
        <th>Length <br/>(in cm)</th>
        <th>Breadth <br/>(in cm)</th>
        <th>Height <br/>(in cm)</th>

    </tr>
    </thead>
    <tbody id="poTable">
    <c:forEach var="grnLineItemDto" items="${pa.grnDto.grnLineItemDtoList}" varStatus="ctr">
        <c:set value="${grnLineItemDto.grnLineItem.sku}" var="sku"/>
        <c:set value="${sku.productVariant}" var="productVariant"/>
        <c:set value="${productVariant.product}" var="product"/>

        <s:hidden name="grnLineItems[${ctr.index}]" value="${grnLineItemDto.grnLineItem.id}"/>
        <c:set var="skumap" value="${pa.skuIsNew}"/>
        <c:set var="skumap" value="${pa.skuIsNew}"/>
        <c:choose>
            <c:when test="${skumap[grnLineItemDto.grnLineItem.sku] != null }">
                <tr style="background-color:goldenrod;"  count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow ':'lineItemRow '}">

            </c:when>
            <c:otherwise>
                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
            </c:otherwise>
        </c:choose>

        <%--<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow ':'lineItemRow'}">--%>
        <td>${ctr.index+1}.</td>
        <td>
            <div class='img48' style="vertical-align:top;">
                <c:choose>
                    <c:when test="${productVariant.product.mainImageId != null}">
                        <div style="display: none;">
                            <hk:productImage imageId="${productVariant.product.mainImageId}" size="<%=EnumImageSize.MediumSize%>" id="mediumImage"/>
                        </div>

                        <a href="#" class="hkProductLightbox">
                            <hk:productImage imageId="${productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/images/ProductImages/ProductImagesOriginal/${productVariant.product.id}.jpg" class="lightbox">
                            <img class="prod48"
                                 src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${productVariant.product.id}.jpg"
                                 alt="${productLineItem.productVariant.product.name}" /></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
        <td>
            <c:if test="${grnLineItemDto.grnLineItem.checkedInQty > 0 && pa.grn.grnStatus.id <  PIForGrnCreatedId}">
                <c:set var="itemCheckedin" value="true" />
                <ul>
                    <shiro:hasPermission name="<%=PermissionConstants.EDIT_GRN%>">
                        <s:link beanclass ="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="downloadBarcode"> Barcode
                            <%--<s:param name="grnLineItemId" value="${grnLineItemDto.grnLineItem.id}"/>--%>
                            <s:param name="grnLineItem" value="${grnLineItemDto.grnLineItem.id}"/>
                            <s:param name="grn" value="${pa.grn.id}"/>
                        </s:link>
                    </shiro:hasPermission>
                </ul>
            </c:if>
            <ul>
                    ${productVariant.id}
                <s:hidden class="variant" name="grnLineItems[${ctr.index}].productVariant"
                          value="${grnLineItemDto.grnLineItem.productVariant.id}"/>
            </ul>
                <%--<s:hidden class="sku" name="grnLineItems[${ctr.index}].sku"
                                     value="${sku}"></s:hidden>--%>
        </td>
        <td>${productVariant.supplierCode}</td>
        <td>${productVariant.otherRemark}</td>
        <td>${product.name}<br/>${productVariant.variantName}<br/>${productVariant.optionsCommaSeparated}
        </td>
        <td>
            <input type="text" class="taxCategory" value="${grnLineItemDto.grnLineItem.sku.tax.value}" disabled="disabled"/>
        </td>
        <%--<td class="taxCategory">
                   &lt;%&ndash;<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
                                                     <input type="hidden" value="finance"
                                                            class="taxIdentifier"/>
                                                     <s:select name="grnLineItems[${ctr.index}].sku.tax"
                                                               value="${grnLineItemDto.grnLineItem.sku.tax.id}" class="valueChange">
                                                         <hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList" value="id"
                                                                                    label="name"/>
                                                     </s:select>
                                                 </shiro:hasPermission>
                                                 <shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
                                                     ${grnLineItemDto.grnLineItem.sku.tax.value}
                                                 </shiro:lacksPermission>&ndash;%&gt;
                   ${grnLineItemDto.grnLineItem.sku.tax.value}
           </td>--%>
        <td>${hk:askedPOQty(pa.grn.purchaseOrder, productVariant)}</td>
        <td>
            <input type="text" id="alreadyGRNQty" value="${hk:getGrnLineItemQtyAlreadySet(grnLineItemDto.grnLineItem.goodsReceivedNote, grnLineItemDto.grnLineItem.sku)}" disabled="disabled"/>
            <input type="hidden" id="poLineItemQty" value="${hk:getPoLineItemQty(grnLineItemDto.grnLineItem)}"/>
            <input type="hidden" id="variantHidden" value="${productVariant.id}" />
        </td>
        <td>
            <s:text name="grnLineItems[${ctr.index}].qty" value="${grnLineItemDto.grnLineItem.qty}"
                    class="receivedQuantity valueChange"/>
        </td>
        <td>
                ${grnLineItemDto.grnLineItem.checkedInQty}
        </td>
        <td>
            <s:text name="grnLineItems[${ctr.index}].costPrice" value="${grnLineItemDto.grnLineItem.costPrice}"
                    class="costPrice valueChange"/>
        </td>
        <td>
            <s:text class="mrp" name="grnLineItems[${ctr.index}].mrp" value="${grnLineItemDto.grnLineItem.mrp}"/>
        </td>
        <td>
            <s:text class="discountPercentage valueChange" name="grnLineItems[${ctr.index}].discountPercent" value="${grnLineItemDto.grnLineItem.discountPercent}"/>
        </td>
        <td>
            <fmt:formatNumber value="${grnLineItemDto.marginMrpVsCP}" maxFractionDigits="2"/>
        </td>
        <td>
            <s:text readonly="readonly" class="taxableAmount" name="grnLineItems[${ctr.index}].taxableAmount" value="${grnLineItemDto.taxable}" />
        </td>
        <td>
            <s:text readonly="readonly" class="taxAmount" name="grnLineItems[${ctr.index}].taxAmount" value="${grnLineItemDto.tax}" />
        </td>
        <td>
            <s:text readonly="readonly" class="surchargeAmount" name="grnLineItems[${ctr.index}].surchargeAmount" value="${grnLineItemDto.surcharge}" />
        </td>
        <td>
            <s:text readonly="readonly" class="payableAmount" name="grnLineItems[${ctr.index}].payableAmount" value="${grnLineItemDto.payable}" />
        </td>
        <td><s:text name="grnLineItems[${ctr.index}].weight" id="variantWeight" class="weight" value="${grnLineItemDto.grnLineItem.weight}"/></td>
        <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.length" value="${productVariant.length}"/></td>
        <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.breadth" value="${productVariant.breadth}"/></td>
        <td><s:text name="grnLineItems[${ctr.index}].sku.productVariant.height" value="${productVariant.height}"/></td>
        <%--<td>--%>
        <%--<s:link  beanclass="com.hk.web.action.admin.inventory.GRNAction" event="deleteGrnLineItem"><span style="color:#ff0000;">delete</span>--%>
        <%--<s:param name="grnLineItem" value="${grnLineItemDto.grnLineItem.id}"/>--%>
        <%--</s:link>--%>
        <%--</td>--%>
        </tr>
    </c:forEach>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="9">Total</td>
        <td colspan="6" class="totalQuantity"></td>
        <td><s:text readonly="readonly" class="totalTaxable" name="grn.taxableAmount" value="${pa.grn.taxableAmount}" /></td>
        <td><s:text readonly="readonly" class="totalTax" name="grn.taxAmount" value="${pa.grn.taxAmount}" /></td>
        <td><s:text readonly="readonly" class="totalSurcharge" name="grn.surchargeAmount" value="${pa.grn.surchargeAmount}"/></td>
        <td><s:text readonly="readonly" class="totalPayable" name="grn.payable" value="${pa.grn.payable}"/></td>
    </tr>
        <%--<tr>
         <td colspan="16"></td><td>Overall Discount<br/>(In Rupees)</td>
         <td><s:text class="overallDiscount footerChanges" name="grn.discount" value="${pa.grn.discount}"/></td>
     </tr>
     <tr>
     <tr>
         <td colspan="16"></td><td>Final Payable</td>
         <td><s:text readonly="readonly" class="finalPayable" name="grn.finalPayableAmount" value="${pa.grn.finalPayableAmount}"/></td>
     </tr>--%>
    </tfoot>
</table>
<div class="variantDetails info"></div>
<br/>
<%--<a href="grn.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>--%>
<shiro:hasPermission name="<%=PermissionConstants.EDIT_GRN%>">
	<s:submit name="save" value="Save" class="requiredFieldValidator" style="display:none;"/>
    <s:button name="validateWeight" id="saveAndContinue" value="Save And Continue" class="validateWeight" />
    <s:button name="validateWeight" id="saveAndClose" value="Save And Close" class="validateWeight" />
    <s:hidden name="saveValue" id="saveValue"/>

    <c:if test='${itemCheckedin && pa.grn.grnStatus.id == GrnCloseId}' >
        <s:link class=" button_green" style="width: 180px; height: 18px; align_right" beanclass ="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="downloadAllBarcode"> Get All Barcodes
            <s:param name="grn" value="${pa.grn.id}"/>
        </s:link>

    </c:if>
</shiro:hasPermission>
<%--<c:choose>
	<c:when test="${pa.grn.grnStatus.id < inCheckedIn}">
		<s:submit name="save" value="Save" class="requiredFieldValidator"/>
	</c:when>
	<c:otherwise>
		<shiro:hasRole name="<%=RoleConstants.GOD%>">
			<s:submit name="save" value="Save" class="requiredFieldValidator"/>
		</shiro:hasRole>
	</c:otherwise>
</c:choose>--%>

</s:form>

</s:layout-component>

</s:layout-render>
