<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <div style="display:inline;float:left; width:450px">
      <s:form beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">
        <h2> Primary Category to download : <s:text name="category"/></h2>
        <br>
        <s:submit name="generateCatalogByCategory" value="Download Catalog By Primary Category"/>
      </s:form>
    </div>

    <div style="display:inline;">
      <s:form beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">
        <h2> Brand to download : <s:text name="brand"/></h2>
        <br>
        <s:submit name="generateCatalogByBrand" value="Download Catalog By Brand"/>
        <br>
      </s:form>
    </div>
    <br><br>

    <div style="display:inline;float:left; width:450px">
      <s:form beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">
        <h2> Amazon : <s:text name="category"/></h2>
        <br>
        <s:submit name="generateAmazonCatalogByCategory" value="Download Amazon feed By Category"/>
        <br>
      </s:form>
    </div>

    <div style="display:inline;">
      <s:form beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">
        <h2> Sub Category to download : <s:text name="category"/></h2>
        <br>
        <s:submit name="generateCatalogBySubCategory" value="Download Catalog By Sub Category"/>
        <br>
      </s:form>
    </div>
      <%--<div style="display:inline;float:left; width:450px">
            <s:form beanclass="com.hk.web.action.admin.catalog.GenerateExcelAction">
                <br>
              <h2> Download All Categories : </h2>
              <s:submit name="generateCatalogForAllCategories" value="Download Catalog For All Categories"/>
            </s:form>
          </div>--%>
  </s:layout-component>



</s:layout-render>
