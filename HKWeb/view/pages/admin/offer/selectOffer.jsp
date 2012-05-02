<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.SelectOfferAction" var="offerBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Select Offer</s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.offer.SelectOfferAction">
      <table class="cont">
        <thead>
        <tr>
          <th width="10px">  </th>
          <th width="25px">Id</th>
          <th width="200px">Description</th>
          <th width="25px">Start Date</th>
          <th width="25px">End Date</th>
          <th width="200px">Offer Action</th>
          <th width="25px">Offer Action Product Group</th>
          <th width="25px">Offer Action Min Qty</th>
          <th width="25px">Offer Action %cent discount on Hk Price</th>
          <th width="25px">Offer Action %cent discount on Marked Price</th>
          <th width="25px">Offer Action order level discount</th>
          <th width="25px">Offer Action dicount percent on shipping</th>
          <th width="25px">Offer Trigger Amount</th>
          <th width="200px">Offer Trigger Desciption</th>
          <th width="25px">Offer Trigger Product Group</th>
          <th width="25px">Offer Trigger Qty</th>
        </tr>
        </thead>
        <c:forEach items="${offerBean.offers}" var="offer" varStatus="offerCount">
          <tr>
            <td width="10px"><s:radio value="${offer.id}" name="offer"/></td>
            <td width="10px">${offer.id}</td>
            <td class="offerTextContainer" width="200px" align="center">${offer.description} </td>
            <td width="25px"><fmt:formatDate value="${offer.startDate}" type="both"/></td>
            <td width="25px"><fmt:formatDate value="${offer.endDate}" type="both"/></td>
            <td width="200px" align="center">${offer.offerAction.description}</td>
            <td width="25px">${offer.offerAction.productGroup.name}</td>
            <td width="25px">${offer.offerAction.qty}</td>
            <td width="25px">${offer.offerAction.discountPercentOnHkPrice}</td>
            <td width="25px">${offer.offerAction.discountPercentOnMarkedPrice}</td>
            <td width="25px">${offer.offerAction.orderLevelDiscountAmount}</td>
            <td width="25px">${offer.offerAction.discountPercentOnShipping}</td>
            <td width="25px">${offer.offerTrigger.amount}</td>
            <td width="200px" align="center">${offer.offerTrigger.description}</td>
            <td width="25px">${offer.offerTrigger.productGroup.name}</td>
            <td width="25px">${offer.offerTrigger.qty}</td>
          </tr>
        </c:forEach>
      </table>
      <s:submit name="selectOffer" value="Create Coupon"/>
      <s:submit name="editOffer" value="Edit Offer"/>
    </s:form>
    <p>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${offerBean}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${offerBean}"/>
    </p>

  </s:layout-component>
</s:layout-render>
