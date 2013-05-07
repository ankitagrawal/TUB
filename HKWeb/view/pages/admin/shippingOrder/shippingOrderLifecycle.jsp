<%@ page import="com.hk.constants.analytics.EnumReasonType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction" var="orderLifeCycleBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipping Order Lifecycle">
  <s:layout-component name="heading">Order Lifecycle : Order#${orderLifeCycleBean.shippingOrder.gatewayOrderId}</s:layout-component>
  <s:layout-component name="content">
   User Comments:- ${orderLifeCycleBean.shippingOrder.baseOrder.userComments}
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zebra_vert">
      <tr class="even_row">
        <th width="20%">Activity Date</th>
        <th width="20%">Activity</th>
        <th width="20%">Activity By User</th>
        <th>Reason/Comments</th>
      </tr>
      <c:forEach items="${orderLifeCycleBean.shippingOrderLifeCycles}" var="orderLifeCycle">
        <tr>
          <td><fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/></td>
          <td>${orderLifeCycle.shippingOrderLifeCycleActivity.name}</td>
          <td>${orderLifeCycle.user.name}</td>
            <td>
                <c:choose>
                    <c:when test="${not empty orderLifeCycle.lifecycleReasons}">
                        <c:forEach items="${orderLifeCycle.lifecycleReasons}" var="lifecycleReason">
                            ${lifecycleReason.reason.classification.primary} - ${lifecycleReason.reason.classification.secondary}
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${not empty hk:getReasonsByType(orderLifeCycle.shippingOrderLifeCycleActivity.name)}">
                            <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction">
                                <s:hidden name="lifecycleReason.shippingOrderLifecycle.id" value="${orderLifeCycle.id}"/>
                                <s:hidden name="shippingOrder" value="${orderLifeCycleBean.shippingOrder}"/>
                                <s:select name="lifecycleReason.reason">
                                    <option value="">Choose Reason</option>
                                    <%--<c:set var="escalateBackReason" value="<%=EnumReasonType.Escalate_Back%>"/>--%>
                                    <c:forEach items="${hk:getReasonsByType(orderLifeCycle.shippingOrderLifeCycleActivity.name)}" var="reason">
                                        <option value="${reason.id}">${reason.classification.primary}- ${reason.classification.secondary}</option>
                                    </c:forEach>
                                </s:select>
                                <s:submit name="logReasonForAnActivity" value="Log Reason"/>
                            </s:form>
                        </c:if>
                    </c:otherwise>
                </c:choose>
                ${orderLifeCycle.comments}</td>
        </tr>
      </c:forEach>
    </table>

    <fieldset class="right_label" style="float:left;margin-top:25px;">
      <legend>New Comment</legend>
      <table>
        <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction">
          <s:hidden name="shippingOrder" value="${orderLifeCycleBean.shippingOrder}"/>

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
