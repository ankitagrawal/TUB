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
<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<s:layout-component name="heading">
    Master CRM Resolution Screen
</s:layout-component>

<s:layout-component name="content">

<script>
    $(document).ready(function(){
        var viewRefund = "${maBean.refundFlag}";
        var viewReward = "${maBean.rewardFlag}";
        var viewReplacement = "${maBean.replacementFlag}";

        $('#rewardDiv').hide();
        $('#refundDiv').hide();
        $('#replacementDiv').hide();

        if (viewRefund == "true") {
            $('#refundDiv').show();
            $('#refund').attr('selected','selected');
        } else if (viewReward == "true") {
            $('#rewardDiv').show();
            $('#addRewardPoints').attr('selected','selected');
        } else if (viewReplacement == "true") {
            $('#replacementDiv').show();
            $('#replacementOrder').attr('selected','selected');
        }

        $('#actionType').change(function(e){
            e.preventDefault();
            var selectedOption = $(this).selected();
            if(selectedOption.val() == "addRewardPoints") {
                $('#rewardDiv').show();
                $('#refundDiv').hide();
                $('#replacementDiv').hide();
            } else if (selectedOption.val() == "refund") {
                $('#rewardDiv').hide();
                $('#refundDiv').show();
                $('#replacementDiv').hide();
            } else if (selectedOption.val() == "replacementOrder") {
                $('#rewardDiv').hide();
                $('#refundDiv').hide();
                $('#replacementDiv').show();
            } else {
                $('#rewardDiv').hide();
                $('#refundDiv').hide();
                $('#replacementDiv').hide();
                alert("No Action Chosen");
            }
        });
        
        $('#search').click(function(e) {
        	if($('#actionType').selected().val()=="none") {
        		e.preventDefault();
        		alert("Choose an action to continue");
        	}
        });

        $('#shippingOrderId').focus();

        $('.createReplacementOrderButton').click(function(event){
            $(this).hide();
	        });
        
    })
</script>

		<div>
			<fieldset style="width: 100%;">
				<s:form
					beanclass="com.hk.web.action.admin.crm.MasterResolutionAction">
					<table>
						<tr>
							<td style="width: 49%;">
								<table>
									<tr>
										<td><label>Search Shipping Order</label></td>
										<td></td>
									</tr>
									<tr>
										<td>Shipping order ID:</td>
										<td><s:text name="shippingOrderId" style="width:200px;" /></td>
									</tr>
									<tr>
										<td>Gateway order ID:</td>
										<td><s:text name="gatewayOrderId" style="width:200px;" /></td>
									</tr>
									<tr>
										<td>Choose action on:</td>
										<td><s:select name="actionType" id="actionType">
												<s:option value="none">-- Select --</s:option>
												<s:option id="addRewardPoints" value="addRewardPoints">Reward Points</s:option>
												<s:option id="refund" value="refund">Refund</s:option>
												<s:option id="replacementOrder" value="replacementOrder">Replacement Order</s:option>
											</s:select></td>
									</tr>
									<tr>
										<td></td>
										<td><s:submit name="searchShippingOrder" value="Search"
												id="search" /></td>
									</tr>
								</table>
							</td>
							<td style="width: 49%;"><c:if
									test="${maBean.actionFlag == true}">
									<table>
										<tr>
											<td><h5>CustomerName:</h5></td>
											<td>${maBean.shippingOrder.baseOrder.user.name}</td>
											<td><h5>SO date:</h5></td>
											<td>${maBean.shippingOrder.createDate}</td>
										</tr>
										<tr>
											<td><h5>Email:</h5></td>
											<td>${maBean.shippingOrder.baseOrder.user.email}</td>
											<td><h5>Address:</h5></td>
											<td>${maBean.shippingOrder.baseOrder.address.city}<br />
												${maBean.shippingOrder.baseOrder.address.state}-
												(${maBean.shippingOrder.baseOrder.address.pincode.pincode})<br />
												Ph: ${maBean.shippingOrder.baseOrder.address.phone}
											</td>
										</tr>
										<tr>
											<td>
												<h5>Status</h5>
											</td>
											<td>${maBean.shippingOrder.orderStatus.name}</td>
										</tr>
									</table>
								</c:if></td>
						</tr>
					</table>
					<c:if test="${!empty maBean.lineItems}">
						<table border="1">
							<thead>
								<th>S No.</th>
								<th>Product</th>
								<th>Qty</th>
							</thead>
							<c:forEach items="${maBean.lineItems}" var="lineItem" varStatus="lineItemCtr">
								<tr>
									<td>${lineItemCtr.count}</td>
									<td>${lineItem.cartLineItem.productVariant.product.name}<br />
										Variant: ${lineItem.cartLineItem.productVariant.id}
									</td>
									<td>${lineItem.RQty}</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
				</s:form>
			</fieldset>
		</div>
		<div id="rewardDiv">
			<c:if test="${!empty maBean.shippingOrder}">

				<s:form beanclass="com.hk.web.action.admin.crm.MasterResolutionAction" method="post">
					<fieldset style="width: 75%">
						<table>
							<s:hidden name="shippingOrder" value="${maBean.shippingOrder.id}" />
							<s:hidden name="baseOrderId" value="${maBean.baseOrderId}" />
							<tr>
								<td>Rewardable Amount</td>
								<td>${maBean.paymentAmount}</td>
								<td>Base Order Id</td>
								<td>${maBean.baseOrderId}</td>
							</tr>
							<tr>
								<td>Expiry Date</td>
								<td><s:text name="expiryDate" class="date_input"
										formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" /></td>
								<td>Comments</td>
								<td><s:textarea name="comment" style="width:320px;height:80px;"/></td>
							</tr>
							<tr>
								<td></td>
								<td><s:submit name="addRewardPoints"
										value="Add reward Points" /></td>
							</tr>
						</table>
					</fieldset>

				</s:form>
			</c:if>
		</div>
