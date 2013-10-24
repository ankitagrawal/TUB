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
<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Please provide your valuable feedback">
    <s:layout-component name="heading">Feedback</s:layout-component>
    <s:layout-component name="left_col">
		<style type="text/css">
			.alert{
				color: green;
				font-weight: bold;
			}
			.recommend{
				width: 75px;
				font-weight: normal;
			}
			.recommendRow1{
				width: 75px;
				font-weight: normal;
				word-break: keep-all;
			}
			.website{
				width: 150px;
				font-weight: normal;
			}
			.tableClass{
				border: solid #ccc;
				border-width: 1px;
			}
		</style>
		<div>
			<s:form beanclass="com.hk.web.action.pages.FeedbackAction">
				<div class="left">
					<h2 class="fnt-bold">Please provide your valuable feedback</h2>
					<s:hidden name="order" value="${baseOrderId}" />
					<div class='label' style="padding: 5px 0;"><h3 class="fnt-bold">1. How likely is it that you would recommend HealthKart to a friend or colleague? </h3></div>
					<table class="tableClass">
						<tr>
							<td colspan="13">&nbsp;</td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="recommendRow1" colspan="10">&nbsp;Not likely</td>
							<td class="recommendRow1" >Very likely</td>
							<td width="10"></td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="recommend"> &nbsp;&nbsp;0</td>
							<td class="recommend"> &nbsp;&nbsp;1</td>
							<td class="recommend"> &nbsp;&nbsp;2</td>
							<td class="recommend"> &nbsp;&nbsp;3</td>
							<td class="recommend"> &nbsp;&nbsp;4</td>
							<td class="recommend"> &nbsp;&nbsp;5</td>
							<td class="recommend"> &nbsp;&nbsp;6</td>
							<td class="recommend"> &nbsp;&nbsp;7</td>
							<td class="recommend"> &nbsp;&nbsp;8</td>
							<td class="recommend"> &nbsp;&nbsp;9</td>
							<td class="recommend"> &nbsp;10</td>
							<td width="10"></td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="recommend"><s:radio value="1" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="1" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="2" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="3" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="4" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="5" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="6" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="7" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="8" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="9" name="recommendToFriends" /></td>
							<td class="recommend"><s:radio value="10" name="recommendToFriends" /></td>
							<td width="10"></td>
						</tr>
						<tr>
							<td colspan="13">&nbsp;</td>
						</tr>

					</table>

					<div class='label' style="padding: 5px 0;"><h3 class="fnt-bold">2. How was your website experience?</h3></div>
					<table class="tableClass">
						<tr>
							<td colspan="7">&nbsp;</td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="website" nowrap="nowrap"> 1. Poor</td>
							<td class="website" nowrap="nowrap"> 2. Below Average</td>
							<td class="website" nowrap="nowrap"> 3. Satisfactory</td>
							<td class="website" nowrap="nowrap"> 4. Excellent</td>
							<td class="website" nowrap="nowrap"> 5. Above Expectations</td>
							<td width="10"></td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="website" align="center"> <s:radio value="1" name="websiteExperienceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="2" name="websiteExperienceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="3" name="websiteExperienceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="4" name="websiteExperienceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="5" name="websiteExperienceFeedback"/></td>
							<td width="10"></td>
						</tr>
						<tr>
							<td colspan="7">&nbsp;</td>
						</tr>
					</table>

					<div class='label' style="padding: 5px 0;"><h3 class="fnt-bold">3. How was your customer care experience?</h3></div>
					<table class="tableClass">
						<tr>
							<td colspan="7">&nbsp;</td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="website" nowrap="nowrap"> 1. Poor</td>
							<td class="website" nowrap="nowrap"> 2. Below Average</td>
							<td class="website" nowrap="nowrap"> 3. Satisfactory</td>
							<td class="website" nowrap="nowrap"> 4. Excellent</td>
							<td class="website" nowrap="nowrap"> 5. Above Expectations</td>
							<td width="10"></td>
						</tr>
						<tr>
							<td width="10"></td>
							<td class="website" align="center"> <s:radio value="1" name="customerServiceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="2" name="customerServiceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="3" name="customerServiceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="4" name="customerServiceFeedback"/></td>
							<td class="website" align="center"> <s:radio value="5" name="customerServiceFeedback"/></td>
							<td width="10"></td>
						</tr>
						<tr>
							<td colspan="7">&nbsp;</td>
						</tr>
					</table>

					<div class='label' style="padding: 5px 0;"><h3 class="fnt-bold">4. Any feedback that you want to share with us?</h3></div>
					<s:textarea name="comments" rows="8" cols="50"/>
				</div>
				<s:submit name="save" value="Submit" class="btn btn-blue"/>
			</s:form>
		</div>

	</s:layout-component>

</s:layout-render>


