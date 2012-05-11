<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="mhc.web.action.admin.BulkEditProductAction" var="bep"/>

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

<div class="errors" style="background-color:salmon; margin-top: 20px; padding: 5px;">

</div>

<s:form beanclass="mhc.web.action.admin.BulkEditProductAction">
<s:hidden name="category" value="${bep.category}"/>
<s:hidden name="brand" value="${bep.brand}"/>
<s:hidden name="toBeEditedOptionsObject" value="${bep.toBeEditedOptionsObject}"/>

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

  <c:if test="${bep.toBeEditedOptions['productBrand']}">
    <th>
      Brand
    </th>
  </c:if>

  <th>
    Secondary Category
  </th>

  <c:if test="${bep.toBeEditedOptions['productSupplierTin']}">
    <th>
      Supplier Tin
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productOrderRanking']}">
    <th>
      Sorting
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productDeleted']}">
    <th>
      Deleted
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productMinDays']}">
    <th>
      Min Days
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productMaxDays']}">
    <th>
      Max Days
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productColorOptions']}">
    <th width="50px">
      Color Product?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productGoogleAd']}">
    <th width="50px">
      Is Google Ad Disallowed?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productAmazonProduct']}">
    <th width="50px">
      Is Amazon Product?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productService']}">
    <th width="50px">
      Is Service?
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productJit']}">
    <th width="50px">
      Is JIT?
    </th>
  </c:if>

</tr>
<c:forEach var="product" items="${bep.products}" varStatus="ctr">

<tr class="productRow">
<s:hidden name="products[${ctr.index}]"/>
<td valign="top">
  <s:text class="productId" name="products[${ctr.index}].id" value="${product.id}" style="width:60px; border: 0"
          readonly="readonly"/>
</td>
<td valign="top">
  <c:if test="${!bep.toBeEditedOptions['productName']}">
    ${product.name}
  </c:if>
  <c:if test="${bep.toBeEditedOptions['productName']}">
    <s:text name="products[${ctr.index}].name" style="width : 200px"/>
  </c:if>
</td>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions['productCategories']}">
    <s:text name="products[${ctr.index}].categoriesPipeSeparated" value="${product.pipeSeparatedCategories}"
            style="width:400px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions['productCategories']}">
    <s:text name="products[${ctr.index}].categoriesPipeSeparated" value="${product.pipeSeparatedCategories}"
            style="width:400px"/>
  </c:if>
</td>

<c:if test="${bep.toBeEditedOptions['productBrand']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].brand" style="width:70px" class="productBrand"/>
  </td>
</c:if>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions['productSecondaryCategory']}">
    <s:text class="productSecondaryCategory" name="secondaryCategory[${ctr.index}]"
            value="${product.secondaryCategory.displayName}"
            style="width: 150px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions['productSecondaryCategory']}">
    <s:text class="productSecondaryCategory" name="secondaryCategory[${ctr.index}]"
            value="${product.secondaryCategory.displayName}"
            style="width: 150px;"
            placeholder="eg. Sports Nutrition"/>
  </c:if>
</td>

<td valign="top">
  <c:if test="${!bep.toBeEditedOptions['productSupplierTin']}">
    <s:text class="productSupplierTin" name="supplierTin[${ctr.index}]"
            value="${product.supplier.tinNumber}"
            style="width: 150px; border: 0" readonly="readonly"/>
  </c:if>
  <c:if test="${bep.toBeEditedOptions['productSupplierTin']}">
    <s:text class="productSupplierTin" name="supplierTin[${ctr.index}]"
            value="${product.supplier.tinNumber}"
            style="width: 150px;"/>
  </c:if>
</td>

<c:if test="${bep.toBeEditedOptions['productOrderRanking']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].orderRanking" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productDeleted']}">
  <td width="60px" align="right" class="productDeleted">
      <%--<s:text name="productDeleted" class="productDeletedText" value="${product.deleted}"--%>
      <%--readonly="readonly" style="border: 0;"/>--%>
      <%--<span class="productDeletedMessage">click on text to edit</span>--%>
    <s:select name="products[${ctr.index}].deleted" class="productDeletedDropDown">
      <s:option value="${product.deleted}" selected="true">${product.deleted}</s:option>
      <c:choose>
        <c:when test="${product.deleted == true}">
          <s:option value="0" class="false">false</s:option>
        </c:when>
        <c:otherwise>
          <s:option value="1" class="true">true</s:option>
        </c:otherwise>
      </c:choose>
    </s:select>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productMinDays']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].minDays" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productMaxDays']}">
  <td valign="top">
    <s:text name="products[${ctr.index}].maxDays" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productColorOptions']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].productHaveColorOptions"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productGoogleAd']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].googleAdDisallowed"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productAmazonProduct']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].amazonProduct"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productService']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].service"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productJit']}">
  <td width="60px" valign="top">
    <s:checkbox name="products[${ctr.index}].jit"/>
  </td>
