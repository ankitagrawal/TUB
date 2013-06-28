<%@page import="com.hk.constants.sku.EnumSkuGroupStatus"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction" var="sbr"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Sku Group Review">
    <s:layout-component name="htmlHead">
    </s:layout-component>

    <s:layout-component name="heading">SKU Group Review</s:layout-component>
    <s:layout-component name="content">
        <div style="display:inline;float:left;">


            <table border="1">
                <thead>
                <tr>
                    <th> SKU Group</th>
                    <th>Variant</th>
                    <th>WH</th>
                    <th>Batch No.</th>
                    <th>Mfg. Date</th>
                    <th>Expiry Date</th>
                    <th> Sku</th>
                    <th>Cost Price</th>
                    <th>MRP</th>
                    <th>Checked in units</th>
                    <th>Net Inv</th>
                    <th> Status</th>
                    <th> Action</th>

                </tr>
                </thead>
                <tbody id="stTable">
                <c:forEach var="skuGroup" items="${sbr.skuGroups}" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${skuGroup.id}</td>
                        <td> ${skuGroup.sku.productVariant} </td>
                        <td> ${skuGroup.sku.warehouse.name} </td>
                        <td>${skuGroup.batchNumber} </td>
                        <td><fmt:formatDate value="${skuGroup.mfgDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${skuGroup.expiryDate}" pattern="dd/MM/yyyy"/></td>
                        <td>${skuGroup.sku}</td>
                        <td> ${skuGroup.costPrice} </td>
                        <td>${skuGroup.mrp}</td>
                        <td>${fn:length(skuGroup.skuItems)}</td>
                        <td>${fn:length(hk:getInStockSkuItems(skuGroup))}</td>
                        <td> ${skuGroup.status}</td>
                       <td> 
                       <c:if test="${sbr.lineItem.markedPrice == skuGroup.mrp && skuGroup.status != EnumSkuGroupStatus.UNDER_REVIEW}">
	                       <s:link
	                                beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction"
	                                event="markSkuGroupAsUnderReview">
	                            Mark Review
	                            <s:param name="searchSkuGroup" value="${skuGroup.id}"/>
	                           <s:param name="lineItem" value="${sbr.lineItem}"/>
	                        </s:link>  
                       </c:if>
                        </td>
                    </tr>

                </c:forEach>
                </tbody>
            </table>

        </div>

    </s:layout-component>
</s:layout-render>