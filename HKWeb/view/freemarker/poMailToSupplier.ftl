Purchase Order Approval Mail
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">
<#if purchaseOrder.supplier.contactPerson??>
  	Dear ${purchaseOrder.supplier.contactPerson}, 
  	<#else>
  	Dear ${purchaseOrder.supplier.name}, 
  	</#if>
<br/>
<br/>
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">Please find the PO attached below. Kindly send across the goods at the earliest.</p>
<br/>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px; font-weight: bold;">Please ensure the following while sending the goods -</p>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">1. Any Cost Price or MRP changes should be highlighted in advance for acceptance of goods at the warehouse by sending the updated catalog.</p>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">2. No excess/damaged/without MRP goods will be accepted against the purchase order raised. The courier charges in case of return of any goods will need to be borne by you.</p>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">3. Please ensure that all details like TIN No, Address, Product Names, Company Name are correct in the invoice sent. Goods will not be accepted at the warehouse if any of the invoice details are incorrect.</p>
    
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">4. PO number and any special schemes should be mentioned on all invoices.</p>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">5. Physical products should be packaged well and unique codes, product name and MRP should be clearly mentioned as specified in the catalog.</p>
    
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">6. Goods with expiry date in the next 6 months or already expired will not be accepted. Goods about to expire will need to be replaced or returned on request.</p>

<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">7. Please share any unique codes for the products that you may be using in your system, so we can include the same in the PO next time for easy identification of the products while you are sending the goods and while we receive them at our warehouse.</p>

<#if purchaseOrder.supplier.state=="HARYANA">
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">8. Kindly ship the goods to our warehouse address as follows - <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;font-weight: bold;">Bright Lifecare Private Limited, Gurgaon Warehouse:</p>Khasra No. 146/25/2/1, Village Badshahpur, Distt Gurgaon, Haryana-122101; TIN Haryana - 06101832036</p>
</#if>
<#if purchaseOrder.supplier.state=="MAHARASHTRA">
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">8. Kindly ship the goods to our warehouse address as follows - <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;font-weight: bold;">Bright Lifecare Private Limited, Mumbai Warehouse:</p>Safexpress Private Limited,Mumbai Nashik Highway N.H-3, Walsind, Lonad, District- Thane- 421302, Maharashtra</p>
</#if>

<br/>
<br/>
<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;font-weight: bold;">Kindly confirm the receipt of PO via email. </p>

<#include "footer.ftl">
</body>
</html>