</c:if>

<td>
<table>
<c:forEach var="variant" items="${product.productVariants}" varStatus="ctrVariant">
<tr>
  <th width="100px" style="text-align:center;">
    Variant ID
  </th>

  <c:if test="${bep.toBeEditedOptions['productVariantUpc']}">
    <th width="90px" style="text-align:center;">
      UPC
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantCostPrice']}">
    <th width="80px" style="text-align:center;">
      Cost Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      MRP
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantB2BPrice']}">
    <th width="80px" style="text-align:center;">
      B2B Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      Postpaid Amount
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      HK Price
    </th>
  </c:if>

  <c:if
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <th width="80px" style="text-align:center;">
      Discount
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantAffiliateCategory']}">
    <th width="100px" style="text-align:center;">
      Affiliate<br/> Category
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantClearanceSale']}">
    <th width="100px" style="text-align:center;">
      Clearance Sale
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantOutOfStock']}">
    <th width="50px" style="text-align:center;">
      Out of stock
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantDeleted']}">
    <th width="50px" style="text-align:center;">
      Deleted
    </th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantInventory']}">
    <th width="75px" style="text-align:center;">
      INVTRY
    </th>
  </c:if>

    <%--<c:if test="${bep.toBeEditedOptions['productVariantCutOffInventory']}">--%>
    <%--<th width="75px" style="text-align:center;">--%>
    <%--Cut-off INVTRY--%>
    <%--</th>--%>
    <%--</c:if>--%>

  <c:if test="${bep.toBeEditedOptions['productVariantWeight']}">
    <th style="text-align:center;">Wt.(gm)</th>
  </c:if>

    <%--<c:if test="${bep.toBeEditedOptions['productVariantLength']}">--%>
    <%--<th style="text-align:center;">L(cm)</th>--%>
    <%--</c:if>--%>

    <%--<c:if test="${bep.toBeEditedOptions['productVariantBreadth']}">--%>
    <%--<th style="text-align:center;">B(cm)</th>--%>
    <%--</c:if>--%>

    <%--<c:if test="${bep.toBeEditedOptions['productVariantHeigth']}">--%>
    <%--<th style="text-align:center;">H(cm)</th>--%>
    <%--</c:if>--%>
  <c:if test="${bep.toBeEditedOptions['productVariantConsumptionTime']}">
    <th style="text-align:center;">Consumption Time(in days)</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantLeadTime']}">
    <th style="text-align:center;">Laed Time</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantLeadTimeFactor']}">
    <th style="text-align:center;">Lead Time Factor</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantBufferTime']}">
    <th style="text-align:center;">Buffer Time</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantNextAvailDate']}">
    <th style="text-align:center;">Next Available Date</th>
  </c:if>

  <c:if test="${bep.toBeEditedOptions['productVariantFollAvailDate']}">
    <th style="text-align:center;">Following Available Date</th>
  </c:if>
</tr>
<tr class="variantRow">
<s:hidden name="products[${ctr.index}].productVariants[${ctrVariant.index}]"/>
<td width="100px">
    ${variant.id}
    ${variant.optionsCommaSeparated}
</td>

<c:if test="${bep.toBeEditedOptions['productVariantUpc']}">
  <td width="80px">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].upc" style="width:70px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantCostPrice']}">
  <td width="50px">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].costPrice" size="5"
            style="width:50px"/>
  </td>
</c:if>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions['productVariantMRP'] && (bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount'])}">
    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].markedPrice"
              class="markedPrice" size="5" style="width:50px"/>
    </td>
  </c:when>
  <c:when
      test="${bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].markedPrice"
              class="markedPrice" size="5" style="width:50px; border: 0;" readonly="readonly"/>
    </td>
  </c:when>
</c:choose>

<c:if test="${bep.toBeEditedOptions['productVariantB2BPrice']}">
  <td width="50px" align="right">
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].b2bPrice" size="5"
            style="width:50px"
            class="b2bPrice"/>
  </td>
</c:if>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions['productVariantPostpaidAmount'] && (bep.toBeEditedOptions['productVariantPostpaidAmount'] || bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount'])}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].postpaidAmount" size="5"
              style="width:50px"
              class="postpaidAmount"/>
    </td>
  </c:when>
  <c:when
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantHKPrice'] || bep.toBeEditedOptions['productVariantDiscount']}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].postpaidAmount" size="5"
              style="width:50px; border: 0;" readonly="readonly"
              class="postpaidAmount"/>
    </td>
  </c:when>
</c:choose>

<c:choose>
  <c:when
      test="${bep.toBeEditedOptions['productVariantHKPrice']}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].hkPrice" size="5"
              style="width:50px"
              class="hkPrice"/>
    </td>

    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].discountPercent"
              class="discountPercent" size="5" style="width:50px; border: 0;" readonly="readonly"/>
    </td>
  </c:when>

  <c:when
      test="${bep.toBeEditedOptions['productVariantDiscount']}">
    <td width="50px" align="right">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].hkPrice" size="5"
              style="width:50px; border: 0;"
              class="hkPrice" readonly="readonly"/>
    </td>

    <td width="50px" align="center">
      <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].discountPercent"
              class="discountPercent" size="5" style="width:50px"/>
    </td>
  </c:when>

  <c:when
      test="${bep.toBeEditedOptions['productVariantMRP'] || bep.toBeEditedOptions['productVariantPostpaidAmount']}">
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

