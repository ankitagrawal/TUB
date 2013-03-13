<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page import="com.hk.domain.inventory.BrandsToAudit" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Brand To Audit">
    <s:useActionBean beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction" var="poa"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
    <s:layout-component name="heading">
        Brand To Audit
    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.inventory.BrandsToAuditAction">
            <label>Brand:</label>&nbsp;${poa.brandsToAudit.brand}<br/><br/>
            <label>Current Audit Status:</label>&nbsp;
            <c:forEach items="<%=EnumAuditStatus.getAllList()%>" var="status">
                <c:if test="${poa.brandsToAudit.auditStatus == status.id}">${status.name}</c:if>
            </c:forEach> <br/><br/>
            <c:if test="${poa.brandsToAudit.id ne null}">
                <label>Audit Status</label><s:select name="brandsToAudit.auditStatus">
                <s:option value="${poa.brandsToAudit.auditStatus}" >--Select--</s:option>
                <c:forEach items="<%=EnumAuditStatus.getPossibleStatuses(poa.getBrandsToAudit().getAuditStatus())%>" var="status">
                    <s:option value="${status.id}">${status.name}</s:option>
                </c:forEach>
            </s:select><br/>
            </c:if><br/><br/>
            <s:hidden name="brandsToAudit" value="${poa.brandsToAudit.id}"/>
            <s:submit name="save" value="Save" id="subBtn"/>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    $(document).ready(function () {
        $('#subBtn').click(function () {
            if ($('#brand-name').val().trim() === "") {
                alert("Kindly enter all the necessary details!");
                return false;
            }
            return true;
        });
    });
</script>