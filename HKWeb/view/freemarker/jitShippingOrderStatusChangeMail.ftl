Attention!! PO against JIT Shipping Orders Cancelled.

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>

<p style="margin-bottom:1.2em">Attention!!</p>
<br>

<p style="margin-bottom:1em">${soCancellationReason}</p>
<br>
<br>
<table border="1">
	<tr>
		<th>Shipping Order Id</th>
		<th>Warehouse</th>
		<th>Line Item Id | Product Variant Id | Quantity</th>
		<th>PO Ids | PO status</th>
		<th></th>
	</tr>

	<tr>
		<td>${shippingOrder.id }</td>
		<td>${shippingOrder.warehouse.identifier}</td>
		<td><#list shippingOrder.lineItems as soLi>
			<div>
				<label> ${soLi.id } | ${soLi.sku.productVariant.id } | ${soLi.qty } </label>
			</div> <br> </#list>
		</td>
		<td><#list shippingOrder.purchaseOrders as soPo>
			<div>
				<label>${soPo.id } | ${soPo.purchaseOrderStatus.name }</label>
			</div> <br> </#list>
		</td>

	</tr>
</table>
</body>
</html>
