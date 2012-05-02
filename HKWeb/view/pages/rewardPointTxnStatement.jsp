<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/default.jsp" pageTitle="Reward Point Txn Statement">
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#referralTerms').jqm({trigger: '.referralTerms', ajax: '@href'});
      });
    </script>
    <style type="text/css">
      .instr {
        font-size: 1.2em;
      }

      .instr li {
        border-bottom: 1px solid #f1f1f1;

        margin-bottom: 10px;
      }
    </style>


    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>

  </s:layout-component>
  <s:layout-component name="heading">Reward Point Txn Statement</s:layout-component>

  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <s:layout-component name="modal">
      <div class="jqmWindow" id="referralTerms"></div>
    </s:layout-component>

    <div style="font-size: 12px; line-height: 18px;">
      <strong>Redeemable Points:</strong><fmt:formatNumber value="${rpBean.redeemablePoint}" pattern="<%=FormatUtils.currencyFormatPattern%>"/><br/>

      <strong>Overused Reward Points: </strong><fmt:formatNumber value="${rpBean.user.userAccountInfo.overusedRewardPoints}" pattern="<%=FormatUtils.currencyFormatPattern%>"/><br/>

      <span class="orange"><strong>Balance Reward
        Points: </strong><fmt:formatNumber value="${rpBean.redeemablePoint - rpBean.user.userAccountInfo.overusedRewardPoints}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
    </div>
    <table class="cont">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>Value</th>
        <th>Txn Date</th>
        <th>Expiry Date</th>
        <th>Txn Type</th>
        <th>Expired</th>
      </tr>
      </thead>
      <c:forEach items="${rpBean.user.rewardPointTxnList}" var="rewardPointTxn" varStatus="ctr">
        <tr>
          <td>${ctr.count}</td>
          <td>${rewardPointTxn.value}</td>
          <td><fmt:formatDate value="${rewardPointTxn.txnDate}" type="both"/></td>
          <td><fmt:formatDate value="${rewardPointTxn.expiryDate}" type="both"/></td>
          <td>${rewardPointTxn.rewardPointTxnType.name}</td>
          <td>${rewardPointTxn.expired ? 'Expired':''}</td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('rpsLink').style.fontWeight = "bold";
  };
</script>