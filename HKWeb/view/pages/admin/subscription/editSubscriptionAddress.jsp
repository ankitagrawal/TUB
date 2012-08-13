<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/18/12
  Time: 4:13 PM
--%>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

    <s:layout-component name="content">

        <s:useActionBean beanclass="com.hk.web.action.admin.subscription.ChangeSubscriptionAddressAction" var="addressBean" event="edit"/>

        <s:form beanclass="com.hk.web.action.admin.subscription.ChangeSubscriptionAddressAction">
            <fieldset>
                <legend>edit subscription address</legend>
                <ul>
                    <li>
                        <s:param name="newAddress.user" value="${addressBean.subscription.user}"/>
                        Name: <s:text name="newAddress.name"/><br/>
                        Address line1: <s:text name="newAddress.line1"/><br/>
                        Address line2: <s:text name="newAddress.line2"/><br/>
                        City: <s:text name="newAddress.city"/><br/>
                        State: <s:text name="newAddress.state"/><br/>
                        Pin: <s:text name="newAddress.pin"/><br/>
                        Phone: <s:text name="newAddress.phone"/><br/>
                        <s:hidden name="subscription" value="${addressBean.subscription}"/>
                        Copy This Address to user's address book <s:checkbox name="copyToUserAddressBook"/><br/>
                        <s:submit name="save" value="save"/>
                        <s:link beanclass="com.hk.web.action.admin.subscription.ChangeSubscriptionAddressAction">
                            cancel
                            <s:param name="subscription" value="${addressBean.subscription.id}"/>
                        </s:link>
                    </li>
                </ul>
            </fieldset>

        </s:form>

    </s:layout-component>

</s:layout-render>
