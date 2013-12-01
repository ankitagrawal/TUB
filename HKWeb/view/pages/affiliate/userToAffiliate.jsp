<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="lhsContent">
    <jsp:include page="/pages/affiliate/aboutAffiliateProgram.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAction">
      <fieldset class="right_label">
        <%--<legend>Signup Existing User as affiliate</legend>--%>
        <ul>
          <li><label>Email</label><s:text name="email"/></li>
          <li><label>Password</label><s:password name="password"/></li>
          <li><label>remember me</label><s:checkbox name="rememberMe"/></li>
          <li><label>&nbsp;</label>
            <div class="buttons"><s:submit name="loginExisting" value="Become Affiliate"/></div>
          </li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>