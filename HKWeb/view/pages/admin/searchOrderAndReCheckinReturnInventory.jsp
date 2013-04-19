<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.*" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction"
                 var="orderAdmin"/>

<c:set var="shippingOrderStatusRTO" value="<%=EnumShippingOrderStatus.SO_RTO.asShippingOrderStatus()%>"/>
<%
    int lineItemGlobalCtr = 0;
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Search">

<s:layout-component name="htmlHead">
    <script type="text/javascript">
        $(document).ready(function() {
            var obj;
            $('#submitButton').click( function() {
                $(this).hide();
                var checkedinQty = obj.parents('.shippingRow').children('.checkedin').children('.checkedinQty').html();
                var qty = obj.parents('.shippingRow').children('.qty').html();
                if(qty == checkedinQty){
                    alert("Please see checkin-qty. All items have been checked in");
                    location.reload();
                    return false;
                }
            });

            $('.good').change(function() {
                $('.rto2').remove();
                $('.rto3').remove();
                $('#conditionOfItem').attr('value', "good");
                 obj = $(this);
                $('#submitButton').click();
            });

            $('.damaged').change(function() {
                $('.rto1').remove();
                $('.rto3').remove();
                $('#conditionOfItem').attr('value', "damaged");
                obj = $(this);
                $('#submitButton').click();
            });

            $('.expired').change(function() {
                $('.rto1').remove();
                $('.rto2').remove();
                $('#conditionOfItem').attr('value', "expired");
                obj = $(this);
                $('#submitButton').click();
            });

            $('.downloadCheck').click(function() {
                var checkedinQty = Number($(this).parents('.shippingRow').children('.checkedin').children('.checkedinQty').html());
                var qty = Number($(this).parents('.shippingRow').children('.qty').html());
                if(qty == checkedinQty){
                    alert("All sku items for this particular line item have been checked in");
                    return false;
                }
            });
        });
    </script>
</s:layout-component>


<s:layout-component name="heading">Search Order and Checkin Returned Units</s:layout-component>

<s:layout-component name="content">
<s:form beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction" method="get"
        autocomplete="false">
    <fieldset class="top_label">
        <ul>
            <div class="grouped">
                <li><label>Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
                <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" style="width :150px"/></li>
            </div>
        </ul>
        <div class="buttons"><s:submit name="searchOrder" value="Search Order"/></div>
    </fieldset>
</s:form>

<c:if test="${orderAdmin.shippingOrder != null}">
<s:form beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction"
        autocomplete="off">
<s:hidden name="orderId" value="${orderAdmin.shippingOrder.id}"/>
<c:set var="order" value="${orderAdmin.shippingOrder.baseOrder}"/>
<c:choose>
    <c:when test="${orderAdmin.shippingOrder.orderStatus.id ==  shippingOrderStatusRTO.id}">
        <h3>SO RTO items check-in</h3>

        (<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" target="_blank">
        <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
        Invoice
    </s:link>)<br>
        <%--(<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" target="_blank">--%>
        <%--<s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>--%>
        <%--<s:param name="printable" value="true"/>--%>
        <%--Personal Care--%>
        <%--</s:link>)<br/><br/> --%>
    </c:when>
    <c:otherwise>
        <h3>SO Customer Return items check-in</h3>
        (<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction"
                 event="reverseOrderInvoice" target="_blank">
        <s:param name="reverseOrder" value="${orderAdmin.reverseOrder}"/>
        <s:param name="shippingOrder" value="${orderAdmin.reverseOrder.shippingOrder}"/>
        Invoice
    </s:link>)<br>
    </c:otherwise>
