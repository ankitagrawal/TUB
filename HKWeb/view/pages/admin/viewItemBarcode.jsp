 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.sku.ViewSkuItemAction" var="viewItem"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="View SkuItems">
    <s:layout-component name="htmlHead"/>

    <s:layout-component name="heading">View Sku Items</s:layout-component>
    <s:layout-component name="content">
        <div style="display:inline;float:left;">


                <table border="1">
                    <thead>
                    <tr>
                        <th>SKU ITEM ID</th>
                        <th>Item Barcode</th>
                        <th>SKU Group</th>
                        <th> Group Barcode</th>
                        <th>Status </th>
                    </tr>
                    </thead>
                    <tbody id="stTable">
                    <c:forEach var="skuItem" items="${viewItem.skuItemList}" varStatus="ctr">
                        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <td>${skuItem.id}</td>
                            <td> ${skuItem.barcode}  </td>
                            <td>${skuItem.skuGroup.id} </td>
                            <td> ${skuItem.skuGroup.barcode}</td>
                            <td> ${skuItem.skuItemStatus.name} </td>

                        </tr>

                    </c:forEach>
                    </tbody>
                </table>


      <%--<span style="display:inline;float:right;"><h2><s:link--%>
              <%--beanclass="com.hk.web.action.admin.inventory.StockTransferAction">&lang;&lang;&lang;--%>
          <%--Back to Stock Transfer List</s:link></h2></span>--%>
        </div>

    </s:layout-component>
</s:layout-render>