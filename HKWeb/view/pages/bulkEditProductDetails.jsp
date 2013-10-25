<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction" var="bep"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>

<s:layout-component name="content">

<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${bep}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${bep}"/>

<div style="text-align: center;">
  <h2>
    Edit Product Variants for
    <c:if test="${bep.category != null}">
      Category: '${bep.category}'
    </c:if>
    <c:if test="${bep.brand != null}">
      &nbsp;&nbsp;Brand: '${bep.brand}'
    </c:if>
  </h2>
</div>

<div class="errors" style="background-color:salmon; margin-top: 20px; padding: 5px;"></div>

<s:form beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction">
<s:hidden name="category" value="${bep.category}"/>
<s:hidden name="brand" value="${bep.brand}"/>
<s:hidden name="optionObject" value="${bep.optionObject}"/>

<table border="1" class="zebra_vert">
<tr>
  <th>
    Product ID
  </th>

  <th>
    Product Name
  </th>

  <th>
    Categories
  </th>

  <c:if test="${bep.toBeEditedOptions.options['productBrand']}">
    <th>
      Brand
    </th>
  </c:if>

  <th>
    Secondary Category
  </th>

  <th>
    Supplier Tin
  </th>

  <c:if test="${bep.toBeEditedOptions.options['productOrderRanking']}">
    <th>
      Sorting
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productCodAllowed']}">
    <th>
      Is COD Allowed
    </th>
  </c:if>

    <%--<c:if test="${bep.toBeEditedOptions.options['productDeleted']}">--%>
  <th>
    Is Deleted
  </th>
    <%--</c:if>--%>

  <th>
    Is Hidden
  </th>

  <c:if test="${bep.toBeEditedOptions.options['productMinDays']}">
    <th>
      Min Days
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productMaxDays']}">
    <th>
      Max Days
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productColorOptions']}">
    <th width="50px">
      Color Product?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productGoogleAd']}">
    <th width="50px">
      Is Google Ad Disallowed?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productAmazonProduct']}">
    <th width="50px">
      Is Amazon Product?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productService']}">
    <th width="50px">
      Is Service?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productJit']}">
    <th width="50px">
      Is JIT?
    </th>
  </c:if>

</tr>
<c:forEach var="product" items="${bep.products}" varStatus="ctr">

<tr class="productRow">
<s:hidden name="products[${ctr.index}]"/>
<td valign="top">
  <s:text class="productId" name="products[${ctr.index}].id" value="${product.id}" style="width:80px; border: 0"
          readonly="readonly"/>
</td>
<td valign="top">
  <c:if test="${!bep.toBeEditedOptions.options['productName']}">
    ${product.name}
  </c:if>
  <c:if test="${bep.toBeEditedOptions.options['productName']}">
    <s:text name="products[${ctr.index}].name" style="width : 200px"/>
  </c:if>
</td>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions.options['productCategories']}">
    <s:text name="products[${ctr.index}].categoriesPipeSeparated" value="${product.pipeSeparatedCategories}"
            style="width:400px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions.options['productCategories']}">
    <s:text name="products[${ctr.index}].categoriesPipeSeparated" value="${product.pipeSeparatedCategories}"
            style="width:400px"/>
  </c:if>
</td>

<c:if test="${bep.toBeEditedOptions.options['productBrand']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].brand" style="width:70px" class="productBrand"/>
  </td>
</c:if>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions.options['productSecondaryCategory']}">
    <s:text class="productSecondaryCategory" name="secondaryCategory[${ctr.index}]"
            value="${product.secondaryCategory.displayName}"
            style="width: 150px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions.options['productSecondaryCategory']}">
    <s:text class="productSecondaryCategory" name="secondaryCategory[${ctr.index}]"
            value="${product.secondaryCategory.displayName}"
            style="width: 150px;"
            placeholder="eg. Sports Nutrition"/>
  </c:if>
