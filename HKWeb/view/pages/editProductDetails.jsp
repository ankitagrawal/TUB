<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="${pa.product.name}">

  <s:layout-component name="menu"> </s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">

    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
       <fieldset>
        <ul>
          <s:hidden name="product"/>
          <li><label>Product ID</label>${pa.product.id}</li>
          <li><label>Product Name</label><s:text name="product.name" style="width: 300px;"/></li>
          <li><label>Categories</label><s:text style="width:200px" name="categories" value="${pa.categories}"/></li>
          <li><label>Sorting</label><s:text name="product.orderRanking"/></li>
          <li><label>Brand</label><s:text name="brand" value="${pa.product.brand}" style="width: 300px;"/></li>
          <li><label>Primary Category</label><s:text name="primaryCategory" value="${pa.product.primaryCategory.displayName}"
                                                     style="width: 300px;" placeholder="Home Devices"/></li>
          <li><label>Secondary Category</label><s:text name="secondaryCategory" value="${pa.product.secondaryCategory.displayName}"
                                                     style="width: 300px;"/></li>
          <li><label>Min Days</label><s:text name="product.minDays"/></li>
          <li><label>Max Days</label><s:text name="product.maxDays"/></li>
          <li><label>Is Deleted</label><s:checkbox name="product.deleted"/></li>
          <li><label>Is Out Of Stock</label><s:checkbox name="product.outOfStock"/></li>
          <li><label>Is Service</label><s:checkbox name="product.service"/></li>
          <li><label>Color Product</label><s:checkbox name="product.productHaveColorOptions"/></li>
          <li><label>Is Google Ad Disallowed</label><s:checkbox name="product.googleAdDisallowed"/></li>
          <li><label>Is Amazon Product</label><s:checkbox name="product.amazonProduct"/></li>
          <li><label>Is Hidden</label><s:checkbox name="product.hidden"/></li>
          <li><label>Is JIT</label><s:checkbox name="product.jit"/></li>
          <li><label>Is Drop Shipping</label><s:checkbox name="product.dropShipping"/></li>  
            <%--//todo ankit, please add the same in bulk edit, take rimals help      --  need to discuss--%>
           <li><label>Is Ground Shipping</label><s:checkbox name="product.groundShipping"/></li>
          <li><label>Is COD Allowed</label><s:checkbox name="product.codAllowed"/></li>

          <li>

            <label>Select Manufacturer</label>
            <s:select name="manufacturer" value="${pa.product.manufacturer.id}">
              <s:option value="">None</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="manufacturerList"
                                         label="name" value="id"/>
            </s:select>
            <br/>
            <br/>
            Manufacturer: ${pa.product.manufacturer.name}
          </li>
          <c:if test="${pa.product.primaryCategory=='services'}">
          <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" target="_blank" event="getManufacturerAddresses">
            Edit/View Manufacturer addresses
            <s:param name="manufacturer" value="${pa.product.manufacturer.id}"/>
            </s:link>
          </c:if>
          <li><label>Supplier</label><s:text name="tin" value="${pa.product.supplier.tinNumber}"/></li>
          <li><label>Video Embed Code</label><s:textarea name="product.videoEmbedCode" style="width: 400px;"/></li>

        </ul>
      </fieldset>

      <div class="buttons">
        <s:submit name="saveProductDetails" value="Save"/>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>