<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.PackingAwaitingQueueAction" var="shipmentQueueBean"/>
<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>


<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Packing Awaiting Queue">
<s:layout-component name="htmlHead">
  <%
      ShippingOrderStatusService shippingOrderStatusService = ServiceLocatorFactory.getService(ShippingOrderStatusService.class);
        pageContext.setAttribute("applicableOrderStatusList", shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForProcessingQueue()));
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
<s:layout-component name="heading">Packing Awaiting Queue</s:layout-component>
<s:layout-component name="content">

<fieldset class="top_label">
  <ul>
    <div class="grouped grid_12">
      <s:form beanclass="com.hk.web.action.admin.queue.PackingAwaitingQueueAction" method="get" autocomplete="false">
          <div style="width:1100px; margin:10px;">
              <label>SO Gateway ID </label><s:text name="gatewayOrderId" id="gatewayOrderId"/>
              <label>SO Order ID </label> <s:text name="shippingOrderId"/>
              <label>BO Gateway ID </label><s:text name="baseGatewayOrderId" id="baseGatewayOrderId"/>
              <label>BO Order ID </label> <s:text name="baseOrderId"/>
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
          </div>

          <div style="width:1120px; margin:20px;">
              <label>Escalation Start
                  Date </label><s:text class="date_input startDate" style="width:150px"
                                       formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
              <label>End
                  Date </label><s:text class="date_input endDate" style="width:150px"
                                       formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
              <label>Payment Start
                  Date </label><s:text class="date_input startDate" style="width:150px"
                                       formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                       name="paymentStartDate"/>
              <label>Payment End
                  Date </label><s:text class="date_input endDate" style="width:150px"
                                       formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="paymentEndDate"/>
          </div>

          <div style="width:1100px; margin:20px;">
              <li><label style="float:left;width: 80px;">SO Category</label>

                  <div class="checkBoxList">
                      <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                          <label><s:checkbox name="basketCategories[${ctr.index}]"
                                             value="${category.name}"/> ${category.displayName}</label>
                      </c:forEach>
                  </div>
              </li>
          </div>

          <s:submit style="margin:0 0 10px 25px;" name="searchOrders" value="Search"/>

        <%--<s:submit name="chooseItemsForPrintingPicking" value="Select Category and Send For Printing/Picking"/>--%>
      </s:form>
      <script language=javascript type=text/javascript>
        $('#gatewayOrderId').focus();
      </script>
    </div>
  </ul>
</fieldset>

<s:form beanclass="com.hk.web.action.admin.queue.PackingAwaitingQueueAction" autocomplete="off">


  <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
  <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  <div style="float:right"><input type="submit" value="Mark All" id="markAll"/></div>
  <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                   shippingOrders="${shipmentQueueBean.shippingOrderList}" isProcessingQueue="true"/>
    <div id="hiddenShippingIds"></div>
    <div>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
  </div>
  <div style="display:inline;float:left;">
    <s:submit id="moveToWaiting" name="moveToActionAwaiting" class="shippingOrderActionBtn" value="Move Back to Action Awaiting"/>
    <s:submit name="reAssignToPackingQueue" id="reAssignToPackingQueue" class="shippingOrderActionBtn"
              value="Re-Assign for process" style="display:none;"/>
  </div>
  <%--<c:if test="${applicableLineItemStatus == lineItemStatusId_PrePrinting}">--%>
  <%--<div style="display:inline;float:right;"><s:submit name="doBatchPrintingPrintJobCart" value="Do Batch Printing, Print Job Cart"/></div>--%>
  <%--</c:if>--%>
</s:form>
<script type="text/javascript">

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

  $("select[name='shippingOrderStatus']").change(function() {
    var selectedOrderStatus = $(this).val();
    if (selectedOrderStatus == <%=EnumShippingOrderStatus.SO_Picking.getId()%>) {
      $("#reAssignToPackingQueue").show();
        $("#markAll").show();
    } else {
      $("#reAssignToPackingQueue").hide();
        $("#markAll").hide();
    }
  });
  
  var selectedSOStatus = $("select[name='shippingOrderStatus']").val()
  
  if(selectedSOStatus == <%=EnumShippingOrderStatus.SO_Picking.getId()%>){
  	$("#reAssignToPackingQueue").show();
  	$("#markAll").show();
  }else{
  	$("#reAssignToPackingQueue").hide();
  	$("#markAll").hide();
  }

    $('#markAll').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            shippingOrderDetailCheckbox.attr("checked", true);
        });
        return false;
    });
$("#moveToWaiting").click(function(){
	$(this).hide();
});

    /*$('.lineItemCheckBox').click(function() {
     $(this).parent().parent("tr").toggleClass('highlight');
     });*/
</script>

</s:layout-component>
</s:layout-render>
