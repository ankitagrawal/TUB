<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment History">

	<s:useActionBean beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction" var="paymentHistoryBean"/>
    
  <s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>

	<s:layout-component name="content">
   <script>
       $(document).ready(function(){
           $('#show-add-new-form').click(function(event) {
               event.preventDefault();
               $('#new-payment-history-form').slideDown();
           });
       })
    </script>
   <s:layout-component name="heading">
    Payment History
  </s:layout-component>
    <fieldset>
      <label style="font-weight:bold;">Supplier</label>
          <table>
            <tr>
              <td style="font-weight:bold;">Supplier Name</td>
              <td>${paymentHistoryBean.supplier.name}</td>

              <td style="font-weight:bold;">Supplier State</td>
              <td>${paymentHistoryBean.supplier.state}</td>

              <td style="font-weight:bold;">Tax</td>
              <td>
                <c:choose>
                  <c:when test="${paymentHistoryBean.supplier.state == paymentHistoryBean.purchaseInvoice.warehouse.state}">
                    <label class="state">NON-CST</label>
                  </c:when>
                  <c:otherwise>
                    <label class="state">CST</label>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <tr>
              <td style="font-weight:bold;">Invoice Date</td>
              <td>${paymentHistoryBean.purchaseInvoice.createDate}</td>
              <td style="font-weight:bold;">Invoice Number</td>
              <td>${paymentHistoryBean.purchaseInvoice.invoiceNumber}</td>
              <td style="font-weight:bold;">Generated By</td>
              <td>
                  ${paymentHistoryBean.purchaseInvoice.createdBy.name}</td>
            </tr>
          <c:if test="${paymentHistoryBean.purchaseInvoiceId != null}">
            <s:form beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction">
            <s:hidden name="purchaseInvoiceId" value="${paymentHistoryBean.purchaseInvoiceId}" />
            <s:hidden name="purchaseInvoice" value="${purchaseInvoice.id}"/>
              <tr>
                <td>Payment Date</td>
                <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseInvoice.paymentDate" value="${purchaseInvoice.paymentDate}"/></td>
                <td>Payment Details<br/><span class="sml gry">(eg. Cheque no.)</span></td>
                <td><s:textarea name="purchaseInvoice.paymentDetails" style="height:50px;" >${purchaseInvoice.paymentDetails}</s:textarea></td>
              </tr>
              <tr>
                <td>
	                <c:if test="${paymentHistoryBean.isEditable}" >
	                    <s:submit name="editPurchaseInvoice" value="Save PI"/>
	              </c:if>
	              </td>
              </tr>
              </s:form>
            </c:if>
          </table>
    </fieldset>

 <%-- search is not required   
      <fieldset class="right_label" style="display:none;">
			<s:form beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction">
				<label>Search by Purchase Order ID</label>
				&nbsp;&nbsp;&nbsp;
				<s:text name="purchaseOrderId" id="purchaseOrderId" style="width:200px;"/>
				<br/>
				<br/>
        <label>Search by Purchase Invoice ID</label>
				&nbsp;&nbsp;&nbsp;
				<s:text name="purchaseInvoiceId" id="purchaseInvoiceId" style="width:200px;"/>
				<br/>
				<br/>
				<s:submit name="search" value="Search"/>    
			</s:form>
        </fieldset>
