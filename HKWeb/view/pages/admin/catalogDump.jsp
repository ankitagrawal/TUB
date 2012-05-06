<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ParseExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.catalog.ParseExcelAction">
      <%--<s:messages key="generalMessages"/>--%>
      <h2>File to Upload
          <%--<s:text name="category"/>--%>
        <s:file name="fileBean" size="30"/></h2>

       <h2>Category/Brand to Upload <s:text name="category" size="30"/></h2>

      <div class="buttons">
        <s:submit name="parse" value="Update"/>
      </div>
    </s:form>

  </s:layout-component>

</s:layout-render>