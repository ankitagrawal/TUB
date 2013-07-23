<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="hk" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction" var="shipmentQueueBean"/>

<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipment Awaiting Queue">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">Shipment Awaiting Queue</s:layout-component>
  <s:layout-component name="content">
    <fieldset class="top_label">
      <ul>
        <div class="grouped grid_12">
          <s:form beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction" method="get" autocomplete="false">
            <label>Gateway ID </label><s:text name="gatewayOrderId" id="gatewayOrderId"/>
            <label>Order ID </label> <s:text name="orderId"/>
            <s:select name="courier" class="courierService">
              <s:option value="">All Couriers</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id"
                                         label="name"/>
            </s:select>
		    <label>Zone:<label>
			<s:select name="zone">
				<s:option value="null">Select</s:option>
					  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
												 value="id"
												 label="name"/>
			</s:select>
			<label>Per Page</label>
            <s:select name="defaultPerPage">
              <s:option value="30">30</s:option>
              <s:option value="60">60</s:option>
              <s:option value="120">120</s:option>
            </s:select>
            <s:submit name="searchOrders" value="Search"/>
          </s:form>
        </div>
      </ul>
    </fieldset>
    <fieldset class="top_label">
      <ul>
        <div class="grouped grid_12">
          <s:form beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction" >
            <label>Courier</label>
            <s:select name="courier" class="courierService">
              <s:option value="">All Couriers</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id"
                                         label="name"/>
            </s:select>

	          <label>Zone:<label>
		          <s:select name="zone">
		          <s:option value="null">Select</s:option>
			          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
			                                     value="id"
			                                     label="name"/>
		          </s:select>
            <s:submit name="generateCourierReport">
                Download Courier Excel
              <s:param name="courierDownloadFunctionality" value="true" />
              </s:submit>
            <s:submit name="generatePDFs" value="Download Invoice PDF"/>
          </s:form>
        </div>
      </ul>
    </fieldset>

    <s:form beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction" autocomplete="off">
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
      <div style="float:right"><input type="submit" value="Mark All" id="markAll"/></div>
      <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                       shippingOrders="${shipmentQueueBean.shippingOrderList}" showCourier="true" isShipmentQueue="true"/>
      <div>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shipmentQueueBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shipmentQueueBean}"/>
      </div>
      <div id="hiddenShippingIds"></div>
      <div style="display:inline;float:left;">
        <s:submit name="moveToActionAwaiting" id="moveToActionAwaiting" value="Move Back to Action Awaiting"/>
      </div>
      <div style="display:none;float:right;">
        <s:submit name="markShippingOrdersAsShipped" id="markShippingOrdersAsShipped"
                  value="Mark Orders As Shipped"/></div>
    </s:form>
    <script type="text/javascript">
      $('#moveToActionAwaiting').click(function() {
          var index = 0;
        $('.shippingOrderDetailCheckbox').each(function() {
          var shippingOrderDetailCheckbox = $(this);
          var isChecked = shippingOrderDetailCheckbox.attr('checked');
          if (isChecked) {
              var reasonId = '.shippingOrderReason_'+$(this).attr('dataId');
              var reason = $(reasonId);
              $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
              $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList['+index+'].reason" value="'+reason.val()+'"/>');
              index++;
          }
        });
        return true;
      });


      $('#markShippingOrdersAsShipped').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
          var shippingOrderDetailCheckbox = $(this);
          var isChecked = shippingOrderDetailCheckbox.attr('checked');
          if (isChecked) {
            $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
          }
        });
        return true;
      });

      $('#markAll').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
          var shippingOrderDetailCheckbox = $(this);
          var isChecked = shippingOrderDetailCheckbox.attr('checked');
          shippingOrderDetailCheckbox.attr("checked", true);
        });
        return false;
      });
      $("#moveToActionAwaiting").click(function(){
    		$(this).hide();
    		$("#markShippingOrdersAsShipped").hide();
    	});
      
      $("#markShippingOrdersAsShipped").click(function(){
  		$(this).hide();
  		$("#moveToActionAwaiting").hide();
  	});
    

    </script>
  </s:layout-component>
</s:layout-render>
