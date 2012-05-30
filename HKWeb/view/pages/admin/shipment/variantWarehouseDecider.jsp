<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Decide Warehouse">
    <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.VariantPreferredWarehouseAction" var="calculator"/>
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getServiceableWarehouses());
    %>

    <s:layout-component name="heading">
        Decide Preferred Warehouse
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
                <s:form beanclass="com.hk.web.action.admin.warehouse.VariantPreferredWarehouseAction">
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <s:label name="productVariant" class="label">Variant ID</s:label>
                        <s:text name="productVariant" style="width:200px" class="text"/>

                        <div class="clear"></div>

                        <s:label name="pincode" class="label">Pincode (Valid Dest)</s:label>
                        <s:text name="pincode" style="width:200px" class="text"/>

                        <div class="clear"></div>

                        <s:label name="cod" class="label">COD ?</s:label>
                        <s:checkbox name="cod"/>

                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <s:submit name="decideWareHouse" value="Decide Warehouse"/>
                    </fieldset>
                </s:form>
            </div>

            <div class="clear"></div>
            <div style="margin-top:40px;"></div>
            <div style="float: left; width:40%">
                <fieldset>

                    Applicable Warehouse and corresponding courier cost

                    <c:forEach items="${calculator.wareHouseCourierCostingMap}" var="wareHouseCourierCostingMap">
                        <div class="clear"></div>
                        ${wareHouseCourierCostingMap.key.name} <br/>
                        <c:forEach items="${wareHouseCourierCostingMap.value}" var="courierCostingMap">
                            <div class="clear"></div>
                            <strong class='num'> ${courierCostingMap.key.name} --><fmt:formatNumber value="${courierCostingMap.value}" type="currency" currencySymbol="Rs "/></strong>
                        </c:forEach>
                        <br/> <br/>
                    </c:forEach>

                </fieldset>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>