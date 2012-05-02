<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">New Campaign</s:layout-component>
  <s:layout-component name="content">
    Create new campaign

    <s:form beanclass="com.hk.web.action.facebook.app.coupon.FanCouponNewCampaignAction">
      <fieldset>
        <ul>
          <li><label>Name*</label><s:text name="name"/></li>
          <li><label>Code*</label><s:text name="code"/></li>
          <li><label>Url</label><s:text name="url"/></li>
          <li><label>Coupons*</label><s:textarea name="coupons"/></li>
          <li><s:submit name="create" value="Create"/></li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>
