<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
   <%
     WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  %>
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
              '  <td>' +
              '    <input type="hidden" name="poLineItems[' + nextIndex + '].id" />' +
              '    <input type="text" class="variant" name="poLineItems[' + nextIndex + '].productVariant"/>' +
              '  </td>' +
              '  <td class="pvDetails"></td>' +
              '  <td>' +
              '    <input type="text" name="poLineItems[' + nextIndex + '].qty" />' +
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
                  $('.variantDetails').html('<h2>Invalid Variant Id</h2>');
                }
              }
              );
        });

      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <div style="display:inline;float:left;">
      <h2>Create New PO</h2>
      <s:form beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction">
        <table>
          <tr>
            <s:hidden name="purchaseOrder.supplier" value="${pa.supplier.id}"/>
            <td>Supplier Name</td>
            <td>${pa.supplier.name}</td>
          </tr>
          <tr>
            <td>Supplier State</td>
            <td>${pa.supplier.state}</td>
          </tr>
          <tr>
            <td>Tax</td>
            <td>
              <c:choose>
                <c:when test="${pa.supplier.state == 'HARYANA'}">
                  Non - CST
                </c:when>
                <c:otherwise>
                  CST
                </c:otherwise>
              </c:choose>

            </td>
          </tr>
        
          <tr>
            <td>PO Number</td>
            <td><s:text name="purchaseOrder.poNumber"/></td>
          </tr>

          <tr>
            <td>For Warehouse</td>
            <td>
            <s:select name="purchaseOrder.warehouse">
              <c:forEach items="${whList}" var="wh">
                <s:option value="${wh.id}">${wh.identifier}</s:option>
              </c:forEach>
            </s:select>
          </td></tr>
          
        </table>

        <s:submit name="save" value="Create PO and Add Items >>>" class="buttons"/>
      </s:form>
    </div>
  </s:layout-component>

</s:layout-render>
