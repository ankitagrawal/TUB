<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.CreateOrSelectTriggerAction" var="triggerBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Step 1: Create / Select offer trigger</s:layout-component>
  <s:layout-component name="content">
    <h3>
      OR <s:link beanclass="com.hk.web.action.admin.offer.CreateOrSelectOfferActionAction">Skip to offer action</s:link>
    </h3>
    <s:errors/>
    <s:form beanclass="com.hk.web.action.admin.offer.CreateOrSelectTriggerAction">
      <fieldset class="right_label">
        <legend>Select an existing trigger</legend>
        <ul>
          <li>
            <label>Trigger</label>
            <s:select name="offerTriggerSelect">
              <s:options-collection collection="${triggerBean.offerTriggerList}" label="description" value="id"/>
            </s:select>
          </li>
          <li><label>&nbsp;</label><div class="buttons"><s:submit name="select" value="Select Trigger"/></div></li>
        </ul>
      </fieldset>
    </s:form>
    <s:form beanclass="com.hk.web.action.admin.offer.CreateOrSelectTriggerAction">
      <fieldset class="right_label">
        <legend>Create a new trigger</legend>
        <ul>
          <li><label>Description</label><s:text name="offerTriggerCreate.description"/></li>
          <li><label>Product Group</label>
            <s:select name="offerTriggerCreate.productGroup">
              <s:option label="n.a." value=""/>
              <s:options-collection collection="${triggerBean.productGroupList}" label="name" value="id"/>
            </s:select>
          </li>
          <li><label>Trigger Amount</label><s:text name="offerTriggerCreate.amount"/></li>
          <li><label>Trigger Qty</label><s:text name="offerTriggerCreate.qty"/></li>
          <li><label>&nbsp;</label><div class="buttons"><s:submit name="create" value="Create New Trigger"/></div></li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>
