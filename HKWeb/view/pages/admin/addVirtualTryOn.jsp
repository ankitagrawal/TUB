<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.admin.pact.service.inventory.AdminInventoryService" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Upload Variant Virtual Try on Filter
     </s:layout-component>

    <s:layout-component name="content">
           <%
               AdminInventoryService adminInventoryService = ServiceLocatorFactory.getService(AdminInventoryService.class);
               pageContext.setAttribute("TryOnList", adminInventoryService.getAllVariantOption());
           %>

        <s:form beanclass="com.hk.web.action.admin.catalog.product.AddVirtualTryOnAction" focus="" onsubmit="return validateForm()" method="post" name="AddTryOnForm">
                    <table>
                       <tr>
                           <td>Enter ProductVariant list seperated by commas</td>
                           <td><s:textarea name="productVariantList" style="height:40px;"/></td>
                       </tr>
                       <tr>
                           <td>Virtual Try On Filter</td>
                           <td>
                               <s:select name="optionId">
                                   <s:option value="">-select- </s:option>
                                   <c:forEach items="${TryOnList}" var="TryOn">
                                       <s:option value="${TryOn.id}">${TryOn.name}</s:option>
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
        var variantlist = document.forms["AddTryOnForm"]["productVariantList"].value;
        var variantoption = document.forms["AddTryOnForm"]["optionId"].value;
        if (variantlist == null || variantlist == "")
        {
            alert("Product Variant list must be filled out");
            return false;
        }
        if (variantoption == "")
        {
            alert("Variant Option must be selected");
            return false;
        }

    }
</script>