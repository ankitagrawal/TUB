<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumGrnStatus" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Goods Received Note (GRN) List">
  <%
      WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
      pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  %>
	<c:set var="checkinInProcess" value="<%=EnumGrnStatus.InventoryCheckinInProcess.getId()%>"/>
	<c:set var="inventoryClosedStatus" value="<%=EnumGrnStatus.Closed.getId()%>"/>
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.GRNAction" var="poa"/>
  <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  </s:layout-component>

  <s:layout-component name="heading">
    Goods Received Note (GRN) List
  </s:layout-component>

  <s:layout-component name="content">
    <fieldset class="right_label">
      <legend>Download GRN</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.GRNAction">
        <label>Start
          Date:</label><s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
        &nbsp; &nbsp;
        <label>End
          Date:</label><s:text class="date_input endDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
        &nbsp; &nbsp;
        <label>Warehouse: </label>
          <c:choose>
            <c:when test="${whAction.setWarehouse != null}">
              <s:hidden name="warehouse" value="${whAction.setWarehouse}"/>
              ${whAction.setWarehouse.identifier}
            </c:when>
            <c:otherwise>
              <s:select name="warehouse">
                <s:option value="0">-All-</s:option>
                <c:forEach items="${whList}" var="wh">
                  <s:option value="${wh.id}">${wh.identifier}</s:option>
                </c:forEach>
              </s:select>
            </c:otherwise>
          </c:choose>   &nbsp; &nbsp;
        <label>GRN
          Status: </label>
         <s:select name="grnStatusValue" value="${poa.grn.grnStatus.id}"
                        style="width:200px;">
             <s:option value="">-All-</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="grnStatusList"
                                           label="name" value="id"/>
              </s:select>
          <br/>
          <label style="margin-top:20px;">Reconciled</label>
          <s:select name="reconciled"  style="margin-top:20px;">
            <s:option value="">-All-</s:option>
            <s:option value="true">True</s:option>
            <s:option value="false">False</s:option>
          </s:select>
          <br/>
        <s:submit name="downloadGRN" value="Download GRN"/>
      </s:form>
    </fieldset>

    <fieldset class="right_label">
      <legend>Search GRN</legend>
      <s:form beanclass="com.hk.web.action.admin.inventory.GRNAction">
        <label>GRN ID:</label><s:text name="grn"/>
        <label>VariantID:</label><s:text name="productVariant"/>
        <label>Invoice Number:</label><s:text name="invoiceNumber"/>
        <label>Tin Number:</label><s:text name="tinNumber"/> <br/>
        <label>Supplier Name:</label><s:text name="supplierName"/>
        <label>Reconciled</label>
          <s:select name="reconciled" >
            <s:option value="">-All-</s:option>
            <s:option value="true">True</s:option>
            <s:option value="false">False</s:option>
          </s:select>
        <label>Status:</label><s:select name="grnStatus">
        <s:option value="">-All-</s:option>
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="grnStatusList" value="id" label="name"/>
      </s:select>
        <label>Warehouse: </label>
          <c:choose>
            <c:when test="${whAction.setWarehouse != null}">
              <s:hidden name="warehouse" value="${whAction.setWarehouse}"/>
              ${whAction.setWarehouse.identifier}
            </c:when>
            <c:otherwise>
              <s:select name="warehouse">
                <s:option value="0">-All-</s:option>
                <c:forEach items="${whList}" var="wh">
                  <s:option value="${wh.id}">${wh.identifier}</s:option>
                </c:forEach>
              </s:select>
            </c:otherwise>
          </c:choose>
        <s:submit name="pre" value="Search GRN"/>
        <s:submit name="generateExcelReport" value="Download to Excel" />
      </s:form>
    </fieldset>


    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
	  <s:form beanclass="com.hk.web.action.admin.inventory.GRNAction">
      <s:submit name="generatePurchaseInvoiceCheck" value="Generate Purchase Invoice"/>
    <table class="zebra_vert">
      <thead>
      <tr>
        <th>GRN ID</th>
        <th>PO ID</th>
        <th>PI ID</th>
        <th>GRN Date</th>
        <th>Invoice No</th>
        <th>Receieved By</th>
        <th>Warehouse</th>
        <th>Supplier</th>
        <th>Supplier TIN</th>
        <th>Status</th>
        <th>Reconciled</th>
        <th>Payable</th>
        <th>Est Payment Date</th>
        <th>Actions</th>
	      <th>Select GRNs</th>
      </tr>
      </thead>
      <c:forEach items="${poa.grnList}" var="grn" varStatus="ctr">
        <tr class="grnRow">
          <td>${grn.id}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" event="pre">
              <s:param name="purchaseOrder" value="${grn.purchaseOrder.id}"/>
              ${grn.purchaseOrder.id}
            </s:link>
          </td>
          <td>
            <c:forEach var="purchaseInvoice" items="${grn.purchaseInvoices}">
              <s:link beanclass="com.hk.web.action.admin.inventory.PurchaseInvoiceAction" event="view" target="_blank">
                <s:param name="purchaseInvoice" value="${purchaseInvoice.id}"/>
                ${purchaseInvoice.id}
              </s:link>
            </c:forEach>
          </td>
          <td><fmt:formatDate value="${grn.grnDate}"/></td>
          <td>${grn.invoiceNumber}</td>
          <td>${grn.receivedBy.name}</td>
          <td>${grn.warehouse.identifier}</td>
          <td>${grn.purchaseOrder.supplier.name}</td>
          <td>${grn.purchaseOrder.supplier.tinNumber}</td>
          <td>${grn.grnStatus.name}</td>
          <td>
            <c:choose>
              <c:when test="${grn.reconciled}">
                Yes &#10004;
              </c:when>
              <c:otherwise>
                No
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <fmt:formatNumber value="${grn.payable}" type="currency" currencySymbol=" " maxFractionDigits="0"/></td>
          <td><fmt:formatDate value="${grn.estPaymentDate}"/></td>
	        <td>
		        <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view">Edit/View
			        <s:param name="grn" value="${grn.id}"/></s:link>
		        <br/>
		        <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="print" target="_blank">Print
			        <s:param name="grn" value="${grn.id}"/></s:link>
		        <br/>
		        <c:if test="${grn.grnStatus.id < inventoryClosedStatus}">
			        <s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" event="pre">
				        Inv.Checkin
				        <s:param name="grn" value="${grn.id}"/></s:link>

			        <br/>
		        </c:if>
                <c:if test="${grn.grnStatus.id == checkinInProcess || grn.grnStatus.id == inventoryClosedStatus}">
			        <s:link beanclass="com.hk.web.action.admin.queue.PutListAction" event="putList">
				        Put List
				        <s:param name="grn" value="${grn.id}"/></s:link>
		        </c:if>
	        </td>
	        <td>
            <s:checkbox name="grnListForPurchaseInvoice[]" value="${grn.id}" class="purchaseLineItemCheckBox"/>
	        </td>
        </tr>
      </c:forEach>
      
      
    </table>
	  </s:form>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
  </s:layout-component>
</s:layout-render>