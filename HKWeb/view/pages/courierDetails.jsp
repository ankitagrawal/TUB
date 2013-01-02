<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="DTDCList" value="<%=EnumCourier.getDTDCCouriers()%>"/>
<c:set var="BluedartList" value="<%=EnumCourier.getBlueDartCouriers()%>"/>
<c:set var="DelhiveryList" value="<%=EnumCourier.getDelhiveryCourierIds()%>"/>
<c:set var="FedexList" value="<%=EnumCourier.getFedexCouriers()%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.order.TrackCourierAction" var="tca"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Courier Details">
    <s:layout-component name="heading">Order Shipped through ${tca.courierName} Courier Services</s:layout-component>
    <s:layout-component name="rhsContent">
        <c:choose>
            <c:when test="${tca.status == null}">
                <div class="clear"></div>
                <br/>
                <br/>
                <%--Checking courier name and forwarding to the relevant site--%>
                <div>
                    Sorry, there is no return of Information from ${tca.courierName} Courier Services
                    <br/>
                    Please
                    <c:if test="${hk:collectionContains(DTDCList, tca.courierId)}"><%--"${DTDCListtca.courierName =='DTDC'}">--%>
                        <a href="http://www.dtdc.in/" target="_blank">Visit ${tca.courierName} Courier</a><br/><br/>
                        Use Reference number=${tca.trackingId}
                    </c:if>

                    <c:if test="${hk:collectionContains(DelhiveryList, tca.courierId)}">
                        <a href="http://www.delhivery.com/" target="_blank">Visit Delhivery Courier</a>
                        and use order number=${tca.shippingOrder.gatewayOrderId}
                    </c:if>

                    <c:if test="${tca.courierName =='Chhotu'}">
                        <a href="http://www.chhotu.in/" target="_blank">Visit Chhotu Courier</a>
                        and use shipment number=${tca.trackingId}
                    </c:if>

                    <c:if test="${hk:collectionContains(BluedartList, tca.courierId)}">
                        <a href="http://www.bluedart.com/" target="_blank">Visit BlueDart Courier</a><br/><br/>
                        Use Reference number=${tca.shippingOrder.gatewayOrderId}
                        <br/>
                        Waybill number=${tca.trackingId}
                    </c:if>

                     <c:if test="${tca.courierName == 'Quantium'}">
                        <a href="http://www.quantiumsolutions.com/" target="_blank">Visit Quantium Courier</a><br/><br/>
                        Use Reference number=${tca.shippingOrder.gatewayOrderId}
                        <br/>
                        Waybill number=${tca.trackingId}
                     </c:if>

                    <c:if test="${hk:collectionContains(FedexList, tca.courierId)}">
                        <a href="http://www.fedex.com/in/" target="_blank">Visit Fedex Courier</a><br/><br/>
                        Use Reference number=${tca.shippingOrder.gatewayOrderId}
                        <br/>
                        Tracking number=${tca.trackingId}
                    </c:if>
                </div>

            </c:when>
            <c:otherwise>
                <div>
                    <div class="row">
                        <s:label name="Customer Name:" class="valueLabel"/>
                        <%--<s:label name="${tca.shippingOrder.baseOrder.user.name} " class="nameLabel"/>--%>
                        ${tca.shippingOrder.baseOrder.user.name}
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Order Id:" class="valueLabel"/>
<%--
                        <s:label name="${tca.shippingOrder.baseOrder.id} " class="nameLabel"/>
--%>
                        ${tca.shippingOrder.baseOrder.id}
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Ship date:" class="valueLabel"/>
                        <%--<s:label name="${tca.shippingOrder.shipment.shipDate} " class="nameLabel"/>--%>
                        ${tca.shippingOrder.shipment.shipDate}

                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Tracking Id:" class="valueLabel"/>
                        <c:choose>
                            <c:when test="${tca.courierName =='Delhivery'}">
                                <%--<s:label name="${tca.awb} " class="nameLabel"/>--%>
                                  ${tca.awb}
                            </c:when>
                            <c:otherwise>
<%--
                                <s:label name="${tca.trackingId} " class="nameLabel"/>
--%>
                                ${tca.trackingId}
                            </c:otherwise>
                        </c:choose>

                    </div>
                    <div class="clear"></div>
                    <c:if test="${tca.courierName =='Delhivery'}">
                        <div class="row">
                            <s:label name="Payment Mode:" class="valueLabel"/>
<%--
                            <s:label name=" ${tca.paymentType} " class="nameLabel"/>
--%>
                            ${tca.paymentType}
                        </div>
                        <div class="clear"></div>

                    </c:if>
                    <div class="row">
                        <s:label name="Shipment Status:" class="valueLabel"/>
<%--
                        <s:label name="${tca.status} " class="nameLabel"/>
--%>
                        ${tca.status}
                    </div>
                    <div class="clear"></div>
                </div>
            </c:otherwise>
        </c:choose>

    </s:layout-component>
</s:layout-render>
<style type="text/css">
    .nameLabel {
        margin-top: 20px;
    }

    .valueLabel {
        float: left;
        margin-left: 10px;
        width: 150px;
    }

    .row {
        margin-top: 20px;
    }

</style>

