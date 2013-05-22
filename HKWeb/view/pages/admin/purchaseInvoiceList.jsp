<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Invoice List">
<%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("warehouseList", warehouseService.getAllActiveWarehouses());
  %>
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" var="pia"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Purchase Invoice List
  </s:layout-component>

  <s:layout-component name="content">

	  <fieldset class="right_label">
		  <legend>Search Purchase Invoice</legend>
		  <s:form beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction">
			  <label>PI ID:</label><s:text name="purchaseInvoice"/>
			  <label>Tin Number:</label><s:text name="tinNumber"/>
			  <label>Supplier Name:</label><s:text name="supplierName"/>
			  <label>Warehouse:</label>
			  <s:select name="warehouse">
				  <s:option value="">-All-</s:option>
				  <c:forEach items="${warehouseList}" var="warehouse">
					  <s:option value="${warehouse.id}">${warehouse.identifier}</s:option>
				  </c:forEach>
			  </s:select>
			  <label>CreatedBy:</label><s:select name="createdBy">
			  <s:option value="">-All-</s:option>
			  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="creatorList" value="id"
			                             label="name"/>
		  </s:select>
			  <br/>
			  <label>Reconciled</label>
			  <s:select name="reconciled">
				  <s:option value="">-All-</s:option>
				  <s:option value="true">True</s:option>
				  <s:option value="false">False</s:option>
			  </s:select>

			  <label>Status:</label><s:select name="purchaseInvoiceStatus">
			  <s:option value="">-All-</s:option>
			  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseInvoiceStatusList" value="id"
			                             label="name"/>
			   </s:select>

			  <label>Start date</label><s:text class="date_input startDate" style="width:150px"
			                                   formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
			                                   name="startDate"/>
			  <label>End date</label><s:text class="date_input endDate" style="width:150px"
			                                 formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>


			  <s:submit name="pre" value="Search"/>
		  </s:form>
	  </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pia}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pia}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Create Date</th>
        <th>Created By</th>
        <th>Supplier</th>
        <th>Supplier TIN</th>
        <th>Credit Days</th>
	    <th>GRNs/Invoice No.</th>
	    <th>GRN-Date</th>
        <th>Warehouse</th>
        <th>Last Update Date</th>
	    <th>Adv Payment</th>
	    <th>Physical Inv. Amt.</th>
        <th>Payable</th>
        <th>Est. Payment Date</th>
        <th>Payment Date</th>
        <th>Payment Details</th>
        <th>Recon-<br/>ciled</th>
	    <th>Status</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${pia.purchaseInvoiceList}" var="purchaseInvoice" varStatus="ctr">
        <tr>
          <td>${purchaseInvoice.id}</td>
          <td><fmt:formatDate value="${purchaseInvoice.createDate}" type="both" timeStyle="short"/></td>
          <td>${purchaseInvoice.createdBy.name}</td>
          <td>${purchaseInvoice.supplier.name}</td>
          <td>${purchaseInvoice.supplier.tinNumber}</td>
          <td>${purchaseInvoice.supplier.creditDays}</td>
	        <td>
		        <c:set var="advPayment" value="0"/>
		        <c:forEach var="grn" items="${purchaseInvoice.goodsReceivedNotes}">
			        <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view" target="_blank">
				        <s:param name="grn" value="${grn.id}"/>
				        ${grn.id}
			        </s:link>
			        - ${grn.invoiceNumber}
			        <br/>
		        </c:forEach>
		        <c:set var="advPayment" value="${purchaseInvoice.goodsReceivedNotes[0].purchaseOrder.advPayment }"/>
	        </td>
	        <td><fmt:formatDate value="${purchaseInvoice.goodsReceivedNotes[0].grnDate}" type="both" timeStyle="short"/></td>
	        <td>${purchaseInvoice.warehouse.identifier}</td>

	        <td><fmt:formatDate value="${purchaseInvoice.createDate}" type="both" timeStyle="short"/></td>
	        <td>
		        <fmt:formatNumber value="${advPayment}" type="currency" currencySymbol=" "
		                          maxFractionDigits="0"/></td>
		    <td><fmt:formatNumber value="${purchaseInvoice.physicalInvoiceAmount}" type="currency" currencySymbol=" "
		                          maxFractionDigits="0"/></td>
	        <td>
		        <fmt:formatNumber value="${purchaseInvoice.finalPayableAmount - advPayment}" type="currency" currencySymbol=" "
		                          maxFractionDigits="0"/></td>
	        <td><fmt:formatDate value="${purchaseInvoice.estPaymentDate}" pattern="dd-MMM-yyyy"/></td>
	        <td><fmt:formatDate value="${purchaseInvoice.paymentDate}" pattern="dd-MMM-yyyy"/></td>
	        <td>${purchaseInvoice.paymentDetails}
		        <s:link beanclass="com.hk.web.action.admin.payment.PaymentHistoryAction" target="_blank">Payment History
			        <s:param name="purchaseInvoiceId" value="${purchaseInvoice.id}"/>
		        </s:link>
	        </td>

          <td>
            <c:choose>
              <c:when test="${purchaseInvoice.reconciled}">
                Yes &#10004;
              </c:when>
              <c:otherwise>
                No
              </c:otherwise>                                                   
            </c:choose>
          </td>
	        <td>${purchaseInvoice.purchaseInvoiceStatus.name}</td>

          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="view" target="_blank">Edit/View
              <s:param name="purchaseInvoice" value="${purchaseInvoice.id}"/>
            </s:link>
	          <br/>
	        <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="paymentDetails" target="_blank"><br>Update status/payment
              <s:param name="purchaseInvoice" value="${purchaseInvoice.id}"/>
            </s:link>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pia}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pia}"/>

  </s:layout-component>
</s:layout-render>