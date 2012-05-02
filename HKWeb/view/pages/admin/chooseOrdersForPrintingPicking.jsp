<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.dao.catalog.category.CategoryDao" %>
<%@ page import="mhc.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.ChooseOrdersForPrintPickAction" var="printPickBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<%
  int lineItemGlobalCtr = 0;
%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Print Pack Awaiting Queue">
  <s:layout-component name="htmlHead">
    <%

      ShippingOrderStatusService shippingOrderStatusService = InjectorFactory.getInjector().getInstance(ShippingOrderStatusService.class);
      pageContext.setAttribute("statusForPrinting", shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ReadyForProcess));
      pageContext.setAttribute("statusForPicking", shippingOrderStatusService.find(EnumShippingOrderStatus.SO_MarkedForPrinting));
      CategoryDao categoryDao = InjectorFactory.getInjector().getInstance(CategoryDao.class);
      pageContext.setAttribute("categoryList", categoryDao.getPrimaryCategories());
    %>

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  </s:layout-component>
  <s:layout-component name="heading">Print Pick Queue</s:layout-component>
  <s:layout-component name="content">
    <script type="text/javascript">
      $(document).ready(function() {
        $(".batchPrinting").click(function() {
          var invoiceLinks = document.getElementsByClassName("invoiceLink");
          var personalCareInvoiceLinks = document.getElementsByClassName("personalCareInvoiceLink");
          var len = invoiceLinks.length;
          for (var i = 0; i < len; i++) {
            var j = i + 1;
            //Normal Partial Invoice
            document.getElementById("orderInvoice").src = invoiceLinks[i].href;
            if (document.getElementById("orderInvoice").src != "") {
              alert("Sending copy 1 to printer of " + j + " of  " + len + " orders");
              printInvoice('orderInvoice');
            }
            //Personal Care Partial Invoice
            document.getElementById("orderInvoice").src = personalCareInvoiceLinks[i].href;
            if (document.getElementById("orderInvoice").src != "") {
              alert("Sending copy 2 to printer of " + j + " of  " + len + " orders");
              printInvoice('orderInvoice');
            }
          }
          return true;
        });

        $('#orderSelector').click(function() {
          if ($(this).attr("checked") == "checked") {
            $('.orderCheckBox').each(function() {
              this.checked = true;
            })
          } else {
            $('.orderCheckBox').each(function() {
              this.checked = false;
            })
          }
        });

        function printInvoice(elementId) {
          var getMyFrame = document.getElementById(elementId);
          getMyFrame.focus();
          getMyFrame.contentWindow.print();
        }

      });
    </script>
    <div align="left">
      <c:if test="${printPickBean.category != null}">
        Showing Orders for Category: <strong>${printPickBean.category.displayName}</strong>
      </c:if>
    </div>
    <div align="center">
      <s:form beanclass="com.hk.web.action.admin.ChooseOrdersForPrintPickAction" method="get" autocomplete="false">
        Category
        <s:select name="category" value="${printPickBean.category.name}">
          <c:forEach items="${categoryList}" var="category">
            <s:option value="${category.name}">${category.displayName} </s:option>
          </c:forEach>
        </s:select>

        <s:submit name="searchOrdersForPrinting" value="Search By Basket Category" style="font-size:0.9em"/>

        SO Gateway Order Id:<s:text name="gatewayOrderId"/>
        BO Gateway Order Id:<s:text name="baseGatewayOrderId"/>
        <s:submit name="searchOrdersForPrinting" value="Search By Gateway OrderId"
                  style="font-size:0.9em"/>
      </s:form>
    </div>

    <s:form beanclass="com.hk.web.action.admin.ChooseOrdersForPrintPickAction" autocomplete="off">
      <%--  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${printPickBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${printPickBean}"/>--%>
      <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                       shippingOrders="${printPickBean.shippingOrdersList}"/>
      <s:hidden name="category" value="${printPickBean.category}"/>
      <s:hidden name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
      <div id="hiddenShippingIds"></div>
      <%-- <div>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${printPickBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${printPickBean}"/>
      </div>--%>
      <c:if test="${fn:length(printPickBean.shippingOrdersList) > 0}">
        <c:if test="${printPickBean.shippingOrderStatus == statusForPrinting}">
          <div style="float:left; font-size: 0.9em; margin-top: 5px; margin-left:20px">

            <s:submit name="batchPrintOrders" class="batchPrinting" value=" Do Batch Printing"/>
            <s:hidden name="baseGatewayOrderId" value="${printPickBean.baseGatewayOrderId}"/>
            <s:hidden name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
          </div>
        </c:if>
        <c:if test="${printPickBean.shippingOrderStatus == statusForPicking}">
          <div style="float:left; font-size: 0.9em; margin-top: 7px; margin-left:50px">
            <s:submit name="sendOrdersBackToProcessingQueue" id="sendOrdersBackToProcessingQueue"
                      value="Move orders to processing Queue"/>
            <s:link beanclass="com.hk.web.action.admin.JobCartAction" target="_blank" class="button_orange">
              <s:param name="category" value="${printPickBean.category}"/>
              <s:hidden name="baseGatewayOrderId" value="${printPickBean.baseGatewayOrderId}"/>
              <s:hidden name="gatewayOrderId" value="${printPickBean.gatewayOrderId}"/>
              Print Job Card
            </s:link>
            <s:submit name="clearPickingQueue" value="Job Done - Clear Queue"/>
          </div>
        </c:if>
      </c:if>

    </s:form>

    <iframe id="orderInvoice" name="orderInvoice" src=""
            style="dispaly:none;visibility:hidden;"></iframe>
    <script type="text/javascript">
      $('#sendOrdersBackToProcessingQueue').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
          var shippingOrderDetailCheckbox = $(this);
          var isChecked = shippingOrderDetailCheckbox.attr('checked');
          if (isChecked) {
            $('#hiddenShippingIds').append('<input type="hidden" name="shipping'OrdersList[]" value="' + $(this).attr('dataId') + '"/>');
          }
        });
        return true;
      });


    </script>
  </s:layout-component>
</s:layout-render>
