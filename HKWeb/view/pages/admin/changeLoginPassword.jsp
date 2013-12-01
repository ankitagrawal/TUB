<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.ChangeLoginPasswordAction" var="userBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">

    <s:useActionBean beanclass="com.hk.web.action.admin.user.ChangeLoginPasswordAction" var="userBean"/>
    Name: ${userBean.user.name}<br/>
    Login: ${userBean.user.login}<br/>
    <s:form beanclass="com.hk.web.action.admin.user.ChangeLoginPasswordAction" method="post">
      Enter new password: <s:password name="password"/><br/>
      Confirm Password: <s:password name="passwordConfirm"/> <br/>
      <s:hidden name="user" value="${userBean.user.id}"/>
      <s:submit name="change" value="Change"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
