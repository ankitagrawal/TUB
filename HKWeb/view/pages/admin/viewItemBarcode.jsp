<%@ page import="com.hk.constants.sku.EnumSkuItemStatus" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page import="com.hk.constants.inventory.EnumStockTransferStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="stockTransferOutId" value="<%=EnumSkuItemStatus.Stock_Transfer_Out.getId()%>"/>
<c:set var="cycleCountStatusId" value="<%=EnumCycleCountStatus.RequestForApproval.getId()%>"/>
<c:set var="stockTransferOutInProcess" value="<%=EnumStockTransferStatus.Stock_Transfer_Out_In_Process.getId()%>"/>

<%
    UserService userService = ServiceLocatorFactory.getService(UserService.class);
    pageContext.setAttribute("warehouse", userService.getWarehouseForLoggedInUser());
%>

<s:useActionBean beanclass="com.hk.web.action.admin.sku.ViewSkuItemAction" var="viewItem"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="View SkuItems">
    <s:layout-component name="htmlHead">
     <script type="text/javascript">
      $(document).ready(function() {
          $('.revertLink').click(function(){
             $('.revertLink').hide();           

          });

      });
      </script>
    </s:layout-component>

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
                    <th>Status</th>
                    <th>Warehouse</th>
                    <c:if test="${viewItem.stockTransferLineItem != null}">
                        <th> Revert</th>
                    </c:if>
                </tr>
                </thead>
                <tbody id="stTable">
                <c:forEach var="skuItem" items="${viewItem.skuItemList}" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${skuItem.id}</td>
                        <td> ${skuItem.barcode} </td>
                        <td>${skuItem.skuGroup.id} </td>
                        <td> ${skuItem.skuGroup.barcode}</td>
                        <td> ${skuItem.skuItemStatus.name} </td>
                        <td> ${skuItem.skuGroup.sku.warehouse.identifier}</td>
                        <td>
                            <c:if test="${viewItem.stockTransferLineItem != null && skuItem.skuItemStatus.id == stockTransferOutId && viewItem.stockTransferLineItem.checkedOutSkuGroup.sku.warehouse.id == warehouse.id && viewItem.stockTransferLineItem.stockTransfer.stockTransferStatus.id == stockTransferOutInProcess}">
                                <s:link class="revertLink" beanclass="com.hk.web.action.admin.inventory.StockTransferAction"
                                        event="revertStockTransferOut">Revert it
                                    <s:param name="stliToBeReduced" value="${viewItem.stockTransferLineItem}"/>
                                    <s:param name="stockTransfer"
                                             value="${viewItem.stockTransferLineItem.stockTransfer}"/>
                                    <s:param name="identifiedSkuItemToRevert" value="${skuItem.id}"/> </s:link>
                            </c:if></td>
                        <%--<td>--%>
                            <%--<c:if test="${viewItem.cycleCount != null && viewItem.cycleCount.cycleStatus == cycleCountStatusId}">--%>
                                <%--<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"--%>
                                        <%--event="deleteScannedSkuItem">Delete--%>
                                    <%--<s:param name="cycleCount" value="${viewItem.cycleCount}"/>--%>
                                    <%--<s:param name="skuItem" value="${skuItem.id}"/>--%>
                                <%--</s:link>--%>
                            <%--</c:if></td>--%>
                    </tr>

                </c:forEach>
                </tbody>
            </table>


      <span style="display:inline;float:right;">
          <c:choose>
          <c:when test="${viewItem.rvLineItem != null}">
          <h2>
              <s:link
                      beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                      event="SubtractReconciled">&lang;&lang;&lang;
              Back to Reconcilliation List
              <s:param name="reconciliationVoucher" value="${viewItem.rvLineItem.reconciliationVoucher.id}"/>
              </s:link></h2></span>
            </c:when>
            <c:when test="${viewItem.stockTransferLineItem != null}">
                <h2>
                    <s:link
                            beanclass="com.hk.web.action.admin.inventory.StockTransferAction"
                            event="stockTransferLineItem">&lang;&lang;&lang;
                    Back to Stock transfer List
                    <s:param name="stockTransfer" value="${viewItem.stockTransferLineItem.stockTransfer.id}"/>
                    </s:link></h2></span>
            </c:when>
            <c:when test="${viewItem.cycleCount!= null}">
                <h2>
                    <s:link
                            beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
                            event="save">&lang;&lang;&lang;
                    Back to Cycle Count List
                    <s:param name="cycleCount" value="${viewItem.cycleCount}"/>
                    </s:link></h2></span>
            </c:when>

            </c:choose>
        </div>

    </s:layout-component>
</s:layout-render>