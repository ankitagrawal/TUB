<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.EditShippingOrderAction" var="orderAdmin" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Order">
  <s:layout-component name="heading">Edit Order#${orderAdmin.shippingOrder.id}</s:layout-component>
  <s:layout-component name="modal">
    <div class="jqmWindow" id="orderDumpLogWindow"></div>
  </s:layout-component>

  <s:layout-component name="content">
    <s:errors/>
    <s:form beanclass="com.hk.web.action.admin.shippingOrder.EditShippingOrderAction">
      <s:hidden name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
      <table class="cont" width="100%">
        <thead>
        <tr>
          <th>Order Info</th>
          <th>Invoice Details (Variant/Qty)</th>
        </tr>
        </thead>
        <c:set var="shippingOrder" value="${orderAdmin.shippingOrder}"/>
        <tr>
          <td>
            <span class="upc lgry sml">ID</span>
            <strong><span class="or"> ${shippingOrder.id}</span></strong>
            <br/>
            <span class="upc lgry sml">GID</span>
            <strong><span class="or"> ${shippingOrder.gatewayOrderId}</span></strong><br/>
          </td>
          <td class="has_table">
            <table>
              <c:forEach items="${shippingOrder.lineItems}" var="lineItem">
                <tr>
                  <td width="">${lineItem.sku.productVariant.product.name}
                    &nbsp;&nbsp;&nbsp;<span class="sml gry em">
                        ${lineItem.sku.productVariant.optionsCommaSeparated}
                    </span>
                    &nbsp;&nbsp;&nbsp;${lineItem.qty}
                    &nbsp;&nbsp;&nbsp;
                    <s:select name="skuMap[${lineItem.sku.id}]"
                              value="${lineItem.sku}">
                      <c:forEach items="${hk:getMatchingSku(lineItem.sku)}" var="sku">
                        <c:if test="${sku.productVariant.hkPrice == lineItem.sku.productVariant.hkPrice}">
                          <s:option value="${sku.id}">${sku.productVariant.id}
                            <c:forEach items="${sku.productVariant.productOptions}" var="productOption">
                              ${productOption.name}:${productOption.value};
                            </c:forEach>
                          </s:option>
                        </c:if>
                      </c:forEach>
                    </s:select>
                  </td>
                </tr>
              </c:forEach>
            </table>
          </td>
        </tr>
      </table>
      <div class="buttons" style="margin-left: 80%;"><s:submit name="editOrder" value="Edit Order"/></div>
    </s:form>
  </s:layout-component>
</s:layout-render>
