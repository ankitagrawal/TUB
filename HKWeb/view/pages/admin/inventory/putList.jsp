<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<link href="<hk:vhostCss/>/css/new.css?v=1.1" rel="stylesheet" type="text/css"/>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.PutListAction" var="ica"/>
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


<h2>Put List for GRN ${ica.grn.id}</h2>
<div class="alert messages">
    <s:messages key="generalMessages"/></div>
<div class="clear"></div>

<div>
    <h3>Put List Details</h3>
    <table style="font-size: .8em; width:700px; padding:0">
        <tr>
            <th width="100px">Product Variant</th>
            <th width="250px">Item</th>
            <th width="150px">Batch</th>
            <th width="150px">Barcode</th>
            <th width="150px">Qty</th>

        </tr>
        <c:forEach items="${ica.skuSkuGroupMap}" var="skuSkuGroup" varStatus="sno">
            <tr>
                <c:set var="sku" value="${skuSkuGroup.key}"/>
                <c:set var="productVariant" value="${sku.productVariant}"/>
                <c:set var="listOfSkuGroup" value="${skuSkuGroup.value}"/>
                <td>${productVariant.id}</td>
                <td>${productVariant.product.name} <br/> ${productVariant.optionsPipeSeparated}</td>
                <td colspan="3">
                    <table>
                        <c:forEach items="${listOfSkuGroup}" var="skuGroup">
                            <tr>
                                <td width="150px">${skuGroup.batchNumber}</td>
                                <td width="150px">
                                    <c:choose>
                                        <c:when test="${skuGroup.barcode != null}">
                                            ${skuGroup.barcode}
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${skuGroup.skuItems}" var="skuItem" begin="0" end="0">
                                                <c:set var="siBarcode" value="${skuItem.barcode}"/>
                                                <c:set var="siBarcodeArr" value="${fn:split(siBarcode, '-')}"/>
                                                ${siBarcodeArr[0]}-${siBarcodeArr[1]}
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td width="150px">${fn:length(skuGroup.skuItems)}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
