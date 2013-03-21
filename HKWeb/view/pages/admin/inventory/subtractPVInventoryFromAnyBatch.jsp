<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" var="icBean"/>
<s:layout-component name="htmlHead">
    <script type="text/javascript">
        $(document).ready(function () {

            $('.submit').click(function () {
                var qty = $('#qty').val();
                if (qty == null || qty.trim() == '' || isNaN(qty))
                {
                    alert('Plz enter qty in numbers');
                    return false;
                }

            });

        });
    </script>
</s:layout-component>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkout">

    <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
        <s:hidden name="warehouse" value="${icBean.warehouse.id}"/>
        <ul>
            <li><label>Warehouse :</label> ${icBean.warehouse.city}</li>

            <li>
                <label>Scan Product Variant Id</label>
                <s:text name="productVariant"/>
                <label>Qty</label>
                <s:text id="qty" name="qty"/>
                <s:submit class="submit" name="deleteInventoryForProductVariant" value="deleteInventory"/>
            </li>


        </ul>
    </s:form>

    <s:layout-component name="content">

    </s:layout-component>

</s:layout-render>