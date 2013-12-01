<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CreateInventoryFileAction" var="creatingFile"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" >

  <s:layout-component name="content">
    <div style="display:inline;">
      <s:form beanclass="com.hk.web.action.admin.inventory.CreateInventoryFileAction">
        <fieldset>
          <legend><h2>Create Inventory File</h2></legend>
          <br>
          <br>
          <label width="5">Brand:</label><s:text name="brand" class="brand" />
          <br>
          <br>
          <br>
          <s:submit name="createInventoryFile" value="Create File For Barcode Printing"/>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:submit name="createDetailedInventoryFile" value="Create Detailed Inventory File "/>
        </fieldset>
      </s:form>
    </div>
  </s:layout-component>


</s:layout-render>
