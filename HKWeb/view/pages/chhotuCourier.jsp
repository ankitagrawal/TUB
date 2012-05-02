<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.TrackCourierAction" var="tca"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Chhotu Courier Services">
	<s:layout-component name="heading">Order Shipped through Chhotu Courier Services</s:layout-component>
	<s:layout-component name="rhsContent">
		<c:choose>
			<c:when test="${tca.chhotuCourierDelivery.myStatus == 'ERROR'}">
				<br/><br/>
				Sorry, there is no return of Information from Chhotu Courier Services please visit
				<br/>
				<a href="http://www.chhotu.in/" target="_blank">Visit Chhotu Courier</a>
				 and use shipment number=${tca.trackingId}
			</c:when>
			<c:otherwise>
				<table>
					<p>
					<h2>Delivery Details</h2>
					</p>
					<tr>
						<td>Delivery Type :</td>
						<td>${tca.chhotuCourierDelivery.deliveryType}</td>
					</tr>
					<tr>
						<td>Customer Name :</td>
						<td>${tca.chhotuCourierDelivery.customerName}</td>
					</tr>
					<tr>
						<td>COD Amount :</td>
						<td>${tca.chhotuCourierDelivery.codAmount}</td>
					</tr>
					<tr>
						<td>Pincode :</td>
						<td>${tca.chhotuCourierDelivery.pinCode}</td>
					</tr>
					<tr>
						<td>Tracking Id :</td>
						<td>${tca.chhotuCourierDelivery.trackingId}</td>
					</tr>
					<tr>
						<td>Shipment Status :</td>
						<td>${tca.chhotuCourierDelivery.shipmentStatus}</td>
					</tr>
					<tr>
						<td>My Status :</td>
						<td>${tca.chhotuCourierDelivery.myStatus}</td>
					</tr>
					<tr>
						<td>Delivery Date :
							<br/>(yyyy-MM-dd)
						</td>
						<td>
							<c:choose>
							<c:when test="${tca.chhotuCourierDelivery.deliveryDate!=null}">
								${tca.chhotuCourierDelivery.deliveryDate}
							</c:when>
							<c:otherwise>
							Not Delivered
							</c:otherwise>
							</c:choose>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
	</s:layout-component>

</s:layout-render>
