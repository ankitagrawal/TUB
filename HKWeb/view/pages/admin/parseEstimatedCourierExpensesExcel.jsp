<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Upload Estimated Expenses">
	<s:layout-component name="content">
		<s:form beanclass="com.hk.web.action.admin.shipment.ParseEstimatedCourierExpensesExcelAction">
			<p style="color:red; font-size:20px">This is the <strong style="color:green;">ESTIMATED</strong> courier expenses
				excel upload. Please verify your excel before uploading. </p><br/><br/>

			<h2>File to Upload
				<s:file name="fileBean" size="30"/></h2>

			<div class='label'>Email</div>
			<s:text class="emailId" name="email" placeholder="Your Email"/>
			<br/>
			Enter your email address to receive the error report
			<div class="buttons">
				<s:submit class="emailValidate" name="parse" value="Update"/>
			</div>
		</s:form>
		<script type="text/javascript">
			$(document).ready(function() {
				$('.emailValidate').click(function() {
					var emailRegEx = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
					var emailId = $('.emailId').val();
					if (!emailRegEx.test(emailId)) {
						alert("Enter correct email address.");
						return false;
					}
				});
			});
		</script>
	</s:layout-component>
</s:layout-render>