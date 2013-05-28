<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Hub">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.ZoneAction" var="zoneAction"/>
	<s:layout-component name="content">
		<script>
			$(document).ready(function() {
				$('.show-edit-block').click(function(event) {
					event.preventDefault();
					var edit_block = $(this).parents('.column').siblings('.zone-edit-block');
					edit_block.slideDown();
				});

				$('#show-new-zone-form').click(function(event){
					event.preventDefault();					 	
					$('#new-zone-form').slideDown();
				});

			})
		</script>
		<s:layout-component name="heading">
			Manage Zones
		</s:layout-component>
		<table border="1">
			<thead>
			<tr>
				<th style="text-align:center;">Zone Id</th>
				<th style="text-align:center;">Zone Name</th>
				<th></th>
				<th></th>
			</tr>
			</thead>
			<tbody>

			<c:forEach items="${zoneAction.zoneList}" var="zone_edit" varStatus="zoneCtr">
				<s:form beanclass="com.hk.web.action.admin.courier.ZoneAction">
					<tr>
						<td style="text-align:center;">
								${zone_edit.id}
						</td>
						<td style="text-align:center;" class="column">
								${zone_edit.name}
							<a href="#" class="show-edit-block"> Edit name </a>
						</td>

						<td class="zone-edit-block" style="display: none;">
							<s:hidden name="zone" value="${zone_edit.id}"/>
							<s:text name="zone.name" value="${zone_edit.name}"/>
						</td>
						<td class="zone-edit-block" style="display: none;">
							<s:submit name="saveZone" value="Save"/>
						</td>
					</tr>
				</s:form>
			</c:forEach>
			
			</tbody>
		</table>
		<a href="#" id="show-new-zone-form">Add new zone</a>
		<s:form beanclass="com.hk.web.action.admin.courier.ZoneAction" id="new-zone-form" style="display: none;">
			<s:label name="Zone name:"></s:label>
				<s:text name="zone.name" />
				<s:submit name="saveZone" value="Save"/>
			</s:form>
	</s:layout-component>
</s:layout-render>
