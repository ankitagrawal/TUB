<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create Replacement Order">
  <s:useActionBean beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction"
                   var="replacementOrderBean"/>
  <s:layout-component name="heading">
    Create Replacement Order
  </s:layout-component>

  <s:layout-component name="content">
    <script>
      $(document).ready(function() {
        $('#shippingOrderId').focus();

        /*    $('#show-add-new-form').click(function(event) {
         event.preventDefault();
         $('#new-default-courier-form').slideDown();
         });
         */
      })
    </script>
    <fieldset class="right_label">
      <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
        <label>Search Shipping Order</label>
        <br/><br/>
        <s:text name="shippingOrderId" id="shippingOrderId" style="width:200px;"/>
        <br/>
        <br/>
        <s:submit name="searchShippingOrder" value="Search"/>
      </s:form>
    </fieldset>
    <c:if test="${!empty mpaBean.courierServiceList}">
      <s:form beanclass="com.hk.web.action.admin.replacementOrder.ReplacementOrderAction">
        <fieldset style="float:left;">
          <table>
            <s:hidden name="shippingOrder" value="${replacementOrderBean.shippingOrderId}"/>
            <tr>
              <td>CustomerName:</td>
              <td>${replacementOrderBean.shippingOrder.baseOrder.user.name}</td>
              <td>SO date:</td>
              <td>${replacementOrderBean.shippingOrder.createDate}</td>
            </tr>
            <tr>
              <td>Email:</td>
              <td>${replacementOrderBean.shippingOrder.baseOrder.user.email}
              </td>
              <td>Address:</td>
              <td>
                  ${replacementOrderBean.shippingOrder.baseOrder.address.city}<br/>
                  ${replacementOrderBean.shippingOrder.baseOrder.address.state} -
                ( ${replacementOrderBean.shippingOrder.baseOrder.address.pin} ) -
                  ${replacementOrderBean.shippingOrder.baseOrder.address.phone}<br/>
              </td>
            </tr>
            <tr>
              <td>
                Is RTO:
              </td>
              <td>
                <s:radio value="1" name="isRto"/>
              </td>
              <td>
                Is Replacement:
              </td>
              <s:radio value="0" name="isRto"/>
            </tr>
          </table>
        </fieldset>

        <fieldset style="float:left;" id="is-rto">
          <table border="1">
            <thead>
              <th>S No.</th>
              <th>Product</th>
              <th>Original Qty</th>
              <th>Replacement Qty</th>
            </thead>
            <c:forEach items="${replacementOrderBean.shippingOrder.lineItems}" var="lineItem" varStatus="lineItemCtr">
              <tr>
                <td>${lineItemCtr +1}</td>
                <td>${lineItem.cartLineItem.productVariant.product.name}</td>
                <td>${lineItem.qty}</td>
                <td>${lineItem.qty}</td>
              </tr>
            </c:forEach>
          </table>
        </fieldset>
        <fieldset style="float:left;" id="is-rto">
          <c:forEach items="${replacementOrderBean.shippingOrder.lineItems}" var="lineItem" varStatus="lineItemCtr">
            <td>${lineItemCtr +1}</td>
            <td>${lineItem.cartLineItem.productVariant.product.name}</td>
            <td>${lineItem.qty}</td>
            <td><s:text name="lineItems[${lineItemCtr}].qty" value="0"></s:text> </td>
          </c:forEach>
        </fieldset>
        <s:submit name="createReplacementOrder" value="Generate Replacement Order" />
      </s:form>
    </c:if>

  </s:layout-component>
</s:layout-render>