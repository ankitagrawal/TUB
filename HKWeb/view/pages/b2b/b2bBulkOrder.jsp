<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<style type="text/css">
  .b2bTable {
    border: 1px solid #ccc;
    width: 100%;
  }

  .b2bTable tr th {
    border-right: 1px solid #ccc;
    text-align: left;
    padding-left:2px;
  }

</style>


<s:layout-render name="/layouts/b2bLayout.jsp">
<s:useActionBean beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction" var="atc"/>
<s:useActionBean beanclass="com.hk.web.action.core.b2b.B2BCartAction" var="pa"/>

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/calendar-blue.css"
      rel="stylesheet" type="text/css"/>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath}/css/jquery.lightbox-0.5.css"
      media="screen"/>

<script type="text/javascript">


  var productList = [];
  $(document).ready(function () {
    var totalPayableAmount = 0.0;
    $('.totalPrice').each(function() {
      totalPayableAmount = totalPayableAmount + parseFloat($(this).html());
    });
    $("#totalPayableAmount").html(totalPayableAmount);
    var counter = $("#indexCounter").val();
    $('.addRowButton').click(function () {
      if (isNaN(counter)) {
        counter = 0;
      }
      ++counter;
      var index = counter - 1;
      var newRowHtml =
          '<tr class="bodyTr">' +
          '<td>' + counter +
          '</td>' +
          '<td><input name="productVariantList[' + index + ']" class="variant b2bTableInput" /></td>' +
          '<td align="center"><span id="pvDetails" class="pvDetails"></span></td>' +
          '<td><div class="img48" style="vertical-align:top;"></div></td>' +
          '<td align="center"><span id="mrp" class="mrp"/></td>' +
          '<td><input name="productVariantList[' + index + '].qty" class="qty b2bTableInput" value="0"/></td>' +
          '<td align="center"><label id="totalPrice" class="totalPrice b2bTableInput">0</label></td>' +
          '</tr><tr height="10"></tr>';
      $('#poTable').append(newRowHtml);
      return true;
    });

    $('.qty').live("blur", function () {
      var row = $(this).parent().parent();
      var quantity = parseFloat(row.find('.qty').val());
      var productVariantId = row.find('.variant').val();
      if(quantity>=0 && typeof quantity === 'number' && quantity % 1 == 0){
      var price = parseFloat(row.find(".mrp").html());
      if (quantity != null && quantity >= 0 && !isNaN(price)) {
        var totalPrice = parseFloat(quantity * price);
        row.find(".totalPrice").html(totalPrice.toFixed(2));
      } else {
        row.find(".totalPrice").html(0);
      }
      updateTotal('.totalPrice', "#totalPayableAmount", productVariantId, quantity);
      }
      else{
    	  alert('Please provide an integral quantity');
    	  quantity = 0;
    	  row.find('.qty').val(0);
    	  row.find(".totalPrice").html(0);
    	  updateTotal('.totalPrice', "#totalPayableAmount", productVariantId, quantity);
      }
    });
    
    $('.variant').live("blur", function () {
    	var row = $(this).parent().parent();
    	var productVariantId = row.find('.variant').val();
    	var rIndex=row.get(0).children[0].innerHTML-1;
    	var value = true;
    	$('#poTable  > .bodyTr').each(
				function(currIndex,ob) {
					if(productVariantId!=null&&productVariantId!=""){
					if(rIndex!=currIndex)
						{
						if(!$(this).find(".variant").is("input")){
							if ($(this).find(".variant").get(0).innerHTML == productVariantId){
								alert('Cannot add mutiple rows for same Variant');
								value= false;
								return value;
								}
							}
							else{
								if($(this).get(0).children[0].innerHTML!=row.get(0).children[0].innerHTML){
								if ($(this).find(".variant").val() == productVariantId){
									alert('Cannot add mutiple rows for same Variant');
									value= false;
									return value;
								}
						}
							}
					}
				}
										
		}); 
		
		if(!value){
			row.find(".variant").val("");
			productVariantId = "";
		}
      $("#variantDetailsInavlid").html('');
      var self1 = row.find(".pvDetails");
      var self2 = row.find(".mrp");
      var self3 = row.find(".totalPrice");
      var self4 = row.find(".qty");
      var imgDiv = row.find(".img48");

      $.getJSON($('#pvInfoLink').attr('href'), {
        productVariantId : productVariantId
      },

          function(res) {
            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {

              $('.addToCartButton').attr('disabled', 'disabled');
              var error = false;
              var variantName = res.data.product.toCapitalCase();
              var options = res.data.options.toCapitalCase();
              self1.html(variantName + "</br>" + "<em>" + options + "</em>");
              if (res.data.variant.b2bPrice != null) {
                self2.html(res.data.variant.b2bPrice);
              }
              else
              {
                self2.html(res.data.variant.hkPrice);
              }
              self4.val(0);
              var path = "${pageContext.request.contextPath}";
              var url = path + res.data.imageUrl;
              imgDiv.html('<img class="prod48" src="' + url + '" />');
              row.find('.variant').attr("title", "");
              row.find('.variant').css("background-color", "");
              if (res.data.variant.outOfStock) {
                row.find('.variant').css("background-color", "#FF0000");
                row.find('.variant').attr("title", "Product Out of Stock");
                $("#variantDetailsInavlid").html('<h4>' + "Out Of Stock" + '</h4>');
                error = true;
              }


              if (res.data.inventory == 0) {
                row.find('.variant').css("background-color", "#FF0000");
                row.find('.variant').attr("title", "Inventory Not Found");
                $("#variantDetailsInavlid").html('<h4>' + "Inventory Not Found" + '</h4>');
                error = true;
              }
              if ((res.data.variant.outOfStock) && (res.data.inventory == 0)) {
                row.find('.variant').css("background-color", "#FF0000");
                row.find('.variant').attr("title", "Inventory Not Found and out of stock also");
                error = true;
              }
              if (!error) {
                row.css('background-color', '#F2F7FB');
                $('.addToCartButton').removeAttr('disabled');
                $("#variantDetailsInavlid").html('');
              }

              updateTotal('.totalPrice', "#totalPayableAmount", productVariantId, 0);
            } else {

              row.find('.variant').css("background-color", "");
              row.find('.variant').attr("title", "");
              $('.addToCartButton').removeAttr('disabled');
              if (productVariantId != "") {
                row.find('.variant').attr("title", "Invalid Input");
                row.find('.variant').css("background-color", "#FF0000");
                $('.addToCartButton').attr('disabled', 'disabled');
              }
              self1.html('');
              self2.html('');
              self3.html(0);
              self4.val(0);
              imgDiv.html("");
              $("#variantDetailsInavlid").html('<h4>' + res.message + '</h4>');
            }

          });
      
    });

    $('#excelUpload').live("click", function() {
      var filebean = $('#filebean').val();
      if (filebean == null || filebean == '') {
        alert('choose file');
        return false;
      }
    });
    
  });

  function lookup(lookupId) {
    for (var i = 0, len = productList.length; i < len; i++) {
      if (productList[i].id === lookupId)
        return true;
    }
    return false;
  }

  function update(updateId, updateQty) {
    for (var i = 0, len = productList.length; i < len; i++) {
      if (productList[i].id === updateId) {
        productList[i].qty = updateQty;
      }
    }
  }

  function updateTotal(fromTotalClass, toTotalClass, a, b) {
    var total = 0;
    $.each($(fromTotalClass), function(index, value) {
      var eachRow = $(value);
      var eachRowValue = eachRow.html().trim();
      total += parseFloat(eachRowValue);
    });

    if (!lookup(a)) {
      if (b > 0) {
        productList.push({
          id : a,
          qty : b
        });
      }
    } else {
      update(a, b);

    }

    $(toTotalClass).html(total);

  }
  ;

  String.prototype.toCapitalCase = function() {
    var re = /\s/;
    var words = this.split(re);
    re = /(\S)(\S+)/;
    for (i = words.length - 1; i >= 0; i--) {
      re.exec(words[i]);
      words[i] = RegExp.$1.toUpperCase() + RegExp.$2.toLowerCase();
    }
    return words.join(' ');
  }
