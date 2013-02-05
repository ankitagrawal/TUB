<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ShipmentInstallationAwaitingQueueAction" var="installationQueueBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<s:layout-component name="heading">Installation Awaiting Queue</s:layout-component>
<s:layout-component name="content">
  <s:form beanclass="com.hk.web.action.admin.queue.ShipmentInstallationAwaitingQueueAction" method="get" autocomplete="false">
    <fieldset class="top_label">
      <ul>
        <div class="grouped">
          <li><label>Order ID</label> <s:text name="orderId"/></li>
          <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId" class="gatewayOrderId"/></li>
          <li>
            <div class="buttons"><s:submit name="searchOrders" value="Search"/></div>
          </li>
        </div>
      </ul>
    </fieldset>
  </s:form>

  <s:form beanclass="com.hk.web.action.admin.queue.ShipmentInstallationAwaitingQueueAction" autocomplete="off">
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${installationQueueBean}"/>
    <%--<h7> Showing Total -- ${installationQueueBean.noOfInstallableItems} -- Items </h7>--%>
      <%--<br>--%>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${installationQueueBean}"/>
    <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                     shippingOrders="${installationQueueBean.shippingOrderList}"/>
    <div>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${installationQueueBean}"/>
     <%--<br>--%>
     <%--<h7> Showing Total -- ${installationQueueBean.noOfInstallableItems} -- Items </h7>--%>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${installationQueueBean}"/>
    </div>
    <div id="hiddenShippingIds"></div>
    <div style="display:inline;float:right;">
      <s:submit name="markShippingOrderAsInstalled" id="markShippingOrdersAsInstalled"
                value="Mark Orders As Installed"/></div>
  </s:form>
  <script type="text/javascript">
    $('#markShippingOrdersAsInstalled').click(function() {
      $('.shippingOrderDetailCheckbox').each(function() {
        var shippingOrderDetailCheckbox = $(this);
        var isChecked = shippingOrderDetailCheckbox.attr('checked');
        if (isChecked) {
          $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
        }
      });
      return true;
    });

  </script>
</s:layout-component>
</s:layout-render>


