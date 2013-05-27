<%@ page import="com.hk.constants.queue.EnumActionTask" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" var="actionItemBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

    <s:layout-component name="heading">Display the Action Item : ${actionItemBean.actionItem.id}</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" method="post">
            <h2>Buckets:</h2>
            <s:hidden name="actionItem" value="${actionItemBean.actionItem}"/>
            <div class="checkBoxList">
                <c:forEach items="${actionItemBean.buckets}" var="bucket" varStatus="ctr">
                    <label><s:checkbox name="buckets[${ctr.index}].selected"/> ${bucket.name}
                        <s:hidden name="buckets[${ctr.index}].id" value="${bucket.id}"/>
                    </label>
                    <br/>
                </c:forEach>
                  <s:submit name="save" value="Save"/>
            </div>

            <div align="justify" >
                Select the Action Task  <br>
                 <s:select name="actionTaskId" value="${actionItemBean.actionItem.currentActionTask.id}">
                    <c:forEach items="${hk:listNextActionTasks(actionItemBean.actionItem)}" var="enumActionTask">
                        <s:option value="${enumActionTask.id}">${enumActionTask.name}</s:option>
                    </c:forEach>
                </s:select>
                <s:submit name="updateTask" value="Update Task"/>
            </div>

        </s:form>

        <div align="center">
            <h1>
                <s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" event="flagActionItems">
                    <s:param name="actionItem" value="${actionItemBean.actionItem.id}"/>
                    Mark Flag
                </s:link>
            </h1>

            <h1>
                <s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" event="changeTrafficState">
                    <s:param name="actionItem" value="${actionItemBean.actionItem.id}"/>
                    Assign Trafic State
                </s:link>
            </h1>
        </div>
    </s:layout-component>

</s:layout-render>
