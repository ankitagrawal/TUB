<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.admin.pact.service.courier.CourierService" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Calculate Shipment Cost">
    <s:useActionBean beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction" var="calculator"/>
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        CourierService courierService = ServiceLocatorFactory.getService(CourierService.class);
        pageContext.setAttribute("whList", warehouseService.getServiceableWarehouses());
        pageContext.setAttribute("applicableCourierList", courierService.getAllCouriers());
    %>

    <s:layout-component name="heading">
        Calculate Shipment Cost
    </s:layout-component>
    <s:layout-component name="htmlHead">

        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <style type="text/css">
            .text {
                margin-left: 100px;
                margin-top: 20px;
            }

            .label {
                margin-top: 20px;
                float: left;
                margin-left: 10px;
                width: 150px;
            }

        </style>

    </s:layout-component>
    <s:layout-component name="content">
        <div>
            <div style="float: left; width:30%">
                <s:form beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction">
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <li>
                            <label>Pincode (Valid Dest)<label>
                                    <s:text name="pincode" style="width:100px"/>
                                <div class="clear"></div>
                        </li>
                        <li>
                            <label>Weight (gm)<label>
                                    <s:text name="weight" style="width:100px"/>
                                <div class="clear"></div>
                        </li>
                        <li>
                            <label>Amount (SO Payment Amount)</label>
                            <s:text name="amount" style="width:100px"/>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <label>Source Warehouse</label>
                            <s:select name="srcWarehouse">
                                <s:option value="">-Select-</s:option>
                                <c:forEach items="${whList}" var="wh">
                                    <s:option value="${wh.id}">${wh.identifier}</s:option>
                                </c:forEach>
                            </s:select>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <label>COD ?</label>
                            <s:checkbox name="cod"/>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div style="margin-top:15px;"></div>
                                <s:submit name="calculateViaPincode" value="Get Possible Shipping Cost"/>
                    </fieldset>
                </s:form>
            </div>

            <div style="float: left; width:30%">
                Applicable Couriers

                <c:forEach items="${calculator.courierCostingMap}" var="courierCostingMap">
                    <div class="clear"></div>
                    ${courierCostingMap.key.name}
                </c:forEach>

                <fieldset>

                    <legend> Cheapest Courier Costs</legend>

                    <c:forEach items="${calculator.courierCostingMap}" var="courierCostingEntry">
                        <div class="clear"></div>
                        <strong class='num'>  ${courierCostingEntry.key.name} -->
                            Rs ${courierCostingEntry.value} </strong>
                    </c:forEach>

                </fieldset>
            </div>

            <div style="float: right; width:40%">
                <s:form beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction">
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <div class="clear"></div>
                        <li>
                            <label>SO Order ID</label>
                            <s:text name="shippingOrderId" style="width:200px"/>
                        </li>
                        <li>
                            <div class="clear"></div>
                            <label>Start
                                date</label><s:text class="date_input startDate" style="width:150px"
                                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                    name="shippedStartDate"/>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <label>End
                                date</label><s:text class="date_input endDate" style="width:150px"
                                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                    name="shippedEndDate"/>
                        </li>
                        <li>
                            <div class="clear"></div>
                            <label style="width: 50px;">Weight (KG)</label>
                            <s:text name="weight" style="width:50px"/>
                            <span style="font-size: 10px;">if system weight needs to be updated</span>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <label>Courier</label>
                            <s:select name="applicableCourier" class="courierService">
                                <s:option value="">All Couriers</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="availableCouriers" value="id"
                                                           label="name"/>
                            </s:select>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <label>Override Historical Shipment Cost ?</label>
                            <s:checkbox name="overrideHistoricalShipmentCost"/>
                            <div class="clear"></div>
                        </li>
                        <div style="margin-top:15px;"></div>
                        <s:submit name="calculateCourierCostingForShippingOrder" value="Calculate Shipping Cost"/>
                        <shiro:hasPermission name="<%=PermissionConstants.SAVE_SHIPPING_COST%>">
                            <s:submit name="saveHistoricalShipmentCost" value="Save Shipping Cost (By Date)"/>
                            <s:submit name="saveActualShippingCostForShippingOrder"
                                      value="Save Shipping Cost (Single SO)"/>
                        </shiro:hasPermission>
                    </fieldset>
                </s:form>
            </div>

        </div>

        <div class="clear"></div>

        <div>
            <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                <s:form beanclass="com.hk.web.action.admin.order.split.BulkOrderSplitterAction">
                    <fieldset class="top_label">
                        <legend> Bulk BO Splitter</legend>

                        <div class="clear"></div>

                        <div style="margin-top:15px;"></div>
                        <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                            <s:submit name="bulkSplitOrders" value="Bulk Split Orders"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
                <div class="clear"></div>
                <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction">
                    <fieldset class="top_label">
                        <legend> Bulk SO Escalate</legend>

                        <div class="clear"></div>

                        <div style="margin-top:15px;"></div>
                        <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                            <s:submit name="bulkEscalateShippingOrder" value="Bulk Escalate SO"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
                <div class="clear"></div>
                <s:form beanclass="com.hk.web.action.admin.util.MarkVariantsInStockAction">
                    <fieldset class="top_label">
                        <legend> Mark Variants In Stock</legend>
                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                            <s:submit name="quickInventoryCheck" value="Quick Inventory Check"/>
                            <s:submit name="setVariantsInStockHavingInventory" value="Mark Variants In Stock"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
            </shiro:hasAnyRoles>

        </div>
    </s:layout-component>
</s:layout-render>