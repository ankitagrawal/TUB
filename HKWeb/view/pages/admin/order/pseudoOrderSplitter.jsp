<%@ page import="com.hk.admin.pact.service.inventory.MasterInventoryService" %>
<%@ page import="com.hk.domain.order.CartLineItem" %>
<%@ page import="com.hk.domain.sku.Sku" %>
<%@ page import="com.hk.pact.service.inventory.SkuService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Splitter">

    <%
        SkuService skuService = ServiceLocatorFactory.getService(SkuService.class);
        MasterInventoryService masterInventoryService = ServiceLocatorFactory.getService(MasterInventoryService.class);
    %>

    <s:useActionBean beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction" var="splitter"/>

    <s:layout-component name="heading">
        Split Base Order
    </s:layout-component>
    <s:layout-component name="htmlHead">

        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
    <s:layout-component name="content">
        <div style="float: left; width:90%">
            <s:form beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction">
                <fieldset class="top_label">
                    <legend> Enter Details</legend>
                    <s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>
                    <s:text name="gatewayOrderId" style="width:200px" class="text"/>

                    <div class="clear"></div>
                    <div style="margin-top:15px;"></div>
                    <s:submit name="splitOrderPractically" value="Split Order Practically"
                              style="float:left;padding:3px;font-size:.9em;"/>
                    <s:submit name="splitOrderIdeally" value="Split Order Ideally"
                              style="float:left;padding:3px;font-size:.9em;"/>
                    <s:submit name="splitViaNewSplitter" value="Split Order Via New Splitter"
                              style="float:left;padding:3px;font-size:.9em;"/>
                </fieldset>
            </s:form>
        </div>

        <div class="clear"></div>

        <c:if test="${splitter.order != null}">
            <div class="box" style="width:100%">
                <strong>Order Details </strong> - Order Amount: ${splitter.order.amount} |
                IsCod: ${splitter.order.COD} | Pincode: ${splitter.order.address.pincode.pincode}
                <table style="border:0px;">
                    <tr>
                        <th>Variant Id</th>
                        <th>Name</th>
                        <th>MRP</th>
                        <th>Qty</th>
                        <th>Weight</th>
                        <th>SKU Details</th>
                    </tr>
                    <c:forEach items="${splitter.order.productCartLineItems}" var="cartLineItem">
                        <%
                            CartLineItem cartLineItem = (CartLineItem) pageContext.getAttribute("cartLineItem");
                            //List<Sku> skus = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
                            List<Sku> skus = skuService.getSKUsForProductVariantAtServiceableWarehouses(cartLineItem.getProductVariant());
                            pageContext.setAttribute("skus", skus);
                        %>
                        <tr>
                            <td>
                                    ${cartLineItem.productVariant.id}
                            </td>
                            <td style="max-width:33%;">${cartLineItem.productVariant.product.name}<br/>
                                <span style="font-size:.8em;">${cartLineItem.productVariant.optionsCommaSeparated}</span>
                            </td>
                            <td> ${cartLineItem.markedPrice}</td>
                            <td> ${cartLineItem.qty}</td>
                            <td>${cartLineItem.productVariant.weight}</td>
                            <td>
                                <%
                                    for (Sku sku : skus) {
                                        Long unitsForSplitting = masterInventoryService.getCheckedInUnits(Arrays.asList(sku), cartLineItem.getMarkedPrice())
                                                - masterInventoryService.getUnbookedLIUnits(Arrays.asList(sku), cartLineItem.getMarkedPrice());
                                %>

                                <span style="font-weight:<%=unitsForSplitting > 0 ? "bold" : ""%>"><%=sku.getWarehouse().getIdentifier()%> | <%=sku.getTax().getValue()%> | UnitsForSplitting:<%=unitsForSplitting%></span>
                                <br/>
                                <%
                                    }
                                %>

                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

        </c:if>
        <div class="clear"></div>
        <c:forEach items="${splitter.sortedDummyOrderMaps}" var="dummyOrderEntry" varStatus="combCtr">
            <div>
                <strong>Combination# ${combCtr.index+1}</strong> | <strong>(Shipping + Tax)
                Cost:</strong>${dummyOrderEntry.value}
                <hr/>
                <c:forEach items="${dummyOrderEntry.key}" var="dummyOrder">
                    <div style="float: left; margin-right:10px; width:45%">
                        <table>
                            <tr>
                                <th>Warehouse</th>
                                <th>${dummyOrder.warehouse.identifier}</th>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <c:forEach items="${dummyOrder.cartLineItemList}" var="cartLineItem">
                                        <strong>${cartLineItem.productVariant.product.name}</strong><br/>
                                        <span style="font-size:.8em">${cartLineItem.productVariant.optionsCommaSeparated}</span><br/>
                                    </c:forEach>

                                </td>
                            </tr>
                            <tr>
                                <td>Tax Incurred</td>
                                <td>${dummyOrder.taxIncurred}</td>
                            </tr>
                            <tr>
                                <td>Courier</td>
                                <td>${dummyOrder.dummySO.courier}</td>
                            </tr>
                            <tr>
                                <td>Shipping Cost</td>
                                <td>${dummyOrder.dummySO.shipmentCost}</td>
                            </tr>
                        </table>

                    </div>
                </c:forEach>
                <br/>
            </div>
            <div class="clear"></div>
        </c:forEach>
    </s:layout-component>
</s:layout-render>
<style type="text/css">
    table {
        border-collapse: collapse;
        width: 100%
    }

    table tr td {
        padding: 5px;
        border: 1px solid #CCC;
    }

    table tr th {
        padding: 5px;
        border: 1px solid #CCC;
        text-align: left;
    }

    h2t {
        margin: 0;
        padding: 0;
    }

    h1 {
        margin: 0;
        padding: 0;
    }

    table.header tr td {
        border: none;
        vertical-align: top
    }

    .clear {
        clear: both;
        display: block;
        overflow: hidden;
        visibility: hidden;
        width: 0;
        height: 0
    }

    .messages {
        color: red;
        font: 10px;

    }

</style>
