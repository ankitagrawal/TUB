<%@page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" var="ibaa"/>
<%-- <c:set name ="warehouseId" value ="${ibaa.warehosue.id }" /> --%>

<s:layout-render name="/layouts/inventory.jsp" pageTitle="Bin Allocation">

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/healthkart/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="/healthkart/js/jquery.hkCommonPlugins.js"></script> 
<script type="text/javascript">

$(document).ready(function () {
	$("#firstLocation").focus();
	$("#resposeLabel").css("background-color", "");
	$("#firstLocation").bind('input propertychange', function(){
		var location = $(this).val();
		  if(location.length >0){
		    $("#barcode").focus();
		  }
		});
	
	$("#barcode").bind('input propertychange', function(){
		var location = $(this).val();
		  if(location.length >0){
		    $("#finalLocation").focus();
		  }
		});
	
	$("#finalLocation").bind('input propertychange', function(){
		var firstLocation = $("#firstLocation").val();
		var barcode = $("#barcode").val();
		var finalLocation = $("#finalLocation").val();
		var warehouseId = '${ibaa.warehouse.id}';
		if(firstLocation!=null && firstLocation.trim(firstLocation)!="" && barcode!=null && barcode.trim(barcode)!="" && 
				finalLocation.trim(finalLocation)!=null && finalLocation!=null){
			$.getJSON($('#saveBinLink').attr('href'), {
				firstLocation : firstLocation, barcode : barcode, finalLocation : finalLocation, warehouse : warehouseId
		      },
		          function(res) {
		            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
		            	$("#responseLabel").html('<h4>' + res.message + '</h4>');
		            	$("#firstLocation").focus();
		            	window.location.href = "${pageContext.request.contextPath}/admin/inventory/checkin/InventoryBinAllocation.action";
		            }else {
		            	$("#responseLabel").html('<h4>' + res.message + '</h4>'+". Reloading in 3 seconds");
		            	 $("#firstLocation").val('');
		            	 $("#barcode").val('');
		            	 $("#finalLocation").val('');
		            	 $("#finalLocation").val('');
		            	 $("#responseLabel").delay(6000);
		            	 window.location.href = "${pageContext.request.contextPath}/admin/inventory/checkin/InventoryBinAllocation.action";
		            }
		            });
		}
		else{
			$("#responseLabel").val('');
			$("#responseLabel").html("One of the input seems to be empty. Try Again");
			$("#responseLabel").css("background-color", "#FF0000");
			 $("#firstLocation").val('');
        	 $("#barcode").val('');
        	 $("#finalLocation").val('');
        	 $("#firstLocation").focus();
		}
		
	});
	
});
</script>
</s:layout-component>


        <s:layout-component name="content">
        
        <div style="display: none">
    	<s:link beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction"
            id="saveBinLink" event="saveBinForIndividualItem"></s:link>
  		</div>
        
        <div align="center"><label><font size="6px">Create Unique Barcodes</font></label></div><br><br><br>
        
        <div style="margin-top:20px" height="500px" align="center">
            <s:form beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" autocomplete="off">
            <s:hidden name="warehouse" value="${ibaa.warehouse.id }" />
            <label><strong><u>SCAN THE BAR CODE HERE</u></strong></label>
            <br><br>
            
            <table align="center">
            <tr>
            	<td><label>Enter Location: </label></td>
            	<td><s:text name="firstLocation" id="firstLocation"  style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
            </tr>
            <tr>
            	<td><label>Product Barcode: </label></td>
            	<td><s:text name="barcode" id="barcode" style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
            </tr>
            <tr>
            	<td><label>Enter Location Again: </label></td>
            	<td><s:text name="finalLocation" id="finalLocation"
                        style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
            </tr>
            </table>
            
				<div align="center"><label id="responseLabel"><font size="6px"></font></label></div>
                <!-- <div><s:submit name="save" value="Save"/></div> -->
            </s:form>
        </div>
        </s:layout-component>
</s:layout-render>