<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Newsletter Home</s:layout-component>
  <s:layout-component name="content">
    <s:link beanclass="web.action.admin.newsletter.CreateEmailNewsletterCampaign">Create New Email Campaign</s:link><br/>
    <s:link beanclass="web.action.admin.newsletter.SendEmailNewsletterCampaign">Send Email Campaign</s:link><br/>
    <s:link beanclass="web.action.admin.marketing.SendWeMissYouEmailer">Send We Miss You Emailer</s:link>
  </s:layout-component>
</s:layout-render>