</td>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions.options['productSupplierTin']}">
    <s:text class="productSupplierTin" name="supplierTin[${ctr.index}]"
            value="${product.supplier.tinNumber}"
            style="width: 150px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions.options['productSupplierTin']}">
    <s:text class="productSupplierTin" name="supplierTin[${ctr.index}]"
            value="${product.supplier.tinNumber}"
            style="width: 150px;"/>
  </c:if>
</td>

<c:if test="${bep.toBeEditedOptions.options['productOrderRanking']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].orderRanking" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productCodAllowed']}">
  <td width="60px" align="right" class="productCodAllowed">
    <%--<s:select name="products[${ctr.index}].codAllowed" class="productCodAllowedDropDown">
      <s:option value="${product.codAllowed}" selected="true">${product.codAllowed}</s:option>
      <c:choose>
        <c:when test="${product.codAllowed == true}">
          <s:option value="0" class="false">false</s:option>
        </c:when>
        <c:otherwise>
          <s:option value="1" class="true">true</s:option>
        </c:otherwise>
      </c:choose>
    </s:select>--%>
      ${product.codAllowed}
  </td>
</c:if>

  <%--<c:if test="${bep.toBeEditedOptions.options['productDeleted']}">--%>
<td width="60px" align="right" class="productDeleted">
  <%--<s:select name="products[${ctr.index}].deleted" class="productDeletedDropDown">
    <s:option value="${product.deleted}" selected="true">${product.deleted}</s:option>
    <c:choose>
      <c:when test="${product.deleted == true}">
        <s:option value="0" class="false">false</s:option>
      </c:when>
      <c:otherwise>
        <s:option value="1" class="true">true</s:option>
      </c:otherwise>
    </c:choose>
  </s:select>--%>
    ${product.deleted}
</td>
  <%--</c:if>--%>

<td width="60px" align="right" class="productHidden">
  <s:select name="products[${ctr.index}].hidden" class="productHiddenDropDown">
    <s:option value="${product.hidden}" selected="true">${product.hidden}</s:option>
    <c:choose>
      <c:when test="${product.hidden == true}">
        <s:option value="0" class="false">false</s:option>
      </c:when>
      <c:otherwise>
        <s:option value="1" class="true">true</s:option>
      </c:otherwise>
    </c:choose>
  </s:select>
</td>

<c:if test="${bep.toBeEditedOptions.options['productMinDays']}">
  <td valign="top">
    ${product.minDays}
    <%--<s:text name="products[${ctr.index}].minDays" style="width:70px"/>--%>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productMaxDays']}">
  <td valign="top">
      ${product.maxDays}
    <%--<s:text name="products[${ctr.index}].maxDays" style="width:70px"/>--%>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productColorOptions']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].productHaveColorOptions"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productGoogleAd']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].googleAdDisallowed"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productAmazonProduct']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].amazonProduct"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productService']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].service"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productJit']}">
  <td width="60px" valign="top">
      ${product.jit}
    <%--<s:checkbox name="products[${ctr.index}].jit"/>--%>
  </td>
</c:if>

