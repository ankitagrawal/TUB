<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.constants.core.EnumTax" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.web.HealthkartResponse" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Extra Inventory">
<s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="extraInventory"/>


<s:layout-component name="htmlHead">

    <script type="text/javascript">
        $(document).ready(function () {

            $('#createRtv').live('click',function(){
                var bool = true;
                $('.checkbox1').each(function(){
                    if ($(this).attr("checked") == "checked") {
                        bool = false;
                    }
                });
                if(bool){
                    alert("Please select Line Items to create RTV / GRN");
                    return false;
                }
            });

            $('.variantId').live('change',function(){
                var variant = $(this).val();
                var obj = $(this);
                $.getJSON($('#skuCheck').attr('href'), {productVariantId: variant, wareHouseId: ${extraInventory.wareHouseId}}, function (res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>'){
                        obj.parent().parent().children('td.skuId').children('.skus').val(res.data.sku.id);
                    }
                    else{
                        alert(res.message);
                        obj.val("");
                        obj.parent().parent().children('td.skuId').children('.skus').val("");
                        return false;
                    }
                }
                        );
            });

            $(".variants").live('click',function(){
                var index = $(this).attr('value');
                $('.variants').each(function(){
                    var this_index = $(this).attr('value');
                    if(this_index == index){
                        //                        alert(index);
                        var txtBox = "<input type='text'  class='variantId' placeholder='Enter Variant ID'/>" ;
                        var txtBox2 = "<input type='text' class='skus' readonly='readonly' name='extraInventoryLineItems[" + index + "].sku' />" ;
                        var isChecked = $(this).attr('checked')?true:false;
                        if(isChecked){
                            $(this).parent().children('span').html('');
                            $(this).parent().parent().children('td.skuId').append(txtBox2);
                            $(this).parent().append(txtBox);
                        }
                        else{
                            $(this).parent().children('input[type="text"]').remove();
                            $(this).parent().parent().children('td.skuId').children('.skus').remove();
                            $(this).parent().children('span').html('check this if you know Variant Id :');
                        }
                    }
                });
            });

            $("#save").click(function(){
                var bool = true;
                $('.productName').each(function(){
                    var product = $(this).val();
                    if(product == null || product.trim(product) == ""){
                        alert("Product Name can't be left Empty");
                        bool = false;
                        return false;
                    }
                });
                $('.mrp').each(function(){
                    var mrp = $(this).val();
                    if(mrp==null || mrp.trim(mrp) == "" || isNaN(mrp)){
                        alert("Enter MRP in correct format.");
                        bool = false;
                        return false;
                    }
                });
                $('.costPrice').each(function(){
                    var costPrice = $(this).val();
                    if(costPrice == null || costPrice.trim(costPrice) == "" || isNaN(costPrice)){
                        alert("Enter Cost Price in correct format.");
                        bool = false;
                        return false;
                    }
                });
                $(".variantId").each(function(){
                    var variant = $(this).val();
                    var obj = $(this);
                    if(variant == null || variant.trim(variant) == ""){
                        alert("Variant Id can't be left Empty.");
                        bool = false;
                        return false;
                    }
                    else if(isNaN(variant)){
                        $.getJSON($('#skuCheck').attr('href'), {productVariantId: variant, wareHouseId: ${extraInventory.wareHouseId}}, function (res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>'){
                                obj.parent().parent().children('td.skuId').children('.skus').val(res.data.sku.id);
                            }
                            else{
                                alert(res.message);
                                obj.val("");
                                obj.parent().parent().children('td.skuId').children('.skus').val("");
                                bool = false;
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
                        return false;
                    }
                });
                if(!bool) return false;
            });


            $('#addRowButton').click(function () {

                var lastIndex = $('.lastRow').attr('count');
                if (!lastIndex) {
                    lastIndex = -1;
                }

                $('.lastRow').removeClass('lastRow');

                var nextIndex = eval(lastIndex + "+1");

                var newRowHtml =
                        '<tr class="lastRow lineItemRow" count="' + nextIndex + '" >' +
                        '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                        '<td>' +
                        '</td>' +
                        '<td class="skuId">' +
                        '</td>' +
                        '<td class="txtSku"> <input type="checkbox" class="variants" value="' + nextIndex + '" /> <span> Check this if you know Variant Id: </span>' +
                        '</td>' +
                        '<input type="hidden" name="extraInventoryLineItems[' + nextIndex + '].id" />' +
                        '  <td>' +
                        '    <input type="text" class="productName" name="extraInventoryLineItems[' + nextIndex + '].productName" />' +
                        '  </td>' +
                        '  <td>' +
                        '    <input class="mrp valueChange" type="text" name="extraInventoryLineItems[' + nextIndex + '].mrp" />' +
                        '  </td>' +
                        '  <td>' +
                        '    <input class="costPrice valueChange" type="text" name="extraInventoryLineItems[' + nextIndex + '].costPrice" />' +
                        '  </td>' +
                        '  <td>' +
                        '    <input type="text" class="receivedQty valueChange" name="extraInventoryLineItems[' + nextIndex + '].receivedQty"  />' +
                        '  </td>' +
                        '<td>' +
                        ' <select class="taxSelect" name="extraInventoryLineItems[' + nextIndex + '].tax" >' +
                        '<option value="<%=EnumTax.NA.getValue()%>" ><%=EnumTax.NA.getName()%></option>' +
                        '<option value="<%=EnumTax.SERVICE_10_3.getValue()%>" ><%=EnumTax.SERVICE_10_3.getName()%> </option>' +
                        '<option value="<%=EnumTax.VAT_0.getValue()%>" > <%=EnumTax.VAT_0.getName()%> </option>' +
                        '<option value="<%=EnumTax.VAT_5.getValue()%>" ><%=EnumTax.VAT_5.getName()%></option>' +
                        '<option value="<%=EnumTax.VAT_12_5.getValue()%>" ><%=EnumTax.VAT_12_5.getName()%></option>' +
                        '<option value="<%=EnumTax.VAT_12_36.getValue()%>" ><%=EnumTax.VAT_12_36.getName()%></option>' +
                        ' </select>' +
                        '</td>' +
                        '</tr>';

                $('#poTable').append(newRowHtml);

                return false;
            });

            $('.valueChange').live("change", function () {
                var value = $(this).val();
                if(value.trim(value) =="" || value == null){
                    alert("All fields are compulsory.");
                    return false;
                }
                if(isNaN(value)){
                    alert("Enter value in correct format.");
                    return false;
                }
            });

        });
    </script>
</s:layout-component>
<s:layout-component name="content">
    <h2>Purchase Order # ${extraInventory.purchaseOrderId}</h2>
    <c:choose>
        <c:when test="${fn:length(extraInventory.extraInventoryLineItems)!=0}">
            <h4 style ="color:blue"> Edit Extra Inventory Line Items  </h4>
        </c:when>
        <c:otherwise>
            <h4 style ="color:blue"> Create Extra Inventory Line Items  </h4>
        </c:otherwise>
    </c:choose>

    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" id="skuCheck" event="getSku"></s:link>
    </div>
    <br><br>
    <s:form beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" >
        <h2>Extra Inventory</h2>
        <table>
            <thead>
            <tr>
                <th>Extra Inventory Id</th>
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
                        <td>${extraInventory.extraInventory.createdBy.name}</td>
                        <td>${extraInventory.extraInventory.createDate}</td>
                        <td>${extraInventory.extraInventory.updateDate}</td>
                        <td><textarea name = "comments" rows="10" cols="10">${extraInventory.extraInventory.comments}</textarea></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><textarea name = "comments" rows="10" cols="20"></textarea></td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <s:hidden name="extraInventoryId" value="${extraInventory.extraInventory.id}"/>
        <br><br>
        <div class="clear"></div>
        <h2>Extra Inventory Line Items</h2>
        <table border="1">
            <thead>
            <tr>
                <th>S.No</th>
                <th>ID</th>
                <th>SKU ID</th>
                <th>Variant ID</th>
                <th>Product Name</th>
                <th>MRP</th>
                <th>Cost Price</th>
                <th>Received QTY</th>
                <th>TAX</th>
            </tr>
            </thead>
            <tbody id="poTable">
            <c:if test="${extraInventory.extraInventoryLineItems!=null}">
                <c:forEach items="${extraInventory.extraInventoryLineItems}" var="eInLineItems" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${ctr.index+1}.</td>
                        <td>
                            <c:set var="bool" value="0"/>
                            <c:if test="${eInLineItems.rtvCreated}">
                                ${eInLineItems.id}(RTV Created)
                                <s:hidden name="extraInventoryLineItems[${ctr.index}].rtvCreated" value="${eInLineItems.rtvCreated}"/>
                                <c:set var="bool" value="1"/>
                            </c:if>
                            <c:if test="${eInLineItems.grnCreated}">
                                ${eInLineItems.id}(GRN Created)
                                <s:hidden name="extraInventoryLineItems[${ctr.index}].grnCreated" value="${eInLineItems.grnCreated}"/>
                                <c:set var="bool" value="1"/>
                            </c:if>
                            <c:if test="${bool eq '0'}">
                                <s:checkbox class="checkbox1" value="${eInLineItems.id}" name="extraInventoryLineItemsSelected[${ctr.index}].id"/>${eInLineItems.id}
                            </c:if>
                            <s:hidden name="extraInventoryLineItems[${ctr.index}].id" value="${eInLineItems.id}"/>
                        </td>
                        <td class="skuId">
                            <c:choose>
                                <c:when test="${eInLineItems.rtvCreated or eInLineItems.grnCreated}">
                                    ${eInLineItems.sku.id}
                                    <s:hidden name="extraInventoryLineItems[${ctr.index}].sku" value="${eInLineItems.sku.id}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${eInLineItems.sku !=null}">
                                        <s:text name="extraInventoryLineItems[${ctr.index}].sku" class="skus" readonly="readonly" value="${eInLineItems.sku.id}"/>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="txtSku">
                            <c:choose>
                                <c:when test="${eInLineItems.rtvCreated or eInLineItems.grnCreated}">
                                    ${eInLineItems.sku.productVariant.id}
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${eInLineItems.sku !=null}">
                                            <input type="text" class='variantId' value="${eInLineItems.sku.productVariant.id}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="variants" value="${ctr.index}"> <span>check this if you know Variant Id :</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <input type="text" class="productName" name="extraInventoryLineItems[${ctr.index}].productName" value="${eInLineItems.productName}"/>
                        </td>
                        <td>
                            <input type="text" class="mrp valueChange" name="extraInventoryLineItems[${ctr.index}].mrp" value="${eInLineItems.mrp}"/>
                        </td>
                        <td>
                            <input type="text" class="costPrice valueChange" name="extraInventoryLineItems[${ctr.index}].costPrice" value="${eInLineItems.costPrice}"/>
                        </td>
                        <td>
                            <input type="text" class="receivedQty valueChange" name="extraInventoryLineItems[${ctr.index}].receivedQty" value="${eInLineItems.receivedQty}"/>
                        </td>
                        <td>
                            <select class="taxSelect" name="extraInventoryLineItems[${ctr.index}].tax">
                                <option value="<%=EnumTax.NA.getValue()%>" ${eInLineItems.tax.value == 0.0 ? 'selected' : ''}> <%=EnumTax.NA.getName()%> </option>
                                <option value="<%=EnumTax.SERVICE_10_3.getValue()%>" ${eInLineItems.tax.value == 0.103 ? 'selected' : ''}> <%=EnumTax.SERVICE_10_3.getName()%> </option>
                                <option value="<%=EnumTax.VAT_0.getValue()%>" ${eInLineItems.tax.value == 0.0 ? 'selected' : ''}> <%=EnumTax.VAT_0.getName()%> </option>
                                <option value="<%=EnumTax.VAT_12_36.getValue()%>" ${eInLineItems.tax.value == 0.1236 ? 'selected' : ''}> <%=EnumTax.VAT_12_36.getName()%> </option>
                                <option value="<%=EnumTax.VAT_12_5.getValue()%>" ${eInLineItems.tax.value == 0.125 ? 'selected' : ''}> <%=EnumTax.VAT_12_5.getName()%> </option>
                                <option value="<%=EnumTax.VAT_5.getValue()%>" ${eInLineItems.tax.value == 0.05 ? 'selected' : ''}> <%=EnumTax.VAT_5.getName()%> </option>
                            </select>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <c:choose>
            <c:when test="${extraInventory.reconciledStatus==null or (extraInventory.reconciledStatus!=null and !extraInventory.reconciledStatus eq 'reconciled')}">
                <a href="extraInventoryItems.jsp?purchaseOrderId=${extraInventory.purchaseOrderId}&wareHouseId=${extraInventory.wareHouseId}#" id="addRowButton" style="font-size:1.2em">Add new row</a>
            </c:when>
            <c:otherwise>
                <h4 style="color:blue;">RTV Status Closed</h4>
            </c:otherwise>
        </c:choose>
        <br/>
        <s:hidden name="wareHouseId" value="${extraInventory.wareHouseId}" />
        <s:hidden name="purchaseOrderId" value="${extraInventory.purchaseOrderId}" />
        <s:submit name="save" value="SAVE" id="save" />
        <c:if test="${extraInventory.reconciledStatus==null or (extraInventory.reconciledStatus!=null and !extraInventory.reconciledStatus eq 'reconciled')}">
            <s:submit name="createRtv" value="Create RTV" id="createRtv" />
        </c:if>
        <s:submit name="editRtv" value="Check RTV Status" />
        <s:submit name="createGRN" value="Create GRN" id="createRtv" />
        <s:submit name="editGRN" value="Check GRN Status"/>
    </s:form>
    <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="pre">
        <div align="center" style="font-weight:bold; font-size:150%">BACK</div>
    </s:link>
    <div class="clear"></div>

</s:layout-component>
</s:layout-render>