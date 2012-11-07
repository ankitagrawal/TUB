<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
 <s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="addcou"/>
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
                <label>Status:</label><label>Status:</label><s:select name="status">
			        <s:option value="">-ALL-</s:option>
			        <s:option value="false">Active</s:option>
			        <s:option value="true">Inactive</s:option>
	            </s:select>
                <s:submit name="pre" value="Search"/>                 
            </fieldset>
				</s:form>
		</div>

		 <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${addcou}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${addcou}"/>

		<table class="zebra_vert">
            <thead>
            <tr>
                <th>Courier Name</th>
                <th>Courier Group</th>
                <th>Active?</th>
                <th>Actions</th>
            </tr>
            </thead>
		    <c:forEach items="${addcou.courierList}" var="courierv">
                <tr>
                    <td>${courierv.name}</td>
                    <td>${courierv.courierGroup}</td>
                     <c:when test="${courierv.disabled}">
	                  InActive
                     </c:when>
	                <c:otherwise>
		               Active
	                </c:otherwise>

                    <td>
                        <s:link beanclass="com.hk.web.action.admin.courier.AddCourierAction" event="createOrEditCourier">Edit
                            <s:param name="courier" value="${courierv.id}"/></s:link>
	                    <c:if test="${supplier.active}">
		                    <br/>
		                    <s:link beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction">Create PO
			                    <s:param name="supplier" value="${supplier.id}"/></s:link>
		                    <br/>
		                    <s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view">
			                    Raise Debit Note
			                    <s:param name="supplier" value="${supplier.id}"/></s:link>
	                    </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${sma}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${sma}"/>
		</s:layout-component>



</s:layout-render>