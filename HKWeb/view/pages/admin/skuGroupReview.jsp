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
                    <th>Cost Price</th>
                </tr>
                </thead>
                <tbody id="stTable">
                <c:forEach var="skuGroup" items="${sbr.skuGroups}" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${skuGroup.id}</td>
                        <td> ${skuGroup.sku.productVariant} </td>
                        <td> ${skuGroup.sku.warehouse.name} </td>
                        <td>${skuGroup.batchNumber} </td>
                        <td> ${skuGroup.expiryDate}</td>
                        <td> ${skuGroup.costPrice} </td>
                        <c:if test="${skuGroup ==  sbr.searchSkuGroup }">
                            <td>
                                <s:link
                                        beanclass="com.hk.web.action.admin.sku.SkuBatchesReviewAction"
                                       event="markSkuGroupAsUnderReview">&lang;&lang;&lang;
                              Mark Review
                                <s:param name="searchSkuGroup" value="${sbr.searchSkuGroup }"/>
                                </s:link> </td>
                        </c:if>

                    </tr>

                </c:forEach>
                </tbody>
            </table>

        </div>

    </s:layout-component>
</s:layout-render>