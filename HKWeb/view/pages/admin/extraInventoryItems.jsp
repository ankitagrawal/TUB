<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.core.Surcharge" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.core.Tax" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.rtv.EnumExtraInventoryStatus" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Extra Inventory">
<s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="extraInventory"/>

	<%

	TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
	List<Tax> taxList = taxDao.getTaxList();
	pageContext.setAttribute("taxList", taxList);

	MasterDataDao masterDataDao = (MasterDataDao) ServiceLocatorFactory.getService(MasterDataDao.class);
    response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    response.setHeader("pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    List<Tax> surchargeList = taxDao.getCentralTaxList();
	pageContext.setAttribute("surchargeList", surchargeList);
%>
	<s:layout-component name="htmlHead">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
<script type="text/javascript">
$(document).ready(function () {

    $('.createRtv').live('click', function() {
        var bool = true;
        $('.checkbox1').each(function() {
            if ($(this).attr("checked") == "checked") {
                bool = false;
            }
        });
        if (bool) {
            alert("Please select Line Items to create RTV / PO");
            return false;
        }
        if (confirm('Are you sure you want to create RTV / PO for selected items?')) {
            $(this).hide();
        }
        else {
            return false;
        }
    });
    
    $('.createShort').live('click', function() {
        var bool = true;
        $('.checkbox1').each(function() {
            if ($(this).attr("checked") == "checked") {
                bool = false;
            }
        });
        if (bool) {
            alert("Please select Line Items to create Short");
            return false;
        }
        if (confirm('Are you sure you want to create Short for selected items?')) {
        	$(this).hide();
        }
        else {
            return false;
        }
    });

    $('.variantId').live('change', function() {
        var variant = $(this).val();
        var obj = $(this);
        $.getJSON($('#skuCheck').attr('href'), {wareHouseId: ${extraInventory.wareHouseId} , productVariantId: variant}, function (res) {
            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                obj.parent().parent().children('td.skuId').children('.skus').val(res.data.sku.id);
                var checkLength = res.data.productName;
                var finalProName = res.data.productName;
                obj.parent().parent().children('td.proName').children('.productName').val(finalProName);
                obj.parent().parent().children('td.taxClass').children('.taxValue').val(res.data.taxId);
                $('#checkRtvStatus').remove();
                $('.createRtv').remove();
            }
            else {
                alert(res.message);
                obj.val("");
                obj.parent().parent().children('td.skuId').children('.skus').val("");
                return false;
            }
        }
                );
    });

    $(".variants").live('click', function() {
        var index = $(this).attr('value');
        $('.variants').each(function() {
            var this_index = $(this).attr('value');
            if (this_index == index) {
                var txtBox = "<input type='text'  class='variantId' placeholder='Enter Variant ID'/>";
                var txtBox2 = "<input type='text' class='skus' readonly='readonly' name='extraInventoryLineItems[" + index + "].sku' />";
                var isChecked = $(this).attr('checked') ? true : false;
                if (isChecked) {
                    $(this).parent().children('span').html('');
                    $(this).parent().parent().children('td.skuId').append(txtBox2);
                    $(this).parent().append(txtBox);
                }
                else {
                    $(this).parent().children('input[type="text"]').remove();
                    $(this).parent().parent().children('td.skuId').children('.skus').remove();
                    $(this).parent().children('span').html('check this if you know Variant Id :');
                }
            }
        });
        $('#checkRtvStatus').remove();
        $('.createRtv').remove();
    });

    $(".save").click(function(event) {
    	var id = event.target.id;
        var saveObj = $(this);
        //$(this).hide();
        var bool = true;
        $('.productName').each(function() {
            var product = $(this).val();
            if (product == null || product.trim(product) == "") {
                alert("Product Name can't be left Empty");
                bool = false;
                saveObj.show();
                return false;
            }
        });
        $('.mrp').each(function() {
            var mrp = $(this).val();
            if (mrp == null || mrp.trim(mrp) == "" || isNaN(mrp)) {
                alert("Enter MRP in correct format.");
                bool = false;
                saveObj.show();
                return false;
            }
        });
        $('.receivedQuantity').each(function() {
            var receivedQuantity = $(this).val();
            if (receivedQuantity == null || receivedQuantity.trim(receivedQuantity) == "" || isNaN(receivedQuantity)) {
                alert("Enter Received Quantity in correct format.");
                bool = false;
                saveObj.show();
                return false;
            }
        });
        $('.costPrice').each(function() {
            var costPrice = $(this).val();
            if (costPrice == null || costPrice.trim(costPrice) == "" || isNaN(costPrice)) {
                alert("Enter Cost Price in correct format.");
                bool = false;
                saveObj.show();
                return false;
            }
            costPrice = parseFloat(costPrice);
            var mrp = $(this).parent().parent().children('td').children('.mrp').val();
            mrp = parseFloat(mrp);
            if (mrp <= costPrice) {
                alert("MRP can't be less than Cost Price");
                bool = false;
                $(this).val("");
                $(this).parent().parent().children('td').children('.mrp').val("");
                saveObj.show();
                return false;
            }

        });
        
        if(id=="createShort"){
        $('.checkbox1').each(function(){
        	var state = $(this).is(':checked'); //state = true/false depending on state
            if (state){
        		var productVariant = $(this).parent().parent().children('td').children('.variantId').val();
        		if(productVariant==null||productVariant.trim(productVariant) == ""){
        			alert("SHORT can not be created if Variant is Unknown!!");
                    bool = false;
                    return false;
        		}
        	}
        });
        }
        $(".variantId").each(function() {
            var variant = $(this).val();
            var obj = $(this);
            if (variant == null || variant.trim(variant) == "") {
                alert("Variant Id can't be left Empty.");
                bool = false;
                saveObj.show();
                return false;
            }
            else if (isNaN(variant)) {
                $.getJSON($('#skuCheck').attr('href'), {productVariantId: variant, wareHouseId: ${extraInventory.wareHouseId}}, function (res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        obj.parent().parent().children('td.skuId').children('.skus').val(res.data.sku.id);
                        var checkLength = res.data.productName;
                        var finalProName = res.data.productName;
                        if (checkLength.length > 45) {
                            finalProName = finalProName.substring(0, 44);
                        }
                        obj.parent().parent().children('td.proName').children('.productName').val(finalProName);
                        obj.parent().parent().children('td.taxClass').children('.taxValue').val(res.data.taxId);
                    }
                    else {
                        alert(res.message);
                        obj.val("");
                        obj.parent().parent().children('td.skuId').children('.skus').val("");
                        bool = false;
                        saveObj.show();
                        return false;
                    }
                }
                        );
            }
        });
        $('.receivedQty').each(function () {
            var quantity = $(this).val();
            if (quantity == null || quantity.trim(quantity) == "" || isNaN(quantity)) {
                alert("Please enter a valid quantity.");
                bool = false;
                saveObj.show();
                return false;
            }
        });
        var extraInventoryStatus = parseFloat($('#extraInventoryStatus').val());
        var extraInventoryStatusDB = parseFloat(${extraInventory.extraInventory.extraInventoryStatus});
        if (extraInventoryStatusDB > extraInventoryStatus) {
            alert("Invalid ExtraInventory Status");
            saveObj.show();
            return false;
        }
        if(extraInventoryStatus == 40 && extraInventoryStatusDB!=40){
            $('.checkbox1').each(function() {
                alert("Extra Inventory Status can't be closed until all ExtraInventoryLineItems is RTV or PO.");
                bool  = false;
                saveObj.show();
                return false;
          });
        }
        if(extraInventoryStatusDB==10 && extraInventoryStatus == 40){
            alert("Status can't directly change from created to closed.");
            saveObj.show();
            return false;
        }
        if (!bool) return false;
        
        $('#eiForm').submit(function() {
			  $(this).find(':input:disabled') 
			    .prop('disabled', false); 
			});
    });


    $('#addRowButton').click(function () {

        var lastIndex = $('.lastRow').attr('count');
        if (!lastIndex) {
            lastIndex = -1;
        }

        $('.lastRow').removeClass('lastRow');

        var nextIndex = eval(lastIndex + "+1");
        var taxOptions = "";
	<c:choose>
	<c:when test="${extraInventory.sameState}">
		taxOptions = '<select class="taxCategory valueChange" name="extraInventoryLineItems[' + nextIndex + '].tax">';
	<c:forEach items="${taxList}" var="tax">
		taxOptions += '<option value="'+${tax.id}+
		'">'+${tax.value}+
		'</option>';
	</c:forEach>
	</c:when>
	<c:otherwise>
		taxOptions = '<select class="taxCategory valueChange" name="extraInventoryLineItems[' + nextIndex + '].tax">';
	<c:forEach items="${surchargeList}" var="surcharge">
		taxOptions += '<option value="'+${surcharge.id}+
		'">' + ${surcharge.value} + '</option>';
	</c:forEach>
	</c:otherwise>
	</c:choose>
	
        var newRowHtml =
                '<tr class="lastRow lineItemRow" count="' + nextIndex + '" >' +
                '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                '<td>' +
                '</td>' +
                '<td>' +
                '</td>' +
                '<td class="skuId">' +
                '</td>' +
                '<td class="txtSku"> <input type="checkbox" class="variants" value="' + nextIndex + '" /> <span> Check this if you know Variant Id: </span>' +
                '</td>' +
                '<input type="hidden" name="extraInventoryLineItems[' + nextIndex + '].id" />' +
                '  <td class="proName">' +
                '    <input type="text" class="productName" maxlength="45" name="extraInventoryLineItems[' + nextIndex + '].productName" />' +
                '  </td>' +
                '  <td>' +
                '    <input class="mrp valueChange" type="text" name="extraInventoryLineItems[' + nextIndex + '].mrp" />' +
                '  </td>' +
                '  <td>' +
                '    <input class="costPrice valueChange" type="text" name="extraInventoryLineItems[' + nextIndex + '].costPrice" />' +
                '  </td>' +
                '  <td>' +
                '    <input type="text" class="receivedQuantity valueChange" name="extraInventoryLineItems[' + nextIndex + '].receivedQty"  />' +
                '  </td>' +
                '<td>' +
				taxOptions +
				'</select>' +
				'<input type="hidden" value="finance" class="taxIdentifier"/>' +
				'</td>' +
                '  <td ><input class="taxableAmount" type="text" readonly="readonly" name="extraInventoryLineItems[' + nextIndex + '].taxableAmount"/></td>' +
				'  <td ><input class="taxAmount" type="text" readonly="readonly" name="extraInventoryLineItems[' + nextIndex + '].taxAmount"></td>' +
				'  <td ><input class="surchargeAmount" type="text" readonly="readonly" name="extraInventoryLineItems[' + nextIndex + '].surchargeAmount"/></td>' +
				'  <td ><input class="payableAmount" type="text" readonly="readonly" name="extraInventoryLineItems[' + nextIndex + '].payableAmount"/></td>' +
                '<td>' +
                '    <textarea rows="10" cols="10" style="height:60px; width:210px;" name="extraInventoryLineItems[' + nextIndex + '].remarks" />' +
                '</td>' +
                /* '<td class="shortCheck"> <input type="checkbox" class="rowShortCheck" name="extraInventoryLineItems[' + nextIndex + '].id"/>' +
                '</td>' + */
                '</tr>';

        $('#poTable').append(newRowHtml);
        $('#checkRtvStatus').remove();
        $('.createRtv').remove();

        return false;
    });
    
    
    $('.valueChange').live("blur", function() {
		var table = $(this).parent().parent().parent();
		var valueChangeRow = $(this).parents('.lineItemRow');
		var costPrice = valueChangeRow.find('.costPrice').val();
		var mrp = valueChangeRow.find('.mrp').val();
		var qty = valueChangeRow.find('.receivedQuantity').val();
		var taxIdentifier = valueChangeRow.find('.taxIdentifier').val();
		var value = $(this).val();
	       if (value.trim(value) == "" || value == null) {
	            alert("All fields are compulsory.");
	            return false;
	       }
	       if (isNaN(value)) {
	           alert("Enter value in correct format.");
	           return false;
	        }
		if (isNaN(qty) || isNaN(costPrice) || qty < 0 || costPrice < 0) {
			alert("Enter values in correct format.");
			return false;
		}
		var taxCategory;
		if (taxIdentifier == 'finance') {
			var taxCat = valueChangeRow.find('.taxCategory');
			var selectedTax = $(taxCat).find('option:selected');
			taxCategory = selectedTax.text();
		} else {
			taxCategory = parseFloat(valueChangeRow.find('.taxCategory').val().trim());
		}
		var taxable = costPrice * qty;
		var surchargeCategory = 0.0;
		var stateIdentifier = $('.state').html();
		surchargeCategory = ${hk:getSurchargeValue(extraInventory.supplierState, extraInventory.warehouseState)};
		var tax = taxable * taxCategory;
		var surcharge = tax * surchargeCategory;
		var payable = surcharge + taxable + tax;

		valueChangeRow.find('.taxableAmount').val(taxable.toFixed(2));
		valueChangeRow.find('.taxAmount').val(tax.toFixed(2));
		valueChangeRow.find('.surchargeAmount').val(surcharge.toFixed(2));
		valueChangeRow.find('.payableAmount').val(payable.toFixed(2));
	});
    
    if(${(extraInventory.reconciledStatus!=null && extraInventory.reconciledStatus == 'reconciled')
    	||extraInventory.isPiReconciled}){
    	$("#eiForm :input").each(function(){
    		 $(this).attr('readonly', true);;
    		});
    	$("#eiForm .taxValues").each(function(){
			$(this).prop('disabled', true);
    		});
    }

});
</script>
</s:layout-component>
<s:layout-component name="content">
<h2>Purchase Order # ${extraInventory.purchaseOrderId}</h2>
<c:choose>
    <c:when test="${fn:length(extraInventory.extraInventoryLineItems)!=0}">
        <h4 style="color:blue"> Edit Extra Inventory Line Items/Report Short/Damaged </h4>
    </c:when>
    <c:otherwise>
        <h4 style="color:blue"> Create Extra Inventory Line Items </h4>
    </c:otherwise>
