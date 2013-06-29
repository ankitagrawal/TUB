<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.sku.EnumSkuItemTransferMode" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<c:set var="RvLineItemOut" value="<%=EnumSkuItemTransferMode.RV_LINEITEM_OUT.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Reconciliation Voucher">
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<s:layout-component name="htmlHead">

    <%
        MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
        List<ReconciliationType> reconciliationTypeList = masterDataDao.getReconciliationTypeList();
        pageContext.setAttribute("reconciliationTypeList", reconciliationTypeList);
        List<ReconciliationType> debitNoteReconciliationTypeList = masterDataDao.getDebitNoteReconciliationType();
        pageContext.setAttribute("debitNoteReconciliationTypeList", debitNoteReconciliationTypeList);
        
    %>


    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
    </script>
</s:layout-component>

<s:layout-component name="content">
    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
                event="getPVDetails">

        </s:link>
        <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" id="batchInfoLink"
                event="getBatchDetails"></s:link>
        <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" class="singleSaveLink"
                event="saveAndReconcileRv"/>
    </div>
    <div>

    </div>
    <h2>Subtract/Edit Reconciliation Voucher</h2>

    <h2>RV No # ${pa.reconciliationVoucher.id}</h2>
    <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
        <s:hidden class="reconciliationId" name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
        <div style="display: none">
    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
            id="supplierInfo" event="getSupplierAgainstBarcode"></s:link>
  </div>
        <div id="rvInfoDiv">
        	<div id="rvAndSupplierDiv">
        	<table>
                    <tr>
                        <td><strong>Reconciliation Date</strong></td>
                        <td>
                                ${pa.reconciliationVoucher.reconciliationDate}
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Remarks</strong><br/><span class="sml gry">(eg. XXX)</span></td>
                        <td>${pa.reconciliationVoucher.remarks}</td>
                    </tr>
                    <tr>
                        <td><strong>For Warehouse</strong></td>
                        <td>
                                ${pa.reconciliationVoucher.warehouse.identifier}
                        </td>
                    </tr>
                    </table>
                    
                    <c:if test="${pa.reconciliationVoucher.supplier!=null }">
                    <table id="supplierTable">
                    <tr>
                    <td><strong>Supplier Name</strong></td>
                    <td>${pa.reconciliationVoucher.supplier.name}</td>
                    </tr>
                    <tr>
                    <td><strong>State</strong></td>
                    <td>${pa.reconciliationVoucher.supplier.state}</td>
                    </tr>
                    <tr>
                    <td><strong>Tin</strong></td>
                    <td>${pa.reconciliationVoucher.supplier.tinNumber}</td>
                    </tr>
                    </table>
                    </c:if>
                    </div>
        	<div id="scanDiv">
        		<table>
                     <tr>
                         <td colspan="2" style="font-size:20px;"> Scan Barcode:</td>
                     </tr>
                     <tr>
                         <td><strong> UPC (Barcode)</strong></td>
                         <td><s:text name="upc" id="upc" size="50" style="padding:5px; width:125px;"/></td>
                     </tr>
                     <tr>
                         <td><strong> Remarks</strong></td>
                             <%--<td> <s:textarea style="height:60px;" name="rvLineItem.remarks" value="${rvLineItem.remarks}"> </s:textarea></td>--%>
                         <td><s:textarea style="height:60px;" name="remarks"> </s:textarea></td>
                     </tr>
                     <tr>
                     <td><strong>Reconciliation Type</strong></td>
                         
					<c:choose>
					<c:when test="${pa.reconciliationVoucher.supplier!=null}">
					<td><s:select name="reconciliationType"
	                                       value="${rvLineItem.reconciliationType.id}" class="valueChange">
	                             <hk:master-data-collection service="<%=MasterDataDao.class%>"
	                                                        serviceProperty="debitNoteReconciliationType" value="id"
	                                                        label="name"/> </s:select></td>
					</c:when>
					<c:otherwise>
					<td><s:select name="reconciliationType"
	                                       value="${rvLineItem.reconciliationType.id}" class="valueChange">
	                             <hk:master-data-collection service="<%=MasterDataDao.class%>"
	                                                        serviceProperty="reconciliationTypeList" value="id"
	                                                        label="name"/></s:select></td>
					</c:otherwise>
					</c:choose>
                         <td colspan="2">
                             <div style="float:left;"><s:submit id="SubtractReconciled" name="SubtractReconciled" value="Subtract"/></div>
                         </td>
                     </tr>
                    </table>
        </div>
        </div>

        <div id="rvDiv">

        <table id="rvTable">
            <thead>
            <tr>
                <th>Barcode</th>
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
                <th>Reconcilied Qty</th>
                <th> Item Details</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="rvLineItem" items="${pa.reconciliationVoucher.rvLineItems}" varStatus="ctr">
                <c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
                <tr style="background-color:#ccff99;">
                    <td style="width:80px;">${rvLineItem.skuGroup.barcode}</td>
                    <td style="width:75px;">
                            ${productVariant.id}
                    </td>
                    <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                    </td>
                    <td>${rvLineItem.qty}
                    </td>
                    <td>${rvLineItem.reconciliationType.name}
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
                    <td>${rvLineItem.reconciledQty}</td>
                    <td><s:link beanclass="com.hk.web.action.admin.sku.ViewSkuItemAction" event="pre">
                        View Item Details
                        <s:param name="rvLineItem" value="${rvLineItem.id}"/>
                        <s:param name="entityId" value="${RvLineItemOut}"/>
                    </s:link></td>
                </tr>

            </c:forEach>
            </tbody>
        </table>

    </s:form>

	<div id="closeButtonDiv">
	<c:if test="${pa.reconciliationVoucher.rvLineItems!=null && fn:length(pa.reconciliationVoucher.rvLineItems) >0 }">
	<s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" id="createDebitNoteButton" event="createDebitNote" Value="CreateDebitNote" class="button_green addToCartButton" >
	<s:param name="reconciliationVoucher" value="${pa.reconciliationVoucher}" />
	<s:param name = "warehouse" value="${pa.reconciliationVoucher.warehouse}"/>
	 Create Debit Note </s:link>
	</c:if>
	</div>

    <shiro:hasRole name="<%=RoleConstants.WH_MANAGER%>">
        <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" id="uploadForm">
            <s:hidden name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
            <fieldset>
                <legend>Upload Excel to Subtract RV</legend>
                <br/>
        (GROUP BARCODE,ITEM BARCODE,VARIANT_ID, QTY, BATCH_NUMBER, EXP_DATE(yyyy/MM), MFG_DATE(yyyy/MM), MRP, COST,RECON REASON) as excel headers

                <br/><br/>

                <h2>File to Upload: <s:file id="fileBean" name="fileBean" size="30"/></h2>

                <div class="closeButtonDiv">
                    <s:submit id="excelUpload" name="parseSubtractRVExcel" value="Create RV LineItems"/>
                </div>

            </fieldset>
        </s:form>
    </shiro:hasRole>


    <script type="text/javascript">
        $(document).ready(function() {
            $('.saveButton').click(function(e) {
                var result = true;
                $('#poTable tr').each(function() {
                    var qty = $(this).find('#quantity').val() ;
                    var variant = $(this).find('.variant').val();
                    var batch = $(this).find('.batch').val();

                    if ($(this).find("input").length) {
                        if (variant == null || variant.trim() == '') {
                            $(this).remove();
                            return;
                        }
                    }
                    if (variant != null && ( (batch == null || batch.trim() == '') || (qty == null || qty.trim() == ''))) {
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

            });
            
            $('#excelUpload').live("click", function() {
            	$(this).hide();
                var filebean = $('#fileBean').val();
                if (filebean == null || filebean == '') {
                	$(this).show();
                  alert('choose file');
                  return false;
                }
              });
            
            $('#upc').change(function() {
            	var supplier = null;
            	if(${pa.reconciliationVoucher.reconciliationType.id==190}){
            		if(${pa.supplier}==null){
                		supplier = null;
                	}
                	else{
                		supplier = ${pa.supplier};
                	}
            	}
            	var barcode = $('#upc').val();
            	var warehouseId = ${pa.reconciliationVoucher.warehouse.id};
            	var productSupplier;
            	
            	$.getJSON($('#supplierInfo').attr('href'), {barcode : barcode, warehouseId : warehouseId},function(res) {
                	  if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                		  if(res.data.supplier.id!=supplier){
                			  var retVal = confirm("The suppliers against the rv and against the product variant do not match. Do you want to continue?");
                        	   if(retVal){
                        	      return true;               	 
                        	   }
                		  }
                	  }else{
                		  alert('Either Invalid barcode or No Item found.');
                		  $('#upc').val('');
                	  }
                  }
            	);
            	return false;
            });
            
            if(${pa.isDebitNoteCreated!=null && pa.isDebitNoteCreated}){
    			$("#createDebitNoteButton").hide();
    		}
        });
    </script>
    
    <style>
#rvInfoDiv {
	width: 700px;
	float: none;
	margin: auto;
}

#rvAndSupplierDiv {
	float: left;
	position: relative;
	width: 48%;
}

#scanDiv {
	float: right;
	position: relative;
	width: 48%;
}

#rvDiv {
	float: right;
	position: relative;
	width: 100%;
}

#rvTable {
	width: 100%;
}

#closeButtonDiv {
	float: left;
	position: relative;
	left: 40%;
	margin-bottom: 2px;
	margin-top: 2px;
}

#uploadForm {
	float: left;
	position: relative;
}
</style>
</s:layout-component>

</s:layout-render>
