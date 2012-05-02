<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.ParseExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="web.action.admin.ParseDTDCDeliveryStatusExcelAction">
      <%--<s:messages key="generalMessages"/>--%>
      <h2>File to Upload
          <%--<s:text name="category"/>--%>
        <s:file name="fileBean" size="30"/></h2>

      <div class="buttons">
        <s:submit name="parse" value="Update"/>
      </div>
    </s:form>

  </s:layout-component>

</s:layout-render>