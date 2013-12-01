<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.referral.ReferralManagerAction" var="couponBean" event="pre"/>

<s:layout-render name="/layouts/default.jsp" pageTitle="Referral manager">

  <s:layout-component name="heading">Referral Manager</s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#referralTerms').jqm({trigger: '.referralTerms', ajax: '@href'});
      });
    </script>
    <%--<link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>--%>
  </s:layout-component>

  <s:layout-component name="heading">My Referrals and Reward Points</s:layout-component>

  <s:layout-component name="modal">
    <div class="jqmWindow" id="referralTerms"></div>
  </s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <p style="text-align:left;">
      <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction">Referral Program</s:link> |
      My Referrals |
      <s:link beanclass="com.hk.web.action.core.referral.ReferralTermsAction" class="referralTerms">T&C</s:link>
    </p>


    <div class="clear"></div>
    <c:choose>
      <c:when test="${fn:length(couponBean.user.rewardPointList) == 0}">
        <div class="no_data">No Referrals Yet</div>
      </c:when>
      <c:otherwise>
        <table class="cont footer_color" style="font-size: 14px;" id="detail">
          <tr>
            <th>S.No.</th>
            <th>Name</th>
            <th>Order date</th>
            <th>Points</th>
            <th>Status</th>
            <th>Comments</th>
          </tr>
          <c:forEach items="${couponBean.user.rewardPointList}" var="rewardPoint" varStatus="ctr">
            <c:if test="${rewardPoint.referredUser != null}">
              <tr>
                <td>${ctr.count}</td>
                <td>${rewardPoint.referredUser != null ? rewardPoint.referredUser.name : 'NA'}</td>
                <td><fmt:formatDate value="${rewardPoint.createDate}" type="both"/></td>
                <td>${rewardPoint.value}</td>
                <td>${rewardPoint.rewardPointStatus.name}</td>
                <td>${rewardPoint.comment}</td>
              </tr>
            </c:if>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>

  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('rpLink').style.fontWeight = "bold";
  };
</script>
<style type="text/css">
  table#detail {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 5px;
    border: 1px solid;
    border-collapse: separate;
  }

  table th {
    background: #f0f0f0;
    padding: 5px;
    text-align: left;
  }

  table td {
    padding: 5px;
    text-align: left;
    font-size: 0.9em;
  }
</style>