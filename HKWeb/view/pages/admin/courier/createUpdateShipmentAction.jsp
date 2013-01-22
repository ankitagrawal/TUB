<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 8, 2013
  Time: 5:57:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.shipment.EnumShipmentServiceType" %>
<%@ page import="com.hk.constants.shipment.EnumBoxSize" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction" var="cusa"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Update Shipment">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function(){
                var estimatedWeight = ${cusa.estimatedWeight};
                $('#search').click(function(){
                    var gateWayOrderId = $('#gatewayOrderId').val();
                    if(gateWayOrderId == "" || gateWayOrderId == null){
                        alert("Gateway Order can't be Empty");
                        return false;
                    }
                });
                $('#validate').click(function(){
                    var boxSize = $('#boxSize').val();
                    var boxWeight = $('#boxWeight').val();
                    var weightDiff = 0.2;
                    var minWeight = (1-weightDiff)*estimatedWeight;
                    var maxWeight = (1+weightDiff)*estimatedWeight;
                    if(boxSize == "" || boxSize == null || boxWeight == "" || boxWeight == null){
                        alert("Box Size or Box Weight can't be Left Empty");
                        return false;
                    }
                    else if(parseFloat(boxWeight)< minWeight || parseFloat(boxWeight) > maxWeight){
                        alert("Box Weight is Out of Range");
                        return false;
                    }
                    else if(isNaN(boxSize)){
                        alert("Box Size must contain only numbers");
                        return false;
                    }
                });
            });
        </script>
    </s:layout-component>
    <s:layout-component name="content">
        <c:set var="userDeliveryComment" value="<%=MasterDataDao.USER_COMMENT_TYPE_DELIVERY_BASE_ORDER%>"/>
        <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">
            <fieldset>
            <label>Enter SO Gateway Id</label>
            <s:text name="gatewayOrderId" id="gatewayOrderId"/>
                <br><br>
            <s:submit name="searchShipment" value="Search" id="search"/>
            </fieldset>
            <div class="clear"></div>
            <fieldset>
            <c:if test="${cusa.shippingOrder!=null}">
                <c:if test="${userDeliveryComment eq cusa.shippingOrder.baseOrder.commentType}">
                    User comment and Type Related to Base Order # <h2 style="color:blue">${cusa.shippingOrder.baseOrder.id}</h2> is :-
                    <br>
                    User Comment :- <h3 style="color:red;">${cusa.shippingOrder.baseOrder.comments}</h3>
                </c:if>
            </c:if>
            </fieldset>
            <fieldset>
            <c:if test="${cusa.shipment!=null}">
                <table class="zebra_vert">
                    <tr>
                        <thead>
                        <th>AWB Tracking ID</th>
                        <th>Courier Name</th>
                        <th>Shipping Order</th>
                        <th>Shipment Service Type</th>
                        <th>Box Size</th>
                        <th>Box Weight</th>
                        <th>Packer Name</th>
                        <th>Picker Name</th>
                        </thead>
                    </tr>
                    <tr>
                        <s:hidden name="shipment.id" value="${cusa.shipment.id}"/>
                        <s:hidden name="shipment.shipDate" value="${cusa.shipment.shipDate}"/>
                        <s:hidden name="shipment.deliveryDate" value="${cusa.shipment.deliveryDate}"/>
                        <s:hidden name="shipment.emailSent" value="${cusa.shipment.emailSent}"/>
                        <s:hidden name="shipment.collectionCharge" value="${cusa.shipment.collectionCharge}" />
                        <s:hidden name="shipment.shipmentCharge" value="${cusa.shipment.shipmentCharge}"/>
                        <s:hidden name="shipment.returnDate" value="${cusa.shipment.returnDate}"/>
                        <s:hidden name="shipment.estmShipmentCharge" value="${cusa.shipment.estmShipmentCharge}" />
                        <s:hidden name="shipment.estmCollectionCharge" value="${cusa.shipment.estmCollectionCharge}" />
                        <s:hidden name="shipment.extraCharge" value="${cusa.shipment.extraCharge}" />
                        <s:hidden name="shipment.zone" value="${cusa.shipment.zone.id}" />
                        <td>
                                ${cusa.shipment.awb.awbNumber}
                            <input type="hidden" name="shipment.awb" value="${cusa.shipment.awb.id}"/>
                        </td>
                        <td>
                            ${cusa.shipment.awb.courier.name}
                        </td>
                        <td>
                            <s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder">${cusa.shipment.shippingOrder.id}
                                <s:param name="shippingOrderId" value="${cusa.shipment.shippingOrder.id}"/>
                                <s:param name="shippingOrderGatewayId" value="${cusa.shippingOrder.gatewayOrderId}"/>
                            </s:link>
                            <s:hidden name="shipment.shippingOrder" value="${cusa.shipment.shippingOrder.id}"/>
                        </td>
                        <td>
                            <c:if test="${cusa.shipment.shipmentServiceType!=null}">
                                ${cusa.shipment.shipmentServiceType.name}
                                <s:hidden name="shipment.shipmentServiceType.id" value="<%=EnumShipmentServiceType.getShipmentTypeFromId(cusa.getShipment().getShipmentServiceType().getId()).asShipmentServiceType()%>"/>
                            </c:if>
                        </td>
                        <td>
                            <s:select name="shipment.boxSize" id="boxSize">
                                <s:option value="">--Select--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allBoxSize"
                                                           value="id" label="name"/>
                            </s:select>
                        </td>
                        <td>
                            <s:text name="shipment.boxWeight" id = "boxWeight" value="${cusa.shipment.boxWeight}"/>
                        </td>
                        <td>
                            <s:select name="shipment.packer" >
                                <s:option value="">--Select</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allPacker"
                                                           value="name" label="name"/>
                            </s:select>
                        </td>
                        <td>
                            <s:select name="shipment.picker">
                                <s:option value="">--Select--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allPicker"
                                                           value="name" label="name"/>
                            </s:select>
                        </td>
                    </tr>
                </table>
                <s:hidden name="shippingOrder" value="${cusa.shippingOrder.id}"/>
                <s:submit name="updateShipment" value="SAVE" id="validate"/>
            </c:if>
            </fieldset>
        </s:form>
        <div class="clear"></div>
        <fieldset>
        <c:if test="${cusa.shippingOrder!=null}">
            <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                             shippingOrders="${cusa.shippingOrderList}" hasAction="false" showCourier="true" isSearchShippingOrder = "true"/>
        </c:if>
        </fieldset>
    </s:layout-component>
</s:layout-render>