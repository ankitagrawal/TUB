<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Hub">
	<s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction" var="hubAction"/>
	<s:layout-component name="content">
		<s:layout-component name="heading">
			Search User
		</s:layout-component>
		<s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
			<label>Email address: </label>
			<s:text name="userEmailId" style="width:250px;"/>
			&nbsp;&nbsp;&nbsp;<s:submit name="searchAgent" value="Search Agent"/>
		</s:form>

		<c:if test="${hubAction.agent != null}">
			<c:choose>
				<c:when test="${hubAction.agent.hub == null}">
					<fieldset class="right_label">
						<legend>Add agent to a hub:</legend>
						<s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
							<ul>
								<li>
									<label><strong>Hub:</strong></label>
									<shiro:hasPermission name="<%=PermissionConstants.SELECT_HUB%>">
										<s:select name="hub" class="hubName">
											<s:option value="-Select Hub-">-Select Hub-</s:option>
											<hk:master-data-collection service="<%=MasterDataDao.class%>"
											                           serviceProperty="hubList" value="id"
											                           label="name"/>
										</s:select>
									</shiro:hasPermission>
									<shiro:hasPermission name="<%=PermissionConstants.VIEW_HUB%>">
										<shiro:lacksPermission name="<%=PermissionConstants.SELECT_HUB%>" >
											<c:set var="hub" value="${hk:getHubForHkdeliveryUser(hubAction.loggedOnUser)}"/>
											<s:hidden name="hub" value="${hub.id}"/>${hub.name}
										</shiro:lacksPermission>
									</shiro:hasPermission>
								</li>
								<li>
									<s:hidden name="agent" value="${hubAction.agent.id}"/>
									<label><strong> Agent name: </strong></label>&nbsp;&nbsp;${hubAction.agent.name}
								</li>
								<li>
									<label><strong> Agent email: </strong></label>&nbsp;&nbsp;${hubAction.agent.email}
								</li>
								<li>
									<s:submit name="saveUserToHub" value="Add agent"/>
								</li>
							</ul>
						</s:form>
					</fieldset>
				</c:when>

				<c:otherwise>
					<fieldset class="right_label">
						<legend>Remove Agent from hub:</legend>
						<br/>
						<s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
						<ul>
							<li>
								<label><strong>Hub:</strong></label>
								<s:hidden name="hub" value="${hubAction.agent.hub.id}"/>
								&nbsp;${hubAction.agent.hub.name}
							</li>
							<li>
								<s:hidden name="agent" value="${hubAction.agent.id}"/>
								<label><strong> Agent name: </strong></label>&nbsp;&nbsp;${hubAction.agent.name}
							</li>
							<li>
								<label><strong> Agent email: </strong></label>&nbsp;&nbsp;${hubAction.agent.email}
							</li>
							<li>
								<c:choose>
									<c:when test="${hubAction.agent.hub.id == null}">
										The given user does not belong to any existing hub
									</c:when>
									<c:otherwise>
										<s:submit name="removeAgentFromHub" value="Remove Agent from hub"/>
									</c:otherwise>
								</c:choose>
							</li>
							</s:form>
					</fieldset>
				</c:otherwise>
			</c:choose>
		</c:if>
	</s:layout-component>
</s:layout-render>
