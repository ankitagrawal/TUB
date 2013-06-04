<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hk" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.core.Tax" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page import="com.hk.domain.warehouse.Warehouse"%>
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SkuAction" var="skuAction"/>
<%
    TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
  List<Tax> taxList = taxDao.getLocalTaxList();
  pageContext.setAttribute("taxList", taxList);

  WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
  pageContext.setAttribute("whList", warehouseDao.getAll(Warehouse.class));
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Sku">
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SkuAction" var="skuBean"/>
<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {

      /*
       $('.requiredFieldValidatorVariant').click(function() {
       var productVariant = $('.productVariant').val();
       if (productVariant == "") {
       alert("Enter a Product Variant to search.");
       return false;
       }
       });
       *//*
       *//* $('.requiredFieldValidator').click(function() {
       var cutOffInventory = $('.cutOffInventory').val();
       var forecastedAmount = $('.forecastedAmount').val();
       var costPrice = $('.costPrice').val();
       var tax = $('.tax').val();
       alert("cutOffInventory,forecastedAmount,costPrice,tax"+cutOffInventory+forecastedAmount+costPrice+tax);
       if (cutOffInventory == "" || forecastedAmount == "" || costPrice == "" || tax == "") {
       alert("All fields are compulsory.");
       return false;
       }
       if (isNaN(cutOffInventory) || isNaN(forecastedAmount) || isNaN(costPrice)) {
       alert("Cut Off Inventory, Forecasted Amount, Amount Spend and cost price should be numbers.")
       }
       });*/


      $('.requiredFieldValidator').click(function() {
        var category = $('.category').val();
        var brand = $('.brand').val();
        var productId = $('.productId').val();
        var productVariant = $('.productVariant').val();
        var count = 0;
        alert("category,brand,productId,productVariant" + category + brand + productId + productVariant);


        if (category != "")
        {
          count++;
          if (brand != "")
          {
            count++;
            if (productId != "")
            {
              count++;
              if (productVariant != "")
              {
                count++;
              }
            }
          }
        }
        /* else {
         count--;
         }
         */
        alert("count" + count);

        if (count > 1) {
          $('.category').val('');
          $('.brand').val('');
          $('.productId').val('');
          $('.productVariant').val('');
          alert("Please enter only one field .");
          return false;
        }
      });


    });
  </script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>

<s:layout-component name="heading">
  Add/Edit SKUs
</s:layout-component>

