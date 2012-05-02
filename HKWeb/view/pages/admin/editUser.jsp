<%@ page import="com.hk.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.EditUserAction" var="userBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="web.action.admin.EditUserAction" method="post">
      <s:errors/>
      <s:hidden name="user" value="${userBean.user.id}"/>
      Name: <s:text name="user.name"/> <br/>
      Login: <s:text name="user.login"/><br/>
      Email: <s:text name="user.email"/> <br/>
      User Hash: <s:text name="user.userHash"/><br/>
      <s:submit name="save" value="save"/>

      <hr/>
      Other Actions:
      <br/>
      <s:link beanclass="web.action.admin.ChangeUserRolesAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Change User Roles
      </s:link>
      <br/>
      <s:link beanclass="web.action.admin.ResetPasswordLinkAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Generate Reset Password Link
      </s:link>
      <br/>
      <s:link beanclass="web.action.admin.ChangeLoginPasswordAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Change Password
      </s:link>
      <br/>
      <s:link beanclass="web.action.admin.AssignWarehouseAction" event="pre">
        <s:param name="user" value="${userBean.user.id}"/>
        Assign User Warehouse
      </s:link>
      <br/>
      <%--<s:link beanclass="web.action.admin.DeleteUserAction">--%>
        <%--<s:param name="user" value="${userBean.user.id}"/>--%>
        <%--Delete User--%>
      <%--</s:link>--%>


    </s:form>

  </s:layout-component>

</s:layout-render>
