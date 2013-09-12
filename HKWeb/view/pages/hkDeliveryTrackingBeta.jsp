<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.hkDelivery.HKDeliveryConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.order.TrackCourierAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Healthkart Consignment Tracking">

   <s:layout-component name="heading">Order Shipped through HKDelivery Courier Services</s:layout-component>
    <s:layout-component name="fullContent">
	    <c:set var="customer" value="<%=HKDeliveryConstants.DELIVERY_HUB%>" />
	    <c:set var="warehouse" value="<%=HKDeliveryConstants.HEALTHKART_HUB%>" />

        <div class="hkDeliveryWorksheetBox">
                <fieldset class="right_label">
                   <h2 class='prod_title' itemprop="name">Order Details:</h2>
                    <table class="orderdetailBlock mrgn-t-20">
	                    <tr>
		                    <td class = "fnt-bold">Order Id<span class="cont-rht">:</span></td>
		                    <td class = "pad-l-10">${hkdBean.consignment.cnnNumber}</td>
	                    </tr>
	                    <tr>
		                    <td class = "fnt-bold">Order Amount<span class="cont-rht">:</span></td>
		                    <td class = "pad-l-10">${hkdBean.consignment.amount} </td>
	                    </tr>
	                    <tr>
		                    <td class = "fnt-bold">Payment type<span class="cont-rht">:</span></td>
		                    <td class = "pad-l-10">${hkdBean.consignment.paymentMode}</td>
	                    </tr>
	                    <tr>
		                    <td class = "fnt-bold">Shipping Address<span class="cont-rht">:</span></td>
		                    <td class = "pad-l-10">${hkdBean.consignment.address}</td>
	                    </tr>
                    </table>
                </fieldset>
        </div>
        <c:if test="${!empty hkdBean.consignmentTrackingList}">
        <div id="consignmentTrackingData">
          <table  class="order-tbl">
              <tr class="order-specs-hdr btm-brdr">
                  <th class="fnt-bold">Date</th>
                  <th class="fnt-bold">Came From</th>
                  <th class="fnt-bold">To Destination</th>
                  <th class="fnt-bold">Status</th>
              </tr>
            <tbody>
                <c:forEach items="${hkdBean.consignmentTrackingList}" var="consignmentTrackingList" varStatus="ctr">
                    <c:if test="${ctr.first}">
                        <tr class="order-tr top-brdr">
                    </c:if>
                    <c:if test="${ctr.last}">
                        <tr class="${ctr.index%2==0? 'order-tr btm-brdr':'order-tr btm-brdr bg-gray'}">
                    </c:if>
                    <c:if test="${!(ctr.first || ctr.last)}">
                        <tr class="${ctr.index%2==0? 'order-tr':'order-tr bg-gray'}">
                    </c:if>
                        <td><fmt:formatDate value="${consignmentTrackingList.createDate}" type="both" timeStyle="short"/></td>
                        <td class="border-td">
                             <c:choose>
                                <c:when test="${consignmentTrackingList.sourceHub.name == customer || consignmentTrackingList.sourceHub.name == warehouse}">
                                    ${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.sourceHub.name)}
                                </c:when>
                                <c:otherwise>
                                    ${consignmentTrackingList.sourceHub.name}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="border-td">
                             <c:choose>
                                <c:when test="${consignmentTrackingList.destinationHub.name == customer || consignmentTrackingList.destinationHub.name == warehouse}">
                                    ${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.destinationHub.name)}
                                </c:when>
                                <c:otherwise>
                                    ${consignmentTrackingList.destinationHub.name}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${hk:getDisplayNameForHkdeliveryTracking(consignmentTrackingList.consignmentLifecycleStatus.status)}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>
        </c:if>
	    <br /><br />
    </s:layout-component>
</s:layout-render>
<style type="text/css">
    /*.nameLabel {*/
        /*margin-top: 20px;*/
	    /*font-weight: bold;*/
	    /*font-size:15px;*/
    /*}*/
    .orderdetailBlock{
        width:450px;
        float:left;
    }

    /*.valueLabel {*/
        /*margin-left: 10px;*/
        /*width: 300px;*/
        /*float:right;*/
        /*word-wrap:break-word ;*/
    /*}*/

    /*.row {*/
        /*padding-bottom: 40px;*/
    /*}*/
	/*.column{*/
		/*text-align:center;*/
		/*padding-left:6px;*/
		/*padding-right:6px;*/
	/*}*/
    /*table.zebra_vert td{ padding-bottom: 10px; padding-top: 10px; }*/

</style>



