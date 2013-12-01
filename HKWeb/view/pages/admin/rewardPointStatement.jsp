<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.RewardPointStatementAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reward Point Statement">
  <s:layout-component name="heading">${rpBean.currentBreadcrumb.name}</s:layout-component>

  <s:layout-component name="content">
    <table width="100%" class="cont">
      <thead>
      <tr>
        <th>S.no.</th>
        <th>Name</th>
        <th>Order date</th>
        <th>Points</th>
        <th>Status</th>
        <th>Comment</th>
      </tr>
      </thead>
      <c:forEach items="${rpBean.user.rewardPointList}" var="rewardPoint" varStatus="ctr">
        <tr>
          <td>${ctr.count}</td>
          <td>${rewardPoint.referredUser != null ? rewardPoint.referredUser.name : 'NA'}</td>
          <td><fmt:formatDate value="${rewardPoint.createDate}" type="both"/></td>
          <td>${rewardPoint.value}</td>
          <td>${rewardPoint.rewardPointStatus.name}</td>
          <td>${rewardPoint.comment}</td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>

</s:layout-render>
