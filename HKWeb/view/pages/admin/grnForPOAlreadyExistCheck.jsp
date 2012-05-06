<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="GRN For PO Already Exist Check">
  <s:layout-component name="heading">
    GRN For PO Already Exist
  </s:layout-component>
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.inventory.POAction" var="poBean"/>
    <div align="center">
    <strong>${fn:length(poBean.purchaseOrder.goodsReceivedNotes)} GRNs for PO#${poBean.purchaseOrder.id} already
      exist.</strong>
    <br/> <br/> <br/>
    <h3>Are you sure you want to create a new one?</h3>
    <br/> <br/> <br/>
    <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="pre" class="button_green" style="background:gray; padding:2px; font-size:.9em">
      No, I'll use the same one
    </s:link>

    <s:link beanclass="com.hk.web.action.admin.inventory.POAction" event="generateGRN" class="button_green">
      <s:param name="purchaseOrder" value="${poBean.purchaseOrder.id}"/>
      Yes, Need to create a new one
    </s:link>
    </div>
  </s:layout-component>
</s:layout-render>