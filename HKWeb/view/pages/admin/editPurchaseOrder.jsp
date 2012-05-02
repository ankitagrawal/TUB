<%@ page import="mhc.web.json.HealthkartResponse" %>
<%@ page import="mhc.service.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus" %>
<%@ page import="com.hk.constants.EnumImageSize" %>
<%@ page import="com.hk.constants.RoleConstants" %>
<%@ page import="mhc.service.dao.WarehouseDao" %>
<%@ page import="app.bootstrap.guice.InjectorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.EditPurchaseOrderAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
<%
    WarehouseDao warehouseDao = InjectorFactory.getInjector().getInstance(WarehouseDao.class);
    pageContext.setAttribute("whList", warehouseDao.listAll());
%>
<c:set var="poApproved" value="<%=EnumPurchaseOrderStatus.Approved.getId()%>"/>
<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
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
            '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
            '<td>' + Math.round(nextIndex + 1) + '.</td>' +
            '<td></td>' +
            '  <td>' +
            '    <input type="hidden" name="poLineItems[' + nextIndex + '].id" />' +
            '    <input type="text" class="variant" name="poLineItems[' + nextIndex + '].productVariant"/>' +
            '  </td>' +
            '<td></td>' +
            '  <td class="pvDetails"></td>' +
            '<td></td>' +
            '  <td>' +
            '    <input type="text" name="poLineItems[' + nextIndex + '].qty" class="quantity" />' +
            '  </td>' +
            '  <td>' +
            '    <input class="costPrice" type="text" name="poLineItems[' + nextIndex + '].costPrice" />' +
            '  </td>' +
            '  <td>' +
            '    <input class="mrp" type="text" name="poLineItems[' + nextIndex + '].mrp" />' +
            '  </td>' +
            '</tr>';

        $('#poTable').append(newRowHtml);

        return false;
      });

      $('.variant').live("change", function() {
        var variantRow = $(this).parents('.lineItemRow');
        var productVariantId = variantRow.find('.variant').val();
        var productVariantDetails = variantRow.find('.pvDetails');
        $.getJSON(
            $('#pvInfoLink').attr('href'), {productVariantId: productVariantId},
            function(res) {
              if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                variantRow.find('.mrp').val(res.data.variant.markedPrice);
                variantRow.find('.costPrice').val(res.data.variant.costPrice);
                productVariantDetails.html(
                    res.data.product + '<br/>' +
                    res.data.options
                    );
              } else {
                $('.variantDetails').html('<h2>'+res.message+'</h2>');
              }
            }
		        );
      });

	   $('.requiredFieldValidator').click(function() {
		    var quantity = $('.quantity').val();
		    if (quantity == "" || isNaN(quantity)) {
			    alert("Please enter a valid quantity.");
			    return false;
		    }
		    var statusSelected=$('.status').find('option:selected');
		    var approver = $('.approver').find('option:selected');
		   var test =approver.text();
		    if(statusSelected.text() == "Sent For Approval" && approver.text()=="-Select Approver-"){
			    alert("Approver Not Selected.");
			    return false;
		    }
	    });


    });
  </script>
  <%--<style type="text/css">
    input {
      text-transform: uppercase;
    }
  </style>--%>
</s:layout-component>

<s:layout-component name="content">
<div style="display: none;">
  <s:link beanclass="web.action.admin.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
