<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Signup</s:layout-component>
  <s:layout-component name="lhsContent">
    <s:form beanclass="com.hk.web.action.core.user.SignupAction">
      Name : <s:text name="name" /><br/>
      Email : <s:text name="email" /><br/>
      Password : <s:password name="password"/><br/>
      Confirm password : <s:password name="passwordConfirm"/><br/>
      Agree to terms <s:checkbox name="agreeToTerms"/><br/>
      <s:submit name="signup"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
