<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.reward.AddRewardPointAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Reward Point">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">Add Reward Point for user ${rpBean.user.login}</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.reward.AddRewardPointAction" method="post">
      <fieldset>
           Cancel Order Reward Points will not be given from this screen.
      </fieldset>
      <fieldset class="left_label">
        <ul>
          <s:hidden name="user" value="${rpBean.user.id}"/>
          <li><label>Value</label><s:text name="value"/></li>
          <li><label>Order Id</label><s:text name="orderId"/></li>
          <li><label>Mode</label>
            <s:select name="rewardPointMode"><hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="rewardPointModes" value="id" label="name"/>
            </s:select></li>
          <li><label>Expiry
            Date</label><s:text name="expiryDate" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
          </li>
          <li><label>Comment</label><s:textarea name="comment"/></li>
        </ul>
      </fieldset>
      <s:submit name="add" value="Add"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
