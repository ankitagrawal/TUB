<%@ page import="com.hk.domain.user.Permission" %>
<%@ page import="com.hk.domain.user.Role" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.dao.RoleDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp"%>


<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Link Roles To Permissions and Users">
    <s:layout-component name="htmlHead">
        <%
            RoleDao roleDao = ServiceLocatorFactory.getService(RoleDao.class);
            pageContext.setAttribute("roleList", roleDao.getAll(Role.class));
            pageContext.setAttribute("permissionList",roleDao.getAll(Permission.class));
            pageContext.setAttribute("userList",roleDao.getAll(User.class));
        %>
    </s:layout-component>
    <s:layout-component name="heading">
        Link Roles To Permissions and Users
    </s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction">
            <div>
                <label>Roles</label>&nbsp;
                <s:select name="SelectedRoles"  multiple="true">
                    <c:forEach items="${roleList}" var="roleName">
                        <s:option value="${roleName.name}">${roleName.name} </s:option>
                    </c:forEach>
                </s:select>
            </div>
            <br/>
            <div>
                <label>Permissions </label>&nbsp;
                <s:select name="SelectedPermissions"  multiple="true">
                    <c:forEach items="${permissionList}" var="permissionName">
                        <s:option value="${permissionName.name}">${permissionName.name} </s:option>
                    </c:forEach>
                </s:select>
            </div>
            <br/>
            <div>
                <label>Users </label>&nbsp;
                <s:select name="SelectedUsers"  multiple="true">
                    <c:forEach items="${userList}" var="userName">
                        <s:option value="${userName.name}">${userName.name} </s:option>
                    </c:forEach>
                </s:select>
            </div>
            <s:submit name="linkRoles" value="linkRoles" style="font-size:0.9em"/>
        </s:form>
    </s:layout-component>
</s:layout-render>

