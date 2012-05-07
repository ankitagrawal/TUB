<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="mhc.service.dao.order.OrderStatusDao" %>
<%@ page import="mhc.service.dao.TaxDao" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

<s:layout-component name="htmlHead">
  <%--

    <%
      OrderStatusDao orderStatusDao = InjectorFactory.getInjector().getInstance(OrderStatusDao.class);
      pageContext.setAttribute("orderStatusListForReporting", orderStatusDao.listOrderStatusForReporting());

      TaxDao taxDao = InjectorFactory.getInjector().getInstance(TaxDao.class);
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

<s:layout-component name="heading">Report Master : Orders# ${reportActionBean.totalOrders}
  &nbsp;|&nbsp; MRP:<fmt:formatNumber value="${reportActionBean.sumOfMrp}" type="currency" currencySymbol=" "
                                      maxFractionDigits="0"/>
  &nbsp;|&nbsp; HKPrice:<fmt:formatNumber value="${reportActionBean.sumOfHkPrice}" type="currency" currencySymbol=" "
                                          maxFractionDigits="0"/>
</s:layout-component>

<s:layout-component name="content">
<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Sales Reports</legend>
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
          <s:submit name="generateSalesByDateReport" value="Generate Sales-by-date Report"/>
            <%--
                      <s:submit name="generateDetailedSalesDailyReport" value="Generate Detailed Sales Report"/>
                      <s:submit name="generateAccountingSalesExcel" value="Generate Accounting Sales Excel"/>
                      <s:submit name="generateAccountingSalesExcelForBusy" value="Generate Sales Excel for Busy"/>
                      <s:submit name="generateSalesByDateReportForShippedProducts"
                                value="Generate Sales Report by Ship Date"/>
            --%>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Product Variant Sales Report</legend>
      <ul>
        <li>
          <label>Start date</label>
          <s:text class="date_input startDate" style="width:150px"
                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
        </li>

        <li>
          <label>End date</label>
          <s:text class="date_input endDate" style="width:150px"
                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
        </li>

        <li>
          <label>Base Order Status</label>
          <s:select name="orderStatus">
            <option value="">Any order status</option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="orderStatusList"
                                       value="id" label="name"/>
          </s:select>
        </li>

        <li>
          <label>Top Level Category</label>
          <s:select name="topLevelCategory">
            <option value="">All categories</option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                       value="name" label="displayName"/>
          </s:select>
        </li>

        <li>
            <%--<s:submit name="generateSKUSalesExcel" value="Generate Excel"/>--%>
          <s:submit name="generateOrderReportExcel" value="Generate Report"/>
        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Performance Reports</legend>
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
          <s:submit name="generateMasterPerformanceReport" value="Generate Master Performance Report"/>
          <%--&nbsp;<s:submit name="generateShipmentPerformaceReport" value="Generate Shipment Performace Report"/>--%>
         <%-- &nbsp;<s:submit name="generateCODConfirmationReport" value="Generate CoD Confirmation Report"/>--%>

       </li>
      </ul>
    </fieldset>
  </s:form>
</div>

<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>CRM Report</legend>
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

        <li><label>Top Level Category</label><s:select name="topLevelCategory">
          <option value="">All categories</option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                     value="name" label="displayName"/>
        </s:select></li>

        <li><s:submit name="generateCRMReport" value="Generate CRM Report"/>
        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

<%--<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Categories Performance Report</legend>
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
        <table>
          <tr>
            <td colspan="2">Top Level Categories</td>
            <td colspan="2"><input type="checkbox" id="markAll">Mark All</td>
          </tr>
          <tr>
            <c:forEach items="${primaryCategories}" var="category" varStatus="pcCtr">
              <td><s:checkbox name="categoryList[${pcCtr.index}]" value="${category.name}"
                              class="lineItemCheckBox"/><br/>${category.displayName}</td>
            </c:forEach>
          </tr>
          <tr>
          <td><s:checkbox name="categoryList[0]" value="baby" class="lineItemCheckBox"/> Baby</td>
          <td><s:checkbox name="categoryList[1]" value="beauty" class="lineItemCheckBox"/> Beauty</td>
          <td><s:checkbox name="categoryList[2]" value="diabetes" class="lineItemCheckBox"/> Diabetes</td>
        </tr>
          <tr>
            <td><s:checkbox name="categoryList[3]" value="eye" class="lineItemCheckBox"/> Eye</td>
            <td><s:checkbox name="categoryList[4]" value="home-devices" class="lineItemCheckBox"/> Home Devices</td>
            <td><s:checkbox name="categoryList[5]" value="nutrition" class="lineItemCheckBox"/> Nutrition</td>
          </tr>
          <tr>
            <td><s:checkbox name="categoryList[6]" value="personal-care" class="lineItemCheckBox"/> Personal Care</td>
            <td><s:checkbox name="categoryList[7]" value="services" class="lineItemCheckBox"/> Services</td>
            <td><s:checkbox name="categoryList[8]" value="sports" class="lineItemCheckBox"/> Sports</td>
          </tr>
        </table>


          <s:submit name="generateCategoryPerformanceReportUI" value="Generate"/>
          <s:submit name="generateDailyCategoryPerformaceReportUI" value="Daily Report"/>
          <s:submit name="generateSixHourlyCategoryPerformaceReportUI" value="Six Hourly Report"/>
    </fieldset>
  </s:form>
