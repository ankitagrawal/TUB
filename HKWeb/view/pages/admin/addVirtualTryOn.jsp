<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Upload Variant Virtual Try on Filter
        <s:form beanclass="com.hk.web.action.admin.catalog.product.AddVirtualTryOnAction" focus="" onsubmit="return validateForm()" method="post" name="AddTryOnForm">
                    <table>
                       <tr>
                           <td>Enter ProductVariant list seperated by commas</td>
                           <td><s:textarea name="productVariantList" style="height:40px;"/></td>
                       </tr>
                       <tr>
                           <td>Virtual Try On Filter</td>
                           <td>
                               <s:select name="configId">
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