<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">

<s:layout-component name="heading">
		Search and Add Courier
	</s:layout-component>

	<s:layout-component name="content">

		<div>
			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
				 <fieldset>
                <legend>Search Courier List</legend>

                <label>Courier Name:</label><s:text name="courier" style="width:150px"/>
                &nbsp; &nbsp;
                <label>Courier Group:</label>
	            &nbsp; &nbsp;
                <label>Status:</label><label>Status:</label><s:select name="sta">
			        <s:option value="">-ALL-</s:option>
			        <s:option value="false">Active</s:option>
			        <s:option value="true">Inactive</s:option>
	            </s:select>
                <s:submit name="pre" value="Search"/>                 
            </fieldset>
				</s:form>
		</div>

		</s:layout-component>



</s:layout-render>