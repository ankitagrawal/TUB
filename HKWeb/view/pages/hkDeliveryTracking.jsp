<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.hkDelivery.HKDeliveryConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.order.TrackCourierAction" var="hkdBean"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Healthkart Consignment Tracking">

   <s:layout-component name="heading">Order Shipped through HKDelivery Courier Services</s:layout-component>
    <s:layout-component name="rhsContent">
	    <c:set var="customer" value="<%=HKDeliveryConstants.DELIVERY_HUB%>" />
	    <c:set var="warehouse" value="<%=HKDeliveryConstants.HEALTHKART_HUB%>" />
        <c:set var="userStatusCustomerHold" value="<%=HKDeliveryConstants.USER_STATUS_CUSTOMERHOLD%>" />

        <div class="hkDeliveryWorksheetBox">
                <fieldset class="right_label">
                   <h2 class='prod_title' itemprop="name">Order Details:</h2>
                    <ul class="orderdetailBlock">
	                    <li>
		                    <label class="nameLabel">Order Id : </label>
		                    <label class="valueLabel">${hkdBean.consignment.cnnNumber}</label>
	                    </li>
	                    <li>
		                    <label class="nameLabel">Order Amount : </label>
		                    <label class="valueLabel">${hkdBean.consignment.amount} </label>
	                    </li>
	                    <li>
		                    <label class="nameLabel">Payment type : </label>
		                    <label class="valueLabel">${hkdBean.consignment.paymentMode}</label>
	                    </li>
	                    <li>
		                    <label class="nameLabel">Shipping Address : </label>
		                    <label class="valueLabel">${hkdBean.consignment.address}</label>
	                    </li>

                    </ul>
                </fieldset>
        </div>
	    <br /><br />
        <c:if test="${!empty hkdBean.consignmentTrackingList}">
        <div id="consignmentTrackingData">
          <table class="zebra_vert">
            <thead>
            <tr class="row">
	            <td class="column">Date</td>
                <td class="column">Came From</td>
                <td class="column">To Destination</td>
                <td class="column">Status</td>
            </tr>
            </thead>
            <c:forEach items="${hkdBean.consignmentTrackingList}" var="consignmentTrackingList" varStatus="ctr">
                <tr class="row">
	                <td class="column"><fmt:formatDate value="${consignmentTrackingList.createDate}" type="both" timeStyle="short"/></td>
	                <td class="column">
						 <c:choose>
							<c:when test="${consignmentTrackingList.sourceHub.name == customer || consignmentTrackingList.sourceHub.name == warehouse}">
								${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.sourceHub.name)}
							</c:when>
							<c:otherwise>
								${consignmentTrackingList.sourceHub.name}
							</c:otherwise>
						</c:choose>
	                </td>
	                <td class="column">
						 <c:choose>
							<c:when test="${consignmentTrackingList.destinationHub.name == customer || consignmentTrackingList.destinationHub.name == warehouse}">
								${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.destinationHub.name)}
							</c:when>
							<c:otherwise>
								${consignmentTrackingList.destinationHub.name}
							</c:otherwise>
						</c:choose>
	                </td>
                    <td class="column">
                        <c:choose>
                            <c:when test="${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.consignmentLifecycleStatus.status) == userStatusCustomerHold }">
                                ${hk:getDisplayNameForHkdeliveryTrackingRemarks(consignmentTrackingList.remarks)}
                            </c:when>
                            <c:otherwise>
                                ${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.consignmentLifecycleStatus.status)}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
        </div>
        </c:if>
	    <br /><br />
    </s:layout-component>
</s:layout-render>
<style type="text/css">
    .nameLabel {
        margin-top: 20px;
	    font-weight: bold;
	    font-size:15px;
    }
    .orderdetailBlock{
        width:450px;
        float:left;
    }

    .valueLabel {
        margin-left: 10px;
        width: 300px;
        float:right;
        word-wrap:break-word ;
    }

    .row {
        padding-bottom: 40px;
    }
	.column{
		text-align:center;
		padding-left:6px;
		padding-right:6px;
	}
    table.zebra_vert td{ padding-bottom: 10px; padding-top: 10px; }

</style>



