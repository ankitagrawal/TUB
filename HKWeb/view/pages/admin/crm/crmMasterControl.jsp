<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="CRM Master Screen">
<s:useActionBean beanclass="com.hk.web.action.admin.crm.MasterResolutionAction" var="maBean"/>
<s:useActionBean beanclass="com.hk.web.action.admin.reward.AddRewardPointAction" event="pre" var="rpBean"/>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction" var="replacementOrderBean"/>
<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<c:set var="shippingOrderStatusCustomerReturn" value="<%=EnumShippingOrderStatus.SO_Customer_Return_Replaced.getId()%>"/>
<c:set var="shippingOrderStatusCustomerAppease" value="<%=EnumShippingOrderStatus.SO_Customer_Appeasement.getId()%>"/>
<c:set var="shippingOrderStatusRTO_instantiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusSO_returned" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>
<s:layout-component name="heading">
    Master CRM fesolution Screen
</s:layout-component>

<s:layout-component name="content">

<script>
    $(document).ready(function(){
        $('rewardDiv').hide();
        $('refundDiv').hide();
        $('replacementDiv').hide();

        $('actionType').change(function(e){
            e.preventDefault();
            var selectedOption = $(this).selectedIndex;
            if(selectedOption.val() == "addRewardPoints") {
                $('rewardDiv').show();
                $('refundDiv').hide();
                $('replacementDiv').hide();
            } else if (selectedOption.val() == "refund") {
                $('rewardDiv').hide();
                $('refundDiv').show();
                $('replacementDiv').hide();
            } else if (selectedOption.val() == "replacementOrder") {
                $('rewardDiv').hide();
                $('refundDiv').hide();
                $('replacementDiv').show();
            } else {
                $('rewardDiv').hide();
                $('refundDiv').hide();
                $('replacementDiv').hide();
                alert("No Action Chosen");
            }
        });

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
        $('.createReplacementOrderButton').click(function(event){
            $(this).hide();
            var button = $(this);
            event.preventDefault();
            var shippingOrderId= $('#shippingOrderIdText').val();
            var formName;
            if($(this).hasClass('rto')){
                formName =  $('#createReplacementOrderForRtoForm');
            }
            else{
                formName = $('#createReplacementOrderForRepForm');
            }
            var formUrl = formName.attr('action');
            $.getJSON(
                    $('#checkReplacementOrderLink').attr('href'), {shippingOrderId:shippingOrderId},
                    function(res) {
                        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            var confirm_action = confirm("A replacement order exists for given shipping order, are you sure you want to create another replacement order?");
                            if (confirm_action == false) {
                                button.show();
                                event.preventDefault();
                            }
                            else{
                                formName.attr('action', formUrl+"?createReplacementOrder=");
                                formName.submit();
                            }
                        }
                        else{
                            formName.attr('action', formUrl+"?createReplacementOrder=");
                            formName.submit();
                        }
                    });
        });
    })
</script>

<div>
<fieldset>
    <label>Master </label>
   <%-- <s:form beanclass="com.hk.web.action.admin.crm">--%>
        <table>
<%--
            <tr>
                <td>Enter SO ID</td>
                <td><s:text name="shippingOrderId" maxlength="15"/></td>
            </tr>
--%>
            <tr>
                <td>Choose action:</td>
                <td><s:select name="actionType" id="actionType" >
                    <s:option value="none">-- Select --</s:option>
                    <<%--c:forEach items="${maBean.actionSet}" var="action">
                            <s:option value="${action}">${action}</s:option>
                        </c:forEach>--%>
                    <s:option value="addRewardPoints">Add Reward Points</s:option>
                    <s:option value="refund">Refund</s:option>
                    <s:option value="replacementOrder">Create Replacement Order</s:option>
                </s:select>
                </td>
            </tr>

        </table>
<%--
        <s:submit name="search" value="Search"></s:submit>
--%>
</fieldset>

<div id="rewardDiv">
    <s:form beanclass="com.hk.web.action.admin.reward.AddRewardPointAction" method="post">
        <fieldset>
            <label style="color: #ff0000; font-weight: bold; font-size: 25px;">Reward Points given due
                to cancellations will not be given from this screen. They will be given automatically</label>
        </fieldset>
        <fieldset class="left_label">
            <ul>
                <s:hidden name="user" value="${rpBean.user.id}"/>
                <li><label>Value</label><s:text name="value"/></li>
                <li><label>Order Id</label><s:text name="orderId"/></li>
                <li><label>Mode</label>
                    <s:select name="rewardPointMode"><hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="rewardPointModes" value="id" label="name"/>
                    </s:select></li>
                <li><label>Expiry
                    Date</label><s:text name="expiryDate" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
                </li>
                <li><label>Comment</label><s:textarea name="comment"/></li>
            </ul>
        </fieldset>
        <s:submit name="add" value="Add"/>
    </s:form>
</div>
<!-- Reward Points block ends -->

