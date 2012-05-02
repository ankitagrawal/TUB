<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.VerifyAffiliateAction" var="verifyAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.VerifyAffiliateAction">
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${verifyAction}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${verifyAction}"/>
      <table>
        <tr>
          <th>
            Name
          </th>
          <th>
            Email
          </th>
          <th>
            Verify
          </th>
          <th>
            WebSite
          </th>
          <th>
            Contact
          </th>
        </tr>
        <c:forEach items="${verifyAction.unverifiedAffiliates}" var="unverifiedAffiliate">
          <tr>
            <td>
                ${unverifiedAffiliate.user.name}
            </td>
            <td>
                ${unverifiedAffiliate.user.email}
            </td>
            <td>
              <s:checkbox name="affiliateUsersToVerify[]" value="${unverifiedAffiliate.user}"/>
            </td>
            <td>
                ${unverifiedAffiliate.websiteName}
            </td>
            <td>
              <c:if test="${fn:length(unverifiedAffiliate.user.addresses) > 0}">
                ${unverifiedAffiliate.user.addresses[0].phone}
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </table>
      <s:submit name="verifyAffiliates" value="Verify"/>
    </s:form>
  </s:layout-component>
</s:layout-render>