</script>

</s:layout-component>
<s:layout-component name="checkoutStep">
  <div style="display: none">
    <s:link beanclass="com.hk.web.action.core.b2b.B2BCartAction"
            id="pvInfoLink" event="getPVDetails"></s:link>
  </div>
  <div>
    <s:form beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction" class="addToCartForm">
      <strong>B2B Order Cart </strong>

      <table class="b2bTable">

        <tr height="40px" bgcolor="#EEE8CD">
          <th width="10%">S No.</th>
          <th width="18%">Variant ID</th>
          <th width="20%">Product Detail</th>
          <th width="17%">Product Image</th>
          <th width="10%">B2B Price</th>
          <th width="13%">Quantity</th>
          <th width="12%">Total</th>
        </tr>

        <tbody id="poTable" style="font-size:.9em;padding:1px;">
        <c:forEach items="${pa.cartLineItems}" var="lineItem"
                   varStatus="item">
          <c:set value="${lineItem.productVariant}" var="productVariant"/>
          <c:set value="${productVariant.product}" var="product"/>
          <tr class="bodyTr" style="background-color: #F2F7FB">
            <s:hidden
                name="productVariantList[${item.count-1}]"
                value="${lineItem.productVariant.id}"/>
            <td>${item.count}.</td>
            <td class="variant">${productVariant.id}</td>
            <td align="center"><span id="pvDetails" class="pvDetails">${product.name}
											<br> <em>
                  ${productVariant.optionsCommaSeparated} </em>
									</span></td>
            <td>
              <div class='img48' style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
                               <c:choose>
                                   <c:when test="${product.mainImageId != null}">
                                       <hk:productImage imageId="${product.mainImageId}"
                                                        size="<%=EnumImageSize.TinySize%>"/>
                                   </c:when>
                                   <c:otherwise>
                                       <img class="prod48"
                                            src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${product.id}.jpg"
                                            alt="${product.name}"/>
                                   </c:otherwise>
                               </c:choose>
                           </div>
            </td>
            <td align="center"><span id="mrp" class="mrp">
									<c:choose>
                    <c:when
                        test="${(productVariant.b2bPrice)!=null}">${productVariant.b2bPrice}</c:when>
                    <c:otherwise>${productVariant.hkPrice}</c:otherwise>
                  </c:choose>
									</span></td>
            <td align="center"><s:text name="productVariantList[${item.count-1}].qty"
                       class="qty" value="${lineItem.qty}" style="width:50px;"/></td>
            <td align="center"><label id="totalPrice"
                                      class="totalPrice b2bTableInput">

              <c:choose>
                <c:when
                    test="${(productVariant.b2bPrice)!=null}">${productVariant.b2bPrice*lineItem.qty}</c:when>
                <c:otherwise>${productVariant.hkPrice*lineItem.qty}</c:otherwise>
              </c:choose>


            </label></td>
          </tr>
          <c:set var="newIndex" value="${item.count}" scope="page"/>
        </c:forEach>
        <input type="hidden" id="indexCounter" value="${newIndex}"/>
        </tbody>
        <tfoot>
        <tr>
          <td colspan="7">
            <hr/>
          </td>
        </tr>
        <tr>
          <td colspan="6"><span id="variantDetailsInavlid"></span> <a href="#"
                                                                      class="addRowButton" style="font-size: 1em">Add
            new row</a>
          <td>
						<span id="summaryGrandTotalPayable">Rs: <label
                id="totalPayableAmount" style="font-weight: bold;"></label></span></td>
        </tr>

        <tr>
          <td colspan="5" style="text-align:right;">
            <span class="special"> C-Form
            <c:choose>
              <c:when test="${pa.CFormAvailable}">
                <input type="checkbox" checked="checked" name="CFormAvailable"  />
              </c:when>
              <c:otherwise>
                <input type="checkbox" name="CFormAvailable"/>
              </c:otherwise>
            </c:choose>

              </span>
          </td>
          <td colspan="2"><s:submit name="b2bAddToCart" value="Save To Cart" style="padding:2px;"
                                    class="addToCartButton cta button_green"/></td>
        </tr>
        </tfoot>
      </table>
    </s:form>
  </div>
  <hr/>
  <div>
    <s:form body="center" accept-charset="UTF-8" class="excelUploadForm"
            beanclass="com.hk.web.action.core.b2b.B2BCartAction">

      <div class="left">

        <strong> Upload Order by Excel (Headers: VARIANT_ID, QTY) </strong>

        <p>
          <label>Browse to Upload: </label>
          <s:file id="filebean" name="fileBean" size="30"/>
        </p>

        <div class="buttons">
          <s:submit id="excelUpload" name="parseOnly" value="Upload" style="padding:2px;"
                    class="cta button_green"/>
        </div>
        <br>
      </div>

    </s:form>
  </div>
</s:layout-component>
<s:layout-component name="endScripts">
  <script type="text/javascript">

    $(document).ready(function() {
      // bind 'myForm' and provide a simple callback function
      $('.addToCartForm').ajaxForm(function(res1) {
        if (res1.code == '<%=HealthkartResponse.STATUS_OK%>') {
          $('.message .line1').html("Your cart has been updated");
          $('#productsInCart').html(res1.data.itemsInCart);
          alert('Added To Cart');
        }
        else if (res1.code == "null_exception") {
          alert(res1.message);
        }
        else if (res1.code == '<%=HealthkartResponse.STATUS_ERROR%>') {

            if (res1.data.notAvailable != null) {
              alert("Not available");
            }
            else {
              alert("Warning: Some of the given products are out of stock, So they were not added");

            }
          }
        window.location.href = "${pageContext.request.contextPath}/core/b2b/B2BCart.action";
      });

    });


  </script>
</s:layout-component>
</s:layout-render>
