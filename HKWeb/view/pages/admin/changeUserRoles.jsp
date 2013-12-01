<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.ChangeUserRolesAction" var="userBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">

    <s:useActionBean beanclass="com.hk.web.action.admin.user.ChangeUserRolesAction" var="userBean"/>
    Name: ${userBean.user.name}<br/>
    Login: ${userBean.user.login}<br/>
    <s:form beanclass="com.hk.web.action.admin.user.ChangeUserRolesAction" method="post">
      <h2>Roles:</h2>
      <c:forEach items="${userBean.userRoles}" var="role" varStatus="roleCount">
        <s:hidden name="userRoles[${roleCount.index}].name" value="${role.name}"/>
        <s:checkbox name="userRoles[${roleCount.index}].selected"/> ${role.name}<br/>
      </c:forEach>
      <s:hidden name="user" value="${userBean.user.id}"/>
      <s:submit name="change" value="Change Roles"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