<div id="refundDiv">
    <div>
        &nbsp;&nbsp;&nbsp;&nbsp;Payment Seeker (Currently works for ICICI/Citrus/EBS/Icici via Citrus (Credit debit cards only)
    </div>
    <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
        <fieldset style="width:45%;">
            <br>
            <table>
                <tr>
                    <td><label>Enter Payment Gateway Order Id</label></td>
                    <td><s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/></td>
                </tr>

                <tr>
                    <td><label>Enter Amount</label></td>
                    <td><s:text name="amount" id = "amount" style="width:180px;height:25px;"/></td>
                </tr>
                <tr>
                    <td><label>Enter Reason for Refund</label></td>
                    <td><s:select name="refundReason" style="width:185px;height:28px;">
                        <s:option value="">-- Select --</s:option>
                        <c:forEach items="${cpa.refundReasons}" var="reason">
                            <s:option value="${reason.id}"> ${reason.classification.primary} - ${reason.classification.secondary}</s:option>
                        </c:forEach>
                    </s:select>
                    </td>
                </tr>
                <tr>
                    <td>Comments </td>
                    <td><s:textarea name="reasonComments" cols="5" rows="2" id="reasonCom" style="width:320px;height:80px;"/> </td>
                </tr>
            </table>
            <br>
            <s:submit name="seekPayment" value="Seek" id="save"/>
            <s:submit name="refundPayment" value="Refund " id="refund"/>
            <s:submit name="updatePayment"  value="Update" id="update"/>
        </fieldset>
    </s:form>

    <c:if test="${not empty cpa.hkPaymentResponseList}">
        <c:set var="count" value="1" />
        <table>
            <thead>
            <th> S No. </th>
            <th>Gateway Order Id </th>
            <th>Transaction Type </th>
            <th>Amount </th>
            <th>Payment Status </th>
            <th>Response Message </th>
            <th>Root Reference No </th>
            <th>Authentication Code </th>
            <th>Gateway </th>
            <th>Error Log </th>
            </thead>
            <tbody>
            <c:forEach  items="${cpa.hkPaymentResponseList}" var="response">
                <tr>
                    <td>${count}</td>
                    <td>${response.gatewayOrderId}</td>
                    <td>${response.transactionType}</td>
                    <td>${response.amount}</td>
                    <td>${response.HKPaymentStatus.name}</td>
                    <td>${response.responseMsg}</td>
                    <td>${response.rrn}</td>
                    <td>${response.authIdCode}</td>
                    <td>${response.gateway.name}</td>
                    <td>${response.errorLog}</td>
                </tr>
                <c:set var="count" value="${count+1}" />
            </c:forEach>
            </tbody>
        </table>
    </c:if>


    <c:if test="${not empty cpa.payment}">
        <c:set var="count" value="1" />
        <table>
            <thead>
            <th>Gateway Order Id </th>
            <th>Transaction Type </th>
            <th>Amount </th>
            <th>Payment Status </th>
            <th>Response Message </th>
            <th>Root Reference No </th>
            <th>Gateway </th>
            <th>Payment Date </th>
            <th>Error Log </th>
            <th>Parent </th>
            </thead>
            <tbody>
            <tr>
                <td>${cpa.payment.gatewayOrderId}</td>
                <td>${cpa.payment.transactionType}</td>
                <td>${cpa.payment.amount}</td>
                <td>${cpa.payment.paymentStatus.name}</td>
                <td>${cpa.payment.responseMessage}</td>
                <td>${cpa.payment.rrn}</td>
                <td>${cpa.payment.gateway.name}</td>
                <td>${cpa.payment.createDate}</td>
                <td>${cpa.payment.errorLog}}</td>
                <td>${cpa.payment.parent.gatewayOrderId}</td>
            </tr>
            </tbody>
        </table>
    </c:if>
</div>
<!-- Refund block ends -->

<div id="replacementDiv">
<div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction" id="checkReplacementOrderLink"
            event="checkExistingReplacementOrder"></s:link>