</c:choose>
<c:set var="exInStatus" value="<%=EnumExtraInventoryStatus.Closed.getName()%>"/>
<div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" id="skuCheck" event="getSku"></s:link>
</div>
<br><br>
<s:form beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" id="eiForm">
<h2>Extra Inventory</h2>
<table>
    <thead>
    <tr>
        <th>Extra Inventory Id</th>
        <th>Status</th>
        <th>Created By</th>
        <th>Create Date</th>
        <th>Update Date</th>
        <th>Comments</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${extraInventory.extraInventory!=null}">
            <tr>
                <td> ${extraInventory.extraInventory.id}</td>
                <td>
                    <c:choose>
                        <c:when test="${extraInventory.extraInventory.extraInventoryStatus.name eq exInStatus}">
                            <span style="color:blue;">(Closed)</span>
                            <s:hidden name="extraInventoryStatusId" value="<%=EnumExtraInventoryStatus.Closed.getId()%>" />
                        </c:when>
                        <c:otherwise>
                            <s:select name="extraInventoryStatusId" id="extraInventoryStatus">
                                <s:option
                                        value="<%=EnumExtraInventoryStatus.Created.getId()%>"><%=EnumExtraInventoryStatus.Created.getName()%>
                                </s:option>
                                <s:option
                                        value="<%=EnumExtraInventoryStatus.SentToCategory.getId()%>"><%=EnumExtraInventoryStatus.SentToCategory.getName()%>
                                </s:option>
                                <s:option
                                        value="<%=EnumExtraInventoryStatus.Closed.getId()%>"><%=EnumExtraInventoryStatus.Closed.getName()%>
                                </s:option>
                            </s:select>
                            <script type="text/javascript">
                                $('#extraInventoryStatus').val(${extraInventory.extraInventory.extraInventoryStatus});
                            </script>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${extraInventory.extraInventory.createdBy.name}</td>
                <td>${extraInventory.extraInventory.createDate}</td>
                <td>${extraInventory.extraInventory.updateDate}</td>
                <td><textarea name="comments" rows="10" cols="10"
                              style="height:60px; width:210px;">${extraInventory.extraInventory.comments}</textarea>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><textarea name="comments" rows="10" cols="10" style="height:60px; width:210px;"></textarea></td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
