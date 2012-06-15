<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.order.TrackCourierAction" var="tca"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Chhotu Courier Services">
    <s:layout-component name="heading">Order Shipped through Chhotu Courier Services</s:layout-component>
    <s:layout-component name="rhsContent">
        <c:choose>
            <c:when test="${tca.chhotuCourierDelivery.myStatus == 'ERROR'}">
                <div class="clear"></div>
                <br/>
                <br/>
                <%--Checking courier name and forwarding to the relevant site--%>
                <div>
                    Sorry, there is no return of Information from Chhotu Courier Services please visit
                    <br/>
                    <a href="http://www.chhotu.in/" target="_blank">Visit Chhotu Courier</a>
                    and use shipment number=${tca.trackingId}

                </div>

            </c:when>
            <c:otherwise>
                <div>
                    <div><h2>Delivery Details</h2></div>
                    <div class="row">
                        <s:label name="Delivery Type :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.deliveryType}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Customer Name :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.customerName}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="COD Amount :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.codAmount}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Pincode :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.pinCode}" class="nameLabel"/>

                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Tracking Id :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.trackingId}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Shipment Status :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.shipmentStatus}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="My Status :" class="valueLabel"/>
                        <s:label name="${tca.chhotuCourierDelivery.myStatus}" class="nameLabel"/>
                    </div>
                    <div class="clear"></div>
                    <div class="row">
                        <s:label name="Delivery Date :" class="valueLabel"/>
                        <c:choose>
                            <c:when test="${tca.chhotuCourierDelivery.deliveryDate!=null}">
                                <s:label name="${tca.chhotuCourierDelivery.deliveryDate}" class="nameLabel"/>
                            </c:when>
                            <c:otherwise>
                                Not Delivered
                            </c:otherwise>
                        </c:choose>

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


