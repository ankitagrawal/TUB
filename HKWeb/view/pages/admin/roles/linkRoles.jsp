<%@ page import="com.hk.domain.user.Permission" %>
<%@ page import="com.hk.domain.user.Role" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.dao.RoleDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Link Roles To Permissions and Users">
    <s:layout-component name="heading">
        Link Roles To Permissions and Users
    </s:layout-component>
</s:layout-render>
<%
    RoleDao roleDao = ServiceLocatorFactory.getService(RoleDao.class);
    pageContext.setAttribute("roleList", roleDao.getAll(Role.class));
    pageContext.setAttribute("permissionList",roleDao.getAll(Permission.class));
    pageContext.setAttribute("userList",roleDao.getAll(User.class));
%>

<s:layout-component name="content">
    <div align="left">
        Roles
        <s:select name="multiRole" mulitple="multiple">
            <c:forEach items="${roleList}" var="roleName">
                <s:option value="${roleName.name}">${roleName.name} </s:option>
            </c:forEach>
        </s:select>
        <s:submit name="" value="Search By Basket Category" style="font-size:0.9em"/>
    </div>
</s:layout-component>