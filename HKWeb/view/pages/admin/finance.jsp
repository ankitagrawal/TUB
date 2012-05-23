<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.OrderStatusDao" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

<s:layout-component name="htmlHead">
  <%--

    <%
      OrderStatusDao orderStatusDao = ServiceLocatorFactory.getService(OrderStatusDao.class);
      pageContext.setAttribute("orderStatusListForReporting", orderStatusDao.listOrderStatusForReporting());

      TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
      pageContext.setAttribute("taxList", taxDao.taxListForReport());

      CategoryDao categoryDao = (CategoryDao)ServiceLocatorFactory.getService(CategoryDao.class);
      pageContext.setAttribute("primaryCategories",categoryDao.getPrimaryCategories());

    %>
  --%>

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      $('#markAll').click(function() {
        if ($(this).attr("checked") == "checked") {
          $('.lineItemCheckBox').each(function() {
            this.checked = true;
          })
        } else {
          $('.lineItemCheckBox').each(function() {
            this.checked = false;
          })
        }
      });
    });
  </script>

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
<div class="accountingInvoicePDFBox">
  <s:form beanclass="com.hk.web.action.core.accounting.AccountingInvoicePdfAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Download Accounting Invoice PDFs</legend>
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
          <s:submit name="downloadAccountingInvoicePDF" value="Download Accounting Invoice PDF"/>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>
  
</s:layout-component>
</s:layout-render>
