<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment Failed">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">
    <div style="margin-top: 50px;">
      <h2 class="red" style="font-size:17px;">
        Oops! Your Transaction could not be processed.
      </h2>
    </div>
  </s:layout-component>

  <s:layout-component name="left_col">
    <p>Please try again.</p>
    <ul type="disc" style="margin-left:20px; list-style:disc;">
            <li>For any other query read our <a href="/pages/termsAndConditions.jsp">Terms &amp; Conditions</a></li>
            <li>For any other assistance pertaining to your order contact our customer care at 0124-4551616 or at email:  <a href="mailto:info@healthkart.com">info@healthkart.com</a></li>
          </ul>
    
  </s:layout-component>

</s:layout-render>
