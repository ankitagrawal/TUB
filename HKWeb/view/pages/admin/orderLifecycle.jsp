<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" var="orderLifeCycleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Lifecycle">
  <s:layout-component name="heading">Order Lifecycle : Order#${orderLifeCycleBean.order.gatewayOrderId}</s:layout-component>
  <s:layout-component name="content">
   User Comments:- ${orderLifeCycleBean.order.userComments}
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
      <tr class="even_row">
        <th width="20%">Activity Date</th>
        <th width="20%">Activity</th>
        <th width="20%">Activity By User</th>
        <th>Comments</th>
      </tr>
      <c:forEach items="${orderLifeCycleBean.order.orderLifecycles}" var="orderLifeCycle">
        <tr>
          <td><fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/></td>
          <td>${orderLifeCycle.orderLifecycleActivity.name}</td>
          <td>${orderLifeCycle.user.name}</td>
          <td>${orderLifeCycle.comments}</td>
        </tr>
      </c:forEach>
    </table>

    <fieldset class="right_label" style="float:left;margin-top:25px;">
      <legend>New Comment</legend>
      <table>
        <s:form beanclass="com.hk.web.action.admin.order.OrderLifecycleAction">
          <s:hidden name="order" value="${orderLifeCycleBean.order}"/>

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
