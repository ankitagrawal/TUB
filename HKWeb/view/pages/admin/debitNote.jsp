<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.core.Surcharge" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.domain.core.Tax" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.courier.EnumPickupStatus" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
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

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Debit Note">
<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $(document).ready(function() {
        	
        	function updateTotalPI(fromTotalClass, toTotalClass, toHtml, fromtable, totable) {
    			var total = 0;	var otherTable;
    			
    			$.each(fromtable.parent().find(fromTotalClass), function(index, value) {
    				var eachRow = $(value);
    				var eachRowValue = eachRow.val().trim();
    				total += parseFloat(eachRowValue);
    			});
    			total = total.toFixed(2);
    			if (toHtml == 1) {
    				total = Math.floor(total);
    				totable.find(toTotalClass).html(total);
    			} else {
    				totable.find(toTotalClass).val(total);
    			}
    		};
        	
        	
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
                
                if($('#returnByHand').is(":checked")){
               	  var retVal = confirm("Are you sure to return by hand. You may not be able to add courier details further");
               	   if( retVal == true ){
               		   bool = true;
               		  return true;
               	   }else{
               		   obj.show();
               		   bool = false;
               		  return false;
               	   }
                  }
                
                if(!bool){
                    obj.show();
                   return false;
                    }
                
                $('#dnForm').submit(function() {
  				  $(this).find(':input:disabled') 
  				    .prop('disabled', false); 
  				});
            });
			
            
            var debitNoteStatusDB = parseFloat(${pa.debitNote.debitNoteStatus});
            if (debitNoteStatusDB >=50){
            	$("#debitNoteLineItems :input").each(function(){
   	    		 $(this).attr('readonly', true);
   	    		});
            }
            
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
            
            $('.valueChange').live("change", function() {
    			var table = $(this).parent().parent().parent();
    			var valueChangeRow = $(this).parents('.lineItemRow');
    			var costPrice = valueChangeRow.find('.costPrice').val();
    			var mrp = valueChangeRow.find('.mrp').val();
    			var qty = valueChangeRow.find('.receivedQuantity').val();
    			var taxIdentifier = valueChangeRow.find('.taxIdentifier').val();
    			if(table.parent().attr('id')==$("#debitNoteLineItems").attr('id')){
    				if (qty == "" || costPrice == "") {
        				alert("All fields are compulsory.");
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
    			var discountPercentage;
    			if(valueChangeRow.find('.discountPercentage').length!=0){
    			discountPercentage = valueChangeRow.find('.discountPercentage').val();
    			}
    			else{
    				discountPercentage=0;
    			}
    			var discountedAmount;
    			if (isNaN(discountPercentage)) {
    				alert("Enter valid discount");
    				return;
    			}
    			discountedAmount = (discountPercentage / 100) * taxable;
    			taxable -= discountedAmount;
    			var surchargeCategory = 0.0;
    			var stateIdentifier = $('.state').html();
    			surchargeCategory = ${hk:getSurchargeValue(pia.purchaseInvoice.supplier.state, pia.purchaseInvoice.warehouse.state)};
    			/*if (stateIdentifier == 'CST') {
    			 surchargeCategory = 0.0;
    			 } else {
    			 surchargeCategory = 0.05;
    			 }*/
    			var tax = taxable * taxCategory;
    			var surcharge = tax * surchargeCategory;
    			var payable = surcharge + taxable + tax;

    			valueChangeRow.find('.taxableAmount').val(taxable.toFixed(2));
    			valueChangeRow.find('.taxAmount').val(tax.toFixed(2));
    			valueChangeRow.find('.surchargeAmount').val(surcharge.toFixed(2));
    			valueChangeRow.find('.payableAmount').val(payable.toFixed(2));

    			
    			updateTotalPI('.taxableAmount', '.totalTaxable', 0, table, $("#totalsTable"));
    			updateTotalPI('.taxAmount', '.totalTax', 0, table, $("#totalsTable"));
    			updateTotalPI('.surchargeAmount', '.totalSurcharge', 0, table, $("#totalsTable"));
    			updateTotalPI('.payableAmount', '.totalPayable', 0, table, $("#totalsTable"));
    			}
    			var finalPayable = parseFloat($("#totalsTable").parent().find('.totalPayable').val());
    			if (isNaN(overallDiscount)) {
    				overallDiscount = 0;
    			}
    			var freightCharges; var overallDiscount; 
    			if($("#totalsTable").parent().find('.overallDiscount').length!=0){
    				if($("#totalsTable").parent().find('.overallDiscount').val().indexOf('-') === -1){
    					overallDiscount = parseFloat($("#totalsTable").parent().find('.overallDiscount').val().replace(/,/g, ''));
    				}else
    					overallDiscount = parseFloat($("#totalsTable").parent().find('.overallDiscount').val());
    			}
    			
    			if(finalPayable-overallDiscount<0){
    				alert('the final amount will be negative');
    				$("#totalsTable").find($('.overallDiscount')).val('0');
    			}else{
    				finalPayable -= overallDiscount;
    			}
    			freightCharges = parseFloat($("#totalsTable").find('.freight').val());
    			finalPayable = finalPayable+freightCharges;
    			$("#totalsTable").find('.finalDebitAmount').val(finalPayable.toFixed(2));
    		});
            
            
            if(${pa.debitNote.purchaseInvoice!=null}){
            	$(".addRowButton").hide();
            }
            
            if(${pa.debitNote.returnByHand!=null && pa.debitNote.returnByHand}){
            	$("#debitNoteCourier").hide();
            }
            
            $('#returnByHand').click(function(){
            	if($(this).is(":checked")){
            		$("#debitNoteCourier").hide();	
            	}else{
            		$("#debitNoteCourier").show();
            	}
            	
              });
            
            if(${(pa.debitNote.purchaseInvoice!=null)}){
    			$("#dnForm .taxValues").each(function(){
    				$(this).prop('disabled', true);
    	    		});
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

    <s:form beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" id="dnForm">
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
                <td>Supplier Invoice #</td>
                <td><c:if test="${pa.debitNote.purchaseInvoice!=null }">${pa.debitNote.purchaseInvoice.invoiceNumber}</c:if></td>

            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                        ${pa.debitNote.warehouse.identifier}
                </td>
                <td>Remarks</td>
                <td><s:textarea name="debitNote.remarks" style="height:50px;"/></td>
                <td>Return By Hand</td>
                <td>
                <c:choose>
	              <c:when test="${pa.debitNote.returnByHand}">
	                <!-- <input type="checkbox" checked="checked" id="returnByHand" name="debitNote.returnByHand"  /> -->
	                <label>Yes</label>
	              </c:when>
	              <c:otherwise>
	                <input type="checkbox" id="returnByHand" name="debitNote.returnByHand"/>
	              </c:otherwise>
	            </c:choose>
            </td>
            </tr>
        </table>
<br>
<div class="clear"></div>

<div id="debitNoteCourier">
<h2>Courier Details</h2>
        <table border="1">
            <thead>
              <tr>
                  <th>Courier</th>
                  <th>Pickup Confirmation No.</th>
                  <th>Tracking No.</th>
                  <th>PickUp Status</th>
                  <th>Pickup Date</th>
                  <th>Destination Address</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                  <td>
                      <input type="hidden" name="courierPickupDetail.id" value="${pa.debitNote.courierPickupDetail.id}" >
                     <s:select name="courierPickupDetail.courier" id="allActiveCourier">
                         <s:option value="">--Select--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="allActiveCourier" value="id" label="name"/>
                      </s:select>
                      <script type="text/javascript">
                          $('#allActiveCourier').val(${pa.debitNote.courierPickupDetail.courier.id});
                      </script>
                  </td>
                  <td>
                       <s:text name="courierPickupDetail.pickupConfirmationNo" value="${pa.debitNote.courierPickupDetail.pickupConfirmationNo}"/>
                  </td>
                  <td>
                      <s:text name="courierPickupDetail.trackingNo" value="${pa.debitNote.courierPickupDetail.trackingNo}"/>
                  </td>
                  <td>
                      <s:select name="pickupStatusId" id="pickupStatus">
                          <s:option value="">--Select--</s:option>
                          <s:option value="<%=EnumPickupStatus.OPEN.getId()%>"><%=EnumPickupStatus.OPEN.getName()%></s:option>
                          <s:option value="<%=EnumPickupStatus.CLOSE.getId()%>"><%=EnumPickupStatus.CLOSE.getName()%></s:option>
                      </s:select>
                      <script type="text/javascript">
                          $('#pickupStatus').val(${pa.debitNote.courierPickupDetail.pickupStatus.id});
                      </script>
                  </td>
                  <td><s:text class="date_input" id="pickupDate" formatPattern="yyyy-MM-dd" name="courierPickupDetail.pickupDate" value="${pa.debitNote.courierPickupDetail.pickupDate}"/></td>
                  <td>
                      <s:text name="destinationAddress" value="${pa.debitNote.destinationAddress}" id="destinationAddress" />
                  </td>
              </tr>
            </tbody>
        </table>
        </div>
        <br><br>
        
        <table border="1" id="debitNoteLineItems">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>VariantID</th>
                <th>UPC</th>
                <th>Details</th>
                <c:choose>
				<c:when test="${fn:toLowerCase(pa.debitNote.supplier.state) == fn:toLowerCase(pa.debitNote.warehouse.state)}">
				<th>Tax<br/>Category</th>
				</c:when>
				<c:otherwise>
				<th>Surcharge<br/>Category</th>
				</c:otherwise>
				</c:choose>
                <th>Debit Qty</th>
                <th>Cost Price<br/>(Without TAX)</th>
                <th>MRP</th>
                <c:if test="${pa.debitNote.debitNoteType.id==20}">
                <th>Discount Percent</th>
                </c:if>
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
                    
                    <td class="taxCategory">
					<shiro:hasPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<input type="hidden" value="finance"
					       class="taxIdentifier"/>
					<c:choose>
						<c:when test="${fn:toLowerCase(pa.debitNote.supplier.state) == fn:toLowerCase(pa.debitNote.warehouse.state)}">
							<s:select name="debitNoteLineItems[${ctr.index}].tax" 
							          value="${debitNoteLineItemDto.debitNoteLineItem.tax.id}" class="valueChange taxValues">
								<hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="localTaxList"
								                           value="id"
								                           label="value"/>
							</s:select>
						</c:when>
						<c:otherwise>
							<s:select name="debitNoteLineItems[${ctr.index}].tax"
							          value="${debitNoteLineItemDto.debitNoteLineItem.tax.id}" class="valueChange taxValues">
								<hk:master-data-collection service="<%=TaxDao.class%>"
								                           serviceProperty="centralTaxList" value="id"
								                           label="value"/>
							</s:select>
						</c:otherwise>
					</c:choose>
				</shiro:hasPermission>
				<shiro:lacksPermission name="<%=PermissionConstants.UPDATE_RECONCILIATION_REPORTS%>">
					<c:choose>
						<c:when test="${fn:toLowerCase(pa.debitNote.supplier.state) == fn:toLowerCase(pa.debitNote.warehouse.state)}">
							<s:text name="debitNoteLineItems[${ctr.index}].tax"
							        value="${debitNoteLineItemDto.debitNoteLineItem.tax.value}" class="taxCategory taxValues"/>
						</c:when>
						<c:otherwise>
							<s:text name="debitNoteLineItems[${ctr.index}].tax"
							        value="${debitNoteLineItemDto.debitNoteLineItem.tax.value}" class="taxCategory taxValues"/>
						</c:otherwise>
					</c:choose>
				</shiro:lacksPermission>
			</td>
                    
                    <td>
                    	<c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.qty}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].qty" value="${debitNoteLineItemDto.debitNoteLineItem.qty}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text class="qty valueChange receivedQuantity" name="debitNoteLineItems[${ctr.index}].qty" value="${debitNoteLineItemDto.debitNoteLineItem.qty}"/>
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
                                   class="costPrice valueChange" value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
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
                            <s:hidden class="mrp valueChange" name="debitNoteLineItems[${ctr.index}].mrp"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
                    		</c:when>
                    		<c:otherwise>
                    		<shiro:hasRole name="<%=RoleConstants.FINANCE%>">
                            <s:text class="mrp valueChange" name="debitNoteLineItems[${ctr.index}].mrp"
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
                    <c:if test="${pa.debitNote.debitNoteType.id==20}">
                	<td>
                    		 <s:text class="discountPercentage valueChange" name="debitNoteLineItems[${ctr.index}].discountPercent"
                                      value="${debitNoteLineItemDto.debitNoteLineItem.discountPercent}"/>
                    		</td>
                	</c:if>
                    <td>
                    <c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.taxableAmount}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].taxableAmount" value="${debitNoteLineItemDto.debitNoteLineItem.taxableAmount}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text readonly="readonly" class="taxableAmount"
				        name="debitNoteLineItems[${ctr.index}].taxableAmount"
				        value="${debitNoteLineItemDto.debitNoteLineItem.taxableAmount}"/>
                    		</c:otherwise>
                    	</c:choose>
				</td>
                <td>    
                <c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.taxAmount}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].taxAmount" value="${debitNoteLineItemDto.debitNoteLineItem.taxAmount}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text readonly="readonly" class="taxAmount"
				        name="debitNoteLineItems[${ctr.index}].taxAmount"
				        value="${debitNoteLineItemDto.debitNoteLineItem.taxAmount}"/>
                    		</c:otherwise>
                    	</c:choose>
                
			</td>
			<td>
			
				<c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.surchargeAmount}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].surchargeAmount" value="${debitNoteLineItemDto.debitNoteLineItem.surchargeAmount}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text readonly="readonly" class="surchargeAmount"
				        name="debitNoteLineItems[${ctr.index}].surchargeAmount"
				        value="${debitNoteLineItemDto.debitNoteLineItem.surchargeAmount}"/>
                    		</c:otherwise>
                    	</c:choose>
			
			</td>
			<td>
			<c:choose>
                    		<c:when test="${pa.debitNote.purchaseInvoice!=null }">
                    		${debitNoteLineItemDto.debitNoteLineItem.payableAmount}
                    		<s:hidden name="debitNoteLineItems[${ctr.index}].payableAmount" value="${debitNoteLineItemDto.debitNoteLineItem.payableAmount}"/>
                    		</c:when>
                    	<c:otherwise>
                    		<s:text readonly="readonly" class="payableAmount"
				        name="debitNoteLineItems[${ctr.index}].payableAmount"
				        value="${debitNoteLineItemDto.debitNoteLineItem.payableAmount}"/>
                    		</c:otherwise>
                    	</c:choose>
			</td>
                </tr>
            </c:forEach>
            </tbody>
            <%-- <tfoot>
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
            </tfoot> --%>
        </table>
        <div class="variantDetails info"></div>
        
        <div id="totalsdiv">
		<table id="totalsTable">
		<tr>
		<th></th>
		<th>Taxable</th>
		<th>Tax</th>
		<th>Surcharge</th>
		<th>Payable</th>
		</tr>
		<tr>
		<td colspan="">Total</td>
		  <td><s:text readonly="readonly" class="totalTaxable" name="debitNote.totalTaxable" value="${pa.debitNoteDto.totalTaxable}"/></td>
		<td><s:text readonly="readonly" class="totalTax" name="debitNote.totalTax" value="${pa.debitNoteDto.totalTax}"/></td>
		<td><s:text readonly="readonly" class="totalSurcharge" name="debitNote.totalSurcharge" value="${pa.debitNoteDto.totalSurcharge}"/></td>
		<td><s:text readonly="readonly" class="totalPayable" name="debitNote.totalPayable" value="${pa.debitNoteDto.totalPayable}"/></td>
		</tr>
		<tr>
		<td colspan="4">Further Discount (if any)</td>
		<td><s:text class="overallDiscount valueChange" name="debitNote.discount" value="${pa.debitNoteDto.totalDiscount}"/></td>
		</tr>
		<tr>
		<td colspan="4">Freight And Forwarding</td>
		<td><s:text  class="overallDiscount valueChange freight"  name="debitNote.freightForwardingCharges" value="${debitNote.freightForwardingCharges}" id="freightForwardingCharges"/></td>
		</tr>
		<tr>
		<td colspan="4">Final Payable</td>
		<td>
		<s:text readonly="readonly" class="finalPayable finalDebitAmount" name="debitNote.finalDebitAmount" value="${pa.debitNoteDto.finalDebitAmount}"/></td>
				            <td>
		</tr>
		</table>
		</div>
        
        <br/>
        <!-- <a href="debitNote.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a> -->

        <s:submit name="save" id="save" value="Save" />
        <shiro:hasRole name="<%=RoleConstants.GOD%>">
			<s:submit name="delete" value="Delete"/>
		</shiro:hasRole>
        
    </s:form>

</s:layout-component>

</s:layout-render>

<style>
#totalsTable{
border:1px solid #000;
position: relative;
float: right;
}
#totalsdiv{
position: relative;
float: right;
}
#debitNoteLineItems{
width:100%;
}

</style>