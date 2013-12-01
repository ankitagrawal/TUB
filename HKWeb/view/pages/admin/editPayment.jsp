<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.payment.EditPaymentAction" var="paymentBean"/>

    <s:form beanclass="com.hk.web.action.admin.payment.EditPaymentAction">
      <s:hidden name="payment.id"/>
      <fieldset>
        <label>Order Id</label> ${paymentBean.payment.order.id}<br/>
        <label>Amount</label> <s:text name="payment.amount"/><br/>
        <label>Payment Status</label>
        <s:select name="payment.paymentStatus">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentStatusList" value="id" label="name"/>
        </s:select><br/>
        <label>Payment Date</label><s:text name="payment.paymentDate" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
        <br/>
        <s:submit name="update"/>
      </fieldset>
    </s:form>

  </s:layout-component>

</s:layout-render>
