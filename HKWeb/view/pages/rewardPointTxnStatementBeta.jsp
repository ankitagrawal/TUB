<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Reward Point Txn Statement">
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
  </s:layout-component>
  <s:layout-component name="heading">Reward Point Txn Statement</s:layout-component>

  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <s:layout-component name="modal">
      <div class="jqmWindow" id="referralTerms"></div>
    </s:layout-component>
    <div class="mrgn-l-40 my-acnt-ht">
        <table class="cont footer_color" style="font-size: 14px;" id="summary">
      <tr>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5">Redeemable Points:</td>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5">
          <fmt:formatNumber value="${rpBean.redeemablePoint}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
        </td>
      </tr>
      <tr>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5">Overused Reward Points:</td>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5">
          <fmt:formatNumber value="${rpBean.user.userAccountInfo.overusedRewardPoints}"
                            pattern="<%=FormatUtils.currencyFormatPattern%>"/>
        </td>
      </tr>
      <tr>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5"><span class="lightBlue">Balance Reward Points:</span></td>
        <td class="fnt-sz10 mrgn-bt-5 mrgn-lr-5">
          <span class="lightBlue">
          <fmt:formatNumber
              value="${rpBean.redeemablePoint - rpBean.user.userAccountInfo.overusedRewardPoints}"
              pattern="<%=FormatUtils.currencyFormatPattern%>"/>
          </span>
        </td>
      </tr>
    </table>
    <div class="clear"></div>
    <table class="order-tbl">
      <tr class="order-specs-hdr btm-brdr">
        <th class="fnt-bold">S.No.</th>
        <th class="fnt-bold">Value</th>
        <th class="fnt-bold">Txn Date</th>
        <th class="fnt-bold">Expiry Date</th>
        <th class="fnt-bold">Txn Type</th>
        <th class="fnt-bold">Expired</th>
      </tr>
      <tbody>
          <c:forEach items="${rpBean.user.rewardPointTxnList}" var="rewardPointTxn" varStatus="ctr">
              <c:if test="${ctr.first}">
                  <tr class="order-tr top-brdr">
              </c:if>
              <c:if test="${ctr.last}">
                  <tr class="${ctr.index%2==0? 'order-tr btm-brdr':'order-tr btm-brdr bg-gray'}">
              </c:if>
              <c:if test="${!(ctr.first || ctr.last)}">
                  <tr class="${ctr.index%2==0? 'order-tr':'order-tr bg-gray'}">
              </c:if>
              <td>${ctr.count}</td>
              <td class="border-td">${rewardPointTxn.value}</td>
              <td class="border-td"><fmt:formatDate value="${rewardPointTxn.txnDate}" type="both"/></td>
              <td class="border-td"><fmt:formatDate value="${rewardPointTxn.expiryDate}" type="both"/></td>
              <td class="border-td">${rewardPointTxn.rewardPointTxnType.name}</td>
              <td>${rewardPointTxn.expired ? 'Expired':''}</td>
            </tr>
          </c:forEach>
      </tbody>
    </table>
    </div>

  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
      $('#rpsLink').addClass('selected');
  };
</script>
