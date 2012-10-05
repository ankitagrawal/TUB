<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="courierBean"/>
	<s:layout-component name="htmlHead">
		<script type="text/javascript">


			$(document).ready(function() {
				$('#courierDropDown').blur(function() {
					var courieren = $('#courierDropDown').val();
					if (courieren == "") {
						return false;
					}
					else {
						$.getJSON(
								$('#selectgroup').attr('href'), {courier:courieren}, function(response) {
							if (response.code == '<%=HealthkartResponse.STATUS_OK%>') {
								$('#groupDropDown').val(response.data.id);
							}
							else {
								$('#groupDropDown').val("");
							}

						});

					}


				});


			});
		</script>
		<style type="text/css">
			.alert {
				font: bolder;
			}
		</style>

	</s:layout-component>


	<s:layout-component name="heading">
		Search and Add Courier
	</s:layout-component>
	<s:layout-component name="content">
		<div style="display:none">
			<s:link id="selectgroup" beanclass="com.hk.web.action.admin.courier.AddCourierAction"
			        event="getCourierGroupForCourier"></s:link>

		</div>
		<div>
			Total Courier in System = ${fn:length(courierBean.courierList)}
			<fieldset class="right_label">
				<legend>add New Courier</legend>

				<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
					<ul>
						<li>
							<label>Enter New Courier Name</label>
							<s:text name="courierName"></s:text>
							&nbsp; &nbsp;  &nbsp; &nbsp;&nbsp;
							<label>Enable Courier</label>
							<s:select id="courierDropDown" name="courier">
								<s:option value="">--Select Courier --</s:option>
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="disableCourier" value="id" label="name"/>
							</s:select>
						</li>

						<s:submit name="saveCourier" value="Save/Enable courier"/>
						</li>
					</ul>
				</s:form>
			</fieldset>
		</div>

		<div class="clear"></div>

		<div>
			<fieldset class="right_label">
				<legend>assign Group Courier</legend>
				<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
					<ul>
						<div>
							<li><label>Courier </label>
								<s:select id="courierDropDown" name="courier">
									<s:option value="">--Select Courier --</s:option>
									<hk:master-data-collection service="<%=MasterDataDao.class%>"
									                           serviceProperty="availableCouriers" value="id" label="name"/>
								</s:select><s:submit name="deleteCourier" value="disable courier"/></li>
						</div>
						<div class="clear" style="height:50px;">
						</div>
						<div>
							<li><label>Courier Group </label><s:select id="groupDropDown" name="courierGroup">
								<s:option value="">-- No Courier Assigned -- </s:option>
								<hk:master-data-collection service="<%=MasterDataDao.class%>"
								                           serviceProperty="courierGroupList" value="id" label="name"/>
							</s:select>
								<%--<s:submit name="deleteCourierGroup" value="delete group"/>--%>
							</li>
						</div>
						<s:submit name="assignCourierGroup" value="assign group"/>
					</ul>

				</s:form>
			</fieldset>
		</div>


		<div>
			Total Courier Group in System = ${fn:length(courierBean.courierGroupList)}
			<fieldset class="right_label">
				<legend>add New Courier Group</legend>

				<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
					<ul>
						<li>
							<label> Courier Group</label>
							<s:text name="courierGroup.name"></s:text>
						</li>
						<s:submit name="addNewCourierGroup" value="save courier group"/>
						
					</ul>
				</s:form>
			</fieldset>
		</div>


	</s:layout-component>


</s:layout-render>