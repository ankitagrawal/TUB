<%@ page import="com.hk.pact.dao.MasterDataDao" %>
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
      <s:form beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign">
        <div style="margin-top:15px;"></div>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${emailBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${emailBean}"/>

        <c:if test="${!empty emailBean.emailCampaigns}">
          <table>
            <thead>
            <tr>
              <th>Id</th>
              <th>name</th>
              <th>template</th>
              <th>create date</th>
              <th>sent count</th>
            </tr>
            </thead>
            <c:forEach items="${emailBean.emailCampaigns}" var="emailCampaign">
              <tr>
                <td>
                  <s:radio value="${emailCampaign}" name="emailCampaign"/>${emailCampaign.id}
                </td>
                <td>${emailCampaign.name}</td>
                <td>${emailCampaign.template}</td>
                <td>${emailCampaign.createDate}</td>
                <td>
                  <div id="sentCount">check sent count</div>
                </td>
                <%--<td>${emailCampaign.sentCount}</td>--%>
              </tr>
            </c:forEach>
          </table>
          <s:submit name="selectCampaign"/>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${emailBean}"/>
          <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${emailBean}"/>
        </c:if>
      </s:form>
    </fieldset>
  </s:layout-component>
  <script type="text/javascript">
    $(document).ready(function(){
      $('#sentCount').click(function(){
        $('#sentCount').load();
      });
    });
  </script>
</s:layout-render>
