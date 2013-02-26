/**
* Created with IntelliJ IDEA.
* User: Shilpa
* Date: 2/25/13
* Time: 7:16 PM
* To change this template use File | Settings | File Templates.
*/

<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Roles and Permissions">
    <s:layout-component name="heading">
        Add Roles and Permissions
    </s:layout-component>
    <s:layout-component name="contents">
        <s:form beanclass = "com.hk.web.action.admin.roles.AddRolePermissionAction" >
            <fieldset>
                <legend>Add New Role</legend>
                <s:label>Role : </s:label>${roleBean.role}
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>




