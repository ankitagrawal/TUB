<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle=" Create Cycle Count">
	<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cycle"/>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="heading">
		Create Cycle Count
	</s:layout-component>

	<s:layout-component name="content">


		<div id="errordiv" style="font-weight: bold;font-size: 12px;margin-bottom: 30px;">
					<span style="color:red;">${cycle.message}</span>			
		</div>

		<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
			<s:hidden name="cycleCount"  value="${cycle.cycleCount}"/>
			<div>
				<ul>
					<li>
						<label>Brand/ProductId/ProductVariantId*: </label><s:text id="audit-by" name="auditBy"/><br/>
					</li>					
					<li>
						<label> Cycle Count Type</label>
						<s:select id="countType" name="cycleCountType">
							<s:option value="">--Select Type --</s:option>
							<s:option value="1">Brand</s:option>
							<s:option value="2">Product</s:option>
							<s:option value="3">Product Variant</s:option>
						</s:select>
					</li>
				</ul>
			</div>
			<s:submit name="saveCycleCount" value="Save" id="subBtn"/>
		</s:form>
	</s:layout-component>
</s:layout-render>
<script type="text/javascript">
	$(document).ready(function () {
		$('#subBtn').click(function () {
			if ($('#audit-by').val().trim() === "") {
				alert("Enter Brand/Product!");
				return false;
			}
			else if ($('#countType').val().trim() === "") {
					alert("Select Cycle Count Type!");
					return false;
				}
            $('#subBtn').hide();
			return true;
		});
	});
</script>