</div>
<h2>Edit PO# ${pa.purchaseOrder.id}</h2>
<s:form beanclass="web.action.admin.EditPurchaseOrderAction">
  <s:hidden name="purchaseOrder" value="${pa.purchaseOrder}"/>
  <table>
    <tr>
      <td>Supplier Name</td>
      <td>${pa.purchaseOrder.supplier.name}</td>

      <td>Supplier State</td>
      <td>${pa.purchaseOrder.supplier.state}</td>
      <td>Tax</td>
      <td>
        <c:choose>
          <c:when test="${pa.purchaseOrder.supplier.state == pa.purchaseOrder.warehouse.state}">
            Non - CST
          </c:when>
          <c:otherwise>
            CST
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td>PO Date</td>
      <td>
        <s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.poDate"/></td>

      <td>PO Number</td>
      <td><s:text name="purchaseOrder.poNumber"/></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>Payable<br/>Adv. Payment</td>
      <td class="payable">
        <fmt:formatNumber value="${actionBean.purchaseOrderDto.totalPayable}" type="currency" currencySymbol=" "
                          maxFractionDigits="0"/>
        <br/> <s:text name="purchaseOrder.advPayment"/>
      </td>

      <td>Payment Details<br/><span class="sml gry">(eg. Adv. Amount)</span></td>
      <td><s:textarea name="purchaseOrder.paymentDetails" style="height:60px;"/></td>
      <td></td>
      <td></td>
    </tr>

    <tr>
      <td>Status</td>
      <td class="status">
        <c:if test="${pa.purchaseOrder.purchaseOrderStatus.id >= poApproved}">
          <shiro:hasRole name="<%=RoleConstants.PO_APPROVER%>">
            <s:select name="purchaseOrder.purchaseOrderStatus" value="${pa.purchaseOrder.purchaseOrderStatus.id}">
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseOrderStatusList"
                                         value="id" label="name"/>
            </s:select>
          </shiro:hasRole>
          <shiro:lacksRole name="<%=RoleConstants.PO_APPROVER%>">
            ${pa.purchaseOrder.purchaseOrderStatus.name}
          </shiro:lacksRole>
        </c:if>
        <c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
          <s:select name="purchaseOrder.purchaseOrderStatus" value="${pa.purchaseOrder.purchaseOrderStatus.id}">
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="purchaseOrderStatusList"
                                       value="id" label="name"/>
          </s:select>
        </c:if>
      </td>

      <td>Approver</td>
      <td class="approver"><s:select name="purchaseOrder.approvedBy" value="${pa.purchaseOrder.approvedBy}">
        <s:option value="">-Select Approver-</s:option>
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="approverList" value="id"
                                   label="name"/>
      </s:select></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>Est. Delivery Date</td>
      <td>
        <s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.estDelDate"/></td>

      <td>Est Payment Date</td>
      <td>
        <s:text class="date_input" formatPattern="yyyy-MM-dd" name="purchaseOrder.estPaymentDate"/></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td>For Warehouse</td>
      <td>
      <s:hidden name="purchaseOrder.warehouse" value="${pa.purchaseOrder.warehouse}"/>
       ${pa.purchaseOrder.warehouse}
    </td></tr>
  </table>

  <table border="1">
    <thead>
    <tr>
      <th>S.No.</th>
      <th></th>
      <th>VariantID</th>
      <th>UPC</th>
      <th>Details</th>
      <th>Tax<br/>Category</th>
      <th>Qty</th>
      <th>Cost Price<br/>(Without TAX)</th>
      <th>MRP</th>
      <th>Taxable</th>
      <th>Tax</th>
      <th>Surcharge</th>
      <th>Payable</th>

    </tr>
    </thead>
    <tbody id="poTable">
    <c:forEach var="poLineItemDto" items="${pa.purchaseOrderDto.poLineItemDtoList}" varStatus="ctr">
      <c:set value="${poLineItemDto.poLineItem.sku.productVariant}" var="productVariant"/>
      <c:set value="${poLineItemDto.poLineItem.sku}" var="sku"/>

      <s:hidden name="poLineItems[${ctr.index}]" value="${poLineItemDto.poLineItem.id}"/>
      <s:hidden name="poLineItems[${ctr.index}].productVariant" value="${productVariant.id}"/>
      <%--<s:hidden name="poLineItems[${ctr.index}].sku" value="${sku.id}"/>--%>

      <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
        <td>${ctr.index+1}.</td>
        <td>
          <div class='img48' style="vertical-align:top;">
            <c:choose>
              <c:when test="${productVariant.product.mainImageId != null}">
                <hk:productImage imageId="${productVariant.product.mainImageId}"
                                 size="<%=EnumImageSize.TinySize%>"/>
              </c:when>
              <c:otherwise>
                <img class="prod48"
                     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${productVariant.product.id}.jpg"
                     alt="${productVariant.product.name}"/>
              </c:otherwise>
            </c:choose>
          </div>
        </td>
        <td>
            ${productVariant.id}
          <s:hidden class="variant" name="poLineItems[${ctr.index}].productVariant"
                    value="${poLineItemDto.poLineItem.productVariant.id}"/>
        </td>
        <td>${productVariant.upc}</td>
        <td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
        </td>
        <td>
          <fmt:formatNumber value="${sku.tax.value * 100}" maxFractionDigits="2"/>
        </td>
        <td>
          <s:text name="poLineItems[${ctr.index}].qty" value="${poLineItemDto.poLineItem.qty}" class="quantity"/>
        </td>
        <td>
          <s:text class="costPrice" name="poLineItems[${ctr.index}].costPrice"
                  value="${poLineItemDto.poLineItem.costPrice}"/>
        </td>
        <td>
          <s:text class="mrp" name="poLineItems[${ctr.index}].mrp" value="${poLineItemDto.poLineItem.mrp}"/>
        </td>
        <td>
          <fmt:formatNumber value="${poLineItemDto.taxable}" maxFractionDigits="2"/>
        </td>
        <td>
          <fmt:formatNumber value="${poLineItemDto.tax}" maxFractionDigits="2"/>
        </td>
        <td>
          <fmt:formatNumber value="${poLineItemDto.surcharge}" maxFractionDigits="2"/>
        </td>
        <td>
          <fmt:formatNumber value="${poLineItemDto.payable}" maxFractionDigits="2"/>
        </td>
      </tr>
    </c:forEach>
    </tbody>
    <tfoot>
    <tr>
      <td colspan="9">Total</td>
      <td><fmt:formatNumber value="${pa.purchaseOrderDto.totalTaxable}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${pa.purchaseOrderDto.totalTax}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${pa.purchaseOrderDto.totalSurcharge}" maxFractionDigits="2"/></td>
      <td><fmt:formatNumber value="${pa.purchaseOrderDto.totalPayable}" maxFractionDigits="2"/></td>
    </tr>
    </tfoot>
  </table>
  <div class="variantDetails info"></div>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">

  <c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
    <a href="editPurchaseOrder.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
    <s:submit name="save" value="Save" class="requiredFieldValidator"/>
  </c:if>

  <c:if test="${pa.purchaseOrder.purchaseOrderStatus.id >= poApproved}">
    <shiro:hasRole name="<%=RoleConstants.PO_APPROVER%>">
      <a href="editPurchaseOrder.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
      <s:submit name="save" value="Save" class="requiredFieldValidator"/>
    </shiro:hasRole>
  </c:if>
</shiro:hasAnyRoles>


</s:form>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">
  <c:if test="${pa.purchaseOrder.purchaseOrderStatus.id < poApproved}">
    <hr/>

    <fieldset>
      <legend>Upload Excel to Create PO LineItems</legend>
      <br/>
      <span class="large gry">(VARIANT_ID, QTY, COST, MRP) as excel headers</span>
      <br/><br/>
      <s:form beanclass="web.action.admin.EditPurchaseOrderAction">
        <h2>File to Upload: <s:file name="fileBean" size="30"/></h2>
        <s:hidden name="purchaseOrder" value="${pa.purchaseOrder.id}"/>

        <div class="buttons">
          <s:submit name="parse" value="Create PO LineItems"/>
        </div>
      </s:form>
    </fieldset>
  </c:if>
</shiro:hasAnyRoles>
</s:layout-component>

</s:layout-render>
