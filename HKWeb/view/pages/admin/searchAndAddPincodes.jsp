<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" var="mpaBean"/>
	<s:layout-component name="heading">
		Search and Add Pincode
	</s:layout-component>
	<s:layout-component name="content">
		<h2>Total Pincodes in System = ${mpaBean.pincodesInSystem}</h2>



      <fieldset class="right_label">
        <legend>Search Pincode</legend>

			<s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
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
        <s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
          <br/>
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadPincodeExcel" value="Upload"/>
            <br/>
             (Worksheet Name: PincodeInfo &nbsp&nbsp&nbsp 6 Fields: PINCODE &nbspCITY &nbspSTATE &nbspREGION &nbspLOCALITY &nbspDEFAULT_COURIER_ID &nbspZONE)</li>
					</s:form>
			</ul>
          </fieldset>
		<div style="display:inline-block;">
       <fieldset style="float:left;">
         <legend>Add Pincode Or Edit Picode Details</legend>
			<table>
				<s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
					<s:hidden name="pincode.id" value="${mpaBean.pincode.id}"/>
					<tr>
						<td>Pincode:</td>
						<td><s:text name="pincode.pincode" value="${mpaBean.pincode.pincode}"/></td>
					</tr>
                    <tr>
						<td>Region:</td>
						<td><s:text name="pincode.region" value="${mpaBean.pincode.region}"/></td>
					</tr>
					<tr>
						<td>Locality:</td>
						<td><s:text name="pincode.locality" value="${mpaBean.pincode.locality}"/></td>
					</tr>
					<tr>
						<td>City:</td>
						<%--<td><s:text name="pincode.city.id" value="${mpaBean.pincode.city.id}"/></td>--%>
                        <td><s:select name="pincode.city.id" value="${mpaBean.pincode.city.id}">
							<s:option value="">-Select-</s:option>
						 <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="cityList" value="id" label="name"/>
						</s:select></td>

					</tr>
					<tr>
						<td>State:</td>
						<td><s:select name="pincode.state.id" value="${mpaBean.pincode.state.id}">
							<s:option value="">-Select-</s:option>
						 <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="stateList" value="id" label="name"/>
						</s:select></td>
					</tr>
          <tr>
						<td>Default Courier:</td>
						<td><s:select name="pincode.defaultCourier" value="${mpaBean.pincode.defaultCourier.id}">
							<s:option value="">-Select-</s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id" label="name"/>

						</s:select></td>
					</tr>
					<tr>
						<td>
							<label>Zone:</label>
								</td>
						<td>
								<s:select name="pincode.zone">
								<s:option value="null">Select</s:option>
									<hk:master-data-collection service="<%=MasterDataDao.class%>"
									                           serviceProperty="allZones"
									                           value="id"
									                           label="name"/>
								</s:select>
						</td>
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
		</div>
		<div>
			<fieldset style="display:inline-block;width:250px; padding:7px;">
			<legend>Click Link To Add PRZ</legend>
			 <span style="font:bold;color:darkolivegreen;"><s:link
					 beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" event="directToPincodeRegionZone">
				 Add Pincode Region Zone
			 </s:link></span>
			</fieldset>
		</div>
	</s:layout-component>
</s:layout-render>