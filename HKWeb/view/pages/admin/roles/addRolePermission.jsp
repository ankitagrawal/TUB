<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Roles and Permissions">

    <s:layout-component name="heading">
        Add Roles And Permissions
    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass = "com.hk.web.action.admin.roles.AddRolePermissionAction" >
            <a href="#" id="buttonRole"> Add New Role or Permission</a> <br/><br/>
            <fieldset id = "addRole">
                <div class = "new role">
                    <legend>Add New Role/Permission</legend>  <br/>
                    <label>Role: </label> <s:text id = "role-name" name="role.name"/> <br/>
                    <label>Permission: </label> <s:text id = "permission-name" name="permission.name"/>
                </div>
            </fieldset>
                <s:link beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" event="linkRolePermission">
                    <s:submit name="saveRoleAndPermission" value="save"/>  <br/><br/>
                    <div>Link Roles to Permissions and Users</div>
                    <s:param name="role" value="${roleBean.role}"/>
                    <s:param name="permission" value="${roleBean.permission}"/>
                </s:link>
        </s:form>
    </s:layout-component>

</s:layout-render>

<script type="text/javascript">
    $(document).ready(function(){
        $('#addRole').hide();
        $('#addPermission').hide();
        $("#buttonRole").click(function(){
            $('#buttonRole').hide();
            $('#addRole').show(500);
        });
    });
    $('#role-name').focus();
    $('#permission-name').focus();
</script>




