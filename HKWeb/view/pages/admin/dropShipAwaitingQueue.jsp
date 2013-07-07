<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction" var="shipmentQueueBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
 <c:set var="shippingOrderStatusDropShippingAwaiting" value="<%=EnumShippingOrderStatus.SO_ReadyForDropShipping.getId()%>"/>
 <c:set var="shippingOrderStatusCheckedOut" value="<%=EnumShippingOrderStatus.SO_CheckedOut.getId()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Drop Ship Awaiting Queue">
<s:layout-component name="htmlHead">
  <%
      ShippingOrderStatusService shippingOrderStatusService = ServiceLocatorFactory.getService(ShippingOrderStatusService.class);
        pageContext.setAttribute("applicableOrderStatusList", shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForDropShippingQueue()));
        CategoryDao categoryDao = (CategoryDao)ServiceLocatorFactory.getService(CategoryDao.class);
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
<s:layout-component name="heading">Drop Ship Queue</s:layout-component>
<s:layout-component name="content">

<fieldset class="top_label">
  <ul>
    <div class="grouped grid_12">
      <s:form beanclass="com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction" method="get" autocomplete="false">
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
          <br>

          <li><label style="float:left;width: 100px;"><strong> SO Category </strong></label> &nbsp;
                    <div class="checkBoxList">
                        <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                            <label><s:checkbox name="basketCategories[${ctr.index}]"
                                               value="${category.name}"/> ${category.displayName}</label>
                            <%--<br/>--%>
                        </c:forEach>
                    </div>
        </li>
       <div  style="text-align: right">
        <s:submit name="searchOrders" value="Search"/>
       </div>    
        <%--<s:submit name="chooseItemsForPrintingPicking" value="Select Category and Send For Printing/Picking"/>--%>
      </s:form>
      <script language=javascript type=text/javascript>
        $('#gatewayOrderId').focus();
      </script>
    </div>
  </ul>
</fieldset>

<s:form beanclass="com.hk.web.action.admin.queue.DropShippingAwaitingQueueAction" autocomplete="off">


  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
  <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                   shippingOrders="${shipmentQueueBean.shippingOrderList}" isDropShipQueue="true"/>

  <div id="hiddenShippingIds"></div>
  <div>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  </div>
  <div style="display:inline;float:left;">
      <c:if test="${shipmentQueueBean.shippingOrderStatus.id == shippingOrderStatusDropShippingAwaiting || shipmentQueueBean.shippingOrderStatus.id == shippingOrderStatusCheckedOut }">
          <s:submit name="moveToActionAwaiting" class="shippingOrderActionBtn" value="Move Back to Action Awaiting"/>
          <s:submit name="reAssignToPackingQueue" id="reAssignToPackingQueue" class="shippingOrderActionBtn"
                    value="Re-Assign for process" style="display:none;"/>
           <s:submit name="markShippingOrdersAsShipped" class="button_orange shipped shippingOrderActionBtn" value="Mark Order as Shipped"/>
       </c:if>


      <br>
      <br>
  </div>
  <%--<c:if test="${applicableLineItemStatus == lineItemStatusId_PrePrinting}">--%>
  <%--<div style="display:inline;float:right;"><s:submit name="doBatchPrintingPrintJobCart" value="Do Batch Printing, Print Job Cart"/></div>--%>
  <%--</c:if>--%>
</s:form>
<script type="text/javascript">
  $("select[name='shippingOrderStatus']").change(function() {
    var selectedOrderStatus = $(this).val();
    if (selectedOrderStatus == <%=EnumShippingOrderStatus.SO_Picking.getId()%>) {
      $("#reAssignToPackingQueue").show();
    } else {
      $("#reAssignToPackingQueue").hide();
    }
  });

  var grncheck = false;
  $('.shipped').click(function() {
      var con = confirm("Verify that you are saving correct information ");
      if (con == true) {
          grncheck = true;
      } else {
          return false;
      }
      if (grncheck) {
          var bool = confirm("Verify that you have already created GRN ");
          if (bool == true) {
              return true
          } else {
              return false;
          }
      }

  });

  $('.shippingOrderActionBtn').click(function() {
      var index = 0;
      $('.shippingOrderDetailCheckbox').each(function() {
          var shippingOrderDetailCheckbox = $(this);
          var isChecked = shippingOrderDetailCheckbox.attr('checked');
          if (isChecked) {
              var reasonId = '.shippingOrderReason_'+$(this).attr('dataId');
              var reason = $(reasonId);
              $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList['+index+']" value="' + $(this).attr('dataId') + '"/>');
              $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList['+index+'].reason" value="'+reason.val()+'"/>');
              index++;
          }
      });
      return true;
  });

</script>
</s:layout-component>
</s:layout-render>
