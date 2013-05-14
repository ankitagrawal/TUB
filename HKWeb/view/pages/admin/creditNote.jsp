<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CreditNoteAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Credit Note">
<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $(document).ready(function() {
            $('.addRowButton').click(function() {

                var lastIndex = $('.lastRow').attr('count');
                if (!lastIndex) {
                    lastIndex = -1;
                }
                $('.lastRow').removeClass('lastRow');
                var creditNoteId = $('#creditNoteId').val();
                var nextIndex = eval(lastIndex + "+1");
                var newRowHtml =
                        '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                                '<td class="sNo">' + Math.round(nextIndex + 1) + '.</td>' +
                                '<td>' +
                                '    <input type="hidden" name="creditNoteLineItems[' + nextIndex + '].id" />' +
                                '    <input type="text" class="variant" name="creditNoteLineItems[' + nextIndex + '].productVariant"/>' +
                                '    <input type="hidden" class="creditNotes" name="creditNoteLineItems[' + nextIndex + '].creditNote" value="' + creditNoteId + '" ' +
                                '  </td>' +
                                '<td></td>' +
                                '  <td class="pvDetails"></td>' +
                                '<td></td>' +
                                '  <td>' +
                                '    <input class="qty" type="text" name="creditNoteLineItems[' + nextIndex + '].qty" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="costPrice" type="text" name="creditNoteLineItems[' + nextIndex + '].costPrice" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="mrp" type="text" name="creditNoteLineItems[' + nextIndex + '].mrp" />' +
                                '  </td>' +
                                '</tr>';

                $('#poTable').append(newRowHtml);

                return false;
            });

            $('#save').click(function() {
                var obj = $(this);
                obj.hide();
                var bool = true;
                $('.pvDetails').each(function(){
                   var pvDetails = $(this).html();
                    if(pvDetails == null || pvDetails == ""){
                        alert("Enter Valid Product Variant");
                        bool = false;
                        return false;
                    }
                });
                if(bool){
                $('.qty').each(function(){
                  var qty = $(this).val();
                    if(qty == null || qty == "" || isNaN(qty)){
                        alert("Enter Valid Qty");
                        bool = false;
                        return false;
                    }
                });
                }
                if(bool){
                $('.costPrice').each(function(){
                    var costPrice=$(this).val();
                    if(costPrice==null || costPrice=="" || isNaN(costPrice)) {
                    alert("Cost Price is not valid");
                    bool = false;
                    return false;
                    }
                });
                }
                if(bool){
                $('.mrp').each(function(){
                   var mrp=$(this).val();
                   if(mrp==null || mrp == "" || isNaN(mrp)) {
                       alert("Mrp is not valid");
                       bool=false;
                       return false;
                   }
                });
                }
                if(!bool){
                    obj.show();
                   return false;
                    }
            });


            $('.variant').live("change", function() {
                var variantRow = $(this).parents('.lineItemRow');
                var productVariantId = variantRow.find('.variant').val();
                var productVariantDetails = variantRow.find('.pvDetails');
                var obj = $(this);
                var index = $(this).parents("tr").children(".sNo").html();
                index = index.replace(".","");
                index = parseInt(index) - 1;
                $.getJSON(
                        $('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${whAction.setWarehouse.id}},
                        function(res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                variantRow.find('.mrp').val(res.data.variant.markedPrice);
                                variantRow.find('.costPrice').val(res.data.variant.costPrice);
                                productVariantDetails.html(
                                        res.data.product + '<br/>' +
                                                res.data.options
                                );
                                obj.parent().append('<input type="hidden" name="creditNoteLineItems[' + index + '].sku" value="' + res.data.sku.id + '" />');
                            } else {
                                $('.variantDetails').html('<h2>'+res.message+'</h2>');
                            }

                        }
                );
            });


        });
    </script>
</s:layout-component>
<s:layout-component name="heading">
    Create/Edit Credit Note # ${pa.creditNote.id}
