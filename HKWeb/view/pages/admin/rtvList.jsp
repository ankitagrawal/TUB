<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Dec 27, 2012
  Time: 9:21:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.constants.rtv.EnumRtvNoteStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="RTV Note List">
    <s:useActionBean beanclass="com.hk.web.action.admin.rtv.RTVAction" var="rtv"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        RTV Note List
    </s:layout-component>
    <s:layout-component name="content">
        <fieldset class="right_label">
              <legend>Search RTV</legend>
              <s:form beanclass="com.hk.web.action.admin.rtv.RTVAction">
                <label>RTV ID:</label><input type="text" name="rtvNoteId"/>
                <label>Extra Inventory ID:</label><input type="text" name="extraInventoryId"/>
                <label>Status:</label>
                  <select name="enumRtvNoteStatus">
                      <option selected="selected" value="">---ALL---</option>
                      <option value="<%=EnumRtvNoteStatus.Created%>"><%=EnumRtvNoteStatus.Created.getName()%></option>
                      <option value="<%=EnumRtvNoteStatus.SentToSupplier%>"><%=EnumRtvNoteStatus.SentToSupplier.getName()%></option>
                      <option value="<%=EnumRtvNoteStatus.Reconciled%>"><%=EnumRtvNoteStatus.Reconciled.getName()%></option>
                  <select>
                <s:submit name="pre" value="Search RTV"/>
              </s:form>
            </fieldset>

            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rtv}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rtv}"/>

        <table class="zebra_vert">
              <thead>
              <tr>
                <th>ID</th>
                <th>Extra Inventory ID</th>
                <th>Rtv Note Status</th>
                <th>Created By</th>
                <th>is Debit To Supplier</th>
                <th>Create Date</th>
                <th>Update Date</th>
                <th>Remarks</th>
                <th>Action</th>
              </tr>
              </thead>
            <c:forEach items="${rtv.rtvNotes}" var="rtvNote">
            <tr>
               <td>
                   ${rtvNote.id}
               </td>
                <td>
                  <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" event="pre" >${rtvNote.extraInventory.id}
                  <s:param name="purchaseOrderId" value="${rtvNote.extraInventory.purchaseOrder.id}"/>
                   <s:param name="wareHouseId" value="${rtvNote.extraInventory.purchaseOrder.warehouse.id}"/>
                  </s:link>
                </td>
                <td>
                    ${rtvNote.rtvNoteStatus.name}
                </td>
                <td>
                    ${rtvNote.createdBy.name}
                </td>
                <td>
                    ${rtvNote.debitToSupplier}
                </td>
                <td>
                    ${rtvNote.createDate}
                </td>
                <td>
                    ${rtvNote.updateDate}
                </td>
                <td>
                    ${rtvNote.remarks}
                </td>
                <td>
                    <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" event="editRtvNoteLineItems" >Edit RTV Note
                    <s:param name="rtvNoteId" value="${rtvNote.id}"/>
                     <s:param name="extraInventoryId" value="${rtvNote.extraInventory.id}"/>
                      <s:param name="purchaseOrderId" value="${rtvNote.extraInventory.purchaseOrder.id}"/>
                    </s:link>
                </td>
            </tr>
            </c:forEach>
    </table>

    </s:layout-component>
</s:layout-render>