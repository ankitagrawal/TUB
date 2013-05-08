<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.courier.ReverseOrderTypeConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction" event="pre" var="reverseOrderAction"/>
<c:set var="pickupNotValid" value="${reverseOrderAction.exceededPolicyLimit}"/>
<c:set var="orderTypeList" value="<%=ReverseOrderTypeConstants.getReverseOrderTypes()%>"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Order">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
            $(document).ready(function() {
                if (${pickupNotValid}) {
                    alert("Customer Returns only allowed within 14 days after delivery. This limit has been exceeded.");
                    if(!confirm("Are you sure you want to proceed ?")){
                        return false;
                    }
                    <%--window.location.href="<%=request.getContextPath()%>/admin";--%>
                }

                $('#validateOnSubmit').click(function() {
                    var bool = true;
                    $('.returnQty').each(function() {
                        var qty = $(this).val();

                        if (isNaN(qty)) {
                            alert("Quantity must be in Numbers Only");
                            bool = false;
                            return false;
                        }
                        var lineItemQty = Number($(this).parent().parent().children('td.lineItem').children('.lineItemQty').html());
                        if(qty == null || qty == ""){
                            alert("Enter quantity for all items. Enter 0 for items not returning");
                            bool = false;
                            return false;
                        }
//                        alert(qty);
//                        alert(lineItemQty);
                        if (qty > lineItemQty) {
                            alert("Return quantity is greater that Qty Sent for some item(s)");
                            bool = false;
                            return false;
                        }
                    });

                    var reason = $('#returnReason').val();
                    if(reason == null || reason == ""){
                        alert("Please enter a reason for return");
                        return false;
                    }

                    if (!bool) return false;

                });
            });
    </script>

    </s:layout-component>


    <s:layout-component name="heading">Create Reverse Order</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction">
            <div>
          Shipping Order Delivered : ${reverseOrderAction.shippingOrder.gatewayOrderId}
          </div>
          <s:errors/>
            <p></p>
          <h4>Reverse Order Items:</h4>

              <table border="1">
                  <thead>
                  <th>Item</th>
                  <th>Qty sent</th>
                  <th>Qty to return</th>
                  </thead>

                  <tbody>
                  <c:forEach var="lineItem" items="${reverseOrderAction.shippingOrder.lineItems}">
                      <tr>
                          <td>
                            ${lineItem.cartLineItem.productVariant.product.name}
                          </td>
                          <td class="lineItem">
                            <span class="lineItemQty">${lineItem.qty}</span>
                          </td>
                          <td>
                              <input type="text" name="itemMap[${lineItem.id}]" size="1" class="returnQty"/>
                          </td>
                          
                      </tr>
                  </c:forEach>
                  </tbody>
              </table>
            <p>
            <label>Reason to Return :</label>
            <s:select name="returnOrderReason" id="returnReason" >
                <s:option value="">-Select Reason-</s:option>
                <s:option value="Damaged Product">Damaged Product</s:option>
                <s:option value="Expired Product">Expired Product</s:option>
                <s:option value="Wrong Product">Wrong Product</s:option>
                <s:option value="Not Interested">Not Interested</s:option>
            </s:select>
            <p>
            <label>Select Reverse Order Type :</label>
               <s:select name="reverseOrderType" value="<%=ReverseOrderTypeConstants.Healthkart_Managed_Courier%>" >
                    <c:forEach items="${orderTypeList}" var="orderType">
                        <s:option value="${orderType}">${orderType}</s:option>
                     </c:forEach>
                </s:select>
            </p>
            <p></p>
        <s:param name="shippingOrder" value="${reverseOrderAction.shippingOrder.id}"/>
        <s:submit name="submit" value="Submit" id="validateOnSubmit"/>
        </s:form>

    </s:layout-component>
</s:layout-render>