</div>
<fieldset class="right_label">
    <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
        <label>Search Shipping Order</label>
        <br/><br/>
        <label>Shipping order id: </label>
        <s:text name="shippingOrderId" id="shippingOrderIdText" style="width:200px;"/>
        <br/>
        <br/>
        <label>Gateway order id: </label>
        <s:text name="gatewayOrderId" id="shippingOrderIdText" style="width:200px;"/>
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
                    (${replacementOrderBean.shippingOrder.baseOrder.address.pincode.pincode})<br/>
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

                    <c:if test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusCustomerReturn}">
                        <a href="#" id="is-replacement-radio">
                            <h5>Create RO<br />for Customer Return</h5>
                        </a>
                        (<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="reverseOrderInvoice" target="_blank">
                        <s:param name="reverseOrder" value="${replacementOrderBean.reverseOrder}"/>
                        <s:param name="shippingOrder" value="${replacementOrderBean.shippingOrder}"/>
                        View Reverse Order
                    </s:link>)
                    </c:if>

                    <c:if test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusCustomerAppease}">
                        <a href="#" id="is-replacement-radio">
                            <h5>Create RO<br />for Customer Satisfaction</h5>
                        </a>
                    </c:if>
                </td>
                <td>

                    <c:if test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusRTO_instantiated
                            || replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusSO_returned}">
                        <a href="#" id="is-rto-radio">
                            <h5>Create RO<br />for RTO</h5>
                        </a>
                    </c:if>
                </td>
            </tr>
        </table>
    </fieldset>

    <fieldset style="display:none;" id="is-rto">
        <h4>Returned to origin</h4>
        <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction" id="createReplacementOrderForRtoForm">
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
                    <s:hidden name="lineItems[${lineItemCtr.index}].rewardPoints" value="${lineItem.rewardPoints}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].orderLevelDiscount" value="${lineItem.orderLevelDiscount}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].codCharges" value="${lineItem.codCharges}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].shippingCharges" value="${lineItem.shippingCharges}"/>

                    <tr>
                        <td>${lineItemCtr.count}</td>
                        <td>${lineItem.cartLineItem.productVariant.product.name}<br/>
                            Variant: ${lineItem.cartLineItem.productVariant.id}
                        </td>
                        <td>${lineItem.qty}</td>
                        <td>
                            <s:hidden name="lineItems[${lineItemCtr.index}].qty" value="${lineItem.qty}"/>
                                ${lineItem.qty}
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <s:label name="Reason for Replacement:" style="margin-left:7px;"/>
            <s:select name="replacementOrderReason">
                <s:option value="-Select Reason-">-Select Reason-</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                           serviceProperty="replacementOrderReasonForRto" value="id"
                                           label="name"/>
            </s:select>
            <br/><br/>
            <s:label name="Comment/Remark:" style="margin-left:7px;"/><s:textarea name="roComment" style="height:50px;" />
            <s:submit class="createReplacementOrderButton rto" name="createReplacementOrder" value="Generate Replacement Order"/>
        </s:form>
    </fieldset>

    <fieldset style="display:none;" id="is-replacement">
        <h4>Replacement for Customer Return</h4>
        <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction" id="createReplacementOrderForRepForm">
            <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrder.id}"/>
            <table border="1">
                <thead>
                <th>S No.</th>
                <th>Product</th>
                <th>Original Qty</th>
                <th>Replacement Qty</th>
                </thead>
                <s:hidden name="isRto" value="0"/>
                    <%--<c:choose>--%>
                    <%--<c:when test="${replacementOrderBean.shippingOrder.orderStatus.id == shippingOrderStatusCustomerAppease}">--%>
                <c:forEach items="${replacementOrderBean.lineItems}" var="lineItem"
                           varStatus="lineItemCtr">
                    <s:hidden name="lineItems[${lineItemCtr.index}].sku" value="${lineItem.sku}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].cartLineItem"
                              value="${lineItem.cartLineItem.id}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].costPrice"
                              value="${lineItem.costPrice}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].markedPrice"
                              value="${lineItem.markedPrice}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].hkPrice"
                              value="${lineItem.hkPrice}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].discountOnHkPrice"
                              value="${lineItem.discountOnHkPrice}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].tax" value="${lineItem.tax}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].rewardPoints"
                              value="${lineItem.rewardPoints}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].orderLevelDiscount"
                              value="${lineItem.orderLevelDiscount}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].codCharges"
                              value="${lineItem.codCharges}"/>
                    <s:hidden name="lineItems[${lineItemCtr.index}].shippingCharges"
                              value="${lineItem.shippingCharges}"/>
                    <tr>
                        <td>${lineItemCtr.count}</td>
                        <td>
                                ${lineItem.cartLineItem.productVariant.product.name} <br/>
                            Variant: ${lineItem.cartLineItem.productVariant.id}
                        </td>
                        <td>${lineItem.qty}</td>
                        <td><s:text name="lineItems[${lineItemCtr.index}].qty" class="qty"/>
                            <script type="text/javascript">
                                $('.qty').val(0);
                            </script>
                        </td>
                    </tr>
                </c:forEach>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>

                    <%--</c:otherwise>--%>
                    <%--</c:choose>--%>
            </table>
            <s:label name="Reason for Replacement:" style="margin-left:7px;"/>
            <s:select name="replacementOrderReason">
                <s:option value="-Select Reason-">-Select Reason-</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                           serviceProperty="replacementOrderReasonForReplacement" value="id"
                                           label="name"/>
            </s:select>
            <br/><br/>
            <s:label name="Comment/Remark:" style="margin-left:7px;"/><s:textarea name="roComment" style="height:50px;" />
            <br/>
            <s:submit class="createReplacementOrderButton" name="createReplacementOrder" value="Generate Replacement Order"/>
        </s:form>
    </fieldset>

</c:if>
</div>
</s:layout-component>
</s:layout-render>