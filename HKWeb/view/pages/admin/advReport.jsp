<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.AdvReportAction" var="reportActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Adv Reports">

	<s:layout-component name="htmlHead">
		<style type="text/css">
			table tr th, table tr td {
				border: 1px solid #CCC;
			}
		</style>
	</s:layout-component>
	<s:layout-component name="heading">
		Traffic Source Perfomance
	</s:layout-component>
	<s:layout-component name="content">
		<table border="1" cellpadding="5" cellspacing="5">
			<tr><th>Traffic Source</th><th>Paid?</th><th>Traffic</th><th>Orders</th><th>%Cnv.</th><th>New Orders</th><th>%New Orders</th></tr>
			<c:set var="totalTraffic" value="0"/>
			<c:set var="totalOrders" value="0"/>
			<c:set var="totalNewOrders" value="0"/>
			<c:forEach items="${reportActionBean.srcPerformanceDtoList}" var="traffic">
				<c:set var="totalTraffic" value="${totalTraffic + traffic.trafficCount}"/>
				<c:set var="totalOrders" value="${totalOrders + traffic.orderCount}"/>
				<c:set var="totalNewOrders" value="${totalNewOrders + traffic.firstOrderCount}"/>
				<tr><td>${traffic.trafficSrc}</td><td>${traffic.trafficSrcPaid}</td><td>${traffic.trafficCount}</td><td>${traffic.orderCount}</td>
					<td><fmt:formatNumber value="${traffic.orderCount/traffic.trafficCount * 100}"
					                      pattern="##.##"/> </td>
					<td>${traffic.firstOrderCount}</td>
					<td><fmt:formatNumber value="${traffic.firstOrderCount/traffic.orderCount * 100}"
					                      pattern="##.##"/> </td>
				</tr>
			</c:forEach>
			<tr><th colspan="2">Total</th><th>${totalTraffic}</th><th>${totalOrders}</th>
				<th><fmt:formatNumber value="${totalOrders/totalTraffic * 100}" pattern="##.##"/> </th>
				<th>${totalNewOrders}</th>
				<th><fmt:formatNumber value="${totalNewOrders/totalOrders * 100}" pattern="##.##"/> </th>
			</tr>
		</table>
		<p><strong>NOTE:</strong>The report is exclusive to healthkart.com traffic/orders and doesn't include traffic/orders from admin, mih, mobile etc.</p>
	</s:layout-component>
</s:layout-render>
