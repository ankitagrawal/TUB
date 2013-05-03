<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction" var="shipmentQueueBean"/>
<%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllWarehouses());
%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Download Courier Excel">

<s:layout-component name="htmlHead">

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

</s:layout-component>

<s:layout-component name="content">
<div class="downloadCourierExcel">
  <s:form beanclass="com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction">
    <fieldset class="right_label">
      <legend>Download Courier Excel</legend>
      <ul>
        <li>
            <label>Select Courier</label>
            <s:select name="courier" class="courierService">
              <s:option value="">All Couriers</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                         label="name"/>
            </s:select>
        </li>
        <li>
          <label>Start
            date</label><s:text class="date_input startDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
        </li>
        <li>
          <label>End
            date</label><s:text class="date_input endDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
        </li>
        <li>
          <label>
            Warehouse
          </label>
            <s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">
              <c:forEach items="${whList}" var="wh">
                <s:option value="${wh.id}">${wh.identifier}</s:option>
              </c:forEach>
            </s:select>
        </li>
        <li>
            <s:submit name="generateCourierReport" value="Download Courier Excel" class="dateFieldValidator" style="width:400px">
              Download Courier Excel
            <s:param name="courierDownloadFunctionality" value="true"/>
            </s:submit>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>
<script type="text/javascript">
   $('.dateFieldValidator').click(function() {
    //alert("in validator");
    var startDate = $('.startDate').val();
    var endDate = $('.endDate').val();
    if (startDate == "yyyy-mm-dd hh:mm:ss" || endDate == "yyyy-mm-dd hh:mm:ss") {
      alert("Please enter all fields .");
      return false;
    }
  });
  </script>

