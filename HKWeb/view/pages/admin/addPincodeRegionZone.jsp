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

                $('.addRowButton').click(function () {
                    var lastIndex = $('.lastRow').attr('count');
                    if (!lastIndex) {
                        lastIndex = -1;
                    }
                    $('.lastRow').removeClass('lastRow');

                    var nextIndex = eval(lastIndex + "+1");
                    var newRowHtml =
                            '<tr count="' + nextIndex + '" class="lastRow przRow">' +
                                    '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                                    '<td>' +
                                    '<input type="text" name="pincodeRegionZoneList[' + nextIndex + '].pincode" class="pincode">' +
                                    '</td>' +
                                    '<td>' +
                                    '<select name="pincodeRegionZoneList[' + nextIndex + '].courierGroup" class="courierGroup">' +
                                    '<option value="">--Select--</option>' +
                                     <c:forEach items="${aprza.courierGroupList}" var="courierGroup">
                                    '<option value="' + ${courierGroup.id} + '"> ' + "${courierGroup.name}" + '</option>' +
                                     </c:forEach>
                                    '</select>' +
                                    '</td>' +
                                    '<td>' +
                                    '<select name="pincodeRegionZoneList[' + nextIndex + '].regionType" class="regionType">' +
                                    '<option value="">--Select--</option>' +
                                     <c:forEach items="${aprza.regionTypeList}" var="regionType">
                                    '<option value="' + ${regionType.id} + '"> ' + "${regionType.name}" + '</option>' +
                                     </c:forEach>
                                    '</select>' +
                                    '</td>' +
                                    '<td>' +
                                    '<select name="pincodeRegionZoneList[' + nextIndex + '].warehouse" class="warehouse">' +
                                    '<option value="">--Select--</option>' +
                                     <c:forEach items="${aprza.warehouseList}" var="warehouse">
                                    '<option value="' + ${warehouse.id} + '"> ' + "${warehouse.identifier}" + '</option>' +
                                    </c:forEach>
                                    '</select>' +
                                    '</td>' +
                                    '</tr>';

                    $('.pincode-region-table').append(newRowHtml);
                    return false;
                });

				$('.saveall').click(function(e) {
                    $.each($('.przRow'), function() {
                        var currentRow = $(this);
                        var pincode = currentRow.find('.pincode').val();
                        var courierGroup = currentRow.find('.courierGroup').val();
                        var regionType = currentRow.find('.regionType').val();
                        var warehouse = currentRow.find('.warehouse').val();
                        if (pincode == "" || courierGroup == "" || regionType == "" || warehouse == "") {
                            alert('Enter All Values');
                            e.preventDefault();
                            return false;
                        }
                        return true;
                    });
				});

				$('.pinsearch').click(function(){
					var pin = $('#pin').val();
					if(pin == null || pin.trim() == ''){
						alert('Enter Pincode');
						return false;
					}
					return true;

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

            <s:form id="regionform" beanclass="com.hk.web.action.admin.courier.AddPincodeRegionZoneAction">

                <div style="display:inline-block; %">

                    <fieldset>

                        <legend>Add and Update Pincode Region</legend>

                        <table>
                            <tr>
                                <td>
                                    <label>Pincode</label>
                                </td>

                                <td><s:text name="pincodeRegionZone.pincode"   id="pin"/></td>

                                <td>
                                    <label>Courier Group</label>
                                </td>

                                <td>
                                    <s:select name="pincodeRegionZone.courierGroup" id="group">
                                        <s:option value="">--Select Courier Group--</s:option>
                                        <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                                   serviceProperty="courierGroupList" value="id"
                                                                   label="name"/>
                                    </s:select>
                                </td>

                                <td>
                                    <label>Region</label>
                                </td>

                                <td>
                                    <s:select name="pincodeRegionZone.regionType" id="region">
                                        <s:option value="">--Select Region--</s:option>
                                        <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                               serviceProperty="regionTypeList" value="id"
                                                               label="name"/>
                                    </s:select>
                                </td>

                                <td>
                                    <label>Warehouse</label>
                                </td>

                                <td>
                                    <s:select name="pincodeRegionZone.warehouse" id="warehouse">
                                        <s:option value="">--Select Warehouse--</s:option>
                                        <s:options-collection collection="${warehouseList}" value="id" label="identifier"/>
                                    </s:select>
                                </td>

                            </tr>

                        </table>

                        <div>
                            <s:submit name="searchPincodeRegion" class="pinsearch" value="Search"/>
                        </div>

                    </fieldset>

                </div>

                <div style="display:inline-block;">

                    <c:if test="${(aprza.pincodeRegionZoneList != null) &&(fn:length(aprza.pincodeRegionZoneList) > 0)}">


                        <table class="pincode-region-table cont"> Available Pincode Region :
                            <thead>
                            <tr>
                                <th>S.No</th>
                                <th>Pincode</th>
                                <th>Courier Group</th>
                                <th>Region Type</th>
                                <th>Warehouse</th>
                            </tr>
                            </thead>

                            <c:forEach items="${aprza.pincodeRegionZoneList}" var="prz" varStatus="ctr">

                                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow przRow':'przRow'} przList">

                                    <td>
                                        ${ctr.index+1}.
                                        <s:hidden name="pincodeRegionZoneList[${ctr.index}].id" value="${prz.id}"/>
                                        <input type="hidden" name="pincodeRegionZoneList[${ctr.index}].pincode"
                                               value="${prz.pincode.pincode}"/>
                                    </td>

                                    <td>
                                        ${prz.pincode.pincode}
                                    </td>

                                    <td>
                                        ${prz.courierGroup.name}
                                        <s:hidden name="pincodeRegionZoneList[${ctr.index}].courierGroup" value="${prz.courierGroup.id}"/>
                                    </td>

                                    <td>
                                        <s:select name="pincodeRegionZoneList[${ctr.index}].regionType" id="region"
                                                  value="${prz.regionType.id}" class="regionType">
                                            <s:option value="">--Select Region--</s:option>
                                            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                                       serviceProperty="regionTypeList" value="id"
                                                                       label="name"/>
                                        </s:select>
                                    </td>

                                    <td>
                                        ${prz.warehouse.identifier}
                                        <s:hidden name="pincodeRegionZoneList[${ctr.index}].warehouse" value="${prz.warehouse.id}"/>
                                    </td>

                                </tr>

                            </c:forEach>

                        </table>

                        <div>
                            <a href="#" class="addRowButton" style="font-size:1.2em">Add new row</a> <br/> <br/>
                            <s:submit  class ="saveall" name="savePincodeRegionList" value="Save"/>
                        </div>

                    </c:if>

                </div>

            </s:form>

        </div>

	</s:layout-component>

</s:layout-render>