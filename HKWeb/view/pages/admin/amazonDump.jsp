<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.AmazonParseExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="web.action.admin.AmazonParseExcelAction">
      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>

       <h2>Amazon Category to Upload <s:text name="category" size="30"/></h2>

      <div class="buttons">
        <s:submit name="parse" value="Update"/>
      </div>
    </s:form>

  </s:layout-component>

</s:layout-render>