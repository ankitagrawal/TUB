<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Debit Note">
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
                var debitNoteId = $('#debitNoteId').val();
                var nextIndex = eval(lastIndex + "+1");
                var newRowHtml =
                        '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                                '<td class="sNo">' + Math.round(nextIndex + 1) + '.</td>' +
                                '<td>' +
                                '    <input type="hidden" name="debitNoteLineItems[' + nextIndex + '].id" />' +
                                '    <input type="text" class="variant" name="debitNoteLineItems[' + nextIndex + '].productVariant"/>' +
                                '    <input type="hidden" class="debitNotes" name="debitNoteLineItems[' + nextIndex + '].debitNote" value="' + debitNoteId + '" ' +
                                '  </td>' +
                                '<td></td>' +
                                '  <td class="pvDetails"></td>' +
                                '<td></td>' +
                                '  <td>' +
                                '    <input class="qty" type="text" name="debitNoteLineItems[' + nextIndex + '].qty" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="costPrice" type="text" name="debitNoteLineItems[' + nextIndex + '].costPrice" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="mrp" type="text" name="debitNoteLineItems[' + nextIndex + '].mrp" />' +
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
                var debitNoteStatus = parseFloat($('#debitNoteStatusId').val());
                var debitNoteStatusDB = parseFloat(${pa.debitNote.debitNoteStatus});
                if (debitNoteStatusDB > debitNoteStatus) {
                    alert("Invalid Debit Note Status");
                    obj.show();
                    bool=false;
                    return false;
                }
                
                if(!bool){
                    obj.show();
                   return false;
                    }
            });
			
            
            var debitNoteStatusDB = parseFloat(${pa.debitNote.debitNoteStatus});
            if (debitNoteStatusDB >=50){
            	$("#debitNoteLineItems :input").each(function(){
   	    		 $(this).attr('readonly', true);
   	    		});
            }
            
            $('#freightForwardingCharges').live("change", function(){
            	var table = $(this).parent().parent().parent();
            	var totalPayableRow = table.find('#totalPayable');
            	var totalPayable =  parseFloat(totalPayableRow.text().replace(",",""));
            	var freightForwardingCharges = parseFloat($(this).val());
            	var finalDebitAmount = totalPayable+freightForwardingCharges;
            	table.find('#finalDebitAmount').val(finalDebitAmount.toFixed(2));
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

                                obj.parent().append('<input type="hidden" name="debitNoteLineItems[' + index + '].sku" value="' + res.data.sku.id + '" />');
	                              obj.parent().append('<input type="hidden" name="debitNoteLineItems[' + index + '].tax" value="' + res.data.tax_id + '" />');
                            } else {
                                $('.variantDetails').html('<h2>'+res.message+'</h2>');
                            }

                        }
                );
            });
            
            if(${pa.debitNote.purchaseInvoice!=null}){
            	$(".addRowButton").hide();
            }


        });
    </script>
