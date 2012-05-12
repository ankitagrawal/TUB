<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.EditUserAction" var="userBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.user.EditUserAction" method="post">
      <s:errors/>
      <s:hidden name="user" value="${userBean.user.id}"/>
      Name: <s:text name="user.name"/> <br/>
      Login: <s:text name="user.login"/><br/>
      Email: <s:text name="user.email"/> <br/>
      User Hash: <s:text name="user.userHash"/><br/>
      <s:submit name="save" value="Save"/>

      <hr/>
      Other Actions:
      <br/>
      <s:link beanclass="com.hk.web.action.admin.user.ChangeUserRolesAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Change User Roles
      </s:link>
      <br/>
      <s:link beanclass="com.hk.web.action.admin.user.ResetPasswordLinkAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Generate Reset Password Link
      </s:link>
      <br/>
      <s:link beanclass="com.hk.web.action.admin.user.ChangeLoginPasswordAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Change Password
      </s:link>
      <br/>
      <s:link beanclass="com.hk.web.action.admin.warehouse.AssignWarehouseAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Assign User Warehouse
      </s:link>
      <br/>
      <%--<s:link beanclass="mhc.web.action.admin.DeleteUserAction">--%>
        <%--<s:param name="user" value="${userBean.user.id}"/>--%>
        <%--Delete User--%>
      <%--</s:link>--%>


    </s:form>

  </s:layout-component>

</s:layout-render>