<!-- Reward Points block ends -->

		<div id="refundDiv">
			<c:if test="${!empty maBean.shippingOrder}">
				<s:form beanclass="com.hk.web.action.admin.crm.MasterResolutionAction">
					<fieldset style="width: 75%;">
						<br>
						<table>
							<s:hidden name="shippingOrder" value="${maBean.shippingOrder.id}" />
							<tr>
								<td><label>Refundable Amount</label></td>
								<td>${maBean.paymentAmount}</td>
							</tr>
							<tr>
								<td><label>Enter Reason for Refund</label></td>
								<td><s:select name="refundReason"
										style="width:185px;height:28px;">
										<s:option value="">-- Select --</s:option>
										<c:forEach items="${maBean.refundReasons}" var="reason">
											<s:option value="${reason.id}"> ${reason.classification.primary} - ${reason.classification.secondary}</s:option>
										</c:forEach>
									</s:select></td>
							</tr>
							<tr>
								<td>Comments</td>
								<td><s:textarea name="refundComments" cols="5" rows="2"
										id="reasonCom" style="width:320px;height:80px;" /></td>
							</tr>
						</table>
						<br>
						<s:submit name="refundPayment" value="Refund " id="refund" />
					</fieldset>
				</s:form>
				</c:if>
			</div>
			<div>
				<c:if test="${not empty maBean.payment}">
					<c:set var="count" value="1" />
					<table>
						<thead>
							<th>Gateway Order Id</th>
							<th>Transaction Type</th>
							<th>Amount</th>
							<th>Payment Status</th>
							<th>Response Message</th>
							<th>Root Reference No</th>
							<th>Gateway</th>
							<th>Payment Date</th>
							<th>Error Log</th>
							<th>Parent</th>
						</thead>
						<tbody>
							<tr>
								<td>${maBean.payment.gatewayOrderId}</td>
								<td>${maBean.payment.transactionType}</td>
								<td>${maBean.payment.amount}</td>
								<td>${maBean.payment.paymentStatus.name}</td>
								<td>${maBean.payment.responseMessage}</td>
								<td>${maBean.payment.rrn}</td>
								<td>${maBean.payment.gateway.name}</td>
								<td>${maBean.payment.createDate}</td>
								<td>${maBean.payment.errorLog}</td>
								<td>${maBean.payment.parent.gatewayOrderId}</td>
							</tr>
						</tbody>
					</table>
				</c:if>
			
		</div>
		<!-- Refund block ends -->

		<div id="replacementDiv">
			<c:if test="${!empty maBean.shippingOrder}">
				<c:if test="${maBean.replacementPossible == true}">
					<fieldset style="width: 75%">
						<s:form beanclass="com.hk.web.action.admin.crm.MasterResolutionAction" id="createReplacementOrderForRtoForm">
							<s:hidden name="shippingOrder" value="${maBean.shippingOrder.id}" />
							<table>
								<tr>
									<td>Reason for Replacement:</td>
									<td>
									<s:select name="replacementOrderReason">
										<s:option value="-Select Reason-">-Select Reason-</s:option>
										<c:choose>
											<c:when test="${maBean.rto== true}">
												<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="replacementOrderReasonForRto" value="id"
														label="name" />
											</c:when>
											<c:otherwise>
						 					<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="replacementOrderReasonForReplacement"
														value="id" label="name" />
											</c:otherwise>
										</c:choose>
									</s:select>
									</td>
								</tr>
								<tr>
									<td>Comments</td>
									<td><s:textarea name="replacementComments" style="width:320px;height:80px;" /></td>
								</tr>
								<tr>
									<td></td><td><s:submit class="createReplacementOrderButton" name="createReplacementOrder" value="Generate Replacement Order" /></td>
								</tr>
							</table>
						</s:form>
						<br>
					</fieldset>

				</c:if>
			</c:if>
		</div>
	</s:layout-component>
</s:layout-render>