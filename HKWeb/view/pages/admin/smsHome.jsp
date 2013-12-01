<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.email.SMSHomeAction" var="smsHome" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="SMS Home">

  <s:layout-component name="heading">SMS Home</s:layout-component>

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.email.SMSHomeAction" method="get" autocomplete="false">
      <fieldset class="right_label" style="float:left;">
      <legend>Send SMS</legend>
      <ul>
        <li><label>Bulk SMS?</label>&nbsp;<s:checkbox name="bulkSMS"/>&nbsp;<i>If checked, leave name/mobile fields
          empty.</i></li>
        <li><label>Top Level Category</label><s:select name="topLevelCategory">
          <option value="">All categories</option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList" value="name" label="displayName"/>
        </s:select></li>
        <hr/>
        <li><label>Mobiles<br>(comma
          separated)</label><s:textarea name="mobiles" rows="6" cols="50" style="height:100px"/></li>
        <hr/>
        <li><label>Name</label><s:text name="name"/></li>
        <li><label>Mobile</label><s:text name="mobile"/></li>
        <hr/>
        <li><label>Message<em>*</em></label><s:textarea name="message" rows="9" cols="75" style="height:150px"/></li>


        <div class="buttons"><s:submit name="sendSMS" value="Send SMS"/></div>
        </li>
      </ul>
    </s:form>

  </s:layout-component>
</s:layout-render>


