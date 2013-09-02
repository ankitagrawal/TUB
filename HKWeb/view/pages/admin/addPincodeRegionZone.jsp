<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.warehouse.Warehouse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddPincodeRegionZoneAction" var="aprza"/>

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
				});

				$('.pinsearch').click(function(){
					var pin = $('#pin').val();
					if(pin == null || pin.trim() == ''){
						alert('Enter Pincode');
						return false;
					}
					return true;

				})

				$('.saveall').click(function() {
					var row = $('.przlist');
					var error = false;
					row.each(function(col) {
						$(this).find('td').each(function() {
							if ($(this).find('select').length) {
								var selectval = $(this).find('select').val();
								if (selectval == null || selectval == '') {
									alert('Select Region Type');
									error = true;
									return false;
								}
							}
						});
						if (error) {
							return false;
						}
					});
					if (error) {
						return false;
					}
					else {
						return true;
					}
				});


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
					<s:form id="regionform" beanclass="com.hk.web.action.admin.courier.AddPincodeRegionZoneAction">
						<table align="center">
							<tr>
								<td><label>Pincode</label></td>
								<td><s:text name="pincodeRegionZone.pincode"   id="pin"/></td>
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
									<s:options-collection collection="${warehouseList}" value="id" label="identifier"/>
								</s:select>

								</td>
							</tr>

						</table>
						<div>
                            <s:submit name="searchPincodeRegion" class="pinsearch" value="Search"/>
							<s:submit id="submit" name="savePincodeRegion" value="Save"/>
						</div>
					</s:form>
				</fieldset>
			</div>

			
			<div style="display:inline-block;">
				<c:if test="${(aprza.pincodeRegionZoneList != null) &&(fn:length(aprza.pincodeRegionZoneList) > 0)}">
				<s:form beanclass="com.hk.web.action.admin.courier.AddPincodeRegionZoneAction">
					<table align="center" class="cont"> Available Pincode Region :
						<thead>
						<tr>
							<th>Pincode</th>
							<th>Warehouse</th>
							<th>Region Type</th>
							<th>Courier Group</th>
						</tr>
						</thead>

						<c:forEach items="${aprza.pincodeRegionZoneList}" var="prz" varStatus="ctr">
							<s:hidden name="pincodeRegionZoneList[${ctr.index}].id" value="${prz.id}"/>
							<input type="hidden" name="pincodeRegionZoneList[${ctr.index}].pincode"
							       value="${prz.pincode.pincode}"/>
							<tr class="przlist">
								<td>${prz.pincode.pincode}</td>

								<td>
									${prz.warehouse.identifier}
									<s:hidden name="pincodeRegionZoneList[${ctr.index}].warehouse" value="${prz.warehouse.id}"/>

									<%--<s:select name="pincodeRegionZoneList[${ctr.index}].warehouse" id="warehouse"--%>
								              <%--value="${prz.warehouse.id}">--%>
									<%--<s:option value="">--Select Warehouse--</s:option>--%>
									<%--<s:options-collection collection="${warehouseList}" value="id" label="name"/>--%>
								<%--</s:select>--%>

								</td>

								<td>
									<s:select name="pincodeRegionZoneList[${ctr.index}].regionType" id="region"
									          value="${prz.regionType.id}">
										<s:option value="">--Select Region--</s:option>
										<hk:master-data-collection service="<%=MasterDataDao.class%>"
										                           serviceProperty="regionTypeList" value="id"
										                           label="name"/>
									</s:select></td>
								<td>
									${prz.courierGroup.name}
									<s:hidden name="pincodeRegionZoneList[${ctr.index}].courierGroup" value="${prz.courierGroup.id}"/>

								</td>

							</tr>

						</c:forEach>
					</table>
					<div>
						<s:submit  class ="saveall" name="savePincodeRegionList" value="saveAll"/>
					</div>
				</s:form>
				</c:if>
			</div>



			<div style="display:inline-block; float:right;padding-right:40px;">
				<span style="font:bold;color:darkolivegreen;"><s:link
						beanclass="com.hk.web.action.admin.courier.AddPincodeRegionZoneAction" event="showRemainingPrz">
					Remaining Pincode Region
				</s:link> </span>

				<c:if test="${(aprza.pincodeList  != null )&& (fn:length(aprza.pincodeList) > 0)}">
					<table align="center" class="cont">
						<thead>
						<tr>
							<th>Pincode</th>
						</tr>
						</thead>
						<c:forEach items="${aprza.pincodeList}" var="pinc" varStatus="ctr">
							<tr>
								<td>${pinc.pincode}</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
			</div>

		</div>
	</s:layout-component>

</s:layout-render>