--%>
       <fieldset>
         <c:if test="${paymentHistoryBean.purchaseOrderId != null}">
           <label>
              Payment History against purchase order ${paymentHistoryBean.purchaseOrderId}
           </label>
         </c:if>
         <c:if test="${paymentHistoryBean.purchaseInvoiceId != null}">
           <label>
              Payment History against purchase Invoice Id ${paymentHistoryBean.purchaseInvoiceId}
           </label>
         </c:if>
                     <table border="1">
                       <thead>
                          <tr>
                           <th>Amount</th>
                           <th>Mode of Payment</th>
                           <th>Scheduled Payment Date  <br /> (yyyy-mm-dd)</th>
                           <th>Actual Payment Date  <br/> (yyyy-mm-dd)</th>
                           <th>Remarks</th>
                           <th>Payment Reference</th>
                          </tr>
                        </thead>
                    <s:form beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction">
                    <c:forEach items="${paymentHistoryBean.paymentHistories}" var="paymentHistory" varStatus="paymentHistoryCount">
                          <tr>
                            <s:hidden name="purchaseInvoiceId" value="${paymentHistoryBean.purchaseInvoiceId}" />
                            <s:hidden name="purchaseOrderId" value="${paymentHistoryBean.purchaseOrderId}" />
                            <s:hidden name="paymentHistories[${paymentHistoryCount.index}]" value="${paymentHistory.id}"/>    
                           <%-- <td>
                              <c:if test="${paymentHistory.purchaseOrder != null}">
                               <label>
                                  Purchase Order Id # ${paymentHistory.purchaseOrder.id}
                               </label>
                             </c:if>
                              <br>
                              <c:if test="${paymentHistory.purchaseInvoice != null}">
                               <label>
                                  Purchase Invoice Id # ${paymentHistory.purchaseInvoice.id}
                               </label>
                             </c:if>
                            </td>--%>
                            <td><s:text name="paymentHistories[${paymentHistoryCount.index}].amount"/></td>

                            <td><s:select name="paymentHistories[${paymentHistoryCount.index}].modeOfPayment">
                                <s:option value="NEFT">NEFT</s:option>
                                <s:option value="Cheque">Cheque</s:option>
                                <s:option value="Cash">Cash</s:option>
                                <s:option value="DemandDraft">Demand Draft</s:option>
                                <s:option value="Others">Others</s:option>
                              </s:select>
                            </td>

                            <td><s:text class="date_input" formatPattern="yyyy-MM-dd"  style="width:150px" name="paymentHistories[${paymentHistoryCount.index}].scheduledPaymentDate" /></td>

                            <td><s:text style="width:150px" class="date_input" formatPattern="yyyy-MM-dd"  name="paymentHistories[${paymentHistoryCount.index}].actualPaymentDate" /></td>

                            <td><s:text name="paymentHistories[${paymentHistoryCount.index}].remarks" value="${paymentHistory.remarks}"/></td>
                            <td><s:text name="paymentHistories[${paymentHistoryCount.index}].paymentReference" value="${paymentHistory.paymentReference}"/></td>
                            <td>
                              <s:link beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction" event="delete">
                                <s:param name="paymentHistory" value="${paymentHistory.id}"/>
                                <s:param name="purchaseInvoiceId" value="${paymentHistoryBean.purchaseInvoiceId}" />
                                <s:param name="purchaseOrderId" value="${paymentHistoryBean.purchaseOrderId}" />
                                  Delete
                              </s:link>
                            </td>
                          </tr>
                       </c:forEach>
                     <tr>
                       <td><label>Total Payable: ${paymentHistoryBean.purchaseInvoice.finalPayableAmount}</label></td>
                       <td><label>Oustanding Amount: ${paymentHistoryBean.outstandingAmount}</label></td>
                       <td>
	                       <c:if test="${paymentHistoryBean.isEditable}" >
		                       <s:submit name="save" value="Save"/>
	                       </c:if>
                       </td>
                     </tr>
                    </s:form>

                      </table>
          </fieldset>
           <br />
          <fieldset style="float:left; display:none;" id="new-payment-history-form">
            <s:form beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction">
                  <c:if test="${paymentHistoryBean.purchaseOrderId != null}">
                 <label>
                    Add a Payment detail against purchase order ${paymentHistoryBean.purchaseOrderId}
                 </label>
               </c:if>
               <c:if test="${paymentHistoryBean.purchaseInvoiceId != null}">
                 <label>
                    Add a payment Detail against purchase Invoice Id ${paymentHistoryBean.purchaseInvoiceId}
                 </label>
               </c:if>
              <s:hidden name="purchaseOrderId" value="${paymentHistoryBean.purchaseOrderId}" />
               <s:hidden name="purchaseInvoiceId" value="${paymentHistoryBeanpurchaseInvoiceId}" />
                <table>
                  <thead>
                    <tr>
                       <th>Amount</th>
                       <th>Mode of Payment</th>
                       <th>Scheduled Payment Date <br /> (yyyy-mm-dd)</th>
                       <th>Actual Payment Date <br /> (yyyy-mm-dd)</th>
                       <th>Remarks</th>
                       <th>Payment Reference</th>
                    </tr>
                  </thead>
                    <tr>
                        <td><s:text name="paymentHistory.amount" /></td>
                        <td><s:select name="paymentHistory.modeOfPayment" >
                                <s:option value="NEFT">NEFT</s:option>
                                <s:option value="Cheque">Cheque</s:option>
                                <s:option value="Cash">Cash</s:option>
                                <s:option value="DemandDraft">Demand Draft</s:option>
                                <s:option value="Others">Others</s:option>
                              </s:select>
                        </td>
                        <td><s:text style="width:150px"  class="date_input" formatPattern="yyyy-MM-dd" name="paymentHistory.scheduledPaymentDate" value=""/></td>
                        <td><s:text style="width:150px"  class="date_input" formatPattern="yyyy-MM-dd" name="paymentHistory.actualPaymentDate" value=""/></td>
                        <td><s:text name="paymentHistory.remarks" value="${paymentHistory.remarks}"/></td>
                      <td><s:text name="paymentHistory.paymentReference" value="${paymentHistory.paymentReference}"/></td>
                        <td>
	                        <c:if test="${paymentHistoryBean.isEditable}" >
	                            <s:submit name="add_paymentHistory" value="Add Payment"/>
	                        </c:if>
	                    </td>
                  </tr>                
              </table>
            </s:form>
          </fieldset>
            <a href="#" id="show-add-new-form">Add New Payment Details</a><br />

	</s:layout-component>
</s:layout-render>