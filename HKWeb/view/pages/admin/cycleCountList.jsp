<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.inventory.EnumAuditStatus" %>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.inventory.EnumCycleCountStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Cycle Count  List">
	<s:useActionBean beanclass="com.hk.web.action.admin.inventory.CycleCountAction" var="cc"/>

	<s:layout-component name="heading">
		Cycle Count  List
	</s:layout-component>

	<s:layout-component name="content">
		<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction" event="createCycleCount">
			Create New Cycle Count
		</s:link>
		<fieldset class="right_label">
			<legend>Search Cycle Count List</legend>
			<s:form beanclass="com.hk.web.action.admin.inventory.CycleCountAction">
				<label>Brand/Product/ProductVariant:</label><s:text name="auditBy"/>
				<label>Auditor Login Email:</label><s:text name="auditorLogin"/>
				<label>Status:</label>
				<s:select name="cycleCountStatus">
                                <s:option value="">--Select--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allCycleCountStatus"
                                                           value="id" label="name"/>
                            </s:select>
				<s:submit name="pre" value="Search"/>
			</s:form>
		</fieldset>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cc}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cc}"/>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Count Id</th>
				<th>CC Start Date</th>
				<th>WH</th>
				<th>Auditor</th>
				<th>Brand/Product/ProductVariant</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${cc.cycleCountList}" var="cycleCountV" varStatus="ctr">
				<c:set value="" var="auditOn"/>
				<c:if test="${cycleCountV.brand != null}">
				 <c:set value="${cycleCountV.brand}" var="auditOn"/>
				</c:if>
				<c:if test="${cycleCountV.product != null}">
				 <c:set value="${cycleCountV.product.id}" var="auditOn"/>
				</c:if>

				<c:if test="${cycleCountV.productVariant != null}">
				  <c:set value="${cycleCountV.productVariant.id}" var="auditOn"/>
				</c:if>

				<tr>
					<td>${cycleCountV.id}</td>
					<td><fmt:formatDate value="${cycleCountV.createDate}" pattern="dd/MM/yyyy"/></td>
					<td>${cycleCountV.warehouse.city}</td>
					<td>${cycleCountV.user.login}</td>
					<td>${auditOn}</td>
					<td>
						<c:forEach items="<%=EnumCycleCountStatus.getAllList()%>" var="status">
							<c:if test="${cycleCountV.cycleStatus == status.id}">
								${status.name}
							</c:if>
						</c:forEach>
					</td>
					<td>

						<c:set value="<%=EnumCycleCountStatus.InProgress.getId()%>" var="inProgress"/>
						<c:choose>
							<c:when test="${cycleCountV.cycleStatus == inProgress}">
                                <!-- seema - change event= directToCycleCountPage to event= view --!>
								<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
								        event="view">
									<s:param name="cycleCount" value="${cycleCountV.id}"/>
									<span style="color:brown;">Edit Cycle Count</span>
								</s:link>
							</c:when>
							<c:otherwise>
								<s:link beanclass="com.hk.web.action.admin.inventory.CycleCountAction"
								        event="save">
									<s:param name="cycleCount" value="${cycleCountV.id}"/>
									<span style="color:brown;">View Cycle Count</span>
								</s:link>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cc}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cc}"/>
	</s:layout-component>
</s:layout-render>