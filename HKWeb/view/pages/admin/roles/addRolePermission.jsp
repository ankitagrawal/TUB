<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" var="roleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Roles and Permissions">

  <s:layout-component name="heading">
    Add Roles And Permissions
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass = "com.hk.web.action.admin.roles.AddRolePermissionAction" >
      <a href="#" id="buttonRole"> Add New Role</a> <br/><br/>
      <fieldset id = "addRole">
        <div class = "new role">
          <legend>Add New Role</legend>  <br/> <br/>
          <label style="float:left;width:300px;">Name of new Role: </label> &nbsp;
          <s:text style= "width:200px;" id = "role-name" name="role.name"/> <br/>
          <s:submit style="float:left; font-size:0.9em;" name="saveRole" id="saveRole"/>
        </div>
      </fieldset>
      <a href="#" id="buttonPermission"> Add New Permission</a> <br/><br/>
      <fieldset id = "addPermission">
        <div class = "new permission">
          <legend>Add New Permission</legend>  <br/> <br/>
          <label style="float:left;width:300px;">Name of a new Permission: </label> &nbsp;
          <s:text style="width:200px;" id = "permission-name" name="permission.name"/><br/>
          <s:submit style="float:left; font-size:0.9em;" name="savePermission" id="savePerm"/>
        </div>
      </fieldset>
      <s:link beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" event="linkRolePermission">
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

    $("#buttonPermission").click(function(){
      $('#buttonPermission').hide();
      $('#addPermission').show(500);
    });

    $("#saveRole").click(function(){
      if($("#role-name").val() == ""){
        alert("Add a role");
        return false;
      }
    });
    $("#savePerm").click(function(){
      if($("#permission-name").val() == "" ){
        alert("Add a permission");
        return false;
      }
    });
  });

  $('#role-name').focus();
  $('#permission-name').focus();
</script>




