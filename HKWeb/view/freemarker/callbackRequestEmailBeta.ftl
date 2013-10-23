Callback Request(Non-Paid) - ${dcml.category}
<html>
<head>
    <title>Callback Request(Non-Paid) - ${dcml.category}</title>
</head>
<body>

<#include "header.ftl">

<p><strong>Category: </strong>${dcml.category}</p>

<p><strong>UserId: </strong>${userId}</p>

<p><strong>Name: </strong>${dcml.name}</p>

<#if dcml.email??>
<p><strong>Email: </strong>${dcml.email}</p>
</#if>
<#if dcml.mobile??>
    <p><strong>Mobile: </strong>${dcml.mobile}</p>
</#if>



<p>- HealthKart Admin</p>

<#include "footer.ftl">
</body>
</html>