<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Admin Home">

    <s:layout-component name="heading">Refresh Cache</s:layout-component>
    <s:layout-component name="content">

        <div class="left roundBox">

            <h2>Refresh Cache</h2>

            <h3><s:link beanclass="com.hk.web.action.core.RefreshCacheAction" event="refreshRoles">Refresh Roles</s:link></h3>

        </div>

        <div class="cl"></div>

    </s:layout-component>
</s:layout-render>
