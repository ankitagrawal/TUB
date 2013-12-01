<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.admin.pact.service.inventory.AdminInventoryService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Run Variant Config
    </s:layout-component>

    <s:layout-component name="content">
        <%
            AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
            pageContext.setAttribute("confList", adminInventoryService.getAllVariantConfig());
        %>

        <s:form beanclass="com.hk.web.action.admin.catalog.product.AddEyeConfigAction" focus="" onsubmit="return validateForm()" method="post" name="AddConfigForm">
            <s:errors/>
            <s:messages/>
            <table>
                <tr>
                    <td>Enter ProductVariant list separated by commas</td>
                    <td><s:textarea name="productVariantList" style="height:40px;"/></td>
                </tr>
                <tr>
                    <td>Variant Config</td>
                    <td>
                        <s:select name="configId">
                            <s:option value="">-select- </s:option>
                            <c:forEach items="${confList}" var="conf">
                                <s:option value="${conf.id}">${conf.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <s:submit class="requiredFieldValidator" name="save" value="Save"/>
                    </td>
                </tr>


            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script>
    function validateForm()
    {
        var variantlist = document.forms["AddConfigForm"]["productVariantList"].value;
        var variantconfig = document.forms["AddConfigForm"]["configId"].value;
        if (variantlist == null || variantlist == "")
        {
            alert("Product Variant list must be filled out");
            return false;
        }
        if (variantconfig == "")
        {
            alert("Variant config must be selected");
            return false;
        }

    }
</script>