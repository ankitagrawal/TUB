<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.ResetPasswordLinkAction" var="userBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">

    <s:useActionBean beanclass="com.hk.web.action.admin.user.ResetPasswordLinkAction" var="userBean"/>
    Name: ${userBean.user.name}<br/>
    Login: ${userBean.user.login}<br/>
    Reset Password Link: ${userBean.resetPasswordLink}<br/>

  </s:layout-component>

</s:layout-render>
