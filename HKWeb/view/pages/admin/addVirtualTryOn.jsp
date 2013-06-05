<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Upload Variant Virtual Try on Filter
     </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.catalog.product.AddVirtualTryOnAction" focus="" onsubmit="return validateForm()" method="post" name="AddTryOnForm">
                    <table>
                       <tr>
                           <td>Enter ProductVariant list separated by commas</td>
                           <td><s:textarea name="productVariantList" style="height:40px;"/></td>
                           </tr>
                        <tr>
                           <td colspan="2">
                               <s:submit class="requiredFieldValidator" name="save" value="Add Virtal Try On Filter"/>
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
        if (variantlist == null || variantlist == "")
        {
            alert("Product Variant list must be filled out");
            return false;
        }
    }
</script>