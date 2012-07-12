<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction" var="editBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Edit Amazon S3 Email Newsletter</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
      <fieldset style="width:50%">
        Name: <s:text name="emailCampaign.name" readonly="readonly" style="border:0;"/><br/>
        Subject: <s:text name="emailCampaign.subject" style=" margin-top: 15px;"/><br/>
        Email Type:
        <s:select name="emailCampaign.emailType" style=" margin-top: 15px;">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="emailTypeList"
                                     value="id" label="name"/>
        </s:select>
        <br/>
        Minimum Day gap : <s:text name="emailCampaign.minDayGap" value="5" style=" margin-top: 15px;"/><br/>
      </fieldset>
      <fieldset style=" margin-top: 15px; text-align:center;">
        <legend>FTL GENERATED</legend>
        <s:textarea name="emailCampaign.templateFtl" style="width:95%; margin:10px;"/>
      </fieldset>
      <fieldset style=" margin-top: 15px; text-align:center;">
        <legend>HTML GENERATED</legend>
        <s:textarea name="htmlContents" style="width:95%; margin:10px;"/>
      </fieldset>
      <s:hidden name="emailCampaign"/>
      <s:submit name="saveEmailCampaign" value="SAVE CHANGES"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
