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
                <div>
                    <div>
                        <label>Permissions </label>&nbsp;
                        <s:select id="mltPermission" name="permissionList"  style="width: 275px; height: 126px; padding: 2" multiple="true">
                            <s:options-collection collection="${permissionList}" value="name" label="name"/>
                        </s:select> &nbsp;&nbsp;&nbsp;
                        <label> Existing Permissions to Role </label>
                        <s:select name="currPermissions" id="currPerm" style="width: 275px; height: 126px; padding: 2" multiple="true">
                            <s:option value="">---Select to delete---</s:option>
                        </s:select>
                    </div>
                </div>
                <s:hidden name="userPermissions" id="userPermissions"/>
                <s:hidden name="deletePermissions" id="delPermissions"/>
                <s:submit name="linkRoles" value="linkRoles" style="font-size:0.9em" id="savePermissions" />
                <s:submit name="linkRoles" value="deletePermissions" style="font-size: 0.9em" id="deletePermissions"/>
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
                <div >
                    <label>Roles</label>&nbsp;
                    <s:select id="mltRoles" name="roleList" multiple="true" style="width: 275px; height: 126px; padding: 2">
                        <s:options-collection collection="${roleList}" value="name" label="name"/>
                    </s:select>
                </div>
                <s:hidden name="userRoles" id="userRoles"  />
                <s:submit name="linkRoles" value="linkRoles" style="font-size:0.9em" id="saveRoles"/>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">

    $(document).ready(function(){
        $("#roleSelect").change(function(){
            var r = $(this).val();
            $.getJSON($("#getPerm").attr('href'),{roleName : r}, function(result){
                        if (result.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            $("#currPerm").empty();
                            $('<option />', {value: "", text: "----Select to Delete---"}).appendTo($("#currPerm"));
                            $.each(result.data, function(key,val){
                                $('<option />', {value: val, text: val}).appendTo($("#currPerm"));
                            });
                        }else {
                            alert(result.message);
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
        $('#savePermissions').click(function(){
            if($('#roleSelect').val() == ""){
                alert("Choose valid entries");
                return false;
            }
            $('#mltPermission').each(function(j,selectedPermissions){
                var userPermissions = "";
                userPermissions += (userPermissions == "") ? "" : ",";
                userPermissions += ($(selectedPermissions).val());
                if(userPermissions == "null"){
                    alert("Select atleast one Permission to link to this Role");
                    return false;
                }else{
                    $('#userPermissions').val(userPermissions);
                }
            });
        });

        $('#deletePermissions').click(function(){
            if($('#roleSelect').val() == ""){
                alert("Choose valid entries");
                return false;
            }
            $('#currPerm').each(function(j,selectedPermissions){
                var deletePermissions = "";
                deletePermissions += (deletePermissions == "") ? "" : ",";
                deletePermissions += ($(selectedPermissions).val());
                if(deletePermissions == "null"){
                    alert("Select atleast one Permission to delete");
                    return false;
                }else{
                    $('#delPermissions').val(deletePermissions);
                }
            });
        });
    });
</script>

