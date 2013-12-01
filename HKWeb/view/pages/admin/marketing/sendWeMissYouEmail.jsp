<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.SendWeMissYouEmailer" var="emailBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Send Missing Users Email Campaign</s:layout-component>
  <s:layout-component name="content">
    <p>
      <s:form beanclass="com.hk.web.action.admin.marketing.SendWeMissYouEmailer">
        <h2>Select campaign, Enter Coupon Code to be mailed, and the no of days the user has been missing</h2><br/>
        Coupon Code <s:text name="couponCode"/><br/>
        No Of Days <s:text name="noOfDays"/>
        <table>
          <br/><br/>Existing Campaigns <br/>
          <thead>
          <tr>
            <th>name</th>
            <th>template</th>
            <th>create date</th>
          </tr>
          </thead>
          <c:forEach items="${emailBean.emailCampaigns}" var="emailCampaign">
            <tr>
              <td>${emailCampaign.name}</td>
              <td>${emailCampaign.template}</td>
              <td>${emailCampaign.createDate}</td>
            </tr>
          </c:forEach>
        </table>
        <s:submit name="confirmCampaign" value="Confirm Campaign"/>
      </s:form>
    </p>
  </s:layout-component>
</s:layout-render>
