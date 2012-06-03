<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.accounting.AccountingInvoicePdfAction" var="accInvoicePDFbean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Finance">

<s:layout-component name="htmlHead">

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

</s:layout-component>

<s:layout-component name="content">
<div class="accountingInvoicePDFBox">
  <s:form beanclass="com.hk.web.action.core.accounting.AccountingInvoicePdfAction">
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
          <s:submit name="downloadAccountingInvoicePDF" value="Download Accounting Invoice PDF" class="dateFieldValidator"/>

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

