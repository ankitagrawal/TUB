<%@ taglib prefix="itv" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.review.EnumReviewStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction" var="rpBean"/>
<c:set var="review" value="${rpBean.review}"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Product Review">

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.catalog.product.PendingProductReviewAction">
      <table class="cont" width="100%">
        <thead>
        <tr>
          <th>Product</th>
          <th>User</th>
          <th>Date</th>
          <th>Status</th>
          <th>Title</th>
          <th>Review</th>
          <th>Mood</th>
        </tr>
        </thead>
        <s:hidden name="review" value="${review.id}"/>
        <s:hidden name="review.starRating" value="${review.starRating}"/>
        <c:choose>
          <c:when test="${review != null}">
            <tr>
              <td>${review.product}</td>
              <td>${review.postedBy.name}</td>
              <td>${review.reviewDate}</td>
              <td>
                <s:select name="review.reviewStatus" value="${review.reviewStatus}">
                  <s:option value="<%=EnumReviewStatus.Pending.getId()%>">Pending</s:option>
                  <s:option value="<%=EnumReviewStatus.Published.getId()%>">Published</s:option>
                  <s:option value="<%=EnumReviewStatus.Deleted.getId()%>">Deleted</s:option>
                </s:select></td>
              <td>
                <s:text name="review.title" class="title" value="${review.title}" maxlength="45" style="width:480px"/>
              </td>
              <td>
                <s:textarea name="review.review" class="review" cols="52"
                            style="resize:none;word-wrap:break-word" value="${review.review}"/>
              </td>
              <td>
                <s:text name="review.mood" value="${review.mood}" maxlength="45"/></td>
            </tr>
          </c:when>
        </c:choose>

      </table>
      <s:submit name="saveReviews" value="Save" style="width:200px">
        Save Review
        <s:param name="saveReview" value="true"/>
      </s:submit>
    </s:form>
  </s:layout-component>
</s:layout-render>

