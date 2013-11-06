<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Track Shipment">
	<s:layout-component name="heading">Order Status</s:layout-component>
	<s:layout-component name="htmlHead">

		<style type="text/css">
			.lhsContent {
				float: left;
				width: 100%;
				background: none;
				font-size:1.1em;
			}
		</style>
	</s:layout-component>
	<s:layout-component name="lhsContent">
		<div>
			<p style="font-weight: 600; font-size: 1.2em; padding: 5px 0;">Congratulations! Your order is shipped and will reach you soon.</p>
			<%--<p>At Healthkart.com we provide free shipping on almost all our products within India</p>--%>
			<ul type="disc" style="margin-left:20px; list-style:disc;margin-top: 10px;line-height: 22px;">
				<li>Your order status would be updated within 24 hours.</li>
				<li>We would like to remind you that an order can take upto 3 to 4 days to reach the preferred
				    location.
				</li>
				<li>For any other query read our <a href="/pages/termsAndConditions.jsp">Terms &amp; Conditions</a></li>
				<li>For any other assistance pertaining to your order contact our customer care at 0124-4616444 or at
				    email: <a href="mailto:info@healthkart.com">info@healthkart.com</a></li>
			</ul>
		</div>
	</s:layout-component>

</s:layout-render>