<s:layout-component name="content">

  <s:form beanclass="com.hk.web.action.admin.sku.SkuAction">
    <fieldset>
      <legend>Add New SKU</legend>
      <s:submit name="addSku" value="Add a New SKU"/>
    </fieldset>
  </s:form>

  <fieldset class="right_label">
  <legend>Search And Edit SKU</legend>
  <s:form beanclass="com.hk.web.action.admin.sku.SkuAction">

    <label>Category:</label><s:text name="category" class="category"/> /

    <label>Brand:</label><s:text name="brand" class="brand"/> /

    <label>Product Id:</label><s:text name="productId" class="productId"/> /

    <label>VariantID:</label><s:text name="productVariant" class="productVariant"/>

    <s:submit name="searchSKUs" value="Search SKUs"/>

  </s:form>
  <c:set var="skuList" value="${skuAction.skuList}"/>
  <%--<c:choose>--%>
    <%--
        <c:when test="${skuList != null && fn:length(skuList) > 0 }">
    --%>
    <s:form beanclass="com.hk.web.action.admin.sku.SkuAction">
      <table class="zebra_vert">
        <thead>
        <tr>
          <th>ID</th>
          <th>VariantID</th>
          <th>Product</th>
          <th>Warehouse</th>
          <th>Cut Off Inventory</th>
          <th>Forecasted Quantity</th>
          <th>Tax</th>
          <th>Net Inventory</th>
          <th>Booked Qty</th>
          <th>Net Available Unbooked Inventory</th>
        </tr>
        </thead>
        <c:forEach items="${skuList}" var="sku" varStatus="ctr">
          <tr>
            <s:hidden name="skuList[${ctr.index}].id" value="${sku.id}"/>
            <s:hidden name="skuList[${ctr.index}].productVariant" value="${sku.productVariant}"/>
            <s:hidden name="skuList[${ctr.index}].warehouse" value="${sku.warehouse}"/>
            <td>${sku.id}</td>
            <td>${sku.productVariant}</td>
            <td>${sku.productVariant.product.name}<br/>${sku.productVariant.optionsCommaSeparated}</td>
            <td>${sku.warehouse.identifier}</td>
            <td><s:text name="skuList[${ctr.index}].cutOffInventory" value="${sku.cutOffInventory}"
                        class="cutOffInventory"/></td>
            <td><s:text name="skuList[${ctr.index}].forecastedQuantity" value="${sku.forecastedQuantity}"
                        class="forecastedAmount"/></td>
            <td><s:select name="skuList[${ctr.index}].tax" class="tax">
              <s:option value="">-Select-</s:option>
              <c:forEach items="${taxList}" var="tax">
                <s:option value="${tax.id}">${tax.name}</s:option>
              </c:forEach>
            </s:select></td>
            <td>
                ${hk:netInventory(sku)}
            </td>
            <td>
                ${hk:bookedQty(sku)}
            </td>
            <td>
                ${hk:netAvailableUnbookedInventory(sku)}
            </td>
          </tr>
        </c:forEach>
      </table>
      <%--<s:submit name="save" value="Save" class="requiredFieldValidator"/>--%>
      <s:submit name="save" value="Save"/>
      </fieldset>
    </s:form>

    <%--</c:when>--%>
    <%-- <c:otherwise>--%>
    <%--Sorry!! no SKU found. Do you want to make a new entry.--%>

    <%--</c:otherwise>--%>
  <%--</c:choose>--%>

  <%--<s:form beanclass="com.hk.web.action.admin.sku.SkuAction">
  <table class="zebra_vert">
    <thead>
    <tr>
      <th>ID</th>
      <th>VariantID</th>
      <th>Product</th>
      <th>Warehouse</th>
      <th>Cut Off Inventory</th>
      <th>Forecasted Quantity</th>
      <th>Cost Price</th>
      <th>Tax</th>
    </tr>
    </thead>
    <c:forEach items="${skuAction.skuList}" var="sku" varStatus="ctr">
      <tr>
        <s:hidden name="skuList[${ctr.index}].id" value="${sku.id}"/>
        <s:hidden name="skuList[${ctr.index}].productVariant" value="${sku.productVariant}"/>
        <s:hidden name="skuList[${ctr.index}].warehouse" value="${sku.warehouse}"/>
        <td>${sku.id}</td>
        <td>${sku.productVariant}</td>
        <td>${sku.productVariant.product.name}<br/>${sku.productVariant.optionsCommaSeparated}</td>
        <td>${sku.warehouse.city}</td>
        <td><s:text name="skuList[${ctr.index}].cutOffInventory" value="${sku.cutOffInventory}"
                    class="cutOffInventory"/></td>
        <td><s:text name="skuList[${ctr.index}].forecastedQuantity" value="${sku.forecastedQuantity}"
                    class="forecastedAmount"/></td>
        <td><s:text name="skuList[${ctr.index}].costPrice" value="${sku.costPrice}"
                    class="costPrice"/></td>
        <td><s:select name="skuList[${ctr.index}].tax" class="tax">
          <s:option value="">-Select-</s:option>
          <c:forEach items="${taxList}" var="tax">
            <s:option value="${tax.id}">${tax.name}</s:option>
          </c:forEach>
        </s:select></td>
      </tr>
    </c:forEach>
  </table>
  --%><%--<s:submit name="save" value="Save" class="requiredFieldValidator"/>--%><%--
          <s:submit name="save" value="Save"/>
				</s:form>--%>
  <%--
        </c:otherwise>
      </c:choose>
  --%>
</s:layout-component>
</s:layout-render>