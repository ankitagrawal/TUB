<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.InventoryHealthStatusAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <%--<div style="display:inline;">
      <s:form beanclass="web.action.admin.InventoryHealthStatusAction">
        <h2><label>Category:</label><s:text name="category" class="category"/></h2>

        <h2><label>Brand:</label><s:text name="brand" class="brand"/></h2>
        <br>
        <s:submit name="generateWHInventoryExcel" value="Download WH Snapshot By Category"/>
        <br>
      </s:form>
    </div>
    <br/>--%>

    <div style="display:inline;">
      <s:form beanclass="web.action.admin.InventoryHealthStatusAction">
        <fieldset>
          <legend><h2>Download WH Inventory Excel</h2></legend>
          <br>
          <br>
          <label width="5">Category:</label><s:text name="category" class="category" />

          <label width="5">Brand:</label><s:text name="brand" class="brand" />
          <br>
          <br>
          <br>
          <s:submit name="generateWHInventoryExcel" value="Download WH Snapshot"/>
        </fieldset>
      </s:form>
    </div>
  </s:layout-component>


</s:layout-render>
