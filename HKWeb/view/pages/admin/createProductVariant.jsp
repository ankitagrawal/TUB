<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction" var="csp"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
//        $('.discountPercent').keyup(_updateHkPrice).change(_updateHkPrice);
//        $('.markedPrice').keyup(_updateHkPrice).change(_updateHkPrice);
          $('.markedPrice').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);
          $('.hkPrice').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);
          $('.postpaidAmount').keyup(_updateDiscountonMrp).change(_updateDiscountonMrp);

        $('.variant').live("change", function() {
          var variantRow = $(this).parents('.variantList');
          var productVariantId = variantRow.find('.variant').val();
          $.getJSON(
              $('#pvInfoLink').attr('href'), {productVariantId: productVariantId},
              function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                   $('.variantDetails').html('<h2 style="color:red">Variant Id already esists</h2>');
                } else {
                  $('.variantDetails').html('<h2 style="color:green">Variant Id is available</h2>');
                }
              }
              );
        });
      });
      function _updateHkPrice(e) {
        var variantList = $(e.target).parents('.variantList');
        variantList.find('.hkPrice').val(variantList.find('.markedPrice').val() * (1 - variantList.find('.discountPercent').val()));
      }
      function _updateDiscountonMrp(e) {
        var variantRow = $(e.target).parents('.variantList');
        variantRow.find('.discountPercent').val(1 - (Math.round(variantRow.find('.hkPrice').val()) + Math.round(variantRow.find('.postpaidAmount').val())) / Math.round((variantRow.find('.markedPrice').val())));
      }
    </script>
  </s:layout-component>
  <s:layout-component name="heading">
    ${csp.product.id} : ${csp.product.name}                                
  </s:layout-component>
  <s:layout-component name="content">
    <div style="display: none;">
      <s:link beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
    </div>
    <div style="word-wrap:break-word; font-size:.8em">
    <b>Existing Variant IDs:</b> <c:forEach items="${csp.product.productVariants}" var="variant">${variant.id},&nbsp;</c:forEach>
    </div>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction">
      <s:hidden name="product"/>
      <fieldset class="right_label">
        <legend>Create a new Product Variant</legend>
        <ul class="variantList">
          <li>
            <label>Variant ID</label>
            <s:text name="productVariant.id" class="variant"/>
            <span class="variantDetails"></span>
          </li>
          <li>
            <label>Cost Price</label>
            <s:text name="productVariant.costPrice" class="costPrice"/>
          </li>
          <li>
            <label>MRP</label>
            <s:text name="productVariant.markedPrice" class="markedPrice"/>
          </li>
          <li>
            <label>HK Price</label>
            <s:text name="productVariant.hkPrice" class="hkPrice"/>
          </li>
          <li>
            <label>Discount Percent</label>
            <s:text name="productVariant.discountPercent" class="discountPercent" readonly="readonly"/>
          </li>
          <li>
            <label>Postpaid Amount</label>
            <s:text name="productVariant.postpaidAmount" class="postpaidAmount"/>
          </li>
          <li>
            <label>B2B Price</label>
            <s:text name="productVariant.b2bPrice" class="b2bPrice"/>
          </li>
          <li>
            <label>Shipping Base Quantity</label>
            <s:text name="productVariant.shippingBaseQty" value="1" readonly="readonly"/>
          </li>
          <li>
            <label>Shipping Base Price</label>
            <s:text name="productVariant.shippingBasePrice" value="30" readonly="readonly"/>
          </li>
          <li>
            <label>Shipping Add Quantity</label>
            <s:text name="productVariant.shippingAddQty" value="1" readonly="readonly"/>
          </li>
          <li>
            <label>Shipping Add Price</label>
            <s:text name="productVariant.shippingAddPrice" value="30" readonly="readonly"/>
          </li>
          <li>
            <label>Tax ID</label>
            <s:select name="productVariant.tax">
              <hk:master-data-collection service="<%=TaxDao.class%>" serviceProperty="localTaxList" label="name" value="id"/>
            </s:select>
          </li>
          <li>
            <label>Variant Name</label>
            <s:text name="productVariant.variantName"/>
          </li>
          <li>
            <label>Affiliate Category</label>
            <s:select name="productVariant.affiliateCategory" class="affiliateCategory" style="width:100px;">
              <s:option value="">None</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allAffiliateCategories" label="affiliateCategoryName" value="affiliateCategoryName"/>
            </s:select>
          </li>
          <li>
            <label>UPC</label>
            <s:text name="productVariant.upc"/>
          </li>
          <li>
            <label>Cut off Inventory</label>
            <s:text name="productVariant.cutOffInventory"/>
          </li>
          <li>
            <label>Sorting</label>
            <s:text name="productVariant.orderRanking"/>
          </li>
          <li>
            <label>Options</label>
            <s:textarea name="options"/>
          </li>
          <li>
            <label>Extra Options</label>
            <s:textarea name="extraOptions"/>
          </li>
          <li>
            <label>Out Of Stock</label>
            <s:checkbox name="productVariant.outOfStock"/>
          </li>
          <li>
            <label>Deleted</label>
            <s:checkbox name="productVariant.deleted"/>
          </li>
          <li>
            <label>&nbsp;</label>
            <div class="buttons">
              <s:submit name="createVariant" value="create"/>
            </div>
          </li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>