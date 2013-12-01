<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.admin.pact.dao.marketing.AdNetworksDao" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%
    AdNetworksDao adNetworksDao = ServiceLocatorFactory.getService(AdNetworksDao.class);
    pageContext.setAttribute("adNetworksList", adNetworksDao.listAdNetworks());
		%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Order List">
  <s:useActionBean beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction" var="mea"/>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	  <script type="text/javascript">
		  $(document).ready(function() {
				$('.verifyDate').click(function() {
					var startDate =  $('.startDate').val();
					var endDate = $('.endDate').val();
					if(startDate>endDate){
						alert("End Date cannot be less than Start Date.") ;
						return false;
					}
				} );
				});
	  </script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">
    Marketing Expense List
  </s:layout-component>

  <s:layout-component name="content">
	  <s:form beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction">
		  <s:submit name="addMarketingExpense" value="Add New Marketing Expense"/>
	  </s:form>

    <fieldset class="right_label">
      <legend>Search Marketing Expense</legend>
      <s:form beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction">
        <label>Category:</label><s:select name="category">
          <s:option value="">-All-</s:option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="marketExpenseCategoriesList" value="name" label="displayName"/>
        </s:select>
			<label>Ad Networks:</label><s:select name="adNetworksId">
          <s:option value="">-Select-</s:option>
           <c:forEach items="${adNetworksList}" var="adNetworks">
	           <s:option value="${adNetworks.id}">${adNetworks.name}</s:option>
           </c:forEach>
        </s:select>
        <label>Start Date:</label><s:text class="date_input startDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
        <label>End Date:</label><s:text class="date_input endDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
        <s:submit name="pre" value="Search Marketing Expense" class="verifyDate"/>
      </s:form>
    </fieldset>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${mea}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${mea}"/>

    <table class="zebra_vert">
      <thead>
      <tr>
        <th>ID</th>
        <th>Date</th>
        <th>Category</th>
        <th>Impression</th>
        <th>Click</th>
        <th>Amount Spent</th>
        <th>Conversion</th>
        <th>Cost of Conversion</th>
        <th>Ad Network</th>
        <th>Actions</th>
      </tr>
      </thead>
      <c:forEach items="${mea.marketingExpenseList}" var="marketingExpense" varStatus="ctr">
        <tr>
          <td>${marketingExpense.id}</td>
          <td><fmt:formatDate value="${marketingExpense.date}" pattern="dd-MM-yyyy"/></td>
          <td>${marketingExpense.category.displayName}</td>
          <td>${marketingExpense.impression}</td>
          <td>${marketingExpense.click}</td>
          <td>${marketingExpense.amountSpend}</td>
          <td>${marketingExpense.conversion}</td>
          <td>${marketingExpense.costOfConversion}</td>
          <td>${marketingExpense.adNetworks.name}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction" event="view">Edit
              <s:param name="marketingExpenseId" value="${marketingExpense.id}"/></s:link>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${mea}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${mea}"/>

  </s:layout-component>
</s:layout-render>