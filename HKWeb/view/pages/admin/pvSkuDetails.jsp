<%@ page import="com.hk.admin.pact.service.inventory.MasterInventoryService" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page import="com.hk.pact.service.catalog.ProductVariantService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%
    ProductVariantService productVariantService = ServiceLocatorFactory.getService(ProductVariantService.class);
    if (request.getParameter("variantId") != null) {
        ProductVariant productVariant = productVariantService.getVariantById((String) request.getParameter("variantId"));
        if (productVariant != null) {
            pageContext.setAttribute("productVariant", productVariant);
            MasterInventoryService masterInventoryService = ServiceLocatorFactory.getService(MasterInventoryService.class);
            Double mrp = null;                      
            if (request.getParameter("mrp") != null) {
                mrp = Double.valueOf((String) request.getParameter("mrp"));
            }
            List<MasterInventoryService.SKUDetails> skuDetailsList = masterInventoryService.getSkuDetails(productVariant, mrp);
            pageContext.setAttribute("skuDetailsList", skuDetailsList);
            pageContext.setAttribute("unbookedCLIUnits", masterInventoryService.getUnbookedCLIUnits(productVariant, mrp));
            pageContext.setAttribute("availableUnits", masterInventoryService.getAvailableUnits(productVariant, mrp));
            pageContext.setAttribute("underReviewUnits", masterInventoryService.getUnderReviewUnits(productVariant, mrp));
        }
    }

%>
<style type="text/css">
    table {
        border-collapse: collapse;
        width: 100%
    }

    table tr td {
        padding: 5px;
        border: 1px solid #CCC;
    }

    table tr th {
        padding: 5px;
        border: 1px solid #CCC;
        text-align: left;
    }

    h2t {
        margin: 0;
        padding: 0;
    }

    h1 {
        margin: 0;
        padding: 0;
    }

    table.header tr td {
        border: none;
        vertical-align: top
    }

    .clear {
        clear: both;
        display: block;
        overflow: hidden;
        visibility: hidden;
        width: 0;
        height: 0
    }

    .messages {
        color: red;
        font: 10px;

    }

</style>
<h2>
  SKU Details of Variant = ${productVariant.id} | ${productVariant.product.name} - ${productVariant.optionsCommaSeparated}
</h2>

    <div>
        <div style="font-weight:bold;">
            Variant = ${productVariant.id} | Unbooked CLI Qty* = ${unbookedCLIUnits} | Available Qty = ${availableUnits} | Under Review Units = ${underReviewUnits}
        </div>
        <table style="font-size: .8em; width:800px; padding:0">
            <tr>
                <th>SKU ID</th>
                <th>WH</th>
                <th>Phy Qty</th>
                <th>CI Qty</th>
                <th>CI Qty <br/> (Under Review)</th>
                <th>Booked Qty</th>
                <th>Booked Qty <br/> (Under Review)</th>
                <th>Unbooked LI Qty *</th>
                <th>Available Qty <br/> (CI - UBLI)</th>
            </tr>
            <c:forEach items="${skuDetailsList}" var="skuDetails">
                <tr>
                    <td>${skuDetails.sku.id}</td>
                    <td>${skuDetails.sku.warehouse.identifier}</td>
                    <td>${skuDetails.phyQty}</td>
                    <td>${skuDetails.ciQty}</td>
                    <td>${skuDetails.ciQtyUnderReview}</td>
                    <td>${skuDetails.bookedQty}</td>
                    <td>${skuDetails.bookedQtyUnderReview}</td>
                    <td>${skuDetails.unbookedLIQty}</td>
                    <td>${skuDetails.ciQty - skuDetails.unbookedLIQty}</td>
                </tr>
            </c:forEach>
            <tfoot>
            <tr>
                <td colspan="9">* : Not Mrp Dependent</td>
            </tr>
            </tfoot>
        </table>
    </div>

