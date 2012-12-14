<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.warehouse.Warehouse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" var="mpa"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode Region Zone">

	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function() {
				$('#submit').click(function() {
					if ($('#pin').val() == '' || $('#region').val() == '' || $('#group').val() == '' || $('#warehouse').val() == '') {
						alert('Enter All Values');
						return false;
					}
					return true;
				})


			});
		</script>
		<link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>
		<%
			WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
			pageContext.setAttribute("warehouseList", warehouseService.getServiceableWarehouses());
		%>
	</s:layout-component>

	<s:layout-component name="content">
		<div>
			<div style=" float:left;display:inline-block; %">
				<fieldset>
					<legend>Add Pincode Region</legend>
					<s:form id="regionform" beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
						<s:hidden name="pincodeRegionZone" value="${mpa.pincodeRegionZone.id}"/>						
						<table align="center" >
							<tr>
								<td><label>Pincode</label></td>
								<td><s:text name="pincodeRegionZone.pincode.pincode" id="pin"/></td>
								<td><s:submit name="searchPincodeRegion" value="Search"/></td>
							</tr>
							<tr>
								<td><label>Region</label></td>
								<td><s:select name="pincodeRegionZone.regionType" id="region">
									<s:option value="">--Select Region--</s:option>
									<hk:master-data-collection service="<%=MasterDataDao.class%>"
									                           serviceProperty="regionTypeList" value="id"
									                           label="name"/>
								</s:select>
								</td>
							</tr>
							<tr>
								<td><label>Group</label></td>
								<td>
									<s:select name="pincodeRegionZone.courierGroup" id="group">
										<s:option value="">--Select Group--</s:option>
										<hk:master-data-collection service="<%=MasterDataDao.class%>"
										                           serviceProperty="courierGroupList" value="id"
										                           label="name"/>
									</s:select>
								</td>
							</tr>
							<tr>
								<td><label>Warehouse</label></td>
								<td><s:select name="pincodeRegionZone.warehouse" id="warehouse">
									<s:option value="">--Select Warehouse--</s:option>
									<s:options-collection collection="${warehouseList}" value="id" label="name"/>
								</s:select>

								</td>
							</tr>

						</table>
						   <div>
								<s:submit id="submit" name="savePincodeRegion" value="Save"/>
						     </div>
					</s:form>
				</fieldset>
			</div>

			<div style="display:inline-block;">
				<table align="center" class="cont"> Available Pincode Region :
					<thead>
					<tr>
						<th>Pincode</th>
						<th>Warehouse</th>
						<th>Region Type</th>
						<th>Courier Group</th>
					</tr>
					</thead>
					<c:forEach items="${mpa.pincodeRegionZoneList}" var="prz" varStatus="ctr">
						<tr>
							<td>${prz.pincode.pincode}</td>
							<td>${prz.warehouse.name}</td>
							<td>${prz.regionType.name}</td>
							<td>${prz.courierGroup.name}</td>

						</tr>
					</c:forEach>
				</table>

			</div>
		 <div style="display:inline-block;">
			 <s:link beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" event="showRemainingPrz">
				Remaining Pincode Region
			 </s:link>

		 </div >

		</div>
	</s:layout-component>

</s:layout-render>