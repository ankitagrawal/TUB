<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <h2>
        ${paymentAction.affiliate.user.name}
    </h2>
    <br/>

    <s:form beanclass="web.action.affiliate.AffiliatePaymentAction">
      <s:errors/>
      <table>
      <thead>
      <tr>
        <th>Affiliate Category</th>
        <th>Overview</th>
        <th>Commission 1st Transaction</th>
        <th>Commission Latter Transaction</th>
      </tr>
      </thead>
      <c:forEach items="${paymentAction.affiliateCategoryCommissionList}" var="affiliateCategoryCommission" varStatus="ctr">
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].id" value="${affiliateCategoryCommission.id}"/>
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].affiliate" value="${affiliateCategoryCommission.affiliate}"/>
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].affiliateCategory" value="${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}"/>
        <tr>
          <td>${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}</td>
          <td>${affiliateCategoryCommission.affiliateCategory.overview}</td>
          <td>
            <s:text name="affiliateCategoryCommissionList[${ctr.index}].commissionFirstTime" value="${affiliateCategoryCommission.commissionFirstTime}"/></td>
          <td>
            <s:text name="affiliateCategoryCommissionList[${ctr.index}].commissionLatterTime" value="${affiliateCategoryCommission.commissionLatterTime}"/></td>
        </tr>
      </c:forEach>
      </table>
      <div class="buttons"><s:submit name="savePlan" value="Save"/></div>
    </s:form>
  </s:layout-component>
</s:layout-render>