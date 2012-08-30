[VERIFY ADVERTISEMENT] Product [${product.id}] is now ${stockStatus}
<html>
<head>
	<title>[ATTENTION] Product [${product.id}] is not ${stockStatus}</title>
</head>
<body>
<#include "header.ftl">
	<p>Hi,</p>

	<h2>Attention!!!</h2>

	<p>
		Product ${product.id} is now ${stockStatus}.
	</p>

	<div>
		<h3>Product Details</h3>
		<table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
			<tr>
				<td>Product</td>
				<td>${product.name}</td>
			</tr>
			<tr>
				<td>Product Id</td>
				<td>${product.id}</td>
			</tr>
		</table>
		product page - <a href="www.healthkart.com/product/${product.slug}/${product.id}">www.healthkart.com/product/${product.slug}/${product.id}</a>
	</div>

	<p>
		- HealthKart Admin
	</p>

	<#include "footer.ftl">
</body>
</html>