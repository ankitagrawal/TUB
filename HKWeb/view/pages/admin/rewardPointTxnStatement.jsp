<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reward Point Txn Statement">


  <s:layout-component name="content">
    <h3>Redeemable Points: ${rpBean.redeemablePoint} </h3>

    <h3>Overused Reward Points: ${rpBean.user.userAccountInfo.overusedRewardPoints} </h3>

    <h3>Balance Reward Points: ${rpBean.redeemablePoint - rpBean.user.userAccountInfo.overusedRewardPoints} </h3>

    <table class="cont">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>Value</th>
        <th>Txn Date</th>
        <th>Expiry Date</th>
        <th>Txn Type</th>
        <th>Mode</th>
        <th>Reward Point Id</th>
        <th>Redeemed Order Id</th>
        <th>Expired</th>
        <th>Comments</th>
        <th>User</th>
      </tr>
      </thead>
      <c:forEach items="${rpBean.user.rewardPointTxnList}" var="rewardPointTxn" varStatus="ctr">
        <tr>
          <td>${ctr.count}</td>
          <td>${rewardPointTxn.value}</td>
          <td><fmt:formatDate value="${rewardPointTxn.txnDate}" type="both"/></td>
          <td><fmt:formatDate value="${rewardPointTxn.expiryDate}" type="both"/></td>
          <td>${rewardPointTxn.rewardPointTxnType.name}</td>
          <td>${rewardPointTxn.rewardPoint.rewardPointMode.name}</td>
          <td align="center">${rewardPointTxn.rewardPoint.id}</td>
          <td align="center">
            <c:if test="${rewardPointTxn.redeemedOrder != null}">
              <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders">
                ${rewardPointTxn.redeemedOrder.id}
                <s:param name="orderId" value="${rewardPointTxn.redeemedOrder.id}"/>
              </s:link>
            </c:if>
          </td>
          <td>${rewardPointTxn.expired ? 'Expired':''}</td>
          <td>${rewardPointTxn.rewardPoint.comment}</td>
          <td>${rewardPointTxn.rewardPoint.referredUser.name}</td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>

</s:layout-render>