<c:if test="${bep.toBeEditedOptions['productVariantAffiliateCategory']}">
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

<c:if test="${bep.toBeEditedOptions['productVariantClearanceSale']}">
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

<c:if test="${bep.toBeEditedOptions['productVariantOutOfStock']}">
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

<c:if test="${bep.toBeEditedOptions['productVariantDeleted']}">
  <td width="60px" align="right">
    <s:checkbox name="products[${ctr.index}].productVariants[${ctrVariant.index}].deleted"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantInventory']}">
  <td width="100px" align="right">
    <s:link beanclass="mhc.web.action.admin.ListBatchesAndCheckinInventory" target="_blank">
      <strong>${hk:netInventory(variant)}</strong>
      <s:param name="upc" value="${variant.id}"/>
    </s:link>
  </td>
</c:if>

  <%--<c:if test="${bep.toBeEditedOptions['productVariantCutOffInventory']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].cutOffInventory" size="2"--%>
  <%--style="width:30px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

<c:if test="${bep.toBeEditedOptions['productVariantWeight']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].weight" size="2"
            style="width:40px"/>
  </td>
</c:if>

  <%--<c:if test="${bep.toBeEditedOptions['productVariantLength']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].length" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

  <%--<c:if test="${bep.toBeEditedOptions['productVariantBreadth']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].breadth" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

  <%--<c:if test="${bep.toBeEditedOptions['productVariantHeigth']}">--%>
  <%--<td>--%>
  <%--<s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].height" size="2"--%>
  <%--style="width:40px"/>--%>
  <%--</td>--%>
  <%--</c:if>--%>

<c:if test="${bep.toBeEditedOptions['productVariantConsumptionTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].consumptionTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantLeadTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].leadTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantLeadTimeFactor']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].leadTimeFactor" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantBufferTime']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].bufferTime" size="2"
            style="width:40px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantNextAvailDate']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].nextAvailableDate" class="date_input"
            formatPattern="yyyy-MM-dd" style="width:100px"/>
  </td>
</c:if>

<c:if test="${bep.toBeEditedOptions['productVariantFollAvailDate']}">
  <td>
    <s:text name="products[${ctr.index}].productVariants[${ctrVariant.index}].followingAvailableDate" class="date_input"
            formatPattern="yyyy-MM-dd" style="width:100px"/>
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

<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${bep}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${bep}"/>
</s:form>
</s:layout-component>
</s:layout-render>

<script type="text/javascript">
  $(document).ready(function() {
    $('.errors').hide();
    if (!($('.discountPercent').attr("readonly") == "readonly")) {
      $('.discountPercent').change(_updateHkPrice);
    }
    $('.markedPrice').change(_updateHkPrice);
    $('.postpaidAmount').change(_updateHkPrice);
    if (!($('.hkPrice').attr("readonly") == "readonly")) {
      $('.hkPrice').change(_updateDiscountonMrp);
    }

    $('.submitButton').click(function() {
      $('.errors').empty();
      var ctr = 0;
      $('.productSecondaryCategory').each(function() {
        if ($(this).val().trim() === "") {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter secondary category for: " + productId);
        }
      });
      $('.productBrand').each(function() {
        if ($(this).val().trim() === "") {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter brand for: " + productId);
        }
      });
      $('.productSupplierTin').each(function() {
        if ($(this).val().trim() === "") {
          ctr = 1;
          var productId = $(this).parents('.productRow').find('.productId').val();
          $('.errors').append("<br/> Please enter supplier's tin number for: " + productId);
        }
      });
      if (ctr) {
        alert("Errors encoountered! Kindly go to the top of the page to view.");
        $('.errors').show();
      }
      return !ctr;
    });
  });
  function _updateHkPrice(e) {
    var variantRow = $(e.target).parents('.variantRow');
    variantRow.find('.hkPrice').val((variantRow.find('.markedPrice').val() * (1 - variantRow.find('.discountPercent').val())) - variantRow.find('.postpaidAmount').val());
  }

  function _updateDiscountonMrp(e) {
    var variantRow = $(e.target).parents('.variantRow');
    variantRow.find('.discountPercent').val(1 - (Math.round(variantRow.find('.hkPrice').val()) + Math.round(variantRow.find('.postpaidAmount').val())) / Math.round((variantRow.find('.markedPrice').val())));
  }
</script>