</div>--%>

<%--<div class="reportBox">--%>
<%--<s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">--%>
<%--<fieldset class="right_label">--%>
<%--<legend>Reconciliation Report</legend>--%>
<%--<ul>--%>
<%--<li>--%>
<%--<label>Start--%>
<%--date</label><s:text class="date_input startDate" style="width:150px"--%>
<%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>--%>
<%--</li>--%>
<%--<li>--%>
<%--<label>End--%>
<%--date</label><s:text class="date_input endDate" style="width:150px"--%>
<%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>--%>
<%--</li>--%>

<%--<li>--%>
<%--<label>Order Status</label>--%>
<%--<s:select name="orderStatus">--%>
<%--<s:option value="">All Orders</s:option>--%>
<%--<hk:master-data-collection service="<%=MasterDataDao.class%>"--%>
<%--serviceProperty="orderListForReconciliationReport" value="id"--%>
<%--label="name"/>--%>
<%--</s:select>--%>
<%--</li>--%>
<%--<li>--%>

<%--<label>Payment Mode</label>--%>
<%--<s:select name="paymentMode" class="paymentModeSelect">--%>
<%--<s:option value="">All Payment Modes</s:option>--%>
<%--<hk:master-data-collection service="<%=MasterDataDao.class%>"--%>
<%--serviceProperty="paymentModesForReconciliationReport" value="id"--%>
<%--label="name"/>--%>
<%--</s:select>--%>
<%--</li>--%>
<%--<li>--%>
<%--<label>Courier</label>--%>
<%--<s:select name="codMode" class="codModeSelect" disabled="true">--%>
<%--<s:option value="">All Couriers</s:option>--%>
<%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"--%>
<%--value="id" label="name"/>--%>
<%--</s:select>--%>
<%--</li>--%>
<%--<li>--%>
<%--<label>Reconciliation Status</label>--%>
<%--<s:select name="reconciliationStatus">--%>
<%--<s:option value="">All Reconciled Status</s:option>--%>
<%--<hk:master-data-collection service="<%=MasterDataDao.class%>"--%>
<%--serviceProperty="reconciliationStatus" value="id" label="name"/>--%>
<%--</s:select>--%>
<%--</li>--%>
<%--<li>--%>
<%--<s:submit name="generateReconciliationReport" value="Generate"/>--%>
<%--<s:link beanclass="com.hk.web.action.admin.ReconciliationAction"--%>
<%--target="_blank">Update Reconciliation Status</s:link>--%>
<%--</li>--%>
<%--</ul>--%>

<%--</fieldset>--%>
<%--</s:form>--%>


<%--</div>--%>

<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Courier Delivery Report</legend>
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

        <li><label>Courier Name</label><s:select name="courier">
          &lt;%&ndash;<option value="">All Couriers</option>&ndash;%&gt;
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
                                     value="id" label="name"/>
        </s:select></li>

        <li><s:submit name="generateCourierReport" value="Generate Courier Report"/>
        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

<%--<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <s:errors/>
    <fieldset class="right_label">
      <legend>Sales Reports By Ship Date</legend>
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
      </ul>
      <table>
        <tr>
          <td> Tax Category<br/>

            <div class="checkBoxList">
              <c:forEach items="${taxList}" var="tax" varStatus="ctr">

                <label><s:checkbox name="taxes[${ctr.index}]"
                                   value="${tax.id}" checked="yes"/> ${tax.name} </label> <br/>
              </c:forEach>
            </div>
          </td>
          <td> Order Status<br/>

            <div class="checkBoxList">
              <c:forEach items="${orderStatusListForReporting}" var="orderStatus" varStatus="ctr">

                <label><s:checkbox name="orderStatuses[${ctr.index}]"
                                   value="${orderStatus.id}"
                                   checked="yes"/> ${orderStatus.name}</label><br/>
              </c:forEach>
            </div>
          </td>
          <td> Region<br/>
            <label><s:checkbox name="inRegion"
                               value="yes"/> haryana </label>
            <br/>
            <label><s:checkbox name="inRegion"
                               value="no"/> non haryana </label>
            <br/><br/><br/>
          </td>
        </tr>
      </table>
      <s:submit name="generateSaleForProductsByTaxAndStatusInRegion" value="Sales Report by Tax & Ship Date"/>
    </fieldset>
  </s:form>
</div>--%>
<%--<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <fieldset class="right_label">
      <legend>Net Margin Report Per Product Variant</legend>
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
          <s:submit name="generateNetMarginReportPerProductVariant" value="Generate Report"/>
    </fieldset>
  </s:form>
</div>--%>
<div class="reportBox">
  <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
    <fieldset class="right_label">
      <legend>Sales Report By Product</legend>
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
          <label>
            Product Ids
          </label><s:text name="productIdListCommaSeparated" style="width:200px"/>
        </li>
        <li>
          (Enter Ids separated by comma)
        </li>
      </ul>
      <s:submit name="generateInventorySoldByDate" value="Generate Report"/>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>
