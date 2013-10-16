<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Payment Failed">

  <%--<s:layout-component name="menu"> </s:layout-component>--%>
  <s:layout-component name="heading">Oops! Your Transaction could not be processed</s:layout-component>

  <s:layout-component name="left_col">
    <p><s:link beanclass="com.hk.web.action.core.payment.PaymentModeAction" class="txt-blue fnt-sz14 mrgn-b-5">Please Try Again</s:link></p>
    <ul type="disc" style="margin-left:20px; list-style:disc;line-height: 20px;margin-top: 5px;margin-bottom: 5px;">
      <li>For any other query read our <a href="/beta/hk/TermsConditions.action" class="txt-blue">Terms &amp; Conditions</a></li>
      <li>For any other assistance pertaining to your order contact our customer care at 0124-4616444 or at email: <a
          href="mailto:info@healthkart.com" class="txt-blue">info@healthkart.com</a></li>
    </ul>

    <a href="/" class="btn btn-blue" style="width: 250px;float: right;margin-top: 10px;">GO BACK TO HEALTHKART.COM</a>
    <div class="floatfix" style="margin-bottom: 40px;"></div>

  </s:layout-component>

</s:layout-render>
