<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Edit Reconciliation Voucher">
<jsp:useBean id="now" class="java.util.Date" scope="request" />
<s:layout-component name="htmlHead">

    <%
        MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
        List<ReconciliationType> reconciliationTypeList  = masterDataDao.getAddReconciliationTypeList();
        pageContext.setAttribute("reconciliationTypeList", reconciliationTypeList);
    %>


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

                var nextIndex = eval(lastIndex + "+1");


                var reconciliationTypeOptions = '<select class="reconciliationType valueChange" name="rvLineItems[' + nextIndex + '].reconciliationType">';
                <c:forEach items="${reconciliationTypeList}" var="reconciliationTypeVar">
                reconciliationTypeOptions += '<option value="'+${reconciliationTypeVar.id}+'">'+"${reconciliationTypeVar.name}"+'</option>';
                </c:forEach>



                var newRowHtml =
                        '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                                '  <td>' +
                                '    <input type="hidden" name="rvLineItems[' + nextIndex + '].id" />' +
                                '    <input type="text" class="variant" name="rvLineItems[' + nextIndex + '].productVariant"/>' +
                                '  </td>' +
                                '  <td class="pvDetails"></td>' +
                                '  <td>' +
                                '    <input type="text" id="quantity" name="rvLineItems[' + nextIndex + '].qty" />' +
                                '  </td>' +
                                '   <td>' +
                                reconciliationTypeOptions+
                                '</select>'+
                                '<input type="hidden" value="finance" class="reconciliationTypeIdentifier"/>'+
                                '</td>' +
                                '  <td>' +
                                '    <input class="costPrice" type="text" name="rvLineItems[' + nextIndex + '].costPrice" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="mrp" type="text" name="rvLineItems[' + nextIndex + '].mrp" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input type="text" class="batch" name="rvLineItems[' + nextIndex + '].batchNumber" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].mfgDate" />' +
                                '  </td>' +
                                '  <td>' +
                                '    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].expiryDate" />' +
                                '  </td>' +
                                '  <td>'  +
								'    <textarea rows="4" columns="10" name="rvLineItems[' + nextIndex + '].remarks" style="height:50px;"/>' +
                                '  </td>' +
                                '</tr>';

                $('#poTable').append(newRowHtml);

                return false;
            });

            $('.variant').live("change", function() {
                var variantRow = $(this).parents('.lineItemRow');
                var productVariantId = variantRow.find('.variant').val();
                var productVariantDetails = variantRow.find('.pvDetails');
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
                            } else {
                                $('.variantDetails').html('<h2>'+res.message+'</h2>');
                            }
                        }
                );
            });

        });
    </script>
</s:layout-component>

