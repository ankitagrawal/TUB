<%--
<%@ page import="com.hk.constants.core.Keys" %>


<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  boolean jqueryCdn = ServiceLocatorFactory.getService(Key.get(Boolean.class, Names.named(Keys.Env.jqueryCdn)));
%>
<s:layout-render name="/layouts/default.jsp" pageTitle="Auto Complete">

  <s:layout-component name="htmlHead">
    <%
      if (jqueryCdn) {
    %>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <%
    } else {
    %>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.1.min.js"></script>
    <%
      }
    %>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.itvCommonPlugins.js"></script>

    <script type="text/javascript">
      $(document).ready(function() {
        $("#searchbox").autocomplete({ url:'http://localhost:8080/healthkart/autocomplete/',
          max: 5
        });
      });
    </script>
  </s:layout-component>

  <s:layout-component name="lhsContent">

    <input type="text" id="searchbox"/>

    --%>
<%--
        <s:form beanclass="com.hk.web.action.test.AutoCompleteAction" id="aucompleteForm" method="get" renderFieldsPresent="false" renderSourcePage="false">
          <h1><s:text name="q" style="width:500px" id="searchBox" autocomplete="off"/></h1>
          <s:text id="search" name="limit"/>
          <s:submit name="pre"/>
        </s:form>
    --%>
<%--

  </s:layout-component>
</s:layout-render>
--%>