</c:choose>
<table class="align_top" width="100%">
    <thead>
    <tr>
        <th>Base Order and payment</th>
        <th>User</th>
        <th>Address</th>
        <th>Items and Qty | Re-checkin (Scan Barcode in the boxes, one at a time)</th>
            <%--<th></th>--%>
    </tr>
    </thead>


    <tr>
        <td>
            <strong>${order.gatewayOrderId}</strong>
            &nbsp;&nbsp;
            <strong>Rs.
                <fmt:formatNumber value="${order.payment.amount}"
                                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></strong>
            <ul>
                <li class="fieldLabel">Date</li>
                <li><fmt:formatDate value="${order.payment.paymentDate}" type="both"/></li>
                <li class="fieldLabel">Mode</li>
                <li>${order.payment.paymentMode.name}</li>
                <li class="fieldLabel">Status</li>
                <li>${order.payment.paymentStatus.name}</li>
            </ul>
        </td>
        <td>
            <ul>
                <li class="fieldLabel">Name</li>
                <li>${order.user.name}</li>
                <li class="fieldLabel">Email</li>
                <li>${order.user.email}</li>
                <li class="fieldLabel">Signup date</li>
                <li><fmt:formatDate value="${order.user.createDate}"/></li>
            </ul>
        </td>
        <td class="addressContainer">
                ${order.address.name}<br/>
                ${order.address.line1}<br/>
            <c:if test="${hk:isNotBlank(order.address.line2)}">${order.address.line2}<br/></c:if>
                ${order.address.city} - ${order.address.pincode.pincode}<br/>
                ${order.address.state}<br/>
            Ph. ${order.address.phone}<br/>
        </td>
        <td style="padding: 0" valign="top">
            <c:choose>
                <c:when test="${orderAdmin.shippingOrder.orderStatus.id ==  shippingOrderStatusRTO.id}">
                    <table width="100%">
                        <c:forEach items="${orderAdmin.shippingOrder.lineItems}" var="lineItem"
                                   varStatus="lineItemCtr">
                            <c:set var="productVariant" value="${lineItem.sku.productVariant}"/>
                            <c:if test="${!productVariant.product.service}">
                                <c:set value="<%=lineItemGlobalCtr%>" var="lineItemGlobalCtr"/>

                                <tr class="shippingRow">
                                    <td width="250">
                                        <s:hidden name="lineItems[${lineItemGlobalCtr}]" value="${lineItem.id}"/>
                                            ${productVariant.product.name}
                                        <c:if test="${not empty productVariant.productOptions}">
                                            <br/><span class="gry">
                                                        <c:forEach items="${productVariant.productOptions}"
                                                                   var="productOption" varStatus="optionCtr">
                                                            ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                                                        </c:forEach>
                                                        </span>
                                        </c:if>
                                    </td>
                                    <td class="qty">${lineItem.qty}</td>
                                    <td title="Re-checkin">
                                       <span class="good">
                                        <label>Good</label>
                                        <s:text name="lineItemRecheckinBarcodeMap[${lineItem}]"
                                            style="width:100px;" class="rto1" />

                                          </p>
                                        </span>
                                        <span class="damaged">
                                        <label>Damaged</label>
                                         <s:text name="lineItemRecheckinBarcodeMap[${lineItem}]"
                                             style="width:100px;" class="rto2" />

                                          </p>
                                          </span>
                                          <span class="expired">
                                          <label>Expired</label>
                                            <s:text name="lineItemRecheckinBarcodeMap[${lineItem}]"
                                                style="width:100px;" class="rto3" />

                                          </p>
                                          </span>

                                    </td>
                                    <td class="checkedin">
                                        Checked-in Qty:
                                        <span class="checkedinQty">${hk:getReCheckedinUnitsCount(lineItem)}</span>
                                    </td>
                                    <td>
                                        <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
                                            <s:link beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction"
                                                    event="downloadBarcode" class="downloadCheck"> Download
                                                <s:param name="lineItem" value="${lineItem.id}"/>
                                                <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
                                            </s:link>
                                        </shiro:hasPermission>
                                    </td>
                                  <p></p><p></p>

                                </tr>
                                <%
                                    lineItemGlobalCtr++;
                                %>
                            </c:if>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <table width="100%">
                        <c:forEach items="${orderAdmin.reverseOrder.reverseLineItems}" var="reverseLineItem"
                                   varStatus="lineItemCtr">
                            <c:set var="productVariant"
                                   value="${reverseLineItem.referredLineItem.sku.productVariant}"/>
                            <c:if test="${!productVariant.product.service}">
                                <c:set value="<%=lineItemGlobalCtr%>" var="lineItemGlobalCtr"/>
                                
                                <tr class="shippingRow">
                                    <td width="250">
                                        <s:hidden name="lineItems[${lineItemGlobalCtr}]"
                                                  value="${reverseLineItem.referredLineItem.id}"/>
                                            ${productVariant.product.name}
                                        <c:if test="${not empty productVariant.productOptions}">
                                            <br/><span class="gry">
                                                        <c:forEach items="${productVariant.productOptions}"
                                                                   var="productOption" varStatus="optionCtr">
                                                            ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                                                        </c:forEach>
                                                        </span>
                                        </c:if>
                                    </td>
                                    <td class="qty" style="width:20px">${reverseLineItem.returnQty}</td>
                                    <td title="Re-checkin">
                                      <span class="good">
                                      <label>Good</label>
                                      <s:text name="lineItemRecheckinBarcodeMap[${reverseLineItem.referredLineItem}]"
                                         style="width:100px;" class="rto1" />

                                       </p>
                                       </span>
                                       <span class="damaged">
                                        <label>Damaged</label>
                                        <s:text name="lineItemRecheckinBarcodeMap[${reverseLineItem.referredLineItem}]"
                                            style="width:100px;" class="rto2" />

                                        </p>
                                         </span>
                                         <span class="expired">
                                         <label>Expired</label>
                                         <s:text name="lineItemRecheckinBarcodeMap[${reverseLineItem.referredLineItem}]"
                                                            style="width:100px;" class="rto3" />

                                         </p>
                                         </span>

                                    </td>
                                    <td class="checkedin">
                                        Checked-in
                                        Qty:<span class="checkedinQty">${hk:getReCheckedinUnitsCount(reverseLineItem.referredLineItem)}</span>
                                    </td>
                                     <td>
                                         <shiro:hasPermission name="<%=PermissionConstants.GRN_CREATION%>">
                                             <s:link beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinReturnInventoryAction"
                                                     event="downloadBarcode" class="downloadCheck"> Download
                                                 <s:param name="reverseLineItem" value="${reverseLineItem.id}"/>
                                                 <s:param name="lineItem"
                                                          value="${reverseLineItem.referredLineItem.id}"/>
                                                 <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
                                             </s:link>
                                         </shiro:hasPermission>
                                    </td>
                                    <p></p><p></p>
                                </tr>
                                <%
                                    lineItemGlobalCtr++;
                                %>
                            </c:if>

                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
            <input type="hidden" name="conditionOfItem" id="conditionOfItem">

            <s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction" event="pre" target="_blank">
                SO Lifecycle
            <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
            </s:link>

            <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre"
                    target="_blank">
                <c:if test="${!empty hk:orderComments(order)}">
                    <text style="color:#f88; font-weight:bold">Comments!</text>
                </c:if>
                <c:if test="${empty hk:orderComments(order)}">Add a comment</c:if>
                <s:param name="order" value="${order}"/>
            </s:link>
        </td>
    </tr>

</table>

<div class="buttons"><s:submit name="checkinReturnedUnits" value="Checkin Returned Unit" id="submitButton"/></div>
</s:form>

</c:if>
</s:layout-component>

</s:layout-render>
