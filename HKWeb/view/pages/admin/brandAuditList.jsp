<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Brand Audit List">
    <s:useActionBean beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" var="poa"/>
    <%
        WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
        pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
    %>
    <s:layout-component name="heading">
        Brands Audit List
    </s:layout-component>
    <s:layout-component name="content">
        <s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
            Add New Brand To Audit
        </s:link>
        <fieldset class="right_label">
            <legend>Search Audit List</legend>
            <s:form beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">
                <label>Brand:</label><s:text name="brand"/>
                <label>Auditor Login:</label><s:text name="auditorLogin"/>
                <label>WH:</label><s:select name="warehouse">
                <s:option value="">--Choose--</s:option>
                <c:forEach items="${whList}" var="wh">
                    <s:option value="${wh.id}">${wh.name}</s:option>
                </c:forEach>
            </s:select>
                <s:submit name="pre" value="Search"/>
            </s:form>
        </fieldset>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Audit Date</th>
                <th>WH</th>
                <th>Auditor</th>
                <th>Brand</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <c:forEach items="${poa.brandsToAuditList}" var="auditBrand" varStatus="ctr">
                <tr>
                    <td><fmt:formatDate value="${auditBrand.auditDate}" pattern="dd/MM/yyyy"/></td>
                    <td>${auditBrand.warehouse.name}</td>
                    <td>${auditBrand.auditor.login}</td>
                    <td>${auditBrand.brand}</td>
                    <td>
                        <c:forEach items="<%=EnumAuditStatus.getAllList()%>" var="status">
                            <c:if test="${auditBrand.auditStatus == status.id}">
                                ${status.name}
                            </c:if>
                        </c:forEach>
                    </td>

                    <td>
                        <s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
                            <s:param name="brandsToAudit" value="${auditBrand.id}"/>
                            Edit
                        </s:link>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
    </s:layout-component>
</s:layout-render>