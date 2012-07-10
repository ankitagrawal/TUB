<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>`
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" var="reportActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

<s:layout-component name="htmlHead">

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  <script type="text/javascript">
    $(document).ready(function() {
      $('.paymentModeSelect').change(function() {
        if ($('.paymentModeSelect').val() == '40') {
          $('.codModeSelect').removeAttr('disabled');
        } else {
          $('.codModeSelect').attr('disabled', 'disabled');
        }
      })
    });
  </script>

</s:layout-component>

<s:layout-component name="content">
<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" >
    <s:errors/>
    <fieldset class="right_label">
      <legend>Reconciliation Reports</legend>
      <ul>

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
          <label>Payment Mode:</label>
          <s:select name="paymentProcess">
            <s:option value="all">All</s:option>
            <s:option value="cod">COD</s:option>
            <s:option value="techprocess">TechProcess</s:option>
           </s:select>
        </li>

          <li>
          <label>Shipping Order Status</label>
          <s:select name="shippingOrderStatusId">
						<s:option value="">All</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="SOStatusForReconcilation" value="id"
						                           label="name"/>
					</s:select>
        </li>

        <li>
          <label>Warehouse</label>
          <s:select name="warehouseId">
            <s:option value="0">All</s:option>
            <s:option value="1">Gurgaon</s:option>
            <s:option value="2">Mumbai</s:option>
          </s:select>
        </li>
        <li>
          <label>Courier</label>
          <s:select name="courier" class="codModeSelect">
						<s:option value="">-All-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
						                           label="name"/>
					</s:select>
        </li>
        <li>
          <s:submit name="generateReconilationReport" value="Generate-Reconcilation-Report" />
        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>
