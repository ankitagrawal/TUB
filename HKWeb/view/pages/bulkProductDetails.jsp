<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <div style="text-align: center;"><h2>BULK EDIT PRODUCTS AND PRODUCT VARIANTS</h2></div>
    <div class="clear"></div>
    <div style="margin-top: 10px"></div>
    <div style="float: left;">Kindly select the options to be edited</div>

    <div class="clear"></div>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.BulkEditProductAction">
      <div>
        <div style="float: left; width: 50%">
          <div>
            <fieldset>
              <legend>PRODUCT ATTRIBUTES</legend>
              <div style="float:left; width: 50%">
                <s:checkbox name="" value="productName"/>NAME
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productCategories"/>CATEGORIES
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productBrand"/>BRAND
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productSecondaryCategory"/>SECONDARY
                CATEGORY
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productSupplierTin"/>SUPPLIER TIN
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productAmazonProduct"/>IS AMAZON PRODUCT
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productCodAllowed"/>IS COD ALLOWED
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productColorOptions"/>HAS COLOUR OPTIONS
                <div style="margin-top: 10px"></div>
              </div>

              <div style="float:right; width: 50%">
                <s:checkbox name="optionToEdit" value="productJit"/>IS JIT
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productGoogleAd"/>IS GOOGLE AD DISALLOWED
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productOrderRanking"/>SORTING
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productService"/>SERVICE
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productDeleted"/>IS DELETED
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productHidden"/>IS HIDDEN
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productMinDays"/>MIN DAYS
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productMaxDays"/>MAX DAYS
              </div>
            </fieldset>
          </div>

          <div class="clear"></div>

          <fieldset style="margin-top:20px;">
            <div style="text-align: center;">
              Category : <s:text name="category" id="category"/>
              &nbsp; &nbsp;
              Brand : <s:text name="brand" id="brand"/>
              <s:submit name="defineOptionsMap" value="Bulk Edit" class="submitButton"
                        style="font-size: 0.9em"/>
            </div>
          </fieldset>
        </div>

        <div style="float: right;  width: 50%">
          <div>
            <fieldset>
              <legend>PRODUCT VARIANT ATTRIBUTES</legend>
              <div style="float:left; width: 50%">
                <s:checkbox name="optionToEdit" value="productVariantUpc"/>UPC
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantMRP"/>MRP
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantHKPrice" id="hkPrice"/>HK
                PRICE(DISCOUNT
                UNEDITABLE)
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantDiscount" id="discount"/>DISCOUNT(HK
                PRICE
                UNEDITABLE)
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantCostPrice"/>COST PRICE
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantB2BPrice"/>B2B PRICE
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantPostpaidAmount"/>POSTPAID
                AMOUNT
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantAffiliateCategory"/>AFFILIATE
                CATEGORY
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantOutOfStock"/>IS OUT OF STOCK
                <div style="margin-top: 10px"></div>

                <s:checkbox name="optionToEdit" value="productVariantDeleted"/>IS DELETED
                <div style="margin-top: 10px"></div>
              </div>

              <div style="float: right; width: 50%;">
                  <s:checkbox name="optionToEdit" value="productVariantInventory"/>INVENTORY
                <div style="margin-top: 10px"></div>
                  <s:checkbox name="optionToEdit" value="productVariantWeight"/>WEIGHT
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantClearanceSale"/>CLEARANCE
                SALE
                <div style="margin-top: 10px"></div>
                  <s:checkbox name="optionToEdit" value="productVariantConsumptionTime"/>CONSUMPTION
                TIME
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantLeadTime"/>LEAD TIME
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantLeadTimeFactor"/>LEAD TIME
                FACTOR
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantBufferTime"/>BUFFER TIME
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantNextAvailDate"/>NEXT
                AVAILABLE DATE
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantFollAvailDate"/>FOLLWING
                AVAILABLE DATE
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="optionToEdit" value="productVariantHasFreeProductVariant"/>FREE
                PRODUCT VARIANT
                <div style="margin-top: 10px"></div>
            </fieldset>
          </div>
        </div>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>
<style type="text/css">
  .clear {
    clear: both;
    height: 1px;
    overflow: hidden;
    margin-top: -1px;
  }
</style>
<script type="text/javascript">
  $(document).ready(function () {
    $('#hkPrice').click(function () {
      if ($(this).attr("checked") == "checked") {
        $('#discount').attr('disabled', 'disabled');
      } else {
        $('#discount').removeAttr('disabled');
      }
    });

    $('#discount').click(function () {
      if ($(this).attr("checked") == "checked") {
        $('#hkPrice').attr('disabled', 'disabled');
      } else {
        $('#hkPrice').removeAttr('disabled');
      }
    });
    $('.submitButton').click(function () {
      var dataCategory = document.getElementById("category").value;
      var dataBrand = document.getElementById("brand").value;
      if (dataCategory === "" && dataBrand === "") {
        alert("Kindly enter the category and(/or) brand for which editing needs to be done!");
        return false;
      } else return true;
    });
  });
</script>