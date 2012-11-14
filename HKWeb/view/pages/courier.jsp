<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="cou"/>
	<s:layout-component name="heading">
		<c:choose><c:when test="${cou.courier == null}">
			<h2>Add a New Courier</h2>
		</c:when>
			<c:otherwise>
				<h2>Courier # ${cou.courier.name}</h2>
			</c:otherwise>
		</c:choose>
	</s:layout-component>

	<s:layout-component name="content">
		<fieldset>
			<legend>Search Courier List</legend>
			<s:form action="com.hk.web.action.admin.courier.AddCourierAction"> 			  
				<s:hidden name="courierGroup" value="${cou.courierGroup}"/>
				<div class="row">
					<s:text name="courier.name"/>
				</div>
				<div class="row">
					<s:label name="Status*">${cou.courier.disabled}</s:label>
					<s:radio name="courier.disabled" value="false" checked="${cou.courier.disabled}">Active</s:radio>
					<s:radio name="courier.disabled" value="true" checked="${cou.courier.disabled}">InActive</s:radio>
				</div>

				<div class="row">
					<s:label name="Group*"/>
					<s:select id="groupDropDown" name="courierGroup" value="${cou.courier.courierGroup}">
						<s:option value="">-- No Group Assigned -- </s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="courierGroupList" value="id" label="name"/>
					</s:select>
				</div>
				<s:submit name="save" value="Save"/>

			</s:form>
		</fieldset>


	</s:layout-component>


</s:layout-render>