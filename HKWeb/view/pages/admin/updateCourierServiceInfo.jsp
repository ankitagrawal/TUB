<%--
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.CourierServiceInfoAction" var="csiaBean"/>
	<s:layout-component name="content">
		<fieldset class="right_label">
			<legend>Download Courier Service Info Excel</legend>
			<ul>
				<s:form beanclass="com.hk.web.action.admin.courier.CourierServiceInfoAction">
					<li><label>Courier Service Info to download : </label><s:select name="courier" class="codModeSelect">
						<s:option value="">-All-</s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
						                           label="name"/>
					</s:select></li>
					<li>

						<div class="buttons">
							<s:submit name="generateCourierServiceInfoExcel" value="Download"/>
						</div>

					</li>
				</s:form>
			</ul>
		</fieldset>

		<fieldset class="right_label">
			<legend>Upload Courier Service Info Excel</legend>
			<ul>

				<s:form beanclass="com.hk.web.action.admin.courier.CourierServiceInfoAction">
				<div class="grid_4">
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>
						<div class="buttons">
							<s:submit name="uploadCourierServiceInfoExcel" value="Upload"/>
						</div>
					</li>
					</s:form>
			</ul>
		</fieldset>
		<fieldset class="right_label">
			<legend>Update Courier Service</legend>
			<s:form beanclass="com.hk.web.action.admin.courier.CourierServiceInfoAction">
				<table>
					<tr>
						<td>Pincode:</td>
						<td><s:text name="pincode"
						            value="${csiaBean.courierServiceInfo.pincode.pincode}"/></td>
					</tr>
					<tr>
						<td>Courier:</td>
						<td><s:select name="courierServiceInfo.courier" value="${csiaBean.courierServiceInfo.courier.id}"
						              class="applicableCourier">
							<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
							                           label="name"/>
						</s:select></td>
					</tr>
					<tr>
						<td>Cod Available</td>
						<td>
							<s:select name="courierServiceInfo.codAvailable" value="">
								<s:option value="0">N</s:option>
								<s:option value="1">Y</s:option>
							</s:select>
						</td>
					</tr>
                    <tr>
						<td>Ground Shipping Available</td>
						<td>
							<s:select name="courierServiceInfo.groundShippingAvailable" value="">
								<s:option value="0">N</s:option>
								<s:option value="1">Y</s:option>
							</s:select>
						</td>
					</tr>
                    <tr>
						<td>Cod Available On Ground Shipping</td>
						<td>
							<s:select name="courierServiceInfo.codAvailableOnGroundShipping" value="">
								<s:option value="0">N</s:option>
								<s:option value="1">Y</s:option>
							</s:select>
						</td>
					</tr>
				</table>
				<s:submit name="save" value="Save"></s:submit>
			</s:form>
		</fieldset>

	</s:layout-component>

</s:layout-render>
--%>
