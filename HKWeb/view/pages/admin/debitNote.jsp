<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Debit Note">
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
            '<td>' +
            '    <input type="hidden" name="debitNoteLineItems[' + nextIndex + '].id" />' +
            '    <input type="text" class="variant" name="debitNoteLineItems[' + nextIndex + '].productVariant"/>' +
            '  </td>' +
            '<td></td>' +
            '  <td class="pvDetails"></td>' +
            '<td></td>' +
            '  <td>' +
            '    <input type="text" name="debitNoteLineItems[' + nextIndex + '].qty" />' +
            '  </td>' +
            '  <td>' +
            '    <input class="costPrice" type="text" name="debitNoteLineItems[' + nextIndex + '].costPrice" />' +
            '  </td>' +
            '  <td>' +
            '    <input class="mrp" type="text" name="debitNoteLineItems[' + nextIndex + '].mrp" />' +
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
            $('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${whAction.setWarehouse.id}},
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


    });
  </script>
</s:layout-component>
<s:layout-component name="heading">
  Edit Debit Note # ${pa.debitNote.id}
</s:layout-component>
<s:layout-component name="content">
  <div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink" event="getPVDetails"></s:link>
  </div>

  <s:form beanclass="com.hk.web.action.admin.inventory.DebitNoteAction">
    <s:hidden name="debitNote" value="${pa.debitNote.id}"/>
    <s:hidden name="debitNote.supplier" value="${pa.debitNote.supplier.id}"/>
    <table>
      <tr>
        <td>Supplier Name</td>
        <td>
        ${pa.debitNote.supplier.name}</td>

        <td>Supplier State</td>
        <td>${pa.debitNote.supplier.state}</td>

        <td>Tax</td>
        <td>
          <c:choose>
            <c:when test="${pa.debitNote.supplier.state == 'HARYANA'}">
              Non - CST
            </c:when>
            <c:otherwise>
              CST
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <td>Create Date</td>
        <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="debitNote.createDate"/></td>
        <td>Debit to Supplier</td>
        <td><s:checkbox name="debitNote.isDebitToSupplier"/></td>
        <td>Status</td>
        <td><s:select name="debitNote.debitNoteStatus" value="${pa.debitNote.debitNoteStatus.id}">
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="debitNoteStatusList"
                                     value="id" label="name"/>
        </s:select></td>

      </tr>
      <tr>
      <td>For Warehouse</td>
      <td>
     <s:hidden name="debitNote.warehouse" value="${whAction.setWarehouse.id}"/>
        ${whAction.setWarehouse.city}
    </td></tr>
    </table>

    <table border="1">
      <thead>
      <tr>
        <th>S.No.</th>
        <th>VariantID</th>
        <th>UPC</th>
        <th>Details</th>
        <th>Tax<br/>Category</th>
        <th>Debit Qty</th>
        <th>Cost Price<br/>(Without TAX)</th>
        <th>MRP</th>
        <th>Taxable</th>
        <th>Tax</th>
        <th>Surcharge</th>
        <th>Payable</th>

      </tr>
      </thead>
      <tbody id="poTable">
      <c:forEach var="debitNoteLineItemDto" items="${pa.debitNoteDto.debitNoteLineItemDtoList}" varStatus="ctr">
        <c:set var="sku" value="${debitNoteLineItemDto.debitNoteLineItem.sku}"/>
        <c:set var="productVariant" value="${debitNoteLineItemDto.debitNoteLineItem.sku.productVariant}"/>
        <s:hidden name="debitNoteLineItems[${ctr.index}].id" value="${debitNoteLineItemDto.debitNoteLineItem.id}"/>
        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
          <td>${ctr.index+1}.</td>

          <td>
              ${productVariant.id}
            <%--<s:hidden class="variant" name="debitNoteLineItems[${ctr.index}].sku"
                      value="${sku.id}"/>--%>
            <s:hidden class="variant" name="debitNoteLineItems[${ctr.index}].productVariant"
                    value="${productVariant.id}"/>
          </td>
          <td>${productVariant.upc}</td>
          <td>${productVariant.product.name}<br/>${productVariant.optionsCommaSeparated}
          </td>
          <td>
            <fmt:formatNumber value="${sku.tax.value * 100}"
                              maxFractionDigits="2"/>
          </td>
          <td>
            <s:text name="debitNoteLineItems[${ctr.index}].qty" value="${debitNoteLineItemDto.debitNoteLineItem.qty}"/>
          </td>
          <td>
            <shiro:hasRole name="<%=RoleConstants.FINANCE%>">
              <s:text name="debitNoteLineItems[${ctr.index}].costPrice"
                      value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
            </shiro:hasRole>
            <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
              ${debitNoteLineItemDto.debitNoteLineItem.costPrice}
              <s:hidden name="debitNoteLineItems[${ctr.index}].costPrice"
                        value="${debitNoteLineItemDto.debitNoteLineItem.costPrice}"/>
            </shiro:lacksRole>
          </td>
          <td>
            <shiro:hasRole name="<%=RoleConstants.FINANCE%>">
              <s:text class="mrp" name="debitNoteLineItems[${ctr.index}].mrp"
                      value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
            </shiro:hasRole>
            <shiro:lacksRole name="<%=RoleConstants.FINANCE%>">
              ${debitNoteLineItemDto.debitNoteLineItem.mrp}
              <s:hidden class="mrp" name="debitNoteLineItems[${ctr.index}].mrp"
                        value="${debitNoteLineItemDto.debitNoteLineItem.mrp}"/>
            </shiro:lacksRole>
          </td>

          <td>
            <fmt:formatNumber value="${debitNoteLineItemDto.taxable}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${debitNoteLineItemDto.tax}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${debitNoteLineItemDto.surcharge}" maxFractionDigits="2"/>
          </td>
          <td>
            <fmt:formatNumber value="${debitNoteLineItemDto.payable}" maxFractionDigits="2"/>
          </td>
        </tr>
      </c:forEach>
      </tbody>
      <tfoot>
      <tr>
        <td colspan="8">Total</td>
        <td><fmt:formatNumber value="${pa.debitNoteDto.totalTaxable}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${pa.debitNoteDto.totalTax}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${pa.debitNoteDto.totalSurcharge}" maxFractionDigits="2"/></td>
        <td><fmt:formatNumber value="${pa.debitNoteDto.totalPayable}" maxFractionDigits="2"/></td>
      </tr>
      </tfoot>
    </table>
    <div class="variantDetails info"></div>
    <br/>
    <a href="debitNote.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

    <s:submit name="save" value="Save"/>
  </s:form>

</s:layout-component>

</s:layout-render>
