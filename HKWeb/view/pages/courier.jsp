<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.AddCourierAction" var="cou"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:layout-component name="heading">
		<div>
		<c:choose><c:when test="${cou.courier == null}">
			Add a New Courier
		</c:when>
			<c:otherwise>
				Courier # ${cou.courier.name}
			</c:otherwise>
		</c:choose>
		</div>
		<div class="clear"></div>
	</s:layout-component>

	<s:layout-component name="content">
	  <div style="margin-top:20px"></div>
      <div class="error" style="width :200px;background-color:salmon; margin-top: 20px; padding: 5px;"></div>
		<div >
			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
				<s:hidden name="courierGroup" value="${cou.courierGroup}"/>
				<s:hidden name="courier" value="${cou.courier}"/>
				<div class="row">
					<s:label class="rowLabel" name="Name*"/>
					<s:text id="name" class="rowText" value = "${cou.courier.name}" name="courierName"/>
				</div>
				<div class="clear"></div>
				<div style="margin-top:10px"></div>
				<div class="row" >
					<s:label class="rowLabel" name="Status*"/>
					<s:radio  id="statusActive" name="courier.disabled"  value="false" checked="${cou.courier.disabled}"/>Active
					<s:radio  id="statusInActive" name="courier.disabled" value="true" checked="${cou.courier.disabled}"/>InActive
				</div>
				<div class="clear"></div>
				<div style="margin-top:10px"></div>
				<div class="row">
					<s:label class="rowLabel" name="Group*"/>
					<s:select id="groupDropDown" name="courierGroup" value="${cou.courier.courierGroup}">
						<s:option value="">-- No Group Assigned -- </s:option>
						<hk:master-data-collection service="<%=MasterDataDao.class%>"
						                           serviceProperty="courierGroupList" value="id" label="name"/>
					</s:select>
				</div>
			  <div class="clear"></div>
			<s:submit class="submit" name="save" value="Save"/>

			</s:form>

		</div>
	</s:layout-component>


</s:layout-render>
<style type="text/css">
	.row {
		margin-top: 0;
		float: left;
		margin-left: 0;
		padding-top: 2px;
		padding-left: 26px;
	}

	.rowLabel {
		float: left;
		padding-right: 5px;
		padding-left: 5px;
		width: 150px;
		height: 24px;
		margin-top: 5px;
		font-weight: bold;
	}

	.rowText {
		float: left;
		border-width: 0;
		padding-top: 0;
		padding-bottom: 0;
		margin-left: 20px;
		font: inherit;
		width : 200px;
	}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$('.error').hide();
		$('.submit').click(function() {
			var name = $('#name').val();
			var status = null;
			if($('#statusActive').is(':checked')||$('#statusInActive').is(':checked'))
			{
				status = '';
			}

			var group = $('#groupDropDown').val();
			$('.error').empty();
			var err = 0;
			if (name == null || name.trim() == '') {
				$('.error').append("<br/>Enter Name<br/>");
				err = 1;
			}
			if (status == null) {
				$('.error').append("<br/>Select Status<br/>");
				err = 1;
			}
			if (err) {
				$('.error').show();
				return false;
			}
			else if (group == '')
		{
			var proceed = confirm('You are removing Group');
			if (!proceed) return false;
		}

		});


	});

</script>