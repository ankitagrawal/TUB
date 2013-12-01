<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Update Reconciliation Status">
  <s:layout-component name="htmlHead">

  </s:layout-component>
  <s:layout-component name="heading">Update Reconciliation Status</s:layout-component>
  <s:layout-component name="content">
    <div style="display:inline;float:left;">
      <h2>Update Reconciliation using Excel</h2>
      <br/>
      <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationAction">
        <%--<s:messages key="generalMessages"/>--%>
        <h2>File to Upload
            <%--<s:text name="category"/>--%>
          <s:file name="fileBean" size="30"/></h2>

        <div class="buttons">
          <s:submit name="parse" value="Update"/>
        </div>
      </s:form>

    </div>

  </s:layout-component>
</s:layout-render>