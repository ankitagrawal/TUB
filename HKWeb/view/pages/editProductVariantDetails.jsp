<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="${pa.product.name}">

  <s:layout-component name="menu"> </s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.markedPrice').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);
        $('.hkPrice').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);
        $('.postpaidAmount').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);
      });
      function _updateHkPrice(e) {
        var variantRow = $(e.target).parents('.variantRow');
        variantRow.find('.hkPrice').val(variantRow.find('.markedPrice').val() * (1 - variantRow.find('.discountPercent').val()));
      }
      function _updateDiscountonMrp(e) {
        var variantRow = $(e.target).parents('.variantRow');
        variantRow.find('.discountPercent').val(1 - (Math.round(variantRow.find('.hkPrice').val()) + Math.round(variantRow.find('.postpaidAmount').val())) / Math.round((variantRow.find('.markedPrice').val())));
      }
    </script>
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="content">
    <h2>Edit Product Variants for Product - ${pa.product.name}</h2>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
       <table>
        <tr>
          <th>Variant ID</th>
          <th>Variant Options</th>
          <th>Variant Name</th>
          <th>UPC</th>
          <th>Cost Price</th>
          <th>MRP</th>
          <th>HK Price</th>
          <th>B2B Price</th>
          <th>Postpaid Amt</th>
          <th>Discount</th>
          <th>Sorting</th>
          <th>Free Variant Id</th>
          <th>Affiliate Category</th>
          <th>Payment Type</th>
          <th>Service Type</th>
          <th>Out of stock</th>
          <th>Deleted</th>
          <th>Clearance</th>
          <th>Inventory</th>
          <th>Wt.(gm)</th>
          <th>L(cm)</th>
          <th>B(cm)</th>
          <th>H(cm)</th>
          <th>Variant Extra Options</th>
          <th>Consumption Time<br/>(Days)</th>
          <th>Lead Time</th>
          <th>Lead Time Factor</th>
          <th>Buffer Time</th>
          <th>Next Avil. Date</th>
          <th>Fol. Avil. Date</th>
		  <th>Supplier Code</th>
		  <th>Other Remarks</th>

        </tr>
        <c:forEach var="productVariant" items="${pa.productVariants}" varStatus="ctr">
          <s:hidden name="product"/>
          <s:hidden name="productVariants[${ctr.index}]"/>
          <tr class="variantRow">
            <td>
                ${productVariant.id}
            </td>
            <td>
              <s:textarea name="options[${ctr.index}]"
                          value="${productVariant.optionsPipeSeparated}" style="width:auto; height:auto;"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].variantName" style="width:100px;"/>
            </td>
            <td><s:text name="productVariants[${ctr.index}].upc" style="width:150px;"/></td>
            <td>
              <s:text name="productVariants[${ctr.index}].costPrice" style="width:100px;" class="costPrice"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].markedPrice" style="width:100px;" class="markedPrice"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].hkPrice" style="width:100px;" class="hkPrice"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].b2bPrice" style="width:100px;" class="b2bPrice"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].postpaidAmount" style="width:100px;" class="postpaidAmount"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].discountPercent" style="width:100px;" class="discountPercent"
                      readonly="readonly"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].orderRanking" style="width:100px;" class="sorting"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].freeProductVariant" style="width:100px;" class="freeProductVariant"/>
            </td>
            <td>
              <s:select name="productVariants[${ctr.index}].affiliateCategory" value="${productVariant.affiliateCategory.affiliateCategoryName}" class="affiliateCategory" style="width:100px;">
                <s:option value="">None</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allAffiliateCategories"
                                           label="affiliateCategoryName" value="affiliateCategoryName"/>
              </s:select><br/>
            </td>
            <td>
              <s:select name="productVariants[${ctr.index}].paymentType" value="${productVariant.paymentType.id}"
                        class="paymentType" style="width:100px;">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentTypes"
                                           label="name" value="id"/>
              </s:select><br/>
            </td>
            <td>
              <s:select name="productVariants[${ctr.index}].serviceType" value="${productVariant.serviceType.id}"
                        class="serviceType" style="width:100px;">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="serviceTypes"
                                           label="name" value="id"/>
              </s:select><br/>
            </td>
            <c:if test="${pa.product.jit}">
                <td>
                    <s:checkbox name="productVariants[${ctr.index}].outOfStock"/>
                </td>
            </c:if>
            <c:if test="${!pa.product.jit}">
                <td>
                <shiro:hasRole name="<%=RoleConstants.GOD%>">
                      <s:checkbox name="productVariants[${ctr.index}].outOfStock"/>
                </shiro:hasRole>
                ${productVariant.outOfStock}
                </td>
            </c:if>
            <td>
              <s:checkbox name="productVariants[${ctr.index}].deleted"/>
            </td>
             <td>
              <s:checkbox name="productVariants[${ctr.index}].clearanceSale" class="clearanceSale"/>
            </td>
            <td><s:link beanclass="com.hk.web.action.admin.inventory.ListBatchesAndCheckinInventory" target="_blank">
              <s:param name="upc" value="${productVariant.id}"/>${hk:netInventoryAtServiceableWarehouses(productVariant)}</s:link>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].weight" style="width:50px;"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].length" style="width:50px;"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].breadth" style="width:50px;"/>
            </td>
            <td>
              <s:text name="productVariants[${ctr.index}].height" style="width:50px;"/>
            </td>
            <td>
              <s:textarea name="extraOptions[${ctr.index}]"
                          value="${productVariant.extraOptionsPipeSeparated}" style="width:auto; height:auto;"/>
            </td>
            <td><s:text name="productVariants[${ctr.index}].consumptionTime" style="width:50px;"/></td>
            <td><s:text name="productVariants[${ctr.index}].leadTime" style="width:50px;"/></td>
            <td><s:text name="productVariants[${ctr.index}].leadTimeFactor" style="width:50px;"/></td>
            <td><s:text name="productVariants[${ctr.index}].bufferTime" style="width:50px;"/></td>
            <td><s:text name="productVariants[${ctr.index}].nextAvailableDate" class="date_input" formatPattern="yyyy-MM-dd" style="width:100px;"/></td>
            <td><s:text name="productVariants[${ctr.index}].followingAvailableDate" class="date_input" formatPattern="yyyy-MM-dd" style="width:100px;"/></td>
	        <td><s:text name="productVariants[${ctr.index}].supplierCode" style="width:50px;"/></td>
	        <td><s:textarea name="productVariants[${ctr.index}].otherRemark" style="width:auto; height:auto;"/></td>

          </tr>
        </c:forEach>
      </table>
      <br/>

        <shiro:hasPermission name="<%=PermissionConstants.UPDATE_VARIANT_INFO%>">

            <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction" event="select"
                    target="_blank">
                <s:param name="product" value="${pa.product.id}"/> Create New Variant
            </s:link>

            <div class="buttons">
                <s:submit name="saveProductVariantDetails" value="Save"/>
            </div>

        </shiro:hasPermission>

    </s:form>
  </s:layout-component>
</s:layout-render>