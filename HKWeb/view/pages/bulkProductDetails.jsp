<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <div style="text-align: center;"><h2>BULK EDIT PRODUCTS AND PRODUCT VARIANTS</h2></div>
    <div class="clear"></div>
    <div style="margin-top: 10px"></div>
    <div style="float: left;">Kindly select the options to be edited</div>

    <div class="clear"></div>
    <s:form beanclass="com.hk.web.action.admin.BulkEditProductAction">
      <div>
        <div style="float: left; width: 50%">
          <div>
            <fieldset>
              <legend>PRODUCT ATTRIBUTES</legend>
              <div style="float:left; width: 50%">
                <s:checkbox name="toBeEditedOptions['productName']"/>NAME
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productCategories']"/>CATEGORIES
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productBrand']"/>BRAND
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productSecondaryCategory']"/>SECONDARY CATEGORY
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productAmazonProduct']"/>IS AMAZON PRODUCT
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productColorOptions']"/>HAS COLOUR OPTIONS
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productGoogleAd']"/>IS GOOGLE AD DISALLOWED
                <div style="margin-top: 10px"></div>
              </div>

              <div style="float:right; width: 50%">
                <s:checkbox name="toBeEditedOptions['productJit']"/>IS JIT
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productOrderRanking']"/>SORTING
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productService']"/>SERVICE
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productDeleted']"/>IS DELETED
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productMinDays']"/>MIN DAYS
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productMaxDays']"/>MAX DAYS
              </div>
            </fieldset>
          </div>
        </div>

        <div style="float: right;  width: 50%">
          <div>
            <fieldset>
              <legend>PRODUCT VARIANT ATTRIBUTES</legend>
              <div style="float:left; width: 50%">
                <s:checkbox name="toBeEditedOptions['productVariantUpc']"/>UPC
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productVariantMRP']"/>MRP
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productVariantHKPrice']" id="hkPrice"/>HK PRICE(DISCOUNT
                UNEDITABLE)
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productVariantDiscount']" id="discount"/>DISCOUNT(HK PRICE
                UNEDITABLE)
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productVariantCostPrice']"/>COST PRICE
                <div style="margin-top: 10px"></div>

                <s:checkbox name="toBeEditedOptions['productVariantB2BPrice']"/>B2B PRICE
                <div style="margin-top: 10px"></div>

              </div>

              <div style="float: right; width: 50%;">
                  <s:checkbox name="toBeEditedOptions['productVariantPostpaidAmount']"/>POSTPAID AMOUNT
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="toBeEditedOptions['productVariantAffiliateCategory']"/>AFFILIATE CATEGORY
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="toBeEditedOptions['productVariantOutOfStock']"/>IS OUT OF STOCK
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="toBeEditedOptions['productVariantDeleted']"/>IS DELETED
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="toBeEditedOptions['productVariantInventory']"/>INVENTORY
                <div style="margin-top: 10px"></div>

                  <%--<s:checkbox name="toBeEditedOptions['productVariantCutOffInventory']"/>CUT-OFF INVENTORY--%>
                  <%--<div style="margin-top: 10px"></div>--%>

                  <s:checkbox name="toBeEditedOptions['productVariantWeight']"/>WEIGHT
                <div style="margin-top: 10px"></div>

                  <s:checkbox name="toBeEditedOptions['productVariantClearanceSale']"/>CLEARANCE SALE
                <div style="margin-top: 10px"></div>

                  <%--<s:checkbox name="toBeEditedOptions['productVariantLength']"/>LENGTH--%>
                  <%--<div style="margin-top: 10px"></div>--%>

                  <%--<s:checkbox name="toBeEditedOptions['productVariantBreadth']"/>BREADTH--%>
                  <%--<div style="margin-top: 10px"></div>--%>

                  <%--<s:checkbox name="toBeEditedOptions['productVariantHeigth']"/>HEIGHT--%>
                  <%--<div style="margin-top: 10px"></div>--%>
            </fieldset>
          </div>
        </div>
      </div>

      <div class="clear"></div>

      <fieldset>
        <div style="text-align: center;">
          Category : <s:text name="category" id="category"/>
          &nbsp; &nbsp;Brand : <s:text name="brand" id="brand"/>
          <s:submit name="bulkEdit" value="Bulk Edit" class="submitButton" style="font-size: 0.9em"/>
        </div>
      </fieldset>
      <%--</div>--%>
      <%--</div>--%>
      <%--</div>--%>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>
<script type="text/javascript">
  $(document).ready(function() {
    $('#hkPrice').click(function() {
      if ($(this).attr("checked") == "checked") {
        $('#discount').attr('disabled', 'disabled');
        //        alert("HK Price and discount cannot be selected simultaneously! Kindly uncheck HK PRICE to enable DISCOUNT.");
      } else {
        $('#discount').removeAttr('disabled');
      }
    });

    $('#discount').click(function() {
      if ($(this).attr("checked") == "checked") {
        $('#hkPrice').attr('disabled', 'disabled');
        //        alert("HK Price and discount cannot be selected simultaneously! Kindly uncheck DISCOUNT to enable HK PRICE.");
      } else {
        $('#hkPrice').removeAttr('disabled');
      }
    });
    $('.submitButton').click(function() {
      var dataCategory = document.getElementById("category").value;
      var dataBrand = document.getElementById("brand").value;
      if (dataCategory === "" && dataBrand === "") {
        alert("Kindly enter the category and(/or) brand for which editing needs to be done!");
        return false;
      } else return true;
    });
  });
</script>