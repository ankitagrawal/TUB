<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction" var="createBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Create Email Newsletter</s:layout-component>
  <s:layout-component name="content">
    <c:choose>
      <c:when test="${createBean.ftlGenerated == false}">
        <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
          <fieldset style="width:50%;">
            <legend>GENERATE FTL</legend>
            <div>
              <div style="float:left; width: 60%; margin-top: 10px; margin-bottom: 10px;">
                Name: <s:text name="name"/><br/>
                Content: <s:file name="contentBean" style=" margin-top: 15px;"/><br/>
              </div>
              <div style="float:right; width:40%;">
                <s:submit name="generateFtlAndUploadData" value="GENERATE FTL" style="font-size: 0.9em;"/>
              </div>
            </div>
          </fieldset>
        </s:form>
      </c:when>
      <c:otherwise>
        <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
          <fieldset style="width:50%">
            Name: <s:text name="emailCampaign.name" value="${createBean.name}"/><br/>
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
            <s:textarea name="ftlContents" style="width:95%; margin:10px; resize:none;"/>
          </fieldset>

          <%--<s:hidden name="name" value="${createBean.name}"/>--%>
          <s:hidden name="contentFolderName" value="${createBean.contentFolderName}"/>
          <%--<s:hidden name="emailCampaign"/>--%>
          <s:submit name="create" value="CREATE CAMPAIGN"/>
        </s:form>
      </c:otherwise>
    </c:choose>

    <%--<s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">--%>
    <%--Name: <s:text name="emailCampaign.name"/><br/>--%>
    <%--Subject: <s:text name="emailCampaign.subject"/><br/>--%>
    <%--Content: <s:file name="contentBean"/><br/>--%>
    <%--Email Type:--%>
    <%--<s:select name="emailCampaign.emailType">--%>
    <%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="emailTypeList"--%>
    <%--value="id" label="name"/>--%>
    <%--</s:select>--%>
    <%--<br/>--%>
    <%--Minimum Day gap : <s:text name="emailCampaign.minDayGap" value="5"/><br/>--%>
    <%--<s:submit name="create"/>--%>
    <%--<s:submit name="generateFtlAndUploadData" value="GENERATE FTL"/>--%>
    <%--</s:form>--%>

    <%--<c:if test="${createBean.emailCampaign != null}">--%>
    <%--<s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">--%>
    <%--<div>--%>
    <%--<h2>DETAILS FOR CAMPAIGN : ${createBean.emailCampaign.name}</h2>--%>

    <%--<div style="float:left; width:50%">--%>
    <%--<h4>.ftl generated: </h4><br/>--%>
    <%--<s:textarea name="emailCampaign.templateFtl"/>--%>
    <%--</div>--%>
    <%--<div style="float:right; width:50%">--%>
    <%--<h4> .html uplaoded to third party</h4>--%>
    <%--<s:textarea name="htmlFile"/>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<s:submit name="create" value="SAVE FTL AND HTML FOR CAMPAIGN"/>--%>
    <%--</s:form>--%>
    <%--</c:if>--%>
  </s:layout-component>
</s:layout-render>