<input type="hidden" name="extraInventoryId" value="${extraInventory.extraInventory.id}"/>
<br><br>

<div class="clear"></div>
<h2>Extra Inventory Line Items</h2>
<table border="1">
    <thead>
    <tr>
        <th>S.No.</th>
        <th>ID</th>
        <th>New PO ID</th>
        <th>SKU ID</th>
        <th>Variant ID</th>
        <th>Product Name</th>
        <th>MRP</th>
        <th>Cost Price</th>
        <th>Received QTY</th>
        <c:choose>
			<c:when test="${extraInventory.sameState}">
				<th>Tax<br/>Category</th>
			</c:when>
			<c:otherwise>
				<th>Surcharge<br/>Category</th>
			</c:otherwise>
		</c:choose>
        <th>Taxable</th>
        <th>Tax</th>
        <th>Surcharge</th>
		<th>Payable</th>
        <th>Remarks</th>
    </tr>
    </thead>
    <tbody id="poTable">
    <c:if test="${extraInventory.extraInventoryLineItems!=null}">
        <c:forEach items="${extraInventory.extraInventoryLineItems}" var="eInLineItems" varStatus="ctr">
        <s:hidden name="extraInventoryLineItems[${ctr.index}].extraInventoryLineItemType.id" value="${eInLineItems.extraInventoryLineItemType.id}" />
            <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                <td>${ctr.index+1}.</td>
                <td>
                    <c:set var="bool" value="0"/>
                    <c:if test="${eInLineItems.rtvCreated}">
                        ${eInLineItems.id}(RTV)
                        <s:hidden name="extraInventoryLineItems[${ctr.index}].rtvCreated"
                                  value="${eInLineItems.rtvCreated}"/>
                        <c:set var="bool" value="1"/>
                    </c:if>
                    <c:if test="${eInLineItems.grnCreated}">
                        ${eInLineItems.id}(PO)
                        <s:hidden name="extraInventoryLineItems[${ctr.index}].grnCreated"
                                  value="${eInLineItems.grnCreated}"/>
                        <c:set var="bool" value="1"/>
                    </c:if>
                    <c:if test="${eInLineItems.extraInventoryLineItemType!=null && eInLineItems.extraInventoryLineItemType.name == 'Short'}">
                        ${eInLineItems.id}<label style="color: red">(SHORT)</label>
                        <s:hidden name="extraInventoryLineItems[${ctr.index}].extraInventoryLineItemType"
                                  value="${eInLineItems.extraInventoryLineItemType}"/>
                        <c:set var="bool" value="1"/>
                    </c:if>
                    <c:if test="${bool eq '0' and eInLineItems.id !=null}">
                        <shiro:hasPermission name="<%=PermissionConstants.PO_MANAGEMENT%>">
                            <s:checkbox class="checkbox1" value="${eInLineItems.id}"
                                        name="extraInventoryLineItemsSelected[${ctr.index}].id"/>${eInLineItems.id}
                        </shiro:hasPermission>
                    </c:if>
                    <input type="hidden" name="extraInventoryLineItems[${ctr.index}].id"
                           value="${eInLineItems.id}"/>
                </td>
                <td>
                    <c:if test="${extraInventory.newPurchaseOrderId!=null and eInLineItems.grnCreated}">
                        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction"
                                event="pre">${extraInventory.newPurchaseOrderId}
                            <s:param name="purchaseOrder" value="${extraInventory.newPurchaseOrderId}"/>
                        </s:link>
                    </c:if>
                </td>
                <td class="skuId">
                    <c:choose>
                        <c:when test="${eInLineItems.rtvCreated or eInLineItems.grnCreated or eInLineItems.extraInventoryLineItemType.name == 'Short'}">
                            ${eInLineItems.sku.id}
                            <s:hidden name="extraInventoryLineItems[${ctr.index}].sku"
                                      value="${eInLineItems.sku.id}"/>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${eInLineItems.sku !=null}">
                                <s:text name="extraInventoryLineItems[${ctr.index}].sku" class="skus"
                                        value="${eInLineItems.sku.id}"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="txtSku">
                    <c:choose>
                        <c:when test="${eInLineItems.rtvCreated or eInLineItems.grnCreated or eInLineItems.extraInventoryLineItemType.name == 'Short'}">
                            ${eInLineItems.sku.productVariant.id}
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${eInLineItems.sku !=null}">
                                    <input type="text" class='variantId'
                                           value="${eInLineItems.sku.productVariant.id}"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" class="variants" value="${ctr.index}"> <span>check this if you know Variant Id :</span>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="proName">
                    <c:choose>
                        <c:when test="${eInLineItems.grnCreated or eInLineItems.rtvCreated or eInLineItems.extraInventoryLineItemType.name == 'Short'}">
                            ${eInLineItems.productName}
                            <s:hidden class="productName" name="extraInventoryLineItems[${ctr.index}].productName"
                                      value="${eInLineItems.productName}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="productName"
                                   name="extraInventoryLineItems[${ctr.index}].productName"
                                   value="${eInLineItems.productName}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${eInLineItems.grnCreated or eInLineItems.rtvCreated}">
                            ${eInLineItems.mrp}
                            <s:hidden class="mrp valueChange" name="extraInventoryLineItems[${ctr.index}].mrp"
                                      value="${eInLineItems.mrp}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="mrp valueChange"
                                   name="extraInventoryLineItems[${ctr.index}].mrp" value="${eInLineItems.mrp}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${eInLineItems.grnCreated or eInLineItems.rtvCreated}">
                            ${eInLineItems.costPrice}
                            <s:hidden class="costPrice valueChange"
                                      name="extraInventoryLineItems[${ctr.index}].costPrice"
                                      value="${eInLineItems.costPrice}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="costPrice valueChange"
                                   name="extraInventoryLineItems[${ctr.index}].costPrice"
                                   value="${eInLineItems.costPrice}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${eInLineItems.grnCreated or eInLineItems.rtvCreated}">
                            ${eInLineItems.receivedQty}
                            <s:hidden class="receivedQuantity valueChange"
                                      name="extraInventoryLineItems[${ctr.index}].receivedQty"
                                      value="${eInLineItems.receivedQty}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="receivedQuantity valueChange"
                                   name="extraInventoryLineItems[${ctr.index}].receivedQty"
                                   value="${eInLineItems.receivedQty}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="taxClass taxCategory">
                <input type="hidden" value="finance"
					       class="taxIdentifier"/>
				<c:choose>
                        <c:when test="${(eInLineItems.grnCreated or eInLineItems.rtvCreated) && extraInventory.sameState}">
                        ${eInLineItems.tax.name}
                        <input type="hidden" name="extraInventoryLineItems[${ctr.index}].tax"
                                   readonly="readonly" value="${eInLineItems.tax.id}"/>
                        </c:when>
                        <c:when test="${(eInLineItems.grnCreated or eInLineItems.rtvCreated) && !extraInventory.sameState}">
                         ${eInLineItems.tax.value}
                        <input type="hidden" name="extraInventoryLineItems[${ctr.index}].tax"
                                   readonly="readonly" value="${eInLineItems.tax.id}"/>
                        </c:when>
					   <c:otherwise>    
                    <c:choose>                        
						<c:when test="${extraInventory.sameState}">
							<s:select name="extraInventoryLineItems[${ctr.index}].tax"
							          value="${eInLineItems.tax.id}" class="valueChange taxValues">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="taxList"
								                           value="id"
								                           label="name"/>
							</s:select>
						</c:when>
						<c:otherwise>
							<s:select name="extraInventoryLineItems[${ctr.index}].tax"
							          value="${eInLineItems.tax.id}" class="valueChange taxValues">
								<hk:master-data-collection service="<%=TaxDao.class%>"
								                           serviceProperty="centralTaxList" value="id"
								                           label="value"/>
							</s:select>
						</c:otherwise>
                    </c:choose>
                    </c:otherwise>
                    </c:choose>
                </td>
                
                <td>
				<s:text readonly="readonly" class="taxableAmount"
				        name="extraInventoryLineItems[${ctr.index}].taxableAmount"
				        value="${eInLineItems.taxableAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="taxAmount" name="extraInventoryLineItems[${ctr.index}].taxAmount"
				        value="${eInLineItems.taxAmount}"/>
			</td>
			<td>
				<s:text readonly="readonly" class="surchargeAmount"
				        name="extraInventoryLineItems[${ctr.index}].surchargeAmount"
				        value="${eInLineItems.surchargeAmount}"/>
			</td>
			<td>

				<s:text readonly="readonly" class="payableAmount"
				        name="extraInventoryLineItems[${ctr.index}].payableAmount"
				        value="${eInLineItems.payableAmount}"/>
			</td>
                
                <td>
                            <s:textarea name="extraInventoryLineItems[${ctr.index}].remarks" rows="10" cols="10" style="height:60px; width:210px;"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>
