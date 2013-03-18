ExtraInventory Id ${extraInventory.id} created.
<html>
<head>
    <title>ExtraInventory Id # ${extraInventory.id} created with PO ID # ${extraInventory.purchaseOrder.id}</title>
</head>
<body>
<p style="margin-bottom:1em"></p>

<div>
    <h3>ExtraInventory Details</h3>
    <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
        <tr>
            <td width="150"><strong>Created By</strong></td>
            <td width="50"><strong>Created Date</strong></td>
        </tr>


        <tr>
                <td><em style="font-size:0.9em; color:#666">
                    ${extraInventory.createdBy.name}
                </em>
            </td>
            <td>
                 ${extraInventory.createDate}
            </td>

        </tr>
    </table>
</div>
  <h3 style="color:blue;">Please click on link http://admin.healthkart.com/admin/rtv/ExtraInventory.action?pre=&purchaseOrderId=${extraInventory.purchaseOrder.id}&wareHouseId=1 to go directly to this ExtraInventory</h3>
 <br>
<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
</body>
</html>