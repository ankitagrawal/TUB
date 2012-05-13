<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadComboImageAction" var="imageBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadComboImageAction">
      <%--<s:messages key="generalMessages"/>--%>
      File to Upload for Combo
      <s:file name="fileBean" size="30"/>

      <s:submit name="uploadComboImage" value="Upload Combo Image"/>
      <s:hidden name="combo" value="${imageBean.combo.id}"/>
    </s:form>

  </s:layout-component>

</s:layout-render>