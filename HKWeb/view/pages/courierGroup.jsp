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
	  <div style="margin-top:15px"></div>
      <div class="error" style="width :200px;background-color:salmon; margin-top: 20px; padding: 5px;"></div>
		 <div style="margin-top:20px"></div>
		<div>
			<s:form beanclass="com.hk.web.action.admin.courier.AddCourierAction">
				<div class="row">
					<s:label class="rowLabel" name="Name*"/>
					<s:text id="name" class="rowText" value = "${cou.courierGroup.name}" name="courierGroup.name"/>
				</div>
			<div>
			<div class="clear"></div>
			<s:submit class="submit" name="saveGroup" value="Save"/>
		</s:form>
	</s:layout-component>
</s:layout-render>
<script type="text/javascript">
	$(document).ready(function() {
		$('.error').hide();
		$('.submit').click(function() {
			var name = $('#name').val();
			$('.error').empty();
			if (name == null || name.trim() == '') {
				$('.error').append("<br/>Enter Name<br/>");
				$('.error').show();
				return false;
			}
		});


	});
	</script>