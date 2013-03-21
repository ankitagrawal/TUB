<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" var="icBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Subtract Inventory By PV">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function () {
                $('.submit').click(function () {
                    var qty = $('#qty').val();
                    if (qty == null || qty.trim() == '' || isNaN(qty)) {
                        alert('Plz enter qty in numbers');
                        return false;
                    }

                });

            });
        </script>
        <style type="text/css">
            .info {
                color: green;
                font-weight: bold;
                font-size: 15px;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
            <s:hidden name="warehouse" value="${icBean.warehouse.id}"/>
            <ul>
                <li>
                    <div style="text-align: center"><span class="info">Subtract Inventory For Product Variant By Any Batch</span>
                    </div>
                    <br/><br/><br/>
                </li>
                <li><span style="font-size:22px; ">Warehouse :${icBean.warehouse.city} </span></li>

                <li>
                    <label>Scan Product Variant Id</label>
                    <s:text name="productVariant"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label>Qty</label>
                    <s:text id="qty" name="qty"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <s:submit class="submit" name="subtractInventoryForPVFromAnyBatch" value="subtract"/>
                </li>


            </ul>
        </s:form>
    </s:layout-component>
</s:layout-render>