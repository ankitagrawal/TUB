<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Please provide your valuable feedback">
	<s:layout-component name="left_col">
		<style type="text/css">
			.link{
				border-bottom: 1px solid #EEEEFF;
				    color: #222288;
				    font-size: 20px;
					font-weight: bold;
				    text-decoration: none;
			}
			.column{
				width: 400px;
				text-align: right;
			}
		</style>
		<br><br><br><br>
		<h1 align="center">Thank you for your feedback.</h1>
		<br><br><br><br>

		<table cellpadding="10" cellspacing="10">
			<tr>
				<td nowrap class="column"><s:link beanclass="com.hk.web.action.HomeAction" class="link">Continue shopping with Healthkart</s:link></td>
				<td nowrap class="column"><a href='${pageContext.request.contextPath}/super-savers' class="link">Checkout our latest Offers</a></td>
			</tr>
		</table>

	</s:layout-component>
</s:layout-render>