<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction" var="icBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkout">

    <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
        <s:hidden name="warehouse" value="${icBean.warehouse.id}"/>
        <ul>
            <li><label>Warehouse :</label> ${icBean.warehouse.city}</li>

            <li>
                <label>Scan Product Variant Id</label>
                <s:text name="productVariant"/>
                <label>Qty</label>
                <s:text name="qty"/>
                <s:submit name="deleteInventoryForProductVariant" value="deleteInventory"/>
            </li>


        </ul>
    </s:form>

    <s:layout-component name="content">

    </s:layout-component>

</s:layout-render>