<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/16/12
  Time: 1:11 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.subscription.SubscriptionLifecycleAction" var="subscriptionLifeCycleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Subscription Lifecycle">
    <s:layout-component name="heading">Subscription Lifecycle : Subscription#${subscriptionLifeCycleBean.subscription.id}</s:layout-component>
    <s:layout-component name="content">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
            <tr class="even_row">
                <th width="20%">Activity Date</th>
                <th width="20%">Activity</th>
                <th width="20%">Activity By User</th>
                <th>Comments</th>
            </tr>
            <c:forEach items="${subscriptionLifeCycleBean.subscription.subscriptionLifecycles}" var="subscriptionLifeCycle">
                <tr>
                    <td><fmt:formatDate value="${subscriptionLifeCycle.date}" type="both"/></td>
                    <td>${subscriptionLifeCycle.subscriptionLifecycleActivity.name}</td>
                    <td>${subscriptionLifeCycle.user.name}</td>
                    <td>${subscriptionLifeCycle.comments}</td>
                </tr>
            </c:forEach>
        </table>

        <fieldset class="right_label" style="float:left;margin-top:25px;">
            <legend>New Comment</legend>
            <table>
                <s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionLifecycleAction">
                    <s:hidden name="subscription" value="${subscriptionLifeCycle.subscription}"/>

                    <tr>
                        <td>Comment:</td>
                        <td><s:textarea name="comment" style="height:100px" cols="50"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div class="buttons"><s:submit name="saveComment" value="Save"/></div>
                        </td>
                    </tr>
                </s:form>
            </table>
        </fieldset>
    </s:layout-component>
</s:layout-render>
