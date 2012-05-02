<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.RelatedProductAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Update Related/Cross Products through Excel">

  <s:layout-component name="content">
    <s:form beanclass="web.action.admin.RelatedProductAction">
      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>

      <div class="buttons">
        <s:submit name="parse" value="Update"/>
      </div>
    </s:form>

  </s:layout-component>

</s:layout-render>