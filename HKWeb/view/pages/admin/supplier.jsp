<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<html>
<head>

<script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
  <script type="text/javascript">

  $(document).ready(function() {
	  
	  $("#saveButton").click(function () {
		  if(($.trim($("#contactPerson").val())==null||$.trim($("#contactPerson").val())=="")||
				  ($.trim($("#contactEmailId").val())==null||$.trim($("#contactEmailId").val())=="")){
			  alert("Please fill the mandatory fields");
			  return false;
		  }
	  });
	 
	  
  });
  </script>
</head><body>
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
					<c:choose><c:when test="${sma.supplier.id == null}">
						<td><s:text name="supplier.tinNumber"/></td>
					</c:when>
						<c:otherwise>
							<td><s:text name="supplier.tinNumber" readonly="readonly"/></td>
						</c:otherwise>
					</c:choose>
					<td>Address Line1</td>
					<td><s:text name="supplier.line1"/></td>
				</tr>
				<tr>
					<td>Supplier Name<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.name"/></td>
					<td>Address Line2</td>
					<td><s:text name="supplier.line2"/></td>
				</tr>
				<tr>
				<td>Credit Days<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.creditDays" maxlength="3"/></td>
					<td>City</td>
					<td><s:text name="supplier.city"/></td>
				</tr>
				<tr>
				<td>Target Credit Days<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.targetCreditDays" maxlength="3"/></td>
					<td>State<span class='aster' title="this field is required">*</span></td>
					<td>
						<s:select name="supplier.state">
							<s:option value="">Select</s:option>
							<s:options-collection collection="<%=StateList.stateList%>"/>
						</s:select>
					</td>
				</tr>
				<tr>
				<td>Lead Time (in Days)<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.leadTime" maxlength="3"/></td>
					<td>PIN</td>
					<td><s:text name="supplier.pincode"/></td>
				</tr>
				<tr>
					<td>Margins <br/></td>
					<td><s:text name="supplier.margins" maxlength="20"/></td>
					<td>Contact Person<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.contactPerson" id="contactPerson"/></td>
					
				</tr>
				<tr>
				<td>Damage and Expiry conditions<br/><font color="red">(Max 500 characters)</font></td>
					<td><s:text name="supplier.damageConditions" maxlength="500"/></td>
					<td>Contact Number<span class='aster' title="this field is required">*</span></td>
					<td><s:text name="supplier.contactNumber" id="contactNumber"/></td>
					
				</tr>
				<tr>
				<td>Company/Brands Name <br/><font color="red">(If more than one Brand Name, Please enter separated
					                                               by comma(,))</font> </td>
					<td><s:text name="supplier.brandName"/></td>
					<td>Email Id <span class='aster' title="this field is required">*</span><br/><font color="red">(PO Mail to supplier will be sent on this id.)</font></td>
					<td><s:text name="supplier.email_id" id="contactEmailId"/></td>
				</tr>
				<tr>
					<td>Validity of Terms of Trade</td>
					<td><s:text name="supplier.tot"/></td>
					<td>Contact Person 2</td>
					<td><s:text name="supplier.contactPerson2"/></td>
				</tr>

				<tr>
					<td>Status<span class='aster' title="this field is required">*</span> - ${sma.supplier.active}</td>
					<td><s:radio name="supplier.active" value="true" checked="${sma.supplier.active}"/>Active
						<s:radio name="supplier.active" value="false" checked="${sma.supplier.active}"/>Inactive</td>
						<td>Contact Number 2</td>
					<td><s:text name="supplier.contactNumber2"/></td>
				</tr>

				<tr>
					<td>Comments</td>
					<td rowspan="5"><s:textarea name="supplier.comments"/></td>
					<td>Email Id 2</td>
					<td><s:text name="supplier.email_id2"/></td>
				</tr>
				<tr>
				<td></td>
					<td>Contact Person 3</td>
					<td><s:text name="supplier.contactPerson3"/></td>
				</tr>
				<tr>
					<td></td>
					<td>Contact Number 3</td>
					<td><s:text name="supplier.contactNumber3"/></td>
				</tr>
				<tr>
					<td></td>
					<td>Email Id 3</td>
					<td><s:text name="supplier.email_id3"/></td>
				</tr>

			</table>
			<s:submit id="saveButton" name="save" value="Save" class="buttons"/>

		</s:form>
	</s:layout-component>

</s:layout-render>
</body>
</html>