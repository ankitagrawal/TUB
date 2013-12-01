<%@ page import="com.hk.domain.inventory.LowInventory" %>
<%@ page import="com.hk.domain.sku.Sku" %>
<%@ page import="com.hk.pact.service.inventory.SkuService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction" var="niaBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Low Inventory List">

  <%
      SkuService skuService = ServiceLocatorFactory.getService(SkuService.class);
  %>

  <s:layout-component name="htmlHead">
    <%--<script type="text/javascript">--%>
    <%--$(document).ready(function() {--%>
    <%--$('#liveSearchBox').keyup(function() {--%>
    <%--var searchString = $(this).val().toLowerCase();--%>
    <%--$('.productVariantRow').each(function() {--%>
    <%--if ($(this).find('.basketCategory').text().toLowerCase().indexOf(searchString) == -1) {--%>
    <%--$(this).hide();--%>
    <%--} else {--%>
    <%--$(this).show();--%>
    <%--}--%>
    <%--});--%>
    <%--});--%>

    <%--$('#liveSearchBox2').keyup(function() {--%>
    <%--var searchString = $(this).val().toLowerCase();--%>
    <%--$('.productVariantRow').each(function() {--%>
    <%--if ($(this).find('.outOfStock').text().toLowerCase().indexOf(searchString) == -1) {--%>
    <%--$(this).hide();--%>
    <%--} else {--%>
    <%--$(this).show();--%>
    <%--}--%>
    <%--});--%>
    <%--});--%>
    <%--});--%>
    <%--</script>--%>
    <style type="text/css">
      table tr th {
        text-align: left;
      }
    </style>
  </s:layout-component>
  <s:layout-component name="heading">Low Inventory List</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction" autocomplete="off">
      <fieldset>
        <legend>Search List</legend>

        <label>Category Name:</label><s:text name="categoryName" style="width:150px"/>
        &nbsp; &nbsp;
        <label>ProductVariant Id:</label><s:text name="productVariant" style="width:150px"/>
        &nbsp; &nbsp;
        <label>Product Id:</label><s:text name="product" style="width:150px"/>
        &nbsp; &nbsp;
        <label>Brand:</label><s:text name="brandName" style="width:150px"/>

        <s:submit name="pre" value="Search"/>
      </fieldset>
    </s:form>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${niaBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${niaBean}"/>

    <div class="clear"></div>
    <s:form beanclass="com.hk.web.action.admin.inventory.InventoryHealthStatusAction">

      <table border="1">
        <tr>
          <th>S.No.</th>
          <th>Category</th>
          <th>Product/Product Name</th>
          <th>Product Variant</th>
          <th>Cutoff Inventory</th>
          <th>Booked Qty</th>
          <th>Inventory</th>
          <th>Out of Stock</th>
          <th>Deleted</th>
          <th>Date</th>
        </tr>
        <c:forEach var="lowInventory" items="${niaBean.lowInventories}" varStatus="ctr">
          <%
            LowInventory lowInventory = (LowInventory) pageContext.getAttribute("lowInventory");
            List<Sku> skus = skuService.getSKUsForProductVariant(lowInventory.getProductVariant());
            pageContext.setAttribute("skus", skus);
          %>

          <tr>
            <td>${ctr.index+1}.</td>
            <td valign="top">
                ${hk:topLevelCategory(lowInventory.productVariant.product)}
            </td>
            <td valign="top">
                ${lowInventory.productVariant.product.id} - ${lowInventory.productVariant.product.name}
            </td>

            <td>
                ${lowInventory.productVariant.id}
              <c:forEach items="${lowInventory.productVariant.productOptions}"
                         var="option"><br>${option.name}:${option.value}
              </c:forEach>
            </td>
            <td><c:forEach items="${skus}" var="sku">${sku.warehouse.city}:${sku.cutOffInventory}<br></c:forEach></td>
            <td>${hk:bookedQty(lowInventory.productVariant, null)}</td>
            <td>${hk:netInventory(lowInventory.productVariant)}</td>
            <td>LI:${lowInventory.outOfStock}<br>PV:${lowInventory.productVariant.outOfStock}</td>
            <td>${lowInventory.productVariant.deleted}</td>
            <td><fmt:formatDate value="${lowInventory.entryDate}" pattern="dd/MM/yyyy HH:mm"/></td>

          </tr>
        </c:forEach>
      </table>

    </s:form>
  </s:layout-component>
</s:layout-render>