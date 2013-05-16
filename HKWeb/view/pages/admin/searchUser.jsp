<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.user.SearchUserAction" event="pre" var="userBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">Search User</s:layout-component>
  <s:layout-component name="content">


    <s:form beanclass="com.hk.web.action.admin.user.SearchUserAction" method="get" renderFieldsPresent="false" renderSourcePage="false">
      <fieldset class="top_label">
        <ul>
          <div class="grouped">
            <li><label>Id </label> <s:text name="userFilterDto.id"/></li>
            <li><label>Login </label> <s:text name="userFilterDto.login"/></li>
            <li><label>Email </label> <s:text name="userFilterDto.email"/></li>
            <li><label>Name </label> <s:text name="userFilterDto.name"/></li>
          </div>
          <div class="grouped">
            <li><label>Create Date From: </label>
              <s:text name="userFilterDto.createDateFrom" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
            </li>
            <li><label>To</label>
              <s:text name="userFilterDto.createDateTo" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
            </li>
            <li><label>Last Login Date From: </label>
              <s:text name="userFilterDto.lastLoginDateFrom" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
            </li>
            <li><label>To</label>
              <s:text name="userFilterDto.lastLoginDateTo" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
            </li>
          </div>
        </ul>
        <div class="buttons"><s:submit name="search" value="Search"/></div>
      </fieldset>

    </s:form>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${userBean}"/>

    <table class="cont" width="100%">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>Login</th>
        <th>Id and Name</th>
        <th>Email</th>
        <th>Orders</th>
        <th>Add Reward Point</th>
        <th>Reward Points Txn History</th>
        <th>User Referrals</th>
        <th>Karma</th>
      </tr>
      </thead>
      <c:forEach items="${userBean.userList}" var="user" varStatus="userCount">
          <c:choose>
          <c:when test="${user.priorityUser}">
               <tr style="background:#F6CECE;" >
          </c:when>
          <c:otherwise>
               <tr >
          </c:otherwise>
        </c:choose>

          <td>${userCount.count}</td>
          <td>
              ${user.login}<br/>
            <s:link beanclass="com.hk.web.action.admin.user.AssumedLoginAction">
              <s:param name="user" value="${user.id}"/>
              [Super login]
            </s:link>
            <s:link beanclass="com.hk.web.action.admin.user.EditUserAction">
              <s:param name="user" value="${user}"/>
              [Edit User]
            </s:link>
            <s:link beanclass="com.hk.web.action.admin.user.CustomerLifeCycleAction">
               <s:param name="user" value="${user}"/>
               [View Customer LifeCycle]
            </s:link>
          </td>
          
          <td>[${user.id}] ${user.name}</td>
          <td>
              ${user.email}
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders">
              <s:param name="email" value="${user.email}"/>
              View orders
            </s:link>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.reward.AddRewardPointAction" event="pre">
              Add Reward Point
              <s:param name="user" value="${user.id}"/>
            </s:link>
          </td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.discount.RewardPointTxnStatementAction" event="pre">
              Reward Points Txn History                     
              <s:param name="user" value="${user.id}"/>
            </s:link>
          </td>
          <td>
              <s:link beanclass="com.hk.web.action.admin.user.UserReferrralsAddressesAction" event="referralList">
                User Referrals
                <s:param name="user" value="${user.id}"/>
              </s:link>
          </td>
          <td>
              ${user.karmaProfile.karmaPoints}
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${userBean}"/>

  </s:layout-component>
</s:layout-render>
