<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page import="com.hk.admin.util.BarcodeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="barcodePrefix" value="<%=BarcodeUtil.BARCODE_SKU_ITEM_PREFIX_AQ%>"/>
<link href="<hk:vhostCss/>/css/new.css?v=1.1" rel="stylesheet" type="text/css"/>
<script>
    <s:useActionBean beanclass="com.hk.web.action.admin.queue.JobCartAction" var="ica"/>


</script>
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
            <p>Put Cart for GRN
                <c:out value="${ica.grn.id}"/>
            </p>
        </td>
    </tr>

</table>
<div class="alert messages">
    <s:messages key="generalMessages"/></div>
<div class="clear"></div>

<div>
    <h3>Put Cart Details</h3>
    <s:form beanclass="com.hk.web.action.admin.queue.JobCartAction">
        <table style="font-size: .8em; width:700px; padding:0">
            <tr>
                <th width="280px">Product Variant</th>
                <th width="280px">Item</th>
                <th width="280px">Suggesetd Location/Bin</th>
                <%--<th width="70px">Bin has Items</th>--%>
                <th width="140px">Batch</th>
                <th width="70px">Barcode</th>
                <th width="70px">Check In Quantity Of Grn</th>
                 <th width="70px"> Quantity Already at Shelf </th>

            </tr>
            <c:forEach items="${ica.listOfSkuGroupListPerGrn}" var="VarSkuList" varStatus="sno">
                <c:set var="length" value="${fn:length(VarSkuList)}"/>

                <tr>
                <c:set var="sku" value="${VarSkuList[0].sku}"/>

                <td rowspan="${length}">
                        ${sku.productVariant.id}
                </td>
                <td rowspan="${length}">
                        ${sku.productVariant.product.name} || ${sku.productVariant.optionsPipeSeparated}
                </td>

                <c:forEach items="${VarSkuList}" var="VarSkuGroup">
                    <td width="150px">
                        <s:form beanclass="com.hk.web.action.admin.queue.JobCartAction" id="binform">
                            <c:set var="mapentry" value="${ica.skuBinMapping}"/>
                            <c:if test="${mapentry[VarSkuGroup.sku.id].barcode != null}">
                                ${mapentry[VarSkuGroup.sku.id].barcode}  &nbsp; &nbsp;&nbsp;
                                (<s:link beanclass="com.hk.web.action.admin.queue.JobCartAction" event="saveBins"
                                         target="_blank">
                                <s:param name="productVariant" value="${sku.productVariant.id}"/>
                                <s:param name="skuGroupField" value="${VarSkuGroup.id}"/>
                                <s:param name="grn" value="${ica.grn.id}"/>
                                <s:param name="bin" value="${mapentry[VarSkuGroup.sku.id].id}"/>
                                  Use Suggested Bin
                            </s:link>)


                            </c:if>
                            <br/> <br/>
                            (<s:link beanclass="com.hk.web.action.admin.queue.JobCartAction" event="addBinForNewProdcutVariant"
                                     target="_blank">
                            <s:param name="productVariant" value="${sku.productVariant.id}"/>
                            <s:param name="grn" value="${ica.grn.id}"/>
                            <s:param name="skuGroupField" value="${VarSkuGroup.id}"/>
                            Add New Bin!
                        </s:link>)
                        </s:form>
                    </td>
                    <%--<td width="70px">--%>
                        <%--<c:set var="entry" value="${ica.skuGroupQtyMapping}"/>--%>
                            <%--${entry[VarSkuGroup.sku.id]}--%>
                    <%--</td>--%>

                    <td width="140px">
                            ${VarSkuGroup.batchNumber}
                    </td>
                    <td width="70px">
                         <c:if test="${VarSkuGroup.barcode != null}">
                            ${VarSkuGroup.barcode}
                          </c:if>
                          <c:if test="${VarSkuGroup.barcode == null}">
                           ${barcodePrefix}${VarSkuGroup.id}
                          </c:if>
                    </td>

                    <td width="70px">
                        <c:set var="entry" value="${ica.skuGroupQtyMappingPerGrn}"/>
                            ${entry[VarSkuGroup.id]}
                    </td>

                    <td width="70px">
                        <c:set var="entry1" value="${ica.skuGroupRemainedQtyMapping}"/>
                            ${entry1[VarSkuGroup]}
                    </td>

                    </tr>
                </c:forEach>
            </c:forEach>
        </table>
        <div>
            <s:submit name="closeWindow" value="Save"/>
            <s:param name="grn" value="${ica.grn.id}"/>
        </div>
    </s:form>
</div>
