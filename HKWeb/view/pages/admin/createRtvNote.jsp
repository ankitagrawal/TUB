<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.constants.rtv.EnumRtvNoteStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.web.HealthkartResponse" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Rtv Note">
 <s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="rtvNote"/>
    <s:layout-component name="htmlHead">
                <script type="text/javascript">

                </script>
        </s:layout-component>
    <s:layout-component name="content">
 <h2>Purchase Order # ${rtvNote.purchaseOrderId}</h2>
 <h4 style="color:blue;">Extra Inventory # ${rtvNote.extraInventory.id}</h4>

        <s:form beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction">
        <table>
            <thead>
            <tr>
                <th>Rtv Note Id</th>
                <th>Extra Inventory Id</th>
                <th>Rtv Note Status</th>
                <th>Created By</th>
                <th>Is debit to Supplier</th>
                <th>Reconciled</th>
                <th>Create Date</th>
                <th>Update Date</th>
                <th>Remarks</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${rtvNote.rtvNote!=null}">
                <tr>
                    <td> ${rtvNote.rtvNote.id}</td>
                    <td>${rtvNote.rtvNote.extraInventory.id}</td>
                    <td>
                            <s:select name="rtvStatus">
                                <s:option value="<%=EnumRtvNoteStatus.Created%>%>">Created</s:option>
                                <s:option value="<%=EnumRtvNoteStatus.Reconciled%>%>">Reconciled</s:option>
                            </s:select>
                        </td>
                    <td>${rtvNote.rtvNote.createdBy.name}</td>
                    <td>
                        <s:select name="isDebitToSupplier">
                           <s:option value="0">No</s:option>
                            <s:option value="1">Yes</s:option>
                        </s:select>
                    </td>
                    <td>
                       <s:select name="isReconciled">
                           <s:option value="0">No</s:option>
                            <s:option value="1">Yes</s:option>
                        </s:select>
                    </td>
                    <td>${rtvNote.rtvNote.createDate}</td>
                    <td>${rtvNote.rtvNote.updateDate}</td>
                    <td><textarea name = "comments" rows="10" cols="10">${rtvNote.rtvNote.remarks}</textarea></td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <s:hidden name="rtvNoteId" value="${rtvNote.rtvNote.id}"/>
        <br><br>
        <div class="clear"></div>
    <h2>Rtv Note Line Items</h2>
        <table border="1">
            <thead>
            <tr>
                <th>S.No</th>
                <th>Rtv Note Line Item ID</th>
                <th>Variant ID</th>
                <th>Product Name</th>
                <th>MRP</th>
                <th>Cost Price</th>
                <th>Received QTY</th>
                <th>TAX</th>
            </tr>
            </thead>
            <tbody id="poTable">
            <c:if test="${rtvNote.rtvNoteLineItems!=null}">
                <c:forEach items="${rtvNote.rtvNoteLineItems}" var="rtvLineItem" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${ctr.index+1}.</td>
                        <td>
                          ${rtvLineItem.id}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.sku.productVariant.id}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.productName}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.mrp}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.costPrice}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.receivedQty}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.tax.value}
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <br/>
        <s:hidden name="purchaseOrderId" value="${extraInventory.purchaseOrderId}" />
        <s:submit name="editRtvNote" value="SAVE" id="save" />
    </s:form>
    </s:layout-component>
    </s:layout-render>