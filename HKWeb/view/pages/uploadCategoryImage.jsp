<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.UploadCategoryImageAction" var="categoryImageBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

 <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.UploadCategoryImageAction">
      <%--<s:messages key="generalMessages"/>--%>
      <strong>
        ${categoryImageBean.errorMessage}
      </strong>
        File to Upload
      <s:file name="fileBean" size="30" />

        <s:submit name="uploadCategoryImage" value="Upload Image"/>
      <s:hidden name="categoryName" value="${categoryImageBean.category.name}"/>
      <%--<input type="text" value="${imageBean.product.id}"/>--%>
      </s:form>
    <%--<s:link href="/pages/test/bulkUploadImage.jsp">Upload in Bulk</s:link>--%>

 </s:layout-component>
</s:layout-render>