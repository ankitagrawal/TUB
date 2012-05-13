<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <table>
    <tr>
      <th>
        Name
      </th>
      <th>
        Email
      </th>
      <th>
        Website
      </th>
      <th>
        Due Amount
      </th>
      <th>
        Plan
      </th>
      <th> Transaction</th>
      <th></th>
      <th>CALL</th>
    </tr>

    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" autocomplete="off">
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${paymentAction}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${paymentAction}"/>
      <c:forEach items="${paymentAction.affiliatePaymentDtoList}" var="affiliateDetails" varStatus="ctr">
        <tr>
          <td>
              ${affiliateDetails.affiliate.user.name}
          </td>
          <td>
              ${affiliateDetails.affiliate.user.email}
          </td>
          <td width="100px">
              ${affiliateDetails.affiliate.websiteName}
          </td>
          <s:hidden name="affiliatePaymentDtoList[${ctr.index}].affiliate" value="${affiliateDetails.affiliate}"/>
          <td>
            <fmt:formatNumber value=" ${affiliateDetails.amount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>             
          </td>
           <td>
            <s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" event="showAffiliatePlan">Manage Plan
              <s:param name="affiliate" value="${affiliateDetails.affiliate.id}"/>
            </s:link>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" event="showAffiliateDetails">Transaction Details
              <s:param name="affiliate" value="${affiliateDetails.affiliate.id}"/>
            </s:link>
          </td>
          <td>
          <s:link beanclass="com.hk.web.action.admin.user.AssumedLoginAction">
            <s:param name="user" value="${affiliateDetails.affiliate.user.id}"/>
            [Super login]
          </s:link>
          </td>
          <td>
            <c:if test="${fn:length(affiliateDetails.affiliate.user.addresses) > 0}">
              ${affiliateDetails.affiliate.user.addresses[0].phone}
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </table>
      <div class="buttons"><s:submit name="save" value="Save"/></div>
    </s:form>
  </s:layout-component>
</s:layout-render>