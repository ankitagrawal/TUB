<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:layout-component name="htmlHead">
	<script type="text/javascript">
		$('document').ready(function(){
	 $('#courierDropDown').selected(function(){
	     val courier =  $('#courierDropDown').val();
		if(courier == ""){
			return ;
		}
		else{
			$(getJson
		}


	 });


		});
   </script>
	</s:layout-component>

	<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="courierBean"/>
	<s:layout-component name="heading">
		Search and Add Courier
	</s:layout-component>
	<s:layout-component name="content">
		<div>
		<h2>Total Courier in System = ${courierBean.courierList}</h2>
		<fieldset class="right_label">
			<legend>add New Courier </legend>

			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
				<ul>
					<li>
					<label> Courier Name</label>
					<s:text name="courier"></s:text>
					</li>
					<s:submit name="saveCourier" value="save courier"/>
					</li>
				</ul>
			</s:form>
		</fieldset>
		</div>

		<div class="clear"></div>

		<div>
		<fieldset class="right_label">
			<legend>assign Group Courier </legend>
		<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
			<ul>
		 <li><label>Courier </label>
			 <s:select  id="courierDropDown" name="courier">
            <s:option value="">--Select Courier --</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
          </s:select></li>

			 <li><label>Courier Group </label><s:select id="groupDropDown" name="courierGroup">
            <s:option value="">-- No Courier Assigned -- </s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierGroupList" value="id" label="name"/>
          </s:select></li>			
		 </ul>
			<s:submit name="assignCourierGroup" value="assign group"/>
			</s:form>
		</fieldset>
	 </div>


	 <div>
		<h2>Total Courier Group in System = ${courierBean.courierGroupList}</h2>
		<fieldset class="right_label">
			<legend>add New Courier Group </legend>

			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
				<ul>
					<li>
					<label> Courier Group</label>
					<s:text name="courier"></s:text>
					</li>
					<s:submit name="addNewCourierGroup" value="save courier group"/>
					</li>
				</ul>
			</s:form>
		</fieldset>
		</div>


	</s:layout-component>


</s:layout-render>