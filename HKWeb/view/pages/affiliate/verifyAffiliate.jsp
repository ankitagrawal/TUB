<%@ page import="com.hk.constants.affiliate.EnumAffiliateMode" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateType" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.VerifyRejectAffiliateAction" var="verifyAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
      <fieldset class="right_label">
          <legend>Search Affiliates</legend>
          <s:form beanclass="com.hk.web.action.core.affiliate.VerifyRejectAffiliateAction" method="get"
                  autocomplete="false">
              <div class='label'>Name</div>
              <s:text name="name" style="width:150px"/>
              <div class='label'>Email</div>
              <s:text name="email" style="width:150px"/>
              <div class='label'>Website</div>
              <s:text name="websiteName" style="width: 100px;"/>
              <div class='label'>Type</div>
              <s:select name="affiliateType">
                  <c:forEach items="<%=EnumAffiliateType.getAllAffiliateTypes()%>" var="aType">
                      <s:option value="${aType.id}">${aType.name}</s:option>
                  </c:forEach>
              </s:select>
              <div class='label'>Mode</div>
              <s:select name="affiliateMode">
                  <c:forEach items="<%=EnumAffiliateMode.getAllAffiliateModes()%>" var="aMode">
                      <s:option value="${aMode.id}">${aMode.name}</s:option>
                  </c:forEach>
              </s:select>
              <div class='label'>Status</div>
              <s:select name="affiliateMode">
                  <c:forEach items="<%=EnumAffiliateStatus.getAllAffiliateStatus()%>" var="aStatus">
                      <s:option value="${aStatus.id}">${aStatus.name}</s:option>
                  </c:forEach>
              </s:select>
              <s:submit name="pre" value="Search"/>
          </s:form>
      </fieldset>

      <s:form beanclass="com.hk.web.action.core.affiliate.VerifyRejectAffiliateAction">
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
            Verify/Reject
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
              <s:checkbox name="affiliateUsersToVerifyOrReject[]" value="${unverifiedAffiliate.user}"/>
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
      <s:submit name="rejectAffiliates" value="Reject"/>
    </s:form>
  </s:layout-component>
</s:layout-render>