<td>
<table>
<c:forEach var="variant" items="${product.productVariants}" varStatus="ctrVariant">
<tr>
  <th width="100px" style="text-align:center;">
    Variant ID
  </th>

  <c:if test="${bep.toBeEditedOptions.options['productVariantUpc']}">
    <th width="90px" style="text-align:center;">
      UPC
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantCostPrice']}">
    <th width="80px" style="text-align:center;">
      Cost Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      MRP
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantB2BPrice']}">
    <th width="80px" style="text-align:center;">
      B2B Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      Postpaid Amount
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      HK Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      Discount
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantAffiliateCategory']}">
    <th width="100px" style="text-align:center;">
      Affiliate<br/> Category
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantClearanceSale']}">
    <th width="100px" style="text-align:center;">
      Clearance Sale
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantOutOfStock']}">
    <th width="50px" style="text-align:center;">
      Out of stock
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantDeleted']}">
    <th width="50px" style="text-align:center;">
      Deleted
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantInventory']}">
    <th width="75px" style="text-align:center;">
      INVTRY
    </th>
  </c:if>

    <%--<c:if test="${bep.toBeEditedOptions.options['productVariantCutOffInventory']}">--%>
    <%--<th width="75px" style="text-align:center;">--%>
    <%--Cut-off INVTRY--%>
    <%--</th>--%>
    <%--</c:if>--%>

  <c:if test="${bep.toBeEditedOptions.options['productVariantWeight']}">
    <th style="text-align:center;">Wt.(gm)</th>
  </c:if>

    <%--<c:if test="${bep.toBeEditedOptions.options['productVariantLength']}">--%>
    <%--<th style="text-align:center;">L(cm)</th>--%>
    <%--</c:if>--%>

    <%--<c:if test="${bep.toBeEditedOptions.options['productVariantBreadth']}">--%>
    <%--<th style="text-align:center;">B(cm)</th>--%>
    <%--</c:if>--%>

    <%--<c:if test="${bep.toBeEditedOptions.options['productVariantHeigth']}">--%>
    <%--<th style="text-align:center;">H(cm)</th>--%>
    <%--</c:if>--%>
  <c:if test="${bep.toBeEditedOptions.options['productVariantConsumptionTime']}">
    <th style="text-align:center;">Consumption Time(in days)</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantLeadTime']}">
    <th style="text-align:center;">Laed Time</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantLeadTimeFactor']}">
    <th style="text-align:center;">Lead Time Factor</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantBufferTime']}">
    <th style="text-align:center;">Buffer Time</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantNextAvailDate']}">
    <th style="text-align:center;">Next Available Date</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantFollAvailDate']}">
    <th style="text-align:center;">Following Available Date</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions.options['productVariantHasFreeProductVariant']}">
    <th style="text-align:center;">Free Product Variant</th>
    <th></th>
  </c:if>
</tr>
<tr class="variantRow">
<s:hidden name="products[${ctr.index}].productVariants[${ctrVariant.index}]"/>
<td width="100px">
    ${variant.id}
    ${variant.optionsCommaSeparated}
</td>

<c:if test="${bep.toBeEditedOptions.options['productVariantUpc']}">
  <td width="80px">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].upc" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantCostPrice']}">
  <td width="50px">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].costPrice" size="5"
            style="width:50px"/>
  </td>
</c:if>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions.options['productVariantMRP'] && (bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount'])}">
    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].markedPrice"
              class="markedPrice" size="5" style="width:50px"/>
    </td>
  </c:when>
  <c:when
      test="${bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].markedPrice"
              class="markedPrice" size="5" style="width:50px; border: 0;" readonly="readonly"/>
    </td>
  </c:when>
</c:choose>

<c:if test="${bep.toBeEditedOptions.options['productVariantB2BPrice']}">
  <td width="50px" align="right">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].b2bPrice" size="5"
            style="width:50px"
            class="b2bPrice"/>
  </td>
</c:if>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions.options['productVariantPostpaidAmount'] && (bep.toBeEditedOptions.options['productVariantPostpaidAmount'] || bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount'])}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].postpaidAmount" size="5"
              style="width:50px"
              class="postpaidAmount"/>
    </td>
  </c:when>
  <c:when
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantHKPrice'] || bep.toBeEditedOptions.options['productVariantDiscount']}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].postpaidAmount" size="5"
              style="width:50px; border: 0;" readonly="readonly"
              class="postpaidAmount"/>
    </td>
  </c:when>
</c:choose>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions.options['productVariantHKPrice']}">
    <td width="50px" align="right">
        ${variant.hkPrice}
      <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].hkPrice" size="5"
              style="width:50px"
              class="hkPrice"/>--%>
    </td>

    <td width="50px" align="center">
        ${variant.discountPercent}
      <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].discountPercent"
              class="discountPercent" size="5" style="width:50px; border: 0;" readonly="readonly"/>--%>
    </td>
  </c:when>

  <c:when
      test="${bep.toBeEditedOptions.options['productVariantDiscount']}">
    <td width="50px" align="right">
         ${variant.hkPrice}
      <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].hkPrice" size="5"
              style="width:50px; border: 0;"
              class="hkPrice" readonly="readonly"/>--%>
    </td>

    <td width="50px" align="center">
         ${variant.discountPercent}
      <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].discountPercent"
              class="discountPercent" size="5" style="width:50px"/>--%>
    </td>
  </c:when>

  <c:when
      test="${bep.toBeEditedOptions.options['productVariantMRP'] || bep.toBeEditedOptions.options['productVariantPostpaidAmount']}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].hkPrice" size="5"
              style="width:50px; border: 0;"
              class="hkPrice" readonly="readonly"/>
    </td>

    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].discountPercent"
              class="discountPercent" size="5" style="width:50px; border: 0;" readonly="readonly"/>
    </td>
  </c:when>
