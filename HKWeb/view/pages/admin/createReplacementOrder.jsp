<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="shippingOrderStatusCustomerReturn" value="<%=EnumShippingOrderStatus.SO_Customer_Return_Replaced.getId()%>"/>
<c:set var="shippingOrderStatusRTO_instantiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusSO_returned" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>

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
	            $('.createReplacementOrderButton').click(function(event){
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
                <h4>Replacement</h4>
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
                                <td>
                                        ${lineItem.cartLineItem.productVariant.product.name}  <br/>
	                                Variant: ${lineItem.cartLineItem.productVariant.id}
                                </td>
                                <td>${lineItem.qty}</td>
                                <td><s:text name="lineItems[${lineItemCtr.index}].qty" /></td>
                            </tr>
                        </c:forEach>
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

    </s:layout-component>
</s:layout-render>