<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" var="sma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add/Edit Supplier">

	<s:layout-component name="content">
		<c:choose><c:when test="${sma.supplier == null}">
			<h2>Add a New Supplier</h2>
		</c:when>
			<c:otherwise>
				<h2>Supplier# ${sma.supplier.name}</h2>
			</c:otherwise>
		</c:choose>

		<s:form beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction">
			<s:hidden name="supplier.id" value="${sma.supplier.id}"/>
			<table>
				<tr>
					<td>TIN<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.tinNumber"/></td>
				</tr>
				<tr>
					<td>Supplier Name<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.name"/></td>
				</tr>
				<tr>
					<td>Address Line1</td>
					<td><s:text name="supplier.line1"/></td>
				</tr>
				<tr>
					<td>Address Line2</td>
					<td><s:text name="supplier.line2"/></td>
				</tr>
				<tr>
					<td>City</td>
					<td><s:text name="supplier.city"/></td>
				</tr>
				<tr>
					<td>State<span class='aster' title="this field is required">*</span></td>
					<td>
						<s:select name="supplier.state">
							<s:option value="">Select</s:option>
							<s:options-collection collection="<%=StateList.stateList%>"/>
						</s:select>
					</td>
				</tr>
				<tr>
					<td>PIN</td>
					<td><s:text name="supplier.pincode"/></td>
				</tr>
				<tr>
					<td>Contact Person</td>
					<td><s:text name="supplier.contactPerson"/></td>
				</tr>
				<tr>
					<td>Contact Number</td>
					<td><s:text name="supplier.contactNumber"/></td>
				</tr>
				<tr>
					<td>Email Id</td>
					<td><s:text name="supplier.email_id"/></td>
				</tr>
				<tr>
					<td>Credit Days</td>
					<td><s:text name="supplier.creditDays" maxlength="3"/></td>
				</tr>
				<tr>
					<td>Target Credit Days</td>
					<td><s:text name="supplier.targetCreditDays" maxlength="3"/></td>
				</tr>
				<tr>
					<td>Lead Time (in Days)</td>
					<td><s:text name="supplier.leadTime" maxlength="3"/></td>
				</tr>
				<tr>
					<td>Margins in Percentage<br/><font color="red">(Please Enter in numbers like(70 or 85.57 or 0.56),
					                                                please don't enter the % sign)</font></td>
					<td><s:text name="supplier.margins" maxlength="20"/></td>
				</tr>
				<tr>
					<td>Damage and Expiry conditions<br/><font color="red">(Max 500 characters)</font></td>
					<td><s:text name="supplier.damageConditions" maxlength="500"/></td>
				</tr>
				<tr>
					<td>Company/Brands Name <br/><font color="red">(If more than one Brand Name, Please enter separated
					                                               by comma(,))</font> </td>
					<td><s:text name="supplier.brandName"/></td>
				</tr>

				<tr>
					<td>Validity of Terms of Trade</td>
					<td><s:text name="supplier.tot"/></td>
				</tr>

				<tr>
					<td>Status - ${sma.supplier.active}</td>
					<td><s:radio name="supplier.active" value="true" checked="${sma.supplier.active}"/>Active
						<s:radio name="supplier.active" value="false" checked="${sma.supplier.active}"/>Inactive</td>
				</tr>
			</table>
			<s:submit name="save" value="Save" class="buttons"/>

		</s:form>
	</s:layout-component>

</s:layout-render>
