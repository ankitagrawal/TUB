<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.admin.pact.service.courier.CourierService" %>
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
            <div style="float: left; width:40%">
                <s:form beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction">
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <s:label name="pincode" class="label">Pincode (Valid Dest)</s:label>
                        <s:text name="pincode" style="width:200px" class="text"/>

                        <div class="clear"></div>

                        <s:label name="weight" class="label">Weight (gm)</s:label>
                        <s:text name="weight" style="width:200px" class="text"/>

                        <div class="clear"></div>

                        <s:label name="amount" class="label">Amount (SO Payment Amount)</s:label>
                        <s:text name="amount" style="width:200px" class="text"/>

                        <div class="clear"></div>

                        <s:label name="srcWarehouse" class="label">Source Warehouse</s:label>
                        <s:select name="srcWarehouse">
                            <s:option value="">-Select-</s:option>
                            <c:forEach items="${whList}" var="wh">
                                <s:option value="${wh.id}">${wh.name}</s:option>
                            </c:forEach>
                        </s:select>

                        <div class="clear"></div>

                        <s:label name="cod" class="label">COD ?</s:label>
                        <s:checkbox name="cod"/>

                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <s:submit name="calculateViaPincode" value="Get Possible Shipping Cost"/>
                    </fieldset>
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <s:label name="shippingOrderId" class="label">SO Gateway Order ID</s:label>
                        <s:text name="shippingOrderId" style="width:200px" class="text"/>

                        <li>
                            <label>Start
                                date</label><s:text class="date_input startDate" style="width:150px"
                                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="shippedStartDate"/>
                        </li>
                        <li>
                            <label>End
                                date</label><s:text class="date_input endDate" style="width:150px"
                                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="shippedEndDate"/>
                        </li>
                        <div class="clear"></div>
                        <s:label name="courier" class="label">Courier</s:label>
                        <c:forEach items="${applicableCourierList}" var="applicableCourier" varStatus="ctr">
                            <div class="clear"></div>
                            <label><s:checkbox name="applicableCourierList[${ctr.index}]"
                                               value="${applicableCourier.id}"/> ${applicableCourier.name}</label>
                        </c:forEach>
                        <div class="clear"></div>
                        <s:label name="overrideHistoricalShipmentCost"
                                 class="label">Override Historical Shipment Cost ?</s:label>
                        <div class="clear"></div>
                        <s:checkbox name="overrideHistoricalShipmentCost"/>
                        <div class="clear"></div>

                        <div style="margin-top:15px;"></div>
                        <s:submit name="calculateCourierCostingForShippingOrder" value="Get Shipping Cost for an SO"/>
                        <shiro:hasAnyRoles name="<%=RoleConstants.ADMIN%>">
                            <s:submit name="saveHistoricalShipmentCost" value="Save Historical Shipping Cost"/>
                            <s:submit name="saveHistoricalDemand" value="Save Historical Demand"/>
                            <s:submit name="saveActualShippingCostForShippingOrder"
                                      value="Save Actual Shipping Cost for SO's"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
            </div>
            <s:form beanclass="com.hk.web.action.admin.order.analytics.DemandStreamInjectorAction">
                <fieldset class="top_label">
                    <legend> Enter Details</legend>

                    <li>
                        <label>Start
                            date</label><s:text class="date_input startDate" style="width:150px"
                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="shippedStartDate"/>
                    </li>
                    <li>
                        <label>End
                            date</label><s:text class="date_input endDate" style="width:150px"
                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="shippedEndDate"/>
                    </li>
                    <div class="clear"></div>

                    <div style="margin-top:15px;"></div>
                    <shiro:hasAnyRoles name="<%=RoleConstants.ADMIN%>">
                        <s:submit name="saveHistoricalDemand" value="Save Historical Demand"/>
                    </shiro:hasAnyRoles>
                </fieldset>
            </s:form>
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
                <s:form beanclass="com.hk.web.action.admin.util.MarkVariantsInStockAction">
                    <fieldset class="top_label">
                        <legend> Mark Variants In Stock</legend>
                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                            <s:submit name="setVariantsInStockHavingInventory" value="Mark Variants In Stock"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
                <s:form beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction">
                    <fieldset class="top_label">
                        <legend> Find Payment</legend>
                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <shiro:hasAnyRoles name="<%=RoleConstants.GOD%>">
                            <s:text name="shippingOrderId" style="width:200px" class="text"/>
                            <s:text name="merchantId" style="width:200px" class="text"/>
                            <s:submit name="findIciciPayment" value="Find Icici Payment"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
            </shiro:hasAnyRoles>

            <div class="clear"></div>
            <div style="margin-top:40px;"></div>
            <div style="float: left; width:40%">
                Applicable Couriers

                <c:forEach items="${calculator.applicableCourierList}" var="courier">
                    <div class="clear"></div>
                    ${courier.name}
                </c:forEach>

                <fieldset>

                    <legend> Cheapest Courier Costs</legend>

                    <c:forEach items="${calculator.courierCostingMap}" var="courierCostingEntry">
                        <div class="clear"></div>
                        <strong class='num'>  ${courierCostingEntry.key.name} --> Rs ${courierCostingEntry.value} </strong>
                    </c:forEach>

                </fieldset>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>