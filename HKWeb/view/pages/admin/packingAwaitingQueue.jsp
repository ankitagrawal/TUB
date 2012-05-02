<%@ page import="app.bootstrap.guice.InjectorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="mhc.service.dao.CategoryDao" %>
<%@ page import="mhc.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.admin.queue.PackingAwaitingQueueAction" var="shipmentQueueBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>


<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Packing Awaiting Queue">
<s:layout-component name="htmlHead">
  <%
    ShippingOrderStatusService shippingOrderStatusService = InjectorFactory.getInjector().getInstance(ShippingOrderStatusService.class);
    pageContext.setAttribute("applicableOrderStatusList", shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForProcessingQueue()));
    CategoryDao categoryDao = InjectorFactory.getInjector().getInstance(CategoryDao.class);
    pageContext.setAttribute("categoryList", categoryDao.getPrimaryCategories());
  %>

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  <style type="text/css">
    .fieldLabel {
      font-size: .8em;
      color: #999999;
      padding-top: 2px;
    }

    table.align_top tr td {
      vertical-align: top;

    }

    table tr.row_border {
      outline: 1px solid #cccccc;
    }

      /*tr.row_shipped {
        outline: 0 solid #cccccc;
      }*/

    .row_shipped td {
      background-color: #b8ffcf;
    }
  </style>
  <script type="text/javascript">
    $(document).ready(function() {

      String.prototype.beginsWith = function(t, i) {
        if (i == false) {
          return
          (t == this.substring(0, t.length));
        } else {
          return (t.toLowerCase()
              == this.substring(0, t.length).toLowerCase());
        }
      }

      $('#liveSearchBox').keyup(function() {
        var searchString = $(this).val().toLowerCase();
        $('.orderRow').each(function() {
          if ($(this).find('.orderId').text().toLowerCase().indexOf(searchString) == -1) {
            $(this).hide();
          } else {
            $(this).show();
          }
        });
      });

    });
  </script>
</s:layout-component>
<s:layout-component name="heading">Packing Awaiting Queue</s:layout-component>
<s:layout-component name="content">

<fieldset class="top_label">
  <ul>
    <div class="grouped grid_12">
      <s:form beanclass="web.action.admin.queue.PackingAwaitingQueueAction" method="get" autocomplete="false">
        <label>SO Gateway ID </label><s:text name="gatewayOrderId" id="gatewayOrderId"/>
        <label>SO Order ID </label> <s:text name="shippingOrderId"/>
        <label>BO Gateway ID </label><s:text name="baseGatewayOrderId" id="baseGatewayOrderId"/>
        <label>BO Order ID </label> <s:text name="baseOrderId"/>
        <label>Escalation Start
          Date </label><s:text class="date_input startDate" style="width:150px"
                               formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
        <label>End
          Date </label><s:text class="date_input endDate" style="width:150px"
                               formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
        <label>Status </label>
        <s:select name="shippingOrderStatus">
          <option value="">Any order status</option>
          <c:forEach items="${applicableOrderStatusList}" var="orderStatus">
            <s:option value="${orderStatus.id}">${orderStatus.name}</s:option>
          </c:forEach>
        </s:select>
        <label>Per Page</label>
        <s:select name="defaultPerPage">
          <s:option value="30">30</s:option>
          <s:option value="60">60</s:option>
          <s:option value="120">120</s:option>
        </s:select>
        <%--<s:select name="category">--%>
        <%--<c:forEach items="${categoryList}" var="category">--%>
        <%--<s:option value="${category.name}">${category.displayName}</s:option>--%>
        <%--</c:forEach>--%>
        <%--</s:select>--%>
        <s:submit name="searchOrders" value="Search"/>
        <%--<s:submit name="chooseItemsForPrintingPicking" value="Select Category and Send For Printing/Picking"/>--%>
      </s:form>
      <script language=javascript type=text/javascript>
        $('#gatewayOrderId').focus();
      </script>
    </div>
  </ul>
</fieldset>

<s:form beanclass="web.action.admin.queue.PackingAwaitingQueueAction" autocomplete="off">


  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
  <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                   shippingOrders="${shipmentQueueBean.shippingOrderList}"/>

  <%--<table class="align_top" width="100%">
<thead>
<tr>
<th width="185px">Order</th>
<th width="175px">User & Adddress</th>
<th>Items and Qty[Checkedout Qty]</th>
<th></th>
</tr>
</thead>
<c:forEach items="${shipmentQueueBean.shippingOrderList}" var="order" varStatus="ctr">
<c:if test="${!order.isExclusivelyServiceOrder}">
<s:hidden name="shippingOrderList[${ctr.index}]"/>
<tr class="${ctr.index % 2 == 0 ? '' : 'alt'} orderRow">
<td>
<strong class="orderId">${order.gatewayOrderId}</strong><br/>
(<s:link beanclass="web.action.InvoiceAction" event="partialInvoice" target="_blank">
<s:param name="order" value="${order}"/>
Invoice
</s:link>)(<s:link beanclass="web.action.InvoiceAction" event="partialInvoice"
                           target="_blank" class="invoiceLink">
<s:param name="order" value="${order}"/>
<s:param name="printable" value="true"/>
Partial PC Invoice
</s:link>)<br/>
Escalation <fmt:formatDate value="${(hk:getEscalationDate(order))}" type="both" timeStyle="short"/><br/>
Order <fmt:formatDate value="${order.payment.paymentDate}" pattern="dd/MM/yyyy"/><br/>
<s:link beanclass="web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
  Lifecycle
  <s:param name="order" value="${order}"/>
