<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Calculate Shipment Cost">
    <s:useActionBean beanclass="com.hk.web.action.admin.shipment.ShipmentCostCalculatorAction" var="calculator"/>
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getServiceableWarehouses());
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

                        <div class="clear"></div>
                        <s:label name="days" class="label">No. Of Days</s:label>
                        <s:text name="days" style="width:200px" class="text"/>

                        <div style="margin-top:15px;"></div>
                        <s:submit name="calculateCourierCostingForShippingOrder" value="Get Shipping Cost for an SO"/>
                        <%--<s:submit name="calculateCourierCostingForShippingOrderByAntTaskMethod" value="Get Shipping Cost for an SO (Ant task)"/>--%>
                        <shiro:hasAnyRoles name="<%=RoleConstants.ADMIN%>">
                            <s:submit name="saveActualShippingCostForShippingOrder"
                                      value="Save Actual Shipping Cost for SO's"/>
                        </shiro:hasAnyRoles>
                    </fieldset>
                </s:form>
            </div>

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