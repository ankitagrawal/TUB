<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Offer Admin</s:layout-component>
  <s:layout-component name="content">
    <div class="roundBox">
      <h3><s:link beanclass="com.hk.web.action.admin.offer.CreateProductGroupAction">Create product groups</s:link></h3>

      <h3><s:link beanclass="com.hk.web.action.admin.offer.CreateOrSelectTriggerAction">Create new offer</s:link></h3>

      <h3><s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">Create coupons</s:link></h3>

      <h3><s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">Edit Offer</s:link></h3>

      <h3><s:link beanclass="com.hk.web.action.admin.offer.EditCouponAction">Edit Coupons</s:link></h3>
    </div>
  </s:layout-component>
</s:layout-render>
