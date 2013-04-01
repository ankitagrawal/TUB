<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ServiceQueueAction" var="serviceQueueBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Service Queue">
<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<s:layout-component name="heading">Service Queue</s:layout-component>
<s:layout-component name="content">

  <fieldset class="top_label">
    <ul>
      <div class="grouped grid_6">
        <s:form beanclass="com.hk.web.action.admin.queue.ServiceQueueAction" method="get" autocomplete="false">
          <li><label>Order ID</label> <s:text name="orderId"/></li>
          <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" id="gatewayOrderId"/></li>
          <li>
            <label> Payment Start
              date</label><s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
          </li>
          <li>
            <label>Payment End
              date</label><s:text class="date_input endDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
          </li>
          <div class="buttons"><s:submit name="searchOrders" value="Search"/></div>
        </s:form>
        <script language=javascript type=text/javascript>
          $('#gatewayOrderId').focus();
        </script>
      </div>
    </ul>
  </fieldset>

  <s:form beanclass="com.hk.web.action.admin.queue.ServiceQueueAction" autocomplete="off">
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${serviceQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${serviceQueueBean}"/>
    <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                     shippingOrders="${serviceQueueBean.shippingOrderList}" isServiceQueue="true"/>

    <div>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${serviceQueueBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${serviceQueueBean}"/>
    </div>
    <div id="hiddenShippingIds"></div>
    <div style="display:inline;float:left;">
      <s:submit name="moveToActionAwaiting" id="moveToActionAwaiting" value="Move Back to Action Awaiting"/>
    </div>
    <div style="display:inline;float:right;">
      <s:submit name="markShippingOrdersAsDelivered" id="markShippingOrdersAsDelivered"
                value="Mark Orders As Delivered"/></div>
    </s:form>
  <script type="text/javascript">
    $('#moveToActionAwaiting').click(function() {
      $('.shippingOrderDetailCheckbox').each(function() {
        var shippingOrderDetailCheckbox = $(this);
        var isChecked = shippingOrderDetailCheckbox.attr('checked');
        if (isChecked) {
          $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
        }
      });
      return true;
    });


    $('#markShippingOrdersAsDelivered').click(function() {
      $('.shippingOrderDetailCheckbox').each(function() {
        var shippingOrderDetailCheckbox = $(this);
        var isChecked = shippingOrderDetailCheckbox.attr('checked');
        if (isChecked) {
          $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
        }
      });
      return true;
    });


    $('.movetoactionqueue').click(function (e) {
        e.preventDefault();
        var ele = $(this);
        var link = ele.attr('href') + '&';
        link += "shippingOrderList[]=" + ele.attr("dataId");
        location.href = link;
    });



  </script>

</s:layout-component>
</s:layout-render>
