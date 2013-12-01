<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.order.OrderCommentAction" var="orderCommentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Comments for Order#${orderCommentAction.order.gatewayOrderId}</s:layout-component>
  <s:layout-component name="content"><br/><br/>
    <c:if test="${orderCommentAction.order.userComments != null}">
      <text style="color:red; font-weight:bold">User Instructions: </text>
      ${orderCommentAction.order.userComments}<br/><br/>
    </c:if>
    <c:if test="${!empty orderCommentAction.order.comments}">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <th>Name</th>
          <th>Comment</th>
          <th>Date</th>
        </tr>
        <c:forEach items="${orderCommentAction.order.comments}" var="comment">
          <tr>
            <td>${comment.name}</td>
            <td>${comment.comment}</td>
            <td>${comment.date}</td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
    <c:if test="${empty orderCommentAction.order.comments}">
      No comment logged.
    </c:if>

    <fieldset class="right_label" style="float:left;margin-top:25px;">
      <legend>New Comment</legend>
      <table>
        <s:form beanclass="com.hk.web.action.admin.order.OrderCommentAction">
          <s:hidden name="order" value="${orderCommentAction.order}"/>          
          <tr>
            <td>Comment:</td>
            <td><s:textarea name="comment" style="height:100px"/></td>
          </tr>
          <tr>
            <td></td>
            <td>
              <div class="buttons"><s:submit name="save" value="Save"/></div>
            </td>
          </tr>
        </s:form>
      </table>
    </fieldset>
  </s:layout-component>
</s:layout-render>
