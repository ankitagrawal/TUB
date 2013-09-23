<%@page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" var="ibaa" />
<%-- <c:set name ="warehouseId" value ="${ibaa.warehosue.id }" /> --%>

<s:layout-render name="/layouts/inventory.jsp" pageTitle="Upload Bin Allocation">

	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/healthkart/js/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="/healthkart/js/jquery.hkCommonPlugins.js"></script>
		<script type="text/javascript">
		$('#excelUpload').live("click", function() {
		      var filebean = $('#filebean').val();
		      if (filebean == null || filebean == '') {
		        alert('choose file');
		        return false;
		      }
		    });
		</script>
	</s:layout-component>
	<s:layout-component name="content">

		<div class="left">
			<div style="margin-top: 20px" height="500px" align="center">
				<s:form beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" autocomplete="off">
					<s:hidden name="warehouse" value="${ibaa.warehouse.id }" />
					<div align="center"><label><font size="6px">Upload Bin Allocation Notepad</font></label></div><br><br><br>
					<p>
						<label><strong>Browse to Upload: </strong></label>
						<s:file id="filebean" name="fileBean" size="30" />
					</p>

					<div width="100px" align="center">
						<s:submit id="excelUpload" name="parseBinAllocationFile" value="Upload" style="padding:2px;" class="cta button_green" />
					</div>
					<br>
					</s:form>
			</div>
		</div>


	</s:layout-component>

</s:layout-render>
