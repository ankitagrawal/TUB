<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.courier.BoxSize" %>
<%@ page import="com.hk.constants.shipment.EnumPicker" %>
<%@ page import="com.hk.constants.shipment.EnumPacker" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.shipment.CreateDropShipmentAction" var="shipmentQueueBean"/>
<%
    BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
    MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
    pageContext.setAttribute("boxSizeList", baseDao.getAll(BoxSize.class));
    pageContext.setAttribute("courierList", masterDataDao.getAvailableCouriers());
    pageContext.setAttribute("vendorCourierList", masterDataDao.getListOfVendorCouriers());
%>

<c:set var="commentTypeDelivery" value="<%= MasterDataDao.USER_COMMENT_TYPE_DELIVERY_BASE_ORDER %>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
        <script type="text/javascript">
            $(document).ready(function() {

                var commentType = $('#commentType').val();
                if (commentType == ${commentTypeDelivery}) {
                    alert("User Instruction : " + $('#userComments').val());
                }

                $('.weight').keyup(function() {
                    var weight = $('.weight').val();
                    if (weight > 5) {
                        confirm("Box Weight entered is " + weight + " Kgs. Do you wanna continue with the same?");
                    }
                });

                $("#shipmentbutton").click(function(event) {
                    var tracking = $('.tracking').val();
                    if (tracking == "" || tracking == null) {
                        $('.error').html("");
                        $('.error').append("Enter Tracking Id");
                        $('.error').show();
                        return false;
                    }
                    if (tracking.length > 20) {
                        $('.error').html("");
                        $('.error').append(" Tracking Id length can not be greater than 20");
                        $('.error').show();
                        return false;
                    }
                    var selected = $('#courier').selected().val();
                    var suggested = $('#sugcouier').val();

                    var weight = $('.weight').val();
                    if (!(weight.match(/^([0-9\.]+)$/g))) {
                        $('.error').html("");
                        $('.error').append(" Box weight can be number only");
                        $('.error').show();
                        return false;
                    }

                    var  selectedCourier = $('#courier').val();
                    var  vendorCourier =  $('#vendorCourier').val();

//                    alert(selectedCourier.length + " " + vendorCourier.length) ;
                     if ( vendorCourier !=""  && selectedCourier!= "") {
                      alert("HK Courier and Vendor Courier cannot be selected at same time");
                      return false;
                  }
                    else if(vendorCourier == "" && selectedCourier == ""){
                         alert("Please select atleast one courier");
                         return false;
                     }
                });

                var grncheck = false;
                $('.shipped').click(function() {
                    var con = confirm("Verify that you are saving correct information ");
                    if (con == true) {
                        grncheck = true;
                    } else {
                        return false;
                    }
                    if (grncheck) {
                        var bool = confirm("Verify that you have already created GRN ");
                        if (bool == true) {
                            return true
                        } else {
                            return false;
                        }
                    }

                });



            });
        </script>

    </s:layout-component>
    <s:layout-component name="heading">Enter Tracking Details for Drop Shipped Orders</s:layout-component>
    <s:layout-component name="content">
        <div style="display:none">
                <%--<input type="hidden" id="commentType" value="${shipmentQueueBean.shippingOrder.baseOrder.commentType}">--%>
                <%--<input type="hidden" id="userComments" value="${shipmentQueueBean.shippingOrder.baseOrder.userComments}">--%>
                <%--<div  class="error" style= "background-color:salmon; width:380px; display:none;">--%>

        </div>
        <div height="500px" align="center">
        </div>
        <script language=javascript type=text/javascript>
            $('#gatewayOrderId').focus();
        </script>
        <c:set var="shippingOrderStatusSoShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
        <fieldset class="top_label">

            <s:form beanclass="com.hk.web.action.admin.shipment.CreateDropShipmentAction"
                    onsubmit="return validateForm()" method="post" name="ShipmentForm">
                <s:hidden name="shippingOrder" value="${shipmentQueueBean.shippingOrder}"/>
                <s:hidden name="suggestedCourier" id="sugcouier" value="${shipmentQueueBean.suggestedCourier}"/>
                <label>Box Size:</label>
                <s:select name="boxSize" value="${shipmentQueueBean.shippingOrder.shipment.boxSize}">
                    <c:forEach var="box" items="${boxSizeList}">
                        <s:option value="${box.id}">${box.name}</s:option>
                    </c:forEach>
                </s:select>
                <label>Box Weight(Kgs):</label><s:text name="boxWeight" size="5" class="weight"
                                                       value="${shipmentQueueBean.shippingOrder.shipment.boxWeight}"/>
                <label>Tracking ID:</label><s:text class="tracking" name="trackingId"
                                                   value="${shipmentQueueBean.shippingOrder.shipment.awb.awbNumber}"/>
                <label>HK Courier</label>
                <s:select name="selectedCourier" value="${shipmentQueueBean.shippingOrder.shipment.awb.courier.id}"
                          id="courier">
                    <s:option value="">-select-</s:option>
                    <c:forEach var="courier" items="${courierList}">
                        <s:option value="${courier.id}">${courier.name}</s:option>
                    </c:forEach>
                </s:select>


                 <label>Vendor Courier</label>
                  <s:select name="vendorCourier" value="${shipmentQueueBean.shippingOrder.shipment.awb.courier.id}"
                          id="vendorCourier">
                    <s:option value="">-select-</s:option>
                    <c:forEach var="vendorCourier" items="${vendorCourierList}">
                        <s:option value="${vendorCourier.id}">${vendorCourier.name}</s:option>
                    </c:forEach>
                </s:select>


                <%--<s:link class="com.hk.web.action.admin.shipment.CreateDropShipmentAction" event="getAwbForHkCourier"> Get HK Couriers </s:link>--%>
                <c:if test="${!(shippingOrderStatusSoShipped ==shipmentQueueBean.shippingOrder.orderStatus.id)}">
                    <div class="buttons" style="margin-left: 90%;"><s:submit id="shipmentbutton"
                                                                             name="saveDropShipmentDetails"
                                                                             value="Save"/></div>
                </c:if>

                <%--<s:submit name="markShippingOrdersAsShipped"    value="Mark Order as shipped"/>--%>
            </s:form>

        </fieldset>

        <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                         shippingOrders="${shipmentQueueBean.shippingOrderList}"/>

        <br> <br> <br>

        <div style="display:inline;float:right;">
            <c:if test="${!(shippingOrderStatusSoShipped ==shipmentQueueBean.shippingOrder.orderStatus.id)}">
                <s:link beanclass="com.hk.web.action.admin.shipment.CreateDropShipmentAction"
                        event="markShippingOrdersAsShipped"
                        class="button_orange shipped">Mark Order as shipped
                    <s:param name="shippingOrder" value="${shipmentQueueBean.shippingOrder}"/> </s:link>
            </c:if>
        </div>

    </s:layout-component>
</s:layout-render>
