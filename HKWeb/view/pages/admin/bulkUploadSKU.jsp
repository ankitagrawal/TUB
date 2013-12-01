<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SkuParseExcelAction" var="skuExcelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Upload SKU Excel">

  <s:layout-component name="content">

    <fieldset>
      <legend>
        <h1> Download SKU Excel </h1>
      </legend>
      <br>
      <s:form beanclass="com.hk.web.action.admin.sku.SkuParseExcelAction">
        <label>Category :</label><s:text name="category" size="30"/>
        <label>Brand :</label><s:text name="brand" size="30"/>
        <s:submit name="generateSkuExcel" value="Download Excel"/>
      </s:form>
    </fieldset>
    <br>
    <br>
    <fieldset>
      <legend>
        <h1> Upload SKUs by Excel </h1>
      </legend>
      <br>
      <s:form beanclass="com.hk.web.action.admin.sku.SkuParseExcelAction">
        <label>File to Upload:</label><s:file name="fileBean" size="30"/>
        <br>
        <label>Category to Upload:</label><s:text name="category" size="30"/>
        <s:submit name="parse" value="Update"/>
      </s:form>

      <p>Columns in Excel - PRODUCT_VARIANT_ID, WAREHOUSE_ID, TAX_ID, CUT_OFF_INVENTORY, FORECASTED_QUANTITY </p>
    </fieldset>


    </div>

  </s:layout-component>

</s:layout-render>