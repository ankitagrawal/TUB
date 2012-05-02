<%@ page import="com.hk.constants.StateList" %>
<%@ page import="mhc.service.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="web.action.admin.MasterPincodeAction" var="mpaBean"/>
	<s:layout-component name="heading">
		Search and Add Pincode
	</s:layout-component>
	<s:layout-component name="content">
		<h2>Total Pincodes in System = ${mpaBean.pincodesInSystem}</h2>

    

      <fieldset class="right_label">
        <legend>Search Pincode</legend>

			<s:form beanclass="web.action.admin.MasterPincodeAction">
							<br/><br/>
				<s:text name="pincodeString" id="pincodeString" style="width:200px;"/>
				<script language=javascript type=text/javascript>
					$('#pincodeString').focus();
				</script>
				<br/>
				<br/>
				<s:submit name="search" value="Search"/>
        <s:submit name="generatePincodeExcel"    value="Download Pincode Xls"/>
			</s:form>
        </fieldset>
       <fieldset class="right_label">
    	   <legend>Upload Pincode List</legend>
         <ul>
        <s:form beanclass="web.action.admin.MasterPincodeAction">
          <br/>
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadPincodeExcel" value="Upload"/>
            <br/>
             (Worksheet Name: PincodeInfo &nbsp&nbsp&nbsp 5 Fields: PINCODE &nbspCITY &nbspSTATE &nbspLOCALITY &nbspDEFAULT_COURIER_ID)</li>
					</s:form>
			</ul>
          </fieldset>
       <fieldset style="float:left;">
         <legend>Add Pincode Or Edit Picode Details</legend>
			<table>
				<s:form beanclass="web.action.admin.MasterPincodeAction">
					<s:hidden name="pincode.id" value="${mpaBean.pincode.id}"/>
					<tr>
						<td>Pincode:</td>
						<td><s:text name="pincode.pincode" value="${mpaBean.pincode.pincode}"/></td>
					</tr>
					<tr>
						<td>Locality:</td>
						<td><s:text name="pincode.locality" value="${mpaBean.pincode.locality}"/></td>
					</tr>
					<tr>
						<td>City:</td>
						<td><s:text name="pincode.city" value="${mpaBean.pincode.city}"/></td>
					</tr>
					<tr>
						<td>State:</td>
						<td><s:select name="pincode.state" value="${mpaBean.pincode.state}">
							<s:option value="">-Select-</s:option>
							<s:options-collection collection="<%=StateList.stateList%>"/>
						</s:select></td>
					</tr>
          <tr>
						<td>Default Courier:</td>
						<td><s:select name="pincode.defaultCourier" value="${mpaBean.pincode.defaultCourier.id}">
							<s:option value="">-Select-</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>

						</s:select></td>
					</tr>
					<tr>
						<td><s:submit name="save" value="Save"/></td>
					</tr>
				</s:form>
			</table>
          </fieldset>
       <fieldset style="float:left;">
			<table align="center" class="cont"> Available Courier Services corresponding to the entered Pincode:
				<thead>
				<tr>
					<th>Courier Service</th>
					<th>Cod Available</th>
				</tr>
				</thead>
				<c:forEach items="${mpaBean.courierServiceList}" var="courierServiceList" varStatus="ctr">
					<tr>
						<td>${courierServiceList.courier.name}</td>
						<td>
							<c:if test="${courierServiceList.codAvailable}">Y</c:if>
							<c:if test="${!courierServiceList.codAvailable}">N</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
          </fieldset>


	</s:layout-component>
</s:layout-render>