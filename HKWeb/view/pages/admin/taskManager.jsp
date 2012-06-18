<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Run Ant tasks">
  <s:useActionBean beanclass="com.hk.web.action.admin.TaskManagerAction" var="taskManagerBean"/>
  <s:layout-component name="heading">
    Run Ant Tasks
  </s:layout-component>

  <s:layout-component name="content">
    <fieldset style="float:left;" id="db-master">
      <label>DB Master</label>
      <s:form beanclass="com.hk.web.action.admin.TaskManagerAction">
      <table>
        <tr>
          <td>
            <s:select name="db_master_service">
              <s:option value="static">Master</s:option>
<%--
              <s:option value="catalog">Catalog</s:option>
              <s:option value="both">Both</s:option>
--%>
            </s:select>
          </td>
        </tr>
        <tr>
          <td><s:submit name="db_master" value="DB Master"/></td>
        </tr>
        </s:form>
      </table>
    </fieldset>
  </s:layout-component>
</s:layout-render>