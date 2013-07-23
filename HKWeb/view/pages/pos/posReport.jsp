<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.pos.POSReportAction" var="pos"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

  <s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  </s:layout-component>

  <s:layout-component name="heading">POS Report Master
  </s:layout-component>

  <s:layout-component name="content">
    <div class="reportBox">
      <s:form beanclass="com.hk.web.action.admin.pos.POSReportAction" target="_blank">

        <fieldset class="right_label">
          <legend>Sales Reports</legend>
          <ul>
            <li>
              <s:submit name="generateDailySalesReport" value="Generate Daily Report"/>
            </li>
          </ul>

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
              <s:submit name="generateSalesReportByDate" value="Generate Sales-by Date Report"/>
            </li>
          </ul>


        </fieldset>
      </s:form>
    </div>

    <div class="reportBox">
      <s:form beanclass="com.hk.web.action.admin.pos.POSReportAction" target="_blank">


        <fieldset class="right_label">
          <legend>Return Reports</legend>

          <ul>
            <li>
              <s:submit name="generateDailyReturnReport" value="Generate Daily Report"/>
            </li>
          </ul>

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
              <s:submit name="generateReturnReportByDate" value="Generate Sales-by Date Report"/>

            </li>
          </ul>

        </fieldset>
      </s:form>
    </div>


  </s:layout-component>
</s:layout-render>
