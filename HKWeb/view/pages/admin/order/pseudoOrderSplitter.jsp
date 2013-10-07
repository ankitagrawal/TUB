<%@ page import="java.util.List" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.inventory.SkuService" %>
<%@ page import="com.hk.domain.order.CartLineItem" %>
<%@ page import="com.hk.domain.sku.Sku" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Splitter">

    <%
        SkuService skuService = ServiceLocatorFactory.getService(SkuService.class);
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
            <div style="float: left; width:60%">
                <s:form beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction">
                    <fieldset class="top_label">
                        <legend> Enter Details</legend>
                        <s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>
                        <s:text name="gatewayOrderId" style="width:200px" class="text"/>

                        <div class="clear"></div>
                        <div style="margin-top:15px;"></div>
                        <s:submit name="splitOrderPractically" value="Split Order Practically" style="float:left"/>
                        <s:submit name="splitOrderIdeally" value="Split Order Ideally" style="float:left"/>
                        <s:submit name="splitViaNewSplitter" value="Split Order Via New Splitter" style="float:left"/>
                    </fieldset>
                </s:form>
            </div>

            <div class="clear"></div>

            <c:if test="${splitter.order != null}">
                <div class="box" style="width:100%">
                    <STRONG>Order Details</STRONG>
                    <div class="clear"></div>
                    Order Amount: ${splitter.order.amount}  &nbsp; &nbsp; IsCod: ${splitter.order.COD} &nbsp; &nbsp; Pincode: ${splitter.order.address.pincode.pincode}
                    <div class="clear"></div>
                    <table style="border:0px;">
                        <tr>
                            <th>Variant Id</th>
                            <th>Name</th>
                            <th>CostPrice</th>
                            <th>HK Price</th>
                            <th>Weight</th>
                            <th>Tax</th>
                        </tr>
                        <c:forEach items="${splitter.order.productCartLineItems}" var="cartLineItem">
                            <%
                                CartLineItem cartLineItem= (CartLineItem) pageContext.getAttribute("cartLineItem");
                                //List<Sku> skus = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
	                            List<Sku> skus = skuService.getSKUsForProductVariantAtServiceableWarehouses(cartLineItem.getProductVariant());
                                pageContext.setAttribute("skus", skus);
                            %>
                            <tr>
                                <td>
                                    ${cartLineItem.productVariant.id}
                                </td>
                                <td>${cartLineItem.productVariant.product.name}: <span class="small gry em">${cartLineItem.productVariant.optionsCommaSeparated}</span> </td>
                                <td> ${cartLineItem.productVariant.costPrice}</td>
                                <td> ${cartLineItem.productVariant.hkPrice}</td>
                                <td>${cartLineItem.productVariant.weight}</td>
                                <td><c:forEach items="${skus}" var="sku">${sku.warehouse.identifier}:${sku.tax.value}<br></c:forEach></td>
                            </tr>
                        </c:forEach>
                    </table>


                </div>

            </c:if>
            <div class="clear"></div>
            <div style="margin-top:40px;"></div>
            <div style="float: left; width:100%">

                Corresponding Shipping Orders

                <c:forEach items="${splitter.sortedDummyOrderMaps}" var="dummyOrderEntry">
                    <div class="clear"></div>

                    <div class="row" style="margin-left:20px; width: 100%;">
                        <label class="valueLabel bolderValue">Shipping Plus Tax Cost:</label>
                        <label class="nameLabel bolderValue">${dummyOrderEntry.value}</label>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-left:20px; width: 100%;">
                        <c:forEach items="${dummyOrderEntry.key}" var="dummyOrder">
                            <div style="float: right; margin-left: 10px; margin-right: 10px; width: 40%; height: 200px; padding: 15px">
                                <fieldset>
                                    <div>
                                        <div class="clear"></div>
                                        <div class="row">
                                            <label class="valueLabel">Warehouse</label>
                                            <label class="nameLabel bolderValue">${dummyOrder.warehouse.identifier}</label>
                                        </div>
                                        <div class="clear"></div>

                                        <c:forEach items="${dummyOrder.cartLineItemList}" var="cartLineItem">
                                            <div class="itemsRow">
                                                <label class="boldValue items">${cartLineItem.productVariant.product.name}: <span class="small gry em">${cartLineItem.productVariant.optionsCommaSeparated}</span></label>
                                            </div>
                                        </c:forEach>


                                        <div class="clear"></div>
                                        <div class="row">
                                            <label class="valueLabel">Tax Incurred:</label>
                                            <label class="nameLabel">${dummyOrder.taxIncurred}</label>
                                        </div>
                                        <div class="clear"></div>
                                        <div class="row">
                                            <label class="valueLabel">Courier:</label>
                                            <label class="nameLabel">${dummyOrder.dummySO.courier.name}</label>
                                        </div>

                                        <div class="clear"></div>
                                        <div class="row">
                                            <label class="valueLabel">Shipment Cost :</label>
                                            <label class="nameLabel">${dummyOrder.dummySO.shipmentCost}</label>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                    <div class="clear"></div>
                                </fieldset>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="clear"></div>
                </c:forEach>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
<style type="text/css">
    .nameLabel {
        margin-top: 10px;
    }

    .valueLabel {
        float: left;
        margin-left: 10px;
        width: 200px;
    }

    .items{
        float: left;
        margin-left: 10px;
        margin-top: 5px;
    }

    .itemsRow {
        margin-top: 10px;
    }

    .row {
        margin-top: 20px;
    }

    .bolderValue{
        font-weight: 900;
    }

    .boldValue{
        font-weight: 500;
    }

    .box{
        padding:10px;
        border:2px solid gray;
        margin:10px;
    }
</style>
