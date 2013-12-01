<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Create Product Group</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.offer.CreateProductGroupAction">
      <fieldset class="right_label">
        <s:errors/>
        <legend>Product Group Details</legend>
        <ul>
          <li><label>Name</label><s:text name="name"/></li>
          <li><label>Category Names</label><s:text name="categoryNames" placeholder="Eg. blood-pressure, respiratory-care"/> </li>
          <li><label>Product Ids</label><s:text name="productIds" placeholder="Eg. D001, E002"/></li>
          <li><label>Product Variant Ids</label><s:text name="productVariantIds" placeholder="Eg. D001-02, E002-01"/></li>
          <li><label>Exclude Variant Ids</label><s:text name="excludeProductVariantIds"/></li>
          <li><label>Exclude Category</label><s:text name="excludedCategoryNames" placeholder="Eg. diapers, baby-food"/></li>
          <li><label>&nbsp;</label><div class="buttons"><s:submit name="create" value="Create product group"/></div></li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>