</c:choose>

<c:if test="${bep.toBeEditedOptions.options['productVariantAffiliateCategory']}">
  <td width="100px">
    <s:select name="products[${ctr.index}].productVariants[${ctrVariant.index}].affiliateCategory"
              value="${variant.affiliateCategory.affiliateCategoryName}" class="affiliateCategory"
              style="width:100px;">
      <s:option value="">None</s:option>
      <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                 serviceProperty="allAffiliateCategories" label="affiliateCategoryName"
                                 value="affiliateCategoryName"/>
    </s:select><br/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantClearanceSale']}">
  <td width="60px" align="right" class="productVariantClearanceSale">
      <%--<s:text name="productVariantClearanceSale" class="productVariantClearanceSaleText" value="${variant.clearanceSale}"--%>
      <%--readonly="readonly" style="border: 0;"/>--%>
    <s:select name="products[${ctr.index}].productVariants[${ctrVariant.index}].clearanceSale"
              class="productVariantClearanceSaleDropDown">
      <s:option value="${variant.clearanceSale}" selected="selected">${variant.clearanceSale}</s:option>
      <c:choose>
        <c:when test="${variant.clearanceSale == true}">
          <s:option value="0" class="false">false</s:option>
        </c:when>
        <c:otherwise>
          <s:option value="1" class="true">true</s:option>
        </c:otherwise>
      </c:choose>
    </s:select>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantOutOfStock']}">
  <td width="50px" align="right">
    <c:choose>
      <c:when test="${!variant.product.jit}">
        ${variant.outOfStock}
      </c:when>
      <c:otherwise>
        <s:checkbox name="products[${ctr.index}].productVariants[${ctrVariant.index}].outOfStock"/>
      </c:otherwise>
    </c:choose>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantDeleted']}">
  <td width="60px" align="right">
       ${variant.deleted}
    <%--<s:checkbox name="products[${ctr.index}].productVariants[${ctrVariant.index}].deleted"/>--%>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantInventory']}">
  <td width="100px" align="right">
    <s:link beanclass="com.hk.web.action.admin.inventory.ListBatchesAndCheckinInventory" target="_blank">
      <strong>${hk:netInventoryAtServiceableWarehouses(variant)}</strong>
      <s:param name="upc" value="${variant.id}"/>
    </s:link>
  </td>
</c:if>

  <%--<c:if test="${bep.toBeEditedOptions.options['productVariantCutOffInventory']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].cutOffInventory" size="2"--%>
  <%--style="width:30px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

<c:if test="${bep.toBeEditedOptions.options['productVariantWeight']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].weight" size="2"
            style="width:40px"/>
  </td>
</c:if>

  <%--<c:if test="${bep.toBeEditedOptions.options['productVariantLength']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].length" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

  <%--<c:if test="${bep.toBeEditedOptions.options['productVariantBreadth']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].breadth" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

  <%--<c:if test="${bep.toBeEditedOptions.options['productVariantHeigth']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].height" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

