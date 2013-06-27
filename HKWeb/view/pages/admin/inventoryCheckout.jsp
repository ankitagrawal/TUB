<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" var="icBean"/>
<c:set var="lineItemStatusId_picking" value="<%=EnumShippingOrderStatus.SO_Picking.getId()%>"/>
<c:set var="lineItemStatusId_shipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="commentTypePacking" value="<%= MasterDataDao.USER_COMMENT_TYPE_PACKING_BASE_ORDER %>" />
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkout">
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
          $('#checkoutItem').click(function(){
             $(this).hide();
          });
        $(document).find('#skuGroup').attr("checked", true);

        $('.upc').change(function() {
          var productVariantRow = $(this).parents('.productVariantRow');
          var productVariantId = productVariantRow.find('.productVariantId').val();
          var upc = $(this).val();
          $.getJSON(
              $('#upcUpdateLink').attr('href'), {productVariant: productVariantId, upc: upc},
              function(responseData) {
                //Notify change
                window.location.reload();
              }
              );
        });

      });
    </script>
  </s:layout-component>

  <s:layout-component
      name="heading">Inventory Checkout for Gateway Order ID#${icBean.shippingOrder.gatewayOrderId}</s:layout-component>
  <s:layout-component name="content">
  <input type="hidden" id="commentType" value="${icBean.shippingOrder.baseOrder.commentType}">
  <input type="hidden" id="userComments" value="${icBean.shippingOrder.baseOrder.userComments}">

    <c:choose>
        <c:when test="${! hk:allItemsCheckedOut(icBean.shippingOrder)}">
          <div align="center" class="prom yellow help" style="height:30px; font-size:20px; color:red; font-weight:bold;">
            ATTENTION: All Units of Line Items (Order) are not checked out. Please insure they are checked out.
          </div>
        </c:when>
        <c:otherwise>
          <div align="center" class="prom yellow help" style="height:30px; font-size:20px; color:green; font-weight:bold;">
            All Units of Line Items (Order) are checked out. Order is ready to be packed.
          </div>
        </c:otherwise>
      </c:choose>
    <c:if test="${icBean.shippingOrder.baseOrder.commentType == commentTypePacking }" >
	    <br>
	    <div style="margin:5px;color:red;font-size:25px;">
		    User Instructions: ${icBean.shippingOrder.baseOrder.userComments}
	    </div>
    </c:if>

    <div style="display:inline;float:left;">

      <c:choose><c:when test="${empty icBean.skuGroups}">
        <c:choose>
          <c:when test="${icBean.upc == null}">
            <fieldset class="top_label">

              <div style="width:450px">
                <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" method="get" autocomplete="false">
                  <ul>
                    <s:hidden name="shippingOrder" value="${icBean.shippingOrder.id}"/>
                    <li><label>UPC (Barcode)</label>
                      <s:text name="upc" id="upc" size="50" style="padding:5px; width:125px;"/>
                    </li>
                    <li>
                      <div style="float:right;"><s:submit name="findSkuGroups" value="Search Batches"/></div>
                    </li>
                  </ul>
                </s:form>
              </div>
              <script language=javascript type=text/javascript>
                $('#upc').focus();
              </script>
            </fieldset>
            <div><s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" style="font-size:1.2em;">
              &lang;&lang;&lang;Back
              to Search Order</s:link></div>
          </c:when>
          <c:otherwise>
            <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
              <s:hidden name="shippingOrder" value="${icBean.shippingOrder.id}"/>
              <s:hidden name="upc" value="${icBean.upc}"/>
              <s:hidden name="lineItem" value="${icBean.lineItem.id}"/>
              There is NO Checked-in Batch of
              <br/><br/>

              <h2 style="color:blue">${icBean.productVariant.product.name} - ${icBean.productVariant.id}</h2>
              <br/>
              <b>Report the same to Admin and Category Manager ugently.</b>
            </s:form>
          </c:otherwise>
        </c:choose>
      </c:when>
        <c:otherwise>
          <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
            <s:hidden name="shippingOrder" value="${icBean.shippingOrder.id}"/>
            <s:hidden name="upc" value="${icBean.upc}"/>
            <s:hidden name="lineItem" value="${icBean.lineItem.id}"/>              
            <input type="hidden" name="skuItemBarcode"  value = "${icBean.skuItemBarcode.id}"/>
            <br/>
            <strong style="color:blue">${icBean.productVariant.product.name} - ${icBean.productVariant.id}
			<br/>MRP=${icBean.productVariant.markedPrice}</strong>
            <table>
              <thead>
              <tr>
                <th></th>
                <th>VariantID</th>
                <th>Details</th>
                <th>Batch No.</th>
                <th>Mfg. Date</th>
                <th>Expiry Date</th>
                <th>MRP</th>
                <th>Inv.</th>
                <th></th>
 
              </tr>
              </thead>
              <c:forEach items="${icBean.skuGroups}" var="skuGroup" varStatus="ctr">
                <c:set var="productVariant" value="${skuGroup.sku.productVariant}"/>
	              <c:choose>
		              <c:when test="${productVariant.markedPrice > skuGroup.mrp}">
			           <tr style="background:lightpink">
		              </c:when>
		              <c:otherwise>
			          <tr>
		              </c:otherwise>
	              </c:choose>

                  <td>
                    <c:choose>
                      <c:when test="${ctr.index == 0}">
                        <s:radio value="${skuGroup.id}" name="skuGroup" id="skuGroup"/>
                      </c:when>
                      <c:otherwise>
                        <s:radio value="${skuGroup.id}" name="skuGroup"/>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td>${productVariant.id}</td>
                  <td>${productVariant.optionsCommaSeparated}</td>
                  <td>${skuGroup.batchNumber}</td>
                  <td><fmt:formatDate value="${skuGroup.mfgDate}" pattern="yyyy-MM-dd"/></td>
                  <td><fmt:formatDate value="${skuGroup.expiryDate}" pattern="yyyy-MM-dd"/></td>
                  <td>${skuGroup.mrp} ${skuGroup.id}</td>
                  <td>${fn:length(hk:getInStockSkuItems(skuGroup))}</td>
	              <td>
		              <s:link beanclass="com.hk.web.action.admin.inventory.SkuGroupAction" target="_blank">
			              <s:param name="gatewayOrderId" value="${icBean.shippingOrder.gatewayOrderId}"/>
			              <s:param name="skuGroup" value="${skuGroup.id}"/>
                          <%--<s:param name="skuItemBarcode"  value = "${icBean.skuItemBarcode.id}"/>--%>
			              <img src="${pageContext.request.contextPath}/images/edit.gif" alt="Edit Batch"/>
		              </s:link>
	              </td>
                </tr>
              </c:forEach>
              <tfoot style="border-top:1px solid gray">
              <tr>
                <td colspan="6" align="left">
                  <s:submit id = "checkoutItem" name="selectItemFromSkuGroup" value="Checkout" style="font-size:2.0em;padding:4px"/>
                </td>
                <td colspan="3" align="right">
                  <c:if test="${hk:isFreeVariant(productVariant)}">
                    <s:submit class="freeCheckout"
                              name="inventoryCheckoutOfItemThatIsNotInOrderLineItem"
                              value="Checkout Free SKU"
                              style="font-size:0.9em;padding:1px"/>
                  </c:if>
                </td>
              </tr>
              </tfoot>
            </table>


            <script type="text/javascript">
            $('.freeCheckout').click(function() {
              var proceed = confirm('Are you sure, you want to do a FREE CHECKOUT?');
              if (!proceed) return false;
            });
          </script>
          </s:form>
        </c:otherwise>
      </c:choose>
    </div>
    <div style="display:inline;float:right;">
      <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" id="upcUpdateLink" event="updateUPC"></s:link>
      </div>
      <table class="align_top" width="100%">
        <thead>
        <tr>
          <th>Item</th>
          <th>ID</th>
          <th>UPC</th>
          <th>MRP</th>
          <th>Qty</th>
          <th width="50px;">Net Inventory</th>
          <th width="75px;">Checked Out Qty</th>
        </tr>
        </thead>
	    <c:set var="alertCount" value="0" />
        <c:forEach items="${icBean.shippingOrder.lineItems}" var="lineItem">
	       <c:set var="alertCount" value="${alertCount + hk:checkedoutItemsCount(lineItem)}"/>
          <c:set var="productVariant" value="${lineItem.sku.productVariant}"/>
          <tr class="productVariantRow">
            <input type="hidden" value="${productVariant.id}" class="productVariantId"/>
            <td>
                ${productVariant.product.name}
              <c:if test="${not empty productVariant.productOptions}">
                <br/><span class="gry">
                            <c:forEach items="${productVariant.productOptions}" var="productOption"
                                       varStatus="optionCtr">
                              ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                            </c:forEach>
                          </span>
              </c:if>
            </td>
            <c:choose><c:when
                test="${icBean.shippingOrder.orderStatus.id == lineItemStatusId_picking || icBean.shippingOrder.orderStatus.id == lineItemStatusId_shipped}">
              <td title="Click to fing SKU Groups/Batch">
                <s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" event="findSkuGroups">
                  ${productVariant.id}
                  <s:param name="shippingOrder" value="${icBean.shippingOrder}"/>
                  <s:param name="upc" value="${productVariant.id}"/>
                  <s:param name="lineItem" value="${lineItem.id}"/> </s:link>
              </td>
              <td>
                ${productVariant.upc}
              </td>
              <td>${lineItem.markedPrice}</td>
              <td>${lineItem.qty}</td>
              <td>${hk:netInventory(lineItem.sku)}</td>
              <td><span style="color:green; font-weight:bold;">[${hk:checkedoutItemsCount(lineItem)}]</span></td>
              <td>  <s:link beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction">
                Review it  <s:param name="lineItem" value="${lineItem.id}"/>                 
              </s:link>
              </td>   
              
              <td>  <s:link beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction">
                Fix it  <s:param name="lineItem" value="${lineItem.id}"/>                 
              </s:link>
              </td>            
            </c:when>
              <c:otherwise>
                <td colspan="5">${icBean.shippingOrder.orderStatus.name}</td>
              </c:otherwise>
            </c:choose>

          </tr>
        </c:forEach>
	      <script type="text/javascript">
		      var commentType = $('#commentType').val();
		      var upc = $('#upc').val();
		      if(${alertCount == 0} && upc == '' && commentType == ${commentTypePacking}) {
			      alert("User Instruction : " + $('#userComments').val());
		      }
	      </script>
      </table>
    </div>
  </s:layout-component>
</s:layout-render>