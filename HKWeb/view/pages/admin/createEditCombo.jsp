<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction" var="comboAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.addRowButton').click(function() {

          var lastIndex = $('.lastRow').attr('count');
          if (!lastIndex) {
            lastIndex = -1;
          }
          $('.lastRow').removeClass('lastRow');

          var nextIndex = eval(lastIndex + "+1");
          var newRowHtml =
              '<tr count="' + nextIndex + '" class="lastRow productRow">' +
              ' <td>' +
              ' <input size="15" type="text" class="product" name="comboProductAndAllowedVariantsDtoList[' + nextIndex + '].product" />' +
              '</td><td class="productDetails">' +
              '</td><td>' +
              ' <input size="1" type="text" name="comboProductAndAllowedVariantsDtoList[' + nextIndex + '].qty" />' +
              '</td><td>' +
              ' <input style="width:250px;" type="text" name="comboProductAndAllowedVariantsDtoList[' + nextIndex + '].allowedVariants"/>' +
              ' </td>' +
              '</tr>';

          console.log(newRowHtml);
          $('#productTable').append(newRowHtml);

          return false;
        });

        $('.product').live("change", function() {
          $('.invalidProduct').html('&nbsp;');
          var productRow = $(this).parents('.productRow');
          var productId = productRow.find('.product').val();
          var productDetails = productRow.find('.productDetails');
          $.getJSON(
              $('#productInfoLink').attr('href'), {productId: productId},
              function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  productDetails.html(
                      res.data.product
                      );
                } else {
                  $('.invalidProduct').html('<h2>Invalid Product Id</h2>');
                }
              }
              );
        });

        $('.comboId').live("change", function() {
          $('.comboIdAvailability').html('&nbsp;');
          var productId = $(this).val();
          $.getJSON(
              $('#productInfoLink').attr('href'), {productId: productId},
              function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  $('.comboIdAvailability').html(
                      "<b style='color:red;font-size:1.4em'>ID Un-available</b>"
                      );
                  $('.comboId').val("");
                  $('.comboId').focus();
                } else {
                  $('.comboIdAvailability').html(
                      "<b style='color:green;font-size:1.4em'>ID Available</b>"
                      );
                }
              }
              );
        });

      });
    </script>
  </s:layout-component>


  <s:layout-component name="heading">
    Create/Edit Combo
  </s:layout-component>
  <s:layout-component name="content">
    <div style="display: none;">
      <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction" id="productInfoLink"
              event="getProductDetails"></s:link>
    </div>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction">
      <s:hidden name="combo" value="${comboAction.combo}"/>
      <div style="font-size:1.4em" class="invalidProduct"></div>
      <div class="grid_6">
        <fieldset class="right_label">
          <legend>Product Details</legend>
          <table border="1" id="productTable">
            <tr>
              <th>Product ID</th>
              <th>Details</th>
              <th>Qty</th>
              <th>Allowed Variants(comma separated)</th>
              <th>Action</th>
            </tr>
            <c:forEach var="productDto" items="${comboAction.comboProductAndAllowedVariantsDtoList}" varStatus="ctr">
              <tr count="${ctr.index}" class="${ctr.last ? 'lastRow':''} productRow">
                <td>
                  <s:text style="width:100px" name="comboProductAndAllowedVariantsDtoList[${ctr.index}].product"/>
                </td>
                <td>
                    ${productDto.product.name}
                </td>
                <td>
                  <s:text style="width:50px" name="comboProductAndAllowedVariantsDtoList[${ctr.index}].qty"/>
                </td>
                <td>
                  <s:text style="width:200px"
                          name="comboProductAndAllowedVariantsDtoList[${ctr.index}].allowedVariants"/>
                </td>
                <td>
                  <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction" event="deleteComboProduct">
                    <s:param name="comboProduct" value="${productDto.id}"/>
                    Delete</s:link>
                </td>
              </tr>
            </c:forEach>
          </table>
          <br/>

          <div class="buttons">
            <a href="#" class="addRowButton">Add new row</a>
          </div>
        </fieldset>
      </div>

      <div class="grid_6">
        <fieldset class="right_label">
          <legend>Combo Details</legend>
          <ul>
            <li>
              <label>Categories*</label>
              <s:text style="width:200px" name="categories" value="${comboAction.categories}"
                      placeholder="eg: Nutrition|Sports Nutrition|Combos"/>
            </li>
            <li>
              <label>Primary Category*</label>
              <s:text style="width:200px" name="primaryCategory"
                      value="${comboAction.combo.primaryCategory.displayName}" placeholder="eg: Nutrition"/>
            </li>
            <li>
              <label>Combo ID*</label>
              <s:text class="comboId" style="width:200px" name="combo.id" value="${comboAction.combo.id}"
                      placeholder="eg: CMB-NUT01"/>
              <span class="comboIdAvailability"></span>
            </li>
            <li>
              <label>Name*</label>
              <s:text style="width:200px" name="combo.name"
                      placeholder="eg: Lean Body Pack"/>
            </li>
            <li>
              <label>Brand</label>
              <s:text style="width:200px" name="combo.brand" placeholder="eg: ON, Combo"/>
            </li>

            <li>
              <label>Order Ranking*</label>
              <s:text style="width:200px" name="combo.orderRanking"
                      value="${comboAction.combo.orderRanking}" placeholder="eg: 10"/>
            </li>

            <li>
              <label>Marked Price(Auto compute)</label>
                ${comboAction.combo.markedPrice}
            </li>
            <li>
              <label>Percentage Discount*</label>
              <s:text style="width:200px" name="combo.discountPercent"
                      placeholder="In decimal eg 0.10"/>
            </li>
            <li>
              <label>HK Price(Auto compute)</label>
                ${comboAction.combo.hkPrice}
            </li>

            <li>
              <label>Deleted?</label>
              <s:checkbox name="combo.deleted"/>
            </li>
          </ul>
           <em class="prom yellow help">* marked fields are manadatory</em>
        </fieldset>

      </div>

      <div class="grid_12">
        <div class="buttons">
          <s:submit name="saveCombo" value="Save"/>
        </div>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>