<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Please provide your valuable feedback">
    <s:layout-component name="heading">Thank you for your feedback</s:layout-component>
    <s:layout-component name="left_col">
		<style type="text/css">
			.link{
                color: #0091d7;
                font-size: 20px;
                font-weight: 600;
                text-decoration: none;
			}
			.column{
				width: 400px;
				text-align: center;
			}
		</style>

		<table cellpadding="10" cellspacing="10" style=" margin: 10px 0;">
			<tr>
				<td nowrap class="column"><s:link beanclass="com.hk.web.action.HomeAction" class="link">Continue shopping with Healthkart</s:link></td>
				<%--<td nowrap class="column"><a href='${pageContext.request.contextPath}/super-savers' class="link">Checkout our latest Offers</a></td>--%>
			</tr>
		</table>

	</s:layout-component>
</s:layout-render>