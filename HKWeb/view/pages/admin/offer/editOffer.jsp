<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.payment.EnumPaymentType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.EditOfferAction" var="offerBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Select Offer</s:layout-component>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.offer.EditOfferAction">
      <table class="cont">
        <thead>
        <tr>
            <th width="25px">Id</th>
            <th width="200px">Description</th>
            <th width="25px">Start Date</th>
            <th width="25px">End Date</th>
            <th width="200px">Offer Action</th>
            <th width="25px">Offer Action Product Group</th>
            <th width="25px">Offer Action Min Qty</th>
            <th width="25px">Offer Action %cent level discount</th>
            <th width="25px">Offer Action order level discount</th>
            <th width="25px">Offer Action dicount percent on shipping</th>
            <th width="25px">Offer Trigger Amount</th>
            <th width="200px">Offer Trigger Desciption</th>
            <th width="25px">Offer Trigger Product Group</th>
            <th width="25px">Offer Trigger Qty</th>
            <th width="25px">Offer terms</th>
            <th width="25px">Payment Type</th>
        </tr>
        </thead>
          <tr>
            <td width="10px">${offerBean.offer.id}</td>
            <td class="offerTextContainer" width="50px" align="center">${offerBean.offer.description} </td>
            <td width="25px"><fmt:formatDate value="${offerBean.offer.startDate}" type="both"/></td>
            <td width="25px"><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="offer.endDate" value="${offerBean.offer.endDate}"/></td>
            <td width="50px" align="center">${offerBean.offer.offerAction.description}</td>
            <td width="25px">${offerBean.offer.offerAction.productGroup.name}</td>
            <td width="25px">${offerBean.offer.offerAction.qty}</td>
            <td width="25px">${offerBean.offer.offerAction.discountPercentOnHkPrice}</td>
            <td width="25px">${offerBean.offer.offerAction.orderLevelDiscountAmount}</td>
            <td width="25px">${offerBean.offer.offerAction.discountPercentOnShipping}</td>
            <td width="25px">${offerBean.offer.offerTrigger.amount}</td>
            <td width="200px" align="center">${offerBean.offer.offerTrigger.description}</td>
            <td width="25px">${offerBean.offer.offerTrigger.productGroup.name}</td>
            <td width="25px">${offerBean.offer.offerTrigger.qty}</td>
            <td width="50px"><s:textarea name="offer.terms"/></td>
	          <td width="25px"><s:select name="offer.paymentType">
		        <c:forEach items="<%=EnumPaymentType.getAllPaymentTypes()%>" var="pType">
			        <s:option value="${pType.id}">${pType.name}</s:option>
		        </c:forEach>
	        </s:select></td>
          </tr>
      </table>
      <s:hidden name="offer" value="${offerBean.offer}"/>
      <s:submit name="save" value="Save"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
