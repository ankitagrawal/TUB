<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.domain.email.EmailCampaign" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign" var="emailBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Send Email Newsletter Campaign</s:layout-component>
  <s:layout-component name="content">
    <fieldset>
      <legend>Select an Email Type</legend>
      <s:form beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign">
        Email Type:
        <s:select name="emailType">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="emailTypeList"
                                     value="id" label="name"/>
        </s:select>
        <s:submit name="pre" value="Filter"/>
      </s:form>
    </fieldset>

    <div class="clear"></div>
    <div style="margin-top:15px;"></div>

    <fieldset>
      <legend>Step 1: Select a campaign</legend>
      <s:form beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign" id="sentCountForm">
        <div style="margin-top:15px;"></div>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${emailBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${emailBean}"/>

        <c:if test="${!empty emailBean.emailCampaigns}">
          <table>
            <thead>
            <tr>
              <th></th>
              <th>Id</th>
              <th>Name</th>
              <th>Template</th>
              <th>Min Day Gap</th>
              <th>Create Date</th>
              <th>Sent Count</th>
              <th></th>
            </tr>
            </thead>
            <c:forEach items="${emailBean.emailCampaigns}" var="emailCampaign">
              <tr class="emailCampaignRow">
                <c:set var="emailCampaignTemplate" value="${emailCampaign.template}"/>

                <td><s:radio value="${emailCampaign}" name="emailCampaign"/></td>
                <td class="emailCampaignId">${emailCampaign.id}</td>
                <td>${emailCampaign.name}</td>
                <td>${emailCampaign.template}</td>
                <td>${emailCampaign.minDayGap}</td>
                <td>${emailCampaign.createDate}</td>
                <td>
                  <s:link beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign"
                          event="getSentCountForEmailCampaign" class="sentCountButton" style="font-size:0.9em;">
                    <s:param name="emailCampaign" value="${emailCampaign.id}"/>
                    Check Sent Count
                  </s:link>
                  <div style="margin-top:5px;" class="sentCount"></div>
                </td>
                <td>
                  <c:if test="${emailCampaignTemplate == null}">
                    <s:link beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction"
                            event="editEmailCampaign" style="font-size:0.9em;">
                      <s:param name="emailCampaign" value="${emailCampaign.id}"/>
                      Edit Campaign
                    </s:link>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
          <s:submit name="selectCampaign"/>

          <div class="clear"></div>
          <div style="margin-top:15px; color: #fff;"></div>

          <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${emailBean}"/>
          <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${emailBean}"/>
        </c:if>
      </s:form>
    </fieldset>

    <s:link beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterAdmin">
      <div style="font-weight:bold; font-size:1.2em">BACK</div>
    </s:link>

    <script type="text/javascript">
      $(document).ready(function() {
        $('.sentCountButton').click(function() {
          $.getJSON($(this).attr('href'), _checkSentCount);
          return false;
        });

        function _checkSentCount(res) {
          var emailCampaignId;
          if (res.code == "<%=HealthkartResponse.STATUS_OK%>") {
            emailCampaignId = res.data.emailCampaignId;
            $('.emailCampaignId').each(function() {
              if ($(this).text() === emailCampaignId) {
                $(this).parents('.emailCampaignRow').find('.sentCount').html(res.data.sentCountValue);
              }
            });
          }
        }
      });
    </script>
  </s:layout-component>
</s:layout-render>

