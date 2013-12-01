<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign" var="emailBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Select Audience</s:layout-component>
  <s:layout-component name="content">
    <h3>Campaign selected: ${emailBean.emailCampaign.name}</h3>

    <h2>Step 2: Select Audience to send to</h2>
    <p>
    <s:form beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign">
      Sender Email: <s:text name="senderEmail" style="width:200px" value="${emailBean.senderEmail}"/>
      Sender Name: <s:text name="senderName" style="width: 100px" value="${emailBean.senderName}"/>
      Reply to email: <s:text name="replyToEmail" style="width: 100px" value="${emailBean.replyToEmail}"/>
      Send headers: <s:checkbox name="sendHeaders" value="${emailBean.sendHeaders}"/>
      <br/><br/>
      Enter comma separated emails/categories<br/><br/>

      Test User emails (note: these must be valid users in healthkart): <s:text name="testEmails"/><br/>
      <s:submit name="testCampaign" value="Send test emails"/>

      <br/><br/><br/>
      Categories: <s:text name="categories"/><br/>
      <s:submit name="confirmCampaign" value="confirm campaign"/> (to send to all users use the keyword
      <strong>ALL</strong>, for all unverified use <strong>ALL-UNVERIFIED</strong>)

      <br/><br/><br/>

      <h2>File to Upload
        <s:file name="fileBeanForUserList" size="30"/></h2>
      <s:submit name="sendCampaignViaCsvUserIDs" value="Send email to user id list"/>

      <br/><br/><br/>

      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>
      <s:submit name="sendCampaignViaCsvUserEmails" value="Send to a List of email ids (txt file)"/>

      <br/><br/><br/>

      <h2>File to Upload
        <s:file name="fileBeanForCustomExcel" size="30"/></h2>
      <br/>
      Sheet Name* <s:text name="sheetName" id="sheetName"/>
      <br/>
        <br/>
        MailGun Campaign Id <s:text name="mailGunCampaignId" id="mailGunCampaignId"/>
        <br/>
      <s:submit name="sendEmailViaExcel" value="Send emails (using excel file)"
                id="sendEmailViaExcelSubmit"/>(Note:
      <strong>email_id</strong> header is mandatory. Also for products and variants,headers need to be <strong>product_id</strong>,
        <strong>product_variant_id</strong>, <strong>similar_product(optional)</strong>)

      <s:hidden name="emailCampaign"/>
    </s:form>
    </p>
  </s:layout-component>
</s:layout-render>
<script type="text/javascript">
  $(document).ready(function() {
    $('#sendEmailViaExcelSubmit').click(function() {
      if ($('#sheetName').val().trim() === "") {
        alert("Please enter the sheet name!");
        return false;
      } else {
        return true;
      }
    });
  });
</script>
