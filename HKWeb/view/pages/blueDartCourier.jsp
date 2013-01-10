<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@include file="/includes/_taglibInclude.jsp" %>--%>
<%--<s:useActionBean beanclass="com.hk.web.action.core.order.TrackCourierAction" var="tca"/>--%>
<%--<s:layout-render name="/layouts/default.jsp" pageTitle="BlueDart Courier Services">--%>
  <%--<s:layout-component name="heading">Order Shipped through BlueDart Courier Services</s:layout-component>--%>
  <%--<s:layout-component name="rhsContent">--%>
    <%--<c:choose>--%>
      <%--<c:when test="${tca.status== null}">--%>
        <%--<div class="clear"></div>--%>
        <%--<br/>--%>
        <%--<br/>--%>

        <%--<div>--%>
          <%--Sorry, there is no return of Information from BlueDart Courier Services--%>
          <%--<br/>--%>
          <%--please--%>
          <%--<a href="http://www.bluedart.com/" target="_blank">Visit BlueDart Courier</a><br/><br/>--%>
          <%--Use Reference number=${tca.shippingOrder.gatewayOrderId}--%>
          <%--<br/>--%>
          <%--Waybill number=${tca.trackingId}--%>
        <%--</div>--%>

      <%--</c:when>--%>
      <%--<c:otherwise>--%>
        <%--<div>--%>
          <%--<div class="row">--%>
            <%--<s:label name="Customer Name:" class="valueLabel"/>--%>
            <%--<s:label name="${tca.shippingOrder.baseOrder.user.name}" class="nameLabel"/>--%>
          <%--</div>--%>
          <%--<div class="clear"></div>--%>
          <%--<div class="row">--%>
            <%--<s:label name="Order Id:" class="valueLabel"/>--%>
            <%--<s:label name="${tca.shippingOrder.baseOrder.id}" class="nameLabel"/>--%>
          <%--</div>--%>
          <%--<div class="clear"></div>--%>
          <%--<div class="row">--%>
            <%--<s:label name="Ship date:" class="valueLabel"/>--%>
            <%--<s:label name="${tca.shippingOrder.shipment.shipDate}" class="nameLabel"/>--%>
          <%--</div>--%>
          <%--<div class="clear"></div>--%>
          <%--<div class="row">--%>
            <%--<s:label name="Tracking Id:" class="valueLabel"/>--%>
            <%--<s:label name="${tca.trackingId}" class="nameLabel"/>--%>
          <%--</div>--%>
          <%--<div class="clear"></div>--%>
          <%--<div class="row">--%>
            <%--<s:label name="Shipment Status:" class="valueLabel"/>--%>
            <%--<s:label name="${tca.status}" class="nameLabel"/>--%>
          <%--</div>--%>
          <%--<div class="clear"></div>--%>
        <%--</div>--%>
      <%--</c:otherwise>--%>
    <%--</c:choose>--%>

  <%--</s:layout-component>--%>
<%--</s:layout-render>--%>
<%--<style type="text/css">--%>
  <%--.nameLabel {--%>
    <%--margin-top: 20px;--%>
  <%--}--%>

  <%--.valueLabel {--%>
    <%--float: left;--%>
    <%--margin-left: 10px;--%>
    <%--width: 150px;--%>
  <%--}--%>

  <%--.row {--%>
    <%--margin-top: 20px;--%>
  <%--}--%>

<%--</style>--%>

