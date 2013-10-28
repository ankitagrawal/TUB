<%@ taglib prefix="itv" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.hk.constants.review.EnumReviewStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction" event="pre"
                 var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pending Reviews Queue">

  <s:layout-component name="content">

    <fieldset class="top_label">
      <legend>Search And Edit Product Review</legend>
      <s:form beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction" method="get"
              autocomplete="false">
        <label>Product:</label><s:text name="product"/>

        <label>Review Status:</label><s:select name="reviewStatus">
        <option value="">All Types</option>
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="reviewStatusList" value="id"
                                   label="name"/>
      </s:select>
        <s:submit name="pre" value="Search" class="buttons"/>
      </s:form>
    </fieldset>

    <s:form beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction">

      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

      <table class="cont">
        <thead>
        <tr>
          <th>Sr No.</th>
          <th>Product</th>
          <th>User</th>
          <th>Date</th>
          <th>Status</th>
          <th>User Rating</th>
          <th>Title</th>
          <th>Review</th>
          <th>Mood</th>
          <th>Edit/Save</th>

        </tr>
        </thead>
        <c:forEach items="${rpBean.productReviews}" var="review" varStatus="ctr">
        <s:hidden name="productReviews[${ctr.index}]" value="${review.id}"/>
        <tr>
          <td>${ctr.count}</td>
          <td>${review.product} - ${review.product.name}</td>
          <td>${review.postedBy.name}</td>
          <td>${review.reviewDate}</td>
          <td>
            <s:select name="productReviews[${ctr.index}].reviewStatus">
              <s:option value="<%=EnumReviewStatus.Pending.getId()%>">Pending</s:option>
              <s:option value="<%=EnumReviewStatus.Published.getId()%>">Published</s:option>
              <s:option value="<%=EnumReviewStatus.Deleted.getId()%>">Deleted</s:option>
            </s:select></td>
          <td>
              ${review.starRating}
          </td>
          <td>
              ${review.title}
          </td>
          <td>
              ${review.review}
          </td>
          <td> ${review.mood}
          </td>
          <td><s:link beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction"
                      event="saveReviews">
            <s:param name="saveReview" value="false"/>
            <s:param name="reviewId" value="${review.id}"/>
            <b>Edit & Save</b>
          </s:link></td>

          </c:forEach>
      </table>
      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

    </s:form>


  </s:layout-component>

</s:layout-render>
