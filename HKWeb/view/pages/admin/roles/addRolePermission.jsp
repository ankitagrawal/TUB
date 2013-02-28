<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Roles and Permissions">

    <s:layout-component name="heading">
        Add Roles And Permissions
    </s:layout-component>

    <s:layout-component name="content">
        <a href="#" id="buttonRole"> Add New Role </a> <br/>
        <fieldset id = "addRole">
            <div class = "new role">
                <legend>Add New Role</legend>  <br/>
                <s:form beanclass = "com.hk.web.action.admin.roles.AddRolePermissionAction" >
                    <label>Role: </label> <s:text id = "role-name" name="role.name"/>
                    <s:submit name="saveRole" value="save"/>
                </s:form>
           </div>
        </fieldset>
    </s:layout-component>

</s:layout-render>

<script type="text/javascript">
    $(document).ready(function(){
        $('#addRole').hide();
        $("#buttonRole").click(function(){
            $('#buttonRole').hide();
            $('#addRole').show(500);
        });
    });
    $('#role-name').focus();
</script>




