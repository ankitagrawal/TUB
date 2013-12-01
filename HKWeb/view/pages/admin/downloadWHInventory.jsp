<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <div style="display:inline;">
      <s:form beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction">
        <fieldset>
          <legend><h2>Download WH Inventory Excel</h2></legend>
          <br>
          <br>
          <label width="5"> Primary Category:</label>
           <s:select name="primaryCategory">
                        <option value="">All categories</option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                                   value="name" label="displayName"/>
           </s:select>
          <label width="5"> Sub Category:</label><s:text name="subCategory" class="category" width="125%"/>
          <label width="5">Brand:</label><s:text name="brand" class="brand" />
         <label>UnBooked Inventory Required</label>  <s:checkbox name="unbookedInventoryRequired"/>
          <br>
          <br>
          <br>
          <s:submit name="generateWHInventoryExcel" value="Download WH Snapshot"/>
        </fieldset>
      </s:form>
    </div>
  </s:layout-component>


</s:layout-render>