</s:layout-component>
<s:layout-component name="content">
    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
    </div>

    <s:form beanclass="com.hk.web.action.admin.inventory.CreditNoteAction">
        <s:hidden name="creditNote" value="${pa.creditNote.id}" id="creditNoteId"  />
        <s:hidden name="creditNote.user" value="${pa.creditNote.user.id}" />
        <table>
            <tr>
                <td>Customer Name</td>
                <td>
                    ${pa.creditNote.user.name}</td>
                <c:set var="b2bUserDetails" value="${hk:getB2bUserDetails(pa.creditNote.user)}"/>
                <td>TIN#</td>
                <td>
                    ${b2bUserDetails.tin}</td>
               <td>State</td>
                <td>
                    ${hk:getStateFromTin(b2bUserDetails.tin)}</td>

            </tr>
            <tr>
                <td>Create Date</td>
                <td>${pa.creditNote.createDt}</td>
                <td>Credit to Customer</td>
                <td><s:checkbox name="creditNote.userCredited"/></td>
                <td>Status</td>
                <td><s:select name="creditNote.creditNoteStatus" value="${pa.creditNote.creditNoteStatus.id}">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="creditNoteStatusList"
                                               value="id" label="name"/>
                </s:select></td>

            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                    <s:hidden name="creditNote.warehouse" value="${whAction.setWarehouse.id}"/>
                        ${whAction.setWarehouse.identifier}
                </td>
                <td>Remarks</td>
                <td><s:textarea name="creditNote.remarks" style="height:50px;"/></td>
            </tr>
        </table>

        <table border="1">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>VariantID</th>
                <th>UPC</th>
                <th>Details</th>
                <th>Tax<br/>Category</th>
                <th>Credit Qty</th>
                <th>Cost Price<br/>(Without TAX)</th>
                <th>MRP</th>
                <th>Taxable</th>
                <th>Tax</th>
                <th>Surcharge</th>
                <th>Payable</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="creditNoteLineItemDto" items="${pa.creditNoteDto.creditNoteLineItemDtoList}" varStatus="ctr">
                <c:set var="sku" value="${creditNoteLineItemDto.creditNoteLineItem.sku}"/>
                <c:set var="productVariant" value="${creditNoteLineItemDto.creditNoteLineItem.sku.productVariant}"/>
                <c:set var="creditNote" value="${creditNoteLineItemDto.creditNoteLineItem.creditNote}" />
                <s:hidden name="creditNoteLineItems[${ctr.index}].id" value="${creditNoteLineItemDto.creditNoteLineItem.id}"/>
                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                    <td class="sNo">${ctr.index+1}.</td>

                    <td>
                            ${productVariant.id}
                            <s:hidden name="creditNoteLineItems[${ctr.index}].creditNote" value="${creditNote.id}" />
                            <s:hidden name="creditNoteLineItems[${ctr.index}].sku" value="${sku.id}" />
                        <s:hidden class="variant" name="creditNoteLineItems[${ctr.index}].productVariant"
                                  value="${productVariant.id}"/>
                    </td>
                    <td>${productVariant.upc}</td>
                    <td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
                    </td>
                    <td>
                        <fmt:formatNumber value="${sku.tax.value * 100}"
                                          maxFractionDigits="2"/>
                    </td>
                    <td>
                        <s:text class="qty" name="creditNoteLineItems[${ctr.index}].qty" value="${creditNoteLineItemDto.creditNoteLineItem.qty}"/>
                    </td>
                    <td>
                        <shiro:hasRole name="<%=RoleConstants.FINANCE%>">
                            <s:text name="creditNoteLineItems[${ctr.index}].costPrice"
                                   class="costPrice" value="${creditNoteLineItemDto.creditNoteLineItem.costPrice}"/>
                        </shiro:hasRole>
                        <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
                            ${creditNoteLineItemDto.creditNoteLineItem.costPrice}
                            <s:hidden name="creditNoteLineItems[${ctr.index}].costPrice"
                                      value="${creditNoteLineItemDto.creditNoteLineItem.costPrice}"/>
                        </shiro:lacksRole>
                    </td>
                    <td>
                        <shiro:hasRole name="<%=RoleConstants.FINANCE%>">
                            <s:text class="mrp" name="creditNoteLineItems[${ctr.index}].mrp"
                                    value="${creditNoteLineItemDto.creditNoteLineItem.mrp}"/>
                        </shiro:hasRole>
                        <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
                            ${creditNoteLineItemDto.creditNoteLineItem.mrp}
                            <s:hidden class="mrp" name="creditNoteLineItems[${ctr.index}].mrp"
                                      value="${creditNoteLineItemDto.creditNoteLineItem.mrp}"/>
                        </shiro:lacksRole>
                    </td>
                    <td>
                        <fmt:formatNumber value="${creditNoteLineItemDto.taxable}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${creditNoteLineItemDto.tax}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${creditNoteLineItemDto.surcharge}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${creditNoteLineItemDto.payable}" maxFractionDigits="2"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="8">Total</td>
                <td><fmt:formatNumber value="${pa.creditNoteDto.totalTaxable}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${pa.creditNoteDto.totalTax}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${pa.creditNoteDto.totalSurcharge}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${pa.creditNoteDto.totalPayable}" maxFractionDigits="2"/></td>
            </tr>
            </tfoot>
        </table>
        <div class="variantDetails info"></div>
        <br/>
        <a href="creditNote.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

        <s:submit name="save" id="save" value="Save" />
    </s:form>

</s:layout-component>

</s:layout-render>
