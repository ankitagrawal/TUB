<%@ page import="com.hk.domain.user.Permission" %>
<%@ page import="com.hk.domain.user.Role" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.dao.RoleDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
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
        <div style="display: none;">
            <s:link beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction" id="getPerm" event="getPermissions"></s:link>
        </div>
        <s:form beanclass="com.hk.web.action.admin.roles.AddRolePermissionAction">
            <fieldset>
                <legend>Add permissions to role</legend> <br/><br/><br/>
                <div align="center">
                    <label>Roles</label>&nbsp;
                    <s:select id = "roleSelect" name="role.name" style="width: 175px;">
                        <s:option value="">---Select Role---</s:option>
                        <s:options-collection collection="${roleList}" value="name" label="name"/>
                    </s:select>
                </div> <br/><br/>
                <div style="width:1160px;margin:10px;float: left;">
                    <div style="float: left;">
                        <label>Permissions </label>&nbsp;
                        <s:select name="allPermission" id= "mltPerm" style="width : 275px; padding: 2">
                            <s:option value="">---Select one permission to add---</s:option>
                            <s:options-collection collection="${permissionList}" value="name" label="name"/>
                        </s:select> &nbsp;&nbsp;&nbsp;
                    </div>
                    <div style="float: right;width:730px;">
                        <label style="float:left;width:265px;"> Add/Delete Permissions to save : </label>
                        <ul id="currPerm" style="float: right;min-width:380px;margin:0 40px 0 0 ;height:100px;overflow-y: scroll; background-color:#F2F7FB; padding: 10px;border:1px solid #A2C4E5; "/>
                    </div>
                </div>
                <s:hidden name="userPermissions" id="userPermissions"/>
                <s:submit name="linkRoles" value="Save Permissions for Role" style="font-size:0.9em" id="savePermissions" />
            </fieldset>
            <fieldset>
                <div>
                    <legend>Add roles to user</legend><br/><br/><br/>
                    <label>Users </label>&nbsp;
                    <s:select id="userSelect" name="user.id" style="width: 175px;">
                        <s:option value="">--Select User--</s:option>
                        <c:forEach items="${userList}" var="userName">
                            <c:if test="${userName.name != 'Guest' && userName.name != null}">
                                <s:option value="${userName.id}">${userName.name}, ${userName.login} </s:option>
                            </c:if>
                        </c:forEach>
                    </s:select>
                </div><br/><br/>
                <div>
                    <label style="float:left;width:265px;">Add Roles to User</label>&nbsp;
                    <s:select id="mltRoles" name="roleList" multiple="true" style="width: 275px; height: 126px; ">
                        <s:options-collection collection="${roleList}" value="name" label="name"/><br/>
                    </s:select>
                </div>
                <s:hidden name="userRoles" id="userRoles"  />
                <s:submit name="linkRoles" value="Save Roles to User" style="font-size:0.9em" id="saveRoles"/>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    $(document).ready(function(){
        $("#roleSelect").change(function(){
            var role = $(this).val();
            $.getJSON($("#getPerm").attr('href'),{roleName : role}, function(result){
                        if (result.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            $("#currPerm").empty();
                            var permissions = result.data.permission;
                            for(var i = 0; i < permissions.length; i++){
                                $('#currPerm').append("<li style='margin-top:0;' class='delPermission'><a href='javascript:void(0)' > " + permissions[i] + "</a></li>");
                            }
                        }else {
                            alert(result.message);
                            $("#currPerm").empty();
                            return false;
                        }
                    }
            );
        });
        $('#saveRoles').click(function(){
            if($('#userSelect').val() == "" ){
                alert("Choose valid entries");
                return false;
            }
            $('#mltRoles').each(function(i,selectedRoles){
                var userRoles = "";
                userRoles += (userRoles == "") ? "" : ",";
                userRoles += ($(selectedRoles).val());
                if(userRoles == "null"){
                    alert("Select atleast one Role!");
                    return false;
                }else{
                    $('#userRoles').val(userRoles);
                }
            });
        });
        $("#mltPerm").change(function(){
            if($('#roleSelect').val() == ""){
                alert("Kindly choose a role");
                return false;
            }
            var x = false;
            $("#currPerm li a").map(function() {
                if($(this).text().trim()==  $("#mltPerm").val()){
                    x = true;
                    alert("This already exists");
                    return false;
                }
            });
            if($(this).val()!= "" && x == false){
                $('#currPerm').append("<li style='margin-top:0;' class='delPermission'><a href='javascript:void(0)' >" + $(this).val() + "</a></li>");
            }
        });
        $(".delPermission").live("click",function(){
            $(this).remove();
        });
        $("#savePermissions").click(function(){
            if($('#roleSelect').val() == ""){
                alert("Choose valid entries");
                return false;
            }
            var values = $("#currPerm li a").map(function() {
                return $(this).text().trim();
            }).get();
            if(values == ""){
                alert("No permissions to save");
                return false;
            }else{
                $('#userPermissions').val(values);
            }
        });
    });
</script>