</s:layout-component>
<s:layout-component name="heading">
    Edit Debit Note # ${pa.debitNote.id}
    <c:if test="${pa.debitNote.purchaseInvoice!=null }">(Against PI # ${pa.debitNote.purchaseInvoice.id})</c:if> 
</s:layout-component>
<s:layout-component name="content">
    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
    </div>

    <s:form beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">
        <s:hidden name="debitNote" value="${pa.debitNote.id}" id="debitNoteId"  />
        <s:hidden name="debitNote.supplier" value="${pa.debitNote.supplier.id}" />
        <s:hidden name="debitNote.purchaseInvoice" value="${pa.debitNote.purchaseInvoice.id}" />
        <s:hidden name="debitNote.reconciliationVoucher" value="${pa.debitNote.reconciliationVoucher.id}" />
        <s:hidden name="debitNote.debitNotetype" value="${pa.debitNote.debitNoteType}"/>
        <table>
            <tr>
                <td>Supplier Name</td>
                <td>
                        ${pa.debitNote.supplier.name}</td>

                <td>Supplier State</td>
                <td>${pa.debitNote.supplier.state}</td>

                <td>Tax</td>
                <td>
                    <c:choose>
                        <c:when test="${pa.debitNote.supplier.state == 'HARYANA'}">
                            Non - CST
                        </c:when>
                        <c:otherwise>
                            CST
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>Create Date</td>
                <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="debitNote.createDate"/></td>
                <!-- <td>Debit to Supplier</td>
                <td><s:checkbox name="debitNote.isDebitToSupplier"/></td> -->
                <td>Status</td>
                <td><s:select name="debitNote.debitNoteStatus" id="debitNoteStatusId" value="${pa.debitNote.debitNoteStatus.id}">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="debitNoteStatusList"
                                               value="id" label="name"/>
                </s:select></td>

            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                    <s:hidden name="debitNote.warehouse" value="${whAction.setWarehouse.id}"/>
                        ${whAction.setWarehouse.identifier}
                </td>
                <td>Remarks</td>
                <td><s:textarea name="debitNote.remarks" style="height:50px;"/></td>
            </tr>
        </table>

        <table border="1" id="debitNoteLineItems">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>VariantID</th>
                <th>UPC</th>
                <th>Details</th>
                <th>Tax<br/>Category</th>
                <th>Debit Qty</th>
                <th>Cost Price<br/>(Without TAX)</th>
                <th>MRP</th>
                <th>Taxable</th>
                <th>Tax</th>
                <th>Surcharge</th>
                <th>Payable</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="debitNoteLineItemDto" items="${pa.debitNoteDto.debitNoteLineItemDtoList}" varStatus="ctr">
                <c:set var="sku" value="${debitNoteLineItemDto.debitNoteLineItem.sku}"/>
                <c:set var="tax" value="${debitNoteLineItemDto.debitNoteLineItem.tax}"/>
                <c:set var="productVariant" value="${debitNoteLineItemDto.debitNoteLineItem.sku.productVariant}"/>
                <c:set var="debitNote" value="${debitNoteLineItemDto.debitNoteLineItem.debitNote}" />
                <s:hidden name="debitNoteLineItems[${ctr.index}].id" value="${debitNoteLineItemDto.debitNoteLineItem.id}"/>
                <s:hidden name="debitNoteLineItems[${ctr.index}].productName" value="${debitNoteLineItemDto.debitNoteLineItem.productName}"/>
                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                    <td class="sNo">${ctr.index+1}.</td>

                    <td>
                            ${productVariant.id}
                            <s:hidden name="debitNoteLineItems[${ctr.index}].debitNote" value="${debitNote.id}" />
                            <s:hidden name="debitNoteLineItems[${ctr.index}].sku" value="${sku.id}" />
	                            <s:hidden name="debitNoteLineItems[${ctr.index}].tax" value="${tax.id}" />
                        <s:hidden class="variant" name="debitNoteLineItems[${ctr.index}].productVariant"
                                  value="${productVariant.id}"/>
                    </td>
                    <td>${productVariant.upc}</td>
                    <c:choose>
                    <c:when test="${productVariant!=null }"><td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
                    </td></c:when>
                    <c:otherwise><td>${debitNoteLineItemDto.debitNoteLineItem.productName}</td>
                    </c:otherwise>
                    </c:choose>
                    <td>
                        <fmt:formatNumber value="${tax.value * 100}"
                                          maxFractionDigits="2"/>
                    </td>
                    <td>
                    	<c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.qty}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].qty" value="${debitNoteLineItemDto.debitNoteLineItem.qty}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text class="qty" name="debitNoteLineItems[${ctr.index}].qty" value="${debitNoteLineItemDto.debitNoteLineItem.qty}"/>
                    		</c:otherwise>
                    	</c:choose>
                        
                    </td>
                    <td>
                    <c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		 ${debitNoteLineItemDto.debitNoteLineItem.costPrice}
                            	<s:hidden name="debitNoteLineItems[${ctr.index}].costPrice"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
                    		</c:when>
                    		<c:otherwise>
                        <shiro:hasRole name="<%=RoleConstants.FINANCE%>">
                            <s:text name="debitNoteLineItems[${ctr.index}].costPrice"
                                   class="costPrice" value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
                        </shiro:hasRole>
                        <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
                            ${debitNoteLineItemDto.debitNoteLineItem.costPrice}
                            <s:hidden name="debitNoteLineItems[${ctr.index}].costPrice"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
                        </shiro:lacksRole>
                        </c:otherwise>
                    </c:choose>
                    </td>
                    <td>
                    <c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		 ${debitNoteLineItemDto.debitNoteLineItem.mrp}
                            <s:hidden class="mrp" name="debitNoteLineItems[${ctr.index}].mrp"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
                    		</c:when>
                    		<c:otherwise>
                    		<shiro:hasRole name="<%=RoleConstants.FINANCE%>">
                            <s:text class="mrp" name="debitNoteLineItems[${ctr.index}].mrp"
                                    value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
                        </shiro:hasRole>
                        <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
                            ${debitNoteLineItemDto.debitNoteLineItem.mrp}
                            <s:hidden class="mrp" name="debitNoteLineItems[${ctr.index}].mrp"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
                        </shiro:lacksRole>
                        </c:otherwise>
                    		</c:choose>
                        
                    </td>
                    <td>
                        <fmt:formatNumber value="${debitNoteLineItemDto.taxable}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${debitNoteLineItemDto.tax}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${debitNoteLineItemDto.surcharge}" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${debitNoteLineItemDto.payable}" maxFractionDigits="2"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="8">Total</td>
                <td><fmt:formatNumber value="${pa.debitNoteDto.totalTaxable}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${pa.debitNoteDto.totalTax}" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber value="${pa.debitNoteDto.totalSurcharge}" maxFractionDigits="2"/></td>
                <td id="totalPayable"><fmt:formatNumber value="${pa.debitNoteDto.totalPayable}" maxFractionDigits="2"/></td>
            </tr>
            <tr></tr>
            <tr>
            <td colspan="8"></td>
            <td colspan="3">Freight And Forwarding</td>
            <td><s:text name="debitNote.freightForwardingCharges" value="${debitNote.freightForwardingCharges}" id="freightForwardingCharges"/></td>
            </tr>
            <tr>
            <td colspan="8"></td>
            <td colspan="3">Final Debit Amount</td>
            <td><s:text name="debitNote.finalDebitAmount" value="${debitNote.finalDebitAmount}" id="finalDebitAmount"/></td>
            </tr>
            </tfoot>
        </table>
        <div class="variantDetails info"></div>
        <br/>
        <!-- <a href="debitNote.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a> -->

        <s:submit name="save" id="save" value="Save" />
        <shiro:hasRole name="<%=RoleConstants.GOD%>">
			<s:submit name="delete" value="Delete"/>
		</shiro:hasRole>
        
    </s:form>

</s:layout-component>

</s:layout-render>
