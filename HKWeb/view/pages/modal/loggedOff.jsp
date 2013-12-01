<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/modal.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">Session Expired!</s:layout-component>

  <s:layout-component name="content">
    <p class="lrg">Your session has expired. Please <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="loggedOffLoginLink">Login</s:link> again to continue.</p>

    <script type="text/javascript">
      var params = {};
      params['<%=J2EESecurityManager.redirectAfterLoginParam%>'] = window.location.href;
      $('.loggedOffLoginLink').attr('href', $('.loggedOffLoginLink').attr('href')+'?'+$.param(params));
    </script>
  </s:layout-component>

</s:layout-render>