<c:choose>
    <c:when test="${!(extraInventory.extraInventory.extraInventoryStatus.name eq exInStatus) &&  !(extraInventory.reconciledStatus!=null and extraInventory.reconciledStatus eq 'reconciled')}">
        <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
            <a href="extraInventoryItems.jsp?purchaseOrderId=${extraInventory.purchaseOrderId}&wareHouseId=${extraInventory.wareHouseId}#"
               id="addRowButton" style="font-size:1.2em">Add new row</a>
        </shiro:hasPermission>
    </c:when>
    <c:otherwise>
        <h4 style="color:blue;">RTV Status Closed or Sent to Supplier or ExtraInventory Status Closed</h4>
    </c:otherwise>
</c:choose>
<br/>
<s:hidden name="wareHouseId" value="${extraInventory.wareHouseId}"/>
<s:hidden name="purchaseOrderId" value="${extraInventory.purchaseOrderId}"/>
<s:submit name="save" class="save" value="SAVE"/>
<shiro:hasPermission name="<%=PermissionConstants.PO_MANAGEMENT%>">
    <c:if test="${extraInventory.reconciledStatus==null or (extraInventory.reconciledStatus!=null and !extraInventory.reconciledStatus eq 'reconciled')}">
        <s:submit name="createRtv" value="Create RTV" class="requiredFieldValidator createRtv"/>
    </c:if>
    <c:if test="${extraInventory.reconciledStatus==null or (extraInventory.reconciledStatus!=null and !extraInventory.reconciledStatus eq 'reconciled')}">
        <s:submit id="createShort" name="createShort" class="requiredFieldValidator save createShort" value="Create Short"/>
    </c:if>
    <s:submit name="editRtv" class="save" value="Check RTV Status" id="checkRtvStatus"/>
    <s:submit name="createPO" value="Create PO" class="save createRtv"/>
</shiro:hasPermission>
</s:form>
<br/>
<br/>
<s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="pre">
    <div align="center" style="font-weight:bold; font-size:150%">BACK</div>
</s:link>
<div class="clear"></div>

</s:layout-component>
</s:layout-render>