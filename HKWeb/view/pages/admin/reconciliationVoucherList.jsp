<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumReconciliationType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="poa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reconciliation Voucher List">
    <%
        WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
        pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
        Long addId = EnumReconciliationType.Add.getId();
        Long productAuditedId = EnumReconciliationType.ProductVariantAudited.getId();
        Long debitNoteId  = EnumReconciliationType.RVForDebitNote.getId();
        pageContext.setAttribute("addId", addId);
        pageContext.setAttribute("productAuditedId", productAuditedId);
        pageContext.setAttribute("debitNoteId", debitNoteId);
    %>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="content">
        <c:if test="${whAction.setWarehouse != null}">
            <div>
                <div style="float:left;">
                    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                            event="subtractReconciliation">Subtract/Delete Reconciliation Voucher</s:link>
                    <br/><br/> <br/>
                    <s:link
                            beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                            event="createProductAuditedForSingleBatchPage"><span
                            style="color:#0000ff;font-weight: bold; ">Subtract RV Variant For Single Batch</span></s:link>
                </div>

                <div style="float:right;">
                    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                            event="addReconciliation">Add Reconciliation Voucher</s:link>
                </div>

            </div>
        </c:if>
        <div class="clear"></div>
        <div>
            <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">

                <fieldset>
                    <legend>Search Reconciliation Voucher List</legend>

                    <s:errors/>
                    <br/>
                    <label>Create Date:</label><s:text class="date_input" formatPattern="yyyy-MM-dd" name="createDate"/>
                    <label>User Login</label><s:text style="width:150px" name="userLogin"/>
                    <label>Warehouse: </label>
                    <c:choose>
                        <c:when test="${whAction.setWarehouse != null}">
                            <s:hidden name="warehouse" value="${whAction.setWarehouse}"/>
                            ${whAction.setWarehouse.city}
                        </c:when>
                        <c:otherwise>
                            <s:select name="warehouse">
                                <s:option value="0">-All-</s:option>
                                <c:forEach items="${whList}" var="wh">
                                    <s:option value="${wh.id}">${wh.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </c:otherwise>
                    </c:choose>
                    <s:submit name="pre" value="Search"/>
                </fieldset>
            </s:form>

            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Create Date</th>
                    <th>Created By</th>
                    <th>Reconciliation Date</th>
                    <th>Reconciliation Type</th>
                    <th>Actions</th>

                </tr>
                </thead>
                <c:forEach items="${poa.reconciliationVouchers}" var="reconvoucher" varStatus="ctr">
                    <tr>
                        <td>${reconvoucher.id}</td>
                        <td><fmt:formatDate value="${reconvoucher.createDate}" type="both" timeStyle="short"/></td>
                        <td>${reconvoucher.createdBy.name} <br/>(${reconvoucher.createdBy.login})</td>
                        <td><fmt:formatDate value="${reconvoucher.reconciliationDate}" type="both"
                                            timeStyle="short"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${reconvoucher.reconciliationType.id == addId}">
                                    Add
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${reconvoucher.reconciliationType.id == productAuditedId }">
                                            Product Audit
                                        </c:when>
                                        <c:otherwise>
                                        <c:choose>
                                        <c:when test="${reconvoucher.reconciliationType.id == debitNoteId}">
                                        Subtract/Rv For Debit Note
                                        </c:when>
                                        <c:otherwise>
                                         Subtract
                                        </c:otherwise>
                                        </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>

                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${reconvoucher.reconciliationType.id == addId}">
                                    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                                            event="addReconciliation">Edit
                                        <s:param name="reconciliationVoucher" value="${reconvoucher.id}"/></s:link>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${reconvoucher.reconciliationType.id == productAuditedId }">
                                            <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                                                    event="directToShowProductAuditedInventoryPage">View
                                                <s:param name="reconciliationVoucher"
                                                         value="${reconvoucher.id}"/></s:link>
                                        </c:when>
                                        <c:otherwise>
                                            <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                                                    event="create">Edit
                                                <s:param name="reconciliationVoucher"
                                                         value="${reconvoucher.id}"/></s:link>
                                        </c:otherwise>
                                    </c:choose>

                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

            <fieldset style="float:right;text-align:center;height:60px;">
                <legend>RV Variant For Any Batch</legend>
                <br/>
                <s:link class="button_orange" style="float:center;height:40px"
                        beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                        event="createProductAuditedForAnyBatchPage">Rv By Any Batch </s:link>
            </fieldset>
        </div>
    </s:layout-component>
</s:layout-render>