<c:if test="${bep.toBeEditedOptions.options['productVariantConsumptionTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].consumptionTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantLeadTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].leadTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantLeadTimeFactor']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].leadTimeFactor" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantBufferTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].bufferTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantNextAvailDate']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].nextAvailableDate" class="date_input"
            formatPattern="yyyy-MM-dd" style="width:100px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantFollAvailDate']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].followingAvailableDate"
            class="date_input"
            formatPattern="yyyy-MM-dd" style="width:100px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions.options['productVariantHasFreeProductVariant']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].freeProductVariant"
            class="freeVariant"/>
  </td>
  <td>
    <s:label name=" " id="variantDetails"/>
  </td>
</c:if>

</tr>
</c:forEach>
</table>
</td>
</tr>
</c:forEach>
</table>
<br/>

<div class="buttons">
  <s:submit name="save" value="Save" class="submitButton"/>
  <s:submit name="pre" value="Back"/>
</div>
<s:link id="variantInfoLink" beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction"
        event="getPVDetails" style="display:none;"> </s:link>
<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${bep}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${bep}"/>
</s:form>
</s:layout-component>
</s:layout-render>

<script type="text/javascript">
  $(document).ready(function () {
    $('.errors').hide();
    if (!($('.discountPercent').attr("readonly") == "readonly")) {
      $('.discountPercent').change(_updateHkPrice);
    }
    $('.markedPrice').change(_updateHkPrice);
    $('.postpaidAmount').change(_updateHkPrice);
    if (!($('.hkPrice').attr("readonly") == "readonly")) {
      $('.hkPrice').change(_updateDiscountonMrp);
    }

    $('.submitButton').click(function () {
      $('.errors').empty();
      var ctr = 0;
      $('.productSecondaryCategory').each(function () {
        if ($(this).val().trim() === "") {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter secondary category for: " + productId);
        }
      });
      /*$('.productBrand').each(function () {
        if ($(this).val().trim() === "") {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter brand for: " + productId);
        }
      });*/
      $('.productSupplierTin').each(function () {
        var isDeleted = $(this).parents('.productRow').find('.productDeletedDropDown').val();
        if ($(this).val().trim() === "" && isDeleted == 'false') {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter supplier's tin number for: " + productId);
        }
      });

      if (ctr) {
        alert("Errors encountered! Kindly go to the top of the page to view.");
        $('.errors').show();
      }
      return !ctr;
    });

    if ($('.freeVariant')) {
      $('.freeVariant').change(_validateProductVariant);
      $('.freeVariant').focusout(_validateProductVariant);
      //      $('.freeVariant').mouseup(_validateProductVariant);
    }
  });

  function _updateHkPrice(e) {
    var variantRow = $(e.target).parents('.variantRow');
    variantRow.find('.hkPrice').val((variantRow.find('.markedPrice').val() * (1 - variantRow.find('.discountPercent').val())) - variantRow.find('.postpaidAmount').val());
  }

  function _updateDiscountonMrp(e) {
    var variantRow = $(e.target).parents('.variantRow');
    variantRow.find('.discountPercent').val(1 - (Math.round(variantRow.find('.hkPrice').val()) + Math.round(variantRow.find('.postpaidAmount').val())) / Math.round((variantRow.find('.markedPrice').val())));
  }

  function _validateProductVariant(e) {
    var variantRow = $(e.target).parents('.variantRow');
    var variantIdText = variantRow.find('.freeVariant');
    var variantId = variantIdText.val().trim();
    var variantDetailsLabel = variantRow.find('#variantDetails');
    if (variantId != "") {
      $.getJSON(
          $('#variantInfoLink').attr('href'), {productVariantId: variantId},
          function (res) {
            if (res.code == "<%=HealthkartResponse.STATUS_OK%>") {
              variantDetailsLabel.html(
                  res.data.product + '<br/>' +
                      res.data.options
              );
            } else {
              variantDetailsLabel.html(res.message).css({color: 'red'});
              variantIdText.val('');
            }
          });
    }
  }

</script>