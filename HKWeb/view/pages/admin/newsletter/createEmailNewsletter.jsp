<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Create Email Newsletter</s:layout-component>
  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
      Name: <s:text name="emailCampaign.name"/><br/>
      Template: <s:text name="emailCampaign.template"/><br/>
      Email Type:
      <s:select name="emailCampaign.emailType">
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="emailTypeList"
                                   value="id" label="name"/>
      </s:select>
      <br/>
      Minimum Day gap : <s:text name="emailCampaign.minDayGap" value="5"/><br/>
      <s:submit name="create"/>
    </s:form>

  </s:layout-component>
</s:layout-render>
