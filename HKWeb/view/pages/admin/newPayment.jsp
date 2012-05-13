<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.payment.NewPaymentAction" var="paymentBean"/>

    <s:form beanclass="com.hk.web.action.admin.payment.NewPaymentAction">
      <s:hidden name="order"/>
      <fieldset>
        <label>Order Id</label> ${paymentBean.order}<br/>
        <label>Amount</label> <s:text name="amount"/><br/>
        <label>Payment Mode</label>
        <s:select name="paymentMode">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentModeList" value="id" label="name"/>
        </s:select><br/>
        <label>Payment Status</label>
        <s:select name="paymentStatus">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentStatusList" value="id" label="name"/>
        </s:select><br/>

        <s:submit name="update"/>
      </fieldset>
    </s:form>

  </s:layout-component>

</s:layout-render>
