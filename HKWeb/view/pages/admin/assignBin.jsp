<%@ page import="com.hk.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.AssignBinAction" var="assignBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Assign Bin">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.AssignBinAction">
      <fieldset>
        <ul>
          <s:hidden name="bin"/>
          <li><label>Aisle</label><s:text name="bin.aisle"/></li>
          <li><label>Rack</label><s:text name="bin.rack" style="width: 300px;"/></li>
          <li><label>Shelf</label><s:text name="bin.shelf"/></li>
        </ul>
        <ul>
          <li><label>Category</label><s:text name="category"/></li>
          <li><label>Brand</label><s:text name="brand"/></li>
          <li><label>Product</label><s:text name="product"/></li>
          <li><label>Product Variant</label><s:text name="productVariant"/></li>
        </ul>
        <ul>
          <li><label>OverRide</label><s:checkbox name="override"/></li>
        </ul>
      </fieldset>
      <div class="buttons">
        <s:submit name="assignBinByCategory" value="Assign Bin By Category"/>
        <s:submit name="assignBinByBrand" value="Assign Bin By Brand"/>
        <s:submit name="assignBinToProduct" value="Assign Bin To Product"/>
        <s:submit name="assignBinToVariant" value="Assign Bin To Variant"/>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>