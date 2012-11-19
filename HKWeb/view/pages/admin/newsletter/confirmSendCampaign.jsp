<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign" var="emailBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Confirm campaign send</s:layout-component>
  <s:layout-component name="content">
    <h2>Please verify your campaign before sending : </h2>
    Your campaign : ${emailBean.emailCampaign.name}<br/>
    No. of users : ${emailBean.userCount}<br/>
    <s:form beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign">
      <s:hidden name="emailCampaign"/>
      <s:hidden name="categories"/>
      <s:hidden name="testEmails"/>
      <s:hidden name="senderEmail"/>
      <s:hidden name="senderName"/>
      <s:submit name="sendCampaign" value="Send Campaign"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
