<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.facebook.app.coupon.FanCouponAdminAction" var="adminBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Fan Coupon Admin</s:layout-component>
  <s:layout-component name="content">
    <h2>Existing Campaigns</h2>

    <ul>
      <c:forEach items="${adminBean.campaignList}" var="campaign">
        <li>${campaign.name} - ${campaign.code}</li>
      </c:forEach>
      <c:if test="${empty adminBean.campaignList}">
        <li>No Campaigns found.</li>
      </c:if>
    </ul>

    <p>
      <s:link beanclass="com.hk.web.action.facebook.app.coupon.FanCouponNewCampaignAction">+ Create a new campaign</s:link>
    </p>
    
  </s:layout-component>
</s:layout-render>
