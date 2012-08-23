<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.VerifyAffiliateAction" var="verifyAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
      <s:form beanclass="com.hk.web.action.core.affiliate.VerifyAffiliateAction" method="get" autocomplete="false">
          <fieldset class="top_label">
              <legend>Search Affiliates</legend>
              <ul>
                  <div class="grouped">
                      <label>Name:</label><s:text name="name" style="width:150px"/>
                      &nbsp; &nbsp;
                      <label>Email:</label><s:text name="email" style="width:150px"/>
                      <li><label>BO Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
                      <li><label>BO Gateway Order ID</label> <s:text name="gatewayOrderId"/></li>
                      <li><label>Email ID</label> <s:text name="email"/></li>
                      <li><label>Login ID</label> <s:text name="login"/></li>
                      <li><label>Name</label> <s:text name="name"/></li>
                      <li><label>Phone</label> <s:text name="phone"/></li>
                      <li><label>Status</label><s:select name="orderStatus">
                          <option value="">Any order status</option>
                          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="orderStatusList" value="id"
                                                     label="name"/>
                      </s:select></li>
                      <li><label>Payment Mode</label><s:select name="paymentMode">
                          <option value="">Any Payment Mode</option>
                          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentModes" value="id"
                                                     label="name"/>
                      </s:select></li>
                          <%--<li><label>Tracking ID</label> <s:text name="trackingId" style="width: 120px;"/></li>--%>
                      <li>
                          <label>Start
                              date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                  name="startDate"/>
                      </li>
                      <li>
                          <label>End
                              date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                  name="endDate"/>
                      </li>
                  </div>
              </ul>
              <s:submit name="pre" value="Search"/>
          </fieldset>
      </s:form>

    <s:form beanclass="com.hk.web.action.core.affiliate.VerifyAffiliateAction">
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