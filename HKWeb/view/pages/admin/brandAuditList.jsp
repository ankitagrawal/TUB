<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Brand Audit List">
    <s:useActionBean beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" var="poa"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
    <%
        WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
    %>
    <s:layout-component name="heading">
        Brands Audit List
    </s:layout-component>

    <s:layout-component name="content">
        <a href="#" id="button">Add Brand to Audit</a><br/>
        <fieldset id = "add">
            <legend>Add Brand To Audit</legend>
            <s:form beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">
                <label>Brand*: </label><s:text id="brand-name" name="brandsToAudit.brand"/>
                <label>Audit Date*: </label><s:text id="audit-date" class="date_input" formatPattern="yyyy-MM-dd"
                                                    name="brandsToAudit.auditDate"/>&nbsp
                <s:hidden name="brandsToAudit" value="${poa.brandsToAudit.id}"/>
                <s:submit name="save" value="Save" id="subBtn"/>
            </s:form>
        </fieldset>

        <fieldset class="right_label">
            <legend>Search Audit List</legend>
            <s:form beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">
                <label>Brand:</label><s:text name="brand"/>
                <label>Auditor Login:</label><s:text name="auditorLogin"/>
                <label>WH:</label><s:select name="warehouse">
                <s:option value="">--Choose--</s:option>
                <c:forEach items="${whList}" var="wh">
                    <s:option value="${wh.id}">${wh.identifier}</s:option>
                </c:forEach>
            </s:select>
                <label>Audit Status</label><s:select name="auditStatus">
               <%-- <s:option value="<%=EnumAuditStatus.Pending.getId()%>" >--Select--</s:option>--%>
                <s:option value="">---Select----</s:option>
                <c:forEach items="<%=EnumAuditStatus.getAllList()%>" var="status">
                    <s:option value="${status.id}">${status.name}</s:option>
                </c:forEach>
            </s:select>
                <s:submit name="pre" value="Search"/>
            </s:form>
        </fieldset>
        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Create Date</th>
                <th>Update Date</th>
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
                    <td><fmt:formatDate value="${auditBrand.updateDate}" pattern="dd/MM/yyyy"/></td>
                    <td>${auditBrand.warehouse.identifier}</td>
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
                        <c:choose>
                            <c:when test="${auditBrand.cycleCount != null}">
                                <c:set value="<%=EnumCycleCountStatus.InProgress.getId()%>" var="inProgress"/>
                                <c:choose>
                                    <c:when test="${auditBrand.cycleCount.cycleStatus == inProgress}">
                                        <s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
                                            <s:param name="brandsToAudit" value="${auditBrand.id}"/>
                                            Edit Audit Status
                                        </s:link>
                                        <s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
                                                event="directToCycleCountPage">
                                            <s:param name="cycleCount.brandsToAudit" value="${auditBrand.id}"/>
                                            <s:param name="cycleCount" value="${auditBrand.cycleCount.id}"/>
                                            <span style="color:brown;">Edit Cycle Count</span>
                                        </s:link>
                                    </c:when>
                                    <c:otherwise>
                                        <s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
                                                event="save">
                                            <s:param name="cycleCount" value="${auditBrand.cycleCount.id}"/>
                                            <span style="color:brown;">View Cycle Count</span>
                                        </s:link>
                                    </c:otherwise>

                                </c:choose>
                            </c:when>

                            <c:otherwise>
                                <c:set value="<%= EnumAuditStatus.Pending.getId() %>" var="pending"/>
                                <c:if test="${auditBrand.auditStatus == pending}">
                                    <s:link beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" event="view">
                                        <s:param name="brandsToAudit" value="${auditBrand.id}"/>
                                        Edit Audit Status
                                    </s:link>
                                    <s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
                                            event="directToCycleCountPage">
                                        <s:param name="cycleCount.brandsToAudit" value="${auditBrand.id}"/>
                                        <span style="color:brown;">Start Cycle Count</span>
                                    </s:link>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${poa}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${poa}"/>
    </s:layout-component>

</s:layout-render>

<script type="text/javascript">
    $(document).ready(function(){
        $('#add').hide();
        if ( $('div.errorContainer').is(':visible')){
            $('#add').show();
        }
        $("#button").click(function(){
            $('#button').hide();
            $('#add').show(500);
        });
        $('#subBtn').click(function () {
            if ($('#brand-name').val().trim() === "") {
                alert("Kindly enter all the necessary details!");
                return false;
            }
            return true;
        });
    });
</script>