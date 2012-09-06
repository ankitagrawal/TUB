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
            <s:layout-component name="heading">
                Add Agent
            </s:layout-component>
             <br />
            <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
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
                    <c:set var="hub" value="${hk:getHubForHkdeliveryUser(hubAction.loggedOnUser)}"/>
                    <s:hidden name="hub" value="${hub.id}"/>${hub.name}
                </shiro:hasPermission>
                <br />
                <s:hidden name="agent" value="${hubAction.agent.id}"/>
                <label><strong> Agent name: </strong></label>&nbsp;&nbsp;${hubAction.agent.name}<br/>
                <label><strong> Agent email: </strong></label>&nbsp;&nbsp;${hubAction.agent.email}<br/><br/>
                <s:submit name="saveUserToHub" value="Add agent"/>
            </s:form>
        </c:if>
    </s:layout-component>
</s:layout-render>
