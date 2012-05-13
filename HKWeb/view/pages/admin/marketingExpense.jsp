<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.admin.pact.dao.marketing.AdNetworksDao" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add/Edit Marketing Expense">
	<s:useActionBean beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction" var="mea"/>
	<s:layout-component name="htmlHead">
		<%
    AdNetworksDao adNetworksDao = ServiceLocatorFactory.getService(AdNetworksDao.class);
    pageContext.setAttribute("adNetworksList", adNetworksDao.listAdNetworks());
		%>
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('.requiredFieldValidator').click(function() {
					var category = $('.category').val();
					var impression = $('.impression').val();
					var click = $('.click').val();
					var amountSpend = $('.amountSpend').val();
					var conversion = $('.conversion').val();
					var costOfConversion = $('.costOfConversion').val();
					var adNetwork = $('.adNetwork').val();
					if(category=="" || impression=="" || click=="" || amountSpend=="" || conversion=="" || costOfConversion=="" || adNetwork == "" ){
						alert("All fields are compulsory.");
						return false;
					}
					if(isNaN(impression) || isNaN(click) || isNaN(amountSpend) || isNaN(conversion) || isNaN(costOfConversion)){
						alert("Impression, click, Amount Spend, Conversion and Cost of Conversion should be numbers.")
					}
				} );
				});
		</script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
	<s:layout-component name="heading">
		Add/Edit Marketing Expense
	</s:layout-component>
	<s:layout-component name="content">
		<div class="reportBox">
			<table>
				<s:form beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction">
					<s:hidden name="marketingExpense.id" value="${mea.marketingExpense.id}"/>
					<tr>
						<td>Date:<span class='aster' title="this field is required">*</span></td>
						<td><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="marketingExpense.date"/></td>
					</tr>
					<tr>
						<td>Category:<span class='aster' title="this field is required">*</span></td>
						<td><s:select name="marketingExpense.category" id="marketingExpenseCat" value="${mea.marketingExpense.category.name}" class="category">
          <s:option value="">-Select-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="marketExpenseCategoriesList" value="name" label="displayName"/>
        </s:select></td>
					</tr>
					<tr>
						<td>Impression:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="marketingExpense.impression" value="${mea.marketingExpense.impression}" class="impression"/></td>
					</tr>
					<tr>
						<td>Click:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="marketingExpense.click" value="${mea.marketingExpense.click}" class="click"/></td>
					</tr>
					<tr>
						<td>Amount Spent:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="marketingExpense.amountSpend" value="${mea.marketingExpense.amountSpend}" class="amountSpend"/></td>
					</tr>
					<tr>
						<td>Conversion:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="marketingExpense.conversion" value="${mea.marketingExpense.conversion}" class="conversion"/></td>
					</tr>
					<tr>
						<td>Cost of Conversion:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="marketingExpense.costOfConversion" value="${mea.marketingExpense.costOfConversion}" class="costOfConversion"/></td>
					</tr>
					<tr>
						<td>Ad Network:<span class='aster' title="this field is required">*</span></td>
						<td><s:select name="marketingExpense.adNetworks.id" class="adNetwork">
          <s:option value="">-Select-</s:option>
           <c:forEach items="${adNetworksList}" var="adNetworks">
	           <s:option value="${adNetworks.id}">${adNetworks.name}</s:option>
           </c:forEach>
        </s:select></td>
					</tr>
					<tr>
						<td><s:submit class="requiredFieldValidator" name="save" value="Save"/></td>
					</tr>
				</s:form>
			</table>
		</div>
	</s:layout-component>
</s:layout-render>