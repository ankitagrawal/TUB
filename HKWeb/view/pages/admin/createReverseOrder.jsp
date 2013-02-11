<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction" event="pre" var="reverseOrderAction"/>
<c:set var="pickupNotValid" value="${reverseOrderAction.exceededPolicyLimit}"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pickup Service">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
          $(document).ready(function() {             
	          if(${pickupNotValid}) {
		          alert("Pickup can be done only within 14 days after delivery.This limit has been exceeded.");
	          }


          });
    </script>

    </s:layout-component>


    <s:layout-component name="heading">Create Reverse Order</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction">
            <div>
          ShippingOrder Delivered : ${reverseOrderAction.shippingOrder.gatewayOrderId}
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
                  <c:forEach var="lineItem" items="${reverseOrderAction.shippingOrder.lineItems}" varStatus="ctr">
                      <tr>
                          <td>
                            ${lineItem.cartLineItem.productVariant.product.name}
                          </td>
                          <td>
                            ${lineItem.qty}</td>
                          <td>
                              <input type="text" name="itemMap[${lineItem.id}]" size="1"/>
                          </td>
                          <%--<s:hidden name="itemMap[${lineItem.id}]" class="hiddenMap" value="1"/>--%>
                          
                      </tr>
                  </c:forEach>
                  </tbody>
              </table>           
        <s:param name="shippingOrder" value="${reverseOrderAction.shippingOrder.id}"/>
        <s:submit name="submit" value="Submit" id="submit"/>
        </s:form>

    </s:layout-component>
</s:layout-render>


