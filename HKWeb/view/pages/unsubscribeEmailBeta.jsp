<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>

<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">Email Unsubscribe</s:layout-component>

  <s:layout-component name="centralContent">
    <%--breadcrumbs begins--%>
    <div class="hk-breadcrumb-cntnr mrgn-bt-10">
                <span>
                   <s:link beanclass="com.hk.web.action.HomeAction">Home</s:link>
                </span>
      <span>&raquo;</span>
      <span class="fnt-bold">Email Unsubscribe</span>
    </div>
    <%--breadcrumbs ends--%>
  </s:layout-component>

  <s:layout-component name="lhsContent">
  </s:layout-component>
  <s:layout-component name="rhsContent">
   <%--   <div class="mrgn-l-40 my-acnt-ht">
        <h2 class="strikeline" style="margin-bottom: 10px;"> Email Unsubscribe</h2>
        <s:messages key="generalMessages"/>
      </div>--%>
  </s:layout-component>
</s:layout-render>


