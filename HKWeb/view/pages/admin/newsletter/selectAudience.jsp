<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.admin.newsletter.SendEmailNewsletterCampaign" var="emailBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Select Audience</s:layout-component>
  <s:layout-component name="content">
    <h3>Campaign selected: ${emailBean.emailCampaign.name}</h3>
    <h2>Step 2: Select Audience to send to</h2>
    <p>
      <s:form beanclass="web.action.admin.newsletter.SendEmailNewsletterCampaign">
        Enter comma separated emails/categories<br/><br/>

        Test User emails (note: these must be valid users in healthkart): <s:text name="testEmails"/><br/>
        <s:submit name="testCampaign" value="Send test emails"/>

        <br/><br/><br/>
        Categories: <s:text name="categories"/><br/>
        <s:submit name="confirmCampaign" value="confirm campaign"/> (to send to all users use the keyword <strong>ALL</strong>, for all unverified use <strong>ALL-UNVERIFIED</strong>)

          <br/><br/><br/>
          <h2>File to Upload
            <s:file name="fileBeanForUserList" size="30"/></h2>
          <s:submit name="sendCampaignViaCsvUserIDs" value="Send email to user id list"/>

          <br/><br/><br/>
          <h2>File to Upload
            <s:file name="fileBean" size="30"/></h2>
          <s:submit name="sendCampaignViaCsvUserEmails" value="Send to a List of email ids (txt file)"/>

        <s:hidden name="emailCampaign"/>
      </s:form>
    </p>
  </s:layout-component>
</s:layout-render>
