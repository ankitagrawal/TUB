<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction" var="createBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Create Email Newsletter</s:layout-component>
  <s:layout-component name="content">
    <div style="color:red; margin-top:5px; margin-bottom:5px;font-size:0.7em;" id="error"></div>
    <c:choose>
      <c:when test="${createBean.ftlGenerated == false}">
        <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
          <fieldset style="width:50%;">
            <legend>GENERATE FTL</legend>
            <div>
              <div style="float:left; width: 60%; margin-top: 10px; margin-bottom: 10px;">
                Name: <s:text name="name" id="name"/><br/>
                Content: <s:file name="contentBean" style=" margin-top: 15px;"/><br/>
              </div>
              <div style="float:right; width:40%;">
                <s:submit name="generateFtlAndUploadData" value="GENERATE FTL" style="font-size: 0.9em;"
                          id="generateFtlBtn"/>
              </div>
            </div>
          </fieldset>
        </s:form>
      </c:when>
      <c:otherwise>
        <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">
          <fieldset style="width:50%">
            Name: <s:text name="emailCampaign.name" value="${createBean.name}" readonly="readonly"/><br/>
            Subject: <s:text name="emailCampaign.subject" style=" margin-top: 15px;" id="subject"/><br/>
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
            <s:textarea name="ftlContents" style="width:95%; margin:10px;"/>
          </fieldset>

          <%--<s:hidden name="contentFolderName" value="${createBean.contentFolderName}"/>--%>
          <s:hidden name="htmlPath" value="${createBean.htmlPath}"/>
          <s:submit name="create" value="CREATE CAMPAIGN" id="saveBtn"/>
        </s:form>
      </c:otherwise>
    </c:choose>
    <s:link beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterAdmin">
      <div style="font-weight:bold; font-size:1.2em">BACK</div>
    </s:link>
    <script type="text/javascript">
      $(document).ready(function() {
        $('#generateFtlBtn').click(function() {
          var name = $('#name').val();
          var validNameRegex = /^[A-Za-z0-9_]+$/;
          if (!validNameRegex.test(name)) {
            $('#error').html("Invalid campaign name: " + name + " . Only Alphanumeric characters and underscore is allowed!");
            return false;
          } else {
            return true;
          }
        });
        $('#saveBtn').click(function() {
          var subject = $('#subject').val().trim();
          if (subject === "") {
            $('#error').html("Kindly mention the subject for the campaign!");
            return false;
          } else {
            return true;
          }
        });
      });
    </script>
  </s:layout-component>
</s:layout-render>