<s:layout-component name="content">
    <c:set var="itemCheckedin" value="false"/>
    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
                event="getPVDetails"></s:link>
    </div>
    <h2>Add/Edit Reconciliation Voucher</h2>
    <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
        <s:hidden name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
        <table>
            <tr>
                <td>Reconciliation Date</td>
                <td>
                    <s:text class="date_input" formatPattern="yyyy-MM-dd" name="reconciliationVoucher.reconciliationDate"
                            value="${pa.reconciliationVoucher.reconciliationDate != null ? pa.reconciliationVoucher.reconciliationDate : now}"/>
                </td>
            </tr>
            <tr>
                <td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
                <td><s:textarea name="reconciliationVoucher.remarks" style="height:50px;"
                                value="${pa.reconciliationVoucher.remarks}"/></td>
            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                    <s:hidden name="reconciliationVoucher.warehouse" value="${whAction.setWarehouse.id}"/>
                        ${whAction.setWarehouse.identifier}
                </td></tr>
        </table>


        <table border="1">
            <thead>
            <tr>
                <th>VariantID</th>
                <th>Details</th>
                <th>Qty<br/>Only(+)</th>
                <th>Reconciliation Type<br/>(New)</th>
                <th>Cost Price<br/>(Without TAX)</th>
                <th>MRP</th>
                <th>Batch Number</th>
                <th>Mfg. Date<br/>(yyyy-MM-dd)</th>
                <th>Exp. Date<br/>(yyyy-MM-dd)</th>
	            <th>Remarks</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="rvLineItem" items="${pa.reconciliationVoucher.rvLineItems}" varStatus="ctr">
                <c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
                <s:hidden name="rvLineItems[${ctr.index}]" value="${rvLineItem.id}"/>
                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                    <td>
                         <c:if test="${rvLineItem.reconciledQty > 0}">
                           <c:set var="itemCheckedin" value="true"/>
                             <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
                                <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                                        event="downloadBarcode"> Barcode
                                    <s:param name="rvLineItem" value="${rvLineItem.id}"/>
                                    <%--<s:param name="rvLineItem.reconciliationVoucher" value="${rvLineItem.reconciliationVoucher.id}"/>--%>
                                     <s:param name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
                                </s:link>
                             </shiro:hasPermission>

                        </c:if>
                            ${productVariant.id}
                    </td>
                    <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                    </td>
                    <td>${rvLineItem.qty}
                    </td>
                    <td class="reconciliationType">
                        <input type="hidden" value="finance"
                               class="reconciliationTypeIdentifier"/>
	                    <s:hidden name="rvLineItems[${ctr.index}].reconciliationType" value="${rvLineItem.reconciliationType.id}"/>
	                     ${rvLineItem.reconciliationType.name}
                    </td>
                    <td>${rvLineItem.costPrice}
                    </td>
                    <td>${rvLineItem.mrp}
                    </td>
                    <td>${rvLineItem.batchNumber}</td>
                    <td>
                        <fmt:formatDate value="${rvLineItem.mfgDate}" type="both"/></td>
                    <td>
                        <fmt:formatDate value="${rvLineItem.expiryDate}" type="both"/></td>
	                <td>${rvLineItem.remarks}</td>

                </tr>

            </c:forEach>
            </tbody>
        </table>
        <div class="variantDetails info"></div>
        <br/>
        <a href="reconciliationVoucher.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

        <s:submit id="reconcileAdd" name="reconcileAdd" value="Save" class="saveButton"/>
        <shiro:hasRole name="<%=RoleConstants.WH_MANAGER%>">
        <hr/>
        <fieldset>
            <legend>Upload Excel to Create RV LineItems</legend>
            <br/>
            <span class="large gry">(VARIANT_ID, QTY, BATCH_NUMBER, EXP_DATE(yyyy/MM), MFG_DATE(yyyy/MM), MRP, COST) as excel headers</span>
            <br/><br/>
            <h2>File to Upload: <s:file  id="fileBean" name="fileBean" size="30"/></h2>
            <div class="buttons">
                <s:submit id="excelUpload" name="parseAddRVExcel" value="Create RV LineItems"/>
            </div>
        </fieldset>
        </shiro:hasRole>

        <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
         <c:if test="${itemCheckedin}">
          <s:link class=" button_green" style="width: 180px; height: 18px; align_right" beanclass ="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="downloadAllBarcode"> Get All Barcodes
             <s:param name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
         </s:link>
            </c:if>
        </shiro:hasPermission>

    </s:form>

    <script type="text/javascript">
	    $(document).ready(function() {
		    $('.saveButton').click(function disableSaveButton() {

			    var result = true;
			    $('#poTable tr').each(function() {
				    var qty = $(this).find('#quantity').val() ;
				    var variant = $(this).find('.variant').val();
				    var batch = $(this).find('.batch').val();

				    if ((variant != null && variant != '') && ( (batch == null || batch.trim() == '') || (qty == null || qty.trim() == ''))) {
					    alert('Enter Batch Number && Qty for variant :::::: ' + variant);
					    result = false;
					    return false;

				    }

			    });
			    if (!result) {
				    return false;
			    }
			    else {
				    return $(this).submit();
			    }
			    $(this).css("display", "none");
		    });
		    
		    $('#excelUpload').live("click", function() {
                var filebean = $('#fileBean').val();
                if (filebean == null || filebean == '') {
                  alert('choose file');
                  return false;
                }
              });
		    
		    $("#reconcileAdd").click(function(){
		  		$(this).hide();
		  	});
	    });
    </script>
</s:layout-component>


</s:layout-render>