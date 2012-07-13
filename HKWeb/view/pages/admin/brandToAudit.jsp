<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
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
			<s:hidden name="brandsToAudit.id"/>
			<label>Brand: </label><s:text name="brandsToAudit.brand"/><br/>
			<label>Audit Date: </label><s:text class="date_input" formatPattern="yyyy-MM-dd" name="brandsToAudit.auditDate"/><br/>
			<label>Audit Status</label><s:select name="brandsToAudit.auditStatus">
		        <c:forEach items="<%=EnumAuditStatus.getAllList()%>" var="status">
			        <s:option value="${status.id}">${status.name}</s:option>
		        </c:forEach>
	        </s:select><br/>
			<s:submit name="save" value="Save"/>
		</s:form>
	</s:layout-component>
</s:layout-render>