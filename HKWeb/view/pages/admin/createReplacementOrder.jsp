<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

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

                $('.generate-replacement-order').click(function(event) {
                    ('#so-details').hide();
                })
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
            <fieldset style="float:left;" id="so-details">
                <table>
                        <%--          <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrderId}"/>--%>
                    <tr>
                        <td><b>CustomerName:</b></td>
                        <td>${replacementOrderBean.shippingOrder.baseOrder.user.name}</td>
                        <td><b>SO date:</b></td>
                        <td>${replacementOrderBean.shippingOrder.createDate}</td>
                    </tr>
                    <tr>
                        <td><b>Email:</b></td>
                        <td>${replacementOrderBean.shippingOrder.baseOrder.user.email}
                        </td>
                        <td><b>Address:</b></td>
                        <td>
                                ${replacementOrderBean.shippingOrder.baseOrder.address.city}<br/>
                                ${replacementOrderBean.shippingOrder.baseOrder.address.state}-
                            (${replacementOrderBean.shippingOrder.baseOrder.address.pin})<br/>
                            Ph: ${replacementOrderBean.shippingOrder.baseOrder.address.phone}
                        </td>
                    </tr>
                    <tr>
                        <td>

                        </td>
                        <td>
                            <s:link href="#" id="is-rto-radio">
                                <b>Is RTO:</b>
                            </s:link>
                        </td>
                        <td>
                            <s:link href="#" id="is-replacement-radio">
                                <b>Is Replacement:</b>
                            </s:link>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </fieldset>

            <fieldset style="display:none;" id="is-rto">
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
                    <s:submit name="createReplacementOrder" value="Generate Replacement Order" class="generate-replacement-order"/>
                </s:form>
            </fieldset>

            <fieldset style="display:none;" id="is-replacement">
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
                    <s:submit name="createReplacementOrder" value="Generate Replacement Order" class="generate-replacement-order"/>
                </s:form>
            </fieldset>

        </c:if>

    </s:layout-component>
</s:layout-render>