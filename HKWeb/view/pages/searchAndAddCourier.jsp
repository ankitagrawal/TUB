<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="cou"/>
	<s:layout-component name="heading">
		Search and Add Courier
	</s:layout-component>
	 <s:layout-component name="htmlHead">
		 <script type="text/javascript">
	$(document).ready(function() {
		$('#couriername').autocomplete({
			url:"${pageContext.request.contextPath}/admin/courier/AddCourier.action?populateCourier="
		});

	});
</script>
   </s:layout-component>
	<s:layout-component name="content">
		<div style="overflow:hidden;">
		<div style="float:left;">
		<s:link event="editCourier" beanclass="com.hk.web.action.admin.courier.AddCourierAction">
			Create New Courier
		</s:link>
		</div>
		<div style="float:right;">
		<s:link event="editCourierGroup" beanclass="com.hk.web.action.admin.courier.AddCourierAction">
			Create New Group
		</s:link>
		</div>
		</div>
		<div>
			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">

				<fieldset>
					<legend>Search Courier List</legend>

					<label>Courier Name:</label><s:text id="couriername" name="courierName" style="width:150px"  autocomplete="off"/>
					&nbsp; &nbsp;
					<label>Courier Group:</label>
					<s:select id="groupDropDown" name="courierGroup">
						<s:option value="">-- No Group Assigned -- </s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="courierGroupList" value="id" label="name"/>
					</s:select>
					&nbsp; &nbsp;
					<label>Status:</label><s:select name="status">
					<s:option value="">-ALL-</s:option>
					<s:option value="true">Active</s:option>
					<s:option value="false">Inactive</s:option>
				</s:select>
					&nbsp; &nbsp;
					<label>Courier Operations:</label>
					<s:select  name="operationBitset">
						<s:option value="">-- All -- </s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="allCourierOperations" value="id" label="name"/>
					</s:select>
					&nbsp; &nbsp;
					<s:submit id="submit" name="pre" value="Search"/>
				</fieldset>
			</s:form>
		</div>

		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cou}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cou}"/>

		<table class="zebra_vert">
			<thead>
			<tr>
				<th>Courier Name</th>
				<th>Courier Group</th>
				<th>Active?</th>
				<th>Actions</th>
			</tr>
			</thead>
			<c:forEach items="${cou.courierList}" var="courierv">
				<tr>
					<td>${courierv.name}</td>
					<td>
						<c:choose>
							<c:when test="${courierv != null}">
								${courierv.courierGroup.name}
							</c:when>
							<c:otherwise/>
						</c:choose>
					</td>
					<td>
					<c:choose>
					<c:when test="${courierv.active}">
                        Active
					</c:when>
					<c:otherwise>
                        InActive
					</c:otherwise>
					</c:choose>
					</td>
					<td>
						<s:link beanclass="com.hk.web.action.admin.courier.AddCourierAction" event="editCourier">Edit
							<s:param name="courier" value="${courierv.id}"/></s:link>
					</td>
				</tr>
			</c:forEach>
		</table>
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${cou}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${cou}"/>
	</s:layout-component>
</s:layout-render>
