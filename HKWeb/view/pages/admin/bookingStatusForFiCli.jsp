<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="com.hk.constants.order.EnumCartLineItemType"%>
<%@ page import="com.hk.constants.payment.EnumPaymentMode"%>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus"%>
<%@ page import="com.hk.constants.order.EnumOrderStatus"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@page import="com.hk.constants.core.PermissionConstants"%>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.admin.booking.AdminBookingAction" var="adminBookingBean" />

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Booking Status">
    <c:set var="cartIds" value="" />
    <s:layout-component name="htmlHead">

        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    </s:layout-component>

    <s:layout-component name="content">

        <div align="center"><label><font size="6px">Booking Status Tables From FiCLI</font></label></div><br><br><br>
        <c:choose>
            <c:when test="${(adminBookingBean.foreignSkuItemCLIList != null) && (fn:length(adminBookingBean.foreignSkuItemCLIList)>0)}">

                <table class="t1" id="skuItemLineItemTable" border="1" align="center" >
                    <tr>
                        <th>FSICLI ID</th>
                        <th>CLI ID</th>
                        <th>PV ID</th>
                        <th>Foreign Sku Group ID</th>
                        <th>Foreign Sku Item ID</th>
                        <th>Foreign Barcode</th>
                        <th>Foreign BO</th>
                        <th>Foreign SO</th>
                        <th>Processed Status</th>
                    </tr>
                    <c:forEach var="skuItemLineItem" items="${adminBookingBean.foreignSkuItemCLIList}" varStatus="ctr">
                        <tr>
                            <td>${skuItemLineItem.id }</td>
                            <td>${skuItemLineItem.cartLineItem.id }</td>
                            <td>${skuItemLineItem.productVariant.id }</td>
                            <td>${skuItemLineItem.foreignSkuGroupId }</td>
                            <td>${skuItemLineItem.skuItemId }</td>
                            <td>${skuItemLineItem.foreignBarcode }</td>
                            <td>${skuItemLineItem.foreignOrderId}</td>
                            <td>${skuItemLineItem.foreignShippingOrderId}</td>
                            <td>${skuItemLineItem.processedStatus}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <br>
                <div align="center"><label>No Foreign Data Avalable</label></div><br><br><br>
            </c:otherwise>
        </c:choose>

    </s:layout-component>
</s:layout-render>
<style>
    .t1 {
        border-width: 0 0 1px 1px;
        border-style: solid;
        border-color: rgba(0, 0, 0, 0.1);
    }

    .t1 tr td{
        text-align: left;
        font-size: small;
        border-width: 1px 1px 0 0;
        border-style: solid;
        border-color: rgba(0, 0, 0, 0.1);
    }
    #bookingIdButton{
        float: left;
        position: relative;
        left: 43%;
        margin-bottom: 2px;
        margin-top: 2px;
    }
</style>