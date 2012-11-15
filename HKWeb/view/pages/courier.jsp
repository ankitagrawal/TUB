<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" event="save" var="cou"/>
	<s:layout-component name="heading">
		<div>
		<c:choose><c:when test="${cou.courier == null}">
			<h2>Add a New Courier</h2>
		</c:when>
			<c:otherwise>
				Courier # ${cou.courier.name}
			</c:otherwise>
		</c:choose>
		</div>
		<div class="clear"></div>
	</s:layout-component>

	<s:layout-component name="content">

		<div class="container_12">
			<s:form action="com.hk.web.action.admin.courier.AddCourierAction">
				<s:hidden name="courierGroup" value="${cou.courierGroup}"/>
				<div class="row">
					<s:label class="rowLabel" name="Name*"/>
					<s:text class="rowText" value = "${cou.courier.name}" name="courier.name"/>
				</div>
				<div class="clear"></div>
				<div style="margin-top:10px"></div>
				<div class="row">
					<s:label class="rowLabel" name="Status*"/>
					<s:radio name="courier.disabled"  value="false" checked="${cou.courier.disabled}"/>Active
					<s:radio name="courier.disabled" value="true" checked="${cou.courier.disabled}"/>InActive
				</div>
				<div class="clear"></div>
				<div style="margin-top:10px"></div>
				<div class="row">				
					<s:label class="rowLabel" name="Group*"/>
					<s:select id="groupDropDown" name="courierGroup" value="${cou.courier.courierGroup}">
						<s:option value="">-- No Group Assigned -- </s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="courierGroupList" value="id" label="name"/>
					</s:select>
				</div>
				<div class="clear"></div>
				<div style="margin-top:10px"></div>
				<div class="row">
				<s:submit name="save" value="Save"/>
				</div>
			</s:form>
		</div>

	</s:layout-component>


</s:layout-render>
<style type="text/css">
	.row {
		margin-top: 0;
		float: left;
		margin-left: 0;
		padding-top: 2px;
		padding-left: 26px;
	}

	.rowLabel {
		float: left;
		padding-right: 5px;
		padding-left: 5px;
		width: 150px;
		height: 24px;
		margin-top: 5px;
		font-weight: bold;
	}

	.rowText {
		float: left;
		border-width: 0;
		padding-top: 0;
		padding-bottom: 0;
		margin-left: 20px;
		font: inherit;
	}
</style>