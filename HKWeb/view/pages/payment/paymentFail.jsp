<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment Failed">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">
    <div style="margin-top: 50px;">
      <h2 class="red">
        Payment Unsuccessful
      </h2>
    </div>
  </s:layout-component>

  <s:layout-component name="left_col">
    <p>Your payment has failed. Please try again.</p>
    <h2>Support</h2>
    <p><s:link beanclass="com.hk.com.hk.web.action.pages.ContactAction">Write to us</s:link> if you have any questions.</p>
  </s:layout-component>

</s:layout-render>
