<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" var="imageBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadImageAction">
      <%--<s:messages key="generalMessages"/>--%>
        File to Upload
      <s:file name="fileBean" size="30" />

        <s:submit name="uploadProductImage" value="Upload Image"/>
      <s:hidden name="product" value="${imageBean.product.id}"/>
      </s:form>
    <%--<s:link href="/pages/test/bulkUploadImage.jsp">Upload in Bulk</s:link>--%>

  </s:layout-component>

</s:layout-render>