</s:link>
<s:link beanclass="web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
  <c:if test="${!empty hk:orderComments(order)}">
    <text style="color:#f88; font-weight:bold">Comments!</text>
  </c:if>
  <c:if test="${empty hk:orderComments(order)}">Add comment</c:if>
  <s:param name="order" value="${order}"/>
</s:link>
</td>
<td>
  ${order.user.email}<br/>
  ${order.address.name}<br/>
  ${order.address.city} - ${order.address.pin}<br/>
  ${order.address.state}<br/>
Ph. ${order.address.phone}<br/>
</td>
<td style="padding: 0" valign="top">
  --%><%--<c:if test="${! hk:allItemsCheckedOut(order)}">--%><%--
              <table width="100%">
                <c:forEach items="${order.lineItems}" var="lineItem" varStatus="lineItemCtr">
                  <c:if test="${lineItemTypeId_Product == lineItem.lineItemType.id}">
                    <c:if
                        test="${lineItem.productVariant.product.service == null || !lineItem.productVariant.product.service}">
                      <c:if test="${lineItemStatusId_Processable == lineItem.lineItemStatus.id}">
                        <c:set value="<%=lineItemGlobalCtr%>" var="lineItemGlobalCtr"/>
                        <tr class="shippingRow ${lineItem.lineItemStatus.id == lineItemStatusId_Shipped ? 'row_shipped' : 'row_border'}">
                          <td width="60%">
                            <s:hidden name="lineItems[${lineItemGlobalCtr}]" value="${lineItem.id}"/>
                            <c:if test="${lineItem.comboInstance != null}">
                                <span style="color:crimson;text-decoration:underline">
                                  (Part of Combo: ${lineItem.comboInstance.combo.name})
                                </span><br/>
                            </c:if>
                              ${lineItem.productVariant.product.name}
                              <span class="gry">
                                  ${lineItem.productVariant.optionsCommaSeparated}
                                <c:if test="${not empty lineItem.lineItemConfig.lineItemConfigValues}">

                                  <c:set var="TH" value="TH"/>
                                  <c:set var="THBF" value="THBF"/>
                                  <c:set var="CO" value="CO"/>
                                  <c:set var="COBF" value="COBF"/>


                                  <c:forEach items="${lineItem.lineItemConfig.lineItemConfigValues}" var="configValue"
                                             varStatus="configCtr">
                                    <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
                                    <c:set var="addParam" value="${variantConfigOption.additionalParam}"/>
                                    ${variantConfigOption.displayName} : ${configValue.value}
                                    <c:if
                                        test="${(addParam ne TH) or (addParam ne THBF) or (addParam ne CO) or (addParam ne COBF) }">
                                      <c:if
                                          test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                                        (R)
                                      </c:if>
                                      <c:if
                                          test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                                        (L)
                                      </c:if>
                                    </c:if>
                                    ${!configCtr.last?',':''}

                                  </c:forEach>
                                </c:if>
                              </span>
                          </td>
                          <td width="40%">
                              ${lineItem.qty}
                            <span style="color:green; font-weight:bold;">[${hk:checkedoutItemsCount(lineItem)}]</span>
                            <b>(${lineItem.productVariant.netInventory})</b>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                              ${lineItem.lineItemStatus.name}
                          </td>
                        </tr>
                        <%
                          lineItemGlobalCtr++;
                        %>
                      </c:if>
                      <c:if test="${lineItemStatusId_Processable != lineItem.lineItemStatus.id}">
                        <tr>
                          <td width="60%">
                              ${lineItem.productVariant.product.name}
                                  <span class="gry">
                                      ${lineItem.productVariant.optionsCommaSeparated}
                                  </span>
                          </td>
                          <td width="40%">
                              ${lineItem.qty}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                              ${lineItem.lineItemStatus.name}</td>
                        </tr>
                      </c:if>
                    </c:if>
                  </c:if>
                </c:forEach>
              </table>
                --%><%--</c:if>--%><%--
            </td>
            <td class="checkBox">
              <s:checkbox name="shippingOrderList[${ctr.index}].selected" class="lineItemCheckBox"/>
            </td>
          </tr>
        </c:if>
      </c:forEach>
    </table>--%>
  <div id="hiddenShippingIds"></div>
  <div>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  </div>
  <div style="display:inline;float:left;">
    <s:submit name="moveToActionAwaiting" class="shippingOrderActionBtn" value="Move Back to Action Awaiting"/>
    <s:submit name="reAssignToPackingQueue" id="reAssignToPackingQueue" class="shippingOrderActionBtn"
              value="Re-Assign for process" style="display:none;"/>
  </div>
  <%--<c:if test="${applicableLineItemStatus == lineItemStatusId_PrePrinting}">--%>
  <%--<div style="display:inline;float:right;"><s:submit name="doBatchPrintingPrintJobCart" value="Do Batch Printing, Print Job Cart"/></div>--%>
  <%--</c:if>--%>
</s:form>
<script type="text/javascript">
  $('.shippingOrderActionBtn').click(function() {
    $('.shippingOrderDetailCheckbox').each(function() {
      var shippingOrderDetailCheckbox = $(this);
      var isChecked = shippingOrderDetailCheckbox.attr('checked');
      if (isChecked) {
        $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
      }
    });
    return true;
  });

  $("select[name='shippingOrderStatus']").change(function() {
    var selectedOrderStatus = $(this).val();
    if (selectedOrderStatus == <%=EnumShippingOrderStatus.SO_Picking.getId()%>) {
      $("#reAssignToPackingQueue").show();
    } else {
      $("#reAssignToPackingQueue").hide();
    }
  });

  /*$('.lineItemCheckBox').click(function() {
   $(this).parent().parent("tr").toggleClass('highlight');
   });*/
</script>

</s:layout-component>
</s:layout-render>
