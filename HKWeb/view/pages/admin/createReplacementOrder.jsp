<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatusDelivrd" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatusRTO_instantiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusSO_returned" value="<%=EnumShippingOrderStatus.SO_Returned.getId()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create Replacement Order">
    <s:useActionBean beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction"
                     var="replacementOrderBean"/>
    <s:layout-component name="heading">
        Create Replacement Order
    </s:layout-component>

    <s:layout-component name="content">
        <script>
            $(document).ready(function() {
                $('#shippingOrderId').focus();

                $('#is-replacement-radio').click(function(event) {
                    event.preventDefault();
                    $('#is-rto').hide();
                    $('#is-replacement').slideDown();
                });

                $('#is-rto-radio').click(function(event) {
                    event.preventDefault();
                    $('#is-replacement').hide();
                    $('#is-rto').slideDown();
                });
            })
        </script>
        <fieldset class="right_label">
            <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
                <label>Search Shipping Order</label>
                <br/><br/>
                <s:text name="shippingOrderId" id="shippingOrderId" style="width:200px;"/>
                <br/>
                <br/>
                <s:submit name="searchShippingOrder" value="Search"/>
            </s:form>
        </fieldset>

        <c:if test="${!empty replacementOrderBean.shippingOrder}">
            <fieldset style="float:left;">
                <table>
                        <%--          <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrderId}"/>--%>
                    <tr>
                        <td><h5>CustomerName:</h5></td>
                        <td>${replacementOrderBean.shippingOrder.baseOrder.user.name}</td>
                        <td><h5>SO date:</h5></td>
                        <td>${replacementOrderBean.shippingOrder.createDate}</td>
                    </tr>
                    <tr>
                        <td><h5>Email:</h5></td>
                        <td>${replacementOrderBean.shippingOrder.baseOrder.user.email}
                        </td>
                        <td><h5>Address:</h5></td>
                        <td>
                                ${replacementOrderBean.shippingOrder.baseOrder.address.city}<br/>
                                ${replacementOrderBean.shippingOrder.baseOrder.address.state}-
                            (${replacementOrderBean.shippingOrder.baseOrder.address.pin})<br/>
                            Ph: ${replacementOrderBean.shippingOrder.baseOrder.address.phone}
                        </td>
                    </tr>
                    <tr>
                        <td>
                           <h5>Status</h5>
                        </td>
                        <td>
                            ${replacementOrderBean.shippingOrder.orderStatus.name}
                        </td>
                        <td>
                            <c:if test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusDelivrd
                            || replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusShipped}">
                                <s:link href="#" id="is-replacement-radio">
                                    <h5>Create RO<br />for replacement</h5>
                                </s:link>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusRTO_instantiated
                            || replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusSO_returned}">
                                <s:link href="#" id="is-rto-radio">
                                    <h5>Create RO<br />for Returned Goods</h5>
                                </s:link>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </fieldset>

            <fieldset style="display:none;" id="is-rto">
                <h4>Returned to origin</h4>
                <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
                    <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrder.id}"/>
                    <table border="1">
                        <thead>
                        <th>S No.</th>
                        <th>Product</th>
                        <th>Original Qty</th>
                        <th>Replacement Qty</th>
                        </thead>
                        <s:hidden name="isRto" value="1"/>
                        <c:forEach items="${replacementOrderBean.lineItems}" var="lineItem" varStatus="lineItemCtr">
                            <s:hidden name="lineItems[${lineItemCtr.index}].sku" value="${lineItem.sku}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].cartLineItem"
                                      value="${lineItem.cartLineItem.id}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].costPrice" value="${lineItem.costPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].markedPrice"
                                      value="${lineItem.markedPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].hkPrice" value="${lineItem.hkPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].discountOnHkPrice"
                                      value="${lineItem.discountOnHkPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].tax" value="${lineItem.tax}"/>

                            <tr>
                                <td>${lineItemCtr.count}</td>
                                <td>${lineItem.cartLineItem.productVariant.product.name}</td>
                                <td>${lineItem.qty}</td>
                                <td>
                                    <s:hidden name="lineItems[${lineItemCtr.index}].qty" value="${lineItem.qty}"/>
                                        ${lineItem.qty}
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <s:submit name="createReplacementOrder" value="Generate Replacement Order"/>
                </s:form>
            </fieldset>

            <fieldset style="display:none;" id="is-replacement">
                <h4>Replacement</h4>
                <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
                    <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrder.id}"/>
                    <table border="1">
                        <thead>
                        <th>S No.</th>
                        <th>Product</th>
                        <th>Original Qty</th>
                        <th>Replacement Qty</th>
                        </thead>
                        <s:hidden name="isRto" value="0"/>
                        <c:forEach items="${replacementOrderBean.lineItems}" var="lineItem" varStatus="lineItemCtr">
                            <s:hidden name="lineItems[${lineItemCtr.index}].sku" value="${lineItem.sku}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].cartLineItem"
                                      value="${lineItem.cartLineItem.id}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].costPrice" value="${lineItem.costPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].markedPrice"
                                      value="${lineItem.markedPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].hkPrice" value="${lineItem.hkPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].discountOnHkPrice"
                                      value="${lineItem.discountOnHkPrice}"/>
                            <s:hidden name="lineItems[${lineItemCtr.index}].tax" value="${lineItem.tax}"/>
                            <tr>
                                <td>${lineItemCtr.count}</td>
                                <td>
                                        ${lineItem.cartLineItem.productVariant.product.name}
                                </td>
                                <td>${lineItem.qty}</td>
                                <td><s:text name="lineItems[${lineItemCtr.index}].qty" value="0"></s:text></td>
                            </tr>
                        </c:forEach>
                    </table>
                    <s:submit name="createReplacementOrder" value="Generate Replacement Order"/>
                </s:form>
            </fieldset>

        </c:if>

    </s:layout-component>
</s:layout-render>