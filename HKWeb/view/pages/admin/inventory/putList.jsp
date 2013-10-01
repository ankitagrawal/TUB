<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.hk.admin.util.BarcodeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="barcodePrefix" value="<%=BarcodeUtil.BARCODE_SKU_ITEM_PREFIX%>"/>
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


<table class="header">
    <tr>
        <td>
            <p>Put List for GRN
                <c:out value="${ica.grn.id}"/>
            </p>
        </td>
    </tr>

</table>
<div class="alert messages">
    <s:messages key="generalMessages"/></div>
<div class="clear"></div>

<div>
    <h3>Put List Details</h3>
        <table style="font-size: .8em; width:700px; padding:0">
            <tr>
                <th width="280px">Product Variant</th>
                <th width="280px">Item</th>
                <th width="140px">Batch</th>
                <th width="70px">Barcode</th>
                <th width="70px">Checked In Qty</th>

            </tr>
            <c:forEach items="${ica.skuSkuGroupMap}" var="skuSkuGroup" varStatus="sno">
                <tr>
                <c:set var="sku" value="${skuSkuGroup.key}"/>
                <c:set var="productVariant" value="${sku.productVariant}"/>
                <c:set var="listOfSkuGroup" value="${skuSkuGroup.value}"/>
                    <td>${productVariant.id}</td>
                    <td>${productVariant.optionsPipeSeparated}</td>
                    <td colspan="3">
                    <table>
                        <c:forEach items="${listOfSkuGroup}" var="skuGroup">
                          <tr>
                              <td>${skuGroup.batchNumber}</td>
                              <td>${skuGroup.barcode}</td>
                              <td>${fn:length(skuGroup.skuItems)}</td>
                          </tr>
                        </c:forEach>
                     </table>
                    </td>
                    </tr>
            </c:forEach>
        </table>
</div>
