<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Audit List">
    <s:useActionBean beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction" var="iaaBean"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="heading">
        Sku Item Audit List
    </s:layout-component>

    <s:layout-component name="content">

        <fieldset class="right_label">
            <legend><strong>Search Audit List </strong></legend>
            <s:form beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction">
                <table>
                    <tr>
                        <td><label>Brand:</label><s:text name="brand"/></td>
                        <td><label>VariantID:</label><s:text name="variantId"/></td>
                        <td><label>Item Barcode:</label><s:text name="barcode"/></td>
                        <td><label>Warehouse: </label>
                            <c:choose>
                                <c:when test="${whAction.setWarehouse != null}">
                                    <s:hidden name="warehouse" value="${whAction.setWarehouse}"/>
                                    ${whAction.setWarehouse.identifier}
                                </c:when>
                                <c:otherwise>
                                    <s:select name="warehouse">
                                        <s:option value="0">-All-</s:option>
                                        <c:forEach items="${whList}" var="wh">
                                            <s:option value="${wh.id}">${wh.identifier}</s:option>
                                        </c:forEach>
                                    </s:select>
                                </c:otherwise>
                            </c:choose></td>
                    </tr>
                    <tr>
                        <td><label>Audit By:</label><s:text name="auditedBy"/></td>
                        <td><label>Start date</label>
                            <s:text class="date_input startDate" style="width:150px"
                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                    name="startDate"/>
                        </td>
                        <td><label>End date</label>
                            <s:text class="date_input endDate" style="width:150px"
                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                    name="endDate"/></td>
                    </tr>
                    <tr><td colspan="3" align="right"><s:submit name="auditList" value="Search"/></td></tr>
                </table>
            </s:form>
        </fieldset>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${iaaBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${iaaBean}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>Variant ID</th>
                <th width="25%">Name</th>
                <th>Brand</th>
                <th>Item Barcode</th>
                <th>Location</th>
                <th>Warehouse</th>
                <th>Audit Date</th>
                <th>Audted By</th>
            </tr>
            </thead>
            <c:forEach items="${iaaBean.skuItemAuditList}" var="skuItemAudit" varStatus="ctr">
                <c:set var="sku" value="${skuItemAudit.skuItem.skuGroup.sku}"/>
                <c:set var="variant" value="${sku.productVariant}"/>
                <c:set var="product" value="${variant.product}"/>
                <tr>
                    <td>${ctr.index+1}</td>
                    <td>${variant.id}</td>
                    <td>${product.name}<br/>
                            ${variant.optionsCommaSeparated}</td>
                    <td>${product.brand}</td>
                    <td>${skuItemAudit.skuItem.barcode}</td>
                    <td>${skuItemAudit.skuItem.bin.barcode}</td>
                    <td>${sku.warehouse.identifier}</td>
                    <td><fmt:formatDate value="${skuItemAudit.auditDate}" type="both" timeStyle="short"/></td>
                    <td>${skuItemAudit.user.login}</td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${iaaBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${iaaBean}"/>

    </s:layout-component>
</s:layout-render>
