<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page import="com.hk.pact.service.catalog.ProductVariantService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" var="ica"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkin">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">
  <c:choose>
  <c:when test="${ica.isBrightSupplier}">Inventory Checkin Against Bright Shipping Order# ${ica.grn.invoiceNumber} : GRN#${ica.grn.id} (PO#${ica.grn.purchaseOrder.id})</c:when>
  <c:otherwise>Inventory Checkin - GRN# ${ica.grn.id} (PO# ${ica.grn.purchaseOrder.id})</c:otherwise>
  </c:choose>
  </s:layout-component>
  <s:layout-component name="content">
    <div style="display:inline;float:left;">
       <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" id="invBarcodeCheckinForm">
        <s:hidden name="grn" value="${ica.grn.id}"/>
           <s:hidden name="invoiceNumber" value="${ica.grn.invoiceNumber}"/>
            <s:hidden name="invoiceDate" value="${ica.grn.invoiceDate}"/>
        <table border="1">
          <tr>
            <td><b>Inventory BARCODE:</b></td>
            <td><s:text name="invBarcode" class="invBarcode" style="padding:5px;"/></td>
          </tr>
        </table>
         <s:submit name="save" value="Save" style="visibility:hidden;padding:1px;font-size:1px;"/>
         
         <script language=javascript type=text/javascript>
           $(document).ready(function() {
             $('.invBarcode').focus();
             $('.invBarcode').live(function() {
               var formName = $('#invBarcodeCheckinForm');
               var formURL = formName.attr('action');
               formName.attr('action', formURL + "?checkinInvBarcode=");
               formName.submit();
             });
           });
         </script>
      </s:form>
      <hr/>
      <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" id="checkinForm">
        <s:hidden name="grn" value="${ica.grn.id}"/>
        <s:hidden name="isBrightSupplier" value="${ica.isBrightSupplier}"/>
        <table border="1">
          <tr>
          <td>UPC(Barcode) or VariantID:</td>
			  <td><s:text name="upc" class="variant"/></td>
          </tr>
          <tr>
            <td>Qty:</td>
            <td><s:text name="qty" value="0" id="qty"/></td>
          </tr>
		<tr>
			<td>Cost Price:</td>
			<td><s:text name="costPrice" value="0.0" id="costPrice"/></td>
		</tr>
		<tr>
			<td>MRP:</td>
			<td><s:text name="mrp" value="0.0" id="mrp"/></td>
		</tr>
          <tr>
            <td>Batch Number:</td>
            <td><s:text name="batch"/></td>
          </tr>
          <tr>
            <td>Mfg. Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="mfgDate"/></td>
          </tr>
          <tr>
            <td>Expiry Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="expiryDate"/></td>
          </tr>
          <tr>
            <td>Invoice Number:</td>
            <td><s:text name="invoiceNumber" value="${ica.grn.invoiceNumber}" readonly="readonly"/></td>
          </tr>
          <tr>
            <td>Invoice Date:</td>
            <td>
              <s:text class="date_input" formatPattern="yyyy-MM-dd" name="invoiceDate" value="${ica.grn.invoiceDate}" readonly="readonly"/></td>
          </tr>
        </table>
        <script language=javascript type=text/javascript>
          $('#courierTrackingId').focus();

          function stopRKey(evt) {
            var evt = (evt) ? evt : ((event) ? event : null);
            var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
            if ((evt.keyCode == 13) && (node.type == "text")) {return false;}
          }
          document.onkeypress = stopRKey;

        </script>
        <br/>
        <s:submit class="invCheckin" name="save" value="Save"/>
        
      </s:form>
      <span style="display:inline;float:right;"><h2><s:link beanclass="com.hk.web.action.admin.inventory.GRNAction">&lang;&lang;&lang;
        Back to GRN List</s:link></h2></span>
    </div>
	  <div style="display: none;">
	  	<s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" id="validationLink"
	  	        event="validateFields"></s:link>
	  </div>

    <div style="display:inline;" align="center">

      <table style="font-size: .8em;">
        <tr>
          <th width="">S.No.</th>
          <th width="">Item</th>
          <th width="">VariantId</th>
          <th width="">UPC</th>
          <th width="">Qty</th>
          <th width="">Checked-in Qty</th>
          <th width="">Download Barcode</th>
        </tr>
        <c:forEach items="${ica.grn.grnLineItems}" var="grnLineItem" varStatus="ctr">
          <c:set value="${grnLineItem.sku.productVariant}" var="productVariant"/>
          <tr class="chkInInfo">
            <td>${ctr.index+1}</td>
            <td>
                ${productVariant.product.name}<br/>
              <em><c:forEach items="${productVariant.productOptions}" var="productOption">
                ${productOption.name} ${productOption.value}
              </c:forEach></em>
            </td>
            <td><a href="#" onclick="$('.variant').val(this.innerHTML);">${productVariant.id}</a></td>
            <td >${productVariant.upc}</td>
            <td class="chkInInfoQty">${grnLineItem.qty}</td>
            <td class="chkInQty" style="color:green; font-weight:bold">${grnLineItem.checkedInQty}</td>
            <td> <s:link beanclass ="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="downloadBarcode"> Download
                <s:param name="grnLineItem" value="${grnLineItem.id}"/>
                <%--<s:param name="grnLineItem" value="${grnLineItem.id}"/>--%>
                <s:param name="grn" value="${ica.grn.id}"/>
            </s:link></td>

          </tr>
        </c:forEach>
          <tr>
              <td colspan="2"> <s:link class=" button_green" style="width: 150px; height: 16px; align_right" beanclass ="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="downloadAllBarcode"> Download All
                  <s:param name="grn" value="${ica.grn.id}"/>
            </s:link>

              </td>
          </tr>

      </table>

    </div>
    
    <%-- <c:if test="${ica.isBrightSupplier}">
    <div id = "freezeDiv">
    <s:link class=" button_green" style="width: 150px; height: 16px;" beanclass ="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="freezeCheckin"> Freeze Checkin
                  <s:param name="grn" value="${ica.grn.id}"/>
            </s:link>
    </div></c:if> --%>
    <script type="text/javascript">
	    $(document).ready(function() {
		    $('.chkInInfo').each(function(){
			     var qty = Number($(this).find('.chkInInfoQty').text());
			     var upc = Number($(this).find('.chkInQty').text());
			    if(qty != upc)
			    {
				    $(this).css('background-color','#FFA07A');
			    }

		    });
		    $('.invCheckin').click(function(event){
		    	var cp = $('#costPrice').val();
		    	var mrp = $('#mrp').val();
		    	var qty = $('#qty').val();
	    		if (cp == "" || mrp == "" || qty=="") {
					alert("Qty, CP, Mrp  fields are compulsory.");
					return false;
				}
	    		if (isNaN(cp) || isNaN(mrp) || cp < 0 || mrp < 0 || isNaN(qty)||qty<0) {
					alert("Qty, CP, Mrp  fields are compulsory.");
					return false;
				}
	    		if(brightSupplier){
	    			if(!isNaN(qty)&&(qty==0 || qty>1)){
	    				alert("Only one unit can be checked in");
	    				return false;
	    			}
	    		}
		    	if(parseFloat(cp)>parseFloat(mrp)){
		    		alert("Cost Price cannot be greater than MRP");
		    		return false;
		    	}
		    	
			    //$(this).css("display", "none");
			    event.preventDefault();
			    var saveBtn = $(this);
			    saveBtn.hide();
			    $.getJSON(
					    $('#validationLink').attr('href'), {upc : $('.variant').val(), costPrice : $('#costPrice').val(), mrp : $('#mrp').val(), barcode: $('#barcode').val()},
					    function (res) {
						    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
						    	$('#checkinForm').attr('action', $('#checkinForm').attr('action') + "?save=");
							    $('#checkinForm').submit();
							    $(this).css("display", "none");
						    } else {
							    var confirmMessage = confirm(res.message);
							    if(confirmMessage == true) {
							    	$('#checkinForm').attr('action', $('#checkinForm').attr('action') + "?save=");
								    $('#checkinForm').submit();
								    $(this).css("display", "none");
							    } else {
							    	 saveBtn.show();
								    return false;
							    }
						    }
					    }
			    );
		    });
	    });
    </script>
  </s:layout-component>
</s:layout-render>