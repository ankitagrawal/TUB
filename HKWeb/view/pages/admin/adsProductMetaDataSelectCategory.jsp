<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Enter category</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.marketing.AdsProductMetaDataAction" target="_blank">
      Category : <s:text name="category"/>
      <div class="buttons"><s:submit name="reportCategory"/></div>
    </s:form>
    <s:form beanclass="com.hk.web.action.admin.marketing.AdsProductMetaDataAction" target="_blank">
      Brand : <s:text name="brand"/>
      <div class="buttons"><s:submit name="reportBrand"/></div>
    </s:form>
  </s:layout-component>
</s:layout-render>
