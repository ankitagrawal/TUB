<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
	//String feedback = (String)request.getAttribute("feedback");
	//String feedback = (String)pageContext.getAttribute("feedback");
	String feedback = (String)request.getParameter("feedback");
	pageContext.setAttribute("feedback", feedback);

%>

<s:layout-render name="/layouts/default.jsp" pageTitle="Please provide your valuable feedback">

	<s:layout-component name="left_col">
		<div style="background: #fcfcfc; padding: 15px; ">
			<s:form beanclass="com.hk.web.action.pages.FeedbackAction">
				<div class="left">
					<h2>Please provide your valuable feedback</h2>
					<script type="text/javascript">alert('hello ' + ${feedback});</script>
					<div class='label'><h3>1. How was your website experience?</h3></div>
					<s:radio value="1" name="websiteExperienceFeedback"/>Poor
					<s:radio value="2" name="websiteExperienceFeedback"/>Below Average
					<s:radio value="3" name="websiteExperienceFeedback"/>Satisfactory
					<s:radio value="4" name="websiteExperienceFeedback"/>Excellent
					<s:radio value="5" name="websiteExperienceFeedback"/>Above Expectations
					<div class='label'><h3>2. How was your website experience?</h3></div>
					<s:radio value="1" name="customerServiceFeedback"/>Poor
					<s:radio value="2" name="customerServiceFeedback"/>Below Average
					<s:radio value="3" name="customerServiceFeedback"/>Satisfactory
					<s:radio value="4" name="customerServiceFeedback"/>Excellent
					<s:radio value="5" name="customerServiceFeedback"/>Above Expectations
					<div class='label'><h3>3. Any feedback that you want to share with us?</h3></div>
					<s:textarea name="comments" rows="8" cols="50"/>
				</div>
				<s:submit name="save" />
			</s:form>
		</div>

	</s:layout-component>

</s:layout-render>


