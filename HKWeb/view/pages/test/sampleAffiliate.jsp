<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp">

  <s:layout-component name="lhsContent">
    <div class="lhsContent" width="100%">
        <%--"Test it dude", but remember cookie stays for 2 weeks unless u dont touch it, so if u want to test other account,
     use another browser :P--%>
      <br>
      Pratham
      <a href="http://www.indiabookstore.net/redirect?url=http://localhost:8080/healthkart/product/accu-chek-active-test-strip-box/DS001?affid=prathamLP2K33N6">accu-chek-active-test-strip</a>
      <br>
      Ajeet
      <a href="http://localhost:8080/healthkart/diabetes?affid=prathamLP2K33N6">diabetes</a>

      <div class="floatfix"></div>
    </div>

  </s:layout-component>
</s:layout-render>
