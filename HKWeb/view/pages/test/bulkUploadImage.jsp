<%--
  Created by IntelliJ IDEA.
  User: nitin
  Date: 2 Jun, 2011
  Time: 4:49:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadImageAction" var="imageBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadImageAction">
      <%--<s:messages key="generalMessages"/>--%>
       Upload Images.
      <%--<s:file name="fileBean" size="30" />--%>

        <s:submit name="bulkUpload" value="Upload Images"/>
      <%--<s:hidden name="productId" value="${imageBean.productId}"/>--%>
      </s:form>

  </s:layout-component>

</s:layout-render>