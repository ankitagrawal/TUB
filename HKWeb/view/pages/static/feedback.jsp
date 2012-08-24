<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
	String recommendToFriendsFeedback = request.getParameter("recommendToFriends");
	pageContext.setAttribute("recommendToFriends", recommendToFriendsFeedback);
	String baseOrderId = request.getParameter("baseOrderId");
	pageContext.setAttribute("baseOrderId", baseOrderId);

%>
<script type="text/javascript">
	$(document).ready(function () {
		$('#recommendToFriends').val(${recommendToFriends});

	});

</script>
<s:layout-render name="/layouts/default.jsp" pageTitle="Please provide your valuable feedback">
	<s:layout-component name="left_col">
		<style type="text/css">
			.alert{
			    color: green;
				font-weight: bold;
			}
		</style>
		<div style="background: #fcfcfc; padding: 15px; ">
			<s:form beanclass="com.hk.web.action.pages.FeedbackAction">
				<div class="left">
					<h2>Please provide your valuable feedback</h2>
					<s:hidden name="order" value="${baseOrderId}" />
					<div class='label'><h3>1. How likely is it that you would recommend HealthKart to a friend or colleague?</h3></div>
					<table cellpadding="5" cellspacing="5">
						<tr>
							<td><s:radio value="1" name="recommendToFriends" /> 1</td>
							<td><s:radio value="2" name="recommendToFriends" /> 2</td>
							<td><s:radio value="3" name="recommendToFriends" /> 3</td>
							<td><s:radio value="4" name="recommendToFriends" /> 4</td>
							<td><s:radio value="5" name="recommendToFriends" /> 5</td>
							<td><s:radio value="6" name="recommendToFriends" /> 6</td>
							<td><s:radio value="7" name="recommendToFriends" /> 7</td>
							<td><s:radio value="8" name="recommendToFriends" /> 8</td>
							<td><s:radio value="9" name="recommendToFriends" /> 9</td>
							<td><s:radio value="10" name="recommendToFriends" /> 10</td>
						</tr>
					</table>

					<div class='label'><h3>2. How was your website experience?</h3></div>
					<s:radio value="1" name="websiteExperienceFeedback"/>Poor
					<s:radio value="2" name="websiteExperienceFeedback"/>Below Average
					<s:radio value="3" name="websiteExperienceFeedback"/>Satisfactory
					<s:radio value="4" name="websiteExperienceFeedback"/>Excellent
					<s:radio value="5" name="websiteExperienceFeedback"/>Above Expectations

					<div class='label'><h3>3. How was your customer care experience?</h3></div>
					<s:radio value="1" name="customerServiceFeedback"/>Poor
					<s:radio value="2" name="customerServiceFeedback"/>Below Average
					<s:radio value="3" name="customerServiceFeedback"/>Satisfactory
					<s:radio value="4" name="customerServiceFeedback"/>Excellent
					<s:radio value="5" name="customerServiceFeedback"/>Above Expectations

					<div class='label'><h3>4. Any feedback that you want to share with us?</h3></div>
					<s:textarea name="comments" rows="8" cols="50"/>
				</div>
				<s:submit name="save" />
			</s:form>
		</div>

	</s:layout-component>

</s:layout-render>


