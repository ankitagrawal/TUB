<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 21, 2013
  Time: 10:44:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Order List">
    <s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="extraInventory"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        ExtraInventory List
    </s:layout-component>
    <s:layout-component name="content">
        <fieldset class="right_label">
              <legend>Search ExtraInventory</legend>
              <s:form beanclass="com.hk.web.action.admin.rtv.RTVAction">
                <label>ExtraInventory ID:</label><input type="text" name="extraInventoryId"/>
                <label>Purchase Order ID:</label><input type="text" name="purchaseOrderId"/>
                <s:submit name="searchExtraInventory" value="Search ExtraInventory"/>
              </s:form>
            </fieldset>

            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${extraInventory}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${extraInventory}"/>

        <table class="zebra_vert">
              <thead>
              <tr>
                <th>Extra Inventory ID</th>
                <th>Purchase Order ID</th>
                <th>Created By</th>
                <th>Created Date</th>
                <th>Comments</th>
              </tr>
              </thead>
            <c:forEach items="${extraInventory.extraInventories}" var="extraInventory">
            <tr>
               <td>
                  <s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" event="pre" >${extraInventory.id}
                     <s:param name="purchaseOrderId" value="${extraInventory.purchaseOrder.id}"/>
                     <s:param name="wareHouseId" value="${extraInventory.purchaseOrder.warehouse.id}"/>
                  </s:link>
               </td>
                <td>

                </td>
                <td>
                    <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" event="pre">${extraInventory.purchaseOrder.id}
                        <s:param name="purchaseOrder" value="${extraInventory.purchaseOrder.id}"/>
                    </s:link>
                </td>
                <td>
                    ${extraInventory.createdBy.name}
                </td>
                <td>
                    ${extraInventory.comments}
                </td>
            </tr>
            </c:forEach>
    </table>

    </s:layout-component>
</s:layout-render>